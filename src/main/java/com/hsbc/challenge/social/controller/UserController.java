package com.hsbc.challenge.social.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.challenge.social.message.model.Message;
import com.hsbc.challenge.social.user.model.User;
import com.hsbc.challenge.social.user.repository.UserRepository;
import com.hsbc.challenge.social.user.service.AddRemove;
import com.hsbc.challenge.social.user.service.UserService;

@RestController
@RequestMapping("/social")
public class UserController {
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired UserService userService;
	@Autowired UserRepository userRepository;
	
	// /social/user
	// /social/user?type=ids
	// /social/user?type=names
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers(@RequestParam(value="type", required=false) String type){ 
		Set<User> usersObjects = userService.getAllUsers();
		Set<?> users = usersObjects;
		if(type != null && ( type.equals("names") || type.equals("ids") ))
			users = usersObjects.stream().parallel().map( usr -> type.equals("names")? usr.getName(): usr.getId() )
				.collect( Collectors.toSet() );
		
		logger.info(users != null ? users.toString(): "NO Users FOUND!!!");
		
		return new ResponseEntity<Set<?>>(users, users != null && users.size() > 0?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}
	
	// /social/user/id/1
	@RequestMapping(value = "/user/id/{user_id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserMessages(@PathVariable("user_id") Long userId){ 
		Set<Message> messages = userService.getMessages(userId);
		logger.info(messages != null  && ! messages.isEmpty() ? messages.toString(): "NO Messages FOUND!!!");
		
		return new ResponseEntity<Set<Message>>(messages, messages != null && ! messages.isEmpty()?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}
	
	// /social/user/User Name
	@RequestMapping(value = "/user/{user_name}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserMessages(@PathVariable("user_name") String userName){ 
		Set<Message> messages = userService.getMessages(userName);
		logger.info(messages != null  && ! messages.isEmpty() ? messages.toString(): "NO Messages FOUND!!!");
		
		return new ResponseEntity<Set<Message>>(messages, messages != null && ! messages.isEmpty()?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}

	// /social/user/all/id/1
	@RequestMapping(value = "/user/all/id/{user_id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserAllMessages(@PathVariable("user_id") Long userId){ 
		Set<Message> messages = userService.getAllMessages(userId);
		logger.info(messages != null  && ! messages.isEmpty() ? messages.toString(): "NO Messages FOUND!!!");
		
		return new ResponseEntity<Set<Message>>(messages, messages != null && ! messages.isEmpty()?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}

	// /social/user/all/User Name
	@RequestMapping(value = "/user/all/{user_name}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserAllMessages(@PathVariable("user_name") String userName){ 
		Set<Message> messages = userService.getAllMessages(userName);
		logger.info(messages != null  && ! messages.isEmpty() ? messages.toString(): "NO Messages FOUND!!!");
		
		return new ResponseEntity<Set<Message>>(messages, messages != null && ! messages.isEmpty()?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}

	// /social/message/'User name'?message='User Name Message'
	@RequestMapping(value = "/message/{user_name}", method = RequestMethod.PUT)
	public ResponseEntity<?> createMessageParam( @PathVariable("user_name") String userName, 
			@RequestParam(value="message", required=true) String messageText){
		Message message = userService.createMessage(userName, messageText);
		logger.info(message != null ? message.toString(): "NO Messages CREATED!!!");
		
		return new ResponseEntity<Message>(message, message != null?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}

	// /social/message/User Name/User Name message
	@RequestMapping(value = "/message/{user_name}/{message}", method = RequestMethod.PUT)
	public ResponseEntity<?> createMessage(@PathVariable("user_name") String userName, 
			@PathVariable("message") String messageText){
		Message message = userService.createMessage(userName, messageText);
		logger.info(message != null ? message.toString(): "NO Messages CREATED!!!");
		
		return new ResponseEntity<Message>(message, message != null?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}

	// /social/message/User Name/User Display Name/User Name message
	@RequestMapping(value = "/message/{user_name}/{display_name}/{message}", method = RequestMethod.PUT)
	public ResponseEntity<?> createMessage(@PathVariable("user_name") String userName, @PathVariable("display_name") String displayName,
			@PathVariable("message") String messageText){
		Message message = userService.createMessage(userName, messageText);
		logger.info(message != null ? message.toString(): "NO Messages CREATED!!!");
		
		return new ResponseEntity<Message>(message, message != null?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}	


	// /social/message/User Name
	/*
	 BODY:
      	[{ "message": "User Name Message 1" },{ "message": "User Name Message 2" },
      	{ "message": "User Name Message 3" } ]
	 */
	@RequestMapping(value = "/message/{user_name}", method = RequestMethod.POST)
	public ResponseEntity<?> createMessage(@PathVariable("user_name") String userName, 
			@RequestBody Message[] messages){
		Set<Message> messagesSet = new TreeSet<>();
		Stream.of(messages).forEach( msg -> {
			Message message = userService.createMessage( userName, msg.getMessage() );
			if(message != null)
				messagesSet.add(message);
		});
		
		logger.info(messagesSet != null  && ! messagesSet.isEmpty() ? messagesSet.toString(): "NO Messages CREATED!!!");
		
		return new ResponseEntity<Set<Message>>(messagesSet, messagesSet != null  && ! messagesSet.isEmpty() ?  HttpStatus.OK: HttpStatus.NOT_FOUND);
	}

	// /social/user/follow/1/2
	// /social/user/follow/1/2?flag=ADD
	// /social/user/follow/1/2?flag=REMOVE
	@RequestMapping(value = "/user/follow/{user_id}/{follow_id}", method = RequestMethod.PUT)
	public ResponseEntity<?> addFollow(@PathVariable("user_id") Long userId, 
			@PathVariable("follow_id") Long followId, @RequestParam(value="flag", required=false) AddRemove flag){ 
		boolean added = (flag != null && flag == AddRemove.REMOVE)? userService.follow(userId, followId, AddRemove.REMOVE)
				:userService.follow(userId, followId); // Short add, when flag not presented or flag presented and different than REMOVE
		logger.info( added ? "Successfully added followId = " + followId: "Follow NOT ADDED!!!");
		
		return new ResponseEntity<Boolean>( added, added?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}

	// /social/user/follow/1?follow=Follow Name
	// /social/user/follow/1?follow=Follow Name&flag=ADD
	// /social/user/follow/1?follow=Follow Name&flag=REMOVE
	@RequestMapping(value = "/user/follow/{user_id}", method = RequestMethod.PUT)
	public ResponseEntity<?> addFollow(@PathVariable("user_id") Long userId, 
			@RequestParam(value="follow", required=true) String followName, @RequestParam(value="flag", required=false) AddRemove flag){ 
		boolean added = (flag != null && flag == AddRemove.REMOVE)? userService.follow(userId, followName, AddRemove.REMOVE)
				:userService.follow(userId, followName);
		logger.info( added ? "Successfully added follow Name = " + followName: "Follow NOT ADDED!!!");
		
		return new ResponseEntity<Boolean>( added, added?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}

	// /social/user/follow?userName=User Name&follow=Follow Name
	// /social/user/follow?userName=User Name&follow=Follow Name&flag=ADD
	// /social/user/follow?userName=User Name&follow=Follow Name&flag=REMOVE
	@RequestMapping(value = "/user/follow", method = RequestMethod.PUT)
	public ResponseEntity<?> addFollow(@RequestParam(value="userName", required=true) String userName, 
			@RequestParam(value="follow", required=true) String followName, @RequestParam(value="flag", required=false) AddRemove flag){ 
		boolean added = (flag != null && flag == AddRemove.REMOVE)? userService.follow(userName, followName, AddRemove.REMOVE)
				:userService.follow(userName, followName);
		logger.info( added ? "Successfully added follow Name = " + followName: "Follow NOT ADDED!!!");
		
		return new ResponseEntity<Boolean>( added, added?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}
	
	// /social/user/follow/1	
	// /social/user/follow/1?flag=ADD	
	// /social/user/follow/1?flag=REMOVE
	/*
	 BODY: [1, 2, 3, 4]
	 */
	@RequestMapping(value = "/user/follow/{user_id}", method = RequestMethod.POST)
	public ResponseEntity<?> addFollows(@PathVariable("user_id") Long userId, 
					@RequestBody Long[] ids, @RequestParam(value="flag", required=false) AddRemove flag){ 
		Set<User> added =  (flag != null && flag == AddRemove.REMOVE)? userService.follows(userId, ids, AddRemove.REMOVE)
				:userService.follows(userId, ids);
		logger.info( added != null &&  ! added.isEmpty()? "Successfully added = " + added: "Follows NOT ADDED!!!");
		
		return new ResponseEntity<Set<User>>( added, 
				added != null &&  ! added.isEmpty()?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}

	// /social/user/follow?userName=User Name
	// /social/user/follow?userName=User Name&flag=ADD
	// /social/user/follow?userName=User Name&flag=REMOVE
	/*
	 BODY: ["Name 1", "Name 2", "Name 3", "Name 4"]
	 */
	@RequestMapping(value = "/user/follow", method = RequestMethod.POST)
	public ResponseEntity<?> addFollows(@RequestParam(value="userName", required=true) String userName, 
			@RequestBody String[] names, @RequestParam(value="flag", required=false) AddRemove flag){ 
		Set<User> added = (flag != null && flag == AddRemove.REMOVE)? userService.follows(userName, names, AddRemove.REMOVE)
				:userService.follows(userName, names);
		logger.info( added != null &&  ! added.isEmpty()? "Successfully added = " + added: "Follows NOT ADDED!!!");
		
		return new ResponseEntity<Set<User>>( added, 
				added != null &&  ! added.isEmpty()?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}

	// /social/user/follow/User Name
	// /social/user/follow/User Name?type=ids
	// /social/user/follow/User Name?type=names
	@RequestMapping(value = "/user/follow/{user_name}", method = RequestMethod.GET)
	public ResponseEntity<?> getFollows(@PathVariable("user_name") String user_name, 
			@RequestParam(value="type", required=false) String type){ 		
		User user = userRepository.findByName(user_name);
		Set<User> followsUsers = user != null? user.getFollows(): new HashSet<>();
		Set<?> follows = followsUsers;
		if( type == null || 
				(type != null && ( type.equals("names") || type.equals("ids") )) )
			follows = followsUsers.stream().parallel()
				.map( usr -> type == null || type.equals("ids")? usr.getId(): usr.getName() ) //If type not presented, default one is ids
				.collect( Collectors.toSet() );
		
		return new ResponseEntity<Set<?>>( follows, follows != null?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}

	// /social/user/follow/id/1
	// /social/user/follow/id/1?type=ids
	// /social/user/follow/id/1?type=names
	@RequestMapping(value = "/user/follow/id/{user_id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFollows(@PathVariable("user_id") Long userId, 
			@RequestParam(value="type", required=false) String type){ 		
		Optional<User> user = userRepository.findById(userId);
		Set<User> followsUsers = user.isPresent()? user.get().getFollows(): new HashSet<>();
		Set<?> follows = followsUsers;
		if( type == null || 
				(type != null && ( type.equals("names") || type.equals("ids") )) )
			follows = followsUsers.stream().parallel()
				.map( usr -> type == null || type.equals("ids")? usr.getId(): usr.getName() ) //If type not presented, default one is ids
				.collect( Collectors.toSet() );
		
		return new ResponseEntity<Set<?>>( follows, follows != null?  HttpStatus.OK: HttpStatus.NOT_FOUND );
	}

}

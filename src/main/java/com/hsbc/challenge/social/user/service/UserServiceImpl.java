package com.hsbc.challenge.social.user.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsbc.challenge.social.message.model.Message;
import com.hsbc.challenge.social.message.repository.MessageRepository;
import com.hsbc.challenge.social.user.model.User;
import com.hsbc.challenge.social.user.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired private UserRepository userRepository;
	@Autowired private MessageRepository messageRepository;

	@Override
	public Set<User> getAllUsers() {
		return new HashSet<>( userRepository.findAll() );
	}

	@Override
	public SortedSet<Message> getMessages(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		return user.isPresent() ? user.get().getMessages(): new TreeSet<Message>();
	}

	@Override
	public SortedSet<Message> getMessages(String name) {
		User user = userRepository.findByName(name);
		return user != null? user.getMessages(): new TreeSet<Message>();
	}

	@Override
	public SortedSet<Message> getAllMessages(Long userId) {
		SortedSet<Message> allMessages = new TreeSet<Message>();
		
		Optional<User> user = userRepository.findById(userId);
		if( user.isPresent() )
			user.get().getFollows().parallelStream().forEach( usr -> 
				allMessages.addAll(usr.getMessages()) );
		
		return allMessages;
	}

	@Override
	public SortedSet<Message> getAllMessages(String name) {
		SortedSet<Message> allMessages = new TreeSet<Message>();
		
		User user = userRepository.findByName(name);
		if(user != null && (! user.getFollows().isEmpty()) )
			user.getFollows().parallelStream().forEach( usr -> 
				allMessages.addAll(usr.getMessages()) );
		
		return allMessages;
	}

	@Override
	public User createUser(String name) {
		User user = userRepository.findByName(name);
		if(user!= null)
			return user;
		return userRepository.save( new User(name) );
	}

	@Override
	public User createUser(String name, String displayName) {
		return userRepository.save( new User(name, displayName) );
	}

	@Override
	public Message createMessage(String userName, String message) {		
		return createMessage(userName, userName, message);
	}

	@Override
	public Message createMessage(String userName, String displayName, String message) {
		User user = userRepository.findByName(userName);		
		if( user == null )
			user = createUser(userName, displayName);
		
		Message msg = messageRepository.save( new Message( message, user ));
		user.addMessage(msg);
//		userRepository.save(user);
		
		return msg;
	}

	@Override
	public Message createMessage(Long userId, String message) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			Message msg = messageRepository.save( new Message( message, user.get() ));
			user.get().addMessage(msg);
//			userRepository.save(user.get());
			
			return msg;
		}
		
		return null;
	}

	@Override
	public boolean follow(Long userId, Long followId) {
		return follow(userId, followId, AddRemove.ADD);
	}

	@Override
	public boolean follow(Long userId, String followName) {
		return follow(userId, followName, AddRemove.ADD);
	}

	@Override
	public boolean follow(String name, String followName) {
//		User user = userRepository.findByName(name);
//		User follow = userRepository.findByName(followName);
//		if(user != null && follow != null) 
//			// Existing follower follows existing follow already is following by Name
//			if( ! user.getFollows().contains(follow)) {
//				boolean result = user.getFollows().add( follow );
//				userRepository.save( user );
//				return result;
//			}
//		
//		return false;
		return follow(name, followName, AddRemove.ADD);
	}

	@Override
	public boolean follow(Long userId, Long followId, AddRemove flag) {
//		Optional<User> user = userRepository.findById(userId);
//		Optional<User> follow = userRepository.findById(followId);
//		if(user.isPresent() && follow.isPresent()) 
//			if( ! (user.get().equals( follow.get() ) // No self followed
//					|| (user.get().getFollows().contains(follow.get()) && flag == AddRemove.ADD ) ) ){ // Not containing already followed
//				boolean result = false; 
//				if( flag == AddRemove.ADD)
//					result = user.get().getFollows().add( follow.get() );
//				else
//					result = user.get().getFollows().remove( follow.get() );
//				userRepository.save( user.get() );
//				return result;
//			}
//		
//		return false;

		Set<User> result = follows( userId, new Long[] {followId}, flag);
		return (result != null && result.size() == 1 
				&& result.iterator().next().getId().equals(followId));
	}

	@Override
	public boolean follow(Long userId, String followName, AddRemove flag) {
//		Optional<User> user = userRepository.findById(userId);
//		User follow = userRepository.findByName(followName);
//		if(user.isPresent() && follow != null) 
//			if( ! user.get().getFollows().contains(follow)) { // Condition added after test testAddFollowLongString 
//				// Existing follower follows existing follow already is following by Name
//				boolean result = false; 
//				if( flag == AddRemove.ADD)
//					result = user.get().getFollows().add( follow );
//				else
//					result = user.get().getFollows().remove( follow );
//				userRepository.save( user.get() );
//				return result;
//			}
//		return false;		

		User follow = userRepository.findByName(followName);
		if(follow != null && follow.getId() != null) {
			Set<User> result = follows( userId, new Long[] {follow.getId()}, flag);
			return (result != null && result.size() == 1 
					&& result.iterator().next().getId().equals(follow.getId()));
		}else
			return false;
	}

	@Override
	public boolean follow(String name, String followName, AddRemove flag) {
		Set<User> result = follows( name, new String[] {followName}, flag);
		return (result != null && result.size() == 1 
				&& result.iterator().next().getName().equals(followName));
	}

	@Override
	public Set<User> follows(String name, String[] followNames) {
		return follows(name, followNames, AddRemove.ADD);
	}

	@Override
	public Set<User> follows(String name, String[] followNames, AddRemove flag) {
		Set<User> added = new HashSet<>();
		User user = userRepository.findByName(name);
		if(user != null) {
			Arrays.stream(followNames).filter( followName -> ! followName.equals( user.getName() ) )
				.forEach(followName -> {
					User followUser = userRepository.findByName(followName);
					if(followUser != null && 
							!(user.equals(followUser)  && flag == AddRemove.ADD) ) {
						boolean success = false;
						if(flag == AddRemove.ADD) 
							success = user.getFollows().add(followUser);
						else
							success = user.getFollows().remove(followUser);
						if(success)
							added.add(followUser);
					}
				});
			
			userRepository.save(user);
		}
		
		return added;
	}

	@Override
	public Set<User> follows(Long userId, Long[] followIds) {
		return follows(userId, followIds, AddRemove.ADD);
	}

	@Override
	public Set<User> follows(Long userId, Long[] followIds, AddRemove flag) {
		Set<User> processed = new HashSet<>();
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			Arrays.stream(followIds).filter( followId -> followId != user.get().getId() )
				.forEach(followId -> {
					Optional<User> followUser = userRepository.findById(followId);
					if(followUser.isPresent() && 
							!( user.get().equals(followUser.get()) && flag == AddRemove.ADD)) { 
						// Can remove itself from follows if added by mistake, but cannot itself
						boolean success = false;
						if(flag == AddRemove.ADD) 
							success = user.get().getFollows().add(followUser.get());
						else
							success = user.get().getFollows().remove(followUser.get());
						if(success)
							processed.add(followUser.get());
					}
				});
			
			userRepository.save(user.get());
		}
		
		return processed;
	}
}

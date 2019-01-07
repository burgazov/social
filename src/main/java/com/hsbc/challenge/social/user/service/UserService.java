package com.hsbc.challenge.social.user.service;

import java.util.Set;
import java.util.SortedSet;

import com.hsbc.challenge.social.message.model.Message;
import com.hsbc.challenge.social.user.model.User;

public interface UserService {
	public Set<User> getAllUsers();
	
	public SortedSet<Message> getMessages(Long userId);
	public SortedSet<Message> getMessages(String name);
	
	public SortedSet<Message> getAllMessages(Long userId);
	public SortedSet<Message> getAllMessages(String name);

	public User createUser(String name);
	public User createUser(String name, String displayName);

	public Message createMessage(String name, String message);
	public Message createMessage(String name, String displayName, String message);

	public Message createMessage(Long userId, String message);
	
	public boolean follow(Long userId, Long followId);	
	public boolean follow(Long userId, String followName);
	public boolean follow(String name, String followName);
	
	public boolean follow(Long userId, Long followId, AddRemove flag);	
	public boolean follow(Long userId, String followName, AddRemove flag);
	public boolean follow(String name, String followName, AddRemove flag);

	public Set<User> follows(String name, String[] followNames);
	public Set<User> follows(Long userId, Long[] followIds);

	public Set<User> follows(String name, String[] followNames, AddRemove flag);
	public Set<User> follows(Long userId, Long[] followIds, AddRemove flag);
}

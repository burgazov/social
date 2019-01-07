package com.hsbc.challenge.social.message.repository;

import java.util.Optional;
import java.util.SortedSet;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsbc.challenge.social.message.model.Message;
import com.hsbc.challenge.social.user.model.User;

public interface MessageRepository extends JpaRepository<Message, Long>{
	public Optional<Message> findById(Long id);
	public SortedSet<Message> findByUser(User user);
}

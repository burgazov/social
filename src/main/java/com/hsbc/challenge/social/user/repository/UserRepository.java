package com.hsbc.challenge.social.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsbc.challenge.social.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findById(Long id);
	public User findByName(String name);
	public User findByDisplayName(String displayName);
}

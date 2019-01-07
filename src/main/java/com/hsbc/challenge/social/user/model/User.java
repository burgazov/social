package com.hsbc.challenge.social.user.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hsbc.challenge.social.message.model.Message;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@Entity
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="USER_ID")
	private Long id;
	
	@Column(name="DISPLAY_NAME",nullable=false, unique=true)
	private String displayName;
	
	@Column(name="NAME", nullable=false, unique=true)
	private String name;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("created")
	private SortedSet<Message> messages;
	
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="FOLLOWER_USER",
		joinColumns={ @JoinColumn(name="FOLLOWER_ID", referencedColumnName="USER_ID") },
		inverseJoinColumns={ @JoinColumn(name="USER_ID", referencedColumnName="USER_ID") }) 
	private Set<User> follows;
	
	@Column(name="CREATED", nullable=false,
			columnDefinition="datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private Date created;
	
	@Column(name="UPDATED", nullable=false,
			columnDefinition="datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private Date updated;
	
	public User() {}
	
	public User(String name) {
		this(name, name);
	}
	
	public User(String name, String displayName) {
		this.name = name; this.displayName = displayName == null? name: displayName;
		this.created = new Date(); this.updated = new Date();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	// Once user is created the name should not be changed
//	public void setName(String name) {
//		this.name = name;
//	}
	
	public boolean addMessage(Message message) {
		if(messages == null)
			messages = new TreeSet<>();
		return messages.add(message);		
	}

	public SortedSet<Message> getMessages() {
		if(messages == null)
			messages = new TreeSet<>();
		return messages;
	}
	
	// Not quite appropriate to have it. Messages comes one by one, but not many at a moment
//	public void setMessages(SortedSet<Message> messages) {
//		this.messages = messages;
//	}

	// TODO Better return copy of the set follows to avoid direct removal/adding 
	public Set<User> getFollows() {
		if(follows == null)
			follows = new HashSet<>();
		
		return follows;
	}

	// DO NOT add non persisted objects into the Set
	public void setFollows(Set<User> follows) {
		if( follows.contains(this))
			follows.remove( this );
		
		this.follows = follows;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}
	
	// Should NOT be allowed to set the create date. It should be set up on db level
//	public void setCreated(Date created) {
//		this.created = created;
//	}

	public Date getUpdated() {
		return updated;
	}
	
	// https://struberg.wordpress.com/2016/10/15/tostring-equals-and-hashcode-in-jpa-entities/
	@Override
	public boolean equals(Object o) {
		if( o== this )
			return true;
		if( ! (o instanceof User) )
			return false;
		
		User user = (User) o;
		
		return Objects.equals( this.id, user.id );  // DO NOT add non persisted objects into Set
//			|| 	Objects.equals( this.name, user.name ) && Objects.equals(this.displayName, user.displayName);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( this.id, this.name, this.displayName );
	}
	
}

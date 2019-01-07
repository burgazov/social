package com.hsbc.challenge.social.message.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hsbc.challenge.social.user.model.User;

@Entity
public class Message implements Comparable<Message>{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="MESSAGE", nullable=false)
	private String message;
	
	@ManyToOne(fetch = FetchType.LAZY)
	 	@JoinColumn(name = "USER_ID", nullable=false)
	private User user;
	
	@Column(name="CREATED", nullable=false,
			columnDefinition="datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private Date created  = new Date();
	
	@Column(name="UPDATED", nullable=false,
			columnDefinition="datetime NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private Date updated = new Date();
	
	public Message() {this("", null);}
	
	public Message(String message, User user) {
		this.message = message == null? "": message;
		this.user = user;
		this.created = new Date();
		this.updated = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
		
	@Override
	public boolean equals(Object o) {
		if( this == o )
			return true;
		if( ! (o instanceof Message) )
			return false;
		Message mess = (Message) o;
		return Objects.equals( this.id, mess.id) ||
			Objects.equals( this.message, mess.message ) 
				&& Objects.equals( this.user, mess.user);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( this.id, this.message, this.user );
	}
	
	@Override
	public String toString() {
		return "User id: " + user.getId() + ", user name = " + user.getName() +
				", Message id = " + this.id + ", message = " + this.message + ", created + " + this.created;
	}

	@Override
	public int compareTo(Message o) {
		return o.getCreated().compareTo(this.getCreated());
	}
}

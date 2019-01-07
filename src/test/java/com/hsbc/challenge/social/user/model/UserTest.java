package com.hsbc.challenge.social.user.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hsbc.challenge.social.message.model.Message;
import com.hsbc.challenge.social.message.repository.MessageRepository;
import com.hsbc.challenge.social.user.repository.UserRepository;

@DataJpaTest
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@AutoConfigurationPackage
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class UserTest {
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private MessageRepository messageRepository;
	
	public static String[][] NAMES = {
			{"John Smith", "John Crazy Smith"},
			{"Ferdinand Burgazov", "Ferd Jolly Burg"},
			{"Jane Eyre", null}
	};
	
	public static String[] MESSAGES_TEXT = {
		"John Smith message", "John Crazy Smith message",
		"Ferdinand Burgazov message", "Ferd Jolly Burg message",
		"Jane Eyre message", "Jane Poor Eyre message"
	};
	
	public static void initializeUsers(TestEntityManager entityManager
//			, 
			) {
		entityManager.persist( new User( NAMES[ 0 ][ 0 ], NAMES[ 0 ][ 1 ]) );
		entityManager.persist( new User( NAMES[ 1 ][ 0 ], NAMES[ 1 ][ 1 ]) );
		entityManager.persist( new User( NAMES[ 2 ][ 0 ]) );

	}
	
	// Each user two messages as per the above static array MESSAGES_TEXT
	public static void initializeMessages(TestEntityManager entityManager,
			UserRepository userRepository, MessageRepository messageRepository) {
		
		Arrays.asList( 
				new Message( MESSAGES_TEXT[ 0 ], userRepository.findByName( NAMES[ 0][ 0] ) ),
				new Message( MESSAGES_TEXT[ 2 ], userRepository.findByName( NAMES[ 1][ 0] ) ),
				new Message( MESSAGES_TEXT[ 4 ], userRepository.findByName( NAMES[ 2][ 0] ) )
		);	
		
		Stream.of( 
				new Message( MESSAGES_TEXT[ 0 ], userRepository.findByName( NAMES[ 0][ 0] ) ),
				new Message( MESSAGES_TEXT[ 2 ], userRepository.findByName( NAMES[ 1][ 0] ) ),
				new Message( MESSAGES_TEXT[ 4 ], userRepository.findByName( NAMES[ 2][ 0] ) )
		).forEach( msg -> messageRepository.save(msg) );
		
		Stream.of( 
				new Message( MESSAGES_TEXT[ 1 ], userRepository.findByName( NAMES[ 0][ 0] ) ),
				new Message( MESSAGES_TEXT[ 3 ], userRepository.findByName( NAMES[ 1][ 0] ) ),
				new Message( MESSAGES_TEXT[ 5 ], userRepository.findByName( NAMES[ 2][ 0] ) )
		).forEach( msg -> messageRepository.save(msg) );
		
		messageRepository.findAll().stream().forEach(msg -> {
//			msg.getUser().getMessages().add(msg);
			msg.getUser().addMessage(msg);
			entityManager.persist( msg.getUser() );
		} );		
	}
	
	// Each user follows the other two. TODO Elaborate it
	private static void initializeFollows(TestEntityManager entityManager,
			UserRepository userRepository) {
		List<User> users = userRepository.findAll();
		users.stream().forEach( usr -> {
			users.stream().forEach( user -> {
				if( usr != user )
					usr.getFollows().add(user);
			});
		});
		
		userRepository.saveAll( users );
	}
	
	private static Date TIMESTAMP;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {		
		TIMESTAMP = new Date();	
		
		initializeUsers( this.entityManager);// , this.userRepository, this.messageRepository
		
		List<User> users = userRepository.findAll();		
		assertNotNull( users );
		assertEquals( 3l, users.size());
//		users.stream().forEach( usr -> System.out.println(
//				"id = " + usr.getId() + ", name - " + usr.getName()) );		
	}

	@Test
	public void testGetId() {
		User user = userRepository.findByName("John Smith");
		assertNotNull( user.getId() );
		assertTrue( user.getId() > 0 );
		assertEquals( 4l, user.getId().longValue() );		
	}

	@Test
	public void testGetDisplayName() {
		User user = userRepository.findByName("Ferdinand Burgazov");
		assertNotNull( user );
		assertNotNull( user.getName() );
		assertNotNull( user.getDisplayName() );
		assertEquals( "Ferd Jolly Burg", user.getDisplayName() );
		
		user = userRepository.findByName( NAMES[ 2 ][ 0 ] );
		assertNotNull( user );
		assertNotNull( user.getName() );
		assertNotNull( user.getDisplayName() );
		assertEquals( NAMES[ 2 ][ 0 ], user.getDisplayName() );
	}

	@Test
	public void testSetDisplayName() {
		User user = userRepository.findByName( NAMES[ 2 ][ 0 ] );
		assertNotNull( user );
		assertNotNull( user.getDisplayName() );
		assertEquals( NAMES[ 2 ][ 0 ], user.getDisplayName() );
		
		String newDisplayName = "Jane Poor Eyre";
		user.setDisplayName( newDisplayName );
		entityManager.persist( user );
		
		user = userRepository.findByDisplayName(newDisplayName);
		assertNotNull( user );
		assertNotNull( user.getDisplayName() );
		assertNotEquals( NAMES[ 2 ][ 0 ], user.getDisplayName());
		assertEquals( "Jane Poor Eyre", user.getDisplayName() );		
	}

	@Test
	public void testGetMessages() {
		initializeMessages(this.entityManager, this.userRepository, this.messageRepository);
		
		List<User> users = userRepository.findAll();		
		assertNotNull( users );
		assertEquals( 3l, users.size());
		
		users.parallelStream().forEach( usr -> {
			assertEquals( 2l, usr.getMessages().size());
			assertTrue( usr.getMessages().first().getCreated().compareTo(
					usr.getMessages().last().getCreated() ) >= 0 );
		});
		
		// Test for sorting the messages
		User user = userRepository.findByName("John Smith");
		Message message = new Message("Most recent message", user );
		entityManager.persist( message );
		user.getMessages().add(message);
		entityManager.persist( user );
		user.getMessages().stream().filter( msg -> msg != message ).forEach( msg -> 
			assertTrue(	message.getCreated().compareTo( msg.getCreated()) > 0 ) );
	}

//	@Test
//	public void testGetMessagesOld() { // TEST IT VERY THOUROUGHLY
//		List<User> users = userRepository.findAll();		
//		assertNotNull( users );
//		assertEquals( 3l, users.size());
//		users.stream().forEach( usr -> assertNotNull( usr.getMessages() ) );			
//		
//		User user = userRepository.findByName("John Smith");
//		
//		Message message1 = new Message( MESSAGES_TEXT[ 0 ], user);
////		entityManager.persist( message1 );
////		user.getMessages().add(message1);
////		entityManager.persist( user );		
//
//		Message message2 = new Message( MESSAGES_TEXT[ 1 ], user);
////		entityManager.persist( message2 );
////		user.getMessages().add(message2);
////		entityManager.persist( user );
//
//		Message message3 = new Message( "Most Recent message", user);
////		entityManager.persist( message3 );
////		user.getMessages().add(message3);
////		entityManager.persist( user );
//
//		Message message4 = new Message( "Most Recent message 4", user);
////		entityManager.persist( message4 );
////		user.getMessages().add(message4);
//		
//		messageRepository.saveAll( Arrays.asList( message1, message2, message3, message4 ) );
//		boolean added = user.getMessages().addAll( Arrays.asList( message1, message2, message3, message4 ) );
//		entityManager.persist( user );
//		
//		
//		assertEquals( 4l, user.getMessages().size());
//		
//		assertEquals( 1l, 
//				user.getMessages().first().getCreated().compareTo( 
//						user.getMessages().last().getCreated() ) );
//	}

	@Test
	public void testSetMessages() {
		
	}

	@Test
	public void testEquals() {
		Object object = new Object();
		List<User> users = userRepository.findAll();
		users.parallelStream().forEach( usr -> {
			users.stream().forEach( user -> {
				if( usr != user )
					assertFalse( usr.equals(user));
				else
					assertTrue( usr.equals(user));
				assertFalse( usr.equals( object ));				
			});		
		} );
	}

	@Test
	public void testGetFollows() {
		initializeFollows( this.entityManager, this.userRepository );
		
		// Confirms each user follows two other users, according to static initializeFollows
		userRepository.findAll().parallelStream().forEach(usr -> {
			assertEquals( 2l, usr.getFollows().size() );
			assertFalse( usr.getFollows().contains(usr) ); // Doesn't follow itself // TODO Elaborate it

		});	
	}

	@Test
	public void testSetFollows() {	
		initializeFollows( this.entityManager, this.userRepository );
		
		List<User> users = userRepository.findAll();
		
		users.parallelStream().forEach(usr -> 
			usr.setFollows( new HashSet<>(users) ) );	// each follows all the rest		
		users.parallelStream().forEach(usr -> 
			assertEquals( 2l, usr.getFollows().size() ) ); 
		
		users.parallelStream().forEach(usr -> 
			usr.setFollows( new HashSet<>( Arrays.asList(usr) ) ) ); // each user to itself		
		users.parallelStream().forEach(usr -> 
			assertEquals( 0, usr.getFollows().size() ) ); // Not allowed to follow itself
	}

	@Test
	public void testGetCreated() {
		List<User> users = userRepository.findAll();
		assertNotNull( users );
		assertEquals( 3l, users.size());
		users.stream().forEach( 
				user -> assertNotNull( user.getCreated() ));
	}

}

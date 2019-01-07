/**
 * 
 */
package com.hsbc.challenge.social.user.service;

import static com.hsbc.challenge.social.user.model.UserTest.MESSAGES_TEXT;
import static com.hsbc.challenge.social.user.model.UserTest.NAMES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hsbc.challenge.social.message.model.Message;
import com.hsbc.challenge.social.message.repository.MessageRepository;
import com.hsbc.challenge.social.user.model.User;
import com.hsbc.challenge.social.user.repository.UserRepository;

/**
 * @author ferdinand.burgazov
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest {
	@Mock private UserRepository userRepository;
	@Mock private MessageRepository messageRepository;
	
	@InjectMocks private UserServiceImpl userService;

	private static Map<Long, User> USERS_MAP = new HashMap<>(); // TODO Move it to Map
	private static Map<Long, Message> MESSAGES_MAP = new HashMap<>(); // new Message[ MESSAGES_TEXT.length ]; // TODO Move it to Map to avoid repetition and expand in elements 
	

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {		
		for(int i = 0; i < MESSAGES_TEXT.length; i++ ) {
			Message message = new Message( MESSAGES_TEXT[ i ], new User("Fake User") );
			message.setId( (long) (i + 1) );
			MESSAGES_MAP.put(message.getId(), message);
			Thread.sleep( 100 );// Delay to create each message with different time stamp
		}	
		
		for(int i = 0; i < NAMES.length; i++ ) {
			User user = new User( NAMES[ i ][ 0 ], NAMES[ i ][ 1 ] );
			user.setId( (long) (i + 1) );
			
			user.addMessage(  MESSAGES_MAP.get( (long) (i * 2 + 1) ) ); // MESSAGES_MAP[ i * 2 ]
			MESSAGES_MAP.get( (long) (i * 2 + 1) ).setUser( user );
			
			user.addMessage( MESSAGES_MAP.get( (long) (i * 2 + 2) ) );
			MESSAGES_MAP.get( (long) (i * 2 + 2) ).setUser( user );
			
			USERS_MAP.put( user.getId(), user);
		}		

		assertEquals(MESSAGES_TEXT.length, MESSAGES_MAP.size());
		assertEquals(NAMES.length, USERS_MAP.size());
		
		USERS_MAP.values().forEach( usr -> 
				assertEquals( 2, usr.getMessages().size()) );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	private static User findUserByName( String userName) {
		Optional<User> user = 
			USERS_MAP.values().stream().filter(  usr -> usr.getName().equals(  userName ) ).findFirst();
		return user.isPresent()? user.get(): null;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		when( userRepository.findAll() ).thenReturn( new ArrayList<User>( USERS_MAP.values()  ) );			
		
		for(int i = 0; i < USERS_MAP.size(); i++ )		
			when( userRepository.findByName( NAMES[ i ][ 0 ] ) ).thenReturn( findUserByName(NAMES[ i ][ 0 ]) );
		
		for(int i = 1; i <= MESSAGES_MAP.size(); i++ )		
			when( messageRepository.findById( (long) i ) ).thenReturn( Optional.of( MESSAGES_MAP.get( (long) i ) ));
		
		when( userRepository.findByName( any(String.class)  ) ).thenAnswer( new Answer<User>() {
			@Override public User answer(InvocationOnMock invocation) throws Throwable {
				return findUserByName( invocation.getArgument( 0 ) );
			}
			
		});
		
		when( userRepository.findById( any(Long.class) ) ).thenAnswer(new Answer<Optional<User>>() {
			@Override public Optional<User> answer(InvocationOnMock invocation) throws Throwable {
				Long key = invocation.getArgument( 0 );
				return Optional.ofNullable( USERS_MAP.get(key) );
			}
			
		});
		
		when( userRepository.save( any(User.class) ) ).thenAnswer(new Answer<User>() {
			@Override public User answer(InvocationOnMock invocation) throws Throwable {
				User user = invocation.getArgument( 0 );
				if( user.getId() == null ) {
					User userByName = findUserByName( user.getName() );
					if( userByName != null )
						return userByName;
					user.setId( 
						USERS_MAP.keySet().stream().max(Comparator.comparing(Long::valueOf)).get() + 1);	
					USERS_MAP.put( user.getId(), user);
				}else
					USERS_MAP.put( user.getId(), user);
				
				return user;
			}			
		});
		

		when( messageRepository.save(any(Message.class))).thenAnswer( new Answer<Message>() {
			@Override public Message answer(InvocationOnMock invocation) throws Throwable {
				Message message = invocation.getArgument( 0 );
				if( message.getId() == null ) {
					message.setId( 
						MESSAGES_MAP.keySet().stream().max(Comparator.comparing(Long::valueOf)).get() + 1);
					MESSAGES_MAP.put( message.getId(), message);
				}else
					MESSAGES_MAP.put( message.getId(), message);
				return message;
			}			
		});
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#getAllUsers()}.
	 */
	@Test
	public void testGetAllUsers() {		
		Set<User> users = userService.getAllUsers();
		assertEquals( 3l, users.size() );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#getMessages(java.lang.Long)}.
	 */
	@Test
	public void testGetMessagesLong() {		
		User user = userRepository.findById( 1l ).get();
		user.addMessage( MESSAGES_MAP.get( 1l )); // Should NOT add it twice
		
		SortedSet<Message> messages = userService.getMessages( 1l );
		assertEquals( 2l, messages.size() ); // It expects 2, but not 3. It's NOT added twice
		assertTrue( messages.first().getCreated().compareTo( 
				messages.last().getCreated()) > 0 ); // Returns messages in reverse order of creation
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#getMessages(java.lang.String)}.
	 */
	@Test
	public void testGetMessagesString() {		
		User user = userRepository.findByName( NAMES[ 1 ][ 0 ] );
		
		SortedSet<Message> messages = userService.getMessages( user.getName() );
		assertEquals( 2l, messages.size() ); // It expects 2, but not 3. It's NOT added twice
		assertTrue( messages.first().getCreated().compareTo( 
				messages.last().getCreated()) > 0 ); // Returns messages in reverse order of creation
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#getAllMessages(java.lang.Long)}.
	 */
	@Test
	public void testGetAllMessagesLong() {
		List<User> users = userRepository.findAll();
		users.stream().forEach( user -> user.setFollows( new HashSet<User>( users )) );
		
		users.stream().forEach( user -> {
			SortedSet<Message> messages = userService.getAllMessages( user.getId() );
			assertEquals( 4l, messages.size() );
			assertTrue(messages.first().getCreated().compareTo( 
				messages.last().getCreated()) > 0 ); 
		});
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#getAllMessages(java.lang.String)}.
	 */
	@Test
	public void testGetAllMessagesString() {
		List<User> users = userRepository.findAll();
		users.stream().forEach( user -> user.setFollows( new HashSet<User>( users )) );
		
		users.stream().forEach(user -> System.out.println( user ) );
		users.stream().forEach( user -> {
			SortedSet<Message> messages = userService.getAllMessages( user.getName() );
			assertEquals( 4l, messages.size() );
			assertTrue(messages.first().getCreated().compareTo( 
				messages.last().getCreated()) > 0 ); 
		});
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#createUser(java.lang.String)}.
	 */
	@Test 
	public void testCreateUserString() {
		// Create new user with already existing user with same name will return created User
		USERS_MAP.keySet().parallelStream().forEach( key -> {
			User user = userService.createUser( USERS_MAP.get(key).getName() );
			assertNotNull(user);
			assertEquals( USERS_MAP.get(key).getId(), user.getId());
			assertEquals( USERS_MAP.get(key), user);
		});

		User user = userService.createUser("New User Name");
		assertNotNull(user);
		assertEquals( USERS_MAP.size(),  user.getId().longValue() );
		assertEquals( user.getName(), user.getDisplayName() );
		assertEquals( "New User Name", user.getDisplayName() );
		
		USERS_MAP.remove(user.getId());
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#createUser(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateUserStringString() {
		USERS_MAP.keySet().parallelStream().forEach( key -> {
			User user = userService.createUser( USERS_MAP.get(key).getName(), USERS_MAP.get(key).getDisplayName() );
			assertNotNull(user);
			assertEquals( USERS_MAP.get(key).getId(), user.getId());
			assertEquals( USERS_MAP.get(key), user);
		});
		
		User user = userService.createUser("New User Name", "New User Display Name");
		assertNotNull(user);
		assertEquals( USERS_MAP.size(),  user.getId().longValue() );
		assertEquals( "New User Name", user.getName() );
		assertEquals( "New User Display Name", user.getDisplayName() );
		
		USERS_MAP.remove(user.getId());
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#createMessage(java.lang.String, java.lang.String)}.
	 * @throws InterruptedException 
	 */
	@Test
	public void testCreateMessageStringString() throws InterruptedException {		
		// Create message with existing user
		Message message = userService.createMessage( NAMES[ 1 ][ 0 ], 
				"Recent message of " + NAMES[ 1 ][ 0 ] + " for testCreateMessageStringString");
		assertNotNull( message );
		assertEquals( "Recent message of " + NAMES[ 1 ][ 0 ] + " for testCreateMessageStringString", 
				message.getMessage());
		assertEquals( (long) MESSAGES_MAP.size(), message.getId().longValue() );
		assertEquals( message, message.getUser().getMessages().first());
		message.getUser().getMessages().remove( message ); // Remove the recent message to avoid other tests disruptions
		message.setUser( null );
		MESSAGES_MAP.remove(message.getId());
		
		// Create message with NON existing user
		message = userService.createMessage( "New User Name", "New User Message" );
		assertNotNull( message );
		assertEquals( "New User Message", message.getMessage() );
		assertEquals( (long) MESSAGES_MAP.size(), message.getId().longValue() );
		
		assertEquals( message, message.getUser().getMessages().first());
		assertEquals( message, message.getUser().getMessages().last());		
		assertEquals( NAMES.length + 1, message.getUser().getId().longValue());
		assertEquals( "New User Name", message.getUser().getName());
		assertEquals( "New User Name", message.getUser().getDisplayName());	
		
		// Add additional message to the existing user
		Thread.sleep( 100 );
		Message newMessage = userService.createMessage( "New User Name", "New User Message Next" );
		assertNotNull( newMessage );
		assertEquals( "New User Message Next", newMessage.getMessage() );
		assertEquals( (long) MESSAGES_MAP.size(), newMessage.getId().longValue() );	

		assertNotEquals(message, newMessage);
		assertEquals(message.getUser(), newMessage.getUser());
		assertEquals( newMessage, message.getUser().getMessages().first());
		assertEquals( message, message.getUser().getMessages().last());	
		
		MESSAGES_MAP.remove(message.getId());
		MESSAGES_MAP.remove(newMessage.getId());
		USERS_MAP.remove(message.getUser().getId());
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#createMessage(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateMessageStringStringString() {
		
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#createMessage(java.lang.Long, java.lang.String)}.
	 */
	@Test
	public void testCreateMessageLongString() {
		// Create message with existing user
		Message message = userService.createMessage( 2L, 
				"New Message for testCreateMessageLongString User id = 2" );
		assertNotNull( message );
		assertEquals( "New Message for testCreateMessageLongString User id = 2", message.getMessage());
		assertEquals( (long) MESSAGES_MAP.size(), message.getId().longValue() );
		assertEquals( message, message.getUser().getMessages().first());		
		
		message.getUser().getMessages().remove( message ); // Remove the recent message to avoid other tests disruptions
		message.setUser( null );
		MESSAGES_MAP.remove(message.getId());		

		// Create message with NON existing user
		message = userService.createMessage( (long) USERS_MAP.size() + 1, 
				"New Message for testCreateMessageLongString User id = 2" );
		assertNull( message );		
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#follow(java.lang.Long, java.lang.Long)}.
	 */
	@Test
	public void testAddFollowLongLong() {
		// NON existing follower follows NON existing follow both by Id. Should crash!!!
		boolean nonExistingBothIds = userService.follow(100l, 100l);
		assertFalse( nonExistingBothIds );
		
		// Existing follower follows NON existing follow both by Id
		nonExistingBothIds = userService.follow( 1l, 100l);
		assertFalse( nonExistingBothIds );
		
		// Existing follower follows itself both by Id
		nonExistingBothIds = userService.follow( 1l, 1l);
		assertFalse( nonExistingBothIds );
		
		// Existing follower follows existing follow already is following
		nonExistingBothIds = userService.follow( 1l, 2l);
		assertFalse( nonExistingBothIds );
		
		// New user with new message
		Message message = userService.createMessage("New User for testAddFollowLongLong", 
			"New Message for testAddFollowLongLong");
		assertNotNull( message );
		assertNotNull( message.getUser() );
		
		// Existing follower follows existing follow both by Id
		User user = userRepository.findById( 1l ).get();
		boolean existingBothIds = userService.follow( user.getId(), message.getUser().getId() );
		assertTrue( existingBothIds );
		assertEquals( 3, user.getFollows().size());
		assertTrue( user.getFollows().contains( message.getUser() ) );		
		
		user.getFollows().remove(message.getUser());
		assertEquals( 2, user.getFollows().size());
		MESSAGES_MAP.remove(message.getId());
		USERS_MAP.remove(message.getUser().getId());
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#follow(java.lang.Long, java.lang.String)}.
	 */
	@Test
	public void testAddFollowLongString() {
		// NON existing follower by Id follows NON existing follow by Name. 
		// Should not create new user and should crash!!!
		boolean nonExistingBothIds = userService.follow( 100l, "New NON Existing");
		assertFalse( nonExistingBothIds );
		
		// Existing follower follows NON existing follow both by Id
		nonExistingBothIds = userService.follow( 1l, "New NON Existing");
		assertFalse( nonExistingBothIds );		

		User user = userRepository.findById( 1l ).get();
		
		// Existing follower follows existing follow already is following by Name
		nonExistingBothIds = userService.follow( user.getId(), 
				user.getFollows().stream().findFirst().get().getName() );		
		assertFalse( nonExistingBothIds );
		
		// New user with new message
		Message message = userService.createMessage("New User for testAddFollowLongString", 
			"New Message for testAddFollowLongString");
		assertNotNull( message );
		assertNotNull( message.getUser() );
		
		// Existing follower by Id follows existing follow by name
		boolean existingByIdFllowsByName = userService.follow( user.getId(), 
				"New User for testAddFollowLongString" );
		assertTrue( existingByIdFllowsByName );
		assertEquals( 3, user.getFollows().size());
		assertTrue( user.getFollows().contains( message.getUser() ) );	
		
		user.getFollows().remove(message.getUser());
		assertEquals( 2, user.getFollows().size());
		MESSAGES_MAP.remove(message.getId());
		USERS_MAP.remove(message.getUser().getId());		
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#follow(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddFollowStringString() {
		// NON existing follower by name follows NON existing follow by name
		// Should not create new user and should crash!!!
		boolean nonExistingBothIds = userService.follow( "Non existing user", "New NON Existing");
		assertFalse( nonExistingBothIds );

		User user = userRepository.findById( 1l ).get();
		
		// Existing follower by name follows NON existing by name
		nonExistingBothIds = userService.follow( user.getName(), "New NON Existing");
		assertFalse( nonExistingBothIds );
		
		// Existing follower follows existing follow already followed by Name
		nonExistingBothIds = userService.follow( user.getId(), 
				user.getFollows().stream().findFirst().get().getName() );		
		assertFalse( nonExistingBothIds );
		
		// New user with new message
		Message message = userService.createMessage("New User for testAddFollowLongString", 
			"New Message for testAddFollowLongString");
		assertNotNull( message );
		assertNotNull( message.getUser() );
		
		// Existing follower by Id follows existing follow by name
		boolean existingByIdFllowsByName = userService.follow( user.getName(), 
				"New User for testAddFollowLongString" );
		assertTrue( existingByIdFllowsByName );
		assertEquals( 3, user.getFollows().size());
		assertTrue( user.getFollows().contains( message.getUser() ) );	
		
		user.getFollows().remove(message.getUser());
		assertEquals( 2, user.getFollows().size());
		MESSAGES_MAP.remove(message.getId());
		USERS_MAP.remove(message.getUser().getId());	
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#follows(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public void testAddFollowsStringStringArray() {
		// NON existing follower by name follows NON existing follow by name
		// Should not create new user and should crash!!!
		String[] nonExisitngUsers = {"None 1", "None 2", "Just None", ""};
		Set<User> nonAddedUsers = userService.follows( "Non existing user", nonExisitngUsers);
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );

		User user = userRepository.findById( 1l ).get();
		
		// Existing follower by name follows NON existing users by name
		nonAddedUsers = userService.follows( user.getName(), nonExisitngUsers);
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );
		
		// Existing follower follows existing follow already followed by Name
		nonAddedUsers = userService.follows( user.getName(), 
				user.getFollows().stream().map( usr -> usr.getName() ).toArray( String[]::new ) );
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );
		
		// New user with new message
		Message message = userService.createMessage("New User for testAddFollowsStringStringArray", 
			"New Message for testAddFollowsStringStringArray");
		assertNotNull( message );
		assertNotNull( message.getUser() );
		
		// Existing user to follow existing users by names
		Set<User> addedUsers = userService.follows( user.getName(), new String[] {
				message.getUser().getName(), user.getName()	} );
		assertNotNull( addedUsers );
		assertEquals( 1, addedUsers.size() );
		assertEquals( 3, user.getFollows().size() );

		user.getFollows().remove(message.getUser());
		assertEquals( 2, user.getFollows().size());
		MESSAGES_MAP.remove(message.getId());
		USERS_MAP.remove(message.getUser().getId());	
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.user.service.UserServiceImpl#follows(java.lang.Long, java.lang.Long[])}.
	 */
	@Test
	public void testAddFollowsLongLongArray() {
		// NON existing follower follows NON existing follows all by Id.
		Set<User> nonAddedUsers = userService.follows(100l, new Long[] { 100l, 200l, 300l });
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );
		
		// Existing follower follows NON existing follow both by Id
		nonAddedUsers = userService.follows( 1l, new Long[]{ 100l, 200l, 300l } );
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );
		
		// Existing follower follows itself both by Id
		nonAddedUsers = userService.follows( 1l, new Long[]{ 1l, 1l, 1l } );
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );
		
		// Existing follower follows existing follows which already is following
		nonAddedUsers = userService.follows( 1l, new Long[]{ 2l, 3l });
		assertNotNull( nonAddedUsers );
		assertEquals( 0, nonAddedUsers.size() );		

		User user = userRepository.findById( 1l ).get();
		
		// New user with new message
		Message message = userService.createMessage("New User for testAddFollowLongLong", 
			"New Message for testAddFollowLongLong");
		assertNotNull( message );
		assertNotNull( message.getUser() );
		
//		// Existing follower follows existing follows all by Id
		Set<User> addedUsers = userService.follows( user.getId(), 
				new Long[]{  message.getUser().getId() } );
		assertNotNull( addedUsers );
		assertEquals( 1, addedUsers.size() );
		assertEquals( 3, user.getFollows().size() );	
		
		user.getFollows().remove(message.getUser());
		assertEquals( 2, user.getFollows().size());
		MESSAGES_MAP.remove(message.getId());
		USERS_MAP.remove(message.getUser().getId());
	}

}

/**
 * 
 */
package com.hsbc.challenge.social.message.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.AfterClass;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hsbc.challenge.social.message.repository.MessageRepository;
import com.hsbc.challenge.social.user.model.User;
import com.hsbc.challenge.social.user.model.UserTest;
import com.hsbc.challenge.social.user.repository.UserRepository;

/**
 * @author ferdinand.burgazov
 *
 */
@DataJpaTest
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@AutoConfigurationPackage
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class MessageTest {
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private MessageRepository messageRepository;
	
	private static Date TIMESTAMP;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TIMESTAMP = new Date();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		UserTest.initializeUsers( this.entityManager );
		UserTest.initializeMessages(entityManager, userRepository, messageRepository);

		assertEquals( 3l, userRepository.findAll().size());
		assertEquals( 6l, messageRepository.findAll().size());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#hashCode()}.
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#equals(java.lang.Object)}.
	 */
	@Test
	public void testHashCodeAndEquals() {		
		Optional<Message> message1 = messageRepository.findById( 
				messageRepository.findAll().get( 0 ).getId() );
		assertTrue(message1.isPresent());		

		Optional<Message> message2 = messageRepository.findById( message1.get().getId() );
		assertTrue(message2.isPresent());	
		
		assertEquals(message1.get().hashCode(), message2.get().hashCode());		
		assertTrue( message1.equals(message2));
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#Message()}.
	 */
	@Test(expected=DataIntegrityViolationException.class)
	public void testMessage() {
		Message message = new Message();
		message = messageRepository.save(message); // DataIntegrityViolationException exception		
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#Message(java.lang.String, com.hsbc.challenge.social.user.model.User)}.
	 */
	@Test(expected=DataIntegrityViolationException.class)
	public void testMessageStringNullUser() {
		Message message = new Message("Recent Message to test Constructor", null);
		message = messageRepository.save(message);
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#Message(java.lang.String, com.hsbc.challenge.social.user.model.User)}.
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#getId()}.
	 */
	@Test // (expected=DataIntegrityViolationException.class)
	public void testMessageStringUserAndGetId() {
		Message message = new Message("Recent Message to test Constructor", 
				userRepository.findAll().get( 0 ));
		message = messageRepository.save(message); // Should be OK
		assertNotNull(message);
		assertNotNull(message.getId());
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#getMessage()}.
	 */
	@Test
	public void testGetMessage() {
		Message message = new Message("Recent Message to test Constructor", 
				userRepository.findAll().get( 0 ));
		message = messageRepository.save(message); // Should be OK
		assertNotNull(message);
		assertEquals( "Recent Message to test Constructor", message.getMessage() );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#setMessage(java.lang.String)}.
	 */
	@Test
	public void testSetMessage() {
		String text = "Some text message";
		Message message = new Message("", userRepository.findAll().get( 0 ) );
		message.setMessage(text);	

		message = messageRepository.save(message);
		
		assertEquals( text, message.getMessage() );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#getUser()}.
	 */
	@Test
	public void testGetUser() {
		Message message = messageRepository.findAll().get( 0 );
		assertNotNull( message.getUser() );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#setUser(com.hsbc.challenge.social.user.model.User)}.
	 */
	@Test
	public void testSetUser() {
		User user = userRepository.findAll().get( 1 );
		Message message = messageRepository.findAll().get( 0 );
		assertNotNull( message.getUser() );
		assertNotEquals( user, message.getUser());
		
		message.setUser(user);
		message = messageRepository.save(message);
		assertEquals( user, message.getUser());
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#getCreated()}.
	 */
	@Test
	public void testGetCreated() {
		Message message = new Message("Recent Message to test Constructor", 
				userRepository.findAll().get( 0 ));
		Date created = message.getCreated();
		message = messageRepository.save(message);
		assertTrue( message.getCreated().equals(created) );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#getUpdated()}.
	 */
	@Test
	public void testGetUpdated() {
		Message message = new Message("Recent Message to test Constructor", 
				userRepository.findAll().get( 0 ));
		Date created = message.getUpdated();
		message = messageRepository.save(message);
		assertTrue( message.getUpdated().equals(created) );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#toString()}.
	 */
	@Test
	public void testToString() {
		Message message = messageRepository.findAll().get( 0 );
		String messageToString = message.toString();
		
		assertNotNull(messageToString);
		assertTrue( messageToString.contains( message.getUser().getId().toString() ));
		assertTrue( messageToString.contains( message.getUser().getName() ));
		assertTrue( messageToString.contains( message.getId().toString() ));
		assertTrue( messageToString.contains( message.getMessage() ));
		assertTrue( messageToString.contains( message.getCreated().toString() ));
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.message.model.Message#compareTo(com.hsbc.challenge.social.message.model.Message)}.
	 */
	@Test
	public void testCompareTo() {
		Message messageFirst = messageRepository.findAll().get( 0 );
		Message messageLast = messageRepository.findAll().get( 5 );
		
		Message message = new Message("Recent Message to test Compare to", 
				userRepository.findAll().get( 0 ));

		assertEquals( 1, messageFirst.compareTo(messageLast));
		assertEquals( -1, messageLast.compareTo(messageFirst));

		assertEquals( -1, message.compareTo(messageFirst));
		assertEquals( -1, message.compareTo(messageLast));
	}
}

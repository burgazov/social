package com.hsbc.challenge.social.message.model;

import static org.junit.Assert.*;
import static com.hsbc.challenge.social.user.model.UserTest.NAMES;

import java.util.Date;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.hsbc.challenge.social.user.model.User;
import com.hsbc.challenge.social.user.repository.UserRepository;

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
	
	private static Date TIMESTAMP;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		TIMESTAMP = new Date();
		entityManager.persist( new User( NAMES[ 0 ][ 0 ], NAMES[ 0 ][ 1 ]) );
		entityManager.persist( new User(NAMES[ 1 ][ 0 ], NAMES[ 1 ][ 1 ]) );
		entityManager.persist( new User(NAMES[ 2 ][ 0 ]) );
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMessage() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetId() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetMessage() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetMessage() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetUser() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetUser() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetCreated() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetCreated() {
		// fail("Not yet implemented");
	}

	@Test
	public void testHashCode() {
		// fail("Not yet implemented");
	}

	@Test
	public void testEquals() {
		// fail("Not yet implemented");
	}

	@Test
	public void testClone() {
		// fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		// fail("Not yet implemented");
	}

}

package com.hsbc.challenge.social;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hsbc.challenge.social.controller.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SocialNetworkChallengeApplicationTests {
	@Autowired
    private UserController controller;

	@Test
	public void contextLoads() {
		assertNotNull( controller );
	}
}


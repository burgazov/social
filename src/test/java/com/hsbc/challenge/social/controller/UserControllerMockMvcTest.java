/**
 * 
 */
package com.hsbc.challenge.social.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hsbc.challenge.social.SocialNetworkChallengeApplication;
import com.jayway.jsonpath.JsonPath;

/**
 * @author ferdinand.burgazov
 *
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SocialNetworkChallengeApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerMockMvcTest {
	private MockMvc mockMvc;
	
	@Autowired
    private WebApplicationContext wac;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getUserAllMessages(java.lang.Long)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getUserAllMessages(java.lang.String)}.
	 * 
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#createMessageParam(java.lang.String,java.lang.String)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#createMessage(java.lang.String,java.lang.String)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#createMessage(java.lang.String,java.lang.String,java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void test_1_GetUserAllMessages() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders.put(
				"/social/message/First User Name?message=First User Message") )
				.andDo( print() );	
		mockMvc.perform( MockMvcRequestBuilders.put( "/social/message/First User Name/First User message Second") )
				.andDo( print() );	

		mockMvc.perform( MockMvcRequestBuilders.put( 
				"/social/message/Second User Name/Second User Display Name/Second user message") )
				.andDo( print() );
		mockMvc.perform( MockMvcRequestBuilders.put( 
				"/social/message/Second User Name/Second User Display Name/Second user message NEXT") )
				.andDo( print() );		

		mockMvc.perform(  MockMvcRequestBuilders.get( "/social/user/id") ).andDo( print() );
		
		mockMvc.perform(  MockMvcRequestBuilders.put( "/social/user/follow/1/4") )
			.andExpect( jsonPath("$").isBoolean() )
			.andExpect( jsonPath("$").value( true ) );		
		
		mockMvc.perform(  MockMvcRequestBuilders.put( "/social/user/follow/1?follow=Second User Name") )
			.andExpect( jsonPath("$").isBoolean() )
			.andExpect( jsonPath("$").value( false ) );	
		
		mockMvc.perform(  MockMvcRequestBuilders.put( "/social/user/follow?userName=First User Name&follow=Second User Name") )
			.andExpect( jsonPath("$").isBoolean() )
			.andExpect( jsonPath("$").value( false ) );	
		
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/all/id/1")
				.accept(MediaType.APPLICATION_JSON) )
				.andExpect( jsonPath("$", hasSize( 2 ) ) )
				.andExpect( jsonPath("$[ 0 ].id").exists() )
//				.andExpect( jsonPath("$[ 0 ].id").value( 3 ) )
				.andExpect( jsonPath("$[ 1 ].id").exists() )
//				.andExpect( jsonPath("$[ 1 ].id").value( 2 ) )
				.andExpect( status().isOk() )
				.andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8) )
				.andDo( print() );
		
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/all/First User Name")
				.accept(MediaType.APPLICATION_JSON) )
				.andExpect( jsonPath("$", hasSize( 2 ) ) )
				.andExpect( jsonPath("$[ 0 ].id").exists() )
//				.andExpect( jsonPath("$[ 0 ].id").value( 3 ) )
				.andExpect( jsonPath("$[ 1 ].id").exists() )
//				.andExpect( jsonPath("$[ 1 ].id").value( 2 ) )
				.andExpect( status().isOk() )
				.andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8) )
				.andDo( print() );
		
		// [{"id":1,"displayName":"First User Name","name":"First User Name","messages":[{"id":3,"message":"First User message Second","created":"2019-01-04T00:40:20.287+0000","updated":"2019-01-04T00:40:20.287+0000"},{"id":2,"message":"First User Message","created":"2019-01-04T00:40:20.140+0000","updated":"2019-01-04T00:40:20.140+0000"}],"follows":[{"id":4,"displayName":"Second User Name","name":"Second User Name","messages":[{"id":6,"message":"Second user message NEXT","created":"2019-01-04T00:40:20.323+0000","updated":"2019-01-04T00:40:20.323+0000"},{"id":5,"message":"Second user message","created":"2019-01-04T00:40:20.305+0000","updated":"2019-01-04T00:40:20.305+0000"}],"follows":[],"created":"2019-01-04T00:40:20.303+0000","updated":"2019-01-04T00:40:20.303+0000"}],"created":"2019-01-04T00:40:20.044+0000","updated":"2019-01-04T00:40:20.044+0000"},{"id":4,"displayName":"Second User Name","name":"Second User Name","messages":[{"id":6,"message":"Second user message NEXT","created":"2019-01-04T00:40:20.323+0000","updated":"2019-01-04T00:40:20.323+0000"},{"id":5,"message":"Second user message","created":"2019-01-04T00:40:20.305+0000","updated":"2019-01-04T00:40:20.305+0000"}],"follows":[],"created":"2019-01-04T00:40:20.303+0000","updated":"2019-01-04T00:40:20.303+0000"}]
		ResultActions resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user") )
				.andDo( print() );
		String content = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println( content );
	}
	
	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getUserMessages(java.lang.Long)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getUserMessages(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void test_2_GetUserMessages() throws Exception {
		// {"id":2,"message":"new user name message","created":"2019-01-03T20:54:57.229+0000","updated":"2019-01-03T20:54:57.229+0000"}
		ResultActions resultActions = mockMvc.perform( MockMvcRequestBuilders.put(
					"/social/message/User Name/new user name message") )
				.andExpect( jsonPath( "$.id").exists() )
//				.andExpect( jsonPath( "$.id").value( 8 ) )
				.andExpect( jsonPath( "$.message").exists() )
				.andExpect( jsonPath( "$.message").value( "new user name message" ) )
				.andExpect( jsonPath( "$.created").exists() )
				.andExpect( jsonPath( "$.updated").exists() )
				.andExpect( status().isOk() )
				.andDo( print() );
		
		// {"id":4,"message":"New User Message","created":"2019-01-03T20:56:09.938+0000","updated":"2019-01-03T20:56:09.938+0000"}
		resultActions = mockMvc.perform( MockMvcRequestBuilders.put(
					"/social/message/User Name?message=New User Message") )
				.andExpect( jsonPath( "$.id").exists() )
//				.andExpect( jsonPath( "$.id").value( 9 ) )
				.andExpect( jsonPath( "$.message").exists() )
				.andExpect( jsonPath( "$.message").value( "New User Message" ) )
				.andExpect( jsonPath( "$.created").exists() )
				.andExpect( jsonPath( "$.updated").exists() )
				.andExpect( status().isOk() )
				.andDo( print() );
		
		// [{"id":3,"message":"New User Message","created":"2019-01-03T21:44:10.466+0000","updated":"2019-01-03T21:44:10.466+0000"},{"id":2,"message":"new user name message","created":"2019-01-03T21:43:26.818+0000","updated":"2019-01-03T21:43:26.818+0000"}]	
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/id/1")
				.accept(MediaType.APPLICATION_JSON) )
				.andExpect( jsonPath("$", hasSize( 2 ) ) )
				.andExpect( jsonPath("$[ 0 ].id").exists() )
//				.andExpect( jsonPath("$[ 0 ].id").value( 9 ) )
				.andExpect( jsonPath("$[ 1 ].id").exists() )
//				.andExpect( jsonPath("$[ 1 ].id").value( 8 ) )
				.andExpect( status().isOk() )
				.andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8) )
				.andDo( print() );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/User Name")
				.accept(MediaType.APPLICATION_JSON) )
				.andExpect( jsonPath("$", hasSize( 2 ) ) )
				.andExpect( jsonPath("$[ 0 ].id").exists() )
//				.andExpect( jsonPath("$[ 0 ].id").value( 8 ) )
				.andExpect( jsonPath("$[ 1 ].id").exists() )
//				.andExpect( jsonPath("$[ 1 ].id").value( 7 ) )
				.andExpect( status().isOk() )
				.andExpect( content().contentType(MediaType.APPLICATION_JSON_UTF8) )
				.andDo( print() );
	}
	
	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#createMessage(java.lang.String, com.hsbc.challenge.social.message.model.Message[])}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getAllUsers()}.
	 * @throws Exception 
	 */
	@Test
	public void test_3_MessageCreateAndGetAllUsersAddFollows() throws Exception {
		ResultActions resultActions = mockMvc.perform( MockMvcRequestBuilders.post("/social/message/First User")
				.contentType(MediaType.APPLICATION_JSON)
				.content("[{\"message\":\"First User Message\"},{\"message\":\"First User message Second\"}]") ) // ,
			.andExpect( jsonPath("$", hasSize( 2 ) ) )
			.andDo( print() );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.post("/social/message/TestAddFollows User")
				.contentType(MediaType.APPLICATION_JSON)
				.content("[{\"message\":\"TestAddFollows User Message\"},{\"message\":\"TestAddFollows User message Second\"}]") ) // ,
			.andExpect( jsonPath("$", hasSize( 2 ) ) )
			.andDo( print() );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user") )
			.andExpect( jsonPath("$", hasSize( 5 ) ) )
			.andExpect( jsonPath("$.length()").value( 5 )   )
			.andDo( print() );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user?type=ids") )
			.andExpect( jsonPath("$", hasSize( 5 ) ) )
			.andExpect( jsonPath("$.length()").value( 5 )   )
			.andDo( print() );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user?type=names") )
			.andExpect( jsonPath("$", hasSize( 5 ) ) )
			.andExpect( jsonPath("$.length()").value( 5 )   )
			.andDo( print() );
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println( content );
	}
	
	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getAllUsers(java.lang.String)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#addFollows(java.lang.Long, java.lang.Long[])}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#addFollows(java.lang.String, java.lang.String[])}.
	 * @throws Exception 
	 */ 
	@Test
	public void test_4_MessagesCreatedGetAllUserIdsAndAddFollows() throws Exception {	
		// Obtain list of Ids of all users
		ResultActions resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user?type=ids") )
			.andExpect( jsonPath("$", hasSize( 5 ) ) )
			.andDo( print() );
		// List of Ids of all users as String 
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		// Add to follow all users by array of Ids. 
		// It follows already one of them. Adds 3, excluding the already followed and itself
		mockMvc.perform( MockMvcRequestBuilders.post("/social/user/follow/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content) ) // ,
			.andExpect( jsonPath("$", hasSize( 3 ) ) )
			.andDo( print() );
		
		// Add to follow NON existing users by array of names. Should return empty Set
		// The content is with existing user Ids, but forwarded to the below treats them as names
		mockMvc.perform( MockMvcRequestBuilders.post("/social/user/follow?userName=First User Name")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content) ) // ,
			.andExpect( jsonPath("$", hasSize( 0 ) ) )
			.andDo( print() );
		
		// Add to follow NON Existing users by list/array of names. Should return empty Set
		mockMvc.perform( MockMvcRequestBuilders.post("/social/user/follow?userName=First User Name")
				.contentType(MediaType.APPLICATION_JSON)
				.content( "[ \"User 1\", \"User 2\", \"User 3\", \"User 4\"]" ) ) // ,
			.andExpect( jsonPath("$", hasSize( 0 ) ) )
			.andDo( print() );
		
		// Obtain all user names
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user?type=names") )
				.andExpect( jsonPath("$", hasSize( 5 ) ) )
				.andDo( print() );
		content = resultActions.andReturn().getResponse().getContentAsString();
		
		// Add to follow all users by array of names. The user follows already all of them. Empty set expected
		mockMvc.perform( MockMvcRequestBuilders.post("/social/user/follow?userName=First User Name")
				.contentType(MediaType.APPLICATION_JSON)
				.content(content) ) // ,
			.andExpect( jsonPath("$", hasSize( 0 ) ) )
			.andDo( print() );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getFollows(java.lang.Long, java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void test_5_MessagesCreatedGetFollowsLongString() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/id/1")
				.contentType(MediaType.APPLICATION_JSON) ) // ,
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );
		
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/id/1?type=ids")
				.contentType(MediaType.APPLICATION_JSON) ) // ,
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );
		
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/id/1?type=names")
				.contentType(MediaType.APPLICATION_JSON) ) // ,
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getFollows(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void test_6_MessagesCreatedGetFollowsStringString() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name")
				.contentType(MediaType.APPLICATION_JSON) ) // ,
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );
		
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name?type=ids")
				.contentType(MediaType.APPLICATION_JSON) ) // ,
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );
		
		mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name?type=names")
				.contentType(MediaType.APPLICATION_JSON) ) // ,
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );
	}
	


	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getFollows(java.lang.String, java.lang.String)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#addFollow(java.lang.Long, java.lang.Long, com.hsbc.challenge.social.user.service.AddRemove)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#addFollow(java.lang.String, java.lang.String, com.hsbc.challenge.social.user.service.AddRemove)}.
	 * @throws Exception 
	 */
	@Test
	public void test_7_GetFollowsStringStringRemoveFollow() throws Exception {
		ResultActions resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name")
				.contentType(MediaType.APPLICATION_JSON) )
			.andExpect( jsonPath("$", hasSize( 4 ) ) )
			.andDo( print() );		

		// List of Ids of all follows as String 
		String content = resultActions.andReturn().getResponse().getContentAsString();
		Integer firstIdToRemove = JsonPath.read( content, "$[ 0 ]" );		

		mockMvc.perform(  MockMvcRequestBuilders.put( "/social/user/follow/1/" + firstIdToRemove + "?flag=REMOVE") )
			.andExpect( jsonPath("$").isBoolean() )
			.andExpect( jsonPath("$").value( true ) );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name?type=names")
				.contentType(MediaType.APPLICATION_JSON) )
			.andExpect( jsonPath("$", hasSize( 3 ) ) ) // A follow less
			.andDo( print() );		
		
		// List of names of all follows as String 
		content = resultActions.andReturn().getResponse().getContentAsString();
		String nextNameToRemove = JsonPath.read( content, "$[ 0 ]" );	
		
		mockMvc.perform(  MockMvcRequestBuilders.put( "/social/user/follow?userName=First User Name&follow=" + nextNameToRemove + "&flag=REMOVE") )
			.andExpect( jsonPath("$").isBoolean() )
			.andExpect( jsonPath("$").value( true ) );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name")
				.contentType(MediaType.APPLICATION_JSON) )
			.andExpect( jsonPath("$", hasSize( 2 ) ) )
			.andDo( print() );			
	}

	/**
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#getFollows(java.lang.String, java.lang.String)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#addFollows(java.lang.Long, java.lang.Long[], com.hsbc.challenge.social.user.service.AddRemove)}.
	 * Test method for {@link com.hsbc.challenge.social.controller.UserController#addFollows(java.lang.String, java.lang.String[], com.hsbc.challenge.social.user.service.AddRemove)}.
	 * @throws Exception 
	 */
	@Test
	public void test_8_GetFollowsStringStringRemoveFollows() throws Exception {
		ResultActions resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name")
				.contentType(MediaType.APPLICATION_JSON) )
			.andExpect( jsonPath("$", hasSize( 2 ) ) )
			.andDo( print() );		

		// List of Ids of all follows as String 
		Integer firstIdToRemove = JsonPath.read( 
				resultActions.andReturn().getResponse().getContentAsString(), "$[ 0 ]" );	

		resultActions = mockMvc.perform(  MockMvcRequestBuilders.post( "/social/user/follow/1" + "?flag=REMOVE") //  
				.contentType(MediaType.APPLICATION_JSON)
				.content( "[" + firstIdToRemove + "]" )     ) 
			.andExpect( jsonPath("$", hasSize( 1 ) ) )
			.andDo( print() );
		
		resultActions = mockMvc.perform( MockMvcRequestBuilders.get("/social/user/follow/First User Name?type=names")
				.contentType(MediaType.APPLICATION_JSON) )
			.andExpect( jsonPath("$", hasSize( 1 ) ) )
			.andDo( print() );		
		// List of names of all follows as String 
		String content = resultActions.andReturn().getResponse().getContentAsString();

		resultActions = mockMvc.perform(  MockMvcRequestBuilders.post( 
				"/social/user/follow?userName=First User Name" + "&flag=REMOVE") //  
				.contentType(MediaType.APPLICATION_JSON)
				.content( content )     ) 
			.andExpect( jsonPath("$", hasSize( 1 ) ) )
			.andDo( print() );		
	}
	
}

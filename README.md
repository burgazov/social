# social
simple social networking application

# System run/start
## Windows:
### 1. Pre-requisites
- Maven
- Java JDK

### 2. Compile and run locally
From the root of your project location (e.g. C:\workspace\social>) on cmd do

C:\workspace\social> mvn clean test install

C:\workspace\social> mvn spring-boot:run
	or
C:\workspace\social> java -jar target/simple-social-network-challenge-0.0.1-SNAPSHOT.jar

# Access, use and test
## Access
Running on port 8080
http://localhost:8080/social

## Login
No user name and password required

## Use via plain web browser
Below (GETs) can be used and accessed via web browser (Chrome, IE, etc.)

http://localhost:8080/social/user - return list of all user ids

/social/user?type=ids - return list of all user ids
/social/user?type=names - return list of all user names

/social/user/id/1 - list of all published (posted) messages in reverse chronological order of user with Id 1
/social/user/User Name - list of all published (posted) messages in reverse chronological order of user with user name "User Name"

/social/user/all/id/1 - timeline, list of all follows messages in reverse chronological order of users which user with id 1 follows
/social/user/all/User Name - timeline, list of all follows messages in reverse chronological order of users which user with user name "User Name" follows

/social/user/follow/User Name - list of user Ids which user with user name "User Name" follows
/social/user/follow/User Name?type=ids - list of user Ids which user with user name "User Name" follows
/social/user/follow/User Name?type=names - list of user names which user with user name "User Name" follows

/social/user/follow/id/1 - list of user Ids which user with user Id "1" follows
/social/user/follow/id/1?type=ids - list of user Ids which user with user Id "1" follows
/social/user/follow/id/1?type=names - list of user Ids which user with user Id "1" follows

## Use API DE or API testing tool, e.g. Postman
### Method: PUT

/social/message/User name?message=User Name Message - creates message with text "User Name message" and associates it with user with name "User Name". If the user doesn't exist an user with user and display names "User name" will be created before message creation.

/social/message/User Name/User Name message - creates message with text "User Name message" and associates it with user with name "User Name". If the user doesn't exist an user with user and display names "User name" will be created before message creation.

/social/message/User Name/User Display Name/User Name message - creates message with text "User Name message" and associates it with user with name "User Name". If the user doesn't exist an user with user name "User Name" and display name "User Display Name" will be created before message creation.

/social/user/follow/1/2 - add user Id 2 into follows list of user Id 1, i.e. user 1 will follow and see all messages posted by user 2

/social/user/follow/1?follow=Follow Name - add user with name "Follow Name" into follows list of user Id 1, i.e. user 1 will follow and see all messages posted by user "Follow Name"

/social/user/follow?userName=User Name&follow=Follow Name - add user with name "Follow Name" into follows list of user "User Name", i.e. user "User Name" will follow and see all messages posted by user "Follow Name"

### Method: POST

BODY: [{ "message": "Message 1" },{ "message": "Message 2" }, { "message": "Message 3" } ]
/social/message/User Name - creates messages and associates them with user "User Name". If the user doesn't exist an user with user and display names "User name" will be created before messages creation.

BODY: [1, 2, 3, 4]
/social/user/follow/1 - add all users with Ids listed above into follows list of user Id 1, i.e. user 1 will follow and see all messages posted by users with body's Ids
/social/user/follow/1?flag=ADD - same as above, i.e. if flag omitted by default operation will be add all  
/social/user/follow/1?flag=REMOVE - same as above, but will remove them instead

BODY: ["Name 1", "Name 2", "Name 3", "Name 4"]
/social/user/follow?userName=User Name - add all user with names listed into follows list of user "User Name", i.e. user "User Name" will follow and see all messages posted by users with body's names listed
/social/user/follow?userName=User Name&flag=ADD - same as above, i.e. if flag omitted by default operation will be add all
/social/user/follow?userName=User Name&flag=REMOVE - same as above, but will remove them instead

# Cloud running instance
It will follow shortly on AWS

  



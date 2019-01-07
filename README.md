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

## Use API testing tool, e.g. Postman




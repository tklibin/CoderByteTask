1.) Run the main application as Spring boot app.

POSTMAN API calls in sequence as follows

1.)
POST http://localhost:8080/users/authenticate 

body: (JSON)
username: test
password: test

Response  - will give the jwt token


2.) Set the jwt Bearer token in the Authorization header for all subsequent requests
GET http://localhost:8080/users

3.)
GET http://localhost:8080/posts

4.)
GET http://localhost:8080/comments

5.)
GET http://localhost:8080/posts/4077/comments

6.)
GET http://localhost:8080/posts/1932/comments
# README #

This README file contanisn all the informations you need to know about our application.
You will find explaination about how to setup it, an explanation of the code we wrote.

### What is this repository for? ###

First of all, this repository contains a Java JEE web application, build by following the REST archtecture styles. And
it is managed by Maven.
Thi web app retakes the main functionality of the web app Meet Up.

Meet Up is an web app where users can create groups for event in order to meet.


### How do yo get set up ? ###

If you have Eclipse and a server on it, that's fine. You clone this repositirory and launch it on your server.
And need to be deploy on a server of your choice (Apache, Glassfish,...).

The database is written in MySQL. you have just to upload it to your MySQL server and connect with the app.

### Architecture ###

This app provides two representation. It could be whether a HTML or a Json. To get the Json, you have to change the URI by "MeetupAPI"
and the HTML representation can be accessible by "MeetupMain".

For both representation, the code behind each procedure is quite the same.

The procedure 'addUsers' which can be access through the path "/signup" enbales a user to register in the web app.
this procedure uses the HTTP verb "POST" in order to hide the credential user. It returns a Response type if the action
 was excute correctly or not.

The procedure 'updateUser' which can be access through the path "/updateUser", enbales a user to update his credentials.
this procedure uses the HTTP verb "POST" in order to get data to be processed.
It returns a Response type if the action was excute correctly or not.

The procedure 'deleteUser' which can be access through the path "/deleteUser", enbales a user to delete his credential from the database.
this procedure uses the HTTP verb "POST" in order to hide the credential user.
It returns a Response type if the action was excute correctly or not.

The procedure 'viewUser' which can be access through the path "/viewUser", enbales a user to view credential of an user through his email adress.
this procedure uses the HTTP verb "GET".
It returns a represenation of the user selected and an error message if the user does not exist in the database.

The procedure 'viewGroups' which can be access through the path "/viewGroups", enbales a user to all the group registered on the app.
this procedure uses the HTTP verb "GET".
It returns a represenation of the group list.

The procedure 'viewComments' which can be access through the path "/comments/{query}", enbales a user to see all the comments made by users inside a group.
this procedure uses the HTTP verb "GET".
It returns a represenation of the comments made in a group.

The procedure 'addComment' which can be access through the path "/addComment", enbales a user to add a comment in a group he belongs.
this procedure uses the HTTP verb "POST".
It returns a response type whether the excution was well excuted or not.


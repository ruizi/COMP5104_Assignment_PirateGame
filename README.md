# COMP5104_Assignment_PirateGame


The code is a 3 player game called Pirate.

This a Client-Server Structure project that all the three players have their own client terminal and their score board and round will be automaticlly sync by the central server.

This project use Maven to deal with the package dependiency. (Maven version is 3.6.3)
This project SDK is java version "11.0.6".

the project start by starting the game server by running the GameServer.java and the terminal will show the server started and waiting for the connection request from clients. Then start the Player.java in the package called "Client", input the player name in the terminal and started the other two players in the same way. after the initial Server-Client start and connection establish process, the game loop starts, the server will send the score board and round number to all of the three player clients, and the player will input their commend in their terminals to move to next step of the game.

Junit tests to check the game functionality use TDD approach

Import the game through IntelliJ IDEA by connecting it to the Git repository using the url. You can also download the code from Github and import the project into IntalliJ IDEA or eclipse.

How to run the code through eclipse:
1) Start the GameServer 
2) Start the Player (add three players)
3) Game starts playing with player 1 until one of the three gets more than 6000 points.


Note:For Junit tests make sure the code is built with a jUnit dependency


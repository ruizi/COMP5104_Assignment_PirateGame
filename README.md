# COMP5104_Assignment_PirateGame

Youtube Videos Links here:

---
Assignment 2:
The video for a real game loop and test suite : https://youtu.be/eg7qiD4aVDE

The video for LEVEL 1a: BASIC DYING and SCORING : https://youtu.be/tFL8w2W859o
 
(Row59: https://youtu.be/8PQSyxxpQ_E)

The video for LEVEL 1b: ADVANCED SCORING : https://youtu.be/7ABIPG-i9go

The video for LEVEL 2:  NETWORKED GAME TEST : https://youtu.be/3Nxe-pPB0FE

---
Assignment 1:

The video for the first two short game demo:https://youtu.be/zsRLCMrP6sg

A real game loop and the test suite demo:https://youtu.be/M1RDeX2QK3Y

Test Case for Part1:https://youtu.be/eIHPqJQZTME

Test Case for Part2:https://youtu.be/fef1mQ_W468

---
The code is a 3 player game called Pirate.

This a Client-Server Structure project that all the three players have their own client terminal and their score board and round will be automatically sync by the central server.

This project use Maven to deal with the package dependency. (Maven version is 3.6.3)
This project SDK is java version "11.0.6".
Junit version would be 4.12

the project starts by starting the game server by running the GameServer.java, and the terminal will show the server started and waiting for the connection request from clients. Then start the Player.java in the package called "Client", input the player name in the terminal and started the other two players in the same way. after the initial Server-Client start and connection establish process, the game loop starts, the server will send the score board and round number to all the three player clients, and the player will input their commend in their terminals to move to next step of the game.

Junit tests to check the game functionality use TDD approach

Import the game through IntelliJ IDEA by connecting it to the Git repository using the url. You can also download the code from Github and import the project into IntelliJ IDEA or eclipse.

How to run the code through eclipse or IntelliJ IDEA:
1) Start the GameServer 
2) Start the Player (add three players)
3) Game starts playing with player 1 until one of the three gets more than 6000 points.


Note:For Junit tests make sure the code is built with a jUnit dependency


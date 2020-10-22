@Level2
Feature: LEVEL 2:  NETWORKED GAME TEST (20 points)

  @row126
  Scenario: game starts, each player plays a turn with scores being updated correctly
  for each player (see playRound feature in AnotherYahtzeeGame)
    Given the game server initialed
    And the player 1 input his name as "player 1"
    And the player 1 connected to server
    And the player 2 input his name as "player 2"
    And the player 2 connected to server
    And the player 3 input his name as "player 3"
    And the player 3 connected to server
    And the player 1 received players from server
    And the player 2 received players from server
    And the player 3 received players from server
    And the Server start the game loop
    When the player 1 received round num from server
    And the player 1 received score board from server
    And the player 1 draw a "Diamond" card
    And the player 1 got "sword, sword, sword, coin, coin, coin, coin, coin" on his first roll
    And the player 1 input 2 for menu input
    And the player 1 chose to hold "4,5,6,7,8 "
    And the player 1 get "coin,coin,coin " after re-roll
    And the player 1 input 1 for menu input
    And the player 1 send his score to server

    And the player 2 received round num from server
    And the player 2 received score board from server
    And the player 2 draw a "Monkey Business" card
    And the player 2 got "sword, sword, monkey, coin, coin, parrot, parrot, parrot" on his first roll
    And the player 2 input 1 for menu input
    And the player 2 send his score to server

    And the player 3 received round num from server
    And the player 3 received score board from server
    And the player 3 draw a "Two Skull" card
    And the player 3 got "sword, sword, monkey, coin, coin, parrot, skull, skull" on his first roll
    And the player 3 input 1 for menu input
    And the player 3 send his score to server

    Then end the server
    
  @row128
  Scenario: row128: game starts, players play turns until a winner is declared
    Given the game server initialed
    And the player 1 input his name as "player 1"
    And the player 1 connected to server
    And the player 2 input his name as "player 2"
    And the player 2 connected to server
    And the player 3 input his name as "player 3"
    And the player 3 connected to server
    And the player 1 received players from server
    And the player 2 received players from server
    And the player 3 received players from server
    And the Server start the game loop
    When the player 1 received round num from server
    And the player 1 received score board from server
    And the player 1 draw a "Three Sabre" card
    And the player 1 got "sword, sword, sword, coin, coin, coin, coin, coin" on his first roll
    And the player 1 input 2 for menu input
    And the player 1 chose to hold "4,5,6,7,8 "
    And the player 1 get "sword,coin,coin " after re-roll
    And the player 1 input 1 for menu input
    And the player 1 send his score to server

    And the player 2 received round num from server
    And the player 2 received score board from server
    And the player 2 draw a "Monkey Business" card
    And the player 2 got "sword, sword, monkey, coin, coin, parrot, parrot, parrot" on his first roll
    And the player 2 input 1 for menu input
    And the player 2 send his score to server

    And the player 3 received round num from server
    And the player 3 received score board from server
    And the player 3 draw a "Two Sabre" card
    And the player 3 got "sword, sword, monkey, coin, coin, parrot, skull, skull" on his first roll
    And the player 3 input 1 for menu input
    And the player 3 send his score to server

    And the player 1 received round num from server
    And the player 1 received score board from server
    And the player 1 draw a "Monkey Business" card
    And the player 1 got "sword, sword, sword, parrot, parrot, parrot, coin, coin" on his first roll
    And the player 1 input 2 for menu input
    And the player 1 chose to hold "4,5,6,7,8 "
    And the player 1 get "monkey,monkey,monkey " after re-roll
    And the player 1 input 1 for menu input
    And the player 1 send his score to server

    And the player 2 received round num from server
    And the player 2 received score board from server
    And the player 2 draw a "Gold" card
    And the player 2 got "sword, sword, sword, coin, coin, coin, parrot, parrot" on his first roll
    And the player 2 input 1 for menu input
    And the player 2 send his score to server

    And the player 3 received round num from server
    And the player 3 received score board from server
    And the player 3 draw a "Diamond" card
    And the player 3 got "diamond, diamond, sword, coin, coin, coin, parrot, parrot" on his first roll
    And the player 3 input 1 for menu input
    And the player 3 send his score to server

    And the player 1 received round num from server
    And the player 1 received score board from server
    And the player 1 draw a "Diamond" card
    And the player 1 got "sword, sword, sword, coin, coin, coin, coin, coin" on his first roll
    And the player 1 input 2 for menu input
    And the player 1 chose to hold "4,5,6,7,8 "
    And the player 1 get "coin,coin,coin " after re-roll
    And the player 1 input 1 for menu input
    And the player 1 send his score to server

    And the player 2 received round num from server
    And the player 2 received score board from server
    And the player 2 draw a "Gold" card
    And the player 2 got "sword, sword, sword, coin, coin, coin, parrot, parrot" on his first roll
    And the player 2 input 1 for menu input
    And the player 2 send his score to server

    And the player 3 received round num from server
    And the player 3 received score board from server
    And the player 3 draw a "Diamond" card
    And the player 3 got "diamond, diamond, sword, coin, coin, coin, parrot, parrot" on his first roll
    And the player 3 input 1 for menu input
    And the player 3 send his score to server


    And the player 1 received round num from server
    And the player 2 received round num from server
    And the player 3 received round num from server

    Then the player 1 received the winner info
    And the player 2 received the winner info
    And the player 3 received the winner info



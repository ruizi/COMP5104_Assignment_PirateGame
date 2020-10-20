package com.rui.pirate.cucumber;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Connection;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
import com.rui.pirate.Server.GameServer;
import com.rui.pirate.cucumber.helper.MockedServer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class MyStepDefsForLevel2 {
    public Player[] players = new Player[3];
    GameService game = new GameService(true);
    MockedServer mockedServer;
    private int[] scoreBoard = new int[3];
    Card card;
    Connection clientConnection;
    String input = "";
    int round;
    String[] dieRoll;
    GameServer gameServer;

    @Given("the game server initialed")
    public void the_game_server_initialed() throws ClassNotFoundException {
        mockedServer = new MockedServer();
        mockedServer.start();
    }

    @Given("the player {int} input his name as {string}")
    public void the_player_input_his_name_as(Integer playerID, String name) {
        players[playerID - 1] = new Player(name);
        players[playerID - 1].initializePlayers();
    }

    @Given("the Server start the game loop")
    public void the_Server_start_the_game_loop() {
    }


    @When("the player {int} choose {int}")
    public void the_player_choose(Integer playerID, Integer menuChoice) {
    }

    @When("the player {int} start his turn")
    public void the_player_start_his_turn(Integer playerID) {
        GameService game0 = new GameService(true);
        GameService game1 = new GameService(true);
        GameService game2 = new GameService(true);
        input += 1;
        System.out.println(input);
        Scanner scanner = setTestInputScanner(input);
        game0.setScanner(scanner);
        players[0].startGame(game0);
        System.out.println("xxx");
        System.out.println(input);
        scanner = setTestInputScanner(input);
        game1.setScanner(scanner);
        players[1].startGame(game1);
        players[2].startGame(game2);

    }

    @When("the player {int} connected to server")
    public void the_player_connected_to_server(Integer playerId) {
        players[playerId - 1].connectToClient();
    }

    @When("the player {int} received players from server")
    public void the_player_received_players_from_server(Integer playerId) {
        players[playerId - 1].setPlayers(players[playerId - 1].getClientConnection().receivePlayer());
        //System.out.println(players[playerId - 1].getPlayers()[1].name);
    }

    @When("the player {int} received round num from server")
    public void the_player_received_round_num_from_server(Integer playerId) {
        input = "";
        //round = players[playerId - 1].getClientConnection().receiveRoundNo();//receive the round num from server.
        players[playerId - 1].setRound(players[playerId - 1].getClientConnection().receiveRoundNo());
        if (players[playerId - 1].getRound() != -1) {
            System.out.println("\n \n \n **********Round Number " + players[playerId - 1].getRound() + " / Player:" + playerId + "**********");
        } else {
            System.out.println("For Player " + playerId + ": winner is born, game ends");
        }

    }

    @When("the player {int} received score board from server")
    public void the_player_received_score_board_from_server(Integer playerId) {
        scoreBoard = players[playerId - 1].getClientConnection().receiveScoreBoard();//receive the latest score board from server
        players[playerId - 1].setScoreBoard(scoreBoard);
        game.printPlayerScores(players, scoreBoard); //print the score board.
    }

    @When("the player {int} draw a fortune card")
    public void the_player_draw_a_fortune_card(Integer playerId) {
        String fortuneCard = game.drawFortuneCard();
        System.out.println(fortuneCard);
        card = new Card(fortuneCard);
        System.out.println("| CARD ===> " + card.getName());
    }

    @When("the player {int} draw a {string} card")
    public void the_player_draw_a_card(Integer int1, String fortuneCard) {
        card = new Card(fortuneCard.trim());
        System.out.println("| CARD ===> " + card.getName());
    }

    @When("the player {int} get the initial dieRoll")
    public void the_player_get_the_initial_dieRoll(Integer playerId) {
        dieRoll = game.rollDice();
    }


    @When("the player {int} got {string} on his first roll")
    public void the_player_got_on_his_first_roll(Integer int1, String initRoll) {
        dieRoll = initRoll.trim().split(", ");
    }

    @When("the player {int} chose to hold {string}")
    public void the_player_chose_to_hold(Integer int1, String hold) {
        input += hold;
        System.out.println(input);
    }

    @When("the player {int} get {string} after re-roll")
    public void the_player_get_after_re_roll(Integer int1, String riggedReRoll) {
        input += riggedReRoll.trim().replaceAll(" ", "") + " ";
    }

    @When("the player {int} input {int} for menu input")
    public void the_player_input_for_menu_input(Integer playerId, Integer menuInput) {
        input = "";
        input += menuInput + " ";
    }

    @When("the player {int} send his score to server")
    public void the_player_send_his_score_to_server(Integer playerId) {
        Scanner scanner = setTestInputScanner(input);
        game.setScanner(scanner);
        int roundScore = players[playerId - 1].playerRound(card, dieRoll, game);
        game.printPlayerScores(players, players[playerId - 1].getScoreBoard());
        players[playerId - 1].getClientConnection().sendScores(players[playerId - 1].getScoreBoard());//send the edited score board to the server.
    }


    public Scanner setTestInputScanner(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());//可以通过调用System.setIn（InputStream in）来用自己的流替换System.in。InputStream可以是一个字节数组
        return new Scanner(in);
    }


    @Then("the player {int} received the winner info")
    public void thePlayerReceivedTheWinnerInfo(int playerId) {
        System.out.println();
        System.out.println("*********For Player " + playerId + "**********");
        players[playerId - 1].setScoreBoard(players[playerId - 1].getClientConnection().receiveScoreBoard());
        System.out.println("The Final Score Board：");
        game.printPlayerScores(players, players[playerId - 1].getScoreBoard());
        int winnerID = players[playerId - 1].getClientConnection().receiveWinnerID();//received the winner ID from the server.
        if (players[playerId - 1].getPlayerId() - 1 == winnerID) {
            System.out.println("You win!");
        } else {
            System.out.println("The winner is " + players[winnerID].name);
        }
        System.out.println("Game over!");
    }

    @Then("end the server")
    public void endTheServer() throws IOException {
        mockedServer.gameServer.closeSS();
    }
}

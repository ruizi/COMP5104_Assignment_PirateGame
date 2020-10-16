package com.rui.pirate.cucumber;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class MyStepDefs {
    public final Player player = new Player("Test");
    GameService game;
    Card card;
    String[] dieRoll;
    String input = "";
    boolean isAllowedReRoll = false;
    ArrayList<Integer> held = new ArrayList<>();
    int playerInitScore;

    public Scanner setTestInputScanner(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());//可以通过调用System.setIn（InputStream in）来用自己的流替换System.in。InputStream可以是一个字节数组
        return new Scanner(in);
    }

    @Given("the game service started")
    public void the_game_service_started() {
        game = new GameService(true);
    }

    @Given("the fortune card is {string}")
    public void theFortuneCardIs(String fortuneCard) {
        card = new Card(fortuneCard.trim());
    }

    @Given("all player has a initial score of {int}")
    public void all_player_has_a_initial_score_of(Integer initial_score) {
        player.setScoreBoardForTest(initial_score);
        playerInitScore = initial_score;
    }

    @When("the player got {string} on first roll")
    public void the_player_got_on_first_roll(String initRoll) {
        dieRoll = initRoll.trim().split(", ");
    }

    @Then("run the play round")
    public void run_the_play_round() {
        Scanner scanner = setTestInputScanner(input);
        game.setScanner(scanner);
        int roundScore = player.playerRound(card, dieRoll, game);
        assertEquals(0, roundScore);
    }

    @Then("interface reports death & end of turn")
    public void interface_reports_death_end_of_turn() {
        System.out.println("=====End of turn Check====");
        assertFalse(player.ticketStatus());
    }

    @When("the player chose {int}")
    public void the_player_chose_and_hold(Integer menuChoice) {
        input += menuChoice + " ";
    }

    @When("the player chose {int} for menuChoice")
    public void the_player_chose_for_menuChoice(Integer menuChoice) {
        input += menuChoice + " ";
    }

    @When("the player chose {string} in treasure chest")
    public void the_player_chose_in_treasure_chest(String choiceInChest) {
        input += choiceInChest + " ";
    }

    @When("the player chose {int} for treasure chest menu")
    public void the_player_chose_for_treasure_chest_menu(Integer treasureMenuChoice) {
        input += treasureMenuChoice + " ";
    }

    @When("the player get back {int}")
    public void the_player_get_back(Integer getBack) {
        input += getBack + " ";

    }

    @When("the player hold {string}")
    public void the_player_hold(String hold) {
        input += hold;
        String[] split = hold.trim().split(",");
        for (String s : split) {
            held.add(Integer.parseInt(s));
        }
    }

    @When("set re-roll with {string}")
    public void set_re_roll_with(String riggedReRoll) {
        //System.out.println(riggedReRoll);
        input += riggedReRoll.trim().replaceAll(" ", "") + " ";
    }

//    @Then("player chooses to score board with {int}")
//    public void player_chooses_to_score_board_with(Integer Score) {
//        input += 1;
//        Scanner scanner = setTestInputScanner(input);
//        game.setScanner(scanner);
//        int ExpectedScore = Score;
//        assertEquals(ExpectedScore, player.playerRound(card, dieRoll, game));
//    }

    @Then("execute the sorceress card with {int} skull")
    public void execute_the_sorceress_card_with_skull(Integer skullNum) {
        Scanner scanner = setTestInputScanner(input);
        game.setScanner(scanner);
        game.locateSkull(dieRoll);
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        int oriSkullNum = skullNum;
        int afterSkullNum = skullNum - 1;
        assertEquals(oriSkullNum, skullDice.size());
        card.sorceress.sorceressCard(skullDice, dieRoll, game);
        game.printDieRoll(dieRoll);
        assertTrue(card.sorceress.isUsed());
        assertEquals(afterSkullNum, skullDice.size());
        System.out.println("=====isDie Check====");
        assertFalse(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Then("execute the sorceress card")
    public void execute_the_sorceress_card() {
        Scanner scanner = setTestInputScanner(input);
        game.setScanner(scanner);
        game.locateSkull(dieRoll);
        System.out.println(input);
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        assertEquals(2, skullDice.size());
        card.sorceress.sorceressCard(skullDice, dieRoll, game);
        game.printDieRoll(dieRoll);
        assertTrue(card.sorceress.isUsed());
        assertEquals(1, skullDice.size());
        System.out.println("=====isDie Check====");
        assertFalse(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @When("try to re-roll")
    public void try_to_re_roll() {
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        ArrayList<Integer> heldDice = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        ArrayList<Integer> treasureChest = new ArrayList<>();
        isAllowedReRoll = game.heldDiceValidCheck(skullDice, heldDice, treasureChest);
        System.out.println(isAllowedReRoll);
    }

    @When("go to next round")
    public void go_to_next_round() {
        System.out.println(Arrays.toString(input.split(" ")));
        input = input.split(" ")[1] + " ";
        System.out.println(input);
    }

    @When("roll one skull on the next round")
    public void roll_one_skull_on_the_next_round() {
        //input = "skull,sword,sword " + "6 " + "1,2,3,4,5 " + "monkey,sword,sword ";
        input = "skull,sword,sword ";
        Scanner scanner = setTestInputScanner(input);//System.lineSeparator();
        game = new GameService(true, scanner);
        ArrayList<Integer> held = new ArrayList<>(List.of(0, 1, 2, 3, 4));
        ArrayList<Integer> treasureChest = new ArrayList<>();
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        dieRoll = game.reRollNotHeld(dieRoll, held, skullDice, treasureChest);
        game.printDieRoll(dieRoll);
        input = "";
    }

    @Then("interface reports not allowed")
    public void interface_reports_not_allowed() {
        assertFalse(isAllowedReRoll);
        System.out.println("=====reRoll valid check====");
        System.out.println("Allow to reRoll:" + isAllowedReRoll);
    }

    @Then("interface reports the end of turn is false")
    public void interface_reports_the_end_of_turn_is_false() {
        System.out.println("=====isDie Check====");
        assertFalse(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Then("check the score board with {int} for other players")
    public void check_the_score_board_with_for_other_players(Integer changePoints) {
        assertEquals(playerInitScore, player.getScoreBoardByID(1));
        assertEquals(Math.max(playerInitScore + changePoints, 0), player.getScoreBoardByID(2));
        assertEquals(Math.max(playerInitScore + changePoints, 0), player.getScoreBoardByID(3));
        System.out.println("Player   |\t" + "Init " + "\t|\tchange " + "\t|\tAfter");
        System.out.println("Player 1 |\t" + playerInitScore + " \t|\t" + "0 \t" + " \t|\t" + player.getScoreBoardByID(1));
        System.out.println("Player 2 |\t" + playerInitScore + " \t|\t" + changePoints + " \t|\t" + player.getScoreBoardByID(2));
        System.out.println("Player 3 |\t" + playerInitScore + " \t|\t" + changePoints + " \t|\t" + player.getScoreBoardByID(3));
    }

//    @Then("check the score board with {int} for the player")
//    public void check_the_score_board_with_for_the_player(Integer changePoints) {
//        assertEquals(Math.max(playerInitScore + changePoints, 0), player.getScoreBoardByID(1));
//        System.out.println("Player   |\t" + "Init " + "\t|\tchange " + "\t|\tAfter");
//        System.out.println("Player 1 |\t" + playerInitScore + " \t|\t" + changePoints + " \t|\t" + player.getScoreBoardByID(1));
//    }

    @Then("player chooses to score board with expected score {int}")
    public void player_chooses_to_score_board_with_expected_score(Integer Score) {
        input += 1;
        Scanner scanner = setTestInputScanner(input);
        game.setScanner(scanner);
        int ExpectedScore = Score;
        assertEquals(ExpectedScore, player.playerRound(card, dieRoll, game));
    }

    @Then("check the score board with expected score {int} for the player")
    public void check_the_score_board_with_expected_score_for_the_player(Integer changePoints) {
        assertEquals(Math.max(playerInitScore + changePoints, 0), player.getScoreBoardByID(1));
        System.out.println("Player   |\t" + "Init " + "\t|\tchange " + "\t|\tAfter");
        System.out.println("Player 1 |\t" + playerInitScore + " \t|\t" + changePoints + " \t|\t" + player.getScoreBoardByID(1));
    }

}

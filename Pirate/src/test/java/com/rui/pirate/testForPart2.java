package com.rui.pirate;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
import com.rui.pirate.Server.GameServer;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class testForPart2 {

    public final Player player = new Player("Test");
    public String fortuneCard = "Gold";
    GameService game = new GameService(true);

    @Test
    public void testForRow80() { //roll 2 skulls, re-roll one of them due to sorceress, then go to next round of turn。
        fortuneCard = "Sorceress";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"coin", "parrot", "parrot", "parrot", "parrot", "sword", "skull", "skull"};
        String input = "7 " + "1,2,3,4,5 " + "sword,sword ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        assertEquals(2, skullDice.size());
        card.sorceress.sorceressCard(skullDice, dieRoll, game);
        assertTrue(card.sorceress.isUsed());
        assertEquals(1, skullDice.size());
        System.out.println("=====isDie Check====");
        assertFalse(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow81() { //roll no skulls, then next round roll 1 skull and re-roll for it, then score
        fortuneCard = "Sorceress";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"coin", "parrot", "parrot", "parrot", "parrot", "sword", "sword", "sword"};
        String input = "2 " + "1,2,3,4,5 " + "skull,sword,sword " + "3 " + "6 " + "1,2,3,4,5 " + "monkey,sword,sword " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(300, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow82() { //roll no skulls, then next round roll 1 skull and re-roll for it, then go to next round
        fortuneCard = "Sorceress";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"coin", "parrot", "parrot", "parrot", "parrot", "sword", "sword", "sword"};
        String input = "skull,sword,sword " + "6 " + "1,2,3,4,5 " + "monkey,sword,sword ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        ArrayList<Integer> held = new ArrayList<>(List.of(0, 1, 2, 3, 4));
        ArrayList<Integer> treasureChest = new ArrayList<>();
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        game.reRollNotHeld(dieRoll, held, skullDice, treasureChest);
        game.printDieRoll(dieRoll);
        card.sorceress.sorceressCard(skullDice, dieRoll, game);//新一轮的投掷情况
        skullDice = game.locateSkull(dieRoll);
        game.printDieRoll(dieRoll);
        assertEquals(0, skullDice.size());
        System.out.println("=====isDie Check====");
        assertFalse(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow85() {//first roll gets 3 monkeys 3 parrots  1 skull 1 coin  SC = 1100  (i.e., sequence of of 6 + coin)
        fortuneCard = "Monkey Business";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "parrot", "parrot", "parrot", "skull", "coin"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1100, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow86() {//over several rolls: 2 monkeys, 1 parrot, 2 coins, 1 diamond, 2 swords         SC 400
        fortuneCard = "Monkey Business";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "sword", "parrot", "parrot", "parrot", "coin", "coin"};
        String input = "2 " + "1,2,7,8 " + "sword,sword,parrot,parrot " + "2 " + "1,2,3,4,7,8 " + "parrot,diamond " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(400, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow87() {//over several rolls get 3 monkeys, 4 parrots, 1 sword    SC 2000 (ie seq of 7)
        fortuneCard = "Monkey Business";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "sword", "parrot", "parrot", "parrot", "coin", "coin"};
        String input = "2 " + "1,2,7,8 " + "monkey,parrot,parrot,parrot " + "2 " + "1,2,3,4,5,6 " + "parrot,sword " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(2000, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow90() {//roll 3 parrots, 2 swords, 2 diamonds, 1 coin    put 2 diamonds and 1 coin in chest
        //  then re-roll 2 swords and get 2 parrots put 5 parrots in chest and take out 2 diamonds & coin
        //  then re-roll the 3 dice and get 1 skull, 1 coin and a parrot  score 6 parrots + 1 coin for 1100 points
        fortuneCard = "Treasure Chest";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"parrot", "parrot", "parrot", "sword", "sword", "diamond", "diamond", "coin"};
        String input = "3 " + "1 " + "6,7,8 " + "0 " + "1,2,3 " + "parrot,parrot " + "3 " + "1 " + "1,2,3,4,5 " + "2 " + "6,7,8 " + "0 " + "0 " + "skull,coin,parrot " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1100, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow95() {//roll 2 skulls, 3 parrots, 3 coins   put 3 coins in chest
        //  then re-roll 3 parrots and get 2 diamonds 1 coin    put coin in chest (now 4)
        //  then re-roll 2 diamonds and get 1 skull 1 coin     SC for chest only = 400 + 200 = 600
        //  also interface reports death & end of turn
        fortuneCard = "Treasure Chest";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "skull", "parrot", "parrot", "parrot", "coin", "coin", "coin"};
        String input = "3 " + "1 " + "6,7,8 " + "0 " + "0 " + "diamond,diamond,coin " + "3 " + "1 " + "5 " + "0 " + "0 " + "skull,coin " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(600, player.playerRound(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow101() {//3 monkeys, 3 swords, 1 diamond, 1 parrot FC: coin   => SC 400  (ie no bonus)
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "sword", "diamond", "parrot"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(400, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow102() {//3 monkeys, 3 swords, 2 coins FC: captain   => SC (100+100+200+500)*2 =  1800
        fortuneCard = "Captain";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "sword", "coin", "coin"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1800, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow103() {//3 monkeys, 4 swords, 1 diamond, 1 coin FC: coin   => SC 1100  (ie 600+bonus)
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "sword", "sword", "diamond"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1000, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow104() {//FC: 2 sword sea battle, first  roll:  4 monkeys, 1 sword, 2 parrots and a coin
        //    then re-roll 2 parrots and get coin and 2nd sword
        //     score is: 200 (coins) + 200 (monkeys) + 300 (swords of battle) + 500 (full chest) = 1200
        fortuneCard = "Two Sabre";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "monkey", "sword", "parrot", "parrot", "coin"};
        String input = "2 " + "1,2,3,4,5,8 " + "coin,sword " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1200, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow107() {//FC: monkey business and RTS: 2 monkeys, 1 parrot, 2 coins, 3 diamonds   SC 1200 (bonus)
        fortuneCard = "Monkey Business";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "diamond", "diamond", "diamond"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1200, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow110() {//die by rolling one skull and having a FC with two skulls
        fortuneCard = "Two Skull";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "diamond", "diamond", "skull"};
        String input = "";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(0, player.playerRound(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow111() {//die by rolling 2 skulls and having a FC with 1 skull
        fortuneCard = "One Skull";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "diamond", "skull", "skull"};
        String input = "";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(0, player.playerRound(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow112() {//roll 2 skulls AND have a FC with two skulls: roll 2 skulls next roll, then 1 skull => -700
        fortuneCard = "Two Skull";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "diamond", "skull", "skull"};
        String input = "2 " + "skull,skull,parrot,coin,coin,diamond " + "2 " + "skull,coin,coin,diamond " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-700, player.playerRound(card, dieRoll, game));
        assertEquals(1000, player.getScoreBoardByID(1));
        assertEquals(300, player.getScoreBoardByID(2));
        assertEquals(300, player.getScoreBoardByID(3));
    }

    @Test
    public void testForRow113() {//roll 3 skulls AND have a FC with two skulls: roll no skulls next roll  => -500
        fortuneCard = "Two Skull";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "skull", "skull", "skull"};
        String input = "2 " + "monkey,monkey,parrot,coin,coin ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-500, player.playerRound(card, dieRoll, game));
        assertEquals(1000, player.getScoreBoardByID(1));
        assertEquals(500, player.getScoreBoardByID(2));
        assertEquals(500, player.getScoreBoardByID(3));
    }

    @Test
    public void testForRow114() {//roll 3 skulls AND have a FC with 1 skull: roll 1 skull next roll then none => -500
        fortuneCard = "One Skull";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "skull", "skull", "skull"};
        String input = "2 " + "monkey,monkey,parrot,coin,skull " + "2 " + "monkey,monkey,parrot,coin ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-500, player.playerRound(card, dieRoll, game));
        assertEquals(1000, player.getScoreBoardByID(1));
        assertEquals(500, player.getScoreBoardByID(2));
        assertEquals(500, player.getScoreBoardByID(3));
    }

    @Test
    public void testForRow115() {//show deduction received cannot make your score negative
        fortuneCard = "One Skull";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(300);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "skull", "skull", "skull"};
        String input = "2 " + "monkey,monkey,parrot,coin,skull " + "2 " + "monkey,monkey,parrot,coin ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-500, player.playerRound(card, dieRoll, game));
        assertEquals(300, player.getScoreBoardByID(1));
        assertEquals(0, player.getScoreBoardByID(2));
        assertEquals(0, player.getScoreBoardByID(3));
    }


    @Test
    public void testForRow118() {//FC 2 swords, die on first roll   => lose 300 points
        fortuneCard = "Two Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "skull", "skull", "skull"};
        String input = "";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-300, player.playerRound(card, dieRoll, game));
        assertEquals(700, player.getScoreBoardByID(1));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow119() {//FC 3 swords, die on first roll   => lose 500 points
        fortuneCard = "Three Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "skull", "skull", "skull"};
        String input = "";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-500, player.playerRound(card, dieRoll, game));
        assertEquals(500, player.getScoreBoardByID(1));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow120() {//FC 4 swords, die on first roll   => lose 1000 points
        fortuneCard = "Four Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(800);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "skull", "skull", "skull"};
        String input = "";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-1000, player.playerRound(card, dieRoll, game));
        assertEquals(0, player.getScoreBoardByID(1));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow122() {//FC 2 swords, roll 3 monkeys 2 swords, 1 coin, 2 parrots  SC = 100 + 100 + 300 = 500
        fortuneCard = "Two Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "coin", "parrot", "parrot"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(500, player.playerRound(card, dieRoll, game));
        assertEquals(1500, player.getScoreBoardByID(1));
    }

    @Test
    public void testForRow123() {//FC 2 swords, roll 4 monkeys 1 sword, 1 skull  2 parrots
        //  then re-roll 2 parrots and get 1 sword and 1 skull   SC = 200 +  300 = 500
        fortuneCard = "Two Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "monkey", "sword", "skull", "parrot", "parrot"};
        String input = "2 " + "1,2,3,4,5 " + "sword,skull " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(500, player.playerRound(card, dieRoll, game));
        assertEquals(1500, player.getScoreBoardByID(1));
    }

    @Test
    public void testForRow125() {//FC 3 swords, roll 3 monkeys 4 swords  SC = 100 + 200 + 500 = 800
        fortuneCard = "Three Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "sword", "sword", "parrot"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(800, player.playerRound(card, dieRoll, game));
        assertEquals(1800, player.getScoreBoardByID(1));
    }

    @Test
    public void testForRow126() {//FC 3 swords, roll 4 monkeys 2 swords 2 skulls
        //     then re-roll 4 monkeys and get  2 skulls and 2 swords   -> DIE
        fortuneCard = "Three Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "monkey", "sword", "sword", "skull", "skull"};
        String input = "2 " + "5,6 " + "skull,skull,sword,sword ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(-500, player.playerRound(card, dieRoll, game));
        assertEquals(500, player.getScoreBoardByID(1));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow128() {//FC 4 swords, roll 3 monkeys 4 swords 1 skull  SC = 100 +200 + 1000 = 1300
        fortuneCard = "Four Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "sword", "sword", "skull"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1300, player.playerRound(card, dieRoll, game));
        assertEquals(2300, player.getScoreBoardByID(1));
    }

    @Test
    public void testForRow129() {//FC 4 swords, roll 3 monkeys, 1 sword, 1 skull, 1 diamond, 2 parrots
        //   then re-roll 2 parrots and get 2 swords thus you have 3 monkeys, 3 swords, 1 diamond, 1 skull
        //   then re-roll 3 monkeys and get  1 sword and 2 parrots  SC = 200 + 100 + 1000 = 1300
        fortuneCard = "Four Sabre";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(1000);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "skull", "diamond", "parrot", "parrot"};
        String input = "2 " + "1,2,3,4,6 " + "sword,sword " + "2 " + "4,6,7,8 " + "sword,parrot,parrot " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1300, player.playerRound(card, dieRoll, game));
        assertEquals(2300, player.getScoreBoardByID(1));
    }

    @Test
    public void testForRow40() {
        fortuneCard = "Captain";
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(5500);
        Player one = new Player("One");
        Player two = new Player("Two");
        Player three = new Player("Three");

        String[] dieRoll3 = new String[]{"sword", "sword", "sword", "sword", "sword", "sword", "sword", "sword"};
        String[] dieRoll2 = new String[]{"sword", "sword", "sword", "parrot", "parrot", "monkey", "monkey", "skull"};
        String[] dieRoll1 = new String[]{"sword", "sword", "sword", "parrot", "parrot", "skull", "skull", "skull"};


        String inputForOne = "1 ";
        Scanner scanner = setTestInputScanner(inputForOne);
        game = new GameService(true, scanner);
        int scoreOne = one.playerRound(card, dieRoll1, game);

        String inputForTwo = "1 ";
        scanner = setTestInputScanner(inputForTwo);
        game = new GameService(true, scanner);
        int scoreTwo = two.playerRound(card, dieRoll2, game);

        String inputForThree = "1 ";
        scanner = setTestInputScanner(inputForThree);
        game = new GameService(true, scanner);
        int scoreThree = three.playerRound(card, dieRoll3, game);

        GameServer gameServer = new GameServer();
        gameServer.setPlayers(new Player[]{one, three, three});
        gameServer.updateScoreBoardByID(1, scoreOne);
        gameServer.updateScoreBoardByID(2, scoreTwo);
        gameServer.updateScoreBoardByID(3, scoreThree);
        assertTrue(gameServer.isEnd());
        gameServer.printPlayerScores();
        assertEquals(3, gameServer.getWinner() + 1);
    }

    @Test
    public void testForRow43() {
        Card card = new Card(fortuneCard);
        player.setScoreBoardForTest(0);
        Player one = new Player("One");
        Player two = new Player("Two");
        Player three = new Player("Three");

        String[] dieRoll1 = new String[]{"sword", "sword", "sword", "parrot", "parrot", "parrot", "coin", "skull"};
        String[] dieRoll2 = new String[]{"sword", "sword", "sword", "coin", "coin", "coin", "coin", "coin"};
        String[] dieRoll3 = new String[]{"sword", "sword", "sword", "sword", "parrot", "parrot", "skull", "skull"};

        String inputForOne = "1 " + "1 ";
        Scanner scanner = setTestInputScanner(inputForOne);
        game = new GameService(true, scanner);
        int scoreOneRound1 = one.playerRound(card, dieRoll1, game);
        int scoreOneRound2 = one.playerRound(card, dieRoll1, game);
        System.out.println("=============================");
        System.out.println("Player One finished. Got " + scoreOneRound1 + " in first round." + " Got " + scoreOneRound2 + " in the second round.");
        System.out.println("=============================");

        String inputForTwo = "1 " + "2 " + "1,2,3 " + "sword,sword,sword,sword,sword " + "1 ";
        scanner = setTestInputScanner(inputForTwo);
        game = new GameService(true, scanner);
        int scoreTwoRound1 = two.playerRound(card, dieRoll2, game);
        int scoreTwoRound2 = one.playerRound(card, dieRoll2, game);
        System.out.println("=============================");
        System.out.println("Player Two finished. Got " + scoreTwoRound1 + " in first round." + " Got " + scoreTwoRound2 + " in the second round.");
        System.out.println("=============================");

        String inputForThree = "1 " + "1 ";
        scanner = setTestInputScanner(inputForThree);
        game = new GameService(true, scanner);
        int scoreThreeRound1 = three.playerRound(card, dieRoll3, game);
        int scoreThreeRound2 = three.playerRound(card, dieRoll3, game);
        System.out.println("=============================");
        System.out.println("Player Three finished. Got " + scoreThreeRound1 + " in first round." + " Got " + scoreThreeRound2 + " in the second round.");
        System.out.println("=============================");

        GameServer gameServer = new GameServer();
        gameServer.setPlayers(new Player[]{one, two, three});
        gameServer.updateScoreBoardByID(1, scoreOneRound1 + scoreOneRound2);
        gameServer.updateScoreBoardByID(2, scoreTwoRound1 + scoreTwoRound2);
        gameServer.updateScoreBoardByID(3, scoreThreeRound1 + scoreThreeRound2);
        assertTrue(gameServer.isEnd());
        gameServer.printPlayerScores();
        assertEquals(2, gameServer.getWinner() + 1);
    }

    public Scanner setTestInputScanner(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());//可以通过调用System.setIn（InputStream in）来用自己的流替换System.in。InputStream可以是一个字节数组
        return new Scanner(in);
    }
}

package com.rui.pirate;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.*;

public class testForPart1 {
    public final Player player = new Player("Test");
    public String fortuneCard = "Gold";
    GameService game = new GameService(true);

    @Test
    public void testForRow48() {//die with 3 skulls on first roll  -> interface reports death & end of turn
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "skull", "skull", "coin", "diamond", "diamond", "diamond", "diamond"};
        String input = "";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow49() {//roll 1 skull, 4 parrots, 3 swords, hold parrots, re-roll swords, get 2 skulls 1 sword  die
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "parrot", "parrot", "parrot", "parrot", "sword", "sword", "sword"};
        String input = "2 " + "2,3,4,5 " + "skull,skull,sword ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(0, player.playerRound(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow50() { //roll 2 skulls, 4 parrots, 2 swords, hold parrots, re-roll swords, get 1 skull 1 sword  die
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "skull", "parrot", "parrot", "parrot", "parrot", "sword", "sword"};
        String input = "2 " + "3,4,5,6 " + "skull,sword ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(0, player.playerRound(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow51() { //roll 1 skull, 4 parrots, 3 swords, hold parrots, re-roll swords, get 1 skull 2 monkeys re-roll 2 monkeys, get 1 skull 1 monkey and die
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "parrot", "parrot", "parrot", "parrot", "sword", "sword", "sword"};
        String input = "2 " + "2,3,4,5 " + "skull,monkey,monkey " + "2 " + "2,3,4,5 " + "skull,monkey ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(0, player.playerRound(card, dieRoll, game));
        System.out.println("=====isDie Check====");
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }


    @Test
    public void testForRow53() {//score first roll with nothing but 2 diamonds and 2 coins and FC is captain (SC 800)
        fortuneCard = "Captain";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "diamond", "diamond", "coin", "coin", "parrot"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(800, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow54() { //get set of 2 monkeys on first roll, get 3rd monkey on 2nd roll (SC 200 since FC is coin)
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "parrot", "sword", "sword", "parrot", "skull"};
        String input = "2 " + "1,2 " + "monkey,parrot,sword,sword,skull " + "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(200, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow55() { //score 2 sets of 3 (monkey, swords) in RTS on first roll   (SC 300)
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "monkey", "sword", "sword", "sword", "parrot", "parrot"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(300, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow58() { //score a set of 4 coins correctly (i.e., 200 + 400 points) with FC is a diamond (SC 700)
        fortuneCard = "Diamond";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"coin", "coin", "coin", "coin", "monkey", "parrot", "sword", "sword"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(700, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow66() {//score set of 8 coins on first roll and FC is diamond (SC 5400)
        fortuneCard = "Diamond";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"coin", "coin", "coin", "coin", "coin", "coin", "coin", "coin"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(5400, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow67() {//score set of 8 swords on first roll and FC is captain (SC 4500x2 = 9000) if you have full chest
        fortuneCard = "Captain";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"sword", "sword", "sword", "sword", "sword", "sword", "sword", "sword"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(9000, player.playerRound(card, dieRoll, game));
    }

    @Test
    public void testForRow75() {//get 7 swords on first roll, try to roll the 8 die by itself -> interface reports not allowed
        String[] dieRoll = new String[]{"sword", "sword", "sword", "sword", "sword", "sword", "sword", "monkey"};
        game = new GameService(true);
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        ArrayList<Integer> heldDice = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        assertFalse(game.heldDiceValidCheck(skullDice, heldDice));
    }

    public Scanner setTestInputScanner(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());//可以通过调用System.setIn（InputStream in）来用自己的流替换System.in。InputStream可以是一个字节数组
        return new Scanner(in);
    }
}

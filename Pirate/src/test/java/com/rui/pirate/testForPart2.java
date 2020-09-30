package com.rui.pirate;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
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
    public void testForRow107() {//FC: monkey business and RTS: 2 monkeys, 1 parrot, 2 coins, 3 diamonds   SC 1200 (bonus)
        fortuneCard = "Monkey Business";
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"monkey", "monkey", "parrot", "coin", "coin", "diamond", "diamond", "diamond"};
        String input = "1 ";
        Scanner scanner = setTestInputScanner(input);
        game = new GameService(true, scanner);
        assertEquals(1200, player.playerRound(card, dieRoll, game));
    }

    public Scanner setTestInputScanner(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());//可以通过调用System.setIn（InputStream in）来用自己的流替换System.in。InputStream可以是一个字节数组
        return new Scanner(in);
    }
}

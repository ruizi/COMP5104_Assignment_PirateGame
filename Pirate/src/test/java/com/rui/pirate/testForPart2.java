package com.rui.pirate;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testForPart2 {

    public final Player player = new Player("Test");
    public String fortuneCard = "Gold";
    GameService game = new GameService(true);

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


    public Scanner setTestInputScanner(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());//可以通过调用System.setIn（InputStream in）来用自己的流替换System.in。InputStream可以是一个字节数组
        return new Scanner(in);
    }
}

package com.rui.pirate;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;
import com.rui.pirate.Game.GameService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class testForPart1 {
    public final Player player = new Player("Test");
    public String fortuneCard = "Gold";
    GameService game = new GameService(true);

    @Test
    public void testForRow48() {//die with 3 skulls on first roll  -> interface reports death & end of turn
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "skull", "skull", "coin", "diamond", "diamond", "diamond", "diamond"};
        game = new GameService(true);
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }

    @Test
    public void testForRow75() {//get 7 swords on first roll, try to roll the 8 die by itself -> interface reports not allowed
        String[] dieRoll = new String[]{"sword", "sword", "sword", "sword", "sword", "sword", "sword", "monkey"};
        game = new GameService(true);
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        ArrayList<Integer> heldDice = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        assertFalse(game.heldDiceValidCheck(skullDice, heldDice));
    }
}

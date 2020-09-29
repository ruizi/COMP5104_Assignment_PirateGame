package com.rui.pirate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testForPart1 {
    @Test
    public void testForRow48() {//die with 3 skulls on first roll  -> interface reports death & end of turn
        Card card = new Card(fortuneCard);
        String[] dieRoll = new String[]{"skull", "skull", "skull", "coin", "diamond", "diamond", "diamond", "diamond"};
        game = new GameService(true);
        assertTrue(player.isPlayerTurnDie(card, dieRoll, game));
    }
}

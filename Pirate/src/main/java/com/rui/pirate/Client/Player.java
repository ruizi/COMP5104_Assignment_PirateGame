package com.rui.pirate.Client;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Game.GameService;
import com.rui.pirate.Game.ScoreCalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Player implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name; //Player`s name

    int playerId = 1;

    private int[] scoreBoard = new int[3];

    //constructor for Player
    public Player(String n) {
        name = n; //set the input string as the player`s name.
        //init a scoreSheet for this player with all blanks equals -1
        Arrays.fill(scoreBoard, -1);
    }

    public void setScoreBoardByID(int PlayerID, int score) {
        int currentScore = this.scoreBoard[PlayerID - 1] + score;
        this.scoreBoard[PlayerID - 1] = Math.max(currentScore, 0);
    }

    public boolean isPlayerTurnDie(Card card, String[] dieRoll, GameService game) {
        boolean isDie;
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        game.printSkullPosition(skullDice);
        int skullNum = skullDice.size();
        if (skullNum >= 4) {
            isDie = true;
        } else if (skullNum == 3) {
            System.out.println("Got " + skullNum + " skulls ,you round ends!");
            isDie = true;
        } else { //is skull num < 3 then isDie equals false.
            isDie = false;
        }
        if (game.isTestMode()) {
            System.out.println("isDie:" + isDie);
        }
        return isDie;
    }

    public int playerRound(Card card, String[] dieRoll, GameService game) {
        int roundScore = 0;
        ScoreCalculator scoreCalculator = new ScoreCalculator(card);
        System.out.println("First Roll :");
        game.printDieRoll(dieRoll);
        roundScore = scoreCalculator.roundScore(dieRoll);
        setScoreBoardByID(playerId, roundScore);
        System.out.println("You got " + roundScore + " points this Round.");
        //game.printPlayerScores(players, scoreBoard);
        return roundScore;
    }
}

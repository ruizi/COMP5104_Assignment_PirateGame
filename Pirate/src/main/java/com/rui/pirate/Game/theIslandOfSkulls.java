package com.rui.pirate.Game;

import com.rui.pirate.Card.Card;

import java.util.ArrayList;
import java.util.Arrays;

public class theIslandOfSkulls {
    Card card;
    private Boolean captainCard = false;
    private int[] scoreBoard = new int[3];
    private int playID;

    public Boolean getCaptainCard() {
        return captainCard;
    }

    public void setCaptainCard(Boolean captainCard) {
        this.captainCard = captainCard;
    }

    public int[] getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(int points) {
        for (int i = 0; i < 3; i++) {
            if (i != (playID - 1)) { //deduction the other players score.
                int reducePoints = scoreBoard[i] - points;
                if (reducePoints >= 0) {
                    scoreBoard[i] = scoreBoard[i] - points;
                } else {
                    scoreBoard[i] = 0;
                }
            }
            //System.out.println(scoreBoard[i]);
        }
    }

    public void isTheCardACaptainCard(Card card) {
        if (card.getName().equals("Captain")) {
            this.captainCard = true;
        }
    }

    public theIslandOfSkulls(Card card, int[] scoreBoard, int playID) {
        this.card = card;
        this.scoreBoard = scoreBoard;
        this.playID = playID;
        isTheCardACaptainCard(card);
    }

    public void menu() {
        System.out.println("Select an action:");
        System.out.println("(1) End this round and update the Score Board.");
        System.out.println("(2) Re-Roll to get more skulls.");
    }

    public boolean continueValidCheck(int skullOri, ArrayList<Integer> skullDice) {
        if (skullDice.size() == skullOri) {
            System.out.println("No skulls this turn!");
            return false;
        }
        if (skullDice.size() > 6) {
            System.out.println("Less than two dice to re-roll.");
            return false;
        }
        return true;
    }

    public String[] reRoll(String[] dieRoll, ArrayList<Integer> skullDice, GameService game) {
        ArrayList<Integer> rolls = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        for (int i : skullDice) {
            rolls.remove((Integer) i);
        }
        for (int s : rolls) {
            dieRoll = game.reRollDice(dieRoll, (s));
        }
        if (game.testMode) { //if in the test mode, need to rigging the re-roll process.
            System.out.println("===Random dieRoll:");
            game.printDieRoll(dieRoll);
            game.inputTargetArray();
            for (int i = 0; i < rolls.size(); i++) {
                dieRoll = game.reRollDiceForTest(dieRoll, rolls.get(i), game.target.get(i));
            }
            System.out.println("===rigged dieRoll:");
        }
        return dieRoll;
    }

    public int theGameLoop(ArrayList<Integer> skullDice, String[] dieRoll, GameService game) {
        game.printSkullPosition(skullDice);
        Boolean isContinue = true;
        int roundScore = 0;
        int skullOri = skullDice.size();
        while (isContinue) {
            menu();
            int order = game.inputInt();
            if (order == 1) {
                roundScore = calculateTheDeductPoints(skullDice);
                isContinue = false;
            } else if (order == 2) {
                dieRoll = reRoll(dieRoll, skullDice, game);
                game.printDieRoll(dieRoll);
                skullDice = game.locateSkull(dieRoll);
                System.out.println("Now you got " + skullDice.size() + " skulls.");
                if (continueValidCheck(skullOri, skullDice)) {
                    skullOri = skullDice.size();
                    isContinue = true;
                } else {
                    roundScore = calculateTheDeductPoints(skullDice);
                    isContinue = false;
                }
            } else {
                System.out.println("Input the wrong number! Please input again.");
            }
        }
        return roundScore;
    }

    public int calculateTheDeductPoints(ArrayList<Integer> skullDice) {
        int skullCount = skullDice.size();
        if (card.getName().equals("One Skull") || card.getName().equals("Two Skull")) {
            System.out.println("Has skull card :" + card.getName());
            skullCount += card.skulls.getSkulls();
        }
        System.out.println("You finally got " + skullCount + " skulls in the Island of Skulls!");
        System.out.println("Update Score Board.");
        int deductPoints = skullCount * 100 * (-1);
        if (captainCard) {
            deductPoints = deductPoints * 2;
            //setScoreBoard(deductPoints * 2);
        } else {
            //setScoreBoard(deductPoints);
        }
        return deductPoints;
    }
}

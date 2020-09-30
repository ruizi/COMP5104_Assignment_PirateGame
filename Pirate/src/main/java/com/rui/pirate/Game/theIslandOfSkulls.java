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

    public void setScoreBoard(int points) { //PlayID是从1开始的。 分数不够扣不能是负数，最小为0。
        for (int i = 0; i < 3; i++) {
            if (i != (playID - 1)) { //除去自己以外的其他玩家扣分,先检查是否够扣，不够的话设置为0分。
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
        //held中存的是玩家选择保留的骰子的编号从1开始编码。
        //初始化一个空间大小为8的List与骰子对应。
        ArrayList<Integer> rolls = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        //去除因skull或者玩家保留而frozen的骰子
        for (int i : skullDice) {
            rolls.remove((Integer) i); //在list中去除需要保留的骰子的编号
        }
        // remove the index from the ones to be rolled
        if (!game.isTestMode()) { //如果是实际运行模式，就随机生成。
            // remove the index from the ones to be rolled
            for (int s : rolls) { //其余号码就是本轮需要投掷的骰子。
                dieRoll = game.reRollDice(dieRoll, (s));
            }
        } else { //如果是测试模式，按target数组生成。
            game.inputTargetArray();
            for (int i = 0; i < rolls.size(); i++) {
                dieRoll = game.reRollDiceForTest(dieRoll, rolls.get(i), game.target.get(i));
            }
        }

        return dieRoll;
    }

    public int theGameLoop(ArrayList<Integer> skullDice, String[] dieRoll, GameService game) { //传入当前骰子情况，和骷髅骰子位置
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

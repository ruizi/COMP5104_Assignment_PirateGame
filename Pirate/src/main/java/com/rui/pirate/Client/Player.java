package com.rui.pirate.Client;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Game.GameService;
import com.rui.pirate.Game.ScoreCalculator;
import com.rui.pirate.Game.theIslandOfSkulls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name; //Player`s name

    int playerId = 1;

    private int[] scoreBoard = new int[3];
    static Connection clientConnection;
    Player[] players = new Player[3];

    //constructor for Player
    public Player(String n) {
        name = n; //set the input string as the player`s name.
        //init a scoreSheet for this player with all blanks equals -1
        Arrays.fill(scoreBoard, -1);
    }

    //get the local player object
    public Player getPlayer() {
        return this;
    }

    public int getScoreBoardByID(int PlayerID) {
        return this.scoreBoard[PlayerID - 1];
    }

    public void setScoreBoardForTest(int score) {
        for (int i = 0; i < scoreBoard.length; i++) {
            this.scoreBoard[i] = score;
        }
    }

    public void setScoreBoardByID(int PlayerID, int score) {
        int currentScore = this.scoreBoard[PlayerID - 1] + score;
        this.scoreBoard[PlayerID - 1] = Math.max(currentScore, 0);
    }

    public void setScoreBoardForSkullIslands(int PlayerID, int score) { //PlayID是从1开始的。 分数不够扣不能是负数，最小为0。
        for (int i = 0; i < 3; i++) {
            if (i != (PlayerID - 1)) { //除去自己以外的其他玩家扣分,先检查是否够扣，不够的话设置为0分。传递的是负分
                int currentPoints = scoreBoard[i] + score;
                scoreBoard[i] = Math.max(currentPoints, 0);
            }
        }
    }

    //init the round players in the local terminal.
    public void initializePlayers() {
        for (int i = 0; i < 3; i++) {
            players[i] = new Player(" ");
        }
    }

    public void connectToClient() {
        clientConnection = new Connection();
        playerId = clientConnection.getConnectionID();
        System.out.println("Connected as No. " + playerId + " player.");
        clientConnection.sendPlayer(getPlayer());
    }


    public boolean isPlayerTurnDie(Card card, String[] dieRoll, GameService game) {
        boolean isDie;
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        game.printSkullPosition(skullDice);
        int skullNum = game.skullNum(dieRoll, card);
        if (skullNum >= 4) {
            isDie = true;
        } else if (skullNum == 3) { //骷髅累计等于3个的情况
            //first, check the player if he/she has a Sorceress card which can bring back to life one skull
            if (card.getName().equals("Sorceress") && !card.sorceress.isUsed()) { //拥有女巫卡且未使用
                isDie = false;
            } else {//没有女巫卡或者已经使用
                System.out.println("Got " + skullNum + " skulls ,you round ends!");
                isDie = true;
            }
        } else { //骷髅小于3个的情况
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
        int turnCount = 1;

        while (!isPlayerTurnDie(card, dieRoll, game)) { //还活着的情况 1.等于3个骷髅的时候还拥有女巫卡。2.正常情况
            ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
            int skullNum = game.skullNum(dieRoll, card);
            if (skullNum == 3 && card.getName().equals("Sorceress") && !card.sorceress.isUsed()) {//拥有女巫卡且未使用
                dieRoll = card.sorceress.sorceressCard(skullDice, dieRoll, game);//新一轮的投掷情况
            } else { //正常情况下，skull的个数小于3个
                int act = game.menuChoice(dieRoll, card, skullDice);
                if (act == 1) { //直接选择结束，进入记分程序。
                    break;
                } else if (act == 2) { //正常进行re-roll
                    dieRoll = game.reRollInputAndCheck(skullDice, dieRoll, card); //表示没有treasure chest卡，按正常逻辑处理
                } else { //选择进行Treasure Chest操作 或者女巫卡
                    if (card.getName().equals("Sorceress") && !card.sorceress.isUsed()) {
                        dieRoll = card.sorceress.sorceressCard(skullDice, dieRoll, game);//新一轮的投掷情况
                    } else { //Treasure Chest操作
                        card.treasureChest.treasureChestOperation(skullDice, dieRoll, game);
                        dieRoll = game.reRollInputAndCheck(skullDice, dieRoll, card); //使用treasure chest卡后re-roll.
                    }
                }
            }
            game.printDieRoll(dieRoll); //游戏继续进行
            turnCount++;
        }
        //分数 结束了正常循环的情况
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        int skullNum = game.skullNum(dieRoll, card);

        if (skullNum >= 4) { //大于4个骷髅情况： 1.先考虑是不是第一轮进入了骷髅岛.
            if (turnCount == 1) { //第一轮出现4个骷髅且持有海战卡，结束本轮，未实现海战。
                if (card.getName().equals("Two Sabre") || card.getName().equals("Three Sabre") || card.getName().equals("Four Sabre")) {
                    roundScore = card.seaBattle.seaBattleFailed(); //海战卡扣分
                    setScoreBoardByID(playerId, roundScore); //修改记分牌
                    System.out.println("Got " + skullNum + " skulls in the first turn while hold Sea Battle Card ,you round ends! Lose:" + roundScore + " points");
                } else { //没有海战卡，启动骷髅岛
                    System.out.println("You got " + skullNum + " skulls in your first roll. So Welcome to the Island of Skulls!");
                    theIslandOfSkulls islandOfSkulls = new theIslandOfSkulls(card, scoreBoard, playerId);//启动骷髅岛模式，初始化模式各参数
                    roundScore = islandOfSkulls.theGameLoop(skullDice, dieRoll, game);//游戏循环结束返回更新的记分版。
                    System.out.println(roundScore);
                    setScoreBoardForSkullIslands(playerId, roundScore);//修改记分牌
                }
            } else { //2.如果不是第一轮，那检查是否有treasure chest卡，有的话计算保留分数，没有得话看是否有海战卡，有海战卡要丢分，没有得0分。
                if (card.getName().equals("Treasure Chest")) {//检查是treasure card保留分数。
                    roundScore = scoreCalculator.onlyTreasureChest(dieRoll);
                    setScoreBoardByID(playerId, roundScore);
                    System.out.println("You got " + roundScore + " points from treasure chest this round.");
                } else {
                    //无treasure card。检查是否有海战卡 ，有海战卡需要扣分，没有海战卡就得0分。
                    if (card.getName().equals("Two Sabre") || card.getName().equals("Three Sabre") || card.getName().equals("Four Sabre")) {
                        System.out.println("Got " + skullNum + " skulls in this roll while hold Sea Battle Card ,you round ends!");
                        roundScore = card.seaBattle.seaBattleFailed();
                        setScoreBoardByID(playerId, roundScore); //修改记分牌
                    } else { //没有海战卡，得0分。
                        System.out.println("Ops,You got 0 point this Round.");
                    }
                }
            }
        } else if (skullNum == 3) { //3个骷髅，没有女巫卡或者已经使用。1.没有女巫卡,treasure card或者sea battle card 2.有女巫卡但已经使用了，就直接得0分。
            if (card.getName().equals("Treasure Chest")) {//检查是treasure card保留分数。
                roundScore = scoreCalculator.onlyTreasureChest(dieRoll);
                setScoreBoardByID(playerId, roundScore);
                System.out.println("You got " + roundScore + " points this Round.");
            } else {
                //无treasure card。检查是否有海战卡 ，有海战卡需要扣分，没有海战卡就得0分。
                if (card.getName().equals("Two Sabre") || card.getName().equals("Three Sabre") || card.getName().equals("Four Sabre")) {
                    roundScore = card.seaBattle.seaBattleFailed();
                    System.out.println("Sea battle failed, lose " + roundScore + " Points.");
                    setScoreBoardByID(playerId, roundScore); //修改记分牌
                } else { //没有海战卡，或者女巫卡已经使用。不扣分，得0分。
                    System.out.println("Ops,You got 0 point this Round.");
                }
            }
        } else { //正常情况下，主动结束游戏轮，记分。int addScore
            roundScore = scoreCalculator.roundScore(dieRoll);
            setScoreBoardByID(playerId, roundScore);
            System.out.println("You got " + roundScore + " points this Round.");
        }
        //game.printPlayerScores(players, scoreBoard);
        return roundScore;
    }

    public void startGame(GameService game) {
        // receive players once for names
        players = clientConnection.receivePlayer();
        while (true) {
            int round = clientConnection.receiveRoundNo();//接收服务器传递过来的当前投掷轮数
            if (round == -1) //作为最后的退出
                break;
            System.out.println("\n \n \n ********Round Number " + round + "********");
            scoreBoard = clientConnection.receiveScoreBoard();//同步各玩家的分数
            game.printPlayerScores(players, scoreBoard); //打印当前各玩家分数

            //1. draw one fortune card.
            String fortuneCard = game.drawFortuneCard();
            System.out.println(fortuneCard);
            Card card = new Card(fortuneCard);
            System.out.println("| CARD ===> " + card.getName());

            //2. roll the overall eight dice at the beginning
            String[] dieRoll = game.rollDice(); //投掷骰子

            int roundScore = playerRound(card, dieRoll, game);
            System.out.println("This Round you got:" + roundScore + "points");
            game.printPlayerScores(players, scoreBoard); //打印当前各玩家分数
            clientConnection.sendScores(scoreBoard);//把得分发送出去
        }
    }

    //接收服务器的结束信号，输出winner
    public void showTheWinner(GameService game) {
        scoreBoard = clientConnection.receiveScoreBoard();//同步各玩家的分数
        System.out.println("The Final Score Board：");
        game.printPlayerScores(players, scoreBoard); //打印当前各玩家分数
        int winnerID = clientConnection.receiveWinnerID() + 1;//接收服务器传递过来的赢家ID号
        if (playerId == winnerID) {
            System.out.println("You win!");
        } else {
            System.out.println("The winner is " + players[winnerID - 1].name);
        }
        System.out.println("Game over!");
    }


    public static void main(String[] args) {
        //获取用户名字输入
        Scanner myObj = new Scanner(System.in);
        System.out.print("What is your name ? ");
        String name = myObj.next();

        //构造玩家对象，设置姓名与初始化得分。
        Player p = new Player(name);
        //本地初始化三个玩家
        p.initializePlayers();
        //连接客户端
        p.connectToClient();
        GameService game = new GameService(); //初始化构造一个游戏局
        p.startGame(game);
        p.showTheWinner(game);
        myObj.close();
    }
}

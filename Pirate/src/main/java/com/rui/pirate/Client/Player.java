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
    public boolean ticket = false;

    int round = 0;
    int playerId = 1;

    private int[] scoreBoard = new int[3];
    Connection clientConnection;
    Player[] players = new Player[3];

    //constructor for Player
    public Player(String n) {
        name = n; //set the input string as the player`s name.
        //init a scoreSheet for this player with all blanks equals -1
        Arrays.fill(scoreBoard, -1);
    }

    public Player(String name, int playerId) {
        this.name = name;
        this.playerId = playerId;
        Arrays.fill(scoreBoard, -1);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean ticketStatus() {
        if (ticket) {
            System.out.println("Not End.");
        } else {
            System.out.println("End of the turn.");
        }
        return ticket;
    }

    //get the local player object
    public Player getPlayer() {
        return this;
    }

    public Connection getClientConnection() {
        return clientConnection;
    }

    public int[] getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(int[] scoreBoard) {
        this.scoreBoard = scoreBoard;
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

    public void setScoreBoardForSkullIslands(int PlayerID, int score) { //the PlayID starts from 1. and the score can not down to negative.
        for (int i = 0; i < 3; i++) {
            if (i != (PlayerID - 1)) {
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
        //clientConnection.sendPlayer(getPlayer());
        clientConnection.sendPlayerInfo(playerId, name);
    }


    public boolean isPlayerTurnDie(Card card, String[] dieRoll, GameService game) {
        boolean isDie;
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        game.printSkullPosition(skullDice);
        int skullNum = game.skullNum(dieRoll, card);
        if (skullNum >= 4) {
            isDie = true;
        } else if (skullNum == 3) { //the situation that gets exactly three skulls.
            //first, check the player if he/she has a Sorceress card which can bring back to life one skull
            if (card.getName().equals("Sorceress") && !card.sorceress.isUsed()) { //if has the unused sorceress card, the player can be alive.
                isDie = false;
            } else {
                System.out.println("Got " + skullNum + " skulls ,you round ends!");  //if player do not has sorceress card or already used, then the round ends.
                isDie = true;
            }
        } else { //the skulls num under three, which is fine to continue.
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

        while (!isPlayerTurnDie(card, dieRoll, game)) { //check if the player is still alive.
            ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
            int skullNum = game.skullNum(dieRoll, card);
            if (skullNum == 3 && card.getName().equals("Sorceress") && !card.sorceress.isUsed()) {//has sorceress card and unused.
                dieRoll = card.sorceress.sorceressCard(skullDice, dieRoll, game);
            } else { //skull num under three. input the action the player wants to go.
                int act = game.menuChoice(dieRoll, card, skullDice);
                if (act == 1) { // act equals one means the player give up to continue re-roll and chose to score the board directly.
                    break;
                } else if (act == 2) { //the player chose to re-roll without treasure chest card or sorceress card.
                    dieRoll = game.reRollInputAndCheck(skullDice, dieRoll, card);
                } else { //choose to use Treasure Chest or Sorceress card.
                    if (card.getName().equals("Sorceress") && !card.sorceress.isUsed()) {
                        dieRoll = card.sorceress.sorceressCard(skullDice, dieRoll, game);
                    } else { //Treasure Chest
                        card.treasureChest.treasureChestOperation(skullDice, dieRoll, game);
                        dieRoll = game.reRollInputAndCheck(skullDice, dieRoll, card);
                    }
                }
            }
            game.printDieRoll(dieRoll); //游戏继续进行
            turnCount++;
        }
        //cal Score phrase.
        ArrayList<Integer> skullDice = game.locateSkull(dieRoll);
        int skullNum = game.skullNum(dieRoll, card);

        if (skullNum >= 4) { //if the skull num is over four.
            if (turnCount == 1) { //1.if the situation happened on the first turn without holding sea battle card, the player comes to skull island mode.
                if (card.getName().equals("Two Sabre") || card.getName().equals("Three Sabre") || card.getName().equals("Four Sabre")) {
                    roundScore = card.seaBattle.seaBattleFailed(); //if has sea battle card, in this case, the player will lose his points.
                    setScoreBoardByID(playerId, roundScore);
                    System.out.println("Got " + skullNum + " skulls in the first turn while hold Sea Battle Card ,you round ends! Lose:" + roundScore + " points");
                } else { //with out sea battle card, start the skull of islands.
                    System.out.println("You got " + skullNum + " skulls in your first roll. So Welcome to the Island of Skulls!");
                    theIslandOfSkulls islandOfSkulls = new theIslandOfSkulls(card, scoreBoard, playerId);
                    roundScore = islandOfSkulls.theGameLoop(skullDice, dieRoll, game);
                    System.out.println(roundScore);
                    setScoreBoardForSkullIslands(playerId, roundScore);//修改记分牌
                }
            } else { //2.if it`s not on the first turn, then the rounds end with points from treasure cheat card if the player has and operator this card.
                if (card.getName().equals("Treasure Chest")) {
                    roundScore = scoreCalculator.onlyTreasureChest(dieRoll);
                    setScoreBoardByID(playerId, roundScore);
                    System.out.println("You got " + roundScore + " points from treasure chest this round.");
                } else {
                    //if the played do not have treasure card but a sea battle card, she/he will lose some points.
                    if (card.getName().equals("Two Sabre") || card.getName().equals("Three Sabre") || card.getName().equals("Four Sabre")) {
                        System.out.println("Got " + skullNum + " skulls in this roll while hold Sea Battle Card ,you round ends!");
                        roundScore = card.seaBattle.seaBattleFailed();
                        setScoreBoardByID(playerId, roundScore);
                    } else { //and other case got zero points.
                        System.out.println("Ops,You got 0 point this Round.");
                    }
                }
            }
        } else if (skullNum == 3) { //if finally got three skulls and without Sorceress or already used, then check the score from treasure chest or may lose points by holding sea battle card.
            if (card.getName().equals("Treasure Chest")) {
                roundScore = scoreCalculator.onlyTreasureChest(dieRoll);
                setScoreBoardByID(playerId, roundScore);
                System.out.println("You got " + roundScore + " points this Round.");
            } else {
                if (card.getName().equals("Two Sabre") || card.getName().equals("Three Sabre") || card.getName().equals("Four Sabre")) {
                    roundScore = card.seaBattle.seaBattleFailed();
                    System.out.println("Sea battle failed, lose " + roundScore + " Points.");
                    setScoreBoardByID(playerId, roundScore); //修改记分牌
                } else {
                    System.out.println("Ops,You got 0 point this Round.");
                }
            }
        } else { //the player chose to stop re-roll and score the board directly.
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
            round = clientConnection.receiveRoundNo();//receive the round num from server.
            if (round == -1) //if the server return round num by -1, means the game ends with a player wins.
                break;
            System.out.println("\n \n \n **********Round Number " + round + " / Player:" + name + "**********");
            scoreBoard = clientConnection.receiveScoreBoard();//receive the latest score board from server
            game.printPlayerScores(players, scoreBoard); //print the score board.
            ticket = true;
            //1. draw one fortune card.
            String fortuneCard = game.drawFortuneCard();
            System.out.println(fortuneCard);
            Card card = new Card(fortuneCard);


            //2. roll the overall eight dice at the beginning
            String[] dieRoll = game.rollDice();

            //In short video mode.
            game.setShortVideoMode(0);
            if (game.shortVideoMode == 1) {
                dieRoll = game.shortVideoMode1_dieRoll(playerId);
                if (playerId == 1) {
                    card = new Card("Gold");
                } else if (playerId == 2) {
                    card = new Card("Gold");
                } else {
                    card = new Card("Captain");
                }
            } else if (game.shortVideoMode == 2) {
                dieRoll = game.shortVideoMode2_dieRoll(playerId);
                if (playerId == 1) {
                    card = new Card("Gold");
                } else if (playerId == 2) {
                    card = new Card("Gold");
                } else {
                    card = new Card("Gold");
                }
            }
            System.out.println("| CARD ===> " + card.getName());

            int roundScore = playerRound(card, dieRoll, game);
            System.out.println("This Round you got:" + roundScore + " points");
            game.printPlayerScores(players, scoreBoard);
            clientConnection.sendScores(scoreBoard);//send the edited score board to the server.
            ticket = false;
        }
    }

    public void showTheWinner(GameService game) {
        scoreBoard = clientConnection.receiveScoreBoard();
        System.out.println("The Final Score Board：");
        game.printPlayerScores(players, scoreBoard);
        int winnerID = clientConnection.receiveWinnerID() + 1;//received the winner ID from the server.
        if (playerId == winnerID) {
            System.out.println("You win!");
        } else {
            System.out.println("The winner is " + players[winnerID - 1].name);
        }
        System.out.println("Game over!");
    }


    public static void main(String[] args) {
        //get the player`s name.
        Scanner myObj = new Scanner(System.in);
        System.out.print("What is your name ? ");
        String name = myObj.next();

        //initial construct a player object by it`s name.
        Player p = new Player(name);
        p.initializePlayers();
        //try to establish connection to the game server.
        p.connectToClient();
        GameService game = new GameService();// init a game service which will supply kinds of service which helps game`s logic process.
        p.startGame(game);
        p.showTheWinner(game);
        myObj.close();
    }
}

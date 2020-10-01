package com.rui.pirate.Server;

import com.rui.pirate.Client.Player;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int Round;

    ServerSocket ss;
    Player[] players = new Player[3];
    int[] scoreBoard = new int[3];

    ServerThread[] playerServerThread = new ServerThread[3];
    int PlayerID;

    public static void main(String[] args) throws Exception {
        GameServer gameServer = new GameServer(); //GameServer构造函数
        gameServer.acceptConnections();
        gameServer.gameLoop();
    }

    //initial the game server
    public GameServer() {
        System.out.println("Starting game server");
        PlayerID = 0;
        Round = 0;
        // initialize the players list with new players
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(" ");
        }
        try {
            ss = new ServerSocket(3333);
        } catch (IOException ex) {
            System.out.println("Server Failed to open");
        }
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /*
     * -----------Networking stuff ----------
     *
     */
    public void acceptConnections() throws ClassNotFoundException {
        try {
            System.out.println("Waiting for players...");
            while (PlayerID < 3) { //等待三个用户的连接
                Socket s = ss.accept();
                PlayerID++;

                ServerThread serverThread = new ServerThread(s, PlayerID); //这里的numPlayer直接导入Server的构造器，作为玩家的id
                //第一个使用socket连接进来的就是一号玩家，类推
                // send the playerID to the player
                serverThread.getdOut().writeInt(serverThread.getPlayerId());//向玩家客户端下发玩家编号
                serverThread.getdOut().flush();

                // get the player name
                Player connectingIn = (Player) serverThread.getdIn().readObject(); //得到客户端返回的玩家信息
                System.out.println("Player " + serverThread.getPlayerId() + " ~ " + connectingIn.name + " ~ has joined");

                // add the player to the player list
                players[serverThread.getPlayerId() - 1] = connectingIn; //在服务器上的player表添加当前连接进入的用户
                playerServerThread[PlayerID - 1] = serverThread;//记录下与当前玩家对应连接的server
            }
            System.out.println("Three players have joined the game");

            // start the server threads
            for (int i = 0; i < playerServerThread.length; i++) {
                Thread t = new Thread(playerServerThread[i]);//在游戏服务器上启动分别对应于三个玩家的服务进程
                t.start();
                System.out.println("No." + i + " player server for player:" + playerServerThread[i].getPlayerId() + " ->Thread started!");
            }
            // start their threads
        } catch (IOException ex) {
            System.out.println("Could not connect 3 players");
        }
    }

    public boolean isEnd() {
        boolean flag = false;
        for (int i : scoreBoard) {
            if (i >= 6000) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void updateScoreBoard(int[] scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public void updateScoreBoardByID(int PlayerID, int score) {
        int currentScore = this.scoreBoard[PlayerID - 1] + score;
        this.scoreBoard[PlayerID - 1] = Math.max(currentScore, 0);
    }

    public int getWinner() {
        int winnerScore = -1;
        int winnerID = -1;
        for (int i = 0; i < scoreBoard.length; i++) {
            if (scoreBoard[i] > winnerScore) {
                winnerScore = scoreBoard[i];
                winnerID = i;
            }
        }
        System.out.println("The winner is " + players[winnerID].name);//输出winner
        return winnerID;
    }

    public void printPlayerScores() {
        // print the score sheets
        System.out.println("|---------------------------------------------|");
        for (int i = 0; i < players.length; i++) {
            System.out.printf("| Scores for player : %10s | %10d \n", players[i].name, scoreBoard[i]);
        }
        System.out.println("|---------------------------------------------|");
    }

    public void gameLoop() { //需要修改循环与记分顺序。每次三人投掷完，后统计三人当前得分，如果大于6000则直接结束游戏。
        try {
            playerServerThread[0].sendPlayers(players);//分别向玩家发送服务器维护的玩家表，从而在玩家端能看到其他玩家的信息
            playerServerThread[1].sendPlayers(players);
            playerServerThread[2].sendPlayers(players);

            while (!isEnd()) { //如果小于最大轮数继续 ###注意这里没有最大轮数，停止点是第一个到达6000分。

                Round++;

                // send the round number 三个进程分别向用户推送当前局数以及其他玩家的得分信息，以及从客户端获得的用户该轮得分更新到得分表中。
                System.out.println("*****************************************");
                System.out.println("Round number " + Round);
                playerServerThread[0].sendTurnNo(Round);
                playerServerThread[0].sendScoreBoard(scoreBoard);
                updateScoreBoard(playerServerThread[0].receiveScores());

                System.out.println("Player 1 completed turn and the score is " + scoreBoard[0]);

                playerServerThread[1].sendTurnNo(Round);
                playerServerThread[1].sendScoreBoard(scoreBoard);
                updateScoreBoard(playerServerThread[1].receiveScores());
                System.out.println("Player 2 completed turn and the score is " + scoreBoard[1]);

                playerServerThread[2].sendTurnNo(Round);
                playerServerThread[2].sendScoreBoard(scoreBoard);
                updateScoreBoard(playerServerThread[2].receiveScores());
                System.out.println("Player 3 completed turn and the score is " + scoreBoard[2]);

            }

            //向客户端发送游戏终止信号。
            playerServerThread[0].sendTurnNo(-1);
            playerServerThread[1].sendTurnNo(-1);
            playerServerThread[2].sendTurnNo(-1);

            // send final score sheet after bonus added 将最后的得分发送给各客户端。
            playerServerThread[0].sendScoreBoard(scoreBoard);
            playerServerThread[1].sendScoreBoard(scoreBoard);
            playerServerThread[2].sendScoreBoard(scoreBoard);
//
            int winnerID = getWinner();

            for (int i = 0; i < playerServerThread.length; i++) {
                playerServerThread[i].sendWinnerID(winnerID);//向各个终端推送winner的信息让其输出打印
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

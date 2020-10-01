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
        GameServer gameServer = new GameServer();
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
            while (PlayerID < 3) { //wait for all the three player establish connection with the server.
                Socket s = ss.accept();
                PlayerID++;

                ServerThread serverThread = new ServerThread(s, PlayerID); //set new server thread to establish the connection.
                // send the playerID to the player
                serverThread.getdOut().writeInt(serverThread.getPlayerId());//send player ID to the client.
                serverThread.getdOut().flush();

                // get the player name
                Player connectingIn = (Player) serverThread.getdIn().readObject();
                System.out.println("Player " + serverThread.getPlayerId() + " ~ " + connectingIn.name + " ~ has joined");

                // add the player to the player list
                players[serverThread.getPlayerId() - 1] = connectingIn; //add player to the player array in server.
                playerServerThread[PlayerID - 1] = serverThread;
            }
            System.out.println("Three players have joined the game");

            // start the server threads
            for (int i = 0; i < playerServerThread.length; i++) {
                Thread t = new Thread(playerServerThread[i]);
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
        System.out.println("The winner is " + players[winnerID].name);//show the winner
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

    public void gameLoop() { // the server main game loop.
        try {
            playerServerThread[0].sendPlayers(players);
            playerServerThread[1].sendPlayers(players);
            playerServerThread[2].sendPlayers(players);

            while (!isEnd()) { //check if there is a winner

                Round++;

                // send the round number
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

            //send the game loop end signal to clients.
            playerServerThread[0].sendTurnNo(-1);
            playerServerThread[1].sendTurnNo(-1);
            playerServerThread[2].sendTurnNo(-1);

            // send final score sheet after bonus added
            playerServerThread[0].sendScoreBoard(scoreBoard);
            playerServerThread[1].sendScoreBoard(scoreBoard);
            playerServerThread[2].sendScoreBoard(scoreBoard);
//
            int winnerID = getWinner();

            for (int i = 0; i < playerServerThread.length; i++) {
                playerServerThread[i].sendWinnerID(winnerID);//sent winner`s info to clients.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

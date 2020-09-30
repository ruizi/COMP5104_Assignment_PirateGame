package com.rui.pirate.Server;

import com.rui.pirate.Client.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable{
    private Socket socket;
    private ObjectInputStream dIn;
    private ObjectOutputStream dOut;
    private int playerId;

    public ServerThread(Socket s, int playerid) {
        socket = s;
        playerId = playerid;
        try {
            dOut = new ObjectOutputStream(socket.getOutputStream());
            dIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Server Connection failed");
        }
    }

    /*
     * run function for threads --> main body of the thread will start here
     */
    public void run() {
        try {
            while (true) {
            }

        } catch (Exception ex) {
            {
                System.out.println("Run failed");
                ex.printStackTrace();
            }
        }
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getdIn() {
        return dIn;
    }

    public ObjectOutputStream getdOut() {
        return dOut;
    }

    public int getPlayerId() {
        return playerId;
    }

    /*
     * send the scores to other players
     */
    public void sendPlayers(Player[] pl) { //一个个的发送玩家数据，对应在player.java中第342行一个个接收玩家数据
        try {
            for (Player p : pl) {
                dOut.writeObject(p);
                dOut.flush();
            }

        } catch (IOException ex) {
            System.out.println("Score sheet not sent");
            ex.printStackTrace();
        }

    }

    /*
     * receive scores of other players
     */
    public void sendTurnNo(int r) { //对应于player.java中216行
        try {
            dOut.writeInt(r);
            dOut.flush();
        } catch (Exception e) {
            System.out.println("Score sheet not received");
            e.printStackTrace();
        }
    }

    /*
     * receive scores of other players
     */
    public int[] receiveScores() {
        try {
            int[] scoreBoard = new int[3];
            for (int i = 0; i < 3; i++) {
                scoreBoard[i] = dIn.readInt();
            }
            return scoreBoard;
        } catch (Exception e) {
            System.out.println("Score Board not received");
            e.printStackTrace();
        }
        return null;
    }

    /*
     * send scores of other players
     */
    public void sendScoreBoard(int[] scoreBoard) {
        try {
            for (int i = 0; i < scoreBoard.length; i++) {
                //System.out.println(scoreBoard[i]);
                dOut.writeInt(scoreBoard[i]);
            }
            dOut.flush();
        } catch (Exception e) {
            System.out.println("Score sheet not sent");
            e.printStackTrace();
        }
    }

    public void sendWinnerID(int winnerID) {
        try {
            dOut.writeInt(winnerID);
            dOut.flush();
        } catch (Exception e) {
            System.out.println("Score sheet not received");
            e.printStackTrace();
        }
    }
}

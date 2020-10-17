package com.rui.pirate.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    Socket socket;
    private ObjectInputStream dIn;
    private ObjectOutputStream dOut;
    private int connectionID = 0;

    //constructor with the default connection portID:3333
    public Connection() {
        try {
            socket = new Socket("localhost", 3333);
            //System.out.println(socket.hashCode());
            dOut = new ObjectOutputStream(socket.getOutputStream());
            dIn = new ObjectInputStream(socket.getInputStream());
            connectionID = dIn.readInt();
        } catch (IOException ex) {
            System.out.println("Client failed to open");
        }
    }

    //constructor with given connection portID
    public Connection(int portId) {
        try {
            socket = new Socket("localhost", portId);
            dOut = new ObjectOutputStream(socket.getOutputStream());
            dIn = new ObjectInputStream(socket.getInputStream());
            connectionID = dIn.readInt();
        } catch (IOException ex) {
            System.out.println("Client failed to open");
        }
    }

    public int getConnectionID() {
        return connectionID;
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


    /*
     * function to send strings
     */
    public void sendString(String str) {
        try {
            dOut.writeUTF(str);
            dOut.flush();
        } catch (IOException ex) {
            System.out.println("Player not sent");
            ex.printStackTrace();
        }
    }

    /*
     * send scoresheet
     */
    public void sendScores(int[] scoreBoard) {
        try {
            for (int i = 0; i < scoreBoard.length; i++) {
                dOut.writeInt(scoreBoard[i]);
            }
            dOut.flush();

        } catch (IOException e) {
            System.out.println("scoreBoard sent failed");
            e.printStackTrace();
        }
    }

    /*
     * function: sent the local player info to the game server includes(player name & the score sheet)
     * send player`s information to server.
     */
//    public void sendPlayer(Player newPlayer) {
//        try {
//            dOut.writeObject(newPlayer);
//            dOut.flush();
//        } catch (IOException ex) {
//            System.out.println("Player not sent");
//            ex.printStackTrace();
//        }
//    }

    public void sendPlayerInfo(int id, String name) {
        try {
            dOut.writeUTF(String.valueOf(id) + ":" + name);
            dOut.flush();
        } catch (IOException ex) {
            System.out.println("Player not sent");
            ex.printStackTrace();
        }
    }

    /*
     * receive scores of other players
     */
    public Player[] receivePlayer() {
        Player[] pl = new Player[3];
        try {
            Player p = (Player) dIn.readObject();
            pl[0] = p;
            p = (Player) dIn.readObject();
            pl[1] = p;
            p = (Player) dIn.readObject();
            pl[2] = p;
            return pl;

        } catch (IOException e) {
            System.out.println("Score sheet not received");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("class not found");
            e.printStackTrace();
        }
        return pl;
    }

    /*
     * receive scores of other players
     */
    public int[][] receiveScores() {
        try {
            int[][] sc = new int[3][15];
            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 15; i++) {
                    sc[j][i] = dIn.readInt();
                }
                System.out.println();
            }

            return sc;
        } catch (Exception e) {
            System.out.println("Score sheet not received");
            e.printStackTrace();
        }
        return null;
    }

    public int[] receiveScoreBoard() {
        try {
            int[] scoreBoard = new int[3];
            for (int i = 0; i < 3; i++) {
                scoreBoard[i] = dIn.readInt();
            }
            return scoreBoard;
        } catch (Exception e) {
            System.out.println("Score sheet not received");
            e.printStackTrace();
        }
        return null;
    }

    /*
     * receive scores of other players
     */
    public int receiveRoundNo() {
        try {
            return dIn.readInt();
        } catch (IOException e) {
            System.out.println("Round No. not received");
            e.printStackTrace();
        }
        return 0;
    }

    public int receiveWinnerID() {
        try {
            return dIn.readInt();
        } catch (IOException e) {
            System.out.println("Winner ID not received");
            e.printStackTrace();
        }
        return 0;
    }
}

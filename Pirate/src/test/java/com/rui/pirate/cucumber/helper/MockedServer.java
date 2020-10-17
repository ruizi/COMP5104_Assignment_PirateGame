package com.rui.pirate.cucumber.helper;

import com.rui.pirate.Server.GameServer;

public class MockedServer extends Thread {
    boolean running;
    public GameServer gameServer;

    public MockedServer() {
        running = true;
    }

    public void printScoreBoard() {
        gameServer.printPlayerScores();
    }

    @Override
    public void run() {
        try {
            System.out.println("Server is running...");
            gameServer = new GameServer();
            gameServer.acceptConnections();
            gameServer.printPlayerScores();
            gameServer.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Closing");
    }
}

package com.rui.pirate.Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameService implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public boolean testMode = false;

    public Scanner scanner;

    public ArrayList<String> target;

    public static Map<Integer, String> map;

    {
        map = new HashMap<Integer, String>();
        map.put(1, "skull");
        map.put(2, "monkey");
        map.put(3, "parrot");
        map.put(4, "sword");
        map.put(5, "coin");
        map.put(6, "diamond");
    }

    public GameService() {
        this.scanner = new Scanner(System.in);
    }

    public GameService(boolean testMode) {
        this.testMode = testMode;
    }

    public GameService(boolean testMode, Scanner scanner) {
        this.testMode = testMode;
        this.scanner = scanner;
    }

    public GameService(boolean testMode, Scanner scanner, ArrayList<String> target) {
        this.testMode = testMode;
        this.scanner = scanner;
        this.target = target;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public ArrayList<Integer> locateSkull(String[] dieRoll) {
        ArrayList<Integer> skullDice = new ArrayList<>();
        for (int i = 0; i < dieRoll.length; i++) {
            if (dieRoll[i].equals("skull")) {
                skullDice.add(i);
            }
        }
        return skullDice;
    }


    public void printSkullPosition(ArrayList<Integer> skullDice) {
        //输出当前骷髅的位置
        System.out.print("the skulls are in position :");
        for (int i : skullDice) {
            System.out.print(i + 1 + "  ");
        }
        System.out.println();
    }
}

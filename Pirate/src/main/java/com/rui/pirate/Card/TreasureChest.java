package com.rui.pirate.Card;

import com.rui.pirate.Game.GameService;

import java.util.ArrayList;

public class TreasureChest {
    private ArrayList<Integer> treasureList;

    public ArrayList<Integer> getTreasureList() {
        return treasureList;
    }

    public TreasureChest(ArrayList<Integer> treasureList) {
        this.treasureList = treasureList;
    }

    public int getTreasureListSize() {
        return treasureList.size();
    }

    public void setTreasureList(ArrayList<Integer> treasureList) {
        this.treasureList = treasureList;
    }

    public void addDiceToTreasureList(ArrayList<Integer> addIn) {
        for (int i : addIn) {
            if (!treasureList.contains(i)) {
                treasureList.add(i);
            }
        }
        System.out.println("Add in finished");
    }

    public void removeDiceFromTreasureList(ArrayList<Integer> removeOut) {
        for (int i : removeOut) {
            if (treasureList.contains(i)) {
                treasureList.remove((Integer) i);
            }
        }
        System.out.println("Remove out finished");
    }

    public void printDiceInTreasureChest() {
        System.out.print("Treasure Chest contains :");
        for (int i : treasureList) {
            System.out.print("No." + (i + 1) + " ");
        }
        System.out.println();
    }

    //1.can not add skull into treasure chest. 2. can not add dice which are already in treasure chest.
    public boolean addInValidCheck(ArrayList<Integer> skullDice, ArrayList<Integer> chosenDiceLoc) {
        for (int i : chosenDiceLoc) {
            if (skullDice.contains(i)) {
                System.out.println("Can not add skull dice to treasure chest, Please choose again");
                return false;
            }
            if (treasureList.contains(i)) {
                System.out.println("Can not add dice already in treasure chest, Please choose again");
                return false;
            }
        }
        return true;
    }

    //1. can not remove dice from chest which is actually not in the treasure chest.
    public boolean removeValidCheck(ArrayList<Integer> chosenDiceLoc) {
        for (int i : chosenDiceLoc) {
            if (!treasureList.contains(i)) {
                System.out.println("Can not remove dice which is not in treasure chest, Please choose again");
                return false;
            }
        }
        return true;
    }


    public ArrayList<Integer> inputDiceLocAndCheck(int code, ArrayList<Integer> skullDice, String[] dieRoll, GameService game) {
        ArrayList<Integer> chosenDiceLoc;
        printDiceInTreasureChest();
        while (true) {
            if (code == 1) {
                System.out.println("Select the dice to add into treasure chest: ex. 1,2,... ");
                chosenDiceLoc = game.selectedDice();//the player add dice into treasure chest by input the dice`s loc.
                if (addInValidCheck(skullDice, chosenDiceLoc)) {
                    break;
                }
            } else {
                System.out.println("Select the dice to remove from treasure chest: ex. 1,2,... ");
                chosenDiceLoc = game.selectedDice();//the player remove dice from treasure chest by input the dice`s loc.
                if (removeValidCheck(chosenDiceLoc)) {
                    break;
                }
            }
        }
        return chosenDiceLoc;
    }

    public boolean exitValidCheckPass(ArrayList<Integer> skullDice) {//at least left two dice.
        if ((treasureList.size() + skullDice.size()) > 6) { //Condition Three.
            System.out.println("Add too much, In each roll, you must use at least two dice to start re-roll. Please remove out some dice from treasure chest.");
            return false;
        }
        return true;
    }

    public void treasureChestOperation(ArrayList<Integer> skullDice, String[] dieRoll, GameService game) {
        game.printSkullPosition(skullDice); //print the skulls location.
        printDiceInTreasureChest();
        while (true) {
            int action;
            while (true) {
                System.out.println("Select an action:  (0) Exit || (1) add || (2) remove");
                action = game.inputInt();
                if (action == 0 || action == 1 || action == 2) {
                    break;
                } else {
                    System.out.println("Invalid Input");
                }
            }
            if (action == 1) {
                ArrayList<Integer> addIn = inputDiceLocAndCheck(action, skullDice, dieRoll, game);
                addDiceToTreasureList(addIn);
            } else if (action == 2) {
                ArrayList<Integer> removeOut = inputDiceLocAndCheck(action, skullDice, dieRoll, game);
                removeDiceFromTreasureList(removeOut);
            } else {
                if (exitValidCheckPass(skullDice)) {
                    break;
                }
            }
        }
        printDiceInTreasureChest();
    }
}

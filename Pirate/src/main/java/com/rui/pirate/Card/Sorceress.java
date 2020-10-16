package com.rui.pirate.Card;

import com.rui.pirate.Game.GameService;

import java.util.ArrayList;

public class Sorceress {
    private boolean isUsed = false;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Sorceress() {
    }

    //1.can not hold dice with skull face (include the one just take back by Sorceress card. 2. At least left two dice.
    public Boolean heldDiceValidCheckForSorceress(ArrayList<Integer> skullDice, ArrayList<Integer> heldDice, int choose) {
        for (int i : heldDice) {   //Condition One.
            if (skullDice.contains(i)) {
                System.out.println("Can not hold skull dice, Please choose again");
                return false;
            }
            if (i == choose) {
                System.out.println("Do not hold the No. " + (choose + 1) + " skull dice just get back by using sorceress Card, Please choose again");
                return false;
            }
        }
        if ((heldDice.size() + skullDice.size()) > 6) {
            System.out.println("Hold too much, In each roll, you must use at least two dice to start re-roll. Please choose again");
            return false;
        }
        return true;
    }

    public String[] sorceressCard(ArrayList<Integer> skullDice, String[] dieRoll, GameService game) {
        game.printSkullPosition(skullDice); //print the skull location.
        System.out.println("choose one skull to bring back:");
        int choose = game.inputInt() - 1;
        skullDice.remove((Integer) choose); //get back one skull by using sorceress card. remove the choose dice loc from the skullDice.
        this.setUsed(true);//标记为使用了
        ArrayList<Integer> heldDice = new ArrayList<Integer>();
        ArrayList<Integer> treasureChest = new ArrayList<Integer>();
        while (true) {
            System.out.println("Select the die to hold : ex. 1,2,... Enter 0 for skip ");
            System.out.println("|| Rule 1. can not hold skull dice or dice in the treasure care. ");
            System.out.println("|| Rule 2. leave no less than two dice in the ground.");
            heldDice = game.selectedDice(); //the player input the dice`s location that he/she wants to keep.
            if (heldDice.contains(-1)) { //if input code is 0, then skip the hold process.
                heldDice.clear();
                break;
            }

            if (heldDiceValidCheckForSorceress(skullDice, heldDice, choose)) { //check if the input hold dice loc is valid or not.
                break;
            }
        }
        dieRoll = game.reRollNotHeld(dieRoll, heldDice, skullDice, treasureChest);

        //game.printDieRoll(dieRoll);
        return dieRoll;
    }
}

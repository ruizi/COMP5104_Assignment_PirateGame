package com.rui.pirate.Game;

import com.rui.pirate.Card.Card;
import com.rui.pirate.Client.Player;

import java.io.Serializable;
import java.util.*;

public class GameService implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public boolean testMode = false;

    public int shortVideoMode;

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

    public void setShortVideoMode(int shortVideoMode) {
        this.shortVideoMode = shortVideoMode;
    }

    public int isShortVideoMode() {
        return shortVideoMode;
    }

    public boolean isTestMode() {
        return testMode;
    }

    //get a fortune card randomly.
    public String drawFortuneCard() {
        int rand = (int) (Math.random() * 35 + 1);
        if (rand <= 4) {
            return "Gold"; //Gold Coin * 4
        } else if (rand <= 8) {
            return "Diamond"; //Diamonds * 4
        } else if (rand <= 12) {
            return "Sorceress"; //Sorceress * 4
        } else if (rand <= 16) {
            return "Captain"; //Captain * 4
        } else if (rand <= 20) {
            return "Treasure Chest"; //Treasure Chest * 4
        } else if (rand <= 24) {
            return "Monkey Business";//Monkey Business * 4
        } else if (rand <= 27) {
            return "One Skull"; //One Skull * 3
        } else if (rand <= 29) {
            return "Two Skull"; //Two Skull * 2
        } else if (rand <= 31) {
            return "Two Sabre"; //Two Sabre * 2
        } else if (rand <= 33) {
            return "Three Sabre"; //Three Sabre * 2
        } else if (rand <= 35) {
            return "Four Sabre"; //Four Sabre * 2
        }
        return null;
    }

    //randomly roll all the eight dice.
    public String[] rollDice() {
        String[] dice = new String[8];
        for (int i = 0; i < 8; i++) {
            int rand = (int) (Math.random() * 6 + 1);
            dice[i] = map.get(rand);
        }
        return dice;
    }

    //the game`s main logic input instructions valid check.
    public boolean gameLoopInputCheck(int act, Card card, ArrayList<Integer> skullDice) {
        if (act == 1) { //at any time, give up continue and seek to score board is valid.
            return true;
        } else if (act == 3) { //the player choose to use card effect. need to check if he has the card or not and if the card is used.
            if (card.getName().equals("Treasure Chest") || (card.getName().equals("Sorceress") && !card.sorceress.isUsed() && skullDice.size() > 0)) {
                return true;
            } else {
                System.out.println("You do not have Treasure Chest or Sorceress Card or already used or no skull to bring back. Please choose 1 for Score or 2 for Continue.");
            }
        } else { //left at least two dice.
            int calFrozenDiceNum = skullDice.size();
            if (card.getName().equals("Treasure Chest")) {
                calFrozenDiceNum += card.treasureChest.getTreasureListSize();
            }
            return calFrozenDiceNum <= 6;
        }
        return false;
    }

    //1. Can not hold dice with skull. 2.Can not hold dice in the treasureChest. 3. At least left two dice.
    public Boolean heldDiceValidCheck(ArrayList<Integer> skullDice, ArrayList<Integer> heldDice, ArrayList<Integer> treasureChest) {
        for (int s : heldDice) {   //Condition One.
            if (skullDice.contains(s)) {
                System.out.println("Can not hold skull dice, Please choose again");
                return false;
            }
            if (treasureChest.contains(s)) {
                System.out.println("no need to hold dice in the Treasure Chest, Please choose again");
                return false;
            }
        }
        int frozenDiceNum = heldDice.size() + skullDice.size() + treasureChest.size();
        if (frozenDiceNum > 6) { //Condition Three.
            System.out.println("Hold too much, In each roll, you must use at least two dice to start re-roll. Please choose again");
            return false;
        }
        return true;
    }

    public String[] reRollInputAndCheck(ArrayList<Integer> skullDice, String[] dieRoll, Card card) {
        printSkullPosition(skullDice);
        ArrayList<Integer> treasureChest = new ArrayList<>();
        if (card.getName().equals("Treasure Chest")) {
            treasureChest = card.treasureChest.getTreasureList();
        }
        ArrayList<Integer> heldDice = new ArrayList<>();
        while (true) {
            System.out.println("Select the die to hold : ex. 1,2,... Enter 0 for skip ");
            System.out.println("|| 1. can not hold skull dice or dice in the treasure card. ");
            System.out.println("|| 2. leave no less than two dice in the ground.");
            heldDice = selectedDice();//input the dice positions which you wants to hold.
            if (heldDice.contains(-1)) { //if the input is 0,then skill the hold process.
                heldDice.clear();
                break;
            }
            if (heldDiceValidCheck(skullDice, heldDice, treasureChest)) { //input valid check.
                break;
            }
        }
        dieRoll = reRollNotHeld(dieRoll, heldDice, skullDice, treasureChest);
        return dieRoll;
    }

    /*
     * reRoll die which have not been held or frozen
     */
    public String[] reRollNotHeld(String[] dieRoll, ArrayList<Integer> held, ArrayList<Integer> skullDice, ArrayList<Integer> treasureChest) {
        ArrayList<Integer> rolls = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        ArrayList<Integer> heldAndSkullAndTreasure = new ArrayList<>();
        heldAndSkullAndTreasure.addAll(held);
        heldAndSkullAndTreasure.addAll(skullDice);
        heldAndSkullAndTreasure.addAll(treasureChest);
        for (int i : heldAndSkullAndTreasure) {
            rolls.remove((Integer) i); //remove the dice which can not be re-rolled.
        }
        //System.out.println("Allowed re-roll dice :" + rolls);

        // remove the index from the ones to be rolled
        for (int s : rolls) {
            dieRoll = reRollDice(dieRoll, (s));
        }
        if (testMode) { //if in the test mode, need to rigging the re-roll process.
            System.out.println("===Random dieRoll:");
            printDieRoll(dieRoll);
            inputTargetArray();
            for (int i = 0; i < rolls.size(); i++) {
                dieRoll = reRollDiceForTest(dieRoll, rolls.get(i), target.get(i));
            }
            System.out.println("===rigged dieRoll:");
        }
        return dieRoll;
    }


    //re-roll the specific dice.
    public String[] reRollDice(String[] dieRoll, int i) {
        int rand = (int) (Math.random() * 6 + 1);
        dieRoll[i] = map.get(rand);
        return dieRoll;
    }

    //rigging re-roll with the defined target value.
    public String[] reRollDiceForTest(String[] dieRoll, int reRollLoc, String targetValue) {
        while (true) {
            int rand = (int) (Math.random() * 6 + 1);
            String face = map.get(rand);
            if (face.equals(targetValue)) {
                dieRoll[reRollLoc] = map.get(rand);
                break;
            }
        }
        return dieRoll;
    }

    //Get input position array
    public ArrayList<Integer> selectedDice() {
        Scanner myObj = scanner;
        String input = myObj.next();
        String[] selectedDice = (input).replaceAll("\\s", "").split(","); //the dice chose to hold
        if (testMode) {
            System.out.println(input);
        }
        ArrayList<Integer> selectedDiceArray = new ArrayList<>();
        for (String s : selectedDice) {
            int rem = Integer.parseInt(s) - 1;
            selectedDiceArray.add(rem);
        }
        return selectedDiceArray;
    }

    //Get Integer input
    public int inputInt() {
        Scanner myObj = scanner;
        int input = myObj.nextInt();
        if (testMode) {
            System.out.println(input);
        }
        return input;
    }

    public void inputTargetArray() {
        Scanner myObj = scanner;
        String[] inputTarget = (myObj.next()).replaceAll("\\s", "").split(",");
        target = new ArrayList<>(Arrays.asList(inputTarget));
    }

    public int menuChoice(String[] dieRoll, Card card, ArrayList<Integer> skullDice) {
        int choice;
        while (true) {
            System.out.println("Select an action: ");
            System.out.println("(1) Score this round");
            System.out.println("(2) Roll again (Choose dice to hold or Direct re-roll)");
            if (card.getName().equals("Treasure Chest")) {
                System.out.println("(3) Put dice into Treasure Chest or get them out. and then Roll to Continue!");
            }
            if (card.getName().equals("Sorceress") && !card.sorceress.isUsed() && skullDice.size() > 0) {
                System.out.println("(3) Get back a skull!");
            }
            choice = this.inputInt();
            if (gameLoopInputCheck(choice, card, skullDice)) { //the input valid check.
                break;
            }
        }
        return choice;
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

    public int skullNum(String[] dieRoll, Card card) {
        ArrayList<Integer> skullDice = this.locateSkull(dieRoll);
        int skullNum = skullDice.size();
        if (card.getName().equals("One Skull") || card.getName().equals("Two Skull")) {
            skullNum += card.skulls.getSkulls();
        }
        return skullNum;
    }

    public void printSkullPosition(ArrayList<Integer> skullDice) {
        //print the skull dice location.
        System.out.print("the skulls are in position :");
        for (int i : skullDice) {
            System.out.print(i + 1 + "  ");
        }
        System.out.println();
    }

    public void printPlayerScores(Player[] pl, int[] scoreBoard) {
        // print the score sheets
        System.out.println("|---------------------------------------------|");
        for (int i = 0; i < pl.length; i++) {
            System.out.printf("| Scores for player : %10s | %10d \n", pl[i].name, scoreBoard[i]);
        }
        System.out.println("|---------------------------------------------|");
    }

    /*
     * print the die roll in a clear way
     */
    public void printDieRoll(String[] dieRoll) {
        System.out.println(" 1_______    2_______    3_______    4_______    5_______    6_______    7_______    8_______  ");
        System.out.printf("|%8s|  |%8s|  |%8s|  |%8s|  |%8s|  |%8s|  |%8s|  |%8s|  \n", dieRoll[0], dieRoll[1], dieRoll[2], dieRoll[3], dieRoll[4], dieRoll[5], dieRoll[6], dieRoll[7]);
        System.out.println("|________|  |________|  |________|  |________|  |________|  |________|  |________|  |________|  ");

    }

    public String[] shortVideoMode1_dieRoll(int playerID) {
        if (playerID == 1) {
            return new String[]{"sword", "sword", "sword", "parrot", "parrot", "skull", "skull", "skull"};
        } else if (playerID == 2) {
            return new String[]{"sword", "sword", "sword", "parrot", "parrot", "monkey", "monkey", "skull"};
        } else {
            return new String[]{"sword", "sword", "sword", "sword", "sword", "sword", "sword", "sword"};
        }
    }

    public String[] shortVideoMode2_dieRoll(int playerID) {
        if (playerID == 1) {
            return new String[]{"sword", "sword", "sword", "parrot", "parrot", "parrot", "coin", "skull"};
        } else if (playerID == 2) {
            return new String[]{"sword", "sword", "sword", "sword", "sword", "sword", "sword", "sword"};
        } else {
            return new String[]{"sword", "sword", "sword", "sword", "parrot", "parrot", "skull", "skull"};
        }
    }
}

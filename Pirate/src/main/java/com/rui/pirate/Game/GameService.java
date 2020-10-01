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

    //随机取出一张卡牌
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

    //随机掷骰子
    public String[] rollDice() {
        String[] dice = new String[8];
        for (int i = 0; i < 8; i++) {
            int rand = (int) (Math.random() * 6 + 1);
            dice[i] = map.get(rand);
        }
        return dice;
    }

    //游戏主循环输入合法性检查
    public boolean gameLoopInputCheck(int act, Card card, ArrayList<Integer> skullDice) {
        if (act == 1) { //任何时候都可以放弃继续，而直接打分
            return true;
        } else if (act == 3) { //使用Treasure Chest或这女巫卡前需要先判断是否有这张卡
            if (card.getName().equals("Treasure Chest") || (card.getName().equals("Sorceress") && !card.sorceress.isUsed() && skullDice.size() > 0)) {
                return true;
            } else {
                System.out.println("You do not have Treasure Chest or Sorceress Card or already used or no skull to bring back. Please choose 1 for Score or 2 for Continue.");
            }
        } else { //选择re-roll需要判断当前是否可以继续 1.Treasure Chest保留的 + Skull封闭的 <=6个，才能给足够的空间进行re-roll,极限情况就是不hold已经6个不能动了。
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
            if (skullDice.contains(s)) { //如果包含了skullDice中的骰子，是非法选取，需要重新选择。
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

    //在正常round中，输入想要hold的骰子的编号，之后对这些编号进行合法性判断，主要是不能hold骷髅和宝箱内的元素，要剩下至少两个能抛的骰子，宝箱内的元素不能re-roll
    public String[] reRollInputAndCheck(ArrayList<Integer> skullDice, String[] dieRoll, Card card) {
        printSkullPosition(skullDice); //打印现在骷髅的位置
        ArrayList<Integer> treasureChest = new ArrayList<>(); //如果有Treasure Card，需要取出其中保持的骰子与输入的hold做合法性检查
        //boolean hasTreasureCard = false;
        if (card.getName().equals("Treasure Chest")) {
            treasureChest = card.treasureChest.getTreasureList();
            //hasTreasureCard = true;
        }
        ArrayList<Integer> heldDice = new ArrayList<>();
        while (true) {
            System.out.println("Select the die to hold : ex. 1,2,... Enter 0 for skip ");
            System.out.println("|| 1. can not hold skull dice or dice in the treasure card. ");
            System.out.println("|| 2. leave no less than two dice in the ground.");
            heldDice = selectedDice();//输入准备保留的骰子
            if (heldDice.contains(-1)) { //如果输入了0，就跳过hold步骤
                heldDice.clear();
                break;
            }
            if (heldDiceValidCheck(skullDice, heldDice, treasureChest)) { //检查选择是否合法
                break;
            }
        }
        dieRoll = reRollNotHeld(dieRoll, heldDice, skullDice, treasureChest);
        //printSkullPosition(locateSkull(dieRoll));
        return dieRoll;
    }

    /*
     * reRoll die which have not been held or frozen
     */
    public String[] reRollNotHeld(String[] dieRoll, ArrayList<Integer> held, ArrayList<Integer> skullDice, ArrayList<Integer> treasureChest) {
        //held中存的是玩家选择保留的骰子的编号从1开始编码。
        //初始化一个空间大小为8的List与骰子对应。
        ArrayList<Integer> rolls = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
        ArrayList<Integer> heldAndSkullAndTreasure = new ArrayList<>();
        heldAndSkullAndTreasure.addAll(held);
        heldAndSkullAndTreasure.addAll(skullDice);
        heldAndSkullAndTreasure.addAll(treasureChest);
        //去除因skull或者玩家保留而frozen的骰子
        for (int i : heldAndSkullAndTreasure) {
            rolls.remove((Integer) i); //在list中去除需要保留的骰子的编号
        }
        //System.out.println("Allowed re-roll dice :" + rolls);

        // remove the index from the ones to be rolled
        for (int s : rolls) { //其余号码就是本轮需要投掷的骰子。
            dieRoll = reRollDice(dieRoll, (s));
        }
        if (testMode) { //如果是测试模式，按target数组生成。
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


    //重置骰子
    public String[] reRollDice(String[] dieRoll, int i) {
        int rand = (int) (Math.random() * 6 + 1);
        dieRoll[i] = map.get(rand);
        return dieRoll;
    }

    //重置骰子 rigging 如果target数组中
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
        String[] selectedDice = (input).replaceAll("\\s", "").split(","); //准备保留的卡牌
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
        while (true) { //1和2可以自由选择，选择3需要判断是否有卡，如果选择了2需要判断是否可以继续
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
            if (gameLoopInputCheck(choice, card, skullDice)) { //检查选择是否合法
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
        //输出当前骷髅的位置
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
}

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

    //1. 选取的骰子不能含有skull(包括刚才复活的) 2. 至少要剩余两颗骰子
    public Boolean heldDiceValidCheckForSorceress(ArrayList<Integer> skullDice, ArrayList<Integer> heldDice, int choose) {
        for (int i : heldDice) {   //Condition One.
            if (skullDice.contains(i)) { //如果包含了skullDice中的骰子，是非法选取，需要重新选择。
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
        game.printSkullPosition(skullDice); //打印现在骷髅的位置
        System.out.println("choose one skull to bring back:");
        int choose = game.inputInt() - 1;
        skullDice.remove((Integer) choose); //复活了一个skull骰子，从list中去除
        this.setUsed(true);//标记为使用了
        ArrayList<Integer> heldDice = new ArrayList<Integer>();
        ArrayList<Integer> treasureChest = new ArrayList<Integer>();//为了复用re-roll代码，这里一定是空的，因为已经有女巫卡就不可能有treasure卡

        while (true) {
            System.out.println("Select the die to hold : ex. 1,2,... Enter 0 for skip ");
            System.out.println("|| 1. can not hold skull dice or dice in the treasure care. ");
            System.out.println("|| 2. leave no less than two dice in the ground.");
            heldDice = game.selectedDice(); //准备保留的卡牌
            if (heldDice.contains(-1)) { //如果输入了0，就跳过hold步骤
                heldDice.clear();
                break;
            }
            if (heldDiceValidCheckForSorceress(skullDice, heldDice, choose)) { //检查选择是否合法
                break;
            }
        }
        dieRoll = game.reRollNotHeld(dieRoll, heldDice, skullDice, treasureChest);
        //game.printDieRoll(dieRoll);
        return dieRoll;
    }
}

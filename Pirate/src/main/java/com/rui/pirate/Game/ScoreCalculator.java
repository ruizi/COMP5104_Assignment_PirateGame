package com.rui.pirate.Game;

import com.rui.pirate.Card.Card;

import java.util.*;

public class ScoreCalculator {
    Card card;
    Set<Integer> fullChestSet = new HashSet<>();

    public ScoreCalculator(Card card) {
        this.card = card;
    }

    //Cal Face Value from coin and diamond.
    public int calFaceValue(String[] dieRoll) {
        int faceValue = 0;
        for (int i = 0; i < dieRoll.length; i++) { //face valve of die rolls.
            if (dieRoll[i].equals("diamond") || dieRoll[i].equals("coin")) {
                faceValue += 100;
                fullChestSet.add(i);
            }
        }
        if (card.getName().equals("Gold") || card.getName().equals("Diamond")) { //face value of card.
            faceValue += 100;
        }
        System.out.println("Face Value:" + faceValue);
        return faceValue;
    }

    public int calFaceValueFromTreasureChest(String[] dieRoll, ArrayList<Integer> treasureList) { //如果出现仅能计算treasure card中的分数，不用考虑其他卡牌，直接算面值
        int faceValue = 0;
        for (int i = 0; i < dieRoll.length; i++) { //骰子序列的coin和diamonds的面值
            if ((dieRoll[i].equals("diamond") || dieRoll[i].equals("coin")) && treasureList.contains(i)) {
                faceValue += 100;
            }
        }
        System.out.println("Treasure Chest Face Value:" + faceValue);
        return faceValue;
    }

    public void contributeArrayLoc(String[] dieRoll, String face) {  //label for fullChest cal.
        for (int i = 0; i < dieRoll.length; i++) {
            if (dieRoll[i].equals(face)) {
                fullChestSet.add(i);
            }
        }
    }

    public int addPointByOneKind(int times) {
        int points = 0;
        if (times == 3) {
            points = 100;
        } else if (times == 4) {
            points = 200;
        } else if (times == 5) {
            points = 500;
        } else if (times == 6) {
            points = 1000;
        } else if (times == 7) {
            points = 2000;
        } else if (times == 8 || times == 9) {
            points = 4000;
        }
        return points;
    }

    public HashMap<String, Integer> timesCal(String[] dieRoll) { //Build a map of skull face and showing times.
        HashMap<String, Integer> times = new HashMap<String, Integer>();
        for (String i : dieRoll) {
            if (!i.equals("skull")) {
                if (times.containsKey(i)) { //If there is already a key in the hashmap equals to the given dice, then add it`s value by one.
                    times.put(i, times.get(i) + 1);
                } else {
                    times.put(i, 1); //Or add a key-value map with key equals to the dice face pic, value equals to 1.
                }
            }
        }
        if (card.getName().equals("Gold")) {
            if (times.containsKey("coin")) {
                times.put("coin", times.get("coin") + 1);
            } else {
                times.put("coin", 1);
            }

        }
        if (card.getName().equals("Diamond")) {
            if (times.containsKey("diamond")) {
                times.put("diamond", times.get("diamond") + 1);
            } else {
                times.put("diamond", 1);
            }
        }
        return times;
    }

    //x of a kind , Sequence.
    public int calSequence(String[] dieRoll) {
        if (card.getName().equals("Monkey Business")) { //有猴子卡
            dieRoll = card.monkeyBusiness.parrotToMonkey(dieRoll);
        }
        int bonusPoints = 0;
        //统计各元素出现次数
        HashMap<String, Integer> times = timesCal(dieRoll);
        for (Map.Entry<String, Integer> entry : times.entrySet()) {
            String face = entry.getKey();
            int value = entry.getValue();
            if (value >= 3) {
                contributeArrayLoc(dieRoll, face);
                bonusPoints += addPointByOneKind(value);
            }
        }
        System.out.println("Sequence Value :" + bonusPoints);
        return bonusPoints;
    }

    public int calSequenceFromTreasureChest(String[] dieRoll, ArrayList<Integer> treasureList) {
        int bonusPoints = 0;
        String[] dieRollInTreasureList = new String[treasureList.size()];
        for (int i = 0; i < treasureList.size(); i++) {
            dieRollInTreasureList[i] = dieRoll[treasureList.get(i)];
        }
        //统计各元素出现次数
        HashMap<String, Integer> times = timesCal(dieRollInTreasureList);
        for (Map.Entry<String, Integer> entry : times.entrySet()) { //遍历map，对value值大于等于3的元素，计算序列得分，同时标记作用位置。
            String face = entry.getKey();
            int value = entry.getValue();
            //System.out.println(face + ":" + value);
            if (value >= 3) {
                bonusPoints += addPointByOneKind(value);
            }
        }
        System.out.println("Treasure Chest Sequence Value :" + bonusPoints);
        return bonusPoints;
    }

    public boolean isFullChest() {
        return fullChestSet.size() == 8;
    }

    public int calFullChest() {
        if (isFullChest()) {
            System.out.println("FullChest Value :" + 500);
            return 500;
        } else {
            return 0;
        }
    }

    public int roundScore(String[] dieRoll) { //最后的总分计算。
        System.out.println("======Round Points======");
        int sumPoints = calFaceValue(dieRoll) + calSequence(dieRoll) + calFullChest();
        if (card.getName().equals("Captain")) {
            System.out.println("Captain double: True");
            sumPoints = sumPoints * 2;
        }
        System.out.printf("======Sum %5d======\n", sumPoints);
        return sumPoints;
    }

    public int onlyTreasureChest(String[] dieRoll) { //skull超过了限制只统计TreasureChest中
        System.out.println("======Round Points======");
        int sumPoints = 0;
        if (card.getName().equals("Treasure Chest")) {
            ArrayList<Integer> treasureList = card.treasureChest.getTreasureList();
            if (treasureList.size() > 0) {
                sumPoints = calFaceValueFromTreasureChest(dieRoll, treasureList) + calSequenceFromTreasureChest(dieRoll, treasureList);
            }
        }
        System.out.printf("======Sum %5d======\n", sumPoints);
        return sumPoints;
    }
}

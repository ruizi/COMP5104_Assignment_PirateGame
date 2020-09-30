package com.rui.pirate.Card;

import java.util.ArrayList;

public class Card {
    private String name;

    public Gold gold;
    public Diamond diamond;
    public Captain captain;
    public TreasureChest treasureChest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card(String name) {
        this.name = name;
        if (name.equals("Gold")) {
            gold = new Gold();
        } else if (name.equals("Diamond")) {
            diamond = new Diamond();
        } else if (name.equals("Captain")) {
            captain = new Captain();
        } else if (name.equals("Treasure Chest")) {
            treasureChest = new TreasureChest(new ArrayList<Integer>());
        }
    }
}

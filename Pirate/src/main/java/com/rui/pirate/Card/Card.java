package com.rui.pirate.Card;

import java.util.ArrayList;

public class Card {
    private String name;

    public Gold gold;
    public Diamond diamond;
    public Captain captain;
    public TreasureChest treasureChest;
    public Sorceress sorceress;
    public MonkeyBusiness monkeyBusiness;
    public Skulls skulls;

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
        } else if (name.equals("Sorceress")) { //OK
            sorceress = new Sorceress();
        } else if (name.equals("Monkey Business")) {
            monkeyBusiness = new MonkeyBusiness();
        } else if (name.equals("One Skull")) {
            skulls = new Skulls(1);
        } else if (name.equals("Two Skull")) {
            skulls = new Skulls(2);
        }
    }
}

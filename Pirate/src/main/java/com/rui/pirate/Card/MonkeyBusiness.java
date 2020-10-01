package com.rui.pirate.Card;

public class MonkeyBusiness {
    public String[] parrotToMonkey(String[] dieRoll) { //Card effect, replace parrot with monkey
        for (int i = 0; i < dieRoll.length; i++) {
            if (dieRoll[i].equals("parrot")) {
                dieRoll[i] = "monkey";
            }
        }
        return dieRoll;
    }

    public MonkeyBusiness() {
    }
}

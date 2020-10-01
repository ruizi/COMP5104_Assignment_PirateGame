package com.rui.pirate.Card;

public class SeaBattle {
    private int Sabre;
    private int bonusPoint;

    public int getSabre() {
        return Sabre;
    }

    public SeaBattle(int sabre, int bonusPoint) {
        this.Sabre = sabre;
        this.bonusPoint = bonusPoint;
    }

    public int seaBattlePoint(int swordNum) { //in order to get the bonus points from sea battle card. player need to get at least the same swords as the card required.
        if (swordNum >= Sabre) {
            return bonusPoint;
        } else { //or the player will lost points.
            return -bonusPoint;
        }
    }

    public int seaBattleFailed() {
        return -bonusPoint;
    }
}

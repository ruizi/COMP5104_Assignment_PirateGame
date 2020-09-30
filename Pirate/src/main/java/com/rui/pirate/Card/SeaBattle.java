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

    public int seaBattlePoint(int swordNum) { //判断是否拿到海战分数
        if (swordNum >= Sabre) {  //如果大于等于当前海战卡上剑个数，得到该海战卡分数。
            return bonusPoint;
        } else { //否则需要减分
            return -bonusPoint;
        }
    }

    public int seaBattleFailed() {
        return -bonusPoint;
    }
}

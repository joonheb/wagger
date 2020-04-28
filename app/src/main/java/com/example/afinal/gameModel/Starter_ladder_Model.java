package com.example.afinal.gameModel;

public class Starter_ladder_Model {
    private int index;              // 내기 고유 번호
    private String bet;             // 내기 이름

    public Starter_ladder_Model() {

    }
    public Starter_ladder_Model(int index, String bet) {
        this.index = index;
        this.bet = bet;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBet() {
        return bet;
    }

    public void setBet(String bet) {
        this.bet = bet;
    }
}

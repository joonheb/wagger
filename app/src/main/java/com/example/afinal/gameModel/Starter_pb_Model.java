package com.example.afinal.gameModel;

public class Starter_pb_Model {
    private int index;              // 플레이어 고유 번호
    private String name;            // 플레이어 이름
    private int cardId;             // 고유번호에 해당되는 화투패 리소스 ID값

    public  Starter_pb_Model(){}

    public Starter_pb_Model(int index, String name, int cardId) {
        this.index = index;
        this.name = name;
        this.cardId = cardId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}

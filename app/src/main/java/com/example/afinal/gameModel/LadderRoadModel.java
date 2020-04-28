package com.example.afinal.gameModel;

import android.widget.ImageView;

public class LadderRoadModel {
    ImageView imageView;
    int loadNum;

    public LadderRoadModel() {}
    public LadderRoadModel(ImageView imageView, int loadNum) {
        this.imageView = imageView;
        this.loadNum = loadNum;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getLoadNum() {
        return loadNum;
    }

    public void setLoadNum(int loadNum) {
        this.loadNum = loadNum;
    }
}

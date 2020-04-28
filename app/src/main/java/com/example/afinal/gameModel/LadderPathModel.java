package com.example.afinal.gameModel;


import android.graphics.Path;

public class LadderPathModel {
    float x, y;
    Path path;
    int pathCnt;

    public LadderPathModel() {

    }
    public LadderPathModel(float x, float y, Path path, int pathCnt) {
        this.x = x;
        this.y = y;
        this.path = path;
        this.pathCnt = pathCnt;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getPathCnt() {
        return pathCnt;
    }

    public void setPathCnt(int pathCnt) {
        this.pathCnt = pathCnt;
    }
}

package com.vagrant.tetrisonjavafx.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreManager {
    private final IntegerProperty curScore = new SimpleIntegerProperty(0);
    private final IntegerProperty highestScore = new SimpleIntegerProperty(0);

    public ScoreManager() {
        //加载历史最高分数

    }

    public void addScore(int linesCleared) {
        int point = 0;
        switch (linesCleared) {
            case 1:
                point = 40;
                break;
            case 2:
                point = 100;
                break;
            case 3:
                point = 300;
                break;
            case 4:
                point = 1200;
                break;
            default:
                point = 0;
        }
        curScore.set(curScore.get() + point);
        if (getCurScore() > getHighestScore()) {
            setHighestScore(getCurScore());
        }
    }

    public int getCurScore() {
        return curScore.get();
    }

    public int getHighestScore() {
        return highestScore.get();
    }

    public IntegerProperty getCurScoreProperty() {
        return curScore;
    }

    public IntegerProperty getHighestScoreProperty() {
        return highestScore;
    }
    public void setHighestScore(int score){
        highestScore.set(score);
    }
    public void resetCurScore(){
        curScore.set(0);
    }
}

package com.vagrant.tetrisonjavafx.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GameRecorder implements Comparable<GameRecorder> {
    private int score;
    private String date;

    public GameRecorder() {
    }

    public GameRecorder(int score) {
        this.score = score;
        this.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String toString() {
        return "GameRecorder{" + "score=" + score + ", date='" + date + '\'' + '}';
    }

    @Override
    public int compareTo(GameRecorder other) {
        return Integer.compare(other.score, this.score);//降序
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GameRecorder other = (GameRecorder) o;
        return score == other.getScore() && date.equals(other.getDate());

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

package com.vip.vipagents.ui.slideshow;

import java.io.Serializable;

public class PokerResult implements Serializable, Comparable<PokerResult> {
    private int number = 0, score;
    private String name;

    public PokerResult(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(PokerResult o) {
        if (this.number < o.number) return 1;
        else if (this.number == o.number) return 0;
        else return -1;
    }
}

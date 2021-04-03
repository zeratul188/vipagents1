package com.vip.vipagents.ui.slideshow;

public class Mission implements Comparable<Mission>{
    private String name;
    private int time, difficulty, grade, mode;
    private boolean isClan;

    public Mission(String name, int time, int difficulty, int grade, int mode, boolean isClan) {
        this.name = name;
        this.time = time;
        this.difficulty = difficulty;
        this.grade = grade;
        this.mode = mode;
        this.isClan = isClan;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isClan() {
        return isClan;
    }

    public void setClan(boolean clan) {
        isClan = clan;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public int compareTo(Mission o) {
        if (this.time > o.time) return 1;
        else if (this.time == o.time) return 0;
        else return -1;
    }
}

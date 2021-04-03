package com.vip.vipagents.ui.slideshow;

public class Mission {
    private String name;
    private int time, difficulty, grade;
    private boolean isAttack, isPursuit, isClan;

    public Mission(String name, int time, int difficulty, int grade, boolean isAttack, boolean isPursuit, boolean isClan) {
        this.name = name;
        this.time = time;
        this.difficulty = difficulty;
        this.grade = grade;
        this.isAttack = isAttack;
        this.isPursuit = isPursuit;
        this.isClan = isClan;
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

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    public boolean isPursuit() {
        return isPursuit;
    }

    public void setPursuit(boolean pursuit) {
        isPursuit = pursuit;
    }
}

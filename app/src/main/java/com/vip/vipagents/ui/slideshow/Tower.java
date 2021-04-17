package com.vip.vipagents.ui.slideshow;

public class Tower {
    private int grade, type, x, y;

    public Tower(int x, int y, int grade, int type) {
        this.grade = grade;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
        grade = 0;
        type = 0;
    }

    public void levelUp() {
        grade++;
        type = (int)(Math.random()*123456)%8+1;
    }

    public void reset() {
        grade = 0;
        type = 0;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

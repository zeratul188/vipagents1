package com.vip.vipagents.ui.home;

public class Temp_Member {
    private String id;
    private int grade;
    private boolean isClan;

    public Temp_Member(String id, int grade, boolean isClan) {
        this.id = id;
        this.grade = grade;
        this.isClan = isClan;
    }

    public boolean isClan() {
        return isClan;
    }

    public void setClan(boolean clan) {
        isClan = clan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}

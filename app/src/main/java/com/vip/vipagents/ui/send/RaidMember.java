package com.vip.vipagents.ui.send;

public class RaidMember {
    private String id;
    private int grade;
    private boolean isCommander, isClan;

    public RaidMember(String id, int grade, boolean isCommander, boolean isClan) {
        this.id = id;
        this.grade = grade;
        this.isCommander = isCommander;
        this.isClan = isClan;
    }

    public boolean isCommander() {
        return isCommander;
    }

    public void setCommander(boolean commander) {
        isCommander = commander;
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

    public boolean isClan() {
        return isClan;
    }

    public void setClan(boolean clan) {
        isClan = clan;
    }
}

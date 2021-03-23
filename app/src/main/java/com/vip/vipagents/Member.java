package com.vip.vipagents;

import java.io.Serializable;

public class Member implements Serializable {
    private String id, pwd;
    private int grade;
    private boolean isClan;

    public Member(String id, String pwd, int grade, boolean isClan) {
        this.id = id;
        this.pwd = pwd;
        this.grade = grade;
        this.isClan = isClan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

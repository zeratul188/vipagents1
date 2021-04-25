package com.vip.vipagents;

import java.io.Serializable;

public class Member implements Serializable, Comparable<Member> {
    private String id, pwd;
    private int grade, exp;
    private boolean isClan;

    public Member(String id, String pwd, int grade, boolean isClan, int exp) {
        this.id = id;
        this.pwd = pwd;
        this.grade = grade;
        this.isClan = isClan;
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
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

    @Override
    public int compareTo(Member member) {
        if (this.isClan && !member.isClan) return -1;
        if (!this.isClan && member.isClan) return 1;
        if (this.grade < member.grade) return 1;
        else return -1;
    }
}

package com.vip.vipagents;

import java.io.Serializable;

public class Member implements Serializable {
    private String id, pwd;
    private int grade;

    public Member(String id, String pwd, int grade) {
        this.id = id;
        this.pwd = pwd;
        this.grade = grade;
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
}

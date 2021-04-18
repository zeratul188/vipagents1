package com.vip.vipagents.ui.slideshow;

import com.vip.vipagents.Member;

import java.io.Serializable;

public class MemberTower implements Serializable, Comparable<MemberTower> {
    private String name;
    private int grade, normal, rare, epic, elite, legend, demage;

    public MemberTower(String name, int grade, int normal, int rare, int epic, int elite, int legend) {
        this.name = name;
        this.grade = grade;
        this.normal = normal;
        this.rare = rare;
        this.epic = epic;
        this.elite = elite;
        this.legend = legend;
        demage = (normal*75) + (rare*225) + (epic*675) + (elite*2025) + (legend*6075);
    }

    public int getDemage() {
        return demage;
    }

    public void setDemage(int demage) {
        this.demage = demage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getRare() {
        return rare;
    }

    public void setRare(int rare) {
        this.rare = rare;
    }

    public int getEpic() {
        return epic;
    }

    public void setEpic(int epic) {
        this.epic = epic;
    }

    public int getElite() {
        return elite;
    }

    public void setElite(int elite) {
        this.elite = elite;
    }

    public int getLegend() {
        return legend;
    }

    public void setLegend(int legend) {
        this.legend = legend;
    }

    @Override
    public int compareTo(MemberTower o) {
        if (this.demage < o.demage) return 1;
        else if (this.demage == o.demage) return 0;
        else return -1;
    }
}

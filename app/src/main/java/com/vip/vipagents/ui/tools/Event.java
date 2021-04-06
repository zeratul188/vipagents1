package com.vip.vipagents.ui.tools;

import java.io.Serializable;

public class Event implements Serializable {
    private String title, date, start, end, content;
    private int number, limit, play;

    public Event(int number, String title, String date, String start, String end, String content, int limit, int play) {
        this.number = number;
        this.title = title;
        this.date = date;
        this.start = start;
        this.end = end;
        this.content = content;
        this.limit = limit;
        this.play = play;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }
}

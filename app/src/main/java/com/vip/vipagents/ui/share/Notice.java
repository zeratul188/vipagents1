package com.vip.vipagents.ui.share;

import java.io.Serializable;

public class Notice implements Serializable {
    private String title, writer, content, date;
    private int view, number;

    public Notice(int number, String title, String writer, String content, int view, String date) {
        this.number = number;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.view = view;
        this.date = date;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}

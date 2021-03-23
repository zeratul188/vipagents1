package com.vip.vipagents.ui.share;

import java.util.Date;

public class Notice {
    private String title, writer, content;
    private int view;
    private Date date;

    public Notice(String title, String writer, String content, int view, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

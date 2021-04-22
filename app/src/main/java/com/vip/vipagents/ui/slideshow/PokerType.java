package com.vip.vipagents.ui.slideshow;

public class PokerType {
    private String content;
    private int count;

    public PokerType(String content, int count) {
        this.content = content;
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.wj.work.widget.entity;

public class SifterTag {
    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public SifterTag(int id, String text) {
        this.id = id;
        this.text = text;
    }
}
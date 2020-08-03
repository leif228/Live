package com.wj.work.widget.entity;

public class LiveTypeEntity {

    private String text;
    private int id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LiveTypeEntity(int id, String text) {
        this.text = text;
        this.id = id;
    }
}

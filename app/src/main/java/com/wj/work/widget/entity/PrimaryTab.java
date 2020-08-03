package com.wj.work.widget.entity;

import com.wj.work.widget.entity.SubTab;

import java.util.List;

public class PrimaryTab {

    private int id;
    private String text;
    private List<com.wj.work.widget.entity.SubTab> list;

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

    public List<com.wj.work.widget.entity.SubTab> getList() {
        return list;
    }

    public void setList(List<SubTab> list) {
        this.list = list;
    }
}

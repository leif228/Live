package com.wj.work.widget.entity;

public class ConditionTab {

    private String text;
    private int id;
    private boolean isSelect;
    private int img;
    private int mode; // 价格状态   0:乱序   1:升序  -1:降序

    public ConditionTab(int id, String text, int img, boolean isSelect) {
        this.text = text;
        this.id = id;
        this.isSelect = isSelect;
        this.img = img;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}

package com.wj.work.widget.entity;

// 清晰度
public class DefinitionEntity {
    private int id;
    private String describe;

    public DefinitionEntity(int id, String describe) {
        this.id = id;
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}

package com.wj.work.widget.entity;

/**
 * StoreEntity
 * 2020/4/7 14:23
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class StoreEntity {


    private boolean attentionTag;
    private String name;
    private String avatar;
    private int fenceNum;

    public boolean isAttentionTag() {
        return attentionTag;
    }

    public void setAttentionTag(boolean attentionTag) {
        this.attentionTag = attentionTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFenceNum() {
        return fenceNum;
    }

    public void setFenceNum(int fenceNum) {
        this.fenceNum = fenceNum;
    }
}

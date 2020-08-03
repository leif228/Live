package com.wj.work.widget.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * LiveComment
 * 2020/4/22 14:50
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LiveComment implements MultiItemEntity {

    public static final int TYPE_SYSTEM=1;
    public static final int TYPE_NORMAL=2;
    public static final int TYPE_ENTER=3;


    private String content;
    private String userName;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}

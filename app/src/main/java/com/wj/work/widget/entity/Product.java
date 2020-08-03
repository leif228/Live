package com.wj.work.widget.entity;

/**
 * Product
 * 2020/4/2 19:29
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class Product {

    private int id;
    private String name;
    private String img;
    private String price;
    private String describe;
    private boolean selectTag; // 是否选中
    private boolean attentionTag;// 是否关注

    public boolean isAttentionTag() {
        return attentionTag;
    }

    public void setAttentionTag(boolean attentionTag) {
        this.attentionTag = attentionTag;
    }

    public boolean isSelectTag() {
        return selectTag;
    }

    public void setSelectTag(boolean selectTag) {
        this.selectTag = selectTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}

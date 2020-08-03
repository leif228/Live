package com.wj.work.widget.entity;

/**
 * OrderEntity
 * 2020/4/30 14:38
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class OrderEntity {

    private String name="张三";
    private String avatar="";
    private String describe="天然翡翠A货18K镶金伴钻冰糯种飘绿如意吊坠，含金尺寸46.4-21.6-6.7mm，裸石尺...";
    private int status=1;
    private int number=2;
    private float price=1290.0f;
    private float fee=10.0f;


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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}

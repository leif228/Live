package com.wj.work.widget.entity;

public class SifterEntity {

    private float minPrice;
    private float maxPrice;
    private float minCommission;
    private float maxCommission;
    private int keyid;

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public float getMinCommission() {
        return minCommission;
    }

    public void setMinCommission(float minCommission) {
        this.minCommission = minCommission;
    }

    public float getMaxCommission() {
        return maxCommission;
    }

    public void setMaxCommission(float maxCommission) {
        this.maxCommission = maxCommission;
    }

    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public boolean hasSifter(){
        return minPrice>0||maxPrice>0||minCommission>0||maxCommission>0||keyid>0;
    }
}

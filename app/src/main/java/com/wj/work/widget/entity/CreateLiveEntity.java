package com.wj.work.widget.entity;

import java.io.Serializable;

/**
 * CreateLiveEntity
 * 2020/5/8 9:56
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CreateLiveEntity implements Serializable {

    // "addTime": "2020-05-08T01:29:51.541Z",
    //    "addUserid": 0,
    //    "deleteFlag": 0,
    //    "endTime": "2020-05-08T01:29:51.541Z",
    //    "id": 0,
    //    "latitude": "string",
    //    "likes": 0,
    //    "liveAddress": "string",
    //    "liveCover": "string",
    //    "liveTitle": "string",
    //    "longitude": "string",
    //    "memo1": "string",
    //    "memo2": "string",
    //    "memo3": "string",
    //    "preBroadcastTime": "2020-05-08T01:29:51.541Z",
    //    "recommendGoods": "string",
    //    "sharpnessType": 0,
    //    "shopId": 0,
    //    "status": 0,
    //    "typeId": 0,
    //    "typeSub": 0,
    //    "updataBy": 0,
    //    "updateTime": "2020-05-08T01:29:51.541Z",
    //    "viewers": 0


    private String addTime;
    private long addUserid;
    private String endTime;
    private long id;
    private String latitude;
    private String likes;
    private String liveAddress;
    private String liveCover;
    private String liveTitle;
    private String longitude;
    private String preBroadcastTime;
    private String recommendGoods;
    private String sharpnessType;
    private long shopId;
    private int typeId;
    private int typeSub;
    private long viewers;


    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getAddUserid() {
        return addUserid;
    }

    public void setAddUserid(long addUserid) {
        this.addUserid = addUserid;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLiveAddress() {
        return liveAddress;
    }

    public void setLiveAddress(String liveAddress) {
        this.liveAddress = liveAddress;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPreBroadcastTime() {
        return preBroadcastTime;
    }

    public void setPreBroadcastTime(String preBroadcastTime) {
        this.preBroadcastTime = preBroadcastTime;
    }

    public String getRecommendGoods() {
        return recommendGoods;
    }

    public void setRecommendGoods(String recommendGoods) {
        this.recommendGoods = recommendGoods;
    }

    public String getSharpnessType() {
        return sharpnessType;
    }

    public void setSharpnessType(String sharpnessType) {
        this.sharpnessType = sharpnessType;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeSub() {
        return typeSub;
    }

    public void setTypeSub(int typeSub) {
        this.typeSub = typeSub;
    }

    public long getViewers() {
        return viewers;
    }

    public void setViewers(long viewers) {
        this.viewers = viewers;
    }
}

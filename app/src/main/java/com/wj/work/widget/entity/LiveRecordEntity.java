package com.wj.work.widget.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class LiveRecordEntity implements MultiItemEntity {

    public final static int STATUS_PREVIEW=0x001;
    public final static int STATUS_FINISHED=0x002;
    public final static int STATUS_PAUSE=0x003;

    private long id;
    private String liveTitle;
    private String liveCover;
    private String recommendGoods;
    private int viewers;
    private int likes;
    private int maxOnlineUsers;
    private int shares;
    private int goodsClick;
    private int typeId;
    private int typeSub;
    private int sharpnessType;
    private int status;
    private String preBroadcastTime;
    private String endTime;
    private String addTime;

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public void setLiveCover(String liveCover) {
        this.liveCover = liveCover;
    }

    public String getRecommendGoods() {
        return recommendGoods;
    }

    public void setRecommendGoods(String recommendGoods) {
        this.recommendGoods = recommendGoods;
    }

    public int getViewers() {
        return viewers;
    }

    public void setViewers(int viewers) {
        this.viewers = viewers;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getMaxOnlineUsers() {
        return maxOnlineUsers;
    }

    public void setMaxOnlineUsers(int maxOnlineUsers) {
        this.maxOnlineUsers = maxOnlineUsers;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getGoodsClick() {
        return goodsClick;
    }

    public void setGoodsClick(int goodsClick) {
        this.goodsClick = goodsClick;
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

    public int getSharpnessType() {
        return sharpnessType;
    }

    public void setSharpnessType(int sharpnessType) {
        this.sharpnessType = sharpnessType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPreBroadcastTime() {
        return preBroadcastTime;
    }

    public void setPreBroadcastTime(String preBroadcastTime) {
        this.preBroadcastTime = preBroadcastTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getItemType() {
        int result=STATUS_FINISHED;
        if (status==0)result=STATUS_PAUSE;
        if (status==2)result=STATUS_PREVIEW;
        return result;
    }
}

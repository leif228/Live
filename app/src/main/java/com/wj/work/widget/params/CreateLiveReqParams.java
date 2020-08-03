package com.wj.work.widget.params;

/**
 * CreateLiveReqParams
 * 2020/5/8 9:34
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CreateLiveReqParams {

    private String latitude;
    private String liveCover;
    private String liveTitle;
    private String longitude;
    private String preBroadcastTime;
    private String pushFollowUsersType;
    private String recommendGoods;
    private String sharpnessType;
    private String typeId;
    private String typeSub;

    public String getLatitude() {
        return latitude;
    }

    public CreateLiveReqParams setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLiveCover() {
        return liveCover;
    }

    public CreateLiveReqParams setLiveCover(String liveCover) {
        this.liveCover = liveCover;
        return this;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public CreateLiveReqParams setLiveTitle(String liveTitle) {
        this.liveTitle = liveTitle;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public CreateLiveReqParams setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getPreBroadcastTime() {
        return preBroadcastTime;
    }

    public CreateLiveReqParams setPreBroadcastTime(String preBroadcastTime) {
        this.preBroadcastTime = preBroadcastTime;
        return this;
    }

    public String getPushFollowUsersType() {
        return pushFollowUsersType;
    }

    public CreateLiveReqParams setPushFollowUsersType(String pushFollowUsersType) {
        this.pushFollowUsersType = pushFollowUsersType;
        return this;
    }

    public String getRecommendGoods() {
        return recommendGoods;
    }

    public CreateLiveReqParams setRecommendGoods(String recommendGoods) {
        this.recommendGoods = recommendGoods;
        return this;
    }

    public String getSharpnessType() {
        return sharpnessType;
    }

    public CreateLiveReqParams setSharpnessType(String sharpnessType) {
        this.sharpnessType = sharpnessType;
        return this;
    }

    public String getTypeId() {
        return typeId;
    }

    public CreateLiveReqParams setTypeId(String typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getTypeSub() {
        return typeSub;
    }

    public CreateLiveReqParams setTypeSub(String typeSub) {
        this.typeSub = typeSub;
        return this;
    }
}

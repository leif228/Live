package com.littlegreens.netty.client.extra.task;

import com.alibaba.fastjson.annotation.JSONField;

public class NewClubAtParamDto {

    private String relativeEventNo;
    private String eventNo;

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getRelativeEventNo() {
        return relativeEventNo;
    }

    public void setRelativeEventNo(String relativeEventNo) {
        this.relativeEventNo = relativeEventNo;
    }
}

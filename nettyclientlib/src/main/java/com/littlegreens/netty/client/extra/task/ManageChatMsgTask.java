package com.littlegreens.netty.client.extra.task;

import com.alibaba.fastjson.annotation.JSONField;

public class ManageChatMsgTask extends ManageChatMsgAtParam {

    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

}

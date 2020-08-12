package com.littlegreens.netty.client.extra;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class LoginTask extends BaseTask {
    @JSONField(name="OID")
    private String OID;

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }
}

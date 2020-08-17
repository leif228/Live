package com.littlegreens.netty.client.extra.task;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginTask extends BaseTask {
    @JSONField(name = "OID")
    private String OID;
    @JSONField(name = "PassNum")
    private String PassNum;

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public String getPassNum() {
        return PassNum;
    }

    public void setPassNum(String passNum) {
        PassNum = passNum;
    }
}

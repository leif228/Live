package com.littlegreens.netty.client.extra.task;

import com.alibaba.fastjson.annotation.JSONField;

public class NetSearchNetTask extends BaseTask {
    @JSONField(name="Devtype")
    private String Devtype;
    @JSONField(name="OID")
    private String OID;

    public String getDevtype() {
        return Devtype;
    }

    public void setDevtype(String devtype) {
        Devtype = devtype;
    }

    public String getOID() {
        return OID;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }
}

package com.littlegreens.netty.client.extra;

import java.io.Serializable;

public class LoginTask extends BaseTask {
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}

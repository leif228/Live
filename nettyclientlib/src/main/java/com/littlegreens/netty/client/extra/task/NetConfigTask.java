package com.littlegreens.netty.client.extra.task;

import com.alibaba.fastjson.annotation.JSONField;

public class NetConfigTask extends BaseTask {
    @JSONField(name="Devtype")
    private String Devtype;
    @JSONField(name="Inaeraaddr")
    private String Inaeraaddr;

    public String getDevtype() {
        return Devtype;
    }

    public void setDevtype(String devtype) {
        Devtype = devtype;
    }

    public String getInaeraaddr() {
        return Inaeraaddr;
    }

    public void setInaeraaddr(String inaeraaddr) {
        Inaeraaddr = inaeraaddr;
    }
}

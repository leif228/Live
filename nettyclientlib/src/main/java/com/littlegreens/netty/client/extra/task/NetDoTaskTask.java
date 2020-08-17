package com.littlegreens.netty.client.extra.task;

import com.alibaba.fastjson.annotation.JSONField;

public class NetDoTaskTask extends BaseTask {
    @JSONField(name="PassNum")
    private String PassNum;
    @JSONField(name="PRO")
    private String PRO;

    public String getPassNum() {
        return PassNum;
    }

    public void setPassNum(String passNum) {
        PassNum = passNum;
    }

    public String getPRO() {
        return PRO;
    }

    public void setPRO(String PRO) {
        this.PRO = PRO;
    }
}

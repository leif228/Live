package com.littlegreens.netty.client.extra;

import com.alibaba.fastjson.annotation.JSONField;

public class NetDevCompTask extends BaseTask {
    @JSONField(name="CompanyName")
    private String CompanyName;
    @JSONField(name="PRO")
    private String PRO;

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getPRO() {
        return PRO;
    }

    public void setPRO(String PRO) {
        this.PRO = PRO;
    }
}

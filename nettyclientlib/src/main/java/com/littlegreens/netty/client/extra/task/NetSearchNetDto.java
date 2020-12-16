package com.littlegreens.netty.client.extra.task;

import java.io.Serializable;

public class NetSearchNetDto implements Serializable {
    private String ip;
    private NetSearchNetTask netSearchNetTask;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public NetSearchNetTask getNetSearchNetTask() {
        return netSearchNetTask;
    }

    public void setNetSearchNetTask(NetSearchNetTask netSearchNetTask) {
        this.netSearchNetTask = netSearchNetTask;
    }
}

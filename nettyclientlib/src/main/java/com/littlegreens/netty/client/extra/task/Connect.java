package com.littlegreens.netty.client.extra.task;

import com.littlegreens.netty.client.extra.task.BaseTask;

public class Connect extends BaseTask {
    private String ip;
    private String port;
    private String fzwno;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFzwno() {
        return fzwno;
    }

    public void setFzwno(String fzwno) {
        this.fzwno = fzwno;
    }
}

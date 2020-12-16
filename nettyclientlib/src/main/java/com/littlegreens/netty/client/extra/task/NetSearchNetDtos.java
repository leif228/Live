package com.littlegreens.netty.client.extra.task;

import java.util.ArrayList;
import java.util.List;

public class NetSearchNetDtos extends BaseTask{
    private List<NetSearchNetDto>   netSearchNetDtos = new ArrayList<>();

    public List<NetSearchNetDto> getNetSearchNetDtos() {
        return netSearchNetDtos;
    }

    public void setNetSearchNetDtos(List<NetSearchNetDto> netSearchNetDtos) {
        this.netSearchNetDtos = netSearchNetDtos;
    }
}

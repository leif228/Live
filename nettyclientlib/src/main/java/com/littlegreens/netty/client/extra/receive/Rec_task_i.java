package com.littlegreens.netty.client.extra.receive;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.NettyTcpClient;

import io.netty.channel.ChannelHandlerContext;

public interface Rec_task_i {
    public void doTask(ChannelHandlerContext ctx, String tx, JSONObject objParam, NettyTcpClient nettyTcpClient);
}

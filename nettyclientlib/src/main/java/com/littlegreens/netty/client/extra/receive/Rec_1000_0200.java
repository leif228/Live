package com.littlegreens.netty.client.extra.receive;


import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.NettyTcpClient;
import com.littlegreens.netty.client.extra.WjDecoderHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * at 业务
 * 手机←→网关 业务
 */
public class Rec_1000_0200 implements Rec_task_i{
    @Override
    public void doTask(ChannelHandlerContext ctx, String tx, JSONObject objParam, NettyTcpClient nettyTcpClient) {
        Log.v(WjDecoderHandler.TAG, "at业务:" + tx);
        nettyTcpClient.atTask(tx, objParam);
    }

}

package com.littlegreens.netty.client.extra.receive;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.NettyTcpClient;
import com.littlegreens.netty.client.extra.WjDecoderHandler;

import io.netty.channel.ChannelHandlerContext;

public class Rec_1200_0400 implements Rec_task_i{
    @Override
    public void doTask(ChannelHandlerContext ctx, String tx, JSONObject objParam, NettyTcpClient nettyTcpClient) {
        Log.v(WjDecoderHandler.TAG, "手机配置网关:" + tx);
//        nettyTcpClient.nettyNetGetDevListOver(tx, objParam);
    }
}

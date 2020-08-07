package com.littlegreens.netty.client.extra;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.NettyTcpClient;

import io.netty.channel.ChannelHandlerContext;

public class TaskHandler {
    NettyTcpClient nettyTcpClient;

    public TaskHandler(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    public void doProtocol(ChannelHandlerContext ctx, WjProtocol wjProtocol) {
        JSONObject objParam = null;
        String tx = "";

        if (WjProtocol.FORMAT_TX.equals(wjProtocol.getFormat())) {
            if(wjProtocol.getUserdata() != null){
                String dataStr = new String(wjProtocol.getUserdata());
                tx = dataStr;
            }
        } else if (WjProtocol.FORMAT_JS.equals(wjProtocol.getFormat())) {
            if(wjProtocol.getUserdata() != null){
                String jsonStr = new String(wjProtocol.getUserdata());
                Log.v(WjDecoderHandler.TAG,jsonStr);
                objParam = JSONObject.parseObject(jsonStr);
            }
        } else if (WjProtocol.FORMAT_AT.equals(wjProtocol.getFormat())) {
            if(wjProtocol.getUserdata() != null){
                tx = new String(wjProtocol.getUserdata());
            }
        }

        //======业务处理======
        if (wjProtocol.byte2shortSmall(wjProtocol.getMaincmd()) == 0 && wjProtocol.byte2shortSmall(wjProtocol.getSubcmd()) == 0) {//终端→服务 心跳ping
            this.nettyIdle(ctx, tx);
        }
        if (wjProtocol.byte2shortSmall(wjProtocol.getMaincmd()) == 12 && wjProtocol.byte2shortSmall(wjProtocol.getSubcmd()) == 0) {//手机←网关 搜索返回
            this.nettyNetSearchBack(ctx, tx);
        }
    }

    private void nettyIdle(ChannelHandlerContext ctx, String tx) {
        Log.v(WjDecoderHandler.TAG,"心跳:" + tx);
    }

    private void nettyNetSearchBack(ChannelHandlerContext ctx, String tx) {
        Log.v(WjDecoderHandler.TAG,"nettyNetSearchBack:" + tx);
        nettyTcpClient.nettyNetSearchBack();
    }


    public void sendIdleData(ChannelHandlerContext ctx) {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50,0x00});
        wjProtocol.setMaincmd(new byte[]{0x00,0x00});
        wjProtocol.setSubcmd(new byte[]{0x00,0x00});
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(new byte[]{0x00,0x00});

        int len = WjProtocol.MIN_DATA_LEN + 0;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));

        ctx.write(wjProtocol);

        ctx.flush();
    }
}

package com.littlegreens.netty.client.extra;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.NettyTcpClient;

import java.util.Arrays;

import io.netty.channel.ChannelHandlerContext;

public class TaskHandler {
    NettyTcpClient nettyTcpClient;

    public TaskHandler(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    public void doProtocol(ChannelHandlerContext ctx, WjProtocol wjProtocol) {
        JSONObject objParam = null;
        String tx = null;

        if (WjProtocol.FORMAT_TX.equals(wjProtocol.getFormat())) {
            if (wjProtocol.getUserdata() != null) {
                String dataStr = new String(wjProtocol.getUserdata());
                tx = dataStr;
            }
        } else if (WjProtocol.FORMAT_JS.equals(wjProtocol.getFormat())) {
            if (wjProtocol.getUserdata() != null) {
                String jsonStr = new String(wjProtocol.getUserdata());
                Log.v(WjDecoderHandler.TAG, "tcp收到的jsonStr:" + jsonStr);
                objParam = JSONObject.parseObject(jsonStr);
            }
        } else if (WjProtocol.FORMAT_AT.equals(wjProtocol.getFormat())) {
            if (wjProtocol.getUserdata() != null) {
                tx = new String(wjProtocol.getUserdata());
            }
        }

        //======业务处理======
        if (Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
                && Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//终端←服务 心跳
            this.nettyIdle(ctx, tx);
        }
        if (Arrays.toString(new Byte[]{0x12, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
                && Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 搜索返回
            this.nettyNetSearchBack(ctx, tx);
        }
        if (Arrays.toString(new Byte[]{0x12, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
                && Arrays.toString(new Byte[]{0x01, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 驱动下载完成
            this.nettyNetDownOver(ctx, tx, objParam);
        }
        if (Arrays.toString(new Byte[]{0x12, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
                && Arrays.toString(new Byte[]{0x02, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 获取设备列表完成，页面打开配置页
            this.nettyNetGetDevListOver(ctx, tx, objParam);
        }
        if (Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
                && Arrays.toString(new Byte[]{0x03, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 网关返回任务进度
            this.nettyNetTaskBacking(ctx, tx, objParam);
        }
    }

    private void nettyNetTaskBacking(ChannelHandlerContext ctx, String tx, JSONObject objParam) {
        Log.v(WjDecoderHandler.TAG, "网关返回任务进度:" + tx);
    }

    private void nettyNetGetDevListOver(ChannelHandlerContext ctx, String tx, JSONObject objParam) {
        Log.v(WjDecoderHandler.TAG, "获取设备列表完成，页面打开配置页:" + tx);
    }

    private void nettyNetDownOver(ChannelHandlerContext ctx, String tx, JSONObject objParam) {
        Log.v(WjDecoderHandler.TAG, "驱动下载完成:" + tx);
    }

    private void nettyIdle(ChannelHandlerContext ctx, String tx) {
        Log.v(WjDecoderHandler.TAG, "心跳:" + tx);
    }

    private void nettyNetSearchBack(ChannelHandlerContext ctx, String tx) {
        Log.v(WjDecoderHandler.TAG, "搜索返回:" + tx);
        nettyTcpClient.nettyNetSearchBack();
    }


    public void sendIdleData(ChannelHandlerContext ctx) {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
        wjProtocol.setSubcmd(new byte[]{0x00, 0x00});
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

        int len = WjProtocol.MIN_DATA_LEN + 0;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));

        ctx.write(wjProtocol);

        ctx.flush();
    }
}

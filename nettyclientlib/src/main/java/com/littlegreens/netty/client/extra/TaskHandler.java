package com.littlegreens.netty.client.extra;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.NettyTcpClient;
import com.littlegreens.netty.client.extra.receive.Rec_task_i;
import com.littlegreens.netty.client.extra.send.Sen_0000_0000;
import com.littlegreens.netty.client.extra.send.Sen_0000_0300;
import com.littlegreens.netty.client.extra.send.Sen_factory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import io.netty.channel.ChannelHandlerContext;

public class TaskHandler {
    NettyTcpClient nettyTcpClient;

    public TaskHandler(NettyTcpClient nettyTcpClient) {
        this.nettyTcpClient = nettyTcpClient;
    }

    public void doProtocol(ChannelHandlerContext ctx, WjProtocol wjProtocol) throws UnsupportedEncodingException {
        JSONObject objParam = null;
        String tx = null;

        if (WjProtocol.FORMAT_TX.equals(wjProtocol.getFormat())) {
            if (wjProtocol.getUserdata() != null) {
                String dataStr = new String(wjProtocol.getUserdata(),"UTF-8");
                tx = dataStr;
            }
        } else if (WjProtocol.FORMAT_JS.equals(wjProtocol.getFormat())) {
            if (wjProtocol.getUserdata() != null) {
                String jsonStr = new String(wjProtocol.getUserdata(),"UTF-8");
                Log.v(WjDecoderHandler.TAG, "tcp收到的jsonStr:" + jsonStr);
                objParam = JSONObject.parseObject(jsonStr);
            }
        } else if (WjProtocol.FORMAT_AT.equals(wjProtocol.getFormat())) {
            if (wjProtocol.getUserdata() != null) {
                tx = new String(wjProtocol.getUserdata(),"UTF-8");
            }
        }

        //通用处理
        try {
            String classPath = "com.littlegreens.netty.client.extra.receive";
            String className = "Rec_" + wjProtocol.IntToHexStringLimit2(wjProtocol.getMaincmd()[0]) + wjProtocol.IntToHexStringLimit2(wjProtocol.getMaincmd()[1]) + "_"
                    + wjProtocol.IntToHexStringLimit2(wjProtocol.getSubcmd()[0]) + wjProtocol.IntToHexStringLimit2(wjProtocol.getSubcmd()[1]);
            className = classPath + "." + className;
            Log.v(WjDecoderHandler.TAG, "className:" + className);
            Class genClass = Class.forName(className);
            Rec_task_i rec_task_i = (Rec_task_i) genClass.newInstance();
            rec_task_i.doTask(ctx, tx, objParam, nettyTcpClient);
        } catch (Exception e) {
            Log.e(WjDecoderHandler.TAG, "TaskHandler.doProtocol_报错了:" + e.getMessage());
        }

        //======业务处理======
//        if (Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
//                && Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//终端←服务 心跳
//            this.nettyIdle(ctx, tx);
//        }
//        if (Arrays.toString(new Byte[]{0x12, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
//                && Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 搜索返回
//            this.nettyNetSearchBack(ctx, tx);
//        }
//        if (Arrays.toString(new Byte[]{0x12, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
//                && Arrays.toString(new Byte[]{0x01, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 驱动下载完成
//            this.nettyNetDownOver(ctx, tx, objParam);
//        }
//        if (Arrays.toString(new Byte[]{0x12, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
//                && Arrays.toString(new Byte[]{0x02, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 获取设备列表完成，页面打开配置页
//            this.nettyNetGetDevListOver(ctx, tx, objParam);
//        }
//        if (Arrays.toString(new Byte[]{0x00, 0x00}).equals(Arrays.toString(wjProtocol.getMaincmd()))
//                && Arrays.toString(new Byte[]{0x03, 0x00}).equals(Arrays.toString(wjProtocol.getSubcmd()))) {//手机←网关 网关返回任务进度
//            this.nettyNetTaskBacking(ctx, tx, objParam);
//        }

    }

//    private void nettyNetTaskBacking(ChannelHandlerContext ctx, String tx, JSONObject objParam) {
//        Log.v(WjDecoderHandler.TAG, "网关返回任务进度:" + objParam.toJSONString());
//    }
//
//    private void nettyNetGetDevListOver(ChannelHandlerContext ctx, String tx, JSONObject objParam) {
//        Log.v(WjDecoderHandler.TAG, "获取设备列表完成，页面打开配置页:" + objParam.toJSONString());
//        nettyTcpClient.nettyNetGetDevListOver(tx, objParam);
//    }
//
//    private void nettyNetDownOver(ChannelHandlerContext ctx, String tx, JSONObject objParam) {
//        Log.v(WjDecoderHandler.TAG, "驱动下载完成:" + objParam.toJSONString());
//    }
//
//    private void nettyIdle(ChannelHandlerContext ctx, String tx) {
//        Log.v(WjDecoderHandler.TAG, "心跳:" + tx);
//    }
//
//    private void nettyNetSearchBack(ChannelHandlerContext ctx, String tx) {
//        Log.v(WjDecoderHandler.TAG, "搜索返回:" + tx);
//        nettyTcpClient.nettyNetSearchBack();
//    }


    public void sendIdleData(ChannelHandlerContext ctx) {
//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x00, 0x00});
//        wjProtocol.setFormat("TX");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        int len = WjProtocol.MIN_DATA_LEN + 0;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_0000_0000.main, Sen_0000_0000.sub,null);
        if(wjProtocol == null)
            return;

        ctx.write(wjProtocol);

        ctx.flush();
    }
}

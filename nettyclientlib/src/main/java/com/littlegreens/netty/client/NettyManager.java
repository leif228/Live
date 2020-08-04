package com.littlegreens.netty.client;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.extra.LoginTask;
import com.littlegreens.netty.client.extra.NetInfoTask;
import com.littlegreens.netty.client.extra.NettyConfig;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.MessageStateListener;
import com.littlegreens.netty.client.listener.NettyClientListener;

import java.util.Properties;

/**
 * NettyManager
 * 2020/5/12 16:14
 * wj
 * wj
 *
 * @author Ly
 */
public class NettyManager {

    private NettyTcpClient mNettyTcpClient;

    public NettyManager(int index, String ip, int port) {
        mNettyTcpClient = new NettyTcpClient.Builder()
                .setURL(NettyConfig.SOCKET_DOMAIN)
                .setHost(ip)    //设置服务端地址
                .setPort(port) //设置服务端端口号
                .setMaxReconnectTimes(5)    //设置最大重连次数
                .setReconnectIntervalTime(4000)    //设置重连间隔时间。单位：秒
                .setSendHeartBeat(true) //设置是否发送心跳
                .setHeartBeatInterval(30)    //设置心跳间隔时间。单位：秒
                .setHeartBeatData("jxj") //设置心跳数据，可以是String类型，也可以是byte[]，以后设置的为准
                .setIndex(index)    //设置客户端标识.(因为可能存在多个tcp连接)
//                .setPacketSeparator("#")//用特殊字符，作为分隔符，解决粘包问题，默认是用换行符作为分隔符
                .setMaxPacketLong(2048)//设置一次发送数据的最大长度，默认是1024
                .build();
    }

    // 监听
    public void setNettyClientListener(NettyClientListener listener) {
        mNettyTcpClient.setListener(listener);
    }

    // 首次连接 或者断网之后的重新创建
    public void connect() {
        mNettyTcpClient.connect();
    }

    // 重连
    public void attemptReConnect() {
        mNettyTcpClient.attemptReConnect();
    }

    // 发送消息
    public void senMessage(String msg) {
        mNettyTcpClient.sendMsgToServer(msg);
    }

    // 发送消息
    public void senMessage(String msg, final MessageStateListener listener) {
        mNettyTcpClient.sendMsgToServer(msg, listener);
    }

    /**
     * 搜索网关消息
     */
    public void sendMsgToSearchNet() {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(Short.parseShort("50"));
        wjProtocol.setMaincmd(Short.parseShort("12"));
        wjProtocol.setSubcmd(Short.parseShort("0"));
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(Short.parseShort("0"));

//        String jsonStr = "pipo";
//        byte[] objectBytes = jsonStr.getBytes();

        int len = 21 + 0;
        wjProtocol.setLen((short) len);
//        wjProtocol.setUserdata(objectBytes);

        mNettyTcpClient.sendMsgToServerF(wjProtocol, null);
    }

    // release 断开连接
    public void release() {
        mNettyTcpClient.release();
    }

    public void sendMsgToManageLoin(String fzwno) {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(Short.parseShort("20"));
        wjProtocol.setMaincmd(Short.parseShort("0"));
        wjProtocol.setSubcmd(Short.parseShort("1"));
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(Short.parseShort("0"));

        LoginTask loginTask = new LoginTask();
        loginTask.setOid(fzwno);

//        byte [] objectBytes= ByteUtils.InstanceObjectMapper().writeValueAsBytes(loginTask);

        String jsonStr = JSONObject.toJSONString(loginTask);
        Log.v("ly", jsonStr);
        byte[] objectBytes = jsonStr.getBytes();

        int len = 21 + objectBytes.length;
        wjProtocol.setLen((short) len);
        wjProtocol.setUserdata(objectBytes);

        mNettyTcpClient.sendMsgToServerF(wjProtocol,null);
    }

    public void sendMsgToNetInfo(NetInfoTask netInfoTask) {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(Short.parseShort("20"));
        wjProtocol.setMaincmd(Short.parseShort("0"));
        wjProtocol.setSubcmd(Short.parseShort("2"));
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(Short.parseShort("0"));

        String jsonStr = JSONObject.toJSONString(netInfoTask);
        Log.v("ly", jsonStr);
        byte[] objectBytes = jsonStr.getBytes();

        int len = 21 + objectBytes.length;
        wjProtocol.setLen((short) len);
        wjProtocol.setUserdata(objectBytes);

        mNettyTcpClient.sendMsgToServerF(wjProtocol,null);
    }
}

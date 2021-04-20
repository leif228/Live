package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.send.Sen_0000_0200;
import com.littlegreens.netty.client.extra.send.Sen_0000_0300;
import com.littlegreens.netty.client.extra.send.Sen_1000_0200;
import com.littlegreens.netty.client.extra.send.Sen_1200_0000;
import com.littlegreens.netty.client.extra.send.Sen_1200_0100;
import com.littlegreens.netty.client.extra.send.Sen_1200_0200;
import com.littlegreens.netty.client.extra.send.Sen_1200_0300;
import com.littlegreens.netty.client.extra.send.Sen_1200_0400;
import com.littlegreens.netty.client.extra.send.Sen_factory;
import com.littlegreens.netty.client.extra.task.AtTask;
import com.littlegreens.netty.client.extra.task.BaseTask;
import com.littlegreens.netty.client.extra.task.NetConfigTask;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.task.NetDevCompTask;
import com.littlegreens.netty.client.extra.task.NetDoTaskTask;
import com.littlegreens.netty.client.extra.task.NetInfoTask;
import com.littlegreens.netty.client.extra.task.NetSearchNetDto;
import com.littlegreens.netty.client.extra.task.NetSearchNetDtos;
import com.littlegreens.netty.client.extra.task.NetSearchNetTask;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.littlegreens.netty.client.status.ConnectState;
import com.wj.work.utils.BroadCastToCenter;
import com.wj.work.utils.WebViewJavaScriptFunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class NetService extends Service implements NettyClientListener {
    public static final String COUNTER = "data";
    public static final String COUNTER_TYPE = "type";
    public static final String COUNTER_ELSE = "else";
    public static final String TOAST = "toast";
    public static final Integer tcpport = 8666;
    public static final String ACTION_NAME = "com.wj.work.netservice.COUNTER_ACTION";
    Timer timer = new Timer();

    List<NetSearchNetDto> netSearchNetDtos = new ArrayList<>();

//    private String data_type;

    NettyManager nettyManager;
    Queue<WjProtocol> queue;
    String ip;

    private boolean isSearching = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LL.V("NetService onCreate");
        queue = new LinkedList<WjProtocol>();
//        getIpConnectNet();
//        sendBroadCastToCenter();
//        udpDiscardSServer();
//        sendBroadCastToCenter(new NetSearchNetTask());
    }

    private void toNetTcp(String data_type, String netIp) {
        LL.V("connectNet  ip=" + netIp);
        this.ip = netIp;
        if (nettyManager != null) {
            nettyManager.release();
            nettyManager = null;
        }

        nettyManager = new NettyManager(0, netIp, tcpport);
        nettyManager.setNettyClientListener(this);
        nettyManager.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //从Activity获取data
            String data_type = intent.getStringExtra(COUNTER_TYPE);
            WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum webViewWebSocketFuctionEnum = WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.valueOf(data_type);

            LL.V("NetService onStartCommand: " + data_type);

            switch (webViewWebSocketFuctionEnum) {
                case toNetInfo:
                    NetInfoTask netInfoTask = (NetInfoTask) intent.getSerializableExtra(COUNTER);
                    toNetInfo(data_type, netInfoTask);
                    break;
                case deviceComp:
                    NetDevCompFileTask netDevCompTask = (NetDevCompFileTask) intent.getSerializableExtra(COUNTER);
                    deviceComp(data_type, netDevCompTask);
                    break;
                case saveDevice:
                    NetDevCompTask netDevCompTask2 = (NetDevCompTask) intent.getSerializableExtra(COUNTER);
                    saveDevice(data_type, netDevCompTask2);
                    break;
                case authOver:
                    NetDevCompFileTask netDevCompFileTask = (NetDevCompFileTask) intent.getSerializableExtra(COUNTER);
                    authOver(data_type, netDevCompFileTask);
                    break;
                case toSearchNet:
                    toSearchNet(data_type, new NetSearchNetTask());
                    break;
                case toNetTcp:
                    String ip = intent.getStringExtra(COUNTER_ELSE);
                    toNetTcp(data_type, ip);
                    break;
                case toConfigNet:
                    NetConfigTask netDevCompTask3 = (NetConfigTask) intent.getSerializableExtra(COUNTER);
                    toConfigNet(data_type, netDevCompTask3);
                    break;
                case toAtNet:
                    String at = intent.getStringExtra(COUNTER_ELSE);
                    toAtNet(data_type, at);
                    break;
                default:
            }
        }catch (Exception e){
            e.printStackTrace();
            LL.V("NetService onStartCommand报错了: " + e.getMessage());
        }

//        if ("1".equals(data_type)) {
////            getIpConnectNet();
////            sendBroadCastToCenter(new NetSearchNetTask());
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.toNetInfo.name().equals(data_type)) {
//            NetInfoTask netInfoTask = (NetInfoTask) intent.getSerializableExtra(COUNTER);
//            toNetInfo(netInfoTask);
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.deviceComp.name().equals(data_type)) {
//            NetDevCompFileTask netDevCompTask = (NetDevCompFileTask) intent.getSerializableExtra(COUNTER);
//            deviceComp(netDevCompTask);
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.saveDevice.name().equals(data_type)) {
//            NetDevCompTask netDevCompTask = (NetDevCompTask) intent.getSerializableExtra(COUNTER);
//            saveDevice(netDevCompTask);
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.authOver.name().equals(data_type)) {
//            NetDevCompFileTask netDevCompFileTask = (NetDevCompFileTask) intent.getSerializableExtra(COUNTER);
//            authOver(netDevCompFileTask);
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.toSearchNet.name().equals(data_type)) {
//            toSearchNet(new NetSearchNetTask());
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.toNetTcp.name().equals(data_type)) {
//            String ip = intent.getStringExtra(COUNTER_ELSE);
//            toNetTcp(ip);
//        } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.toConfigNet.name().equals(data_type)) {
//            NetConfigTask netDevCompTask = (NetConfigTask) intent.getSerializableExtra(COUNTER);
//            toConfigNet(netDevCompTask);
//        }else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.toAtNet.name().equals(data_type)) {
//            String at = intent.getStringExtra(COUNTER_ELSE);
//            toAtNet(at);
//        }

        return START_STICKY;
    }

    private void toSearchNet(String data_type, NetSearchNetTask netSearchNetTask) {
        if (!isSearching) {
            isSearching = true;
            BroadCastToCenter broadCastToCenter = new BroadCastToCenter(netSearchNetTask);
            broadCastToCenter.start();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    netSearchNetDtos = broadCastToCenter.getNetSearchNetDtos();
                    broadCastToCenter.stopStay();
                    isSearching = false;
                    backNets(data_type);
                }
            }, 3000l);
        }
    }

    private void backNets(String data_type) {
        NetSearchNetDtos netSearchNetDtoList = new NetSearchNetDtos();
        netSearchNetDtoList.setNetSearchNetDtos(netSearchNetDtos);

        sendMsgToActivity(netSearchNetDtoList, data_type, "");
    }

    private void sendMsgToActivity(BaseTask baseTask, String type, String elses) {
        //向Activity传递data
        final Intent mIntent = new Intent();
        mIntent.setAction(ACTION_NAME);
        mIntent.putExtra(COUNTER, baseTask);
        mIntent.putExtra(COUNTER_TYPE, type);
        mIntent.putExtra(COUNTER_ELSE, elses);
        sendBroadcast(mIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMessageResponseClient(String msg, int index) {

    }

    @Override
    public void onClientStatusConnectChanged(int statusCode, int index) {
        if (statusCode == ConnectState.STATUS_CONNECT_ERROR) {
            Log.v("ly", "onClientStatusConnectChanged:===" + index);

//            this.sendMsgToActivity(null, TOAST, "tcp连接到网关失败:ip=" + ip);
        }
    }

    @Override
    public void connectSuccess(String ip, int index) {
        LL.V(ip + ":index:" + index);
        this.sendMsgToActivity(null, WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.toNetTcp.name(), ip);
//        toSearchNet();
//        this.doTask();
    }

    private void doTask() {
        boolean flag = true;

        if (nettyManager != null) {

            while (flag) {
                if (!queue.isEmpty()) {
                    nettyManager.senMessage(queue.poll());
                } else {
                    flag = false;
                }
            }
        }
    }

    @Override
    public void nettyNetSearchBack() {
        LL.V("nettyNetSearchBack");
    }

    @Override
    public void nettyNetGetDevListOver(String tx, JSONObject objParam) {
        LL.V("nettyNetGetDevListOver:" + objParam);

        if (objParam == null) {
            return;
        }

        LL.V("nettyNetGetDevListOver:" + objParam.toJSONString());
        NetDevCompFileTask netDevCompFileTask = JSONObject.toJavaObject(objParam, NetDevCompFileTask.class);
        sendMsgToActivity(netDevCompFileTask, WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.nettyNetGetDevListOver.name(), ip);
    }

    @Override
    public void nettyNetFileDownOver(String tx, JSONObject objParam) {
        LL.V("nettyNetFileDownOver:" + objParam.toJSONString());
        sendMsgToActivity(null, WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.nettyNetFileDownOver.name(), ip);
    }

    @Override
    public void atTask(String tx, JSONObject objParam) {
        LL.V("收到at消息：" + tx);
        this.sendMsgToActivity(null, WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.netBack.name(), tx);
    }

    //手机←→网关 业务
    private void toAtNet(String data_type, String at) {
        AtTask atTask = new AtTask();
        atTask.setAt(at);
        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1000_0200.main, Sen_1000_0200.sub, atTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, data_type, "发送at命令成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 搜索网关(目前遍历)
    private void toSearchNet() {

//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x12, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x00, 0x00});
//        wjProtocol.setFormat("TX");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        int len = WjProtocol.MIN_DATA_LEN + 0;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1200_0000.main, Sen_1200_0000.sub, null);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, TOAST, "发送搜索网关命令成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 手机选择设备厂商后传递给网关信息
    private void deviceComp(String data_type, NetDevCompFileTask netDevCompTask) {

//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x12, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x01, 0x00});
//        wjProtocol.setFormat("JS");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        String jsonStr = JSONObject.toJSONString(netDevCompTask);
//        Log.v("ly", jsonStr);
//        byte[] objectBytes = jsonStr.getBytes();
//
//        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1200_0100.main, Sen_1200_0100.sub, netDevCompTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, data_type, "选择设备厂商后传递给网关信息成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 手机注册后传递给网关信息
    private void toNetInfo(String data_type, NetInfoTask netInfoTask) {
//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x02, 0x00});
//        wjProtocol.setFormat("JS");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        String jsonStr = JSONObject.toJSONString(netInfoTask);
//        Log.v("ly", jsonStr);
//        byte[] objectBytes = jsonStr.getBytes();
//
//        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_0000_0200.main, Sen_0000_0200.sub, netInfoTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, data_type, "注册后传递给网关信息成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 认证完成，网关获取设备列表
    private void authOver(String data_type, NetDevCompFileTask netDevCompFileTask) {
//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x12, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x02, 0x00});
//        wjProtocol.setFormat("JS");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        String jsonStr = JSONObject.toJSONString(netDevCompFileTask);
//        Log.v("ly", jsonStr);
//        byte[] objectBytes = jsonStr.getBytes();
//
//        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1200_0200.main, Sen_1200_0200.sub, netDevCompFileTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, data_type, "认证完成，网关获取设备列表成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 手机配置房间后传递给网关信息
    private void saveDevice(String data_type, NetDevCompTask netDevCompTask) {
//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x12, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x03, 0x00});
//        wjProtocol.setFormat("JS");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        String jsonStr = JSONObject.toJSONString(netDevCompTask);
//        Log.v("ly", jsonStr);
//        byte[] objectBytes = jsonStr.getBytes();
//
//        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1200_0300.main, Sen_1200_0300.sub, netDevCompTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, data_type, "手机配置房间后传递给网关信息成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 要求网关执行一段任务：
    private void toNetDoTask(NetDoTaskTask netDoTaskTask) {
//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x03, 0x00});
//        wjProtocol.setFormat("JS");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        String jsonStr = JSONObject.toJSONString(netDoTaskTask);
//        Log.v("ly", jsonStr);
//        byte[] objectBytes = jsonStr.getBytes();
//
//        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_0000_0300.main, Sen_0000_0300.sub, netDoTaskTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, TOAST, "要求网关执行一段任务成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //手机→网关 手机配置网关
    private void toConfigNet(String data_type, NetConfigTask netDevCompTask) {
        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1200_0400.main, Sen_1200_0400.sub, netDevCompTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, data_type, "手机配置网关任务成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

}

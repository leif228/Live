package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.NetDevCompTask;
import com.littlegreens.netty.client.extra.NetInfoTask;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.wj.work.utils.ScanDeviceUtile;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NetService extends Service implements NettyClientListener {
    private boolean connecting = false;
    public static final String COUNTER = "data";
    public static final Integer port = 8666;
    public static final String ACTION_NAME = "com.wj.work.netservice.COUNTER_ACTION";

    private String data;
    List<String> iplist;
    List<NettyManager> nettyManagers = new ArrayList<>();

    NettyManager nettyManager;
    Queue<WjProtocol> queue;

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
        getIpConnectNet();
    }

    private void connectNet() {
        LL.V("connectNet");
        for (int i = 0; i < iplist.size(); i++) {
            LL.V("connectNet=" + i + ":ip=" + iplist.get(i));
            NettyManager nettyManager = new NettyManager(i, iplist.get(i), port);
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从Activity获取data
        data = intent.getStringExtra(COUNTER);

        LL.V("NetService onStartCommand: " + data);

        if ("1".equals(data)) {
            getIpConnectNet();
        } else if ("2".equals(data)) {
            NetInfoTask netInfoTask = (NetInfoTask) intent.getSerializableExtra("task");
            toNetInfo(netInfoTask);
        } else if ("3".equals(data)) {
            NetDevCompTask netDevCompTask = (NetDevCompTask) intent.getSerializableExtra("task");
            toDeviceComp(netDevCompTask);
        }

        return START_STICKY;
    }

    private void sendMsgToActivity(String data) {
        //向Activity传递data
        final Intent mIntent = new Intent();
        mIntent.setAction(ACTION_NAME);
        mIntent.putExtra(COUNTER, data);
        sendBroadcast(mIntent);
    }

    private void toSearchNet() {

        WjProtocol wjProtocol = new WjProtocol();

        wjProtocol.setPlat(new byte[]{0x50,0x00});
        wjProtocol.setMaincmd(new byte[]{0x12,0x00});
        wjProtocol.setSubcmd(new byte[]{0x00,0x00});
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(new byte[]{0x00,0x00});

//        String jsonStr = "pipo";
//        byte[] objectBytes = jsonStr.getBytes();

        int len = WjProtocol.MIN_DATA_LEN + 0;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
        } else {
            queue.offer(wjProtocol);
        }
    }

    private void toDeviceComp(NetDevCompTask netDevCompTask) {

        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50,0x00});
        wjProtocol.setMaincmd(new byte[]{0x12,0x00});
        wjProtocol.setSubcmd(new byte[]{0x01,0x00});
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(new byte[]{0x00,0x00});

        String jsonStr = JSONObject.toJSONString(netDevCompTask);
        Log.v("ly", jsonStr);
        byte[] objectBytes = jsonStr.getBytes();

        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));
        wjProtocol.setUserdata(objectBytes);

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
        } else {
            queue.offer(wjProtocol);
        }
    }

    private void toNetInfo(NetInfoTask netInfoTask) {

        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50,0x00});
        wjProtocol.setMaincmd(new byte[]{0x00,0x00});
        wjProtocol.setSubcmd(new byte[]{0x02,0x00});
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(new byte[]{0x00,0x00});

        String jsonStr = JSONObject.toJSONString(netInfoTask);
        Log.v("ly", jsonStr);
        byte[] objectBytes = jsonStr.getBytes();

        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));
        wjProtocol.setUserdata(objectBytes);

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
        } else {
            queue.offer(wjProtocol);
        }
    }

    private void getIpConnectNet() {
        if (!connecting) {
            connecting = true;

            //开启一个线程，对数据进行处理
            new Thread(new Runnable() {
                boolean iping = true;

                @Override
                public void run() {
                    try {
                        while (iping) {
                            iplist = ScanDeviceUtile.getInstance().scan();
                            if (iplist != null && iplist.size() > 0) {
                                iping = false;
                                LL.V(iplist.toString());
                            } else {
                                Thread.sleep(10000);
                            }
                        }
                        if (iplist != null && iplist.size() > 0){
                            while (connecting){
                                connectNet();
                                Thread.sleep(30000);
                            }
                        }
                    } catch (Exception e) {
                        LL.V("getIp:[error]" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connecting = false;
    }

    @Override
    public void onMessageResponseClient(String msg, int index) {

    }

    @Override
    public void onClientStatusConnectChanged(int statusCode, int index) {

    }

    @Override
    public void connectSuccess(String ip, int index) {
        LL.V(ip + ":index:" + index);
        connecting = false;
        nettyManager = nettyManagers.get(index);
        toSearchNet();
        this.doTask();
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
}

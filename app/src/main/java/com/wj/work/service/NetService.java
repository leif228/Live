package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.BaseTask;
import com.littlegreens.netty.client.extra.NetDevCompTask;
import com.littlegreens.netty.client.extra.NetInfoTask;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.wj.work.utils.ScanDeviceUtile;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetService extends Service implements NettyClientListener {
    private boolean connecting = false;
    private boolean iping = true;
    public static final String COUNTER = "data";
    public static final Integer port = 8777;
    public static final String ACTION_NAME = "com.wj.work.netservice.COUNTER_ACTION";

    private String data;
    List<String> iplist;
    List<NettyManager> nettyManagers = new ArrayList<>();

    NettyManager nettyManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LL.V("NetService onCreate");
//        getIp();
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
            getIp();
        } else if ("2".equals(data)) {
            NetInfoTask netInfoTask = (NetInfoTask) intent.getSerializableExtra("task");
            toNetInfo(netInfoTask);
        } else if ("3".equals(data)) {
            NetDevCompTask netDevCompTask = (NetDevCompTask) intent.getSerializableExtra("task");
            deviceComp(netDevCompTask);
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

    private void searchNet() {
        if (nettyManager != null) {
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

            nettyManager.senMessage(wjProtocol);
        }
    }

    private void deviceComp(NetDevCompTask netDevCompTask) {
        if (nettyManager != null) {
            WjProtocol wjProtocol = new WjProtocol();
            wjProtocol.setPlat(Short.parseShort("50"));
            wjProtocol.setMaincmd(Short.parseShort("12"));
            wjProtocol.setSubcmd(Short.parseShort("1"));
            wjProtocol.setFormat("JS");
            wjProtocol.setBack(Short.parseShort("0"));

            String jsonStr = JSONObject.toJSONString(netDevCompTask);
            Log.v("ly", jsonStr);
            byte[] objectBytes = jsonStr.getBytes();

            int len = 21 + objectBytes.length;
            wjProtocol.setLen((short) len);
            wjProtocol.setUserdata(objectBytes);

            nettyManager.senMessage(wjProtocol);
        }
    }

    private void toNetInfo(NetInfoTask netInfoTask) {
        if (nettyManager != null) {
            WjProtocol wjProtocol = new WjProtocol();
            wjProtocol.setPlat(Short.parseShort("50"));
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

            nettyManager.senMessage(wjProtocol);
        }
    }

    private void getIp() {

        //开启一个线程，对数据进行处理
        new Thread(new Runnable() {
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
                    if (iplist != null && iplist.size() > 0)
                        connectNet();
                } catch (Exception e) {
                    LL.V("getIp:[error]"+e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
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
        nettyManager = nettyManagers.get(index);
        searchNet();
    }

    @Override
    public void nettyNetSearchBack() {

    }
}

package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.BaseTask;
import com.littlegreens.netty.client.extra.NetInfoTask;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.wj.work.utils.ScanDeviceUtile;

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
        NetInfoTask netInfoTask = (NetInfoTask) intent.getSerializableExtra("task");

        LL.V("NetService onStartCommand: " + data);
        final Intent mIntent = new Intent();
        mIntent.setAction(ACTION_NAME);
        connecting = true;
        if ("1".equals(data))
            getIp();
        else if("2".equals(data))
            toNetInfo(netInfoTask);

        //开启一个线程，对数据进行处理
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

//                try {
////                    while (connecting) {
////                        //耗时操作：数据处理并保存，向Activity发送广播
////                        mIntent.putExtra(COUNTER, data);
////                        sendBroadcast(mIntent);
////                        data++;
////                        Thread.sleep(300);
////                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        return START_STICKY;
    }

    private void toNetInfo(NetInfoTask netInfoTask) {
        if(nettyManager != null)
            nettyManager.sendMsgToNetInfo(netInfoTask);
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
        nettyManager.sendMsgToSearchNet();
    }

    @Override
    public void nettyNetSearchBack() {

    }
}

package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.LoginTask;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.NettyClientListener;

import java.util.ArrayList;
import java.util.List;

public class ManageService extends Service implements NettyClientListener {
    private boolean connecting = false;
    public static final String COUNTER = "data";
    public static final String ACTION_NAME = "com.wj.work.manageservice.COUNTER_ACTION";

    private String data;

    NettyManager nettyManager;
    List<NettyManager> nettyManagers = new ArrayList<>();
    String fzwno = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从Activity获取data
        data = intent.getStringExtra(COUNTER);
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");
        fzwno = intent.getStringExtra("fzwno");
        LL.V("ManageService onStartCommand " + ip);
        if (ip == null && port == null)
            return START_STICKY;

//        final Intent mIntent = new Intent();
//        mIntent.setAction(ACTION_NAME);
//        connecting = true;
        //开启一个线程，对数据进行处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectManage(ip, port, fzwno);
//                    while (connecting) {
//                        //耗时操作：数据处理并保存，向Activity发送广播
//                        mIntent.putExtra(COUNTER, data);
//                        sendBroadcast(mIntent);
//                        data++;
//                        Thread.sleep(300);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }

    private void connectManage(String ip, String port, String fzwno) {
        LL.V("connectManage");
        LL.V("connectManage=" + ":ip=" + ip);
        NettyManager nettyManager = new NettyManager(1, ip, Integer.valueOf(port));
        nettyManager.setNettyClientListener(this);
        nettyManager.connect();

        nettyManagers.add(nettyManager);
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

        LoginTask loginTask = new LoginTask();
        loginTask.setOid(fzwno);
        nettyManager = nettyManagers.get(index);
        manageLoin(loginTask);
    }

    private void manageLoin(LoginTask loginTask) {
        if (nettyManager != null) {
            WjProtocol wjProtocol = new WjProtocol();
            wjProtocol.setPlat(Short.parseShort("50"));
            wjProtocol.setMaincmd(Short.parseShort("0"));
            wjProtocol.setSubcmd(Short.parseShort("1"));
            wjProtocol.setFormat("JS");
            wjProtocol.setBack(Short.parseShort("0"));

            String jsonStr = JSONObject.toJSONString(loginTask);
            Log.v("ly", jsonStr);
            byte[] objectBytes = jsonStr.getBytes();

            int len = 21 + objectBytes.length;
            wjProtocol.setLen((short) len);
            wjProtocol.setUserdata(objectBytes);

            nettyManager.senMessage(wjProtocol);
        }
    }

    @Override
    public void nettyNetSearchBack() {

    }
}

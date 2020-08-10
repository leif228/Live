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
import com.littlegreens.netty.client.status.ConnectState;

import java.util.ArrayList;
import java.util.List;

public class ManageService extends Service implements NettyClientListener {
    public static final String COUNTER = "data";
    public static final String ACTION_NAME = "com.wj.work.manageservice.COUNTER_ACTION";

    private boolean havaConnectSuccessed = false;
    int connetSuccessIndex = -1;//连接成功的下标
    private String data;

    NettyManager nettyManager;
    List<NettyManager> nettyManagers = new ArrayList<>();
    String fzwno = "";
    String ip;
    String port;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LL.V("ManageService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从Activity获取data
        data = intent.getStringExtra(COUNTER);

        LL.V("ManageService onStartCommand :" + data);

        if ("1".equals(data)) {
            ip = intent.getStringExtra("ip");
            port = intent.getStringExtra("port");
            fzwno = intent.getStringExtra("fzwno");
            if (ip == null && port == null)
                return START_STICKY;
            //没有成功连接过的则重新连接，有成功连接过的，断线内部会自动重连
            connectManage();
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

    private void connectManage() {
        if (nettyManager != null)
            nettyManager.release();
        nettyManagers.clear();
        havaConnectSuccessed = false;
        connetSuccessIndex = -1;

        LL.V("connectManage");
        LL.V("connectManage=" + ":ip=" + ip);
        NettyManager nettyManager = new NettyManager(0, ip, Integer.valueOf(port));
        nettyManager.setNettyClientListener(this);
        nettyManager.connect();

        nettyManagers.add(nettyManager);
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
            if (havaConnectSuccessed) {
                NettyManager nettyManagerFire = nettyManagers.get(index);
                nettyManagerFire.release();
            }
            if(index == connetSuccessIndex)
                connectManage();
        }
    }

    @Override
    public void connectSuccess(String ip, int index) {
        LL.V(ip + ":index:" + index);
        havaConnectSuccessed = true;
        connetSuccessIndex = index;

        LoginTask loginTask = new LoginTask();
        loginTask.setOid(fzwno);
        nettyManager = nettyManagers.get(index);
        manageLoin(loginTask);
    }

    private void manageLoin(LoginTask loginTask) {
        if (nettyManager != null) {
            WjProtocol wjProtocol = new WjProtocol();
            wjProtocol.setPlat(new byte[]{0x50, 0x00});
            wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
            wjProtocol.setSubcmd(new byte[]{0x01, 0x00});
            wjProtocol.setFormat("JS");
            wjProtocol.setBack(new byte[]{0x00, 0x00});

            String jsonStr = JSONObject.toJSONString(loginTask);
            Log.v("ly", jsonStr);
            byte[] objectBytes = jsonStr.getBytes();

            int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
            wjProtocol.setLen(wjProtocol.short2byte((short) len));
            wjProtocol.setUserdata(objectBytes);

            nettyManager.senMessage(wjProtocol);
        }
    }

    @Override
    public void nettyNetSearchBack() {

    }
}

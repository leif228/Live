package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.task.ConnectTk;
import com.littlegreens.netty.client.extra.task.LoginTask;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.littlegreens.netty.client.status.ConnectState;
import com.wj.work.db.SpManager;
import com.wj.work.widget.entity.LoginEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ManageService extends Service implements NettyClientListener {
    public static final String COUNTER = "data";
    public static final String ACTION_NAME = "com.wj.work.manageservice.COUNTER_ACTION";
    public static final String COUNTER_TYPE = "type";

    //    private boolean havaConnectSuccessed = false;
//    int connetSuccessIndex = -1;//连接成功的下标
    private String data_type;

    NettyManager nettyManager;
    List<NettyManager> nettyManagers = new ArrayList<>();
    String fzwno = "";
//    String ip;
//    String port;

    Queue<WjProtocol> queue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LL.V("ManageService onCreate");
        queue = new LinkedList<WjProtocol>();

        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (!"".equals(loginEntity.getIp())) {
            ConnectTk connectTk = new ConnectTk();
            connectTk.setIp(loginEntity.getIp());
            connectTk.setPort(loginEntity.getPort());
            connectTk.setFzwno(loginEntity.getFzwno());

            fzwno = connectTk.getFzwno();
            //重连
            reConnectManage(connectTk);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从Activity获取data
        data_type = intent.getStringExtra(COUNTER_TYPE);

        LL.V("ManageService onStartCommand :" + data_type);

//        if ("1".equals(data)) {
//            ConnectTask connectTask = (ConnectTask) intent.getSerializableExtra("task");
//            fzwno = connectTask.getFzwno();
//            //重连
//            reConnectManage(connectTask);
//        } else
        if ("2".equals(data_type)) {
            ConnectTk connectTk = (ConnectTk) intent.getSerializableExtra(COUNTER);
            fzwno = connectTk.getFzwno();
            //新建连接
            newConnectManage(connectTk);
        }

        return START_STICKY;
    }

    //新建连接
    private void newConnectManage(ConnectTk connectTk) {
        if (nettyManager != null) {
            nettyManager.release();
            nettyManager = null;
            nettyManagers.clear();

            LL.V("断开原连接再新建连接newConnectManage=" + ":ip=" + connectTk.getIp());
            NettyManager nettyManager = new NettyManager(0, connectTk.getIp(), Integer.valueOf(connectTk.getPort()));
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        } else {
            nettyManagers.clear();
            nettyManager = null;

            LL.V("直接新建连接newConnectManage=" + ":ip=" + connectTk.getIp());
            NettyManager nettyManager = new NettyManager(0, connectTk.getIp(), Integer.valueOf(connectTk.getPort()));
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        }
    }

    //重连
    private void reConnectManage(ConnectTk connectTk) {
        if (nettyManager != null) {
//            nettyManager.attemptReConnect();
        } else {
            nettyManagers.clear();
            nettyManager = null;

            LL.V("reConnectManage=" + ":ip=" + connectTk.getIp());
            NettyManager nettyManager = new NettyManager(0, connectTk.getIp(), Integer.valueOf(connectTk.getPort()));
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        }
    }

    private void sendMsgToActivity(String data,String type) {
        //向Activity传递data
        final Intent mIntent = new Intent();
        mIntent.setAction(ACTION_NAME);
        mIntent.putExtra(COUNTER, data);
        mIntent.putExtra(COUNTER_TYPE, type);
        sendBroadcast(mIntent);
    }

//    private void connectManage() {
//        if (nettyManager != null)
//            nettyManager.release();
//        nettyManagers.clear();
//        havaConnectSuccessed = false;
//        connetSuccessIndex = -1;
//
//        LL.V("connectManage");
//        LL.V("connectManage=" + ":ip=" + ip);
//        NettyManager nettyManager = new NettyManager(0, ip, Integer.valueOf(port));
//        nettyManager.setNettyClientListener(this);
//        nettyManager.connect();
//
//        nettyManagers.add(nettyManager);
//    }

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
//            Log.v("ly", "onClientStatusConnectChanged:===" + index);

//            NettyManager nettyManagerFire = nettyManagers.get(index);
//            nettyManagerFire.release();
//            nettyManagerFire = null;
        }
    }

    @Override
    public void connectSuccess(String ip, int index) {
        LL.V(ip + ":index:" + index);

        LoginTask loginTask = new LoginTask();
        loginTask.setOID(fzwno);
        nettyManager = nettyManagers.get(index);
        manageLoin(loginTask);
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

    }

    @Override
    public void nettyNetGetDevListOver(String tx, JSONObject objParam) {

    }

    //终端→服务 登录
    private void manageLoin(LoginTask loginTask) {
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

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
        } else {
            queue.offer(wjProtocol);
        }
    }

}

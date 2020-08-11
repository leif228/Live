package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.NetDevCompTask;
import com.littlegreens.netty.client.extra.NetInfoTask;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.littlegreens.netty.client.status.ConnectState;
import com.wj.work.utils.ScanDeviceUtile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class NetService extends Service implements NettyClientListener {
    private boolean connecting = false;
    private boolean havaConnectSuccessed = false;
    public static final String COUNTER = "data";
    public static final long closeTimes = 30000l;
    public static final Integer port = 8555;
    public static final String ACTION_NAME = "com.wj.work.netservice.COUNTER_ACTION";

    private String data;
    List<String> iplist;
    List<NettyManager> nettyManagers = new ArrayList<>();
    int connetSuccessIndex = -1;//连接成功的下标

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
        //清空列表
        nettyManagers.clear();
        connetSuccessIndex = -1;
        havaConnectSuccessed = false;
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

        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(new byte[]{0x12, 0x00});
        wjProtocol.setSubcmd(new byte[]{0x00, 0x00});
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

        int len = WjProtocol.MIN_DATA_LEN + 0;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
        } else {
            queue.offer(wjProtocol);
        }
    }

    private void toDeviceComp(NetDevCompTask netDevCompTask) {

        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(new byte[]{0x12, 0x00});
        wjProtocol.setSubcmd(new byte[]{0x01, 0x00});
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

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
        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
        wjProtocol.setSubcmd(new byte[]{0x02, 0x00});
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

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
                        if (iplist != null && iplist.size() > 0) {
                            connectNet();
                            noConnectIpSuccessCloseTask();
                        }
                    } catch (Exception e) {
                        LL.V("getIp:[error]" + e.getMessage());
                        connecting = false;
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //定时任务（当没有扫描出目标ip，只扫描到其它ip时，关闭已经开启的连接任务）
    private void noConnectIpSuccessCloseTask() {
        LL.V("定时任务启动。。。");
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {

                if (connecting) {
                    int size = nettyManagers.size();
                    LL.V("定时任务执行：nettySize=" + size);
                    for (int i = 0; i < size; i++) {
                        NettyManager nettyManagerFire = nettyManagers.get(i);
                        nettyManagerFire.release();
                    }
                    nettyManagers.clear();
                    nettyManager = null;
                    connecting = false;
                }
                LL.V("定时任务执行结束。。。");
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,closeTimes);
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
        if (statusCode == ConnectState.STATUS_CONNECT_ERROR) {
            Log.v("ly", "onClientStatusConnectChanged:===" + index);
            if (havaConnectSuccessed) {
                NettyManager nettyManagerFire = nettyManagers.get(index);
                nettyManagerFire.release();
            }
            //全部都失败则重新扫描ip
            if (connetSuccessIndex == index) {
                connecting = false;
                getIpConnectNet();
            }
        }
    }

    @Override
    public void connectSuccess(String ip, int index) {
        LL.V(ip + ":index:" + index);
        connecting = false;
        connetSuccessIndex = index;
        havaConnectSuccessed = true;
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

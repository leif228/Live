package com.wj.work.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.extra.AtProtocol;
import com.littlegreens.netty.client.extra.send.Sen_0000_0100;
import com.littlegreens.netty.client.extra.send.Sen_1000_0000;
import com.littlegreens.netty.client.extra.send.Sen_factory;
import com.littlegreens.netty.client.extra.task.AtTask;
import com.littlegreens.netty.client.extra.task.BaseTask;
import com.littlegreens.netty.client.extra.task.Connect;
import com.littlegreens.netty.client.extra.task.LoginTask;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.task.ManageChatMsgAtParam;
import com.littlegreens.netty.client.extra.task.ManageChatMsgTask;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
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
    public static final String COUNTER_ELSE = "else";
    public static final String TOAST = "toast";


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
            Connect connect = new Connect();
            connect.setIp(loginEntity.getIp());
            connect.setPort(loginEntity.getPort());
            connect.setFzwno(loginEntity.getFzwno());

            fzwno = connect.getFzwno();
            //重连
            reConnectManage(connect);
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
            Connect connect = (Connect) intent.getSerializableExtra(COUNTER);
            fzwno = connect.getFzwno();
            //新建连接
            newConnectManage(connect);
        } else if ("3".equals(data_type)) {

            ManageChatMsgTask manageChatMsgTask = (ManageChatMsgTask) intent.getSerializableExtra(COUNTER);
            sendChatMsg(manageChatMsgTask);
        } else if ("4".equals(data_type)) {

            ManageChatMsgTask manageChatMsgTask = (ManageChatMsgTask) intent.getSerializableExtra(COUNTER);
            toGenEvent(manageChatMsgTask);
        }

        return START_STICKY;
    }

    private void toGenEvent(ManageChatMsgTask manageChatMsgTask) {
        LL.V("事件产生json：" + manageChatMsgTask.getOid());

        String at = this.genAt("N", manageChatMsgTask.getOid(), "0500", "A001", "0001", "0001", manageChatMsgTask.getOid());
        AtTask atTask = new AtTask();
        atTask.setAt(at);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1000_0000.main, Sen_1000_0000.sub, atTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, TOAST, "发送事件产生消息成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //新建连接
    private void newConnectManage(Connect connect) {
        if (nettyManager != null) {
            nettyManager.release();
            nettyManager = null;
            if (!nettyManagers.isEmpty()) {
                int size = nettyManagers.size();
                for (int i = 0; i < size; i++) {
                    NettyManager nettyManagerFire = nettyManagers.get(i);
                    nettyManagerFire.release();
                }
            }
            nettyManagers.clear();

            LL.V("断开原连接再新建连接newConnectManage=" + ":ip=" + connect.getIp());
            NettyManager nettyManager = new NettyManager(0, connect.getIp(), Integer.valueOf(connect.getPort()));
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        } else {
            if (!nettyManagers.isEmpty()) {
                int size = nettyManagers.size();
                for (int i = 0; i < size; i++) {
                    NettyManager nettyManagerFire = nettyManagers.get(i);
                    nettyManagerFire.release();
                }
            }
            nettyManagers.clear();
            nettyManager = null;

            LL.V("直接新建连接newConnectManage=" + ":ip=" + connect.getIp());
            NettyManager nettyManager = new NettyManager(0, connect.getIp(), Integer.valueOf(connect.getPort()));
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        }
    }

    //重连
    private void reConnectManage(Connect connect) {
        if (nettyManager != null) {
//            nettyManager.attemptReConnect();
        } else {
            nettyManagers.clear();
            nettyManager = null;

            LL.V("reConnectManage=" + ":ip=" + connect.getIp());
            NettyManager nettyManager = new NettyManager(0, connect.getIp(), Integer.valueOf(connect.getPort()));
            nettyManager.setNettyClientListener(this);
            nettyManager.connect();

            nettyManagers.add(nettyManager);
        }
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
    public void connectSuccess(String ip, int index) {
        LL.V(ip + ":index:" + index);

        LoginTask loginTask = new LoginTask();
        loginTask.setOID(fzwno);
        nettyManager = nettyManagers.get(index);
        this.sendMsgToActivity(null, TOAST, "连接到管理服务器成功");
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

    @Override
    public void nettyNetFileDownOver(String tx, JSONObject objParam) {

    }

    @Override
    public void atTask(String tx, JSONObject objParam) {
        try {
            LL.V("收到at消息：" + tx);
            AtProtocol atProtocol = AtProtocol.doAtTask(tx);
            if("E001".equals(atProtocol.getBusinessNum()) && "0001".equals(atProtocol.getCommand())){
                LL.V("收到atjson消息Para=" + atProtocol.getPara());
                JSONObject objParamAt = JSONObject.parseObject(atProtocol.getPara());
//                LL.V("收到atjson消息eventNo=" + objParamAt.getString("event_no"));
                ManageChatMsgAtParam manageChatMsgAtParam = (ManageChatMsgAtParam) JSONObject.toJavaObject(objParamAt, ManageChatMsgAtParam.class);
//                LL.V("收到at消息eventNo=" + manageChatMsgAtParam.getEventNo());

                this.sendMsgToActivity(manageChatMsgAtParam,"chatMsg","");
            }else {

                LL.E("收到at业务暂时还不能处理！buss="+atProtocol.getBusinessNum()+",cmd="+atProtocol.getCommand());
                this.sendMsgToActivity(null,TOAST,"收到at业务暂时还不能处理！buss="+atProtocol.getBusinessNum()+",cmd="+atProtocol.getCommand());
            }
        }catch (Exception e){
            LL.E("处理at业务出错了！"+e.getMessage());
        }
    }

    //终端→服务 登录
    private void manageLoin(LoginTask loginTask) {
//        WjProtocol wjProtocol = new WjProtocol();
//        wjProtocol.setPlat(new byte[]{0x50, 0x00});
//        wjProtocol.setMaincmd(new byte[]{0x00, 0x00});
//        wjProtocol.setSubcmd(new byte[]{0x01, 0x00});
//        wjProtocol.setFormat("JS");
//        wjProtocol.setBack(new byte[]{0x00, 0x00});
//
//        String jsonStr = JSONObject.toJSONString(loginTask);
//        Log.v("ly", jsonStr);
//        byte[] objectBytes = jsonStr.getBytes();
//
//        int len = WjProtocol.MIN_DATA_LEN + objectBytes.length;
//        wjProtocol.setLen(wjProtocol.short2byte((short) len));
//        wjProtocol.setUserdata(objectBytes);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_0000_0100.main, Sen_0000_0100.sub, loginTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, TOAST, "发送登录信息到管理服务器成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    //发送聊天消息
    private void sendChatMsg(ManageChatMsgTask manageChatMsgTask) {
        ManageChatMsgAtParam manageChatMsgAtParam = new ManageChatMsgAtParam();
        manageChatMsgAtParam.setEventNo(manageChatMsgTask.getEventNo());
        manageChatMsgAtParam.setMsg(manageChatMsgTask.getMsg());
        manageChatMsgAtParam.setMsgType(manageChatMsgTask.getMsgType());

        String json = JSONObject.toJSONString(manageChatMsgAtParam);
        LL.V("文本消息参数json：" + json);

        String at = this.genAt("N", manageChatMsgTask.getOid(), "0500", "E001", "0001", "0001", json);
        AtTask atTask = new AtTask();
        atTask.setAt(at);

        WjProtocol wjProtocol = Sen_factory.getInstance(Sen_1000_0000.main, Sen_1000_0000.sub, atTask);
        if (wjProtocol == null)
            return;

        if (nettyManager != null) {
            nettyManager.senMessage(wjProtocol);
            this.sendMsgToActivity(null, TOAST, "发送聊天消息成功");
        } else {
            queue.offer(wjProtocol);
        }
    }

    private String genAt(String flag, String oid, String pri, String buss, String port, String cmd, String param) {
        //AT@Nchn0L0a002020091100011000000000E00200010500A001000100010027chn0L0a00202009110001100000000011110102#*

        String paramLen = AtProtocol.IntToHexStringLimit4(param.length());
        String at = AtProtocol.HEARD + flag + oid + pri + buss + port + cmd + paramLen + param + AtProtocol.END;//取出帧头

        LL.V("编码后的at为：" + at);

        return at;
    }


}

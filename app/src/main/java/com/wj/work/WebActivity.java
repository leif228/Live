package com.wj.work;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.lib.kit.utils.LL;
import com.lib.kit.utils.StatusBarUtils;
import com.littlegreens.netty.client.extra.task.BaseTask;
import com.littlegreens.netty.client.extra.task.Connect;
import com.littlegreens.netty.client.extra.task.ManageChatMsgAtParam;
import com.littlegreens.netty.client.extra.task.ManageChatMsgTask;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.task.NetDevCompTask;
import com.littlegreens.netty.client.extra.task.NetInfoTask;
import com.littlegreens.netty.client.extra.task.NetSearchNetDtos;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.db.SpManager;
import com.wj.work.event.NetworkEvent;
import com.wj.work.service.ManageService;
import com.wj.work.service.NetService;
import com.wj.work.ui.contract.WebViewI;
import com.wj.work.ui.presenter.WebPresenter;
import com.wj.work.utils.WebViewJavaScriptFunction;
import com.wj.work.widget.entity.LoginEntity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class WebActivity extends BaseMvpActivity<WebPresenter> implements WebViewI, WebViewJavaScriptFunction {

    @BindView(R.id.wv_webview)
    WebView webView;

    //    private static final String mHomeUrl = "http://192.168.4.17:8848/wujieweb/page/login/login.html";
    private static final String mHomeUrl = "http://192.168.4.16:8080/wujieweb/page/login/login.html";
    private static final String mChatUrl = "http://192.168.4.16:8080/wujieweb/page/login/chat.html";
    //    private static final String mHomeUrl = "http://www.baidu.com";
    private static final String authUrl = "/shared/tcube_app/APP_code/APP_choose_device.php";

    private String mDataType = "";
    private String nDataType = "";
    private Intent nIntent;
    private Intent mIntent;
    private NetReceiver netReceiver;
    private ManageReceiver manageReceiver;
    private boolean bind = false;

    private String compName = "";
    private ManageChatMsgAtParam chatMsgData;
    private boolean fromTcp = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_web;
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected int getNavigationBarColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected void initView() {
        mPresenter = new WebPresenter(this);
        init();

        nIntent = new Intent(this, NetService.class);
        nIntent.setAction("android.intent.action.RESPOND_VIA_MESSAGE");
        netReceiver = new NetReceiver();
        IntentFilter nintentFilter = new IntentFilter();
        nintentFilter.addAction(NetService.ACTION_NAME);
        //注册广播
        registerReceiver(netReceiver, nintentFilter);

        mIntent = new Intent(this, ManageService.class);
        mIntent.setAction("android.intent.action.RESPOND_VIA_MESSAGE2");
        manageReceiver = new ManageReceiver();
        IntentFilter mintentFilter = new IntentFilter();
        mintentFilter.addAction(ManageService.ACTION_NAME);
        //注册广播
        registerReceiver(manageReceiver, mintentFilter);

        startService(nIntent);
        startService(mIntent);
    }

    private void sendMsgToNetService(String type, BaseTask baseTask) {
        //向Service传递data
        nIntent.putExtra(NetService.COUNTER_TYPE, type);
        nIntent.putExtra(NetService.COUNTER, baseTask);
        startService(nIntent);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startService(nIntent);
//                LL.V("延迟执行");
//            }
//        },1000*65);
    }

    private void sendMsgToManageService(String type, BaseTask baseTask) {
        //向Service传递data
        mIntent.putExtra(ManageService.COUNTER_TYPE, type);
        mIntent.putExtra(ManageService.COUNTER, baseTask);
        startService(mIntent);
    }

    @Override
    public void loginSuccess(LoginEntity loginEntity) {
        LL.V("回调loginSuccess");
//        SpManager.getInstance().getLoginSp().putLoginInfoEntity(loginEntity);
//        skipActivity(MainActivity.class);
//        finish();
    }


    private void init() {
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
//        webSetting.setAppCacheEnabled(false);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //android 中 webview 怎么用 localStorage?
        webSetting.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSetting.setAppCachePath(appCachePath);
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(this, "test");//AndroidtoJS类对象映射到js的test对象

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;

            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LL.V("onPageFinished.url="+url);
                if(url.equals(mChatUrl)&&fromTcp){
                    if(chatMsgData != null){
                        LL.V(chatMsgData.getEventNo());
                        webView.loadUrl("javascript:flushChat('"+chatMsgData.getEventNo()+"')");
                        chatMsgData = null;
                    }
                    fromTcp = false;
                }
            }

        });
        webView.loadUrl(mHomeUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    @JavascriptInterface
    public void toTcp(String ip, String port, String fzwno) {
        LL.V(ip + ":" + port + "/" + fzwno);
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setIp(ip);
        loginEntity.setPort(port);
        loginEntity.setFzwno(fzwno);
        SpManager.getInstance().getLoginSp().putLoginInfoEntity(loginEntity);

        Connect connect = new Connect();
        connect.setIp(ip);
        connect.setPort(port);
        connect.setFzwno(fzwno);

        sendMsgToManageService("2", connect);

    }

    @Override
    @JavascriptInterface
    public void toNetInfo(String fzwno, String userName, String preip, String preport, String loginfzwno, String owerip, String owerport, String owerfzwno, String passWord) {

        LL.V("toNetInfo:" + fzwno);
//OID,Name,Identity,PhoneNum,Address,OwnerAddress,ServerIP,ServerPort,ServerOID,OwnerServerIP,OwnerServerPort,OwnerServerOID,PassNum,
        NetInfoTask netInfoTask = new NetInfoTask();
        netInfoTask.setOID(fzwno);
        netInfoTask.setName(userName);
        netInfoTask.setIdentity("");
        netInfoTask.setPhoneNum("");
        netInfoTask.setAddress("");
        netInfoTask.setOwnerAddress("");
        netInfoTask.setServerIP(preip);
        netInfoTask.setServerPort(preport);
        netInfoTask.setServerOID(loginfzwno);
        netInfoTask.setOwnerServerIP(owerip);
        netInfoTask.setOwnerServerPort(owerport);
        netInfoTask.setOwnerServerOID(owerfzwno);
        netInfoTask.setPassNum(passWord);

        sendMsgToNetService("2", netInfoTask);
    }

    @Override
    @JavascriptInterface
    public void deviceComp(String name, String path) {
        this.compName = name;
        NetDevCompFileTask netDevCompTask = new NetDevCompFileTask();
        netDevCompTask.setCompanyName(compName);
        netDevCompTask.setPRO("CompanyChoosed");
        netDevCompTask.setFile(path);
        sendMsgToNetService("3", netDevCompTask);
    }

    @Override
    @JavascriptInterface
    public void saveDevice() {
        LL.V("saveDevice:===");
        NetDevCompTask netDevCompTask = new NetDevCompTask();
        netDevCompTask.setCompanyName(compName);
        netDevCompTask.setPRO("SetOk");

        sendMsgToNetService("4", netDevCompTask);
    }

    @Override
    @JavascriptInterface
    public void authOver(String path) {
        LL.V("authOver:path=" + path);
        NetDevCompFileTask netDevCompFileTask = new NetDevCompFileTask();
        netDevCompFileTask.setFile(path);
        netDevCompFileTask.setCompanyName(compName);
        netDevCompFileTask.setPRO("Authority");

        sendMsgToNetService("5", netDevCompFileTask);
    }

    @Override
    @JavascriptInterface
    public String getMobileFzwno() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        return loginEntity.getFzwno();
    }

//    @Override
//    @JavascriptInterface
//    public String getEventNo() {
//        if (chatMsgData != null)
//            return chatMsgData.getEventNo();
//        else
//            return "";
//    }

    @Override
    @JavascriptInterface
    public String toSure() {
        return null;
    }

    @Override
    @JavascriptInterface
    public String sendChatMsg(String eventNo, String msg) {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (loginEntity.getFzwno().equals("")) {
            return "失败，没有注册手机fzwno！";
        }
        ManageChatMsgTask manageChatMsgTask = new ManageChatMsgTask();
        manageChatMsgTask.setOid(loginEntity.getFzwno());
        manageChatMsgTask.setEventNo(eventNo);
        manageChatMsgTask.setMsg(msg);
        manageChatMsgTask.setMsgType("txt");//消息类型:txt;img;voice;video
        sendMsgToManageService("3", manageChatMsgTask);

        return "0";
    }

    @Override
    @JavascriptInterface
    public void toGenEvent() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (loginEntity.getFzwno().equals("")) {
            Toast.makeText(WebActivity.this, "没有注册手机fzwno！", Toast.LENGTH_LONG).show();
            return;
        }
        ManageChatMsgTask manageChatMsgTask = new ManageChatMsgTask();
        manageChatMsgTask.setOid(loginEntity.getFzwno());
        manageChatMsgTask.setEventNo("");
        manageChatMsgTask.setMsg("手机事件产生了 ");
        manageChatMsgTask.setMsgType("txt");//消息类型:txt;img;voice;video
        sendMsgToManageService("4", manageChatMsgTask);
    }

    @Override
    @JavascriptInterface
    public void toLightOn() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (loginEntity.getFzwno().equals("")) {
            Toast.makeText(WebActivity.this, "没有注册手机fzwno！", Toast.LENGTH_LONG).show();
            return;
        }
        sendMsgToManageService("5", null);
    }

    @Override
    @JavascriptInterface
    public void toLightOff() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (loginEntity.getFzwno().equals("")) {
            Toast.makeText(WebActivity.this, "没有注册手机fzwno！", Toast.LENGTH_LONG).show();
            return;
        }
        sendMsgToManageService("6", null);
    }

    @Override
    @JavascriptInterface
    public void toSearchNet() {
        sendMsgToNetService("6", null);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(NetworkEvent event) {
        if (event.isConnected()) {
            LL.V("-----------    网络可用");
            if (event.isWiFiAvailable()) {
                LL.V("+++++++++++   wifi网络可用");
                sendMsgToNetService("1", null);
            }
//            LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
//            if (!"".equals(loginEntity.getIp())) {
//                LL.V("+++++++++++   wifi网络可用,连接manageservice:" + loginEntity.getIp());
//                ConnectTask connectTask =new ConnectTask();
//                connectTask.setIp(loginEntity.getIp());
//                connectTask.setPort(loginEntity.getPort());
//                connectTask.setFzwno(loginEntity.getFzwno());
//
//                sendMsgToManageService("1",connectTask);
//            }
        } else {
            LL.V("-----------    网络不可用");
        }
    }

    class NetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            runOnUiThread(new Runnable() {
                @SuppressLint("ShowToast")
                @Override
                public void run() {
                    //获取从Service中传来的data
                    nDataType = intent.getStringExtra(NetService.COUNTER_TYPE);
                    if ("1".equals(nDataType)) {
                        NetDevCompFileTask data = (NetDevCompFileTask) intent.getSerializableExtra(NetService.COUNTER);
                        String ip = intent.getStringExtra(NetService.COUNTER_ELSE);
                        LL.V("ip:" + ip);
                        //更新UI
                        String url = "http://" + ip + ":8080/tcube_app/APP_code/APP_choose_device.php";
                        LL.V("url:" + url);
                        webView.loadUrl(url);
                    } else if ("2".equals(nDataType)) {
                        String ip = intent.getStringExtra(NetService.COUNTER_ELSE);
                        LL.V("ip:" + ip);

                        //更新UI
                        String url = "http://" + ip + ":8080/tcube_app/APP_code/APP_choose_device.php";
                        LL.V("url:" + url);
                        webView.loadUrl(url);
                    } else if (NetService.TOAST.equals(nDataType)) {
                        String toast = intent.getStringExtra(NetService.COUNTER_ELSE);
                        LL.V("toast:" + toast);

                        Toast.makeText(WebActivity.this, toast, Toast.LENGTH_LONG).show();
                    } else if ("3".equals(nDataType)) {
                        NetSearchNetDtos data = (NetSearchNetDtos) intent.getSerializableExtra(NetService.COUNTER);
                        LL.V("toast:" + data.getNetSearchNetDtos().size());

//                        Toast.makeText(WebActivity.this, toast, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    class ManageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //获取从Service中传来的data
                    mDataType = intent.getStringExtra(ManageService.COUNTER_TYPE);
                    //更新UI
                    if (ManageService.TOAST.equals(mDataType)) {
                        String toast = intent.getStringExtra(ManageService.COUNTER_ELSE);
                        LL.V("toast:" + toast);

                        Toast.makeText(WebActivity.this, toast, Toast.LENGTH_LONG).show();
                    } else if ("chatMsg".equals(mDataType)) {
                        ManageChatMsgAtParam data = (ManageChatMsgAtParam) intent.getSerializableExtra(ManageService.COUNTER);

                        LL.V("ManageChatMsgAtParam:eventNo=" + data.getEventNo());
                        WebActivity.this.chatMsgData = data;
                        WebActivity.this.fromTcp = true;
                        webView.loadUrl(mChatUrl);
//                        webView.loadUrl("javascript:flushChat('"+data.getEventNo()+"')");
                    }
                }
            });
        }
    }

}

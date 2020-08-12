package com.wj.work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.lib.kit.utils.LL;
import com.lib.kit.utils.StatusBarUtils;
import com.littlegreens.netty.client.extra.BaseTask;
import com.littlegreens.netty.client.extra.ConnectTask;
import com.littlegreens.netty.client.extra.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.NetDevCompTask;
import com.littlegreens.netty.client.extra.NetInfoTask;
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

    private static final String mHomeUrl = "http://192.168.3.60:8080/wujieweb/page/login/login.html";
//    private static final String mHomeUrl = "http://www.baidu.com";

    private String mDataType = "";
    private String nDataType = "";
    private Intent nIntent;
    private Intent mIntent;
    private NetReceiver netReceiver;
    private ManageReceiver manageReceiver;
    private boolean bind = false;


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
        nIntent.putExtra(NetService.COUNTER,baseTask);
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
        mIntent.putExtra(ManageService.COUNTER,baseTask);
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
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(false);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

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

        ConnectTask connectTask =new ConnectTask();
        connectTask.setIp(ip);
        connectTask.setPort(port);
        connectTask.setFzwno(fzwno);

        sendMsgToManageService("2",connectTask);

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

        sendMsgToNetService("2",netInfoTask );
    }

    @Override
    @JavascriptInterface
    public void deviceComp(String name) {
        NetDevCompTask netDevCompTask = new NetDevCompTask();
        netDevCompTask.setCompanyName(name);
        netDevCompTask.setPRO("CompanyChoosed");
        sendMsgToNetService("3",netDevCompTask);
    }

    @Override
    @JavascriptInterface
    public void saveDevice() {
        LL.V("saveDevice:===" );
        NetDevCompTask netDevCompTask = new NetDevCompTask();
        netDevCompTask.setCompanyName("lifeSmart");
        netDevCompTask.setPRO("SetOk");

        sendMsgToNetService("4",netDevCompTask );
    }

    @Override
    @JavascriptInterface
    public void authOver() {
        LL.V("authOver:===" );
        NetDevCompFileTask netDevCompFileTask = new NetDevCompFileTask();
        netDevCompFileTask.setFile("");
        netDevCompFileTask.setCompanyName("lifeSmart");
        netDevCompFileTask.setPRO("Authority");

        sendMsgToNetService("5",netDevCompFileTask );
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
                sendMsgToNetService("1",null);
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
                @Override
                public void run() {
                    //获取从Service中传来的data
                    nDataType = intent.getStringExtra(NetService.COUNTER_TYPE);
                    if("1".equals(nDataType)){
                        NetDevCompFileTask data = (NetDevCompFileTask) intent.getSerializableExtra(NetService.COUNTER);
                        String ip = intent.getStringExtra(NetService.COUNTER_ELSE);
                        LL.V("ip:"+ip);
                        //更新UI
                        String url = "http://" + ip + ":8080/tcube_app/APP_code/APP_choose_device.php";
                        LL.V("url:"+url);
                        webView.loadUrl(url);
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
                }
            });
        }
    }

}

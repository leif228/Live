package com.wj.work;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.lib.kit.utils.StatusBarUtils;
import com.littlegreens.netty.client.extra.AtProtocol;
import com.littlegreens.netty.client.extra.task.BaseTask;
import com.littlegreens.netty.client.extra.task.Connect;
import com.littlegreens.netty.client.extra.task.ManageChatMsgAtParam;
import com.littlegreens.netty.client.extra.task.ManageChatMsgTask;
import com.littlegreens.netty.client.extra.task.ManageLightTask;
import com.littlegreens.netty.client.extra.task.NetConfigTask;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.task.NetDevCompTask;
import com.littlegreens.netty.client.extra.task.NetInfoTask;
import com.littlegreens.netty.client.extra.task.NetSearchNetDto;
import com.littlegreens.netty.client.extra.task.NetSearchNetDtos;
import com.littlegreens.netty.client.extra.task.NetSearchNetTask;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
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

import java.util.List;

import butterknife.BindView;

public class WebActivity extends BaseMvpActivity<WebPresenter> implements WebViewI, WebViewJavaScriptFunction {

    @BindView(R.id.wv_webview)
    WebView webView;

    //        private static final String mHomeUrl = "http://192.168.4.17:8848/wujieweb/page/login/login.html";
    private static final String mHomeUrl = "http://192.168.4.86:8080/wujieweb/page/login/login.html";
    private static final String mChatUrl = "http://192.168.4.86:8080/wujieweb/page/login/chat.html";
    private static final String mAddChatUrl = "http://192.168.4.86:8080/wujieweb/page/login/addchat.html";
    private static final String mIndexUrl = "http://192.168.4.15:8080/wujieweb/page/login/index.html";
    private static final String baseUrl = "http://192.168.4.86:8080/wujieweb/page/login/iframe.html";
//    private static final String baseUrl = "http://192.168.4.17:8848/wujieweb/page/login/headportrait.html";
//        private static final String baseUrl = "http://www.baidu.com";
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
    private String netSearchNetDtos = "[]";
    private String newClubAt = "";
    private String mobileBackWebParam = "";

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private static int FILE_CHOOSER_RESULT_CODE = 0;


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

    private void sendMsgToNetService(String type, BaseTask baseTask, String elses) {
        //向Service传递data
        nIntent.putExtra(NetService.COUNTER_TYPE, type);
        nIntent.putExtra(NetService.COUNTER, baseTask);
        nIntent.putExtra(NetService.COUNTER_ELSE, elses);
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
                LL.V("onPageFinished.url=" + url);
//                LL.V("onPageFinished.mobileBackWebParam=" + mobileBackWebParam);
//                webView.loadUrl("javascript:fromMobile('" + mobileBackWebParam + "')");

//                if (url.equals(mChatUrl) && fromTcp) {
//                    if (chatMsgData != null) {
//                        LL.V(chatMsgData.getEventNo());
//                        webView.loadUrl("javascript:flushChat('" + chatMsgData.getEventNo() + "')");
//                        chatMsgData = null;
//                    }
//                    fromTcp = false;
//                } else if (url.equals(mAddChatUrl)) {
//                    webView.loadUrl("javascript:flushChat('" + newClubAt + "')");
//                }

//                else if (url.equals(mIndexUrl)) {
//                    LL.V("netSearchNetDtos=" + netSearchNetDtos);
//                    webView.loadUrl("javascript:nets('"+netSearchNetDtos+"')");
//                    netSearchNetDtos = "[]";
//                }
            }

        });
        //android WebView如何响应H5中读取文件的请求，唤起文件浏览界面https://blog.csdn.net/yonghuming_jesse/article/details/80583392
        WebChromeClient chromeClient = new WebChromeClient(){
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        };
        webView.setWebChromeClient(chromeClient);
//        webView.loadUrl(mHomeUrl);
        webView.loadUrl(baseUrl);
    }
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mUploadCallbackAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
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

        sendMsgToManageService(WebViewWebSocketFuctionEnum.toTcp.name(), connect);

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

        sendMsgToNetService(WebViewWebSocketFuctionEnum.toNetInfo.name(), netInfoTask, "");
    }

    @Override
    @JavascriptInterface
    public void deviceComp(String name, String path) {
        this.compName = name;
        NetDevCompFileTask netDevCompTask = new NetDevCompFileTask();
        netDevCompTask.setCompanyName(compName);
        netDevCompTask.setPRO("CompanyChoosed");
        netDevCompTask.setFile(path);
        sendMsgToNetService(WebViewWebSocketFuctionEnum.deviceComp.name(), netDevCompTask, "");
    }

    @Override
    @JavascriptInterface
    public void saveDevice() {
        LL.V("saveDevice:===");
        NetDevCompTask netDevCompTask = new NetDevCompTask();
        netDevCompTask.setCompanyName(compName);
        netDevCompTask.setPRO("SetOk");

        sendMsgToNetService(WebViewWebSocketFuctionEnum.saveDevice.name(), netDevCompTask, "");
    }

    @Override
    @JavascriptInterface
    public void authOver(String path) {
        LL.V("authOver:path=" + path);
        NetDevCompFileTask netDevCompFileTask = new NetDevCompFileTask();
        netDevCompFileTask.setFile(path);
        netDevCompFileTask.setCompanyName(compName);
        netDevCompFileTask.setPRO("Authority");

        sendMsgToNetService(WebViewWebSocketFuctionEnum.authOver.name(), netDevCompFileTask, "");
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
        manageChatMsgTask.setMsgContent(msg);
        manageChatMsgTask.setMsgType("txt");//消息类型:txt;img;voice;video
        sendMsgToManageService(WebViewWebSocketFuctionEnum.sendChatMsg.name(), manageChatMsgTask);

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
        manageChatMsgTask.setMsgContent("手机事件产生了 ");
        manageChatMsgTask.setMsgType("txt");//消息类型:txt;img;voice;video
        sendMsgToManageService(WebViewWebSocketFuctionEnum.toGenEvent.name(), manageChatMsgTask);
    }

    @Override
    @JavascriptInterface
    public void toLightOn() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (loginEntity.getFzwno().equals("")) {
            Toast.makeText(WebActivity.this, "没有注册手机fzwno！", Toast.LENGTH_LONG).show();
            return;
        }
//       String at = "AT@Nchn0L0a30202010260001100000000012120101100150100001FFFF001C{\"way\":\"ctl\",\"val\":\"FFFFFF\"}#*";
        String at = "AT@N" + loginEntity.getFzwno() + "100150100001FFFF001C{\"way\":\"ctl\",\"val\":\"FFFFFF\"}#*";
        LL.V("at=" + at);
        ManageLightTask manageLightTask = new ManageLightTask();
        manageLightTask.setAt(at);
        sendMsgToManageService(WebViewWebSocketFuctionEnum.toLightOn.name(), manageLightTask);
    }

    @Override
    @JavascriptInterface
    public void toAt(String at) {
        LL.V("toAt=" + at);

        try {
            ManageLightTask manageLightTask = new ManageLightTask();
            manageLightTask.setAt(at);
            sendMsgToManageService(WebViewWebSocketFuctionEnum.toAt.name(), manageLightTask);
        } catch (Exception e) {
            LL.E("处理at业务出错了！" + e.getMessage());
            Toast.makeText(WebActivity.this, "处理at业务出错了！" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    @JavascriptInterface
    public void toAtNet(String at) {
        LL.V("toAtNet=" + at);

        try {
            AtProtocol atProtocol = AtProtocol.doAtTask(at);
            sendMsgToNetService(WebViewWebSocketFuctionEnum.toAtNet.name(), null, at);
        } catch (Exception e) {
            LL.E("处理atNet业务出错了！" + e.getMessage());
            Toast.makeText(WebActivity.this, "处理atNet业务出错了！" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    @JavascriptInterface
    public void toLightOff() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        if (loginEntity.getFzwno().equals("")) {
            Toast.makeText(WebActivity.this, "没有注册手机fzwno！", Toast.LENGTH_LONG).show();
            return;
        }
//        String at = "AT@Nchn0L0a30202010260001100000000012120101100150100001FFFF001C{\"way\":\"ctl\",\"val\":\"000000\"}#*";
        String at = "AT@N" + loginEntity.getFzwno() + "100150100001FFFF001C{\"way\":\"ctl\",\"val\":\"000000\"}#*";
        LL.V("at=" + at);
        ManageLightTask manageLightTask = new ManageLightTask();
        manageLightTask.setAt(at);
        sendMsgToManageService(WebViewWebSocketFuctionEnum.toLightOff.name(), manageLightTask);
    }

    @Override
    @JavascriptInterface
    public void toSearchNet() {
        sendMsgToNetService(WebViewWebSocketFuctionEnum.toSearchNet.name(), null, "");
    }

    @Override
    @JavascriptInterface
    public void toNetTcp(String ip) {
        sendMsgToNetService(WebViewWebSocketFuctionEnum.toNetTcp.name(), null, ip);
    }

    @Override
    @JavascriptInterface
    public void toConfigNet(String ntype, String addr) {

        LL.V("-----------    ntype=" + ntype + ",addr=" + addr);
        NetConfigTask netConfigTask = new NetConfigTask();
        netConfigTask.setDevtype(ntype);
        netConfigTask.setInaeraaddr(addr);
        sendMsgToNetService(WebViewWebSocketFuctionEnum.toConfigNet.name(), netConfigTask, "");
    }

    @Override
    @JavascriptInterface
    public void fromWeb(String request) {
        LL.V("-----------webFun.param=" + request);
        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            LL.V("不支持的消息数据格式！");
            return;
        }
        if (param == null) {
            LL.V("不支持的消息数据格式！不能为空！");
            return;
        }
        String type = param.get("type").toString();
        WebViewWebSocketFuctionEnum webViewWebSocketFuctionEnum = null;
        try {
            webViewWebSocketFuctionEnum = WebViewWebSocketFuctionEnum.valueOf(type);
        } catch (Exception e) {
            LL.V("不支持的消息数据格式！类别解析出错！");
            return;
        }
        Object data = param.get("data");
        switch (webViewWebSocketFuctionEnum) {
            case toHeart:
//                backMsg(webViewWebSocketFuctionEnum, "心跳返回");//{"type":"toHeart","data":{}}
                break;

            case toTcp:
                Connect connect = JSONObject.parseObject(data.toString(), Connect.class);

                LoginEntity loginEntity = new LoginEntity();
                loginEntity.setIp(connect.getIp());
                loginEntity.setPort(connect.getPort());
                loginEntity.setFzwno(connect.getFzwno());
                SpManager.getInstance().getLoginSp().putLoginInfoEntity(loginEntity);

                sendMsgToManageService(webViewWebSocketFuctionEnum.name(), connect);
                break;

            case sendChatMsg:
                ManageChatMsgTask manageChatMsgTask = JSONObject.parseObject(data.toString(), ManageChatMsgTask.class);
                sendMsgToManageService(webViewWebSocketFuctionEnum.name(), manageChatMsgTask);
                break;
            case toGenEvent:
                ManageChatMsgTask manageChatMsgTask2 = JSONObject.parseObject(data.toString(), ManageChatMsgTask.class);
                sendMsgToManageService(webViewWebSocketFuctionEnum.name(), manageChatMsgTask2);
                break;
//            case toLightOn:
//                TcpClient.sendMsgToManageService(webViewWebSocketFuctionEnum, data);
//                break;
//            case toLightOff:
//                TcpClient.sendMsgToManageService(webViewWebSocketFuctionEnum, data);
//                break;
            case toAt:
                ManageLightTask manageLightTask3 = JSONObject.parseObject(data.toString(), ManageLightTask.class);
                sendMsgToManageService(webViewWebSocketFuctionEnum.name(), manageLightTask3);
                break;


            case toNetInfo:
                NetInfoTask netInfoTask = JSONObject.parseObject(data.toString(), NetInfoTask.class);
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), netInfoTask, "");
                break;
            case deviceComp:
                NetDevCompFileTask netDevCompTask = JSONObject.parseObject(data.toString(), NetDevCompFileTask.class);
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), netDevCompTask, "");
                break;
            case saveDevice:
                NetDevCompTask netDevCompTask2 = JSONObject.parseObject(data.toString(), NetDevCompTask.class);
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), netDevCompTask2, "");
                break;
            case authOver:
                NetDevCompFileTask netDevCompFileTask = JSONObject.parseObject(data.toString(), NetDevCompFileTask.class);
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), netDevCompFileTask, "");
                break;
            case toSearchNet:
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), new NetSearchNetTask(), "");
                break;
            case toNetTcp:
                String ip = (String) data;
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), new BaseTask(), ip);
                break;
            case toConfigNet:
                NetConfigTask netDevCompTask3 = JSONObject.parseObject(data.toString(), NetConfigTask.class);
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), netDevCompTask3, "");
                break;
            case toAtNet:
                String at = (String) data;
                sendMsgToNetService(webViewWebSocketFuctionEnum.name(), new BaseTask(), at);
                break;
            default:
        }
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
                sendMsgToNetService("1", null, "");
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
                    try {
                        String nType = intent.getStringExtra(NetService.COUNTER_TYPE);
                        LL.V("nType:" + nType);
                        String nData = intent.getStringExtra(NetService.COUNTER_ELSE);

                        WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum webViewWebSocketFuctionEnum = WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.valueOf(nType);

                        switch (webViewWebSocketFuctionEnum) {
                            case toNetInfo:
                                break;
                            case deviceComp:
                                break;
                            case saveDevice:
                                break;
                            case authOver:
                                break;
                            case toSearchNet:
                                NetSearchNetDtos data = (NetSearchNetDtos) intent.getSerializableExtra(NetService.COUNTER);
                                List<NetSearchNetDto> netSearchNetDtoList = data.getNetSearchNetDtos();
                                nData = JSONObject.toJSONString(netSearchNetDtoList);
                                break;
                            case toNetTcp:
                                break;
                            case toConfigNet:
                                break;
                            case toAtNet:
                                break;
                            default:
                        }
                        LL.V("nData:" + nData);

                        Toast.makeText(WebActivity.this, nData, Toast.LENGTH_LONG).show();
                        backMsg(WebViewWebSocketFuctionEnum.valueOf(nType), nData);
                    } catch (Exception e) {
                        LL.E(e.getMessage());
                        e.printStackTrace();
                    }

//                    if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.nettyNetGetDevListOver.name().equals(nDataType)) {
//                        NetDevCompFileTask data = (NetDevCompFileTask) intent.getSerializableExtra(NetService.COUNTER);
//                        String ip = intent.getStringExtra(NetService.COUNTER_ELSE);
//                        LL.V("ip:" + ip);
//                        //更新UI
//                        String url = "http://" + ip + ":8080/tcube_app/APP_code/APP_choose_device.php";
//                        LL.V("url:" + url);
//                        webView.loadUrl(url);
//                    } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.nettyNetFileDownOver.name().equals(nDataType)) {
//                        String ip = intent.getStringExtra(NetService.COUNTER_ELSE);
//                        LL.V("ip:" + ip);
//
//                        //更新UI
//                        String url = "http://" + ip + ":8080/tcube_app/APP_code/APP_choose_device.php";
//                        LL.V("url:" + url);
//                        webView.loadUrl(url);
//                    } else if (NetService.TOAST.equals(nDataType)) {
//                        String toast = intent.getStringExtra(NetService.COUNTER_ELSE);
//                        LL.V("toast:" + toast);
//
//                        Toast.makeText(WebActivity.this, toast, Toast.LENGTH_LONG).show();
//                    } else if (WebViewJavaScriptFunction.WebViewWebSocketFuctionEnum.backNets.name().equals(nDataType)) {
//                        NetSearchNetDtos data = (NetSearchNetDtos) intent.getSerializableExtra(NetService.COUNTER);
//                        List<NetSearchNetDto> netSearchNetDtoList = data.getNetSearchNetDtos();
//                        netSearchNetDtos = JSONObject.toJSONString(netSearchNetDtoList);
//                        LL.V("json:" + netSearchNetDtos);
////                        webView.loadUrl(mIndexUrl);
//                        webView.loadUrl("javascript:nets('" + netSearchNetDtos + "')");
////                        Toast.makeText(WebActivity.this, toast, Toast.LENGTH_LONG).show();
//                    }
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
                    try {
                        String mType = intent.getStringExtra(ManageService.COUNTER_TYPE);
                        LL.V("mType:" + mType);
                        String mData = intent.getStringExtra(ManageService.COUNTER_ELSE);
                        LL.V("mData:" + mData);

                        Toast.makeText(WebActivity.this, mData, Toast.LENGTH_LONG).show();
                        backMsg(WebViewWebSocketFuctionEnum.valueOf(mType), mData);
                    } catch (Exception e) {
                        LL.E(e.getMessage());
                        e.printStackTrace();
                    }
                    //更新UI
//                    if (ManageService.TOAST.equals(mDataType)) {
//                        String toast = intent.getStringExtra(ManageService.COUNTER_ELSE);
//                        LL.V("toast:" + toast);
//
//                        Toast.makeText(WebActivity.this, toast, Toast.LENGTH_LONG).show();
//                        backMsg(WebViewWebSocketFuctionEnum.manageBack,toast);
//                    } else if ("chatMsg".equals(mDataType)) {
//                        ManageChatMsgAtParam data = (ManageChatMsgAtParam) intent.getSerializableExtra(ManageService.COUNTER);
//
//                        LL.V("ManageChatMsgAtParam:eventNo=" + data.getEventNo());
//                        WebActivity.this.chatMsgData = data;
//                        WebActivity.this.fromTcp = true;
////                        webView.loadUrl(mChatUrl);
//                    } else if ("newClub".equals(mDataType)) {
//                        newClubAt = intent.getStringExtra(ManageService.COUNTER_ELSE);
//                        LL.V("newClub:" + newClubAt);
//
////                        webView.loadUrl(mAddChatUrl);
//                    }
                }
            });
        }
    }

    public void backMsg(WebViewWebSocketFuctionEnum type, String msg) {
        JSONObject resultJson = new JSONObject();
        JSONObject resutlObj = new JSONObject();
        resutlObj.put("code", "0");
        resutlObj.put("msg", msg);

        resultJson.put("type", type.name());
        resultJson.put("data", resutlObj);
        String mobileBackWebParam = resultJson.toJSONString();

//        webView.loadUrl(baseUrl);
        webView.loadUrl("javascript:fromMobile('" + mobileBackWebParam + "')");
    }

}

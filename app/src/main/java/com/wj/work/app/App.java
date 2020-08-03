package com.wj.work.app;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.wj.work.receiver.NetReceiver;
import com.lib.kit.base.BaseApplication;
import com.lib.kit.utils.SmallUtils;
import com.qiniu.pili.droid.streaming.StreamingEnv;

/**
 * TODO Application
 *
 * @author Ly
 * 2019/10/28 11:38
 * 物界
 * {www.wj.com
 */
public class App extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SmallUtils.init(this); // 初始化SmallUtils
//        StreamingEnv.init(getApplicationContext());
        initReceive();

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    private NetReceiver mReceiver;

    private void initReceive() {
        mReceiver = new NetReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onTerminate() {
        unregisterReceiver(mReceiver);
        super.onTerminate();
    }
}
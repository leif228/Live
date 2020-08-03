package com.wj.work.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.wj.work.event.NetworkEvent;
import com.lib.kit.utils.LL;
import com.lib.kit.utils.NetworkUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * NetReceiver
 * 2020/5/12 15:44
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = NetworkUtils.isAvailable();
            boolean isMobileData = NetworkUtils.isMobileData();
            boolean isWiFiAvailable = NetworkUtils.isWiFiAvailable();
            LL.V("网络状态：" + isConnected);
            LL.V("wifi状态 " + isWiFiAvailable);
            LL.V("移动网络状态 " + isMobileData);
            EventBus.getDefault().postSticky(new NetworkEvent()
                    .setConnected(isConnected)
                    .setMobileData(isMobileData)
                    .setWiFiAvailable(isWiFiAvailable)
            );
        }
    }
}
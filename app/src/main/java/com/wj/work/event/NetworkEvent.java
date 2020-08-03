package com.wj.work.event;

/**
 * NetworkEvent
 * 2020/5/12 16:01
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class NetworkEvent {

    private boolean isConnected;
    private boolean isWiFiAvailable;
    private boolean isMobileData;

    public boolean isConnected() {
        return isConnected;
    }

    public NetworkEvent setConnected(boolean connected) {
        isConnected = connected;
        return this;
    }

    public boolean isWiFiAvailable() {
        return isWiFiAvailable;
    }

    public NetworkEvent setWiFiAvailable(boolean wiFiAvailable) {
        isWiFiAvailable = wiFiAvailable;
        return this;
    }

    public boolean isMobileData() {
        return isMobileData;
    }

    public NetworkEvent setMobileData(boolean mobileData) {
        isMobileData = mobileData;
        return this;
    }
}

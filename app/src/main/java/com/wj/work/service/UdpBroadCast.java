package com.wj.work.service;

import android.util.Log;

import com.lib.kit.utils.LL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class UdpBroadCast extends Thread {

    MulticastSocket sender = null;
    DatagramPacket dj = null;
    InetAddress group = null;

    byte[] data = new byte[1024];

    public UdpBroadCast(String dataString) {
        data = dataString.getBytes();
    }

    @Override
    public void run() {
        try {
            sender = new MulticastSocket();
            group = InetAddress.getByName(getLocAddress());
            LL.V("localhost ip: " + group.getHostAddress());
            dj = new DatagramPacket(data,data.length,group,6789);
            sender.send(dj);
            sender.close();
        } catch(IOException e) {
            e.printStackTrace();
            LL.V("UdpBroadCast: " + "发送udp广播报错了！");
        }
    }

    /**
     * TODO<获取本地ip地址>
     *
     * @return String
     */
    private String getLocAddress() {
        String ipaddress = "";

        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface networks = en.nextElement();
                // 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> address = networks.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (!ip.isLoopbackAddress()
                            && (ip instanceof Inet4Address)) {
                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ly", "获取本地ip地址失败");
            e.printStackTrace();
        }

        Log.e("ly", "本机IP:" + ipaddress);
        return ipaddress;
    }
}

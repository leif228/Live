package com.wj.work.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.alibaba.fastjson.JSONObject;
import com.lib.kit.utils.LL;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.task.NetSearchNetDto;
import com.littlegreens.netty.client.extra.task.NetSearchNetTask;
import com.wj.work.app.App;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BroadCastToCenter extends Thread {

    public static final Integer udpport = 8666;
    //    Timer timer = new Timer();
    NetSearchNetTask netSearchNetTask;
    DatagramSocket theSocket = null;
    List<NetSearchNetDto> netSearchNetDtos = new ArrayList<>();
//    boolean isStay = true;

    public BroadCastToCenter(NetSearchNetTask netSearchNetTask) {
        this.netSearchNetTask = netSearchNetTask;
    }

    public void stopStay() {
        if (theSocket != null)
            theSocket.close();
//        super.stop();
//        timer.cancel();
    }

    public List<NetSearchNetDto> getNetSearchNetDtos() {
        return netSearchNetDtos;
    }

//    public boolean isStay() {
//        return isStay;
//    }

    @Override
    public void run() {
        super.run();
        DatagramSocket theSocket = null;
        try {
            WifiManager wifiMgr = (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            /*这里获取了IP地址，获取到的IP地址还是int类型的。*/
            int ip = wifiInfo.getIpAddress();
            LL.V("ip=" + ip);

            String localIp = Formatter.formatIpAddress(ip);
            LL.V("localIp=" + localIp);

		/*这里就是将int类型的IP地址通过工具转化成String类型的，便于阅读
        String ips = Formatter.formatIpAddress(ip);
		*/

            /*这一步就是将本机的IP地址转换成xxx.xxx.xxx.255*/
            int broadCastIP = ip | 0xFF000000;
            LL.V("broadCastIP=" + broadCastIP);

            String ips = Formatter.formatIpAddress(broadCastIP);
            LL.V("ips=" + ips);

            InetAddress server = InetAddress.getByName(ips);
            theSocket = new DatagramSocket();

//                    String data = "Hello";
            String data = JSONObject.toJSONString(netSearchNetTask);
            LL.V("sendData=" + data);

            DatagramPacket theOutput = new DatagramPacket(data.getBytes(), data.length(), server, udpport);
            /*这一句就是发送广播了，其实255就代表所有的该网段的IP地址，是由路由器完成的工作*/
            theSocket.send(theOutput);

//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    isStay = false;
//                }
//            }, 1000l);

            byte[] buffer = new byte[1024];
            DatagramPacket packetrc = new DatagramPacket(buffer, buffer.length);
            while (true) {
                theSocket.receive(packetrc);
                String src = new String(packetrc.getData(), 0, packetrc.getLength(), "UTF-8");
                LL.V("local_address : " + packetrc.getAddress() + ", port : " + packetrc.getPort() + ", content : " + src);

                NetSearchNetDto netSearchNetDto = new NetSearchNetDto();
                netSearchNetDto.setIp(packetrc.getAddress().getHostAddress());
                JSONObject objParam = JSONObject.parseObject(src);
                NetSearchNetTask netSearchNetTask = JSONObject.toJavaObject(objParam, NetSearchNetTask.class);
                netSearchNetDto.setNetSearchNetTask(netSearchNetTask);

                netSearchNetDtos.add(netSearchNetDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LL.V("sendBroadCastToCenter  Exception=" + e.getMessage());
        } finally {
            if (theSocket != null)
                theSocket.close();
        }
    }


//    private void udpDiscardSServer() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] buffer = new byte[1024];
//                /*在这里同样使用约定好的端口*/
//                DatagramSocket server = null;
//                try {
//                    server = new DatagramSocket(udpport);
//                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//                    while (true) {
//                        server.receive(packet);
//                        String rcIp = packet.getAddress().getHostAddress();
//                        LL.V("rcIp= : " +rcIp);
//                        String s = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
//                        if(rcIp.equals(localIp)){
//                            LL.V("local_address : " + packet.getAddress() + ", port : " + packet.getPort() + ", content : " + s);
//                        }else{
//                            LL.V("address : " + packet.getAddress() + ", port : " + packet.getPort() + ", content : " + s);
//                            connectNet(rcIp);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LL.V("udpDiscardSServer  Exception=" + e.getMessage());
//                } finally {
//                    if (server != null)
//                        server.close();
//                }
//            }
//        }).start();
//    }
}

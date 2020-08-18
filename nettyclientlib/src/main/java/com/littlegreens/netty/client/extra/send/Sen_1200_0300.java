package com.littlegreens.netty.client.extra.send;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.task.BaseTask;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.task.NetDevCompTask;

public class Sen_1200_0300 implements Sen_i{
    public static final byte[] main = new byte[]{0x12, 0x00};
    public static final byte[] sub = new byte[]{0x03, 0x00};

    @Override
    public WjProtocol generateWj(BaseTask baseTask) {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(main);
        wjProtocol.setSubcmd(sub);
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

        int len = 0;
        byte[] objectBytes = null;
        if(baseTask != null){
            NetDevCompTask netDevCompTask = (NetDevCompTask) baseTask;

            String jsonStr = JSONObject.toJSONString(netDevCompTask);
            Log.v(WjProtocol.TAG, jsonStr);
            objectBytes = jsonStr.getBytes();

            len = objectBytes.length;
        }

        len = WjProtocol.MIN_DATA_LEN + len;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));
        wjProtocol.setUserdata(objectBytes);

        return wjProtocol;
    }
}

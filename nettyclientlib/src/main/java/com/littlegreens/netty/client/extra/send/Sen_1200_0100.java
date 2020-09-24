package com.littlegreens.netty.client.extra.send;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.task.BaseTask;
import com.littlegreens.netty.client.extra.task.NetDevCompFileTask;
import com.littlegreens.netty.client.extra.task.NetDevCompTask;

import java.io.UnsupportedEncodingException;

public class Sen_1200_0100 implements Sen_i{
    public static final byte[] main = new byte[]{0x12, 0x00};
    public static final byte[] sub = new byte[]{0x01, 0x00};

    @Override
    public WjProtocol generateWj(BaseTask baseTask) throws UnsupportedEncodingException {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(main);
        wjProtocol.setSubcmd(sub);
        wjProtocol.setFormat("JS");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

        int len = 0;
        byte[] objectBytes = null;
        if(baseTask != null){
//            NetDevCompTask netDevCompTask = (NetDevCompTask) baseTask;
            NetDevCompFileTask netDevCompFileTask = (NetDevCompFileTask) baseTask;

            String jsonStr = JSONObject.toJSONString(netDevCompFileTask);
            Log.v(WjProtocol.TAG, jsonStr);
            objectBytes = jsonStr.getBytes("UTF-8");

            len = objectBytes.length;
        }

        len = WjProtocol.MIN_DATA_LEN + len;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));
        wjProtocol.setUserdata(objectBytes);

        return wjProtocol;
    }
}

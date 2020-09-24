package com.littlegreens.netty.client.extra.send;

import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.task.BaseTask;

import java.io.UnsupportedEncodingException;

public class Sen_0000_0000 implements Sen_i{
    public static final byte[] main = new byte[]{0x00, 0x00};
    public static final byte[] sub = new byte[]{0x00, 0x00};

    @Override
    public WjProtocol generateWj(BaseTask baseTask) throws UnsupportedEncodingException {
        WjProtocol wjProtocol = new WjProtocol();
        wjProtocol.setPlat(new byte[]{0x50, 0x00});
        wjProtocol.setMaincmd(main);
        wjProtocol.setSubcmd(sub);
        wjProtocol.setFormat("TX");
        wjProtocol.setBack(new byte[]{0x00, 0x00});

        int len = 0;
        byte[] objectBytes = null;
//        if(baseTask != null){
//            NetInfoTask netInfoTask = (NetInfoTask) baseTask;
//
//            String jsonStr = JSONObject.toJSONString(netInfoTask);
//            Log.v(WjProtocol.TAG, jsonStr);
//            objectBytes = jsonStr.getBytes();
//
//            len = objectBytes.length;
//        }

        len = WjProtocol.MIN_DATA_LEN + len;
        wjProtocol.setLen(wjProtocol.short2byte((short) len));
        wjProtocol.setUserdata(objectBytes);

        return wjProtocol;
    }
}

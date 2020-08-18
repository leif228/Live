package com.littlegreens.netty.client.extra.send;

import android.util.Log;

import com.littlegreens.netty.client.extra.WjDecoderHandler;
import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.receive.Rec_task_i;
import com.littlegreens.netty.client.extra.task.BaseTask;

public class Sen_factory {
    public static WjProtocol getInstance(byte[] main, byte[] sub, BaseTask baseTask){
        //通用处理
        try {
            String classPath = "com.littlegreens.netty.client.extra.send";
            String className = "Sen_" + IntToHexStringLimit2(main[0]) + IntToHexStringLimit2(main[1]) + "_"
                    + IntToHexStringLimit2(sub[0]) + IntToHexStringLimit2(sub[1]);
            className = classPath + "." + className;
            Log.v(WjDecoderHandler.TAG, "className:" + className);
            Class genClass = Class.forName(className);
            Sen_i sen_i = (Sen_i) genClass.newInstance();

            return sen_i.generateWj(baseTask);
        } catch (Exception e) {
            Log.e(WjDecoderHandler.TAG, "Sen_factory.getInstance_报错了:" + e.getMessage());
        }
        return null;
    }

    private static String IntToHexStringLimit2(int num) {

        String hexString = Integer.toHexString(num);
        switch (hexString.length()) {
            case 1:
                hexString = "0" + hexString;
                break;
            case 2:
                hexString = hexString;
                break;
            default:
                hexString = null;
                break;
        }

        return hexString;
    }
}

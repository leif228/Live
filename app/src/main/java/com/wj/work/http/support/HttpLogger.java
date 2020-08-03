package com.wj.work.http.support;


import android.util.Log;

import androidx.annotation.NonNull;

import com.lib.kit.utils.JsonUtils;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by tanghu on 2018/6/14.
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuffer mMessage = new StringBuffer();

    @Override
    public void log(@NonNull String message) {
        if (message.startsWith("--> POST")
                || message.startsWith("--> GET")
                || message.startsWith("--> PUT")
                || message.startsWith("--> DELETE")) {
            mMessage.setLength(0);
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}")) || (message.startsWith("[") && message.endsWith("]"))) {
            message = JsonUtils.format(JsonUtils.decodeUnicode(message));
        }

        mMessage.append(message.concat("\n"));
        // 响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            Log.v("Http",mMessage.toString());
            mMessage.setLength(0);
        }
    }
}

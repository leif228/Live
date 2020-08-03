package com.lib.kit.utils;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

/**
 * Application --  必须初始化
 */
public class SmallUtils {
    @SuppressLint("StaticFieldLeak")
    private static Application mApplication;

    private SmallUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * 初始化工具类
     * @param app 应用
     */
    public static void init(@NonNull final Application app) {
        SmallUtils.mApplication = app;
    }

    /**
     * 获取Application
     * @return Application
     */
    public static Application getApp() {
        if (mApplication != null) return mApplication;
        throw new NullPointerException("SmallUtils should init first");
    }
}

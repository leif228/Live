package com.lib.kit.base;

import android.app.Application;
import android.content.Context;

/**
 * BaseApplication
 * 2020/5/12 15:54
 * wj
 * wj
 *
 * @author Ly
 */
public class BaseApplication extends Application {

    private static volatile BaseApplication mInstance;

    public static Context getContext(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
    }
}

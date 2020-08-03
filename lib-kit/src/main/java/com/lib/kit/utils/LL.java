package com.lib.kit.utils;

import android.util.Log;

import java.util.ArrayList;

/**
 * Log 打印
 */
public class LL {

    private static final boolean ISDEBUG=true;

    private static String TAG = "ly";

    public static void V(String target) {
        if (ISDEBUG)Log.v(TAG, target);
    }

    public static void stringArray(ArrayList<String> content) {
        if (!ISDEBUG)return;
        for (int i = 0; i < content.size(); i++) {
            Log.v(TAG, "i=" + i + "   ---- " + content.get(i));
        }
    }

    public static void  printObject(Object target){
        if (!ISDEBUG)return;
    }
}

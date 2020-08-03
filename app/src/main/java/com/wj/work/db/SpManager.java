package com.wj.work.db;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.wj.work.db.LoginSp;
import com.lib.kit.utils.SmallUtils;

public class SpManager {

    private SharedPreferences sp;
    private static volatile SpManager instance;
    private com.wj.work.db.LoginSp mLoginSp;

    public static SpManager getInstance() {
        if (instance == null) {
            synchronized (com.wj.work.db.LoginSp.class) {
                if (instance == null) {
                    instance = new SpManager();
                }
            }
        }
        return instance;
    }

    // 所有操作在具体的某个SP里
    private SpManager() {
        sp = SmallUtils.getApp().getSharedPreferences("app_jxj_preferences", Context.MODE_PRIVATE);
        mLoginSp = new com.wj.work.db.LoginSp(this);
        //
    }

    public LoginSp getLoginSp() {
        return mLoginSp;
    }

    // 清空所有SP存储
    public void clear() {
        sp.edit().clear().apply();
    }

    //退出登录
    public void forLogout() {
        mLoginSp.forLogout();
    }

    // ----------------------------------------------------------------------------------------------

    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the string value if sp exists or {@code defaultValue} otherwise
     */
    String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    void putString(@NonNull String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    // -------------
    void putLong(@NonNull String key, long value) {
        sp.edit().putLong(key, value).apply();
    }

    /**
     * Return the long value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the long value if sp exists or {@code defaultValue} otherwise
     */
    long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    // -------------
    void putInt(@NonNull String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    // -------------
    void putBoolean(@NonNull String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    boolean getBooleant(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    // public -----------------------------------------------------------------------------------
}

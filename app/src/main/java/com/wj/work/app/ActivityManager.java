package com.wj.work.app;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity 管理类
 * <p>
 * 2019/10/28 11:38
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ActivityManager {

    private static Stack<Activity> mActivityStack;
    private volatile static ActivityManager mInstance;

    private ActivityManager() {
    }

    //获取单一实例
    public static ActivityManager instance() {
        if (mInstance == null) {
            synchronized (ActivityManager.class) {
                if (mInstance == null) mInstance = new ActivityManager();
                if (mActivityStack == null) mActivityStack = new Stack<>();
            }
        }
        return mInstance;
    }

    //添加activity
    public void addActivity(Activity activity) {
        if (mActivityStack == null) mActivityStack = new Stack<>();
        mActivityStack.add(activity);
    }

    //finish当前Activity
    public void finishActivity() {
        if (mActivityStack == null) return;
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    //finish指定的Activity
    public void finishActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    //再关闭ac 后移除 移除Activity
    public void removeActivity(Activity activity) {
        if (activity != null && mActivityStack != null) mActivityStack.remove(activity);
    }

    //finish指定类名的Activity
    public void finishActivity(Class<?> cls) {
        if (mActivityStack == null) return;
        try {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) finishActivity(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //finish全部的Activity
    public void finishAllActivity() {
        for (Activity activity : mActivityStack) {
            finishActivity(activity);
        }
    }

    //finish全部的Activity 除去指定Activity
    public void finishAllActivityExceptOne(Class cls) {
        for (Activity activity : mActivityStack) {
            if (!activity.getClass().equals(cls)) finishActivity(activity);
        }
    }
}

package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;

import com.wj.work.R;
import com.wj.work.base.BaseActivity;
import com.wj.work.widget.helper.ActivityAnimatorHelper;

/**
 * AdvertiseActivity
 * 2020/5/5 10:47
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class AdvertiseActivity extends BaseActivity {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, AdvertiseActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_advertise;
    }

    @Override
    protected void initView() {

    }
}

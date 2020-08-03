package com.wj.work.widget.helper;

import android.app.Activity;

import com.wj.work.R;

/**
 * ActivityAnimatorHelper
 * 2020/4/22 11:28
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ActivityAnimatorHelper {

    public static void startFromRight(Activity activity) {
        activity.overridePendingTransition(R.anim.start_right_enter, R.anim.start_right_exit);
    }

}

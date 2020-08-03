package com.wj.work.ui.helper;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * ViewHelper
 * 2020/4/22 14:25
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ViewHelper {


    public static void accuracyViewHeight(View target){
        ViewTreeObserver vto = target.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                target.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ViewGroup.LayoutParams params = target.getLayoutParams();
                params.height=target.getHeight();
                target.setLayoutParams(params);
            }
        });
    }

}

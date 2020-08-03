package com.lib.kit.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by tanghu on 2018/6/25.
 */
public class DensityUtils
{
    public static int getScreenWidth()
    {
        return getDisplayMetrics().widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics()
    {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static int getScreenHeight()
    {
        return getDisplayMetrics().heightPixels;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue)
    {
        final float scale = getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue)
    {
        final float scale = getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue)
    {
        float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     *
     * @param sp
     * @return
     */
    public static int sp2px(float sp)
    {
        final float scale = getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}

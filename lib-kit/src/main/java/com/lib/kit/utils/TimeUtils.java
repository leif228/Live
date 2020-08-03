package com.lib.kit.utils;

import android.text.TextUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * TODO Date 转为 String
     * -->   2015-04-12
     */
    public static String dateToStringYYYYMMDDHHMM(Date date) {
        if (date == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     */
    public static Date YYYYMMDDToDate(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(time, pos);
    }

    /**
     * long 转为 String    支持10位轉換
     *
     * @param time   时间 long
     * @param format 模版  "yyyy-MM-dd HH:mm:ss"
     */
    public static String longToString(String time, String format) {

        if (TextUtils.isEmpty(time)) {
            return "";
        }
        long ltime = Long.parseLong(time);
        if (time.length() == 10) {
            ltime = ltime * 1000;
        }
        Date date = new Date(ltime);
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 获取 时间 字符串
     *
     * @param sTime 秒
     */
    public static String getTime(long sTime) {

        if (sTime < 60) {
            return sTime + "秒";   // 秒
        }

        if (sTime < 60 * 60) {       // 几分几秒
            return sTime / 60 + "分" + getTime(sTime % 60);
        }
        if (sTime < 24 * 60 * 60) {
            String a = sTime / (60 * 60) + "小时" + getTime(sTime % (60 * 60));
            System.out.println(a);
            // 几时 几分 几秒
            return a;
        } else {
            return sTime / (60 * 60 * 24) + "天" + getTime(sTime % (60 * 60 * 24));
        }
    }


    /**
     * long 转为 String
     *
     * @param time   时间 long
     */
    public static String LongToMMSS(long time) {
        int secound = (int) (time / 1000);
        long mill=time%1000;
        int millw= (int) (mill/10);
        String secoundStr=secound<10?("0"+secound):secound+"";
        String millStr=millw<10?("0"+millw):millw+"";
        return secoundStr+":"+millStr;
    }


    /**
     * long 转为 String
     * @param time   时间 long    00:05 5秒
     */
    public static String LongTo00SS(long time) {
        int secound = (int) (time / 1000);
        String secoundStr=secound<10?("0"+secound):secound+"";
        return "00:"+secoundStr;
    }
}

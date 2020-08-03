package com.wj.work.utils;

import com.lib.kit.utils.LL;

import java.util.Calendar;
import java.util.Date;

/**
 * DateTimeUtils
 * 2020/4/27 11:20
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class DateTimeUtils {

    public static String showAdvanceTime(Date date){
        StringBuilder sb=new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        return sb.append(month+1)
                .append("月")
                .append(day)
                .append("日")
                .append(" ")
                .append(hour)
                .append("点")
                .append(minute)
                .append("分")
                .toString();
    }

    public static String showAdvanceDay(Date date){
        String result="";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day=calendar.get(Calendar.DAY_OF_WEEK);
        LL.V(" day = "+day);
        switch (day){
            case 2:
                result="星期一";
                break;
            case 3:
                result="星期二";
                break;
            case 4:
                result="星期三";
                break;
            case 5:
                result="星期四";
                break;
            case 6:
                result="星期五";
                break;
            case 7:
                result="星期六";
                break;
            case 1:
                result="星期天";
                break;
        }
        return result;
    }
}

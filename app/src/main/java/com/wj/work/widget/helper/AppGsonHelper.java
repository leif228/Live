package com.wj.work.widget.helper;

import com.google.gson.Gson;

/**
 * AppGsonHelper
 * 2020/4/22 11:00
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class AppGsonHelper {

    public static String toJson(Object target){
        return new Gson().toJson(target);
    }

    public static <T> T fromJson(String target,Class<T> tClass){
//        Integer aa = new Gson().fromJson("deleteData", new TypeToken<T>() {}.getType());
        return new Gson().fromJson(target, tClass);
    }

}

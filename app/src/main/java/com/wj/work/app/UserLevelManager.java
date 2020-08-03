package com.wj.work.app;

/**
 * UserLevelManager
 * 2020/4/30 13:44
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public final class UserLevelManager {

    public static final  int LEVEL_LARGE=0x001; // 大管家
    public static final  int LEVEL_SMALL=0x002; // 小管家
    public static final  int LEVEL_SUPER=0x003; // 超级管家
    public static final  int LEVEL_LARGE_SUPER=0x004; // 大管家+超级管家
    public static final  int LEVEL_SMALL_SUPER=0x005; // 小管家+超级管家

    public static int getUserLevel(){
        return 1;
    }

    public static String showUserLevel(){
        return "小管家";
    }

}

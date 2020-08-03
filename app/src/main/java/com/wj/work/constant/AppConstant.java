package com.wj.work.constant;

import android.Manifest;

import com.wj.work.BuildConfig;

/**
 * @author Ly
 * AppConstant
 * 2019/10/30 13:40
 * 物界
 * {www.wj.com
 */
public final class AppConstant {

//    public static final String RETROFIT_BASE_URL = "http://shenshishibie.tljnn.com:9092/";
    public static final String RETROFIT_BASE_URL = "http://192.168.1.99:8888/api/";

    public static final int HTTP_CONNECT_TIMEOUT = 15;// 秒
    public static final int HTTP_WRITE_TIMEOUT = 90;// 秒


    // 选择图片(包括照相) 需要的权限集合
    public static String[] CAMERA_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,                     // 相机
            Manifest.permission.READ_EXTERNAL_STORAGE,      // 读权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE      // 写权限
    };

    // 录音 需要的权限集合
    public static String  AUDIO_PERMISSIONS= "android.permission.RECORD_AUDIO";

    // 直播 需要的权限集合
    public static String[]  LIVE_PERMISSIONS= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    // 分享 需要的权限集合
    public static String[] SHARE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    // Splash 权限集合
    public static String[] SPLASH_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    // Splash 权限集合
    public static String[] FILE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int PICK_PHOTO_DATA = 0x5521;
    public static final int CAMERA_PHOTO_DATA = 0x5522;
    public static final int PICK_FILE_DATA = 0x5523;
    public static String INTENT_IMG_LIST_SELECT = "intent_img_list_select";

    public static String WX_APP_ID = "wx452631d58a4ec55a"; // 微信appId
    public static String WX_APP_SECRET = "8aaea5287ba789f9366abda7d1266acc"; // 微信appSecret
    public static String PARTNER_ID = "1559226651"; // 微信商户ID
    public static String QQ_APP_ID = "101818769";
    public static String QQ_APP_KEY = "6d7f1491cbf880b3bc805be3f2b20fbe";

    public static int SPLASH_TIME = 2; // SplashActivity 最短停留时间

    public static final String FILE_PROVIDER = BuildConfig.APPLICATION_ID + ".fileProvider";
}

package com.wj.work.widget.helper;

import android.content.Context;

import com.wj.work.constant.AppConstant;
import com.xiaosu.lib.permission.OnRequestPermissionsCallBack;
import com.xiaosu.lib.permission.PermissionCompat;

/**
 * PermissionHelper
 * 常用动态权限申请
 * 2019/10/18 15:27
 * 物界
 * {www.wj.com
 * @author Ly
 */
public class PermissionHelper {

    // 检查分享权限
    public void checkSharePermission(Context context, OnRequestPermissionsCallBack call) {
        PermissionCompat.create(context)
                .permissions(AppConstant.SHARE_PERMISSIONS)
                .retry(true)
                .callBack(call)
                .build()
                .request();
    }

    //检查 相机权限
    public void checkCameraPermission(Context context, OnRequestPermissionsCallBack call) {
        PermissionCompat.create(context)
                .permissions(AppConstant.CAMERA_PERMISSIONS)
                .retry(true)
                .callBack(call)
                .build()
                .request();
    }

    // 申请启动 权限
    public void requestSplashPermission(Context context, OnRequestPermissionsCallBack call) {
        PermissionCompat.create(context)
                .permissions(AppConstant.SPLASH_PERMISSIONS)
                .retry(true)
                .callBack(call)
                .build()
                .request();
    }
}

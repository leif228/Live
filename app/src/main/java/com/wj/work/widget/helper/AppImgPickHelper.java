package com.wj.work.widget.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.wj.work.R;
import com.wj.work.constant.AppConstant;
import com.wj.work.utils.UriUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * PictureSelectorHelper
 * 2019/11/29 9:56
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class AppImgPickHelper {

//    public static List<String> getImgPathArrayListFromIntentByPictureSelector(Intent data) {
//        List<LocalMedia> tmp = PictureSelector.obtainMultipleResult(data);
//        //图片单选和多选数据都是以ArrayList的字符串数组返回的。
//        ArrayList<String> result = new ArrayList<>();
//        for (LocalMedia media : tmp) {
//            result.add(media.getPath());
//        }
//        return result;
//    }

    // Matisse 获取 List 结果
    public static List<String> getImgPathArrayListFromIntent(Context context, Intent data) {
        //图片路径 同样视频地址也是这个 根据requestCode
        List<String> target = Matisse.obtainPathResult(data);
        ArrayList<String> result = new ArrayList<>();
        if (target != null && target.size() > 0) {
            for (String item : target) {
                result.add(UriUtils.getRealPathFromContentPath(context,item));
            }
        }
        return result;
    }

    // Matisse 启动
    public static void startPickPhoto(Activity activity, int num, int requstCode) {
        Matisse.from(activity)
                //选择视频和图片
                //选择图片
                .choose(MimeType.ofImage())
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, AppConstant.FILE_PROVIDER))
                //有序选择图片 123456...
                .countable(true)
                //最大选择数量为9
                .maxSelectable(num)
                .spanCount(4)
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(0.8f)
                //黑色主题
                .theme(R.style.Matisse_Dracula)
                //Glide加载方式
                .imageEngine(new GlideEngine())
                //请求码
                .forResult(requstCode);
    }

    // 单独启动相机
    public static void startCameraForPic(Activity activity, int requstCode) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .loadImageEngine(com.wj.work.widget.helper.GlideEngine.createGlideEngine())
                .forResult(requstCode);
    }
}

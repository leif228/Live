package com.wj.work.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author Ly
 * @Description UriUtils
 * @date 2020/1/2 16:25
 * @copyright 物界
 * @website {www.wj.com
 */
public class UriUtils {

    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // 针对华为等手机返回的 conent 开头的uri 地址   转为手机内存卡真实地址
    public static String getRealPathFromContentPath(Context context, String path) {
        if (path.startsWith("content")||path.startsWith("/content")){
            Uri uri = Uri.parse(path);
            return getRealPathFromUri(context,uri);
        }else{
            return path;
        }
    }

}

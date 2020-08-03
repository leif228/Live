package com.wj.work.ui.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * GlideHelper
 * 2020/4/2 11:00
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class GlideHelper {

    public static DrawableTransitionOptions getCrossFadeOptions(){
        return new DrawableTransitionOptions().crossFade();
    }

    // 圆形图片
    public static void setCircleCropImg(Context context,ImageView target, String path){
        Glide.with(context)
                .load(path)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(RequestOptions
                        .circleCropTransform()
                )
                .into(target);
    }

    // 圆形图片
    public static void setCircleCropImg(Context context,ImageView target, int src){
        Glide.with(context)
                .load(src)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(RequestOptions
                        .circleCropTransform()
                )
                .into(target);
    }

}

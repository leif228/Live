package com.wj.work.utils;

import android.content.Context;
import android.widget.TextView;

import com.wj.work.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * CountDownUtils
 * 2020/4/1 14:52
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CountDownUtils {

    /**
     * 发送验证码 倒计时
     * @param countTime  倒计时总时间
     * @param targetTv  targetTv
     */
    public Disposable countDown(Context context,final int countTime, final TextView targetTv) {
       return Flowable
               .interval(0, 1, TimeUnit.SECONDS)
               .take(countTime + 1)
               .map(aLong -> countTime - aLong)
               .observeOn(AndroidSchedulers.mainThread())
               .doOnSubscribe(subscription -> {
                   if (targetTv.isEnabled()) {
                       targetTv.setEnabled(false);
                   }
                   targetTv.setTextColor(context.getResources().getColor(R.color.app_orange));
               })
               .subscribe(aLong -> targetTv.setText(aLong+"s"), throwable -> {
               }, () -> {
                   if (!targetTv.isEnabled()) {
                       targetTv.setEnabled(true);
                   }
                   targetTv.setText("发送验证码");
                   targetTv.setTextColor(context.getResources().getColor(R.color.app_orange));
               });
    }
}

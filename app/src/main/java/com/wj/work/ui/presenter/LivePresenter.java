package com.wj.work.ui.presenter;

import com.wj.work.base.mvp.BasePresenter;
import com.wj.work.http.api.HttpManager;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.subscriber.AppSubscriber;
import com.wj.work.ui.contract.LiveView;
import com.lib.kit.utils.ToastUtils;


/**
 * LivePresenter
 * 2020/5/8 15:50
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LivePresenter extends BasePresenter<LiveView> {
    public LivePresenter(LiveView view) {
        super(view);
    }

    // 开启 直播录像
    public void reqStartRecording(long id) {
        mDisposable.add(HttpManager
                .serviceApi()
                .reqStartRecord(String.valueOf(id), getToken())
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<Object>(mView) {
                    @Override
                    protected void _onNext(Object list) {
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtils.showLong(msg);
                    }
                }));
    }
}

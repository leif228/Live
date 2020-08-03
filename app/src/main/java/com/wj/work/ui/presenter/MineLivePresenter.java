package com.wj.work.ui.presenter;

import com.wj.work.base.mvp.BasePresenter;
import com.wj.work.http.api.HttpManager;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.subscriber.AppSubscriber;
import com.wj.work.http.support.PageBean;
import com.wj.work.ui.contract.MineLiveView;
import com.wj.work.widget.entity.LiveRecordEntity;
import com.lib.kit.utils.ToastUtils;

/**
 * MineLivePresenter
 * 2020/5/9 16:17
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class MineLivePresenter extends BasePresenter<MineLiveView> {
    public MineLivePresenter(MineLiveView view) {
        super(view);
    }

    public void reqLiveRecordList(int page) {
        mDisposable.add(HttpManager
                .serviceApi()
                .reqLiveRecordList(page, 10, getToken())
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<PageBean<LiveRecordEntity>>(mView) {
                    @Override
                    protected void _onNext(PageBean<LiveRecordEntity> response) {
                        mView.reqLiveRecordSuccess(response);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtils.showLong(msg);
                        mView.reqLiveRecordFailed();
                    }
                }));
    }
}

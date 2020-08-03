package com.wj.work.http.subscriber;


import com.wj.work.base.mvp.BaseView;
import com.wj.work.http.subscriber.BaseResourceSubscriber;

public abstract class AppSubscriber<T> extends BaseResourceSubscriber<T> {
    private BaseView mView;

    protected AppSubscriber(BaseView mView) {
        this.mView = mView;
    }

    @Override
    protected boolean isActivityDestroyed() {
        return mView.isDestroyed();
    }


    @Override
    protected void _onError(Throwable t, String msg) {
        _onError(msg);
    }

    protected abstract void _onError(String msg);
}

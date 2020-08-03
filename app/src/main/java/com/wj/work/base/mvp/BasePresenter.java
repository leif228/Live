package com.wj.work.base.mvp;

import com.wj.work.base.mvp.BaseView;
import com.wj.work.db.SpManager;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<V extends BaseView>{
    protected V mView;
    protected CompositeDisposable mDisposable;

    public BasePresenter(V view) {
        this.mView = view;
        mDisposable = new CompositeDisposable();
    }

    public void unsubscribe() {
//        mView = null;
        mDisposable.clear();
    }

    protected String getToken(){
        return SpManager.getInstance().getLoginSp().getToken();
    }

    protected boolean isDestroyed() {
        return mView.isDestroyed();
    }
}

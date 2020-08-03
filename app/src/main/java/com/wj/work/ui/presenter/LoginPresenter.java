package com.wj.work.ui.presenter;

import com.wj.work.R;
import com.wj.work.base.mvp.BasePresenter;
import com.wj.work.http.api.HttpManager;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.subscriber.AppSubscriber;
import com.wj.work.ui.contract.LoginView;
import com.wj.work.widget.entity.LoginEntity;
import com.lib.kit.utils.LL;
import com.lib.kit.utils.ToastUtils;

public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginPresenter(LoginView mView) {
        super(mView);
    }

    public void login(String tel, String code) {
        mView.showLoadingDialog();
        mDisposable.add(HttpManager.accountApi().login(tel,code)
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<LoginEntity>(mView) {
                    @Override
                    protected void _onNext(LoginEntity loginEntity) {
                        mView.hideLoadingDialog();
                        mView.loginSuccess(loginEntity);
                        LL.V("登录成功！");
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(msg);
                    }
                }));
    }

    public void reqSendCode(String tel) {
        mView.showLoadingDialog();
        mDisposable.add(HttpManager.accountApi().getLoginCode(tel)
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<Object>(mView) {
                    @Override
                    protected void _onNext(Object response) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(R.string.code_send_success);
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(msg);
                    }
                }));
    }
}

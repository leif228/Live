package com.wj.work.ui.presenter;

import android.content.Context;

import com.wj.work.base.mvp.BasePresenter;
import com.wj.work.db.SpManager;
import com.wj.work.http.api.HttpManager;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.subscriber.AppSubscriber;
import com.wj.work.ui.contract.RealNameView;
import com.wj.work.widget.entity.RealNameInfo;
import com.lib.kit.utils.ToastUtils;

import java.io.File;

/**
 * RealNamePresenter
 * 2020/5/7 11:15
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class RealNamePresenter extends BasePresenter<RealNameView> {

    public RealNamePresenter(RealNameView view) {
        super(view);
    }

    public void reqRealNameAuthentication(String realName, String cardNum, String portraitImg, String nationalEmblemImg) {
        mView.showLoadingDialog();
        mDisposable.add(HttpManager.accountApi().userRealNameAuthentication(cardNum, nationalEmblemImg, portraitImg, realName
                , SpManager.getInstance().getLoginSp().getToken()
        )
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<Object>(mView) {
                    @Override
                    protected void _onNext(Object response) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong("认证提交成功,请等待审核!");
                        //0.未实名，1.审核中，2.驳回，3.成功
                        SpManager.getInstance().getLoginSp().updateRealNameType(1);
                        mView.authenticationSuccess();
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(msg);
                    }
                }));
    }

    public void upLoadImg(Context context, String localPath) {
        File file = new File(localPath);
        if (!file.exists()) return;
        mView.showLoadingDialog();
        mDisposable.add(HttpManager.compressUploadFile(context, localPath)
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<String>(mView) {
                    @Override
                    protected void _onNext(String httpPath) {
                        mView.hideLoadingDialog();
                        mView.upLoadImgSuccess(localPath, httpPath);
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        mView.upLoadImgFailed(localPath);
                    }
                }));
    }

    public void reqRealNameInfo() {
        mView.showLoadingDialog();
        mDisposable.add(HttpManager.accountApi()
                .reqCertificationInfo(SpManager.getInstance().getLoginSp().getToken())
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<RealNameInfo>(mView) {
                    @Override
                    protected void _onNext(RealNameInfo info) {
                        mView.hideLoadingDialog();
                        mView.getRealNameInfoSuccess(info);
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(msg);
                        mView.getRealNameInfoFailed(msg);
                    }
                }));
    }
}

package com.wj.work.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.wj.work.base.mvp.BasePresenter;
import com.wj.work.http.api.HttpManager;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.subscriber.AppSubscriber;
import com.wj.work.ui.contract.CreateLiveView;
import com.wj.work.widget.entity.CreateLiveEntity;
import com.wj.work.widget.params.CreateLiveReqParams;
import com.google.common.collect.Maps;
import com.lib.kit.utils.ToastUtils;

import java.io.File;
import java.util.Map;

/**
 * CreateLivePresenter
 * 2020/5/7 15:45
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CreateLivePresenter extends BasePresenter<CreateLiveView> {

    public CreateLivePresenter(CreateLiveView view) {
        super(view);
    }

    // 获取推流地址
    public void reqPushAddress(long id) {
        mDisposable.add(HttpManager.serviceApi()
                .getPushFlowAddress(String.valueOf(id), getToken())
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<String>(mView) {
                    @Override
                    protected void _onNext(String response) {
                        mView.hideLoadingDialog();
                        mView.reqPushAddressSuccess(id,response);
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(msg);
                    }
                }));
    }

    // 创建房间
    public void reqCreateLive(CreateLiveReqParams params) {

        mView.showLoadingDialog();

        Map<String, Object> map = Maps.newHashMap();
        map.put("liveCover", params.getLiveCover());
        map.put("liveTitle", params.getLiveTitle());
        map.put("pushFollowUsersType", params.getPushFollowUsersType());
        map.put("sharpnessType", params.getSharpnessType());
        map.put("typeId", params.getTypeId());
        map.put("typeSub", params.getTypeSub());

        if (!TextUtils.isEmpty(params.getRecommendGoods())) {
            map.put("recommendGoods", params.getRecommendGoods());
        }

        if (!TextUtils.isEmpty(params.getLatitude())) {
            map.put("latitude", params.getLatitude());
        }

        if (!TextUtils.isEmpty(params.getLongitude())) {
            map.put("longitude", params.getLongitude());
        }

        if (!TextUtils.isEmpty(params.getPreBroadcastTime())) {
            map.put("preBroadcastTime", params.getPreBroadcastTime());
        }

        mDisposable.add(HttpManager.serviceApi()
                .reqCreateLive(map, getToken())
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<CreateLiveEntity>(mView) {
                    @Override
                    protected void _onNext(CreateLiveEntity response) {
                        mView.createLiveSuccess(response);
                    }

                    @Override
                    protected void _onError(String msg) {
                        mView.hideLoadingDialog();
                        ToastUtils.showLong(msg);
                    }
                }));
    }

    // 上传图片
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
}

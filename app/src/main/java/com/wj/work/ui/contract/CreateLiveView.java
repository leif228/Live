package com.wj.work.ui.contract;

import com.wj.work.base.mvp.BaseView;
import com.wj.work.widget.entity.CreateLiveEntity;

/**
 * CreateLiveView
 * 2020/5/7 15:44
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface CreateLiveView extends BaseView {
    void createLiveSuccess(CreateLiveEntity response);

    void reqPushAddressSuccess(long id, String pushAddress);

    void upLoadImgSuccess(String localPath, String httpPath);

    void upLoadImgFailed(String localPath);
}

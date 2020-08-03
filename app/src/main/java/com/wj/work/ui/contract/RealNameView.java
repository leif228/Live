package com.wj.work.ui.contract;

import com.wj.work.base.mvp.BaseView;
import com.wj.work.widget.entity.RealNameInfo;

/**
 * RealNameView
 * 2020/5/7 11:15
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface RealNameView extends BaseView {
    void authenticationSuccess();

    void upLoadImgSuccess(String path, String httpPath);

    void upLoadImgFailed(String path);

    void getRealNameInfoFailed(String msg);

    void getRealNameInfoSuccess(RealNameInfo info);
}

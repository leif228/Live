package com.wj.work.ui.contract;

import com.wj.work.base.mvp.BaseView;
import com.wj.work.widget.entity.LoginEntity;

/**
 * LoginView
 * 2020/4/26 17:20
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface WebViewI extends BaseView {
    void loginSuccess(LoginEntity loginEntity);
}

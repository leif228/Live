package com.wj.work.base.mvp;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * BaseAbsView
 * 2019/11/8 11:20
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface BaseView {

    void showLoadingDialog();

    void showLoadingDialog(String hint);

    void showLoadingDialog(boolean cancelAble);

    void showLoadingDialog(String hint, boolean cancelAble);

    void hideLoadingDialog();

    boolean isDestroyed();

    /**
     * @title bindAutoDispose
     * @author tanghu
     * @date 2020/4/3 11:05
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();
}

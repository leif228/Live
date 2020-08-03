package com.wj.work.ui.contract;

import com.wj.work.base.mvp.BaseView;
import com.wj.work.http.support.PageBean;
import com.wj.work.widget.entity.LiveRecordEntity;

/**
 * MineLiveView
 * 2020/5/9 16:17
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface MineLiveView extends BaseView {
    void reqLiveRecordSuccess(PageBean<LiveRecordEntity> response);

    void reqLiveRecordFailed();
}

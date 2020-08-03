package com.wj.work.ui.contract;

import com.wj.work.base.mvp.BaseView;
import com.wj.work.widget.entity.OrderEntity;

import java.util.List;

/**
 * CustomerOrderView
 * 2020/4/30 14:28
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public interface CustomerOrderView extends BaseView {
    void reqOrderListSuccess(int type, List<OrderEntity> list);
}

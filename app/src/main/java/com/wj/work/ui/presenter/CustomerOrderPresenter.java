package com.wj.work.ui.presenter;

import com.wj.work.base.mvp.BasePresenter;
import com.wj.work.http.api.HttpManager;
import com.wj.work.http.rx.RxHelper;
import com.wj.work.http.subscriber.AppSubscriber;
import com.wj.work.ui.contract.CustomerOrderView;
import com.wj.work.widget.entity.OrderEntity;
import com.lib.kit.utils.ToastUtils;

import java.util.List;

/**
 * CustomerOrderPresenter
 * 2020/4/30 14:27
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CustomerOrderPresenter extends BasePresenter<CustomerOrderView> {

    public CustomerOrderPresenter(CustomerOrderView view) {
        super(view);
    }

    public void reqOderListByType(int type){
        mView.showLoadingDialog();
        mDisposable.add(HttpManager.serviceApi().reqOderListByType("tel", "md5Pwd")
                .compose(RxHelper.handleResult())
                .as(mView.bindAutoDispose())
                .subscribeWith(new AppSubscriber<List<OrderEntity>>(mView) {
                    @Override
                    protected void _onNext(List<OrderEntity> list) {
                        mView.hideLoadingDialog();
                        mView.reqOrderListSuccess(type,list);
                    }

                    @Override
                    protected void _onError(String msg) {
                        ToastUtils.showLong(msg);
                        mView.reqOrderListSuccess(type,null);
                        mView.hideLoadingDialog();
                    }
                }));
    }
}

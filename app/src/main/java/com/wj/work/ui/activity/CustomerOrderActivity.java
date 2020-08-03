package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.ui.contract.CustomerOrderView;
import com.wj.work.ui.presenter.CustomerOrderPresenter;
import com.wj.work.widget.adapter.OrderAdapter;
import com.wj.work.widget.entity.OrderEntity;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.wj.work.widget.view.AppEmptyView;
import com.google.common.collect.Lists;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * CustomerOrderActivity
 * 2020/4/30 14:27
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CustomerOrderActivity extends BaseMvpActivity<CustomerOrderPresenter> implements CustomerOrderView {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.tabSegment)
    QMUITabSegment tabSegment;

    OrderAdapter mAdapter;
    AppEmptyView mEmptyView;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, CustomerOrderActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_customer_order;
    }

    @Override
    protected void initView() {
        mPresenter = new CustomerOrderPresenter(this);
        initTabSegment();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new OrderAdapter(null,mActivity);
        mRecyclerView.setAdapter(mAdapter);
        mEmptyView = new AppEmptyView(mActivity);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        // 设置加载更多监听事件
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mAdapter.getData().size() > 20) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appendData();
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    }, 500);
                } else {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appendData();
                            mAdapter.getLoadMoreModule().loadMoreComplete();
                        }
                    }, 500);
                }
            }
        });
        // 设置 下拉刷新
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSmartRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.getData().clear();
                        appendData();
                        mAdapter.getLoadMoreModule().loadMoreComplete();
                        mSmartRefreshLayout.finishRefresh(500);
                    }
                }, 600);
            }
        });

        mPresenter.reqOderListByType(1);
    }

    private void appendData() {
        List<OrderEntity> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(new OrderEntity());
        }
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void initTabSegment() {

        String[] tabArray = new String[]{
                getResources().getString(R.string.All),
                getResources().getString(R.string.wait_for_payment),
                getResources().getString(R.string.Paid),
                getResources().getString(R.string.wait_for_receiving),
                getResources().getString(R.string.completed)
        };

        QMUITabBuilder builder1 = tabSegment.tabBuilder();
        builder1.setColorAttr(R.attr.qmui_config_color_gray_6, R.attr.qmui_config_color_blue)
                .setColor(getResources().getColor(R.color.app_black_l), getResources().getColor(R.color.app_orange))
                .setDynamicChangeIconColor(true)
                .setNormalColor(getResources().getColor(R.color.app_black_l));
        for (String tabString : tabArray) {
            tabSegment.addTab(builder1.setText(tabString).setTypeface(Typeface.DEFAULT, Typeface.DEFAULT).build(mActivity));
        }
        tabSegment.selectTab(0);
        tabSegment.notifyDataChanged();

        tabSegment.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
            }

            @Override
            public void onTabUnselected(int index) {
            }

            @Override
            public void onTabReselected(int index) {
            }

            @Override
            public void onDoubleTap(int index) {
            }
        });
    }

    @Override
    public void reqOrderListSuccess(int type, List<OrderEntity> list) {
        appendData();
    }
}

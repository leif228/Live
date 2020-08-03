package com.wj.work.ui.activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.BaseActivity;
import com.wj.work.widget.adapter.ProductSelectAdapter;
import com.wj.work.widget.entity.Product;
import com.wj.work.widget.view.AppEmptyView;
import com.lib.kit.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectProductActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.commit)
    TextView mCommitView;

    ProductSelectAdapter mAdapter;
    private AppEmptyView mEmptyView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_product;
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator != null) ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        mAdapter = new ProductSelectAdapter(mActivity, null);
        mRecyclerView.setAdapter(mAdapter);
        mEmptyView = new AppEmptyView(mActivity);
        mEmptyView.showLoading(true);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        mAdapter.setAccepter(num -> {
            mCommitView.setText(String.format(getResources().getString(R.string.commit_with_num), num));
            mCommitView.setEnabled(num > 0);
        });
        // 设置加载更多监听事件
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mAdapter.getData().size() > 15) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appendData(DataTemp.getProducts());
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    }, 500);
                } else {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appendData(DataTemp.getProducts());
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
                        mAdapter.getLoadMoreModule().loadMoreComplete();
                        resetData();
                        mSmartRefreshLayout.finishRefresh(500);
                    }
                }, 600);
            }
        });

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetData();
            }
        }, 1000);
        mCommitView.setText(String.format(getResources().getString(R.string.commit_with_num), 0));
    }

    private void resetData() {
        List<Product> list = DataTemp.getProducts();
        setData(list);
    }

    private void appendData(List<Product> list) {
        if (list == null || list.isEmpty()) return;
        mAdapter.appendAll(list);
    }

    private void setData(List<Product> list) {
        if (list == null || list.isEmpty()) return;
        mAdapter.clearAndAddAll(list);
    }

    @OnClick(R.id.commit)
    public void onClickCommit() {
    }

}

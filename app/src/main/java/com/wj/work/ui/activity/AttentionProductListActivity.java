package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.BaseActivity;
import com.wj.work.ui.activity.ProductDetailActivity;
import com.wj.work.widget.adapter.AttentionProductListAdapter;
import com.wj.work.widget.entity.Product;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.wj.work.widget.view.AppEmptyView;
import com.lib.kit.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * AttentionListActivity
 * 关注的商品
 * 2020/4/7 9:49
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class AttentionProductListActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    AttentionProductListAdapter mAdapter;
    private AppEmptyView mEmptyView;


    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity, AttentionProductListActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attention_product;
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected void initView() {

        mAdapter = new AttentionProductListAdapter(mActivity, null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mEmptyView = new AppEmptyView(mActivity);
        mEmptyView.showLoading(true);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
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
                            appendData(DataTemp.getAttentionProductList());
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    }, 500);
                } else {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appendData(DataTemp.getAttentionProductList());
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
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            skipActivity(ProductDetailActivity.class );
        });

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                mAdapter.remove(position);
            }
        });

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetData();
            }
        }, 1000);
    }

    private void setData(List<Product> list) {
        if (list == null || list.isEmpty()) return;
        mAdapter.getData().clear();
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void appendData(List<Product> list) {
        if (list == null || list.isEmpty()) return;
        mAdapter.getData().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void resetData() {
        List<Product> list = DataTemp.getAttentionProductList();
        setData(list);
    }
}

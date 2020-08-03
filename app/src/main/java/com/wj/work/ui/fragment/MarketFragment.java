package com.wj.work.ui.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.BaseFragment;
import com.wj.work.ui.helper.DataHelper;
import com.wj.work.widget.adapter.MarketAdapter;
import com.wj.work.widget.adapter.MarketFlowAdapter;
import com.wj.work.widget.entity.PrimaryTab;
import com.wj.work.widget.entity.Product;
import com.wj.work.widget.entity.SifterEntity;
import com.wj.work.widget.entity.SubTab;
import com.wj.work.widget.view.AppEmptyView;
import com.wj.work.widget.view.MarketConditionView;
import com.wj.work.widget.view.TLinearLayoutCompat;
import com.lib.kit.utils.LL;
import com.lib.kit.utils.StatusBarUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.view.flowlayout.TagFlowLayout;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class MarketFragment extends BaseFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.tabsegment)
    QMUITabSegment mTabSegment1;
    @BindView(R.id.sub_tabsegment)
    QMUITabSegment mTabSegment2;
    @BindView(R.id.condition)
    MarketConditionView mConditionView;
    @BindView(R.id.panel)
    View mSifterPanel;
    @BindView(R.id.tlinear)
    TLinearLayoutCompat mTLinearLayout;
    @BindView(R.id.flowlayout)
    TagFlowLayout mFlowLayout;
    private MarketAdapter mAdapter;
    private SifterEntity mSifterEntity; // 当前的筛选条件
    private AppEmptyView mEmptyView; // 空白页面

    @Override
    public int getContentViewId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MarketAdapter(mActivity, null);
        mRecyclerView.setAdapter(mAdapter);
        mEmptyView =new AppEmptyView(mActivity);
        mEmptyView.showLoading(true);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        // 设置加载更多监听事件
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mAdapter.getData().size()>20){
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            appendData(DataTemp.getProducts());
                            mAdapter.getLoadMoreModule().loadMoreEnd();
                        }
                    }, 500);
                }else{
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
        resetData();
        initTabSegment(DataTemp.getTabList());
        mConditionView.setData(DataTemp.getMarketConditionList());
        mConditionView.setTabClickListener((index, tab) -> {
            if (index == 3) { // 筛选
                clickSifter();
            } else {
                DataHelper.resetTabAfterTap(index, tab);
                mConditionView.notifyDataSetchanged();
            }
        });
        mTLinearLayout.setListener(MarketFragment.this::hideSifterPanel);
        initFlowLayout();
    }

    private void initFlowLayout() {
        MarketFlowAdapter adapter = new MarketFlowAdapter(mActivity, DataTemp.getSifterTagList());
        mFlowLayout.setAdapter(adapter);
    }

    private boolean isSifterPanelShown = false; // 筛选面板是否打开

    // 点击筛选
    private void clickSifter() {
        if (!isSifterPanelShown) {
            showSifterPanel();
        } else {
            hideSifterPanel();
        }
    }

    // 打开筛选面板
    private void showSifterPanel() {
        mSifterPanel.setVisibility(View.VISIBLE);
        mSifterPanel.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.right_in));
        isSifterPanelShown = true;
        mTLinearLayout.canInterceptTouch(true);
    }

    // 隐藏筛选面板
    private void hideSifterPanel() {
        mSifterPanel.setVisibility(View.GONE);
        mSifterPanel.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.out_to_right));
        isSifterPanelShown = false;
        mTLinearLayout.canInterceptTouch(false);
    }

    private List<PrimaryTab> tabList; // 分类条件

    private void initTabSegment(List<PrimaryTab> list) {
        this.tabList = list;
        if (list.size() < 1) return;
        QMUITabBuilder builder1 = mTabSegment1.tabBuilder();
        setTabBuilderParams(builder1);

        for (PrimaryTab tab : list) {
            mTabSegment1.addTab(builder1.setText(tab.getText()).setTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD).build(mActivity));
        }
        mTabSegment1.selectTab(0);
        mTabSegment1.setItemSpaceInScrollMode(QMUIDisplayHelper.dp2px(mActivity, 20));
        mTabSegment1.notifyDataChanged();

        QMUITabBuilder builder2 = mTabSegment2.tabBuilder();
        setTabBuilderParams(builder2);
        for (SubTab tab : list.get(0).getList()) {
            mTabSegment2.addTab(builder2.setText(tab.getText()).build(mActivity));
        }
        mTabSegment2.selectTab(0);
        mTabSegment2.setItemSpaceInScrollMode(QMUIDisplayHelper.dp2px(mActivity, 26));
        mTabSegment2.notifyDataChanged();
        mTabSegment1.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                resetSubSegment2(index);
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

        mTabSegment2.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                LL.V("搜索 == 一级 index=" + tempPrimeIndex + "  二级index=" + index);
                httpReqList(tabList.get(tempPrimeIndex),tabList.get(tempPrimeIndex).getList().get(index));
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

    private void httpReqList(PrimaryTab primaryTab, SubTab subTab){
        mAdapter.clear();
        mEmptyView.showLoading(true);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEmptyView.showEmpty(true);
                resetData();
            }
        },1200);
    }

    private void setTabBuilderParams(QMUITabBuilder tabBuilder) {
        tabBuilder.setColorAttr(R.attr.qmui_config_color_gray_6, R.attr.qmui_config_color_blue)
                .setColor(getResources().getColor(R.color.app_black_l), getResources().getColor(R.color.app_orange))
                .setDynamicChangeIconColor(false)
                .setNormalColor(getResources().getColor(R.color.app_black_l))
        ;
    }

    private int tempPrimeIndex = 0;

    private void resetSubSegment2(int index) {
        if (tabList == null || tabList.size() <= index) return;
        tempPrimeIndex = index;
        mTabSegment2.selectTab(0);
        mTabSegment2.reset();
        QMUITabBuilder tabBuilder = mTabSegment2.tabBuilder();
        setTabBuilderParams(tabBuilder);
        for (SubTab tab : tabList.get(index).getList()) {
            mTabSegment2.addTab(tabBuilder.setText(tab.getText()).build(mActivity));
        }
        mTabSegment2.selectTab(0);
        mTabSegment2.notifyDataChanged();
    }

    private void resetData() {
        List<Product> list = DataTemp.getProducts();
        setData(list);
    }

    @Override
    protected void setFakeStatusParams(View status) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtils.setFakeStatusParams(status, getResources().getColor(R.color.white), 255);
        } else {
            StatusBarUtils.setFakeStatusParams(status, getResources().getColor(R.color.black), 255);
        }
    }

    private void setData(List<Product> list) {
        if (list == null || list.isEmpty()) return;
        mAdapter.clearAndAddAll(list);
    }

    private void appendData(List<Product> list) {
        if (list == null || list.isEmpty()) return;
        mAdapter.appendAll(list);
    }

    @OnClick(R.id.reset)
    protected void onclick(View v) {
        mAdapter.clear();
    }
}

package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.http.support.PageBean;
import com.wj.work.ui.activity.MinePrevueActivity;
import com.wj.work.ui.contract.MineLiveView;
import com.wj.work.ui.presenter.MineLivePresenter;
import com.wj.work.widget.adapter.LiveListAdapter;
import com.wj.work.widget.entity.LiveRecordEntity;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.wj.work.widget.helper.DialogResolver;
import com.wj.work.widget.view.AppEmptyView;
import com.lib.kit.utils.StatusBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

public class MineLiveActivity extends BaseMvpActivity<MineLivePresenter> implements MineLiveView {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;

    private LiveListAdapter mAdapter;
    private AppEmptyView mEmptyView; // 空白页面

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, MineLiveActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_live;
    }

    @Override
    protected void initView() {
        mPresenter=new MineLivePresenter(this);
        mAdapter = new LiveListAdapter(mActivity, null);

        mRecyclerView.setAdapter(mAdapter);
        mEmptyView = new AppEmptyView(mActivity);
        mEmptyView.showLoading(true);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        mAdapter.setAnimationEnable(true);
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        // 设置加载更多监听事件
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            if (isFinishing()) return;
            mPresenter.reqLiveRecordList(pageCurrent+1);
        });
        // 设置 下拉刷新
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.reqLiveRecordList(1));

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.lmbForward:
                    reqForward(mAdapter.getData().get(position));
                    break;
                case R.id.lmbDelete:
                    reqDelete(mAdapter.getData().get(position), position);
                    break;
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            itemClick(mAdapter.getData().get(position));
        });

        mPresenter.reqLiveRecordList(1);
    }

    private void itemClick(LiveRecordEntity mineLiveEntity) {
        if (mineLiveEntity.getItemType()== LiveRecordEntity.STATUS_PAUSE){
            reqContinueLive();
        }
        if (mineLiveEntity.getItemType()== LiveRecordEntity.STATUS_PREVIEW){
            MinePrevueActivity.actionStart(mActivity);
        }
        if (mineLiveEntity.getItemType()== LiveRecordEntity.STATUS_FINISHED){
        }
    }

    private void reqContinueLive(){
        DialogResolver.createAlertDialog(mActivity,
                getResources().getString(R.string.tips),
                getResources().getString(R.string.tip_continue_live),
                (dialog, which) -> {

                }
        ).show();
    }

    private void reqForward(LiveRecordEntity mineLiveEntity) {
        DialogResolver.createForwardDialog(mActivity, (dialog, itemView) -> {
            int index= (int) itemView.getTag();
            switch (index){
                case 0:  // 小程序
                    break;
                case 1:  // 朋友圈
                    break;
                case 2:  // 微信好友
                    break;
            }
            dialog.dismiss();
        }).show();
    }

    // 删除
    private void reqDelete(LiveRecordEntity mineLiveEntity, int position) {
        DialogResolver.createAlertDialog(mActivity,
                getResources().getString(R.string.tips),
                getDeleteMessage(mineLiveEntity),
                (dialog, which) -> {
                    mAdapter.getData().remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
        ).show();
    }

    private String getDeleteMessage(LiveRecordEntity mineLiveEntity) {
        String result="";
        if (mineLiveEntity.getItemType()== LiveRecordEntity.STATUS_PAUSE){
            result=getResources().getString(R.string.tip_live_delete_pause);
        }
        if (mineLiveEntity.getItemType()== LiveRecordEntity.STATUS_PREVIEW){
            result=getResources().getString(R.string.tip_live_delete_preview);
        }
        if (mineLiveEntity.getItemType()== LiveRecordEntity.STATUS_FINISHED){
            result=getResources().getString(R.string.tip_live_delete_finished);
        }
        return result;
    }

    private int pageCurrent;

    @Override
    public void reqLiveRecordSuccess(PageBean<LiveRecordEntity> response) {
        int page = response.getPageNum();
        if (page == 1) {
            pageCurrent = page;
            mAdapter.setNewInstance(response.getList());
            mSmartRefreshLayout.finishRefresh(500);
        } else {
            if (response.getList() != null &&
                    response.getList().size() > 0 && response.getPageNum() == pageCurrent + 1) {
                mAdapter.getData().addAll(response.getList());
                mAdapter.notifyDataSetChanged();
                pageCurrent = response.getPageNum();
            }
            if (page < response.getPages() - 1) {
                mAdapter.getLoadMoreModule().loadMoreComplete();
            } else {
                mAdapter.getLoadMoreModule().loadMoreEnd();
            }
        }
    }

    @Override
    public void reqLiveRecordFailed() {
    }
}

package com.wj.work.widget.helper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.http.support.PageBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * PageHelper
 * 2020/5/9 16:46
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class PageHelper<T> {

    private int pageCurrent;
    private SmartRefreshLayout mSmartRefreshLayout;
    private BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    public PageHelper(SmartRefreshLayout mSmartRefreshLayout, BaseQuickAdapter<T, BaseViewHolder> mAdapter) {
        this.mSmartRefreshLayout = mSmartRefreshLayout;
        this.mAdapter = mAdapter;
    }

    public void makeData(PageBean<T> response){
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
            if (page < response.getTotal() - 1) {
                mAdapter.getLoadMoreModule().loadMoreComplete();
            } else {
                mAdapter.getLoadMoreModule().loadMoreEnd();
            }
        }
    }
}

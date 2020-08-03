package com.wj.work.widget.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.base.BaseAdapter;

import java.util.List;

/**
 * ClientAdapter
 * 2020/4/30 17:58
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ClientAdapter extends BaseAdapter<Object> {

    public ClientAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Object o) {

    }
}

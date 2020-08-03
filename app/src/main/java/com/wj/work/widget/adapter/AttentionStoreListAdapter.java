package com.wj.work.widget.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.R;
import com.wj.work.base.BaseAdapter;
import com.wj.work.ui.helper.GlideHelper;
import com.wj.work.widget.entity.StoreEntity;

import java.util.List;

/**
 * AttentionStoreListAdapter
 * 2019/10/28 11:36
 * 物界
 * {www.wj.com
 * * @author Ly
 */
public class AttentionStoreListAdapter extends BaseAdapter<StoreEntity> implements LoadMoreModule {

    private Context context;

    public AttentionStoreListAdapter(Context context, @Nullable List<StoreEntity> data) {
        super(R.layout.item_attention_store, data);
        this.context = context;
        addChildClickViewIds(R.id.cancel);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, StoreEntity item) {
        helper.setText(R.id.tvName, item.getName());
        helper.setText(R.id.tv_fence_num,String.valueOf(item.getFenceNum()));
        Glide.with(context)
                .load(item.getAvatar())
                .transition(GlideHelper.getCrossFadeOptions())
                .into((ImageView) helper.getView(R.id.iv_avatar));
    }

}

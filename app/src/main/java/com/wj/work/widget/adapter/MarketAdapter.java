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
import com.wj.work.widget.entity.Product;

import java.util.List;

/**
 * MarketAdapter
 * 2019/10/28 11:36
 * 物界
 * {www.wj.com
 * * @author Ly
 */
public class MarketAdapter extends BaseAdapter<Product> implements LoadMoreModule {

    private Context context;

    public MarketAdapter(Context context,@Nullable List<Product> data) {
        super(R.layout.item_market, data);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_describe,item.getDescribe());
        helper.setText(R.id.tv_price,context.getResources().getString(R.string.price,item.getPrice()));

        Glide.with(context)
                .load(item.getImg())
                .transition(GlideHelper.getCrossFadeOptions())
                .into((ImageView) helper.getView(R.id.iv_cargo_pic));
    }
}

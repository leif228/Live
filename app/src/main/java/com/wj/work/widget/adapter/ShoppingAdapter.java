package com.wj.work.widget.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.R;
import com.wj.work.base.BaseAdapter;
import com.wj.work.ui.helper.GlideHelper;
import com.wj.work.widget.entity.Product;

import java.util.List;

/**
 * ShoppingAdapter
 * 2020/5/14 10:38
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ShoppingAdapter extends BaseAdapter<Product> {

    public ShoppingAdapter(@Nullable List<Product> data) {
        super(R.layout.item_shopping, data);
//        if (data!=null){
//            getData().addAll(data);
//            notifyDataSetChanged();
//        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Product item) {
        holder.setText(R.id.tvName, item.getDescribe());
        holder.setText(R.id.tvPrice, getContext().getResources().getString(R.string.price, item.getPrice()));
        Glide.with(getContext())
                .load(item.getImg())
                .transition(GlideHelper.getCrossFadeOptions())
                .into((ImageView) holder.getView(R.id.ivAvatar));
    }
}

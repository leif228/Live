package com.wj.work.widget.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.R;
import com.wj.work.base.BaseAdapter;
import com.wj.work.widget.entity.OrderEntity;

import java.util.List;

/**
 * OrderAdapter
 * 2020/4/30 15:25
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class OrderAdapter extends BaseAdapter<OrderEntity> implements LoadMoreModule {

    private Context context;

    public OrderAdapter(@Nullable List<OrderEntity> data, Context context) {
        super(R.layout.item_order, data);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, OrderEntity product) {
        holder.setText(R.id.tvName, "购买人:" + product.getName());
        holder.setText(R.id.tvStatus, "待付款");
        holder.setText(R.id.tvPrice, "￥" + product.getPrice());
        holder.setText(R.id.tvDescribe, "￥" + product.getDescribe());
        holder.setText(R.id.tvNumber, "*" + product.getNumber());
        holder.setText(R.id.tvFee, "￥" + product.getFee());
        holder.setText(R.id.tvPriceAll, "￥" + (product.getPrice() * product.getNumber()));
        holder.setText(R.id.tvNumAll, String.format(context.getResources().getString(R.string.order_num_des), product.getNumber()));
    }
}

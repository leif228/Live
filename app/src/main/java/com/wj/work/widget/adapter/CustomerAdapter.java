package com.wj.work.widget.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.R;
import com.wj.work.base.BaseAdapter;
import com.wj.work.ui.helper.GlideHelper;
import com.wj.work.widget.entity.Customer;

import java.util.List;

/**
 * CustomerAdapter
 * 2020/5/5 9:33
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CustomerAdapter extends BaseAdapter<Customer> {

    public CustomerAdapter(@Nullable List<Customer> data) {
        super(R.layout.item_customer, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Customer customer) {
        holder.setText(R.id.tvName,customer.getName());
        GlideHelper.setCircleCropImg(getContext(), holder.getView(R.id.ivAvatar),customer.getAvatar());
    }

}
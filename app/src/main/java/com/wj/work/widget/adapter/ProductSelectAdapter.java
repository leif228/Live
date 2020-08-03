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
import com.wj.work.widget.part.NumAccepter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * ProductSelectAdapter
 * 2019/10/28 11:36
 * 物界
 * {www.wj.com
 * * @author Ly
 */
public class ProductSelectAdapter extends BaseAdapter<Product> implements LoadMoreModule {

    private Context context;

    public ProductSelectAdapter(Context context, @Nullable List<Product> data) {
        super(R.layout.item_market_with_point, data);
        this.context=context;
        setOnItemClickListener((adapter, view, position) -> {
            Product item=getData().get(position);
            if (item==null)return;
            item.setSelectTag(!item.isSelectTag());
            notifyItemChanged(position);
            if (accepter!=null)accepter.accept(getSelectedList().size());
        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_describe,item.getDescribe());
        helper.setText(R.id.tv_price,context.getResources().getString(R.string.price,item.getPrice()));
        Glide.with(context)
                .load(item.getImg())
                .transition(GlideHelper.getCrossFadeOptions())
                .into((ImageView) helper.getView(R.id.iv_cargo_pic));
        if (item.isSelectTag()){
            helper.setVisible(R.id.point,true);
        }else{
            helper.setVisible(R.id.point,false);
        }
    }

    // 获取选中
    public List<Product> getSelectedList(){
        List<Product> result= Lists.newArrayList();
        for (Product item:getData()) {
            if (item.isSelectTag())result.add(item);
        }
        return result;
    }

    private NumAccepter accepter;

    public void setAccepter(NumAccepter accepter) {
        this.accepter = accepter;
    }
}

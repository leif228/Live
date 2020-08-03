package com.wj.work.widget.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wj.work.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;


/**
 * BannerAdapter
 * 2020/3/17 15:04
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class BannerImageAdapter extends BannerAdapter<String, BannerImageAdapter.BannerViewHolder> {

    Context context;

    public BannerImageAdapter(List<String> mDatas, Context context) {
        super(mDatas);
        this.context = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        ImageView imgv = item.findViewById(R.id.img);
        return new BannerViewHolder(item, imgv);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        if (!TextUtils.isEmpty(data)) {
            Glide.with(context).load(data).into(holder.imageView);
        }
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        BannerViewHolder(View item, @NonNull ImageView view) {
            super(item);
            this.imageView = view;
        }
    }
}
package com.wj.work.base;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public void clear() {
        getData().clear();
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return getData().contains(elem);
    }

    public void add(T elem) {
        getData().add(elem);
        notifyDataSetChanged();
    }

    public void add(int position, T elem) {
        getData().add(position, elem);
        notifyDataSetChanged();
    }

    // 在最后面添加
    public void appendAll(List<T> elem) {
        if (elem != null && !elem.isEmpty()) {
            getData().addAll(elem);
            notifyDataSetChanged();
        }
    }

    public void addAll(int position, List<T> elem) {
        if (elem != null && !elem.isEmpty()) {
            getData().addAll(position, elem);
            notifyDataSetChanged();
        }
    }

    public void set(T oldElem, T newElem) {
        set(getData().indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        getData().set(index, elem);
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        if (position >= getData().size()) {
            return null;
        }
        return getData().get(position);
    }

    public void remove(T elem) {
        getData().remove(elem);
        notifyDataSetChanged();
    }

    @Override
    public void remove(int index) {
        getData().remove(index);
        notifyItemRemoved(index);
    }

    public void clearAndAddAll(List<T> elem){
        if (elem != null && !elem.isEmpty()) {
            getData().clear();
            getData().addAll(elem);
            notifyDataSetChanged();
        }
    }

    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

}

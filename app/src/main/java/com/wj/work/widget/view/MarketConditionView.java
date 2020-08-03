package com.wj.work.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.wj.work.R;
import com.wj.work.widget.entity.ConditionTab;
import com.wj.work.widget.part.TabClickListener;
import com.google.common.collect.Lists;

import java.util.List;

public class MarketConditionView extends LinearLayoutCompat {

    private List<ConditionTab> mTabList = Lists.newArrayList();
    private List<View> mViewList = Lists.newArrayList();
    Context context;
    private TabClickListener listener;

    public MarketConditionView(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MarketConditionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void initView() {
        LayoutParams itemParams=new LayoutParams(0,LayoutParams.MATCH_PARENT,1);
        removeAllViews();
        setOrientation(HORIZONTAL);
        setWeightSum(mTabList.size());
        for (int i = 0; i < mTabList.size(); i++) {
            View itemView = getItemView(i);
            ViewHolder vh = (ViewHolder) itemView.getTag();
            vh.textView.setText(mTabList.get(i).getText());
            if (mTabList.get(i).getImg() != 0) {
                vh.imgView.setVisibility(View.VISIBLE);
                vh.imgView.setImageResource(mTabList.get(i).getImg());
            } else {
                vh.imgView.setVisibility(View.GONE);
            }
            addView(itemView,itemParams);
        }
    }

    // 刷新
    private void setup() {
        for (int i = 0; i < mTabList.size(); i++) {
            View itemView = getItemView(i);
            ViewHolder vh = (ViewHolder) itemView.getTag();
            vh.textView.setText(mTabList.get(i).getText());

            // 字体颜色
            if (mTabList.get(i).isSelect()){
                vh.textView.setTextColor(context.getResources().getColor(R.color.app_orange));
            }else{
                vh.textView.setTextColor(context.getResources().getColor(R.color.app_black_l));
            }

            // 图片
            if (mTabList.get(i).getImg() != 0) {
                vh.imgView.setVisibility(View.VISIBLE);
                vh.imgView.setImageResource(mTabList.get(i).getImg());
            } else {
                vh.imgView.setVisibility(View.GONE);
            }
        }
    }

    private View getItemView(int index) {
        View result;
        if (mViewList != null && mViewList.size() > index) {
            result = mViewList.get(index);
        } else {
            result = View.inflate(context, R.layout.item_condition, null);
            ViewHolder vh = new ViewHolder();
            vh.textView = result.findViewById(R.id.text);
            vh.imgView = result.findViewById(R.id.img);
            vh.position = index;
            result.setTag(vh);
            result.setTag(R.id.TAG_POSITION, index);
            result.setOnClickListener(onclick);
            mViewList.add(result);
        }
        return result;
    }

    OnClickListener onclick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag(R.id.TAG_POSITION);
            if (listener != null) listener.onTabClick(position,mTabList.get(position));
        }
    };

    static class ViewHolder {
        int position;
        TextView textView;
        ImageView imgView;
    }

    public void setData(List<ConditionTab> list) {
        if (list == null || list.isEmpty()) return;
        mTabList.clear();
        mTabList.addAll(list);
        initView();
    }

    public ConditionTab getTab(int index) {
        return mTabList.get(index);
    }

    public void setTabClickListener(TabClickListener listener) {
        this.listener = listener;
    }

    public void notifyDataSetchanged(){
        setup();
    }
}

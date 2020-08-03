package com.wj.work.widget.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.wj.work.R;

/**
 * AppEmptyView
 * 2020/4/3 10:00
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class AppEmptyView extends LinearLayoutCompat {

    private Context context;
    private View mLoadPanel;
    private View mEmptyPanel;

    public AppEmptyView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayoutCompat.VERTICAL);
        setGravity(Gravity.CENTER);
        View view = View.inflate(context, R.layout.view_empty, null);
        mLoadPanel = view.findViewById(R.id.load_panel);
        mEmptyPanel = view.findViewById(R.id.empty_panel);
        addView(view, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    // 展示/隐藏正在加载数据
    public void showLoading(boolean isShowLoading) {
        if (isShowLoading) {
            mEmptyPanel.setVisibility(View.INVISIBLE);
        } else {
            mEmptyPanel.setVisibility(View.VISIBLE);
        }
    }

    // 展示空白数据
    public void showEmpty(boolean isShowEmptyView) {
        if (isShowEmptyView) {
            mEmptyPanel.setVisibility(View.VISIBLE);
        } else {
            mEmptyPanel.setVisibility(View.INVISIBLE);
        }
    }
}

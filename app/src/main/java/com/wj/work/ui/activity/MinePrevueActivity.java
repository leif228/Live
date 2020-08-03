package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wj.work.R;
import com.wj.work.base.BaseActivity;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.lib.kit.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * MinePrevueActivity
 * 我的预告
 * 2020/4/3 12:45
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class MinePrevueActivity extends BaseActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity, MinePrevueActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.tvHour)
    TextView tvHour;
    @BindView(R.id.tvMinute)
    TextView tvMinute;
    @BindView(R.id.tvSecond)
    TextView tvSecond;

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar,getResources().getColor(R.color.black),255);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_prevue;
    }

    @Override
    protected void initView() {

        tvTitle.setText("双十一大回馈");
        tvTime.setText("2020/02/05 16:00");


    }

    @OnClick({R.id.btnStartLive,R.id.btnDeletePre})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.btnStartLive:
                break;
            case R.id.btnDeletePre:
                break;
        }
    }

}

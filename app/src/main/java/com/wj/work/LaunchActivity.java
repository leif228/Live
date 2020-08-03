package com.wj.work;
import com.wj.work.base.BaseActivity;


public class LaunchActivity extends BaseActivity {

    private static final int SPLASH_TIME = 2000;//延时

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void onBackPressed() {
    }
}

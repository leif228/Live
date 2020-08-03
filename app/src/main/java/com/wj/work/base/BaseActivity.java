package com.wj.work.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.wj.work.R;
import com.wj.work.app.ActivityManager;
import com.wj.work.base.BaseThemeActivity;
import com.lib.kit.utils.StatusBarUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * TODO BaseActivity
 * 基本activity方法  加了一个滑动取消edittext焦点的监听
 * TODO 注释了泛型  presenter 直接在activity 里初始化
 *
 * @author Ly
 * 2019/10/28 11:38
 * 物界
 * {www.wj.com
 */
public abstract class BaseActivity extends BaseThemeActivity {

    private CompositeDisposable mCompositeDisposable;
    private Unbinder mUnBinder;
    protected View mContentView;
    protected Activity mActivity;
    protected RxPermissions mPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 不要title栏

        ActivityManager.instance().addActivity(this);
        mActivity = this;
        mContentView = View.inflate(mActivity, getLayoutResId(), null);
        if (beforeSetContentView()) return;
        setContentView(mContentView);
        // navigationcolor --------------------------
        int navigationColor=getNavigationBarColor();
        if (navigationColor!=0){
            Window window = getWindow();
            window.setNavigationBarColor(navigationColor);
        }
        // --------------------------------------------
        mUnBinder = ButterKnife.bind(this);
        mPermissions = new RxPermissions(this);
        // 在界面初始化完成后设置状态栏属性
        if (reqFullScreen()){
            StatusBarUtils.setFullScreenAndTransparentBar(this);
        }
        View statusBar = findViewById(R.id.status);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarAboveM(statusBar);
        } else {
            setStatusBarBelowM(statusBar);
        }
        mCompositeDisposable = new CompositeDisposable();
        // Eventbus
        if (isRegisterEventBus()) EventBus.getDefault().register(this);
        // 设置当有返回按钮时 点击返回
        View mReturnBtn = findViewById(R.id.lmb_return);
        if (mReturnBtn != null) {
            mReturnBtn.setOnClickListener(view -> {
                finish();
                switch (toAppointFinishAnim()) {
                    case FINISH_ANIM_RIGHT:   // 右边退出
                        overridePendingTransition(R.anim.finish_right_enter, R.anim.finish_right_exit);
                        break;
                    case FINISH_ANIM_BOTTOM:  // 底部退出
                        overridePendingTransition(R.anim.none, R.anim.finish_bottom_exit);
                        break;
                    case FINISH_ANIM_NONE:
                        break;
                }
            });
        }
        initView(); // 加载控件
    }

    protected  boolean reqFullScreen(){
        return true;
    }

    protected int getNavigationBarColor() {
        return getResources().getColor(R.color.white);
    }

    // 如果返回ture activity 退出
    protected boolean beforeSetContentView() {
        return false;
    }

    //6.0 以上的
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    // 5.0 -- 6.0 机型设置statusbar  特殊的子类重写
    protected void setStatusBarBelowM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @LayoutRes
    protected abstract int getLayoutResId();
    protected abstract void initView();

    //Eventbus 默认false
    protected boolean isRegisterEventBus() {
        return false;
    }

    //添加订阅
    protected void addDisposable(Disposable mDisposable) {
        if (mCompositeDisposable == null) mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) mCompositeDisposable.clear();//取消所有订阅
        mUnBinder.unbind();
        ActivityManager.instance().removeActivity(this);
        if (isRegisterEventBus()) EventBus.getDefault().unregister(this);
    }

    // Activity 跳转
    protected void skipActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
        overridePendingTransitionStartFromRight();
    }

    //--------------------------------------------------------------------防止快速点击
    private long lastTime;

    protected boolean clickable(long spaceTime) {
        long currentTime = System.currentTimeMillis();
        boolean result = (currentTime - lastTime) > spaceTime;
        if (result) lastTime = currentTime;
        return result;
    }

    protected boolean clickable() {
        return clickable(300);
    }
    //------------------------------------------------------------------------------
}

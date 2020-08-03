package com.wj.work.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.wj.work.R;
import com.wj.work.app.ActivityManager;
import com.lib.kit.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * TODO HomeBaseActivity
 * 基本activity方法  加了一个滑动取消edittext焦点的监听
 * TODO 注释了泛型  presenter 直接在activity 里初始化
 * <p>
 * 2019/10/28 11:38
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    private CompositeDisposable mCompositeDisposable;
    private Unbinder mUnbinder;
    protected Context mContext;
    protected View mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 不要title栏
        ActivityManager.instance().addActivity(this);
        mContext = this;
        mContentView = View.inflate(mContext, getLayoutResId(), null);
        setContentView(mContentView);
        mUnbinder = ButterKnife.bind(this);
        // 在界面初始化完成后设置状态栏属性  ----------------------------------------------
        StatusBarUtils.setFullScreenAndTransparentBar(this);
        View statusBar= findViewById(R.id.status);;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarAboveM(statusBar);
        }else{
            setStatusBarBelowM(statusBar);
        }
        // --------------------------------------------------------------------------
        mCompositeDisposable = new CompositeDisposable();
        // Eventbus
        if (isRegisterEventbus()) EventBus.getDefault().register(this);
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

    protected void setStatusBarAboveM(View statusBar) {
    }

    // 5.0 -- 6.0 机型设置statusbar  特殊的子类重写
    protected void setStatusBarBelowM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar,getResources().getColor(R.color.black),255);
    }

    protected final int FINISH_ANIM_NONE = 0x100;
    protected final int FINISH_ANIM_RIGHT = 0x110;
    protected final int FINISH_ANIM_BOTTOM = 0x112;

    //当子类需要指定 finish 动画时复写* 默认 右进右出
    protected int toAppointFinishAnim() {
        return FINISH_ANIM_RIGHT;
    }

    protected abstract void initView();

    //Eventbus 默认false
    protected boolean isRegisterEventbus() {
        return false;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    //添加订阅
    protected void addDisposable(Disposable mDisposable) {
        if (mCompositeDisposable == null) mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) mCompositeDisposable.clear();//取消所有订阅
        mUnbinder.unbind();
        ActivityManager.instance().removeActivity(this);
        if (isRegisterEventbus())EventBus.getDefault().unregister(this);
    }

    // Activity 跳转
    protected void skipActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    //跳转动画 右边进入
    protected void overridePendingTransitionStartFromRight() {
        overridePendingTransition(R.anim.start_right_enter, R.anim.start_right_exit);
    }

    //跳转动画 底部进入
    protected void overridePendingTransitionStartFromBottom() {
        overridePendingTransition(R.anim.start_bottom_enter, R.anim.none);
    }

    //跳转动画 底部退出
    protected void overridePendingTransitionFinishToBottom() {
        overridePendingTransition(R.anim.none, R.anim.finish_bottom_exit);
    }

    //跳转动画 右边退出
    protected void overridePendingTransitionFinishToRight() {
        overridePendingTransition(R.anim.finish_right_enter, R.anim.finish_right_exit);
    }

//    private LoadingDialog mLoadingDialog=null;//加载dialog

    //显示 加载dialog
    public void showLoadingDialog(String hint) {
//        if (mLoadingDialog==null)mLoadingDialog=new LoadingDialog(this);
//        mLoadingDialog.show(hint);
    }

    //关闭 加载dialog
    public void hideLoadingDialog() {
//        if (mLoadingDialog!=null&&mLoadingDialog.isShowing())mLoadingDialog.dismiss();
    }
}

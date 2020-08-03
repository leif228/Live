package com.wj.work.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wj.work.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment
 * 基本Fragment方法
 * 注释了泛型  presenter 直接在activity 里初始化
 * <p>
 * 谷歌地图在fragment 里初始化 只能继承 app.Fragment
 * 不能androidx
 *
 * @author Ly
 * 2019/10/28 11:38
 * 物界
 * {www.wj.com
 */
public abstract class BaseFragment extends Fragment {

    public abstract int getContentViewId();

    protected Activity mActivity;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater == null) return null;
        View mRootView = inflater.inflate(getContentViewId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);//绑定framgent
        if (isRegisterEventBus()) EventBus.getDefault().register(this);
        this.mActivity = getActivity();
        setFakeStatusParams(mRootView.findViewById(R.id.status));
        initView(mRootView,savedInstanceState);
        return mRootView;
    }

    // 设置状态栏参数
    protected void setFakeStatusParams(View status) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (isRegisterEventBus()) EventBus.getDefault().unregister(this);
    }

    protected boolean isRegisterEventBus() {
        return false; // 默认不注册
    }

    protected abstract void initView(View view ,Bundle savedInstanceState);

    // 防止快速点击
    private long lastTime;

    protected boolean clickable(long spaceTime){
        long currentTime=System.currentTimeMillis();
        boolean result= (currentTime-lastTime)>spaceTime;
        if (result)lastTime=currentTime;
        return result;
    }

    protected boolean clickable(){
        return clickable(300);
    }

    // Activity 跳转 默认右边进入
    protected void skipActivity(Class clazz) {
        Activity activity=getActivity();
        if (activity==null)return;
        activity.startActivity(new Intent(activity, clazz));
        overridePendingTransitionStartFromRight();
    }

    //跳转动画 右边进入
    protected void overridePendingTransitionStartFromRight() {
        Activity activity=getActivity();
        if (activity==null)return;
        activity.overridePendingTransition(R.anim.start_right_enter, R.anim.start_right_exit);
    }

    //跳转动画 底部进入
    protected void overridePendingTransitionStartFromBottom() {
        Activity activity=getActivity();
        if (activity==null)return;
        activity.overridePendingTransition(R.anim.start_bottom_enter, R.anim.none);
    }

    //跳转动画 底部退出
    protected void overridePendingTransitionFinishToBottom() {
        Activity activity=getActivity();
        if (activity==null)return;
        activity.overridePendingTransition(R.anim.none, R.anim.finish_bottom_exit);
    }

    //跳转动画 右边退出
    protected void overridePendingTransitionFinishToRight() {
        Activity activity=getActivity();
        if (activity==null)return;
        activity.overridePendingTransition(R.anim.finish_right_enter, R.anim.finish_right_exit);
    }

}

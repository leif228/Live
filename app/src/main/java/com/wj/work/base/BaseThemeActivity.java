package com.wj.work.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.wj.work.R;
import com.wj.work.base.mvp.BaseView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * BaseThemeActivity
 * 2020/4/27 9:41
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public abstract class BaseThemeActivity extends AppCompatActivity implements BaseView {

    private  QMUITipDialog mQMUITipDialog;
    protected final int FINISH_ANIM_NONE = 0x100;
    protected final int FINISH_ANIM_RIGHT = 0x110;
    protected final int FINISH_ANIM_BOTTOM = 0x112;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showLoadingDialog() {
       showLoadingDialog(getString(R.string.tip_loading),true);
    }

    @Override
    public void showLoadingDialog(String hint) {
        showLoadingDialog(hint,true);
    }

    @Override
    public void showLoadingDialog(boolean cancelAble) {
        showLoadingDialog(getString(R.string.tip_loading),cancelAble);
    }

    @Override
    public void showLoadingDialog(String hint, boolean cancelAble) {
        if (mQMUITipDialog!=null&&mQMUITipDialog.isShowing())return;
        mQMUITipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(hint)
                .create();
        mQMUITipDialog.setCanceledOnTouchOutside(cancelAble);
        mQMUITipDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (mQMUITipDialog!=null&&mQMUITipDialog.isShowing())mQMUITipDialog.dismiss();
    }

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY));
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

    //当子类需要指定 finish 动画时复写* 默认 右进右出
    protected int toAppointFinishAnim() {
        return FINISH_ANIM_RIGHT;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mQMUITipDialog!=null&&mQMUITipDialog.isShowing())mQMUITipDialog.dismiss();
    }
}

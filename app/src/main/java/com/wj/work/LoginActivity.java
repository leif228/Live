package com.wj.work;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.db.LoginSp;
import com.wj.work.db.SpManager;
import com.wj.work.ui.contract.LoginView;
import com.wj.work.ui.presenter.LoginPresenter;
import com.wj.work.widget.entity.LoginEntity;
import com.lib.kit.helper.EditChecker;
import com.lib.kit.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {

    @BindView(R.id.etTel)
    EditText etTel;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btnSendCode)
    Button btnSendCode;
    @BindView(R.id.tvBottomTip)
    TextView tvBottomTip;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected int getNavigationBarColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected void initView() {
        mPresenter = new LoginPresenter(this);
        initBottomTip();
        initEditText();
    }

    private void initEditText() {
        LoginSp loginSp = SpManager.getInstance().getLoginSp();
        if (!TextUtils.isEmpty(loginSp.getUserId())) {
            etTel.setText(loginSp.getUserId());
        }
    }

    private void initBottomTip() {
        String target1 = getResources().getString(R.string.tip_target_1);
        String target2 = getResources().getString(R.string.tip_target_2);
        String tip = String.format(getResources().getString(R.string.tip_login), target1, target2);

        SpannableString ss = new SpannableString(tip);
        int a = tip.indexOf(target1);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                onclickTip(0);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.app_orange)); //设置颜色
            }
        }, a, a + target1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

//        //《隐私政策》
//        //《用户协议》
        int b = tip.indexOf(target2);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                onclickTip(1);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.app_orange)); //设置颜色
            }
        }, b, b + target2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvBottomTip.setText(ss);
        tvBottomTip.setMovementMethod(LinkMovementMethod.getInstance());
        tvBottomTip.setHighlightColor(Color.parseColor("#00000000"));
    }

    private void onclickTip(int i) {
        if (!clickable()) return;
        if (i == 0) {  // 服务协议
        }
        if (i == 1) {  // 隐私政策
        }
    }

    @OnClick({R.id.login, R.id.btnSendCode})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.login:
                attemptLogin();
                break;
            case R.id.btnSendCode:
                attemptSendCode();
                break;
        }
    }

    private void attemptSendCode() {
        String tel = etTel.getText().toString();
        if (!EditChecker.checkEmpty(tel, getResources().getString(R.string.pls_enter_tel))) return;
        mPresenter.reqSendCode(tel);
    }

    private void attemptLogin() {
        String tel = etTel.getText().toString();
        String code = etCode.getText().toString();
        if (!EditChecker.checkEmpty(tel, getResources().getString(R.string.pls_enter_tel))) return;
        if (!EditChecker.checkEmpty(code, getResources().getString(R.string.pls_enter_code)))
            return;
        mPresenter.login(tel, code);
    }

    @Override
    public void loginSuccess(LoginEntity loginEntity) {
        SpManager.getInstance().getLoginSp().putLoginInfoEntity(loginEntity);
        skipActivity(MainActivity.class);
        finish();
    }
}

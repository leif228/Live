package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.ui.contract.WithDrawView;
import com.wj.work.ui.presenter.WithDrawPresenter;
import com.wj.work.widget.helper.ActivityAnimatorHelper;

import butterknife.BindView;

/**
 * WithdrawActivity
 * 2020/4/30 16:25
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class WithdrawActivity extends BaseMvpActivity<WithDrawPresenter> implements WithDrawView {

    @BindView(R.id.tvCash)
    TextView tvCash;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etAliName)
    EditText etAliName;
    @BindView(R.id.etAliAccount)
    EditText etAliAccount;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, WithdrawActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initView() {
        tvCash.setText(String.format(getResources().getString(R.string.cash_all),"200.0"));
    }
}

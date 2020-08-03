package com.wj.work.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wj.work.R;
import com.wj.work.app.UserLevelManager;
import com.wj.work.base.BaseFragment;
import com.wj.work.ui.activity.AdvertiseActivity;
import com.wj.work.ui.activity.CustomerOrderActivity;
import com.wj.work.ui.activity.MineCustomerListActivity;
import com.wj.work.ui.activity.WithdrawActivity;
import com.wj.work.ui.helper.GlideHelper;
import com.lib.kit.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class StewardFragment extends BaseFragment {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvLevel)
    TextView tvLevel;
    @BindView(R.id.tvReferrer)
    TextView tvReferrer;
    @BindView(R.id.tvInvitationCode)
    TextView tvInvitationCode;
    @BindView(R.id.tvRemain)
    TextView tvRemain;
    @BindView(R.id.tvWithdrawCash)
    TextView tvWithdrawCash;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_steward;
    }

    @Override
    protected void setFakeStatusParams(View status) {
        StatusBarUtils.setFakeStatusParams(status, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        uiUpdateUserInfo();
    }

    private void uiUpdateUserInfo() {
        GlideHelper.setCircleCropImg(mActivity, ivAvatar, R.mipmap.temp_avatar_def);
        tvLevel.setText(UserLevelManager.showUserLevel());
        tvReferrer.setText("我的推荐人:"+18523353131L+"(202234)");
        tvInvitationCode.setText("我的邀请码：202259");
        tvWithdrawCash.setText("3000.00");
        tvRemain.setText("42.00");
    }

    @OnClick({R.id.layOrder,R.id.layWithdraw,R.id.layMyCustomer,R.id.layAdvertise})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.layOrder:
                CustomerOrderActivity.actionStart(mActivity);
                break;
            case R.id.layWithdraw:
                WithdrawActivity.actionStart(mActivity);
                break;
            case R.id.layMyCustomer:
                MineCustomerListActivity.actionStart(mActivity);
                break;
            case R.id.layAdvertise:
                AdvertiseActivity.actionStart(mActivity);
                break;
        }
    }
}

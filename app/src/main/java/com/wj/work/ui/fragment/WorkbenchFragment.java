package com.wj.work.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wj.work.LoginActivity;
import com.wj.work.R;
import com.wj.work.app.ActivityManager;
import com.wj.work.base.BaseFragment;
import com.wj.work.db.SpManager;
import com.wj.work.ui.activity.AttentionProductListActivity;
import com.wj.work.ui.activity.AttentionStoreListActivity;
import com.wj.work.ui.activity.CreateLiveActivity;
import com.wj.work.ui.activity.MineLiveActivity;
import com.wj.work.ui.activity.RealNameActivity;
import com.wj.work.ui.helper.GlideHelper;
import com.wj.work.widget.entity.LoginEntity;
import com.wj.work.widget.helper.DialogResolver;
import com.lib.kit.utils.StatusBarUtils;
import com.lib.kit.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class WorkbenchFragment extends BaseFragment {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUserId)
    TextView tvUserId;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_workbench;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setUserInfo();
    }

    @Override
    protected void setFakeStatusParams(View status) {
        StatusBarUtils.setFakeStatusParams(status, getResources().getColor(R.color.black), 255);
    }

    private void setUserInfo() {
        LoginEntity loginEntity = SpManager.getInstance().getLoginSp().getLoginInfoEntity();
        tvUserId.setText(String.valueOf(loginEntity.getMyInvitationCode()));
        tvName.setText(loginEntity.getUserName());
        if (!TextUtils.isEmpty(loginEntity.getUserImg())) {
            GlideHelper.setCircleCropImg(mActivity, ivAvatar, loginEntity.getUserImg());
        } else {
            GlideHelper.setCircleCropImg(mActivity, ivAvatar, R.mipmap.temp_avatar_def);
        }
    }

    @OnClick({R.id.logout, R.id.item_real_name,
            R.id.item_create_live,
            R.id.item_my_live,
            R.id.tvMineShop,
            R.id.layLiveManager,
            R.id.item_attention_product,
            R.id.item_attention_store})
    public void onclick(View v) {
        if (!clickable()) return;
        switch (v.getId()) {
            case R.id.logout:
                DialogResolver.createAlertDialog(mActivity,
                        getResources().getString(R.string.tips),
                        getResources().getString(R.string.are_you_sure_logout),
                        (dialog, which) -> doLogout()).show();
                break;
            case R.id.item_real_name:
                RealNameActivity.actionStart(mActivity);
                break;
            case R.id.item_create_live:
                // 类型：0.未实名，1.审核中，2.驳回，3.成功
                int typeId=SpManager.getInstance().getLoginSp().getTypeId();
                if (typeId==0||typeId==2){
                    DialogResolver.createAlertDialog(mActivity,
                            getResources().getString(R.string.tips),
                            getResources().getString(R.string.pls_complete_the_real_name_authentication),
                            (dialog, which) -> RealNameActivity.actionStart(mActivity)
                    );
                }else if (typeId==1){
                    ToastUtils.showLong(R.string.tip_real_name_audit);
                }else{
                    CreateLiveActivity.actionStart(mActivity);
                }
                break;
            case R.id.item_my_live:
                MineLiveActivity.actionStart(mActivity);
                break;
            case R.id.item_attention_product:
                AttentionProductListActivity.actionStart(mActivity);
                break;
            case R.id.item_attention_store:
                AttentionStoreListActivity.actionStart(mActivity);
                break;
            case R.id.layLiveManager:
                break;
            case R.id.tvMineShop:
                break;
        }
    }

    // 退出登录
    private void doLogout() {
        SpManager.getInstance().forLogout();
        ActivityManager.instance().finishAllActivity();
        skipActivity(LoginActivity.class);
    }
}

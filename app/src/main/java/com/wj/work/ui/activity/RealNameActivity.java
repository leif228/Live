package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.constant.AppConstant;
import com.wj.work.db.SpManager;
import com.wj.work.ui.contract.RealNameView;
import com.wj.work.ui.presenter.RealNamePresenter;
import com.wj.work.widget.entity.RealNameInfo;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.wj.work.widget.helper.AppImgPickHelper;
import com.wj.work.widget.helper.PermissionHelper;
import com.lib.kit.helper.EditChecker;
import com.lib.kit.utils.StatusBarUtils;
import com.lib.kit.utils.ToastUtils;
import com.xiaosu.lib.permission.OnRequestPermissionsCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RealNameActivity
 * 2020/4/3 12:45
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class RealNameActivity extends BaseMvpActivity<RealNamePresenter> implements RealNameView {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etCardNum)
    EditText etCardNum;
    @BindView(R.id.ivNationalEmblem)
    ImageView ivNationalEmblem;
    @BindView(R.id.ivPortrait)
    ImageView ivPortrait;
    @BindView(R.id.btnCommit)
    Button btnCommit;
    @BindView(R.id.tvExplain)
    TextView tvExplain;

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity, RealNameActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    PermissionHelper mPermissionHelper;
    private boolean canEdit=false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_realname;
    }

    @Override
    protected void setStatusBarBelowM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.app_statusbar_gray), 255);
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.white), 255);
        StatusBarUtils.setLightMode(this);
    }

    @Override
    protected void initView() {
        uiPreSet();
        mPermissionHelper = new PermissionHelper();
        mPresenter = new RealNamePresenter(this);
        mPresenter.reqRealNameInfo();
    }

    private void uiPreSet() {
        int typeId=SpManager.getInstance().getLoginSp().getTypeId();
        switch (typeId){
            case 0:
                btnCommit.setVisibility(View.VISIBLE);
                tvExplain.setVisibility(View.GONE);
                break;
            case 1:
            case 3:
                btnCommit.setVisibility(View.INVISIBLE);
                tvExplain.setVisibility(View.VISIBLE);
                tvExplain.setText(typeId==1?"认证信息已提交,正在审核中。":"已完成实名认证。");
                break;
            case 2:
                btnCommit.setVisibility(View.VISIBLE);
                tvExplain.setVisibility(View.VISIBLE);
                tvExplain.setText("实名认证未成功。 ");
                break;
        }
    }

    private int imgSelectTag = 0;// 0 :人面  1:国徽面

    @OnClick({R.id.card_1, R.id.card_2, R.id.btnCommit})
    public void onclick(View v) {
        if (!clickable()) return;
        if (!canEdit)return;
        switch (v.getId()) {
            case R.id.card_1:
                imgSelectTag = 0;
                mPermissionHelper.checkCameraPermission(RealNameActivity.this, permissionCall);
                break;
            case R.id.card_2:
                imgSelectTag = 1;
                mPermissionHelper.checkCameraPermission(RealNameActivity.this, permissionCall);
                break;
            case R.id.btnCommit:
                attemptRealNameAuthentication();
                break;
        }
    }

    private String portraitImg;
    private String nationalEmblemImg;

    private void attemptRealNameAuthentication() {

        String realName = etName.getText().toString();
        String cardNum = etCardNum.getText().toString();
        String portraitImg = this.portraitImg;
        String nationalEmblemImg = this.nationalEmblemImg;

        if (!EditChecker.checkEmpty(realName, getResources().getString(R.string.pls_enter_tel)))
            return;
        if (!EditChecker.checkEmpty(cardNum, getResources().getString(R.string.pls_enter_card_number)))
            return;

        if (cardNum.length()!=18){
            ToastUtils.showLong(R.string.hint_id_card_length_short);
            return;
        }

        if (!EditChecker.checkEmpty(portraitImg, getResources().getString(R.string.pls_enter_portrait_img)))
            return;
        if (!EditChecker.checkEmpty(nationalEmblemImg, getResources().getString(R.string.pls_enter_nationalEmblem_img)))
            return;
        mPresenter.reqRealNameAuthentication(realName, cardNum, portraitImg, nationalEmblemImg);
    }

    OnRequestPermissionsCallBack permissionCall = new OnRequestPermissionsCallBack() {

        @Override
        public void onGrant() {
            AppImgPickHelper.startPickPhoto(RealNameActivity.this, 1, AppConstant.PICK_PHOTO_DATA);
        }

        @Override
        public void onDenied(String permission, boolean retry) {
            ToastUtils.showLong(R.string.hint_permission_denied);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0 || data == null) return;
        if (requestCode == AppConstant.PICK_PHOTO_DATA && resultCode == RESULT_OK) {
            List<String> selectPaths = AppImgPickHelper.getImgPathArrayListFromIntent(this, data); // PictureSelector
            if (selectPaths.size() > 0) {
                // 单张上传
                String path = selectPaths.get(0);
                mPresenter.upLoadImg(mActivity, path);
                uiUpdateImgForLocal(path);
            }
        }
    }

    private void uiUpdateImgForHttp(String path, boolean isSuccess) {

        if (imgSelectTag == 0) ivPortrait.setAlpha(1f);
        if (imgSelectTag == 1) ivNationalEmblem.setAlpha(1f);

        if (!isSuccess) {
            if (imgSelectTag == 0) {
                ivPortrait.setImageDrawable(null);
            }
            if (imgSelectTag == 1) {
                ivNationalEmblem.setImageDrawable(null);
            }
        }
    }

    private void uiUpdateImgForLocal(String path) {

        if (imgSelectTag == 0) {
            ivPortrait.setAlpha(0.6f);
            Glide.with(this).load(path).into(ivPortrait);
        }

        if (imgSelectTag == 1) {
            ivNationalEmblem.setAlpha(0.6f);
            Glide.with(this).load(path).into(ivNationalEmblem);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionFinishToRight();
    }

    @Override
    public void authenticationSuccess() {
        finish();
    }

    @Override
    public void upLoadImgSuccess(String path, String httpPath) {
        if (imgSelectTag == 0) {
            portraitImg = httpPath;
        } else {
            nationalEmblemImg = httpPath;
        }
        uiUpdateImgForHttp(path, true);
    }

    @Override
    public void upLoadImgFailed(String path) {
        if (imgSelectTag == 0) {
            portraitImg = null;
        } else {
            nationalEmblemImg = null;
        }
        uiUpdateImgForHttp(path, false);
    }

    @Override
    public void getRealNameInfoFailed(String msg) {
        finish();
    }

    @Override
    public void getRealNameInfoSuccess(RealNameInfo info) {
        //类型：0.未实名，1.审核中，2.驳回，3.成功
        int typeId=info.getTypeId();
        SpManager.getInstance().getLoginSp().updateRealNameType(typeId);
        uiSetRealNameInfo(info);
        switch (typeId){
            case 0:
                canEdit=true;
                etName.setEnabled(true);
                etCardNum.setEnabled(true);
                btnCommit.setVisibility(View.VISIBLE);
                tvExplain.setVisibility(View.GONE);
                break;
            case 1:
            case 3:
                canEdit=false;
                etName.setEnabled(false);
                etCardNum.setEnabled(false);
                btnCommit.setVisibility(View.INVISIBLE);
                tvExplain.setVisibility(View.VISIBLE);
                tvExplain.setText(typeId==1?"认证信息已提交,正在审核中。":"已完成实名认证。");
                break;
            case 2:
                canEdit=true;
                etName.setEnabled(true);
                etCardNum.setEnabled(true);
                btnCommit.setVisibility(View.VISIBLE);
                tvExplain.setVisibility(View.VISIBLE);
                tvExplain.setText("实名认证未成功。 原因:"+info.getMemo3());
                break;
        }
    }

    private void uiSetRealNameInfo(RealNameInfo info) {
        if (info==null)return;
        this.portraitImg=info.getIdCardPositiveImg();
        this.nationalEmblemImg=info.getIdCardReverseSideImg();
        if (!TextUtils.isEmpty(info.getRealName())){
           etName.setText(info.getRealName());
        }
        if (!TextUtils.isEmpty(info.getIdCard())){
            etCardNum.setText(info.getIdCard());
        }
        if (!TextUtils.isEmpty(info.getIdCardPositiveImg())){
            Glide.with(mActivity).load(info.getIdCardPositiveImg()).into(ivPortrait);
        }
        if (!TextUtils.isEmpty(info.getIdCardReverseSideImg())){
            Glide.with(mActivity).load(info.getIdCardReverseSideImg()).into(ivNationalEmblem);
        }
    }
}

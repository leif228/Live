package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import com.bumptech.glide.Glide;
import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.constant.AppConstant;
import com.wj.work.ui.activity.LiveActivity;
import com.wj.work.ui.activity.SelectProductActivity;
import com.wj.work.ui.contract.CreateLiveView;
import com.wj.work.ui.presenter.CreateLivePresenter;
import com.wj.work.utils.DateTimeUtils;
import com.wj.work.widget.entity.CreateLiveEntity;
import com.wj.work.widget.entity.DefinitionEntity;
import com.wj.work.widget.entity.LiveTypeEntity;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.wj.work.widget.helper.AppImgPickHelper;
import com.wj.work.widget.helper.DialogResolver;
import com.wj.work.widget.params.CreateLiveReqParams;
import com.wj.work.widget.params.LiveParams;
import com.google.common.collect.Lists;
import com.lib.kit.helper.EditChecker;
import com.lib.kit.utils.StatusBarUtils;
import com.lib.kit.utils.ToastUtils;
import com.suke.widget.SwitchButton;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CreateLiveActivity
 * 2020/4/3 12:45
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class CreateLiveActivity extends BaseMvpActivity<CreateLivePresenter> implements CreateLiveView {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.checkbox)
    AppCompatCheckBox checkbox;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvDefinition)
    TextView tvDefinition;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.btnCommit)
    Button btnCommit;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity, CreateLiveActivity.class);
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_live;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionFinishToRight();
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
        StatusBarUtils.setDarkMode(this);
    }

    @Override
    protected void initView() {
        mPresenter = new CreateLivePresenter(this);
        checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnCommit.setText(isChecked?getResources().getString(R.string.create_preview):
                    getResources().getString(R.string.open_live));
        });
        uiUpdateDefinition();
    }

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

    private LiveTypeEntity chooseType = null;
    //  1: 超清480P     2:超清720P  3 蓝光 1080P
    private DefinitionEntity chooseDefinition = new DefinitionEntity(2,"超清 720P");
    private Date chooseDate = null;

    @OnClick({R.id.ivAvatar, R.id.layLiveType,
            R.id.layDefinition, R.id.layAddProduct,
            R.id.btnCommit,
            R.id.layAdvance
    })
    public void onclick(View v) {
        if (!clickable()) return;
        switch (v.getId()) {
            case R.id.ivAvatar:
                addDisposable(mPermissions.request(AppConstant.CAMERA_PERMISSIONS)
                        .as(bindAutoDispose())
                        .subscribe(accept -> {
                            if (accept) {
                                AppImgPickHelper.startPickPhoto(CreateLiveActivity.this, 1,
                                        AppConstant.PICK_PHOTO_DATA);
                            } else {
                                ToastUtils.showLong(R.string.hint_permission_denied);
                            }
                        })
                );
                break;
            case R.id.layLiveType:
                List<LiveTypeEntity> list = DataTemp.getLiveTypeList();
                DialogResolver.createLiveTypeDialog(mActivity,
                        list, (dialog, itemView, position, tag) -> {
                            chooseType = list.get(position);
                            uiUpdateType();
                            dialog.dismiss();
                        }).show();
                break;
            case R.id.layDefinition:
                List<DefinitionEntity> list2 = DataTemp.getLiveDefinitionList();
                DialogResolver.createLiveDefinitionDialog(mActivity,
                        list2, (dialog, itemView, position, tag) -> {
                            chooseDefinition = list2.get(position);
                            uiUpdateDefinition();
                            dialog.dismiss();
                        }).show();
                break;
            case R.id.layAddProduct:
                skipActivity(SelectProductActivity.class);
                break;
            case R.id.layAdvance:
                DialogResolver.createLiveTimePickDialog(mActivity, (date, v1) -> {
                    chooseDate = date;
                    uiUpdateDate();
                }).show();
                break;
            case R.id.btnCommit:
                addDisposable(mPermissions.request(AppConstant.LIVE_PERMISSIONS)
                        .as(bindAutoDispose())
                        .subscribe(accept -> {
                            if (accept) {
                                attemptCommit();
                            } else {
                                ToastUtils.showLong(R.string.hint_permission_denied);
                            }
                        })
                );
                break;
        }
    }

    private String liveCoverRec; // 直播封面
    private CreateLiveReqParams createLiveReqParams; // 界面参数

    // 提交
    private void attemptCommit() {

        String liveCover = this.liveCoverRec;
        String liveTitle = etTitle.getText().toString();
        boolean isPreView = checkbox.isChecked();
        boolean isPush = switchButton.isChecked();

        if (!EditChecker.checkEmpty(liveCover, getResources().getString(R.string.pls_choose_live_cover)))
            return;
        if (!EditChecker.checkEmpty(liveTitle, getResources().getString(R.string.pls_enter_live_title)))
            return;

        if (chooseType ==null) {
            ToastUtils.showLong(R.string.pls_choose_live_type);
            return;
        }

        if (chooseDefinition ==null) {
            ToastUtils.showLong(R.string.pls_choose_live_sharpness);
            return;
        }

        if (isPreView&&chooseDate==null){
            ToastUtils.showLong(R.string.pls_choose_live_time);
            return;
        }

        createLiveReqParams=new CreateLiveReqParams()
                .setLiveCover(liveCoverRec)
                .setLiveTitle(liveTitle)
                .setRecommendGoods("1")
                .setTypeId(String.valueOf(chooseType.getId()))
                .setSharpnessType(String.valueOf(chooseDefinition.getId()))
                .setTypeSub(String.valueOf(isPreView?1:0))
                .setPushFollowUsersType(String.valueOf(isPush?0:1))
                .setPreBroadcastTime(chooseDate==null?null:createReqTime(chooseDate));
        mPresenter.reqCreateLive(createLiveReqParams);
    }

    private String createReqTime(Date chooseDate) {
        return "";
    }

    private void uiUpdateImgForLocal(String localPath) {
        ivAvatar.setAlpha(0.6f);
        Glide.with(this).load(localPath).into(ivAvatar);
    }

    private void uiUpdateImgForHttp(String localPath, boolean isSuccess) {
        ivAvatar.setAlpha(1f);
        if (!isSuccess) {
            ivAvatar.setImageDrawable(null);
        }
    }

    private void uiUpdateDate() {
        tvTime.setText(chooseDate == null ? "" : DateTimeUtils.showAdvanceTime(chooseDate));
        tvDay.setText(chooseDate == null ? "" : DateTimeUtils.showAdvanceDay(chooseDate));
    }

    private void uiUpdateDefinition() {
        tvDefinition.setText(chooseDefinition == null ? "" : chooseDefinition.getDescribe());
    }

    private void uiUpdateType() {
        tvType.setText(chooseType == null ? "" : chooseType.getText());
    }

    @Override
    public void createLiveSuccess(CreateLiveEntity response) {
        mPresenter.reqPushAddress(response.getId());
    }

    @Override
    public void reqPushAddressSuccess(long id, String pushAddress) {
        List<String> goods = Lists.newArrayList();
        goods.add("098");
        LiveActivity.actionStart(mActivity, new LiveParams()
                .setDefinition(chooseDefinition)
                .setNotifyAllSubscriber(true)
                .setPreview(false)
                .setGoods(goods)
                .setCameraFront(false)
                .setId(id)
                .setPushFlowAddress(pushAddress)
        );
    }

    @Override
    public void upLoadImgSuccess(String path, String httpPath) {
        liveCoverRec = httpPath;
        uiUpdateImgForHttp(path, true);
    }

    @Override
    public void upLoadImgFailed(String path) {
        liveCoverRec = null;
        uiUpdateImgForHttp(path, false);
    }
}

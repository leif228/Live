package com.wj.work.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.view.View;
import android.view.WindowManager;

import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.littlegreens.netty.client.extra.NettyConfig;
import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.mvp.BaseMvpActivity;
import com.wj.work.event.MessageEvent;
import com.wj.work.event.NetworkEvent;
import com.wj.work.ui.contract.LiveView;
import com.wj.work.ui.fragment.live.LiveElementFragment;
import com.wj.work.ui.fragment.live.LiveEmptyFragment;
import com.wj.work.ui.helper.ViewHelper;
import com.wj.work.ui.live.CameraPreviewFrameView;
import com.wj.work.ui.presenter.LivePresenter;
import com.wj.work.widget.adapter.LiveViewAdapter;
import com.wj.work.widget.helper.ActivityAnimatorHelper;
import com.wj.work.widget.helper.AppGsonHelper;
import com.wj.work.widget.helper.DialogResolver;
import com.wj.work.widget.params.LiveParams;
import com.wj.work.widget.view.LiveToolsDialog;
import com.lib.kit.helper.SoftKeyBoardListener;
import com.lib.kit.utils.LL;
import com.lib.kit.utils.StatusBarUtils;
import com.littlegreens.netty.client.NettyManager;
import com.littlegreens.netty.client.listener.NettyClientListener;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import butterknife.BindView;

/**
 * LiveActivity
 * 2020/4/22 10:19
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LiveActivity extends BaseMvpActivity<LivePresenter> implements StreamingStateChangedListener,
        LiveView, StreamStatusCallback, AudioSourceCallback, StreamingSessionListener, NettyClientListener {

    public static final String INTENT_LIVE_PARAMS = "INTENT_LIVE_PARAMS";
    LiveParams mLiveParams;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.cameraPreview_surfaceView)
    CameraPreviewFrameView mCameraPreviewFrameView;

    LiveViewAdapter mAdapter;
    NettyManager mNettyManager;

    public static void actionStart(Activity activity, LiveParams liveParams) {
        Intent intent = new Intent(activity, LiveActivity.class);
        intent.putExtra(INTENT_LIVE_PARAMS, AppGsonHelper.toJson(liveParams));
        activity.startActivity(intent);
        ActivityAnimatorHelper.startFromRight(activity);
    }

    @Override
    protected boolean reqFullScreen() {
        return false;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getNavigationBarColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected void setStatusBarBelowM(View statusBar) {
        StatusBarUtils.setFakeStatusParams(statusBar, getResources().getColor(R.color.black), 255);
    }

    @Override
    protected void setStatusBarAboveM(View statusBar) {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_live;
    }

    @Override
    protected boolean beforeSetContentView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return super.beforeSetContentView();
    }

    LiveElementFragment liveElementFragment;

    @Override
    protected void initView() {
        mPresenter = new LivePresenter(this);
        mNettyManager = new NettyManager(1, NettyConfig.HOST,NettyConfig.TCP_PORT);
        mNettyManager.setNettyClientListener(this);
        mNettyManager.connect();
        mLiveParams = AppGsonHelper.fromJson(getIntent().getStringExtra(INTENT_LIVE_PARAMS), LiveParams.class);
        Fragment[] fragments = new Fragment[2];
        liveElementFragment = new LiveElementFragment();
        fragments[0] = new LiveEmptyFragment();
        fragments[1] = liveElementFragment;
        liveElementFragment.setUIEventListener(listener);
        mAdapter = new LiveViewAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ViewHelper.accuracyViewHeight(mCameraPreviewFrameView);
        initLiveParams();
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                liveElementFragment.recyclerviewScrollToPositionWithOffset();
            }

            @Override
            public void keyBoardHide(int height) {
                liveElementFragment.recyclerviewScrollToPositionWithOffset();
            }
        });
    }

    private MediaStreamingManager mMediaStreamingManager;
    private StreamingProfile mProfile;
    private CameraStreamingSetting mCameraStreamingSetting;
    private Switcher mSwitcher = new Switcher();

    private int beautyProgress = 50; // 磨皮程度
    private int whitenProgress = 50; // 美白程度
    private int reddenProgress = 50; // 红润程度

    private void initLiveParams() {
        if (mLiveParams == null) return;
        String publishURLFromServer = mLiveParams.getPushFlowAddress();
        try {
            //encoding setting
            mProfile = new StreamingProfile();
            int definitionId=mLiveParams.getDefinition().getId();
            int videoQuality = definitionId == 1 ? StreamingProfile.VIDEO_QUALITY_MEDIUM1 :
                    definitionId == 2 ? StreamingProfile.VIDEO_QUALITY_HIGH1 :
                            StreamingProfile.VIDEO_QUALITY_HIGH3;
            mProfile.setVideoQuality(videoQuality)
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
                    .setEncodingSizeLevel(StreamingProfile.VIDEO_ENCODING_HEIGHT_480)
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
                    .setPublishUrl(publishURLFromServer);

            //preview setting
            mCameraStreamingSetting = new CameraStreamingSetting();
            mCameraStreamingSetting.setCameraId(mLiveParams.isCameraFront() ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK)
                    .setContinuousFocusModeEnabled(true)
                    .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                    .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                    .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_VIDEO)
            ;
            mCameraStreamingSetting.setFaceBeautySetting(new CameraStreamingSetting
                    .FaceBeautySetting(
                    beautyProgress / 100.0f,
                    whitenProgress / 100.0f,
                    reddenProgress / 100.0f))
                    .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY)
                    .setBuiltInFaceBeautyEnabled(true)
            ;

            mCurrentCamFacingIndex = mLiveParams.isCameraFront() ? 1 : 0;

            //streaming engine init and setListener
            mMediaStreamingManager = new MediaStreamingManager(this, mCameraPreviewFrameView,
                    AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC);  // soft codec
            mMediaStreamingManager.prepare(mCameraStreamingSetting, mProfile);
            mMediaStreamingManager.setStreamingStateListener(this); // 推流状态监听
            mMediaStreamingManager.setStreamingSessionListener(this); //
            mMediaStreamingManager.setStreamStatusCallback(this);
            mMediaStreamingManager.setAudioSourceCallback(this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    int mCurrentCamFacingIndex = 0;

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();
            CameraStreamingSetting.CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            LL.V("switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);
        }
    }

    final LiveElementFragment.UIEventListener listener = new LiveElementFragment.UIEventListener() {
        @Override
        public void reqSendMessage(String message) {
            mNettyManager.senMessage(message);
        }

        @Override
        public void uiOpenShoppingPanel() {
            DialogResolver.createShoppingPackageDialog(mActivity, DataTemp.getProducts(), new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) {
                }
            }).show();
        }

        @Override
        public void uiOpenToolsPanel() {
            CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
            DialogResolver.createLiveToolsDialog(mActivity, fbSetting, new LiveToolsDialog.ToolsCallBack() {

                // 调整美颜参数
                @Override
                public void onBeautyProgressChanged(int beauty, int whiten, int redden, int tickCount) {
                    updateBeautySetting(beauty, whiten, redden, tickCount);
                }

                // 切换摄像头
                @Override
                public void attemptSwitchCamera(View view) {
                    view.removeCallbacks(mSwitcher);
                    view.postDelayed(mSwitcher, 100);
                }
            }).show();
        }
    };

    public void updateBeautySetting(int beauty, int whiten, int redden, int tickCount) {
        CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
        if (beauty >= 0) fbSetting.beautyLevel = beauty * 1.0f / tickCount;
        if (whiten >= 0) fbSetting.whiten = whiten * 1.0f / tickCount;
        if (redden >= 0) fbSetting.redden = redden * 1.0f / tickCount;
        mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
    }

    private void attemptExist() {
        DialogResolver.createAlertDialog(mActivity,
                getResources().getString(R.string.tips),
                getResources().getString(R.string.hint_stop_live),
                (dialog, which) -> {
                    mMediaStreamingManager.stopStreaming();
                    mNettyManager.release();
                    finish();
                }
        ).show();
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        switch (streamingState) {
            case PREPARING:
                LL.V("PREPARING");
                break;
            case READY:
                LL.V("READY");
                new Thread(() -> {
                    if (mMediaStreamingManager != null) {
                        mMediaStreamingManager.startStreaming();
                    }
                }).start();
                break;
            case CONNECTING:
                LL.V("连接中");
                break;
            case STREAMING:
                LL.V("推流中");
                break;
            case SHUTDOWN: // --
                LL.V("直播中断");
                runOnUiThread(() -> mViewPager.postDelayed(() -> mPresenter.reqStartRecording(mLiveParams.getId()), 1000));
                break;
            case IOERROR:
                LL.V("IOERROR");
                break;
            case OPEN_CAMERA_FAIL:
                LL.V("摄像头打开失败");
                break;
            case DISCONNECTED:
                LL.V("已经断开连接");
                break;
            case TORCH_INFO:
                LL.V("开启闪光灯");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaStreamingManager.pause();
    }

    private long timeMill = 0;

    @Override
    public void notifyStreamStatusChanged(StreamingProfile.StreamStatus status) {
        if (System.currentTimeMillis() - timeMill > 1000 * 30) {
            timeMill = System.currentTimeMillis();
        }
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer srcBuffer, int size, long tsInNanoTime, boolean isEof) {
    }

    @Override
    public boolean onRecordAudioFailedHandled(int code) {
        LL.V("onRecordAudioFailedHandled ");
        return false;
    }

    @Override
    public boolean onRestartStreamingHandled(int code) {
        LL.V("onRestartStreamingHandled ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mMediaStreamingManager != null) {
                    mMediaStreamingManager.startStreaming();
                }
            }
        }).start();
        return false;
    }

    @Override
    public void onBackPressed() {
        attemptExist();
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        return null;
    }

    @Override
    public int onPreviewFpsSelected(List<int[]> list) {
        return -1;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(MessageEvent event) {
        if (event.getCode() == MessageEvent.LIVE_CLICK_EXIST) {
            attemptExist();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(NetworkEvent event) {
        if (event.isConnected()) {
            LL.V("-----------    网络可用");
//            mNettyManager.attemptReConnect();// 由于网络原因 重新连接
            mNettyManager.connect();
        } else {
            LL.V("-----------    网络不可用");
            mNettyManager.release();
        }
    }

    @Override
    public void onMessageResponseClient(String msg, int index) {
        LL.V("客户端收到消息: -- " + msg);
        liveElementFragment.uiAppendMessage(msg);
    }

    @Override
    public void onClientStatusConnectChanged(int statusCode, int index) {
    }

    @Override
    public void connectSuccess(String ip, int index) {

    }

    @Override
    public void nettyNetSearchBack() {

    }

    @Override
    public void nettyNetGetDevListOver(String tx, JSONObject objParam) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNettyManager.release();
    }
}

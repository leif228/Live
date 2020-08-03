package com.wj.work.widget.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.wj.work.R;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qmuiteam.qmui.widget.QMUISlider;

/**
 * 购物袋
 * <p>
 * 2020/5/14 9:27
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LiveToolsDialog extends Dialog {

    private Context context;
    private QMUISlider sliderWhiten;
    private QMUISlider sliderBeauty;
    private QMUISlider sliderRedden;

    private CameraStreamingSetting.FaceBeautySetting fbSetting;

    public LiveToolsDialog(Context context, CameraStreamingSetting.FaceBeautySetting fbSetting) {
        super(context, R.style.bottom_dialog);
        this.context = context;
        this.fbSetting=fbSetting;
    }

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_live_tools, null);
        view.findViewById(R.id.layCamera).setOnClickListener(onclick);
        initSlider(view);
        setContentView(view);
        setCancelable(true);//
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        if (window == null) return;
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.dimAmount=0.1f;
        window.setAttributes(params);
    }

    private void initSlider(View rootView) {

        sliderBeauty=rootView.findViewById(R.id.sliderBeauty);
        sliderWhiten=rootView.findViewById(R.id.sliderWhiten);
        sliderRedden=rootView.findViewById(R.id.sliderRedden);

        sliderBeauty.setCurrentProgress((int) (fbSetting.beautyLevel*100));
        sliderWhiten.setCurrentProgress((int) (fbSetting.whiten*100));
        sliderRedden.setCurrentProgress((int) (fbSetting.redden*100));

        sliderWhiten.setCallback(new QMUISlider.Callback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                callBack.onBeautyProgressChanged(-1,progress,-1,tickCount);
            }

            @Override
            public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {
            }

            @Override
            public void onTouchUp(QMUISlider slider, int progress, int tickCount) {
            }

            @Override
            public void onStartMoving(QMUISlider slider, int progress, int tickCount) {
            }

            @Override
            public void onStopMoving(QMUISlider slider, int progress, int tickCount) {
            }
        });

        sliderBeauty.setCallback(new QMUISlider.Callback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                callBack.onBeautyProgressChanged(progress,-1,-1,tickCount);
            }

            @Override
            public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {
            }

            @Override
            public void onTouchUp(QMUISlider slider, int progress, int tickCount) {
            }

            @Override
            public void onStartMoving(QMUISlider slider, int progress, int tickCount) {
            }

            @Override
            public void onStopMoving(QMUISlider slider, int progress, int tickCount) {
            }
        });

        sliderRedden.setCallback(new QMUISlider.Callback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                callBack.onBeautyProgressChanged(-1,-1,progress,tickCount);
            }

            @Override
            public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {
            }

            @Override
            public void onTouchUp(QMUISlider slider, int progress, int tickCount) {
            }

            @Override
            public void onStartMoving(QMUISlider slider, int progress, int tickCount) {
            }

            @Override
            public void onStopMoving(QMUISlider slider, int progress, int tickCount) {
            }
        });
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layCamera:
                    callBack.attemptSwitchCamera(v);
                    break;
            }
        }
    };

    private ToolsCallBack callBack;

    public LiveToolsDialog subscribe(ToolsCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public interface ToolsCallBack{
        void onBeautyProgressChanged(int beauty, int whiten, int redden, int tickCount);
        void attemptSwitchCamera(View view);
    }

}

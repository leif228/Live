package com.wj.work.widget.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.core.util.Consumer;

import com.bigkoo.pickerview.TimePickerView;
import com.wj.work.R;
import com.wj.work.widget.entity.DefinitionEntity;
import com.wj.work.widget.entity.LiveTypeEntity;
import com.wj.work.widget.entity.Product;
import com.wj.work.widget.view.LiveToolsDialog;
import com.wj.work.widget.view.ShoppingPackageDialog;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.List;

public class DialogResolver {

    // 购物袋
    public static Dialog createShoppingPackageDialog(Context context, List<Product> list, Consumer<Integer> consumer) {
        return new ShoppingPackageDialog(context, list).subscribe(consumer);
    }

    // 提示
    public static AlertDialog createAlertDialog(Context context, String title, String content, DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context)
                .setMessage(content)
                .setTitle(title)
                .setNegativeButton(context.getResources().getString(R.string.Cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(context.getResources().getString(R.string.OK), clickListener).create();
    }

    // 创建直播--直播分类  bottom
    public static Dialog createLiveTypeDialog(Context context, List<LiveTypeEntity> types,
                                              QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(context);
        for (LiveTypeEntity type : types) {
            builder.addItem(type.getText());
        }
        builder.setTitle(context.getResources().getString(R.string.title_live_type))
                .setGravityCenter(false)
                .setRadius(QMUIDisplayHelper.dp2px(context, 3))
                .setAllowDrag(false)
                .setOnSheetItemClickListener(listener);
        return builder.build();
    }

    // 创建直播--选择清晰度  bottom
    public static Dialog createLiveDefinitionDialog(Context context, List<DefinitionEntity> types,
                                                    QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(context);
        for (DefinitionEntity type : types) {
            builder.addItem(type.getDescribe());
        }
        builder.setTitle(context.getResources().getString(R.string.title_live_type))
                .setGravityCenter(false)
                .setRadius(QMUIDisplayHelper.dp2px(context, 3))
                .setAllowDrag(false)
                .setOnSheetItemClickListener(listener);
        return builder.build();
    }

    // 创建直播--选择时间
    public static TimePickerView createLiveTimePickDialog(Context context, TimePickerView.OnTimeSelectListener listener) {
        return new TimePickerView.Builder(context, listener)
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText(context.getResources().getString(R.string.cancel))//取消按钮文字
                .setSubmitText(context.getResources().getString(R.string.complete))//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText(context.getResources().getString(R.string.advance_time))//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
    }

    // 转发
    public static Dialog createForwardDialog(Context context, QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(context);
        builder.addItem(R.mipmap.ic_wx_xcx, context.getResources().getString(R.string.wx_xcx), 0, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.ic_wx_circle, "朋友圈", 1, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.ic_weixin, "微信", 2, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(listener)
        ;
        return builder.build();
    }

    // 直播工具
    public static Dialog createLiveToolsDialog(Context context, CameraStreamingSetting.FaceBeautySetting fbSetting, LiveToolsDialog.ToolsCallBack callBack) {
        return new LiveToolsDialog(context, fbSetting).subscribe(callBack);
    }
}

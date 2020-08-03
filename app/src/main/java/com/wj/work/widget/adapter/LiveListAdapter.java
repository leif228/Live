package com.wj.work.widget.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.R;
import com.wj.work.ui.helper.GlideHelper;
import com.wj.work.widget.entity.LiveRecordEntity;
import java.util.List;

/**
 * LiveListAdapter
 * 2019/10/28 11:36
 * 物界
 * {www.wj.com
 * * @author Ly
 */
public class LiveListAdapter extends BaseMultiItemQuickAdapter<LiveRecordEntity, BaseViewHolder> implements LoadMoreModule {

    private Context context;
    private String uTxtStartTime;
    private String uTxtEndTime;
    private String uTxtDuration;

    public LiveListAdapter(Context context, @Nullable List<LiveRecordEntity> data) {
        super(data);
        this.context = context;
        addItemType(LiveRecordEntity.STATUS_PREVIEW, R.layout.item_live_preview);
        addItemType(LiveRecordEntity.STATUS_PAUSE, R.layout.item_live_pause);
        addItemType(LiveRecordEntity.STATUS_FINISHED, R.layout.item_live_finished);

        uTxtStartTime=context.getString(R.string.u_start_time);
        uTxtEndTime=context.getString(R.string.u_end_time);
        uTxtDuration=context.getString(R.string.u_live_duration);

        addChildClickViewIds(R.id.lmbForward,R.id.lmbDelete);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, LiveRecordEntity mineLiveEntity) {

        int itemType = getItemViewType(baseViewHolder.getAdapterPosition());
        baseViewHolder.setText(R.id.start_time_tv, mineLiveEntity.getAddTime());
        baseViewHolder.setText(R.id.name, mineLiveEntity.getLiveTitle());
        Glide.with(context).load(mineLiveEntity.getLiveCover())
                .transition(GlideHelper.getCrossFadeOptions())
                .into((ImageView) baseViewHolder.getView(R.id.avatar));

        switch (itemType){
            case LiveRecordEntity.STATUS_FINISHED:
                baseViewHolder.setText(R.id.start_time_tv, uTxtStartTime+mineLiveEntity.getAddTime());
                baseViewHolder.setText(R.id.finish_time_tv, uTxtEndTime+mineLiveEntity.getEndTime());
                baseViewHolder.setText(R.id.live_duration, uTxtDuration+"30分钟");
                break;
            case LiveRecordEntity.STATUS_PAUSE:
            case LiveRecordEntity.STATUS_PREVIEW:
                baseViewHolder.setText(R.id.start_time_tv, uTxtStartTime+mineLiveEntity.getAddTime());
                break;
        }
    }
}

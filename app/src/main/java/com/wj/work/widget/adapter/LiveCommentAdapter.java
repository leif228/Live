package com.wj.work.widget.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wj.work.R;
import com.wj.work.widget.entity.LiveComment;

import java.util.List;


/**
 * MarketAdapter
 * 2019/10/28 11:36
 * 物界
 * {www.wj.com
 * * @author Ly
 */
public class LiveCommentAdapter extends BaseMultiItemQuickAdapter<LiveComment, BaseViewHolder> {

    private Context context;

    public LiveCommentAdapter(Context context, @Nullable List<LiveComment> data) {
        super(data);
        this.context=context;
        addItemType(LiveComment.TYPE_SYSTEM, R.layout.item_comment_system);
        addItemType(LiveComment.TYPE_ENTER, R.layout.item_comment_enter);
        addItemType(LiveComment.TYPE_NORMAL, R.layout.item_comment_normal);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LiveComment item) {
        int type=item.getType();
        switch (type){
            case LiveComment.TYPE_SYSTEM:
            case LiveComment.TYPE_ENTER:
                helper.setText(R.id.content,item.getContent());
                break;
            case LiveComment.TYPE_NORMAL:
                helper.setText(R.id.content,item.getUserName()+":"+item.getContent());
                break;
        }
    }
}

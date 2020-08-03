package com.wj.work.ui.fragment.live;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.wj.work.DataTemp;
import com.wj.work.R;
import com.wj.work.base.BaseFragment;
import com.wj.work.event.MessageEvent;
import com.wj.work.widget.adapter.LiveCommentAdapter;
import com.wj.work.widget.entity.LiveComment;
import com.wj.work.widget.helper.DialogResolver;
import com.lib.kit.view.LimitButton;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * LiveElementFragment
 * 2020/4/22 10:46
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class LiveElementFragment extends BaseFragment {

    @BindView(R.id.avatar)
    ImageView imageView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.send)
    LimitButton send;
    @BindView(R.id.delete)
    LimitButton delete;

    private LiveCommentAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_live_element;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Glide.with(this)
                .load(R.mipmap.temp_live_avatar)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);

        mAdapter = new LiveCommentAdapter(mActivity, null);
        recyclerview.setAdapter(mAdapter);
        linearLayoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
        if (linearLayoutManager != null) {
            linearLayoutManager.setStackFromEnd(true);
        }

        mAdapter.getData().addAll(DataTemp.getCommentData());
        mAdapter.notifyDataSetChanged();
        initEditText();
    }

    private void initEditText() {

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(s.toString())) {
                    send.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                } else {
                    send.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                }
            }
        });
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                attemptSendMessage();
            }
            return true;
        });
    }

    @OnClick({R.id.delete, R.id.send, R.id.ivPackage, R.id.layShare, R.id.layTools})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.layShare:  // 分享
                DialogResolver.createForwardDialog(mActivity, (dialog, itemView) -> {
                    int index = (int) itemView.getTag();
                    switch (index) {
                        case 0:  // 小程序
                            break;
                        case 1:  // 朋友圈
                            break;
                        case 2:  // 微信好友
                            break;
                    }
                    dialog.dismiss();
                }).show();
                break;
            case R.id.layTools:
                uiEventListener.uiOpenToolsPanel();
                break;
            case R.id.ivPackage:
                uiEventListener.uiOpenShoppingPanel();
                break;
            case R.id.delete:
                EventBus.getDefault().post(new MessageEvent(MessageEvent.LIVE_CLICK_EXIST));
                break;
            case R.id.send:
                attemptSendMessage();
                break;
        }
    }

    private void attemptSendMessage() {
        String message = etMessage.getText().toString();
        if (TextUtils.isEmpty(message)) return;
        if (uiEventListener != null) uiEventListener.reqSendMessage(message);
        etMessage.setText("");
        uiAppendMessage(message);
    }

    public void uiAppendMessage(String message) {
        recyclerview.post(() -> {
            LiveComment comment = new LiveComment();
            comment.setContent(message);
            comment.setType(2);
            comment.setUserName("竹林小溪");
            mAdapter.getData().add(comment);
            mAdapter.notifyDataSetChanged();
            recyclerviewScrollToPositionWithOffset();
        });
    }

    // 聊天列表滚动到最后
    public void recyclerviewScrollToPositionWithOffset() {
        linearLayoutManager.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, Integer.MIN_VALUE);
    }

    private UIEventListener uiEventListener;

    public void setUIEventListener(UIEventListener listener) {
        this.uiEventListener = listener;
    }

    public interface UIEventListener {
        void reqSendMessage(String message);

        void uiOpenShoppingPanel();

        void uiOpenToolsPanel();
    }
}

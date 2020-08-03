package com.wj.work.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.wj.work.R;
import com.wj.work.widget.part.IndexAccepter;

import java.util.List;

public class LiveTypeDialog extends Dialog  {

    private Context context;

    public LiveTypeDialog(@NonNull Context context,List<String> data) {
        super(context, R.style.bottom_dialog);
        this.context = context;
        setContentData(data);
    }

    LinearLayoutCompat parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_live_type);
        initView();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Window window=getWindow();
        if (window==null)return;
        WindowManager.LayoutParams params =window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM);
    }

    public void initView() {
        parent = findViewById(R.id.parent);
        if (data!=null)setContentData(data);
    }

    private IndexAccepter accepter;

    public void setPositionAccepter(IndexAccepter accepter) {
        this.accepter = accepter;
    }

    LinearLayoutCompat.LayoutParams params=new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

    private List<String> data ;

    public void setContentData(List<String> data) {
        this.data=data;
        if (parent==null)return;
        parent.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            View item = View.inflate(context, R.layout.dialog_item_live_type, null);
            ((TextView)item.findViewById(R.id.name)).setText(data.get(i));
            parent.addView(item,params);
            item.setOnClickListener(onclick);
            item.setTag(R.id.TAG_POSITION,i);
        }
    }

    private View.OnClickListener onclick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag(R.id.TAG_POSITION);
            if (accepter!=null)accepter.accept(position);
            dismiss();
        }
    };
}

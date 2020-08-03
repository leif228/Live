package com.wj.work.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.wj.work.widget.part.OutsideClickListener;

public class TLinearLayoutCompat extends LinearLayoutCompat {

    OutsideClickListener listener;

    public TLinearLayoutCompat(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View child1 = getChildAt(0);
        View child2 = getChildAt(1);
        boolean outIntercept=false;
        if (child1!=null&&child2!=null&&child2.getVisibility()==View.VISIBLE&&event.getX()<child2.getWidth()&&listener!=null){
            listener.onTouchOutSide();
            outIntercept=true;
        }
        super.onTouchEvent(event);
        return canIntercept||outIntercept;
    }

    private boolean canIntercept = false;

    public void canInterceptTouch(boolean canIntercept) {
        this.canIntercept = canIntercept;
    }

    public void setListener(OutsideClickListener listener) {
        this.listener = listener;
    }
}

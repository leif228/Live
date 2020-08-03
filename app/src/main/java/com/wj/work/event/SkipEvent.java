package com.wj.work.event;

import android.content.Intent;

/**
 * ActivitySkipEvent
 * 2020/4/3 14:31
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class SkipEvent {

    public static final int NONE=0x000;
    public static final int RIGHT_IN=0x001;
    public static final int BOTTOM_IN=0x002;

    private Intent intent ;
    private int anim;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getAnim() {
        return anim;
    }

    public void setAnim(int anim) {
        this.anim = anim;
    }
}

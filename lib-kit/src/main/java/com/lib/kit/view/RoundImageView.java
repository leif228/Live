package com.lib.kit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import com.lib.kit.R;
import com.lib.kit.utils.LL;

/**
 * RoundImagView
 * TODO 带圆角的 ImageView
 * XML 中 radus 指定 圆角大小
 * 2019/11/4 14:14
 * wj
 * @author Ly
 * @wj
 */
public class RoundImageView extends AppCompatImageView {

    private float width, height, radus;
    private Path path = null;

    private float topLeftRadus,topRightRadus,bottomLeftRadus,bottomRightRadus;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoundImageView, 0, 0);

        topLeftRadus=a.getDimension(R.styleable.RoundImageView_topLeftRadus, 0);
        topRightRadus=a.getDimension(R.styleable.RoundImageView_topRightRadus, 0);
        bottomLeftRadus=a.getDimension(R.styleable.RoundImageView_bottomLeftRadus, 0);
        bottomRightRadus=a.getDimension(R.styleable.RoundImageView_bottomRightRadus, 0);

        radus = (a.getDimension(R.styleable.RoundImageView_radus, 0));
        if (radus>0){
            topLeftRadus=radus;
            topRightRadus=radus;
            bottomLeftRadus=radus;
            bottomRightRadus=radus;
        }
        a.recycle();
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean isFitstLayout=true;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        if (isFitstLayout){
            if (radus>width/2) radus=width/2;
            if (radus>width/2) radus=width/2;
            setPath();
            isFitstLayout=false;
        }
    }

    private void setPath() {

        if (path==null)path=new Path();
        path.moveTo(topLeftRadus, 0);
        path.lineTo(width - topRightRadus, 0);
        path.quadTo(width, 0, width, topRightRadus);
        path.lineTo(width, height - bottomRightRadus);
        path.quadTo(width, height, width - bottomRightRadus, height);
        path.lineTo(bottomLeftRadus, height);
        path.quadTo(0, height, 0, height - bottomLeftRadus);
        path.lineTo(0, topLeftRadus);
        path.quadTo(0, 0, topLeftRadus, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (path!=null)canvas.clipPath(path);
        super.onDraw(canvas);
    }

}

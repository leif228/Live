package com.lib.kit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.lib.kit.R;


/**
 * 带图片和文字的 button
 * 只设置 normal 点击不会变
 */
public class LimitButton extends LinearLayoutCompat {

	public LimitButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_initViews(context);
	}

	public LimitButton(Context context) {
		super(context);
		_initViews(context);
	}

	public LimitButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		_getAttribute(context, attrs);
		_setAttribute();
		_initViews(context);
	}

	private ImageView iv = null;
	private TextView tv = null;
	private float density;

	private void _initViews(Context context) {
		_addViews(context);
	}
	
	/**
	 * 默认
	 */
	private final int DEF_IV_LENGH_DP=24;
	private float height_iv = 54;
	private float width_iv = 54;
	private int space = 0;

	private float paddingLeft = 0;
	private float paddingRight = 0;
	private float paddingTop = 0;
	private float paddingBottom = 0;
	private String mText = null;
	private float text_Size = 20;
	private int normalDrawable = 0;
	private int checkedDrawable = 0;
	/** 默认 left */
	private int gravity = 0;
	/** 是否文字在前面 */
	private boolean isTextFront=true;
	
	
	private int normal_text_Color = 0;
	private int press_text_Color = 0;
	
	/**
	 * 添加 views
	 */
	private void _addViews(Context context) {
		if (isTextFront) {
			_addTextView(context, space);
			_addImageView(context, 0);
		} else {
			_addImageView(context, space);
			_addTextView(context, 0);
		}
	}

	private void _addImageView(Context context, int marginRight) {
		if (normalDrawable != 0) {
			iv = new ImageView(context);
			LayoutParams p = new LayoutParams(
					(int) width_iv, (int) height_iv);
			p.rightMargin = marginRight;
			addView(iv, p);
		}
	}

	private void _addTextView(Context context, int marginRight) {
		tv = new TextView(context);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_Size);
		tv.setTextColor(normal_text_Color);
		if (mText != null) {
			tv.setText(mText);
		}
		LayoutParams p = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		p.rightMargin = marginRight;
		addView(tv, p);
	}

	private void _getAttribute(Context context, AttributeSet attrs) {
		
		density = context.getApplicationContext().getResources()
				.getDisplayMetrics().density;
		
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.LimitButton, 0, 0);
		paddingLeft = a.getDimension(R.styleable.LimitButton_PaddingLeft, 0);
		paddingRight = a.getDimension(R.styleable.LimitButton_PaddingRight, 0);
		paddingTop = a.getDimension(R.styleable.LimitButton_PaddingTop, 0);
		paddingBottom = a
				.getDimension(R.styleable.LimitButton_PaddingBottom, 0);
		mText = a.getString(R.styleable.LimitButton_Text);
		text_Size = a.getDimension(R.styleable.LimitButton_TextSize, 20);
		press_text_Color = a
				.getColor(R.styleable.LimitButton_PressTextColor, 0);
		normal_text_Color = a
				.getColor(R.styleable.LimitButton_NormalTextColor, getResources().getColor(R.color.kit_black));
		
		space = (int) a.getDimension(R.styleable.LimitButton_Space, 0);
		width_iv = a.getDimension(R.styleable.LimitButton_Iv_Width, DEF_IV_LENGH_DP*density);
		height_iv = a.getDimension(R.styleable.LimitButton_Iv_Height, DEF_IV_LENGH_DP*density);

		isTextFront = a.getBoolean(R.styleable.LimitButton_TextFront,true);
		String gravity_str = a.getString(R.styleable.LimitButton_Gravity);

		if (gravity_str != null) {
			gravity = Integer.parseInt(gravity_str);
		}

		setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight,
				(int) paddingBottom);
		normalDrawable = a.getResourceId(R.styleable.LimitButton_NormalSrc,0);
		checkedDrawable = a.getResourceId(R.styleable.LimitButton_PressSrc,0);
		a.recycle();
	}

	private void _setAttribute() {
		_setGravity();
		setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight,
				(int) paddingBottom);
	}

	private void _setGravity() {
		switch (gravity) {
		case 0:
			setGravity(Gravity.CENTER);
			break;
		case 1:
			setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			break;
		case 2:
			setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			break;
		}
	}

	@Override
	protected void drawableStateChanged() {
		if (isPressed()) {
			if (normalDrawable != 0&&iv!=null) {
				if (checkedDrawable != 0) {
					iv.setImageResource(checkedDrawable);
				} else {
					iv.setImageResource(normalDrawable);
				}
			}
			
			if (press_text_Color!=0&&tv!=null) {
				tv.setTextColor(press_text_Color);
			}
		} else {
			if (normalDrawable != 0&&iv!=null) {
				iv.setImageResource(normalDrawable);
			}
			if (normal_text_Color!=0&&tv!=null) {
				tv.setTextColor(normal_text_Color);
			}
		}
		super.drawableStateChanged();
	}
	
	public void setText(String text){
		if (text!=null&&!"".equals(text)) {
			mText=text;
			tv.setText(text);
		}
	}

	public void setImageResource(int imgRsc){
		if (iv==null||imgRsc==0)return;
		iv.setImageResource(imgRsc);
		this.normalDrawable=imgRsc;
		this.checkedDrawable=imgRsc;
	}
}

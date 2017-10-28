package com.huntergao.dailyweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.huntergao.dailyweather.R;

/**
 * 可以设置指定字体的TextView
 * @author HG
 *
 */
public class TypefacedTextView extends AppCompatTextView {

	public TypefacedTextView(Context context) {
		this(context, null);
	}

	public TypefacedTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TypefacedTextView(Context context, AttributeSet attrs,
                             int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.TypefacedTextView, defStyle, 0);
		String typeface = a.getString(R.styleable.TypefacedTextView_typeface);
//		if (!TextUtils.isEmpty(typeface)) {
//			Typeface face = Typeface.createFromAsset(getContext().getAssets(),
//					"fonts/RobotoThin.ttf");
//			if (face != null) {
//				setTypeface(face);
//			}
//		}
		a.recycle();
	}
}
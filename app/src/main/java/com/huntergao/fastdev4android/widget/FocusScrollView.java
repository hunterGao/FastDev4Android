package com.huntergao.fastdev4android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by HunterGao on 16/2/1.
 */
public class FocusScrollView extends FrameLayout {

    private OverScroller mOverScroller;
    private GestureDetector mGestureDetector;

    private int mColumns = -1;

    public FocusScrollView(Context context) {
        super(context);
    }

    public FocusScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mOverScroller = new OverScroller(context, new AccelerateDecelerateInterpolator());
        setChildrenDrawingCacheEnabled(true);
        this.mGestureDetector = new GestureDetector(context, new FocusGestureDetector());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        this.mGestureDetector.onTouchEvent(ev);
        switch (ev.getAction()){
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        if(childCount == 0){
            return;
        }
        int columns = 1 ;
        if(-1 != mColumns){
            columns = mColumns;
        }
        int rows;

        if(childCount % columns == 0){
            rows = childCount / columns;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if(childCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

    }

    class FocusGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}

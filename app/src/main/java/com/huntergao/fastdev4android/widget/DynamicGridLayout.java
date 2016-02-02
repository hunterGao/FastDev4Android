package com.huntergao.fastdev4android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.huntergao.fastdev4android.utils.Log;

/**
 * 支持动态添加View的格子布局
 * 同时支持通过Adapter添加View
 * Created by HunterGao on 16/1/22.
 */
public class DynamicGridLayout extends ViewGroup {

    /**格子之间的水平间距*/
    private int horizon_margin = 10;
    /**格子之间的垂直间距*/
    private int vertical_margin = 10;

    private int rows = 2;
    private int colums;

    private int childCount;
    private int mMaxChildWidth;
    private int mMaxChildHeight;
    public DynamicGridLayout(Context context) {
        super(context, null);
    }

    public DynamicGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DynamicGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxChildWidth = 0;
        mMaxChildHeight = 0;

        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.UNSPECIFIED);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED);

        childCount = getChildCount();
        if (childCount == 0) {
            super.onMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
            mMaxChildHeight = Math.max(mMaxChildHeight, child.getMeasuredHeight());
        }
        setMeasuredDimension(resolveSize(mMaxChildWidth, widthMeasureSpec),
                resolveSize(mMaxChildHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int height = b - t;// 布局区域高度
        int width = r - l;// 布局区域宽度
        if (childCount == 0)
            return;
        colums = getChildCount()/2;
        Log.d("Hunter", colums+"");
        int gridW = 0;// 格子宽度
        int gridH = 0;// 格子高度
        int left;// 格子左边位置
        int top = 0;//格子的顶部位置

        for (int i = 0; i < rows; i++) {// 遍历行
            for (int j = 0; j < colums; j++) {// 遍历每一行的元素
                View child = this.getChildAt(i * colums + j);
                if (child == null)
                    return;
                // 如果当前布局宽度和测量宽度不一样，就直接用当前布局的宽度重新测量
                gridW = child.getMeasuredWidth();
                gridH = child.getMeasuredHeight();
//                child.measure(MeasureSpec.makeMeasureSpec(gridW, MeasureSpec.EXACTLY),
//                        MeasureSpec.makeMeasureSpec(gridH, MeasureSpec.EXACTLY));
                Log.d("Hunter", "gridH:"+gridH+" gridW:"+gridW);
                left = j * gridW + j * horizon_margin;
                child.layout(left, top, left + gridW, top + gridH);
            }
            top += gridH + horizon_margin;
        }
    }
}

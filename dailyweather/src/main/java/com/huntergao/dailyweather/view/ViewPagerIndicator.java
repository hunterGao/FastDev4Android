package com.huntergao.dailyweather.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.huntergao.dailyweather.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by HG on 2017/11/1.
 */

public class ViewPagerIndicator extends View implements ViewPager.OnPageChangeListener  {

    private float mRadius;
    private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mBitmapPaint = new Paint(ANTI_ALIAS_FLAG);
    private ViewPager mViewPager;
    private int mCurrentPage;
    private int mSnapPage;
    private float mPageOffset;
    private int mScrollState;
    private int mOrientation;
    private boolean mCentered;
    private boolean mSnap;
    private Bitmap mBitmap;

    private ViewPager.OnPageChangeListener mListener;

    public ViewPagerIndicator(Context context) {
        super(context);
        init();
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCentered = true;
        mOrientation = LinearLayout.HORIZONTAL;
        mPaintPageFill.setStyle(Paint.Style.FILL);
        mPaintPageFill.setColor(getResources().getColor(R.color.default_circle_indicator_page_color));
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setColor(getResources().getColor(R.color.default_circle_indicator_stroke_color));
        mPaintStroke.setStrokeWidth(getResources().getDimension(R.dimen.default_circle_indicator_stroke_width));
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(getResources().getColor(R.color.default_circle_indicator_fill_color));
        mRadius = getResources().getDimension(R.dimen.default_circle_indicator_radius);
        mSnap = false;
        mBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.current_loc_active_26x26)).getBitmap();
        mBitmapPaint.setFilterBitmap(true);
        mBitmapPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        if (mOrientation == HORIZONTAL) {
            longSize = getWidth();
            longPaddingBefore = getPaddingLeft();
            longPaddingAfter = getPaddingRight();
            shortPaddingBefore = getPaddingTop();
        } else {
            longSize = getHeight();
            longPaddingBefore = getPaddingTop();
            longPaddingAfter = getPaddingBottom();
            shortPaddingBefore = getPaddingLeft();
        }

        final float threeRadius = mRadius * 5;
        final float shortOffset = shortPaddingBefore + mRadius;
        float longOffset = longPaddingBefore + mRadius;
        if (mCentered) {
            longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);
        }

        float dX;
        float dY;

        float pageFillRadius = mRadius;
        if (mPaintStroke.getStrokeWidth() > 0) {
            pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
        }

        //Draw stroked circles
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * threeRadius);
            if (mOrientation == HORIZONTAL) {
                dX = drawLong;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = drawLong;
            }

            if (iLoop == 0) {
                Rect dst = new Rect((int)(dX - 2 * mRadius), (int)(dY - 2 * mRadius),
                        (int)(dX + mRadius * 2), (int)(dY + mRadius * 2));
                mBitmapPaint.setAlpha(100);
                canvas.drawBitmap(mBitmap, null, dst, mBitmapPaint);
            }

            // Only paint fill if not completely transparent
//            if (mPaintPageFill.getAlpha() > 0) {
//                canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
//            }

            // Only paint stroke if a stroke width was non-zero
            if (pageFillRadius != mRadius && iLoop != 0) {
                canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
            }
        }


        //Draw the filled circle according to the current scroll
        float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
        if (!mSnap) {
            cx += mPageOffset * threeRadius;
        }
        if (mOrientation == HORIZONTAL) {
            dX = longOffset + cx;
            dY = shortOffset;
        } else {
            dX = shortOffset;
            dY = longOffset + cx;
        }
        if (mCurrentPage != 0) {
            canvas.drawCircle(dX, dY, mRadius, mPaintFill);
        } else {
            Rect dst = new Rect((int)(dX - 2 * mRadius), (int)(dY - 2 * mRadius),
                    (int)(dX + mRadius * 2), (int)(dY + mRadius * 2));
            mBitmapPaint.setAlpha(255);
            canvas.drawBitmap(mBitmap, null, dst, mBitmapPaint);
        }
    }

    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec
     *            A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureLong(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = mViewPager.getAdapter().getCount();
            result = (int)(getPaddingLeft() + getPaddingRight()
                    + (count * 3 * mRadius) + (count - 1) * mRadius * 3 + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec
     *            A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int)(2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPage = position;
        mPageOffset = positionOffset;
        invalidate();

        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mSnapPage = position;
            invalidate();
        }

        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }
}

package com.huntergao.fastdev4android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.huntergao.fastdev4android.R;

import java.util.TimeZone;

/**
 * 带有秒针的钟表
 * Created by HunterGao on 15/12/7.
 */
public class SecondClock extends View {

    private static final int SECONDS_FLAG = 0;

    private Time mCalendar;

    private Drawable mHourHand; //时针的图片
    private Drawable mMinuteHand; //分针的图片
    private Drawable mSecondHand; //秒针的图片
    private Drawable mDial;

    private int mDialWidth;
    private int mDialHeight;

    private boolean mAttached;

    private Message secondsMsg;
    private float mSeconds;
    private float mMinutes;
    private float mHour;
    private boolean mChanged;

    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SECONDS_FLAG:
                    //重新获取时间
                    onTimeChanged();
                    //重绘界面
                    invalidate();
                    break;
            }
        }
    };

    public SecondClock(Context context) {
        this(context, null);
    }

    public SecondClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources r = context.getResources();
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SecondClock);

        mDial = a.getDrawable(R.styleable.SecondClock_dial);
        if (mDial == null) {
            mDial = r.getDrawable(R.drawable.clock_dial);
        }

        mHourHand = a.getDrawable(R.styleable.SecondClock_hand_hour);
        if (mHourHand == null) {
            mHourHand = r.getDrawable(R.drawable.clock_hand_hour);
        }

        mMinuteHand = a.getDrawable(R.styleable.SecondClock_hand_minute);
        if (mMinuteHand == null) {
            mMinuteHand = r.getDrawable(R.drawable.clock_hand_minute);
        }

        mCalendar = new Time();

        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            /*
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            // OK, this is gross but needed. This class is supported by the
            // remote views machanism and as a part of that the remote views
            // can be inflated by a context for another user without the app
            // having interact users permission - just for loading resources.
            // For exmaple, when adding widgets from a user profile to the
            // home screen. Therefore, we register the receiver as the current
            // user not the one the context is for.
            getContext().registerReceiverAsUser(mIntentReceiver,
                    android.os.Process.myUserHandle(), filter, null, mHandler);
                    */
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = new Time();

        // Make sure we update to the current time
        onTimeChanged();
        initSecondsThread();
    }

    private void initSecondsThread() {
        secondsMsg = mHandler.obtainMessage(SECONDS_FLAG);
        new Thread() {
            @Override
            public void run() {
                while (mAttached) {
                    // 如果这个消息不重新获取的话，
                    // 会抛一个this message is already in use 的异常
                    secondsMsg = mHandler.obtainMessage(SECONDS_FLAG);
                    // /end
                    mHandler.sendMessage(secondsMsg);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
//            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;
        //绘制表盘
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        //绘制时针
        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        //绘制分针
        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(mSeconds / 60.0f * 360.0f, x, y);
        final Drawable secondHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        secondHand.draw(canvas);
        canvas.restore();

        if (scaled) {
            canvas.restore();
        }
    }

    /**
     * 时间改变
     */
    private void onTimeChanged() {
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;

        mSeconds = second;
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;

        updateContentDescription(mCalendar);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged();
            invalidate();
        }
    };

    private void updateContentDescription(Time time) {
        final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
        String contentDescription = DateUtils.formatDateTime(getContext(),
                time.toMillis(false), flags);
        setContentDescription(contentDescription);
    }
}

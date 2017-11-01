package com.huntergao.dailyweather.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huntergao.dailyweather.R;

import java.util.Locale;

public class CountDownView extends FrameLayout {

	private static final int SET_TIMER_TEXT = 1;
	private TextView secondsView;
	private TextView countdownTitle;
	private Button button;
	private int mRemainingSecs = 0;
	private Animation mCountDownAnim;
	private int errorType;
//	private SoundPool mSoundPool;
//	private int mBeepTwice;
//	private int mBeepOnce;
//	private boolean mPlaySound;
	private OnErrorClickListener errorClickListener;
	private final Handler mHandler = new MainHandler();

	public CountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCountDownAnim = AnimationUtils.loadAnimation(context,
				R.anim.count_down_exit);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.count_down_to_location,this, true);
		setBackgroundColor(getResources().getColor(R.color.common_gray_bg));
		// Load the beeps
//		mSoundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
//		mBeepOnce = mSoundPool.load(context, R.raw.beep_once, 1);
//		mBeepTwice = mSoundPool.load(context, R.raw.beep_twice, 1);
	}

	public boolean isCountingDown() {
		return mRemainingSecs > 0;
	};

	private void remainingSecondsChanged(int newVal) {
		mRemainingSecs = newVal;
		Locale locale = getResources().getConfiguration().locale;
		String localizedValue = String.format(locale, "%d", newVal);
		secondsView.setText(localizedValue);
		mCountDownAnim.reset();
		secondsView.clearAnimation();
		secondsView.startAnimation(mCountDownAnim);

			// Play sound effect for the last 3 seconds of the countdown
//			if (mPlaySound) {
//				if (newVal == 1) {
//					mSoundPool.play(mBeepTwice, 1.0f, 1.0f, 0, 0, 1.0f);
//				} else if (newVal <= 3) {
//					mSoundPool.play(mBeepOnce, 1.0f, 1.0f, 0, 0, 1.0f);
//				}
//			}
			// Schedule the next remainingSecondsChanged() call in 1 second
			mHandler.sendEmptyMessageDelayed(SET_TIMER_TEXT, 1000);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		secondsView = (TextView) findViewById(R.id.remaining_seconds);
		countdownTitle = (TextView) findViewById(R.id.count_down_title);
		button = (Button) findViewById(R.id.cancel_btn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (errorType == 0) {
					mRemainingSecs = 0;
					mHandler.removeMessages(SET_TIMER_TEXT);
					setVisibility(INVISIBLE);
				} else if (errorType == 1) {
					if (errorClickListener != null) {
						errorClickListener.onErrorClick(errorType);
					}
				}
			}
		});
	}

	public void startCountDown(int sec, boolean playSound) {
		if (sec <= 0) {
			return;
		}

		setVisibility(View.VISIBLE);
//		mPlaySound = playSound;
		remainingSecondsChanged(sec);
	}

	public void startCountDown(int sec) {
		if (sec <= 0) {
			return;
		}

		setVisibility(View.VISIBLE);
		secondsView.setVisibility(VISIBLE);
		remainingSecondsChanged(sec);
	}

	public void cancelCountDown(int type) {
		errorType = type;
		switch (type) {
			case 0:
				if (mRemainingSecs > 0) {
					mRemainingSecs = 0;
				}
				if (mHandler.hasMessages(SET_TIMER_TEXT)) {
					mHandler.removeMessages(SET_TIMER_TEXT);
				}
				setVisibility(INVISIBLE);
				break;
			case 1:
				if (mHandler.hasMessages(SET_TIMER_TEXT)) {
					mHandler.removeMessages(SET_TIMER_TEXT);
				}
				countdownTitle.setText("定位失败");
				button.setText("手动添加城市");
				secondsView.setVisibility(INVISIBLE);
				break;
			case 2:
				countdownTitle.setText("请求权限失败");
				button.setText("手动添加城市");
				secondsView.setVisibility(INVISIBLE);
				break;

		}
	}

	public void setErrorClickListener(OnErrorClickListener listener) {
		errorClickListener = listener;
	}

	public interface OnErrorClickListener {
		void onErrorClick(int type);
	}

	private class MainHandler extends Handler {
		@Override
		public void handleMessage(Message message) {
			if (message.what == SET_TIMER_TEXT) {
				remainingSecondsChanged(mRemainingSecs - 1);
			}
		}
	}
}
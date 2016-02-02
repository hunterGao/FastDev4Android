package com.huntergao.fastdev4android;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by HunterGao on 16/1/19.
 */
public class BaseActivity extends Activity {
    protected BaseApplication mBaseApp = null;
    /**窗口管理器*/
    private WindowManager mWindowManager = null;
    /**夜间模式,是在界面增加了一层View*/
    private View mNightView = null;
    /**是否增添了夜间模式View*/
    private boolean isAddedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBaseApp = (BaseApplication) getApplication();
        if(mBaseApp.isNightMode()){
            setTheme(R.style.AppTheme_night);
        } else{
            setTheme(R.style.AppTheme_day);
        }
        super.onCreate(savedInstanceState);
        isAddedView = false;
        if(mBaseApp.isNightMode()){
            initNightView();
            mNightView.setBackgroundResource(R.color.night_mask);
        }
    }

    @Override
    protected void onDestroy() {
        mBaseApp = null;
        if(isAddedView){
            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
        super.onDestroy();
    }

    /**
     * 切换至日间模式
     */
    public void changeToDay(){
        if(null == mNightView || !mBaseApp.isNightMode()){
            Toast.makeText(this, "day", Toast.LENGTH_SHORT).show();
            return;
        }
        mBaseApp.setNightMode(false);
        mNightView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    /**
     * 切换至夜间模式
     */
    public void changeToNight(){
        if (mBaseApp.isNightMode()){
            Toast.makeText(this, "night", Toast.LENGTH_SHORT).show();
            return;
        }
        if(null == mNightView){
            initNightView();
        }
        mBaseApp.setNightMode(true);
        mNightView.setBackgroundColor(getResources().getColor(R.color.night_mask));
    }

    private void initNightView() {
        if (isAddedView)
            return;
        LayoutParams nightViewParam = new LayoutParams(
                LayoutParams.TYPE_APPLICATION,
                LayoutParams.FLAG_NOT_TOUCHABLE | LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, nightViewParam);
        isAddedView = true;
    }

}

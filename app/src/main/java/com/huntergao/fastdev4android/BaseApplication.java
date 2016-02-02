package com.huntergao.fastdev4android;

import android.app.Application;

/**
 * Created by HunterGao on 16/1/13.
 */
public class BaseApplication extends Application {

    /**是否处于夜间模式*/
    private boolean isNightMode;

    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }
}

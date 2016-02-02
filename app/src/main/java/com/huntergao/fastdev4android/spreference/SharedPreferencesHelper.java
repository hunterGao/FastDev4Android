package com.huntergao.fastdev4android.spreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.huntergao.fastdev4android.utils.StringUtils;

/**
 * 采用单例模式封装SharedPreferences的基本方法
 * Created by HunterGao on 15/12/4.
 */
public class SharedPreferencesHelper {

    private final static String SHARED_PATH = "shared_preferences";
    private SharedPreferences sp;
    private static SharedPreferencesHelper instance;

    public static SharedPreferencesHelper getInstance(Context context){
        if(null == instance && null != context){
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    private SharedPreferencesHelper(Context context){
        sp = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
    }

    public void putStringValue(String key, String value){
        if(!StringUtils.isEmpty(key)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public String getStringValue(String key){
        if(!StringUtils.isEmpty(key)){
            return sp.getString(key, null);
        }
        return null;
    }

    public void putIntValue(String key, int value){
        if(!StringUtils.isEmpty(key)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public int getIntValue(String key){
        if(!StringUtils.isEmpty(key)){
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public void putLongValue(String key, long value){
        if(!StringUtils.isEmpty(key)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public long getLongValue(String key){
        if(!StringUtils.isEmpty(key)){
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public void putFloatValue(String key, float value){
        if(!StringUtils.isEmpty(key)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    public float getFloatValue(String key){
        if(!StringUtils.isEmpty(key)){
            return sp.getFloat(key, 0);
        }
        return 0;
    }

    public void putBooleanValue(String key, boolean value){
        if(!StringUtils.isEmpty(key)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public boolean getBooleanValue(String key){
        if(!StringUtils.isEmpty(key)){
            return sp.getBoolean(key, false);
        }
        return false;
    }

}

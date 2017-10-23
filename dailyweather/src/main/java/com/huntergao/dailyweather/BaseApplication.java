package com.huntergao.dailyweather;

import android.app.Application;

import com.huntergao.dailyweather.xml.model.TodayWeatherInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HunterGao on 16/1/20.
 */
public class BaseApplication extends Application{
    /**
     * 用于存放城市名称和城市天气情况的键值对
     */
    public static Map<String, TodayWeatherInfo> mSelectWeatherInfoMap = new HashMap<String, TodayWeatherInfo>();

    /**
     * 用于获取数据的地址
     */
    public static final String ADDRESS = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";


    @Override
    public void onCreate() {
        super.onCreate();
    }
}

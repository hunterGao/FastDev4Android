package com.huntergao.dailyweather;

import android.app.Application;
import android.content.Context;

import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.weathershow.MainActivity;
import com.huntergao.dailyweather.xml.model.WeatherInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by HunterGao on 16/1/20.
 */
public class WeatherApplication extends Application{

    public static Map<City, WeatherInfo> cityWeatherInfoMap = new HashMap<>();
    public static List<City> cityList = new LinkedList<>();
    public static Map<City, Integer> cityPositionMap = new HashMap<>();

    /**
     * 用于获取数据的地址
     */
    public static final String ADDRESS = "http://wthrcdn.etouch.cn/WeatherApi?citykey=";

    public static void jumpMainActivity(Context context, City city) {
        if (context == null || city == null) {
            return;
        }
        if (cityList.contains(city)) {
            int position = cityPositionMap.get(city);
            MainActivity.start(context, position);
        } else {
            cityList.add(city);
            cityPositionMap.put(city, cityList.size() - 1);
            MainActivity.start(context, cityList.size() - 1);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

package com.huntergao.dailyweather.weathershow;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.util.LunarCalendar;
import com.huntergao.dailyweather.util.LunarCalendarConvertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2017/10/19.
 */

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private static final int TODAY_WEATHER_TYPE = 0;
    private static final int FORECAST_TYPE = 1;
    private static final int WEATHER_DETAILS_TYPE = 2;
    private static final int ENVIRONMENT_TYPE = 3;
    private static final int INDEX_TYPE = 4;

    private List<Integer> weatherTypes;
    private LayoutInflater layoutInflater;
    private LunarCalendar lunarCalendar;

    public WeatherRecyclerViewAdapter(Context context) {
        weatherTypes = new ArrayList<Integer>();
        weatherTypes.add(TODAY_WEATHER_TYPE);
        weatherTypes.add(FORECAST_TYPE);
        weatherTypes.add(WEATHER_DETAILS_TYPE);
        weatherTypes.add(ENVIRONMENT_TYPE);
        weatherTypes.add(INDEX_TYPE);
        layoutInflater = LayoutInflater.from(context);
        lunarCalendar = new LunarCalendar(context);
        Time time = new Time();
        time.set(System.currentTimeMillis());
        LunarCalendarConvertUtil.parseLunarCalendar(time.year, time.month,
                time.monthDay, lunarCalendar);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " + viewType);
        View convertView = null;
        switch (viewType) {
            case TODAY_WEATHER_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_today, parent, false);
                break;
            case FORECAST_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_forecast, parent, false);
                break;
            case WEATHER_DETAILS_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_details, parent, false);
                break;
            case ENVIRONMENT_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_aqi, parent, false);
                break;
            case INDEX_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_index, parent, false);
                break;
        }
        if (convertView != null) {
            return new ViewHolder(convertView, viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return weatherTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return weatherTypes.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // 预报
        View forecastViewDay1;
        View forecastViewDay2;
        View forecastViewDay3;
        View forecastViewDay4;
        View forecastViewDay5;
        TextView forecastFootView;
        // 详细信息
        ImageView detailsWeatherIV;
        TextView weatherNameTV;
        TextView feelsTempTV;
        TextView humidityTV;
        TextView windTV;
        TextView windDescTV;
        TextView detailsFootTV;
        // 空气质量
        ImageView aqiIV;
        TextView aqiLevelTV;
        TextView aqiTV;
        TextView pm25TV;
        TextView aqiDescTV;
        TextView aqiFootTV;
        // 指数
        View windIndexView;
        View uaIndexView;// 紫外线指数
        View clotheIndexView;
        View comfortIndexView;
        View carIndexView;
        View insolationIndexView;// 晾晒
        View sportIndexView;
        View travelIndexDivider;
        View travelIndexView;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case TODAY_WEATHER_TYPE:
                    break;
                case FORECAST_TYPE:
                    break;
                case WEATHER_DETAILS_TYPE:
                    break;
                case ENVIRONMENT_TYPE:
                    break;
                case INDEX_TYPE:
                    break;
            }
        }
    }
}

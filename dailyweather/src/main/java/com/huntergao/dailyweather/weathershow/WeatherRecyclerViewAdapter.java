package com.huntergao.dailyweather.weathershow;

import android.content.Context;
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
import com.huntergao.dailyweather.util.TimeUtils;
import com.huntergao.dailyweather.util.WeatherIconUtils;
import com.huntergao.dailyweather.xml.model.ForecastWeatherInfo;
import com.huntergao.dailyweather.xml.model.WeatherInfo;
import com.huntergao.dailyweather.xml.model.WeatherEnvironment;
import com.huntergao.dailyweather.xml.model.WeatherIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG on 2017/10/19.
 */

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private static final int WEATHER_DETAILS_TYPE = 0;
    private static final int FORECAST_TYPE = 1;
    private static final int ENVIRONMENT_TYPE = 2;
    private static final int INDEX_TYPE = 3;

    private List<Integer> weatherTypes;
    private LayoutInflater layoutInflater;
    private LunarCalendar lunarCalendar;
    private String cityName;
    private WeatherInfo weatherInfo;
    private List<ForecastWeatherInfo> forecastInfoList;
    private List<WeatherIndex> weatherIndexList;
    private WeatherEnvironment environment;

    public WeatherRecyclerViewAdapter(Context context) {
        weatherTypes = new ArrayList<>();
        weatherTypes.add(WEATHER_DETAILS_TYPE);
        layoutInflater = LayoutInflater.from(context);
        lunarCalendar = new LunarCalendar(context);
        Time time = new Time();
        time.set(System.currentTimeMillis());
        LunarCalendarConvertUtil.parseLunarCalendar(time.year, time.month,
                time.monthDay, lunarCalendar);
    }

    public void setWeatherInfo(String cityName, WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
        environment = weatherInfo.getEnvironment();
        forecastInfoList = weatherInfo.getForecastList();
        weatherIndexList = weatherInfo.getIndexList();
        if (forecastInfoList != null && !forecastInfoList.isEmpty()) {
            weatherTypes.add(FORECAST_TYPE);
        }
        if (environment != null) {
            weatherTypes.add(ENVIRONMENT_TYPE);
        }
        if (weatherIndexList != null && !weatherIndexList.isEmpty()) {
            weatherTypes.add(INDEX_TYPE);
        }
        this.cityName = cityName;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " + viewType);
        View convertView = null;
        switch (viewType) {
            case WEATHER_DETAILS_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_details, parent, false);
                break;
            case FORECAST_TYPE:
                convertView = layoutInflater.inflate(R.layout.weather_forecast, parent, false);
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
        int type = getItemViewType(position);
        switch (type) {
            case WEATHER_DETAILS_TYPE:
                bindDetailsView(holder);
                break;
            case FORECAST_TYPE:
                bindForecastView(holder);
                break;
            case ENVIRONMENT_TYPE:
                bindAqiView(holder);
                break;
            case INDEX_TYPE:
                bindIndexView(holder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weatherTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return weatherTypes.get(position);
    }

    private void bindForecastView(ViewHolder holder) {
        if (forecastInfoList == null || forecastInfoList.size() < 0)
            return;
        int sunrise = Integer.valueOf(weatherInfo.getSunrise().split(":")[0]);
        int sunset = Integer.valueOf(weatherInfo.getSunset().split(":")[0]);
        // 天气图标
        ImageView iconDay1 = (ImageView) holder.forecastViewDay1
                .findViewById(R.id.forecast_icon);
        ImageView iconDay2 = (ImageView) holder.forecastViewDay2
                .findViewById(R.id.forecast_icon);
        ImageView iconDay3 = (ImageView) holder.forecastViewDay3
                .findViewById(R.id.forecast_icon);
        ImageView iconDay4 = (ImageView) holder.forecastViewDay4
                .findViewById(R.id.forecast_icon);
        ImageView iconDay5 = (ImageView) holder.forecastViewDay5
                .findViewById(R.id.forecast_icon);
        iconDay1.setImageResource(WeatherIconUtils.getWeatherIcon(forecastInfoList.get(0)
                .getDayWeather().getType(), sunrise, sunset));
        iconDay2.setImageResource(WeatherIconUtils.getWeatherIcon(forecastInfoList.get(1)
                .getDayWeather().getType(), sunrise, sunset));
        iconDay3.setImageResource(WeatherIconUtils.getWeatherIcon(forecastInfoList.get(2)
                .getDayWeather().getType(), sunrise, sunset));
        iconDay4.setImageResource(WeatherIconUtils.getWeatherIcon(forecastInfoList.get(3)
                .getDayWeather().getType(), sunrise, sunset));
        iconDay5.setImageResource(WeatherIconUtils.getWeatherIcon(forecastInfoList.get(4)
                .getDayWeather().getType(), sunrise, sunset));

        // 星期
        TextView weekDay1 = (TextView) holder.forecastViewDay1
                .findViewById(R.id.forecast_week_tv);
        TextView weekDay2 = (TextView) holder.forecastViewDay2
                .findViewById(R.id.forecast_week_tv);
        TextView weekDay3 = (TextView) holder.forecastViewDay3
                .findViewById(R.id.forecast_week_tv);
        TextView weekDay4 = (TextView) holder.forecastViewDay4
                .findViewById(R.id.forecast_week_tv);
        TextView weekDay5 = (TextView) holder.forecastViewDay5
                .findViewById(R.id.forecast_week_tv);
        weekDay1.setText("今天");// 从今天开始
		weekDay2.setText(TimeUtils.getWeek(1, TimeUtils.XING_QI));
		weekDay3.setText(TimeUtils.getWeek(2, TimeUtils.XING_QI));
		weekDay4.setText(TimeUtils.getWeek(3, TimeUtils.XING_QI));
		weekDay5.setText(TimeUtils.getWeek(4, TimeUtils.XING_QI));
        // 最高温
        TextView highTempDay1 = (TextView) holder.forecastViewDay1
                .findViewById(R.id.forecast_high_temp_tv);
        TextView highTempDay2 = (TextView) holder.forecastViewDay2
                .findViewById(R.id.forecast_high_temp_tv);
        TextView highTempDay3 = (TextView) holder.forecastViewDay3
                .findViewById(R.id.forecast_high_temp_tv);
        TextView highTempDay4 = (TextView) holder.forecastViewDay4
                .findViewById(R.id.forecast_high_temp_tv);
        TextView highTempDay5 = (TextView) holder.forecastViewDay5
                .findViewById(R.id.forecast_high_temp_tv);
        highTempDay1.setText(forecastInfoList.get(0).getHigh());
        highTempDay2.setText(forecastInfoList.get(1).getHigh());
        highTempDay3.setText(forecastInfoList.get(2).getHigh());
        highTempDay4.setText(forecastInfoList.get(3).getHigh());
        highTempDay5.setText(forecastInfoList.get(4).getHigh());
        // 最低温
        TextView lowTempDay1 = (TextView) holder.forecastViewDay1
                .findViewById(R.id.forecast_low_temp_tv);
        TextView lowTempDay2 = (TextView) holder.forecastViewDay2
                .findViewById(R.id.forecast_low_temp_tv);
        TextView lowTempDay3 = (TextView) holder.forecastViewDay3
                .findViewById(R.id.forecast_low_temp_tv);
        TextView lowTempDay4 = (TextView) holder.forecastViewDay4
                .findViewById(R.id.forecast_low_temp_tv);
        TextView lowTempDay5 = (TextView) holder.forecastViewDay5
                .findViewById(R.id.forecast_low_temp_tv);
        lowTempDay1.setText(forecastInfoList.get(0).getLow());/*"°"*/
        lowTempDay2.setText(forecastInfoList.get(1).getLow());
        lowTempDay3.setText(forecastInfoList.get(2).getLow());
        lowTempDay4.setText(forecastInfoList.get(3).getLow());
        lowTempDay5.setText(forecastInfoList.get(4).getLow());

        holder.forecastFootView.setText("");
    }

    private void bindDetailsView(ViewHolder holder) {
        if(weatherInfo == null)
            return;
        String type = weatherInfo.getForecastList().get(0).getDayWeather().getType();
        int sunrise = Integer.valueOf(weatherInfo.getSunrise().split(":")[0]);
        int sunset = Integer.valueOf(weatherInfo.getSunset().split(":")[0]);
        holder.detailLabelTV.setText(cityName);
        holder.detailsWeatherIV.setImageResource(WeatherIconUtils
                .getWeatherIcon(type, sunrise, sunset));
        holder.weatherNameTV.setText(type);
        holder.feelsTempTV.setText(weatherInfo.getWendu() + "°");
        holder.humidityTV.setText(weatherInfo.getShidu());
        holder.windTV.setText(weatherInfo.getFengxiang());
        holder.windDescTV.setText(weatherInfo.getFengli());

        // holder.detailsFootTV.setText("中国天气网");
        // holder.detailsFootTV.setText(mLunarCalendar.getLunarDayInfo());
        String str[] = lunarCalendar.getLunarCalendarInfo(false);
        holder.detailsFootTV.setText(lunarCalendar
                .getLunarYear(lunarCalendar.lunarYear)
                + "("
                + lunarCalendar.animalsYear(lunarCalendar.lunarYear)
                + ")年"
                + str[1] + str[2]);
    }

    private void bindAqiView(ViewHolder holder) {
        if (environment == null) {
            return;
        }
        holder.aqiIV.setImageResource(getAqiIcon(Integer.valueOf(environment.getAqi())));
        holder.aqiLevelTV.setText(environment.getQuality());
        holder.aqiTV.setText(environment.getAqi() + "μg/m³");
        holder.pm25TV.setText(environment.getPm25() + "μg/m³");
        holder.aqiDescTV.setText(environment.getSuggest());
        holder.aqiFootTV.setText("中国环境检测总站");
    }

    private int getAqiIcon(int aqi) {
        int aqi_img;
        if (aqi > 300) {
            aqi_img = R.drawable.biz_plugin_weather_greater_300;
        } else if (aqi > 200) {
            aqi_img = R.drawable.biz_plugin_weather_201_300;
        } else if (aqi > 150) {
            aqi_img = R.drawable.biz_plugin_weather_151_200;
        } else if (aqi > 100) {
            aqi_img = R.drawable.biz_plugin_weather_101_150;
        } else if (aqi > 50) {
            aqi_img = R.drawable.biz_plugin_weather_51_100;
        } else {
            aqi_img = R.drawable.biz_plugin_weather_0_50;
        }
        return aqi_img;
    }

    private void bindIndexView(ViewHolder holder) {
        if (weatherIndexList == null || weatherIndexList.size() < 0)
            return;
        // 图标
        ImageView windIndexIcon = (ImageView) holder.windIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView uaIndexIcon = (ImageView) holder.uaIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView clotheIndexIcon = (ImageView) holder.clotheIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView comfortIndexIcon = (ImageView) holder.comfortIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView carIndexIcon = (ImageView) holder.carIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView insolationIndexIcon = (ImageView) holder.insolationIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView sportIndexIcon = (ImageView) holder.sportIndexView
                .findViewById(R.id.index_icon_iv);
        ImageView travelIndexIcon = (ImageView) holder.travelIndexView
                .findViewById(R.id.index_icon_iv);
        windIndexIcon.setImageResource(R.drawable.ic_lifeindex_wind);
        uaIndexIcon.setImageResource(R.drawable.ic_lifeindex_ultravioletrays);
        clotheIndexIcon.setImageResource(R.drawable.ic_lifeindex_clothes);
        comfortIndexIcon.setImageResource(R.drawable.ic_lifeindex_cold);
        carIndexIcon.setImageResource(R.drawable.ic_lifeindex_carwash);
        insolationIndexIcon.setImageResource(R.drawable.ic_lifeindex_makeup);
        sportIndexIcon.setImageResource(R.drawable.ic_lifeindex_sport);
        travelIndexIcon.setImageResource(R.drawable.ic_lifeindex_tour);

        // 标题
        TextView windIndexTitle = (TextView) holder.windIndexView
                .findViewById(R.id.index_title_tv);
        TextView uaIndexTitle = (TextView) holder.uaIndexView
                .findViewById(R.id.index_title_tv);
        TextView clotheIndexTitle = (TextView) holder.clotheIndexView
                .findViewById(R.id.index_title_tv);
        TextView comfortIndexTitle = (TextView) holder.comfortIndexView
                .findViewById(R.id.index_title_tv);
        TextView carIndexTitle = (TextView) holder.carIndexView
                .findViewById(R.id.index_title_tv);
        TextView insolationIndexTitle = (TextView) holder.insolationIndexView
                .findViewById(R.id.index_title_tv);
        TextView sportIndexTitle = (TextView) holder.sportIndexView
                .findViewById(R.id.index_title_tv);
        TextView travelIndexTitle = (TextView) holder.travelIndexView
                .findViewById(R.id.index_title_tv);
        windIndexTitle.setText("风力指数");
        uaIndexTitle.setText("紫外线指数");
        clotheIndexTitle.setText("穿衣指数");
        comfortIndexTitle.setText("舒服度指数");
        carIndexTitle.setText("洗车指数");
        insolationIndexTitle.setText("晾晒指数");
        sportIndexTitle.setText("运动指数");
        travelIndexTitle.setText("旅行指数");
        // 描述
        TextView windIndexDesc = (TextView) holder.windIndexView
                .findViewById(R.id.index_desc_tv);
        TextView uandexDesc = (TextView) holder.uaIndexView
                .findViewById(R.id.index_desc_tv);
        TextView clotheIndexDesc = (TextView) holder.clotheIndexView
                .findViewById(R.id.index_desc_tv);
        TextView comfortIndexDesc = (TextView) holder.comfortIndexView
                .findViewById(R.id.index_desc_tv);
        TextView carIndexDesc = (TextView) holder.carIndexView
                .findViewById(R.id.index_desc_tv);
        TextView insolationIndexDesc = (TextView) holder.insolationIndexView
                .findViewById(R.id.index_desc_tv);
        TextView sportIndexDesc = (TextView) holder.sportIndexView
                .findViewById(R.id.index_desc_tv);
        TextView travelIndexDesc = (TextView) holder.travelIndexView
                .findViewById(R.id.index_desc_tv);
        windIndexDesc.setText(weatherIndexList.get(0).getValue());
        uandexDesc.setText(weatherIndexList.get(6).getValue());
        clotheIndexDesc.setText(weatherIndexList.get(2).getValue());
        comfortIndexDesc.setText(weatherIndexList.get(1).getValue());
        carIndexDesc.setText(weatherIndexList.get(7).getValue());
        insolationIndexDesc.setText(weatherIndexList.get(4).getValue());
        sportIndexDesc.setText(weatherIndexList.get(8).getValue());
        travelIndexDesc.setText(weatherIndexList.get(5).getValue());

        // 详细
        TextView windIndexDetail = (TextView) holder.windIndexView
                .findViewById(R.id.index_detail_tv);
        TextView uandexDetail = (TextView) holder.uaIndexView
                .findViewById(R.id.index_detail_tv);
        TextView clotheIndexDetail = (TextView) holder.clotheIndexView
                .findViewById(R.id.index_detail_tv);
        TextView comfortIndexDetail = (TextView) holder.comfortIndexView
                .findViewById(R.id.index_detail_tv);
        TextView carIndexDetail = (TextView) holder.carIndexView
                .findViewById(R.id.index_detail_tv);
        TextView insolationIndexDetail = (TextView) holder.insolationIndexView
                .findViewById(R.id.index_detail_tv);
        TextView sportIndexDetail = (TextView) holder.sportIndexView
                .findViewById(R.id.index_detail_tv);
        TextView travelIndexDetail = (TextView) holder.travelIndexView
                .findViewById(R.id.index_detail_tv);

        windIndexDetail.setText(weatherIndexList.get(0).getDetail());
        uandexDetail.setText(weatherIndexList.get(6).getDetail());
        clotheIndexDetail.setText(weatherIndexList.get(2).getDetail());
        comfortIndexDetail.setText(weatherIndexList.get(1).getDetail());
        carIndexDetail.setText(weatherIndexList.get(7).getDetail());
        insolationIndexDetail.setText(weatherIndexList.get(4).getDetail());
        sportIndexDetail.setText(weatherIndexList.get(8).getDetail());
        travelIndexDetail.setText(weatherIndexList.get(5).getDetail());

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
        TextView detailLabelTV;
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
                case FORECAST_TYPE:
                    forecastViewDay1 = itemView.findViewById(R.id.day1);
                    forecastViewDay2 = itemView.findViewById(R.id.day2);
                    forecastViewDay3 = itemView.findViewById(R.id.day3);
                    forecastViewDay4 = itemView.findViewById(R.id.day4);
                    forecastViewDay5 = itemView.findViewById(R.id.day5);
                    forecastFootView = (TextView) itemView
                            .findViewById(R.id.forecast_foot);
                    break;
                case WEATHER_DETAILS_TYPE:
                    detailLabelTV = (TextView) itemView.findViewById(R.id.details_label);
                    detailsWeatherIV = (ImageView) itemView.findViewById(R.id.details_icon);
                    weatherNameTV = (TextView) itemView.findViewById(R.id.weather_name_tv);
                    feelsTempTV = (TextView) itemView.findViewById(R.id.feelsTemp_tv);
                    humidityTV = (TextView) itemView.findViewById(R.id.humidity_tv);
                    windTV = (TextView) itemView.findViewById(R.id.wind_tv);
                    windDescTV = (TextView) itemView.findViewById(R.id.wind_desc);
                    detailsFootTV = (TextView) itemView.findViewById(R.id.weather_details_foot_tv);
                    break;
                case ENVIRONMENT_TYPE:
                    aqiIV = (ImageView) itemView.findViewById(R.id.aqi_icon);
                    aqiLevelTV = (TextView) itemView.findViewById(R.id.aqi_level);
                    aqiTV = (TextView) itemView.findViewById(R.id.aqi);
                    pm25TV = (TextView) itemView.findViewById(R.id.pm25);
                    aqiDescTV = (TextView) itemView.findViewById(R.id.aqi_desc);
                    aqiFootTV = (TextView) itemView
                            .findViewById(R.id.weather_aqi_foot_tv);
                    break;
                case INDEX_TYPE:
                    windIndexView = itemView.findViewById(R.id.wind_index);
                    uaIndexView = itemView.findViewById(R.id.ua_index);
                    clotheIndexView = itemView.findViewById(R.id.clothe_index);
                    comfortIndexView = itemView.findViewById(R.id.comfort_index);
                    carIndexView = itemView.findViewById(R.id.car_index);
                    insolationIndexView = itemView
                            .findViewById(R.id.insolation_index);
                    sportIndexView = itemView.findViewById(R.id.sport_index);
                    travelIndexDivider = itemView
                            .findViewById(R.id.travel_divider);
                    travelIndexView = itemView.findViewById(R.id.travel_index);
                    break;
            }
        }
    }
}

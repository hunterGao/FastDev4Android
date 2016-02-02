package com.huntergao.hunterweather;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntergao.hunterweather.util.LunarCalendar;
import com.huntergao.hunterweather.util.LunarCalendarConvertUtil;
import com.huntergao.hunterweather.util.WeatherIconUtils;
import com.huntergao.hunterweather.xml.model.ForecastWeatherInfo;
import com.huntergao.hunterweather.xml.model.TodayWeatherInfo;
import com.huntergao.hunterweather.xml.model.WeatherEnvironment;
import com.huntergao.hunterweather.xml.model.WeatherIndex;
import com.hunterweather.app.R;
import com.hunterweather.app.entity.xml.ForecastWeatherInfo;
import com.hunterweather.app.entity.xml.TodayWeatherInfo;
import com.hunterweather.app.entity.xml.WeatherEnvironment;
import com.hunterweather.app.entity.xml.WeatherIndex;
import com.hunterweather.app.util.LunarCalendar;
import com.hunterweather.app.util.LunarCalendarConvertUtil;
import com.hunterweather.app.util.TimeUtils;
import com.hunterweather.app.util.WeatherIconUtils;

public class WeatherListAdapter extends BaseAdapter {
	private static final int FORECAST_TYPE = 0;
	private static final int WEATHER_DETAILS_TYPE = 1;
	private static final int ENVIRONMENT_TYPE = 2;
	private static final int INDEX_TYPE = 3;

	private LayoutInflater mLayoutInflater;
	private List<Integer> mTypes;
	private List<ForecastWeatherInfo> mForecastInfoList;
	private List<WeatherIndex> mWeatherIndexList;
	private WeatherEnvironment mEnvironment;
	private TodayWeatherInfo mWeatherInfo;
	private LunarCalendar mLunarCalendar;

	public WeatherListAdapter(Context context) {
		mTypes = new ArrayList<Integer>();
		mTypes.add(FORECAST_TYPE);
		mTypes.add(WEATHER_DETAILS_TYPE);
		mTypes.add(ENVIRONMENT_TYPE);
		mTypes.add(INDEX_TYPE);
		mLayoutInflater = LayoutInflater.from(context);
		mLunarCalendar = new LunarCalendar(context);
		Time time = new Time();
		time.set(System.currentTimeMillis());
		LunarCalendarConvertUtil.parseLunarCalendar(time.year, time.month,
				time.monthDay, mLunarCalendar);
	}

	public void setWeather(TodayWeatherInfo weatherInfo) {
		mWeatherInfo = weatherInfo;
		mEnvironment = weatherInfo.getEnvironment();
		mForecastInfoList = weatherInfo.getForecastList();
		mWeatherIndexList = weatherInfo.getIndexList();
		
		if ((mEnvironment == null) && mTypes.contains(ENVIRONMENT_TYPE))// 如果没有空气质量信息，则移除该选项。
			mTypes.remove(ENVIRONMENT_TYPE);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mTypes.size();
	}

	@Override
	public Object getItem(int position) {
		return mTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return mTypes.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position < mTypes.size())
			return mTypes.get(position);
		return super.getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemType = getItemViewType(position);
		ViewHolder holder;
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher + itemType) == null) {
			switch (itemType) {
			case FORECAST_TYPE:
				convertView = mLayoutInflater.inflate(
						R.layout.weather_forecast, parent, false);
				break;
			case WEATHER_DETAILS_TYPE:
				convertView = mLayoutInflater.inflate(R.layout.weather_details,
						parent, false);
				break;
			case ENVIRONMENT_TYPE:
				convertView = mLayoutInflater.inflate(R.layout.weather_aqi,
						parent, false);
				break;
			case INDEX_TYPE:
				convertView = mLayoutInflater.inflate(R.layout.weather_index,
						parent, false);
				break;
			default:
				break;
			}
			holder = buildHolder(convertView);
			convertView.setTag(R.drawable.ic_launcher + itemType, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ itemType);
		}
		bindViewData(holder, position);
		return convertView;
	}

	private ViewHolder buildHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		// 预报
		holder.forecastViewDay1 = convertView.findViewById(R.id.day1);
		holder.forecastViewDay2 = convertView.findViewById(R.id.day2);
		holder.forecastViewDay3 = convertView.findViewById(R.id.day3);
		holder.forecastViewDay4 = convertView.findViewById(R.id.day4);
		holder.forecastViewDay5 = convertView.findViewById(R.id.day5);
		holder.forecastFootView = (TextView) convertView
				.findViewById(R.id.forecast_foot);
		// 详细信息
		holder.detailsWeatherIV = (ImageView) convertView
				.findViewById(R.id.details_icon);
		holder.weatherNameTV = (TextView) convertView
				.findViewById(R.id.weather_name_tv);
		holder.feelsTempTV = (TextView) convertView
				.findViewById(R.id.feelsTemp_tv);
		holder.humidityTV = (TextView) convertView
				.findViewById(R.id.humidity_tv);
		holder.windTV = (TextView) convertView.findViewById(R.id.wind_tv);
		holder.windDescTV = (TextView) convertView.findViewById(R.id.wind_desc);
		holder.detailsFootTV = (TextView) convertView
				.findViewById(R.id.weather_details_foot_tv);
		// 空气质量
		holder.aqiIV = (ImageView) convertView.findViewById(R.id.aqi_icon);
		holder.aqiLevelTV = (TextView) convertView.findViewById(R.id.aqi_level);
		holder.aqiTV = (TextView) convertView.findViewById(R.id.aqi);
		holder.pm25TV = (TextView) convertView.findViewById(R.id.pm25);
		holder.aqiDescTV = (TextView) convertView.findViewById(R.id.aqi_desc);
		holder.aqiFootTV = (TextView) convertView
				.findViewById(R.id.weather_aqi_foot_tv);
		// 指数
		holder.windIndexView = convertView.findViewById(R.id.wind_index);
		holder.uaIndexView = convertView.findViewById(R.id.ua_index);
		holder.clotheIndexView = convertView.findViewById(R.id.clothe_index);
		holder.comfortIndexView = convertView.findViewById(R.id.comfort_index);
		holder.carIndexView = convertView.findViewById(R.id.car_index);
		holder.insolationIndexView = convertView
				.findViewById(R.id.insolation_index);
		holder.sportIndexView = convertView.findViewById(R.id.sport_index);
		holder.travelIndexDivider = convertView
				.findViewById(R.id.travel_divider);
		holder.travelIndexView = convertView.findViewById(R.id.travel_index);
		return holder;
	}

	private void bindViewData(ViewHolder holder, int position) {

		switch (getItemViewType(position)) {
		case FORECAST_TYPE:
			bindForecastView(holder, position);
			break;
		case WEATHER_DETAILS_TYPE:
			bindDetailsView(holder, position);
			break;
		case ENVIRONMENT_TYPE:
			bindAqiView(holder, position);
			break;
		case INDEX_TYPE:
			bindIndexView(holder, position);
			break;
		default:
			break;
		}
	}

	private void bindForecastView(ViewHolder holder, int position) {
		if (mForecastInfoList == null || mForecastInfoList.size() < 0)
			return;
		int sunrise = Integer.valueOf(mWeatherInfo.getSunrise().split(":")[0]);
		int sunset = Integer.valueOf(mWeatherInfo.getSunset().split(":")[0]);
		Object item = getItem(position);
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
		iconDay1.setImageResource(WeatherIconUtils.getWeatherIcon(mForecastInfoList.get(0)
				.getDayWeather().getType(), sunrise, sunset));
		iconDay2.setImageResource(WeatherIconUtils.getWeatherIcon(mForecastInfoList.get(1)
				.getDayWeather().getType(), sunrise, sunset));
		iconDay3.setImageResource(WeatherIconUtils.getWeatherIcon(mForecastInfoList.get(2)
				.getDayWeather().getType(), sunrise, sunset));
		iconDay4.setImageResource(WeatherIconUtils.getWeatherIcon(mForecastInfoList.get(3)
				.getDayWeather().getType(), sunrise, sunset));
		iconDay5.setImageResource(WeatherIconUtils.getWeatherIcon(mForecastInfoList.get(4)
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
		highTempDay1.setText(mForecastInfoList.get(0).getHigh());
		highTempDay2.setText(mForecastInfoList.get(1).getHigh());
		highTempDay3.setText(mForecastInfoList.get(2).getHigh());
		highTempDay4.setText(mForecastInfoList.get(3).getHigh());
		highTempDay5.setText(mForecastInfoList.get(4).getHigh());
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
		lowTempDay1.setText(mForecastInfoList.get(0).getLow());/*"°"*/
		lowTempDay2.setText(mForecastInfoList.get(1).getLow());
		lowTempDay3.setText(mForecastInfoList.get(2).getLow());
		lowTempDay4.setText(mForecastInfoList.get(3).getLow());
		lowTempDay5.setText(mForecastInfoList.get(4).getLow());

		holder.forecastFootView.setText("");
	}

	private void bindDetailsView(ViewHolder holder, int position) {
		
		Object item = getItem(position);
		if(mWeatherInfo == null)
			return;
		String type = mWeatherInfo.getForecastList().get(0).getDayWeather().getType();
		int sunrise = Integer.valueOf(mWeatherInfo.getSunrise().split(":")[0]);
		int sunset = Integer.valueOf(mWeatherInfo.getSunset().split(":")[0]);
		holder.detailsWeatherIV.setImageResource(WeatherIconUtils
				.getWeatherIcon(type, sunrise, sunset));
		holder.weatherNameTV.setText(type);
		holder.feelsTempTV.setText(mWeatherInfo.getWendu() + "°");
		holder.humidityTV.setText(mWeatherInfo.getShidu());
		holder.windTV.setText(mWeatherInfo.getFengxiang());
		holder.windDescTV.setText(mWeatherInfo.getFengli());
		
		// holder.detailsFootTV.setText("中国天气网");
		// holder.detailsFootTV.setText(mLunarCalendar.getLunarDayInfo());
		String str[] = mLunarCalendar.getLunarCalendarInfo(false);
		holder.detailsFootTV.setText(mLunarCalendar
				.getLunarYear(mLunarCalendar.lunarYear)
				+ "("
				+ mLunarCalendar.animalsYear(mLunarCalendar.lunarYear)
				+ ")年"
				+ str[1] + str[2]);
	}

	private void bindAqiView(ViewHolder holder, int position) {
		if (mEnvironment == null)
			return;
		Object item = getItem(position);
		holder.aqiIV.setImageResource(getAqiIcon(Integer.valueOf(mEnvironment.getAqi())));
		holder.aqiLevelTV.setText(mEnvironment.getQuality());
		holder.aqiTV.setText(mEnvironment.getAqi() + "μg/m³");
		holder.pm25TV.setText(mEnvironment.getPm25() + "μg/m³");
		holder.aqiDescTV.setText(mEnvironment.getSuggest());
		holder.aqiFootTV.setText("中国环境检测总站");
	}

	private int getAqiIcon(int aqi) {
		int aqi_img = R.drawable.biz_plugin_weather_0_50;
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

	private void bindIndexView(ViewHolder holder, int position) {
		if (mWeatherIndexList == null || mWeatherIndexList.size() < 0)
			return;
		Object item = getItem(position);
//		List<IndexDetail> lists = mIndex.getIndex();
//		if (TextUtils.isEmpty(lists.get(7).getDesc())) {
//			holder.travelIndexDivider.setVisibility(View.GONE);
//			holder.travelIndexView.setVisibility(View.GONE);
//		} else {
//			holder.travelIndexDivider.setVisibility(View.VISIBLE);
//			holder.travelIndexView.setVisibility(View.VISIBLE);
//		}
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
		windIndexDesc.setText(mWeatherIndexList.get(0).getValue());
		uandexDesc.setText(mWeatherIndexList.get(6).getValue());
		clotheIndexDesc.setText(mWeatherIndexList.get(2).getValue());
		comfortIndexDesc.setText(mWeatherIndexList.get(1).getValue());
		carIndexDesc.setText(mWeatherIndexList.get(7).getValue());
		insolationIndexDesc.setText(mWeatherIndexList.get(4).getValue());
		sportIndexDesc.setText(mWeatherIndexList.get(8).getValue());
		travelIndexDesc.setText(mWeatherIndexList.get(5).getValue());

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

		windIndexDetail.setText(mWeatherIndexList.get(0).getDetail());
		uandexDetail.setText(mWeatherIndexList.get(6).getDetail());
		clotheIndexDetail.setText(mWeatherIndexList.get(2).getDetail());
		comfortIndexDetail.setText(mWeatherIndexList.get(1).getDetail());
		carIndexDetail.setText(mWeatherIndexList.get(7).getDetail());
		insolationIndexDetail.setText(mWeatherIndexList.get(4).getDetail());
		sportIndexDetail.setText(mWeatherIndexList.get(8).getDetail());
		travelIndexDetail.setText(mWeatherIndexList.get(5).getDetail());

	}

	private static final class ViewHolder {
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
	}

}

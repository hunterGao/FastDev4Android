package com.huntergao.dailyweather.citymanage;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huntergao.dailyweather.BaseApplication;
import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.xml.model.ForecastWeatherInfo;
import com.huntergao.dailyweather.xml.model.TodayWeatherInfo;
import com.huntergao.dailyweather.util.WeatherIconUtils;
import com.huntergao.dailyweather.db.City;
import com.huntergao.dailyweather.BaseApplication;

public class CityGridViewAdapter extends BaseAdapter{

	public static final int NORMAL_CITY_TYPE = 0;
	public static final int ADD_CITY_TYPE = 1;
	private int refreshingIndex = -1;
	private boolean isEditMode; //是否是编辑模式
	private LayoutInflater mInflater;
	private Context mContext;
	private List<City> mCityList;
	

	public CityGridViewAdapter(Context context, List<City> cityList) {
		this.mContext = context;
		this.mCityList = cityList;
		mInflater = LayoutInflater.from(mContext);
		mCityList.add(null);
	}

	public void setRefreshingIndex(int position) {
		refreshingIndex = position;
		Log.i("HunterWeather", "CityGridAdapter setRefreshingIndex = " + position+"");
		notifyDataSetChanged();
	}

	public boolean isEditMode() {
		return isEditMode;
	}

	@Override
	public void notifyDataSetChanged() {
		int lastPosition = ((getCount() - 1) < 0) ? 0 : (getCount() - 1);
		if (isEditMode) {
			if (!mCityList.isEmpty() && mCityList.get(lastPosition) == null)// 如果最后一个是空,则编辑模式下移出
				mCityList.remove(lastPosition);
		} else {
			if (mCityList.isEmpty()
					|| (mCityList.get(lastPosition) != null))// 如果最后一个不为空， 则添加一个
				mCityList.add(null);
		}
		super.notifyDataSetChanged();
	}

	public void changeEditMode() {
		isEditMode = !isEditMode;
		notifyDataSetChanged();
	}

	public void reorder(int from, int to) {
		if (from != to) {
			City oldCity = mCityList.get(from);
			mCityList.remove(from);
			mCityList.add(to, oldCity);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mCityList.size();
	}

	@Override
	public City getItem(int position) {
		return mCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (getItem(position) == null)
			return ADD_CITY_TYPE;
		return NORMAL_CITY_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int type = getItemViewType(position);
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher + type) == null) {
			switch (type) {
			case NORMAL_CITY_TYPE:
				convertView = mInflater.inflate(
						R.layout.city_manger_grid_item_normal, parent,
						false);
				break;
			case ADD_CITY_TYPE:
				convertView = mInflater.inflate(
						R.layout.city_manger_grid_item_add, parent, false);
				break;
			default:
				break;
			}
			viewHolder = buildHolder(convertView);
			// 因为类型不同，所以给viewHolder设置一个标识,标识必须是资源id，不然会挂掉
			// 我这里为了区分不同的type，所以加上类型
			convertView.setTag(R.drawable.ic_launcher + type, viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView
					.getTag(R.drawable.ic_launcher + type);
		}
		bindViewData(viewHolder, position);
		return convertView;
	}

	private void bindViewData(ViewHolder holder, final int position) {
		City city = mCityList.get(position);
		TodayWeatherInfo weatherInfo = null;
		if (!BaseApplication.mSelectWeatherInfoMap.isEmpty() && city != null)
			weatherInfo = BaseApplication.mSelectWeatherInfoMap.get(city.getName());
		switch (getItemViewType(position)) {
		case NORMAL_CITY_TYPE:
			if (refreshingIndex == position) {
				holder.loadingBar.setVisibility(View.VISIBLE);
				holder.weatherIV.setVisibility(View.GONE);
				holder.tempTV.setText("加载中...");
			} else {
				holder.loadingBar.setVisibility(View.GONE);
				holder.weatherIV.setVisibility(View.VISIBLE);
				if (weatherInfo != null) {
					ForecastWeatherInfo forecast = weatherInfo.getForecastList().get(0);
					int sunrise = Integer.valueOf(weatherInfo.getSunrise().split(":")[0]);
					int sunset = Integer.valueOf(weatherInfo.getSunset().split(":")[0]);
					holder.tempTV.setText(forecast.getLow().substring(2)+
							"~"+ forecast.getHigh().substring(2));
					holder.weatherIV.setImageResource(WeatherIconUtils
							.getWeatherIcon(forecast.getDayWeather().getType(), sunrise, sunset));
				} else {
					holder.tempTV.setText("--~--°");
					holder.weatherIV
							.setImageResource(R.drawable.default_no_weather);
				}
			}
			holder.cityTV.setText(city.getName());
			//设置
			if (city.isLocation()) {
				holder.cityTV.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.current_loc_active_26x26, 0, 0, 0);
			} else {
				holder.cityTV.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						0, 0);
			}
			if (isEditMode && !city.isLocation())
				holder.deleteIV.setVisibility(View.VISIBLE);
			else
				holder.deleteIV.setVisibility(View.GONE);
				holder.deleteIV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 从数据库中删除城市
					((ManageCityActivity)mContext).deleteCityFromTable(position);
				}
			});
			break;
		}
	}

	private ViewHolder buildHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.cityTV = (TextView) convertView
				.findViewById(R.id.city_manager_name_tv);
		holder.tempTV = (TextView) convertView
				.findViewById(R.id.city_manager_temp_tv);
		holder.weatherIV = (ImageView) convertView
				.findViewById(R.id.city_manager_icon_iv);
		holder.deleteIV = (ImageView) convertView
				.findViewById(R.id.city_delete_btn);
		holder.loadingBar = (ProgressBar) convertView
				.findViewById(R.id.city_manager_progressbar);
		holder.addView = convertView;
		return holder;
	}
	
	private static class ViewHolder {
		TextView cityTV;
		TextView tempTV;
		ImageView weatherIV;
		ProgressBar loadingBar;
		ImageView deleteIV;
		View addView;
	}
}

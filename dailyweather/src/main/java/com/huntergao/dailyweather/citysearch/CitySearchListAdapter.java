package com.huntergao.dailyweather.citysearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huntergao.dailyweather.R;
import com.huntergao.dailyweather.WeatherApplication;
import com.huntergao.dailyweather.db.City;

import java.util.ArrayList;
import java.util.List;

public class CitySearchListAdapter extends BaseAdapter {

	private List<City> mResultCities;
	private LayoutInflater mInflater;
	private Context mContext;

	public CitySearchListAdapter(Context context) {
		mContext = context;
		mResultCities = new ArrayList<>();
		mInflater = LayoutInflater.from(mContext);
	}

	public void setResult(List<City> cities) {
		mResultCities = cities;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mResultCities == null ? 0 : mResultCities.size();
	}

	@Override
	public City getItem(int position) {
		return mResultCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final City city = getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.city_search_list_item, parent,
					false);
			holder = new ViewHolder();
			holder.nameTv = (TextView) convertView
					.findViewById(R.id.citylst_name);
			holder.cityTv = (TextView) convertView
					.findViewById(R.id.citylst_city);
			holder.provinceTv = (TextView) convertView
					.findViewById(R.id.citylst_province);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.provinceTv.setText(city.getProvince());
		holder.cityTv.setText(city.getCity());
		holder.nameTv.setText(city.getName());
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WeatherApplication.jumpMainActivity(mContext, city);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		TextView provinceTv;
		TextView cityTv;
		TextView nameTv;
	}
}

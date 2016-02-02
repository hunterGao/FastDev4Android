package com.huntergao.hunterweather.cityquery;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntergao.hunterweather.db.City;

public class HotCityGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<City> mHotCityList;
	
	public HotCityGridViewAdapter(Context context, List<City> hotCityList) {
		this.mContext = context;
		this.mHotCityList = hotCityList;
		mInflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		return mHotCityList.size();
	}

	@Override
	public Object getItem(int position) {
		return mHotCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		City hotCity = (City) getItem(position);
		ViewHoler viewHoler;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.city_query_hotcity_grid_item, parent, false);
			viewHoler = new ViewHoler();
			viewHoler.hotCityTV = (TextView) convertView
					.findViewById(R.id.grid_city_name);
			viewHoler.selectedIV = (ImageView) convertView
					.findViewById(R.id.grid_city_selected_iv);
			convertView.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) convertView.getTag();
		}
		viewHoler.hotCityTV.setText(hotCity.getName());
		if (hotCity.isSelect()) {
			viewHoler.selectedIV.setVisibility(View.VISIBLE);
		} else {
			viewHoler.selectedIV.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	private static class ViewHoler {
		TextView hotCityTV;
		ImageView selectedIV;
	}

}

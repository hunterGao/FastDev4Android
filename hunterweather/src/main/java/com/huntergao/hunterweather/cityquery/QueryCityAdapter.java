package com.huntergao.hunterweather.cityquery;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.huntergao.hunterweather.db.City;
import com.huntergao.hunterweather.db.CityDB;

public class QueryCityAdapter extends BaseAdapter implements Filterable {

	private List<City> mResultCities;
	private LayoutInflater mInflater;
	private Context mContext;

	public QueryCityAdapter(Context context) {
		mContext = context;
		mResultCities = new ArrayList<City>();
		mInflater = LayoutInflater.from(mContext);
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
		City city = getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.city_query_list_item, parent,
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
		return convertView;
	}

	static class ViewHolder {
		TextView provinceTv;
		TextView cityTv;
		TextView nameTv;
	}

	@Override
	public android.widget.Filter getFilter() {
		android.widget.Filter filter = new android.widget.Filter() {
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				mResultCities = (ArrayList<City>) results.values;
				if (results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}

			protected FilterResults performFiltering(CharSequence s) {

				FilterResults results = new FilterResults();
				List<City> queryResultCities = new ArrayList<City>();
//				results.values = queryResultCities;
//				results.count = queryResultCities.size();

				if (TextUtils.isEmpty(s))
					return results;

				String str = s.toString();
				if(str.matches("[0-9]+")){
					queryResultCities = CityDB.getInstance(mContext).readCityListByCode(str);
				}else if(str.matches("[a-zA-Z]+")){
					queryResultCities = CityDB.getInstance(mContext).readCityListByPinyin(str);
				}else if(str.matches("[\\u4e00-\\u9fa5]+")){ //中文
					queryResultCities = CityDB.getInstance(mContext).readCityListByName(str);
				}
				
				results.values = queryResultCities;
				results.count = queryResultCities.size();
				return results;
			}
		};
		return filter;
	}

}

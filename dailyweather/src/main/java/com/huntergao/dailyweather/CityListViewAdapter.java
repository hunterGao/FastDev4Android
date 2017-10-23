package com.huntergao.dailyweather;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * 城市选择ListView的适配器
 * @author HG
 *
 */
public class CityListViewAdapter extends BaseAdapter {

	private Context mContext = null;
	
	private List<String> mList = null;
	
	public CityListViewAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
	}
	
	//listView中的个数
	@Override
	public int getCount() {
		return mList.size();
	}

	//listView中每个view的数据对象
	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	//listView中每个view的id
	@Override
	public long getItemId(int position) {
		return position;
	}

	//获取listView中的每个view，(会循环获取view)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.view_city_listview, null);
			holder = new ViewHolder();
			holder.textview = (TextView) convertView.findViewById(R.id.city_name_listview);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		TextView cityName = holder.textview;
		cityName.setText(mList.get(position));
		return convertView;
	}
	
	class ViewHolder{
		TextView textview = null;
	}
}

package com.huntergao.hunterweather.weathershow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.huntergao.hunterweather.db.City;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {
	private final ArrayList<ItemInfo> mItemInfos;

	public static final class ItemInfo {
		private final City city;
		private Fragment fragment;

		public ItemInfo(City city) {
			this.city = city;
		}
	}

	public WeatherPagerAdapter(Activity activity) {
		super(activity.getFragmentManager());
		mItemInfos = new ArrayList<ItemInfo>();
	}

	public void addItem(City city) {
		ItemInfo info = new ItemInfo(city);
		mItemInfos.add(info);
		notifyDataSetChanged();
	}

	public void addAllItems(List<City> mainItems) {
		mItemInfos.clear();
		for (City item : mainItems) {
			ItemInfo info = new ItemInfo(item);
			mItemInfos.add(info);
		}
		notifyDataSetChanged();
	}

	public void clearItems() {
		mItemInfos.clear();
	}

	@Override
	public Fragment getItem(int position) {
		ItemInfo info = mItemInfos.get(position);
		if (info.fragment == null) {
			info.fragment = new WeatherFragment(info.city);
		}
		return info.fragment;
	}
	
	@Override
	public int getItemPosition(Object object) {
		//返回PagerAdapter.POSITION_NONE，保证删除Fragment
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public int getCount() {
		return mItemInfos.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mItemInfos.get(position).city.getName();
	}

}

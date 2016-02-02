package com.huntergao.hunterweather;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * @author HG
 *
 */
public class WeatherViewPageAdapter extends PagerAdapter {

	/**
	 * 存储View的集合
	 */
	private List<View> viewList = null;
	
	public WeatherViewPageAdapter(List<View> list) {
		this.viewList = list;
	}
	
	/**
	 * 返回View的个数
	 */
	@Override
	public int getCount() {
		if(null != viewList)
			return viewList.size();
		return 0;
	}

	/**
	 * View是否来自Object
	 */
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return (view == obj);
	}
	
	/**
	 * 初始化View
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(viewList.get(position),0);
		return viewList.get(position);
	}
	
	/**
	 * 销毁View
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(viewList.get(position));
	}

}

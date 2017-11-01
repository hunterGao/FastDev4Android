package com.huntergao.dailyweather.citylocation;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationCity {

	private LocationClient mLocationClient;
	private LocationListener mLocationListener;
	
	public LocationCity(Context context, LocationListener status) {
		this.mLocationListener = status;
		mLocationClient = new LocationClient(context, 
				getLocationClientOption(context));
		//注册定位监听器
		mLocationClient.registerLocationListener(mBDLocationListener);
	}
	
	/**
	 * 定位的配置参数
	 * @param context
	 * @return
	 */
	private LocationClientOption getLocationClientOption(Context context) {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间为2000ms
		option.setProdName(context.getPackageName());
		option.setIsNeedAddress(true);
		return option;
	}
	
	// 开始定位
	public void startLocation() {
		mLocationClient.start();
	}

	// 结束定位
	public void stopLocation() {
		mLocationClient.stop();

	}
	
	public boolean isStartLocation(){
		return mLocationClient.isStarted();
	}
	
	private BDAbstractLocationListener mBDLocationListener = new BDAbstractLocationListener() {
		
		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location == null) {
				mLocationListener.onFail();
				return;
			}
			Log.d("HunterWeather", "Location City "+location.getCity());
			Log.d("HunterWeather", "Location County "+location.getCountry());
			Log.d("HunterWeather", "Location Address "+location.getAddrStr());
			Log.d("HunterWeather", "Location Latitude "+location.getLatitude());
			Log.d("HunterWeather", "Location Longitude "+location.getLongitude());
			Log.d("HunterWeather", "Location Type "+location.getLocType());

			if(location.getLocType() != BDLocation.TypeNetWorkLocation) {
				mLocationListener.onFail();
				return;
			}
			String address = location.getAddrStr();
			String cityName = location.getCity();
			if(address.contains("县")){
				cityName = address.substring(address.indexOf("市")+1, address.indexOf("县"));
			}
			else if(address.contains("区")){
				cityName = address.substring(address.indexOf("市")+1, address.indexOf("区"));
			}
			else{
				cityName = cityName.replace("市", "");
			}
			mLocationListener.onSuccess(cityName);
			mLocationClient.stop();//停止定位
		}
	};
	
	public interface LocationListener {
		void onFail();
		
		void onSuccess(String city);
	}
	
}

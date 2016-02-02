package com.huntergao.hunterweather;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final int NO_VALUE_FLAG = -999;//无
	public static final int SUNNY = 0;//晴
	public static final int CLOUDY = 1;//多云
	public static final int OVERCAST = 2;//阴
	public static final int FOGGY = 3;//雾
	public static final int SEVERE_STORM = 4;//飓风
	public static final int HEAVY_STORM = 5;//暴风雨
	public static final int STORM = 6;//暴风雨
	public static final int THUNDERSHOWER = 7;//雷阵雨
	public static final int SHOWER = 8;//阵雨
	public static final int HEAVY_RAIN = 9;//大雨
	public static final int MODERATE_RAIN = 10;//中雨
	public static final int LIGHT_RAIN = 11;//小雨
	public static final int SLEET = 12;//雨夹雪
	public static final int SNOWSTORM = 13;//暴雪
	public static final int SNOW_SHOWER = 14;//阵雪
	public static final int HEAVY_SNOW = 15;//大雪
	public static final int MODERATE_SNOW = 16;//中雪
	public static final int LIGHT_SNOW = 17;//小雪
	public static final int STRONGSANDSTORM = 18;//强沙尘暴
	public static final int SANDSTORM = 19;//沙尘暴
	public static final int SAND = 20;//沙尘
	public static final int BLOWING_SAND = 21;//风沙
	public static final int ICE_RAIN = 22;//冻雨
	public static final int DUST = 23;//尘土
	public static final int HAZE = 24;//霾
	
	public static Map<String, Integer> weatherMap = new HashMap<String, Integer>();
	static{
		weatherMap.put("晴", SUNNY);
		weatherMap.put("多云", CLOUDY);
		weatherMap.put("阴", OVERCAST);
		weatherMap.put("雾", FOGGY);
		weatherMap.put("飓风", SEVERE_STORM);
		weatherMap.put("暴风雨", HEAVY_STORM);
		weatherMap.put("雷阵雨", THUNDERSHOWER);
		weatherMap.put("阵雨", SHOWER);
		weatherMap.put("大雨", HEAVY_RAIN);
		weatherMap.put("中雨", MODERATE_RAIN);
		weatherMap.put("小雨", LIGHT_RAIN);
		weatherMap.put("雨夹雪", SLEET);
		weatherMap.put("暴雪", SNOWSTORM);
		weatherMap.put("阵雪", SNOW_SHOWER);
		weatherMap.put("大雪", HEAVY_SNOW);
		weatherMap.put("阵雪", SNOW_SHOWER);
		weatherMap.put("中雪", MODERATE_SNOW);
		weatherMap.put("小雪", LIGHT_SNOW);
		weatherMap.put("强沙尘暴", STRONGSANDSTORM);
		weatherMap.put("沙尘暴", SANDSTORM);
		weatherMap.put("沙尘", SAND);
		weatherMap.put("风沙", BLOWING_SAND);
		weatherMap.put("冻雨", ICE_RAIN);
		weatherMap.put("尘土", DUST);
		weatherMap.put("霾", HAZE);
	}
}


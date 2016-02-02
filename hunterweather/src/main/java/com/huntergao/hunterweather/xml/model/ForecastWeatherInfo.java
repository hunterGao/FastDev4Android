package com.huntergao.hunterweather.xml.model;

import com.huntergao.hunterweather.xml.model.SimpleWeatherInfo;

/**
 * 未来的天气情况
 * @author HG
 *
 */
public class ForecastWeatherInfo {

	private String date = null;
	private String high = null; //高温
	private String low = null; //低温
	private SimpleWeatherInfo dayWeather = null; //白天的天气情况
	private SimpleWeatherInfo nightWeather = null; //夜晚的天气情况
	
	public ForecastWeatherInfo() {
		super();
	}

	public ForecastWeatherInfo(String date, String high, String low) {
		super();
		this.date = date;
		this.high = high;
		this.low = low;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public SimpleWeatherInfo getDayWeather() {
		return dayWeather;
	}

	public void setDayWeather(SimpleWeatherInfo dayWeather) {
		this.dayWeather = dayWeather;
	}

	public SimpleWeatherInfo getNightWeather() {
		return nightWeather;
	}

	public void setNightWeather(SimpleWeatherInfo nightWeather) {
		this.nightWeather = nightWeather;
	}

}

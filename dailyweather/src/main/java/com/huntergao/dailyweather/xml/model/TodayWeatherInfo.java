package com.huntergao.dailyweather.xml.model;

import java.util.List;

/**
 * 当前的天气情况
 * xml pull解析的主类
 * @author HG
 *
 */
public class TodayWeatherInfo {

	private String city = null;
	private String updatetime = null;
	private String wendu = null;
	private String fengli = null;
	private String shidu = null;
	private String fengxiang = null;
	private String sunrise = null;
	private String sunset = null;
	
	private WeatherEnvironment environment = null;
	private ForecastWeatherInfo yesterdayInfo = null;
	private List<ForecastWeatherInfo> forecastList = null;
	private WeatherAlarm weatherAlarm = null;
	private List<WeatherIndex> indexList = null;
	
	public TodayWeatherInfo() {
		super();
	}
	
	public TodayWeatherInfo(String city, String updatetime, String wendu,
			String fengli, String shidu, String fengxiang, String sunrise,
			String sunset) {
		super();
		this.city = city;
		this.updatetime = updatetime;
		this.wendu = wendu;
		this.fengli = fengli;
		this.shidu = shidu;
		this.fengxiang = fengxiang;
		this.sunrise = sunrise;
		this.sunset = sunset;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getWendu() {
		return wendu;
	}
	public void setWendu(String wendu) {
		this.wendu = wendu;
	}
	public String getFengli() {
		return fengli;
	}
	public void setFengli(String fengli) {
		this.fengli = fengli;
	}
	public String getShidu() {
		return shidu;
	}
	public void setShidu(String shidu) {
		this.shidu = shidu;
	}
	public String getFengxiang() {
		return fengxiang;
	}
	public void setFengxiang(String fengxiang) {
		this.fengxiang = fengxiang;
	}
	public String getSunrise() {
		return sunrise;
	}
	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}
	public String getSunset() {
		return sunset;
	}
	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

	public WeatherEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(WeatherEnvironment environment) {
		this.environment = environment;
	}

	public WeatherAlarm getWeatherAlarm() {
		return weatherAlarm;
	}

	public void setWeatherAlarm(WeatherAlarm weatherAlarm) {
		this.weatherAlarm = weatherAlarm;
	}

	public ForecastWeatherInfo getYesterdayInfo() {
		return yesterdayInfo;
	}

	public void setYesterdayInfo(ForecastWeatherInfo yesterdayInfo) {
		this.yesterdayInfo = yesterdayInfo;
	}

	public List<ForecastWeatherInfo> getForecastList() {
		return forecastList;
	}

	public void setForecastList(List<ForecastWeatherInfo> forecastList) {
		this.forecastList = forecastList;
	}

	public List<WeatherIndex> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<WeatherIndex> indexList) {
		this.indexList = indexList;
	}
	
}

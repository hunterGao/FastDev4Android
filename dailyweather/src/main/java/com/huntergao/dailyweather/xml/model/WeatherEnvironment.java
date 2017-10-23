package com.huntergao.dailyweather.xml.model;

/**
 * 环境信息
 * @author HG
 *
 */
public class WeatherEnvironment {

	/**
	 * 空气质量指数
	 */
	private String aqi = null; 
	private String pm25 = null;
	private String pm10 = null;
	/**
	 * 空气质量
	 */
	private String quality = null; 
	private String suggest = null; 
	/**
	 * 主要污染物
	 */
	private String majorpollutants = null;
	/**
	 * 臭氧
	 */
	private String o3 = null; 
	/**
	 * 一氧化碳
	 */
	private String co = null; 
	/**
	 * 二氧化硫
	 */
	private String so2 = null;
	/**
	 * 二氧化氮
	 */
	private String no2 = null;
	
	public WeatherEnvironment() {
		super();
	}

	public WeatherEnvironment(String aqi, String pm25, String pm10, String quality,
			String suggest, String majorpollutants, String o3, String co,
			String so2, String no2) {
		super();
		this.aqi = aqi;
		this.pm25 = pm25;
		this.pm10 = pm10;
		this.quality = quality;
		this.suggest = suggest;
		this.majorpollutants = majorpollutants;
		this.o3 = o3;
		this.co = co;
		this.so2 = so2;
		this.no2 = no2;
	}

	public String getAqi() {
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public String getPm25() {
		return pm25;
	}

	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}

	public String getPm10() {
		return pm10;
	}

	public void setPm10(String pm10) {
		this.pm10 = pm10;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getMajorpollutants() {
		return majorpollutants;
	}

	public void setMajorpollutants(String majorpollutants) {
		this.majorpollutants = majorpollutants;
	}

	public String getO3() {
		return o3;
	}

	public void setO3(String o3) {
		this.o3 = o3;
	}

	public String getCo() {
		return co;
	}

	public void setCo(String co) {
		this.co = co;
	}

	public String getSo2() {
		return so2;
	}

	public void setSo2(String so2) {
		this.so2 = so2;
	}

	public String getNo2() {
		return no2;
	}

	public void setNo2(String no2) {
		this.no2 = no2;
	} 
	
}

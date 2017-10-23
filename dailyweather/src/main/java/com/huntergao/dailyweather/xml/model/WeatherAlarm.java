package com.huntergao.dailyweather.xml.model;

/**
 * 天气预警信息(不一定存在)
 * @author HG
 *
 */
public class WeatherAlarm {

	private String name = null; //预警名称
	private String detial = null;//预警细节
	private String suggest = null; //建议
	
	public WeatherAlarm() {
		super();
	}

	public WeatherAlarm(String name, String detial, String suggest) {
		super();
		this.name = name;
		this.detial = detial;
		this.suggest = suggest;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetial() {
		return detial;
	}

	public void setDetial(String detial) {
		this.detial = detial;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
}

package com.huntergao.dailyweather.xml.model;

/**
 * 天气的各项指数
 * @author HG
 *
 */
public class WeatherIndex {

	private String name = null; //名称
	private String value = null; //情况
	private String detail = null; //细节
	
	public WeatherIndex() {
		super();
	}

	public WeatherIndex(String name, String value, String detail) {
		super();
		this.name = name;
		this.value = value;
		this.detail = detail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}

package com.huntergao.hunterweather.xml.model;

/**
 * 白天的天气描述
 * @author HG
 *
 */
public class SimpleWeatherInfo {

	private String type = null;
	private String fl = null;
	private String fx = null;
	
	public SimpleWeatherInfo() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getFx() {
		return fx;
	}

	public void setFx(String fx) {
		this.fx = fx;
	}
	
}

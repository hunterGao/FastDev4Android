package com.huntergao.dailyweather.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.huntergao.dailyweather.xml.model.ForecastWeatherInfo;
import com.huntergao.dailyweather.xml.model.SimpleWeatherInfo;
import com.huntergao.dailyweather.xml.model.WeatherInfo;
import com.huntergao.dailyweather.xml.model.WeatherEnvironment;
import com.huntergao.dailyweather.xml.model.WeatherIndex;

/**
 * xml解析工具类
 * @author HG
 *
 */
public class XmlParserUtil {

	/**
	 * Pull解析方式，是一个一个节点的解析，通过每个节点的起始和结束标签判断一个节点是否解析完毕
	 * Pull解析的特点：必须事先知道xml的节点名称
	 * 
	 * Pull解析是在XML文档中寻找想要的标记，把需要的内容拉入内存，而不是把整个文档都拉入内存，
	 * 这种方式比较适合手机等内存有限的小型的移动设备
	 * 注意：字符串的编码保持一致
	 * @param xmlData
	 */
	public static WeatherInfo parseXmlWithPull(String xmlData){
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser pullParser = factory.newPullParser();
			xmlData = new String(xmlData.getBytes(), "utf-8");
			pullParser.setInput(new StringReader(xmlData));
			int type = pullParser.getEventType();
			WeatherInfo weatherInfo = null;
			List<ForecastWeatherInfo> forecastList =
					new ArrayList<ForecastWeatherInfo>();
			List<WeatherIndex> indexList = new ArrayList<WeatherIndex>();
			while(XmlPullParser.END_DOCUMENT != type){
				String node = pullParser.getName();
				switch(type){
				//开始解析节点
				case XmlPullParser.START_TAG:
					if(node.equals("resp")){ //根节点
						weatherInfo = new WeatherInfo();
					} else if(node.equals("city")){
						String city = pullParser.nextText();
						weatherInfo.setCity(city);
					} else if(node.equals("updatetime")){
						String updatetime = pullParser.nextText();
						weatherInfo.setUpdatetime(updatetime);
					} else if(node.equals("wendu")){
						String wendu = pullParser.nextText();
						weatherInfo.setWendu(wendu);
					} else if(node.equals("fengli")){
						String fengli = pullParser.nextText();
						weatherInfo.setFengli(fengli);
					}else if(node.equals("shidu")){
						String shidu = pullParser.nextText();
						weatherInfo.setShidu(shidu);
					}else if(node.equals("fengxiang")){
						String fengxiang = pullParser.nextText();
						weatherInfo.setFengxiang(fengxiang);
					}else if(node.equals("sunrise_1")){
						String sunrise = pullParser.nextText();
						weatherInfo.setSunrise(sunrise);
					}else if(node.equals("sunset_1")){
						String sunset = pullParser.nextText();
						weatherInfo.setSunset(sunset);
					}else if(node.equals("environment")){
						WeatherEnvironment environment = null;
						while(type != XmlPullParser.END_TAG || !node.equals("environment")){
							switch(type){
							case XmlPullParser.START_TAG:
								if(node.equals("environment")){
									environment = new WeatherEnvironment(); 
								}else if(node.equals("aqi") && null != environment){
									environment.setAqi(pullParser.nextText());
								}else if(node.equals("pm25") && null != environment){
									environment.setPm25(pullParser.nextText());
								}else if(node.equals("suggest") && null != environment){
									environment.setSuggest(pullParser.nextText());
								}else if(node.equals("quality") && null != environment){
									environment.setQuality(pullParser.nextText());
								}else if(node.equals("MajorPollutants") && null != environment){
									environment.setMajorpollutants(pullParser.nextText());
								}
								break;
							}
							type = pullParser.next();
							node = pullParser.getName();
						}
						weatherInfo.setEnvironment(environment);
					}else if(node.equals("yesterday")){
						ForecastWeatherInfo info = null;
						while(type != XmlPullParser.END_TAG || !node.equals("yesterday")){
							switch(type){
							case XmlPullParser.START_TAG:
								if(node.equals("yesterday")){
									info = new ForecastWeatherInfo();
								}else if(node.equals("date_1") && null != info){
									info.setDate(pullParser.nextText());
								}else if(node.equals("high_1") && null != info){
									info.setHigh(pullParser.nextText());
								}else if(node.equals("low_1") && null != info){
									info.setLow(pullParser.nextText());
								}else if(node.equals("day_1") && null != info){
									type = parserSimpleYesterdayXml(pullParser, type,
											node, info, "day_1");
								}else if(node.equals("night_1") && null != info){
									type = parserSimpleYesterdayXml(pullParser, type,
											node ,info, "night_1");
								}
								break;
							}
							type = pullParser.next();
							node = pullParser.getName();
						}
						weatherInfo.setYesterdayInfo(info);
					}else if(node.equals("forecast")){
						ForecastWeatherInfo info = null;
						while(type != XmlPullParser.END_TAG || !node.equals("forecast")){
							switch(type){
							case XmlPullParser.START_TAG:
								if(node.equals("weather")){
									info = new ForecastWeatherInfo();
								}else if(node.equals("date") && null != info){
									info.setDate(pullParser.nextText());
								}else if(node.equals("high") && null != info){
									info.setHigh(pullParser.nextText());
								}else if(node.equals("low") && null != info){
									info.setLow(pullParser.nextText());
								}else if(node.equals("day") && null != info){
									type = parserSimpleXml(pullParser, type,
											node, info, "day");
								}else if(node.equals("night") && null != info){
									type = parserSimpleXml(pullParser, type,
											node ,info, "night");
								}
								break;
							case XmlPullParser.END_TAG:
								if(node.equals("weather")){
									forecastList.add(info);
								}
								break;
							}
							type = pullParser.next();
							node = pullParser.getName();
						}
						if(null != weatherInfo){
							weatherInfo.setForecastList(forecastList);
						}
					}else if(node.equals("zhishus")){
						WeatherIndex index = null;
						while(type != XmlPullParser.END_TAG || !node.equals("zhishus")){
							switch(type){
							case XmlPullParser.START_TAG:
								if(node.equals("zhishu")){
									index = new WeatherIndex();
								}else if(node.equals("name") && index != null){
									String name = pullParser.nextText();
									index.setName(name);
								}else if(node.equals("value") && index != null){
									String value = pullParser.nextText();
									index.setValue(value);
								}else if(node.equals("detail") && index != null){
									String detail = pullParser.nextText();
									index.setDetail(detail);
								}
								break;
							case XmlPullParser.END_TAG:
								if(node.equals("zhishu")){
									indexList.add(index);
								}
								break;
							}
							type = pullParser.next();
							node = pullParser.getName();
						}
						if(weatherInfo != null){
							weatherInfo.setIndexList(indexList);
						}
					}
					break;
				//节点解析结束
				case XmlPullParser.END_TAG:
					break;
				}
				type = pullParser.next();
			}
			return weatherInfo;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int parserSimpleXml(XmlPullParser pullParser, int type,
			String node, ForecastWeatherInfo info, String tag)
			throws XmlPullParserException, IOException {
		SimpleWeatherInfo simple = null;
		while(type != XmlPullParser.END_TAG || !node.equals(tag)){
			switch(type){
			case XmlPullParser.START_TAG:
				if(node.equals(tag)){
					simple = new SimpleWeatherInfo();
				}
				else if(node.equals("type")){
					simple.setType(pullParser.nextText());
				}else if(node.equals("fengxiang")){
					simple.setFx(pullParser.nextText());
				}else if(node.equals("fengli")){
					simple.setFl(pullParser.nextText());
				}
				break;
			}
			type = pullParser.next();
			node = pullParser.getName();
		}
		if(tag.equals("day")){
			info.setDayWeather(simple);
		}else if(tag.equals("night")){
			info.setNightWeather(simple);
		}
		return type;
	}
	
	private static int parserSimpleYesterdayXml(XmlPullParser pullParser, int type,
			String node, ForecastWeatherInfo info, String tag)
			throws XmlPullParserException, IOException {
		SimpleWeatherInfo simple = null;
		while(type != XmlPullParser.END_TAG || !node.equals(tag)){
			switch(type){
			case XmlPullParser.START_TAG:
				if(node.equals(tag)){
					simple = new SimpleWeatherInfo();
				}
				else if(node.equals("type_1")){
					simple.setType(pullParser.nextText());
				}else if(node.equals("fx_1")){
					simple.setFx(pullParser.nextText());
				}else if(node.equals("fl_1")){
					simple.setFl(pullParser.nextText());
				}
				break;
			}
			type = pullParser.next();
			node = pullParser.getName();
		}
		if(tag.equals("day_1")){
			info.setDayWeather(simple);
		}else if(tag.equals("night_1")){
			info.setNightWeather(simple);
		}
		return type;
	}
}

package com.huntergao.dailyweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用单例模式将一些常用的数据库操作封装起来
 * 
 * @author HG
 *
 */
public class SelectCityDB {

	/**
	 * 数据库名称
	 */
	public static final String DB_NAME = "selectcity.db";
	
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	
	
	private static SelectCityDB hunterWeatherDB;
	
	private static SQLiteDatabase db;
	
	
	private SelectCityDB(Context context){
		SelectCityOpenHelper dbHelper = new SelectCityOpenHelper(
				context, DB_NAME, null, VERSION);
		
		db = dbHelper.getWritableDatabase();
	}
	
	public synchronized static SelectCityDB getInstance(Context context){
		if(null == hunterWeatherDB){
			hunterWeatherDB = new SelectCityDB(context);
		}
		return hunterWeatherDB;
	}
	
	/**
	 * 从数据库中读取所有的城市信息
	 * @return
	 */
	public List<City> readAllCity(){
		List<City> lists = new ArrayList<City>();
		Cursor cursor = db.query("selectcity", null, null,
				null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setName(cursor.getString(cursor.getColumnIndex("name")));
				city.setPostID(cursor.getString(cursor.getColumnIndex("postID")));
				city.setLocation(cursor.getInt(cursor.getColumnIndex("isLocation")) == 1);
				city.setSelect(true);
				lists.add(city);
			}while(cursor.moveToNext());
		}
		return lists;
	}
	
	public City readLocationCity(){
		Cursor cursor = db.query("selectcity", null, "isLocation=?",
				new String[]{"1"}, null, null, null);
		if(cursor.moveToFirst()){
			City city = new City();
			city.setName(cursor.getString(cursor.getColumnIndex("name")));
			city.setPostID(cursor.getString(cursor.getColumnIndex("postID")));
			city.setLocation(true);
			return city;
		}
		return null;
	}
	
	/**
	 * 从数据库中删除一条记录
	 * @param postID
	 * @return
	 */
	public boolean deleteCity(String postID){
		int result = db.delete("selectcity", "postID=?", new String[]{postID});
		return result == 1;
	}
	
	/**
	 * 向数据库中添加一条记录
	 * @param city
	 * @return
	 */
	public boolean writeCity(City city){
		ContentValues values = new ContentValues();
		values.put("name", city.getName());
		values.put("postID", city.getPostID());
		values.put("isLocation", city.isLocation() ? 1 :0);
		long result = db.insert("selectcity", null, values);
		return result != -1;
	}
	
}

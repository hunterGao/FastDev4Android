package com.huntergao.hunterweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CityDB {

	public static final String DB_NAME = "city.db";
	
	public static CityDB mCityDB = null;
	
	public static SQLiteDatabase db;
	
	private CityDB(Context context){
		copyDB(context);
		db = context.openOrCreateDatabase(DB_NAME, Context.MODE_WORLD_READABLE , null);
	}
	
	public static synchronized CityDB getInstance(Context context){
		if(null == mCityDB){
			mCityDB = new CityDB(context);
		}
		return mCityDB;
	}
	
	/**
	 * 通过postID读取数据库，生成City对象
	 * @param postID
	 * @return
	 */
	public City readCityByPostCode(String postID){
		Cursor cursor = db.query("city", null, "postID=?", new String[]{postID},
				null, null, null);
		if(cursor.moveToFirst()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String cityStr = cursor.getString(cursor.getColumnIndex("city"));
			String province = cursor.getString(cursor.getColumnIndex("province"));
			City city = new City(name, cityStr, province, postID);
			return city;
		}
		return null;
	}
	
	/**
	 * 通过postID读取数据库，生成City对象
	 * @param name
	 * @return
	 */
	public City readCityByName(String name){
		Cursor cursor = db.query("city", null, "name=?", new String[]{name},
				null, null, null);
		if(cursor.moveToFirst()){
			String postID = cursor.getString(cursor.getColumnIndex("postID"));
			String cityStr = cursor.getString(cursor.getColumnIndex("city"));
			String province = cursor.getString(cursor.getColumnIndex("province"));
			City city = new City(name, cityStr, province, postID);
			return city;
		}
		return null;
	}
	
	/**
	 * 通过postID读取数据库，生成City对象
	 * @param areaCode
	 * @return
	 */
	public City readCityByAreaCode(String areaCode){
		Cursor cursor = db.query("city", null, "areaCode=?", new String[]{areaCode},
				null, null, null);
		if(cursor.moveToFirst()){
			String postID = cursor.getString(cursor.getColumnIndex("postID"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String cityStr = cursor.getString(cursor.getColumnIndex("city"));
			String province = cursor.getString(cursor.getColumnIndex("province"));
			City city = new City(name, cityStr, province, postID);
			return city;
		}
		return null;
	}
	
	public List<City> readCityListByName(String chineseName){
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query("city", null, "name like ?", new String[]{chineseName+"%"},
				null, null, null);
		if(cursor.moveToFirst()){
			do{
				String postID = cursor.getString(cursor.getColumnIndex("postID"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String cityStr = cursor.getString(cursor.getColumnIndex("city"));
				String province = cursor.getString(cursor.getColumnIndex("province"));
				City city = new City(name, cityStr, province, postID);
				cityList.add(city);
			} while(cursor.moveToNext());
		}
		return cityList;
	}
	
	/**
	 * 通过邮编或电话区号前缀搜索城市
	 * @param prefix
	 * @return
	 */
	public List<City> readCityListByCode(String prefix){
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query("city", null, "areaCode like ? or phoneCode like ?", 
				new String[]{prefix+"%%", prefix+"%%"},null, null, null);
		if(cursor.moveToFirst()){
			do{
				String postID = cursor.getString(cursor.getColumnIndex("postID"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String cityStr = cursor.getString(cursor.getColumnIndex("city"));
				String province = cursor.getString(cursor.getColumnIndex("province"));
				City city = new City(name, cityStr, province, postID);
				cityList.add(city);
			} while(cursor.moveToNext());
		}
		return cityList;
	}
	
	public List<City> readCityListByPinyin(String prefix){
		List<City> cityList = new ArrayList<City>();
		Cursor cursor = db.query("city", null, "pinyin like ? or py like ?", 
				new String[]{prefix+"%%", prefix+"%%"},null, null, null);
		if(cursor.moveToFirst()){
			do{
				String postID = cursor.getString(cursor.getColumnIndex("postID"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String cityStr = cursor.getString(cursor.getColumnIndex("city"));
				String province = cursor.getString(cursor.getColumnIndex("province"));
				City city = new City(name, cityStr, province, postID);
				cityList.add(city);
			} while(cursor.moveToNext());
		}
		return cityList;
	}
	
	/**
	 * 读取热门城市表的所有记录
	 * @return
	 */
	public List<City> readHotCity(){
		List<City> hotCityList = new ArrayList<City>();
		Cursor cursor = db.query("hotcity", null, null, null,
				null, null, null);
		if(cursor.moveToFirst()){
			do{
				String postID = cursor.getString(cursor.getColumnIndex("postID"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String isSelect = cursor.getString(cursor.getColumnIndex("isSelected"));
				City city = new City(name, postID, isSelect.equals("1"));
				hotCityList.add(city);
			} while(cursor.moveToNext());
		}
		return hotCityList;
	}
	
	public boolean updateHotCitySelect(String name){
		ContentValues values = new ContentValues();
		values.put("isSelected", "1");
		return db.update("hotcity", values, "name=?", new String[]{name})==1;
	}
	
	public boolean updateHotCityNoSelect(String name){
		ContentValues values = new ContentValues();
		values.put("isSelected", "0");
		return db.update("hotcity", values, "name=?", new String[]{name})==1;
	}
	
	/**
	 * 获取数据库存放目录
	 * @param context
	 * @return
	 */
	private static String getDBDirPath(Context context) {
		return "/data" + Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + context.getPackageName() + File.separator
				+ "databases";
	}
	
	/**
	 * 将res目录下raw中的数据库文件复制到存放数据库的目录
	 * @param context
	 */
	private static void copyDB(Context context){
		InputStream in = null;
		BufferedOutputStream bos = null;
		try {
			File dir = new File(getDBDirPath(context));
			if(!dir.exists()){
				dir.createNewFile();
			}
			File datafile = new File(getDBDirPath(context)+File.separator+DB_NAME);
			if(datafile.exists())
				return;
			in = context.getResources().openRawResource(R.raw.city);
			bos = new BufferedOutputStream(new FileOutputStream(datafile));
			byte[] buffer = new byte[500];
			int length;
			while((length = in.read(buffer)) != -1){
				bos.write(buffer, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(null != in){
					in.close();
				}
				if(null != bos){
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package com.huntergao.hunterweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SelectCityOpenHelper extends SQLiteOpenHelper {

	/**
	 * 省份表建表语句
	 */
	public static final String CREATE_SELECT_CITY = "create table selectcity ("
			+ "id integer primary key autoincrement, "
			+ "name text unique, "
			+ "postID text unique,"
			+ "isLocation integer)";
	
	public SelectCityOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_SELECT_CITY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

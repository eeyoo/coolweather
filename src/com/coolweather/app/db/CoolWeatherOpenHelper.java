package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * Province表创建SQL语句
	 */
	public static final String CREATE_PROVINCE = "create table Province ("
				+ " id integer primary key autoincrement, "
				+ " province_name text, "
				+ " province_code text)";
	
	/**
	 * City表创建SQL语句
	 */
	public static final String CREATE_CITY = "";
	
	/**
	 * Country表创建SQL语句
	 */
	public static final String CREATE_COUNTRY = "";
	
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE); // 创建Province表

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		

	}

}

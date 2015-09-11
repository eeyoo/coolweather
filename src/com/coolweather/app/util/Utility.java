package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {

	/**
	 * 处理服务器返回的省份数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
			String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length >0) {
				for(String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProviceCode(array[0]);
					province.setProvinceName(array[1]);
					//随手将省份数据存储到数据库
					coolWeatherDB.saveProvince(province);
				}
				return true;//处理成功
			}
		}
		return false;//处理失败
	}
	
	/**
	 * 处理服务器返回的城市数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities !=null && allCities.length > 0) {
				for(String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//随手存储城市数据
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 处理服务器返回的县级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param cityId
	 * @return
	 */
	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountries = response.split(",");
			if (allCountries != null && allCountries.length > 0) {
				for(String co : allCountries) {
					String[] array = co.split("\\|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析服务器返回的JSON数据并存储到本地
	 * @param context
	 * @param response
	 * @return
	 */
	public synchronized static void handleWeatherResponse(Context context, String response) {
		try {			
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp");
			String temp2 = "null";
			String windDirect = weatherInfo.getString("WD");
			String windLevel = weatherInfo.getString("WS");
			String weatherDesp = weatherInfo.getString("njd");
			String publishTime = weatherInfo.getString("time");		
			
			// 保存到共享文件中
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, windDirect, windLevel, weatherDesp, publishTime);
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("feilin", "JSON解析错误 "+e.toString());
		}
	}
	
	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中
	 */
	public static void saveWeatherInfo(Context context, String cityName, String weatherCode, 
			String temp1, String temp2, String windDirect, String windLevel, String weatherDesp, String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit(); //打开编辑文件开始写入数据
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("wind_direct", windDirect);
		editor.putString("wind_level", windLevel);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit(); //提交写入内容
	}
	
	/**
	 * 中国天气API挂了，使用百度API Store的全球天气API
	 */
	public synchronized static void handleBaiduWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray weatherArray = jsonObject.getJSONArray("HeWeather data service 3.0");
			//JSONObject weatherIfo = jsonObject.getJSONObject("HeWeather data service 3.0");
			//JSONArray jsonArray = new JSONArray();
			JSONObject weatherInfo = weatherArray.getJSONObject(0);
			JSONObject basic = weatherInfo.getJSONObject("basic");
			String cityName = basic.getString("city");
			Log.i("feilin", cityName);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("feilin", "百度JSON解析错误 "+e.toString());
		}
	}
}

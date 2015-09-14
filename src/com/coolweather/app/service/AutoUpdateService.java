package com.coolweather.app.service;

import com.coolweather.app.model.Country;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.R.string;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class AutoUpdateService extends Service {

	private static long count = 1;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//return super.onStartCommand(intent, flags, startId);
		/*new Thread(new Runnable() { //访问网络子线程
			
			@Override
			public void run() {
				Log.i("AutoUpdatService", "background running..." + count++);
				updateWeather();
			}
		}).start();*/
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = preferences.getString("weather_code", "");
		String address = "http://www.weather.com.cn/adat/sk/" + weatherCode + ".html";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {//访问网络统一方法
			
			@Override
			public void onFinish(String response) {
				//成功后保存天气信息
				Log.i("update_servie", "run times "+count++);
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace(); //访问失败
			}
		});
		
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		//int anHour = 8 * 60 * 60 * 1000; //8小时毫秒数
		int anHour = 100; //100毫秒更新一次
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent me = new Intent(this, AutoUpdateService.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, me, 0);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 更新天气
	 */
	private void updateWeather() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = preferences.getString("weather_code", "");
		String address = "http://www.weather.com.cn/adat/sk/" + weatherCode + ".html";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {//访问网络统一方法
			
			@Override
			public void onFinish(String response) {
				//成功后保存天气信息
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace(); //访问失败
			}
		});
	}

}

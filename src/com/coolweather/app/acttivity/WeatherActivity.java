package com.coolweather.app.acttivity;


import java.security.Timestamp;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	/**
	 * 显示城市名
	 */
	private TextView cityNameText;
	
	/**
	 * 显示发布时间
	 */
	private TextView publishTimeText;
	/**
	 * 显示天气信息
	 */
	private TextView weatherDespText;
	/**
	 * 显示最低温度
	 */
	private TextView temp1Text;
	/**
	 * 显示最高温度
	 */
	private TextView temp2Text;
	/**
	 * 显示日期
	 */
	private TextView currentDateText;
	
	/**
	 * 快速切换城市按钮
	 */
	private Button switchCityBtn;
	
	/**
	 * 更新天气按钮
	 */
	private Button refreshWeatherBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉自带title
		setContentView(R.layout.weather_info_layout);
		//初始化UI控件
		//Log.i("feilin", "LinearLayout ");
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		Log.i("feilin", "LinearLayout ");
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishTimeText = (TextView) findViewById(R.id.publish_text);		
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		
		switchCityBtn = (Button) findViewById(R.id.switch_city);
		switchCityBtn.setOnClickListener(this);
		refreshWeatherBtn = (Button) findViewById(R.id.refresh_weather);
		refreshWeatherBtn.setOnClickListener(this);
		
		String countryCode = getIntent().getExtras().getString("country_code");
		Log.i("feilin", "coutry code " + countryCode);
		if (!TextUtils.isEmpty(countryCode)) {
			//有县级代号就去查询天气
			publishTimeText.setText("正在同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			//县级代号获取服务器天气信息
			//Log.i("feilin", "去服务器获取天气信息");
			queryWeatherCode(countryCode);
			
		} else {
			//无县级代号就读取本地天气信息
			//Log.i("feilin", "获取本地天气信息");
			//Log.i("feilin", "显示本地天气信息");
			showWeather();
		}
		
	}
	
	/**
	 * 由县级代号查询天气代号
	 */
	private void queryWeatherCode(String countryCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + countryCode + ".xml";
		//从服务器获取该县的天气信息
		queryFromServer(address, "countryCode");
	}
	
	/**
	 * 由天气代号查询天气
	 * @param weatherCode
	 */
	private void queryWeatherInfo(String weatherCode) {
		//String address = "http://www.weather.com.cn/data/cityInfo/" + weatherCode + ".html";
		String address = "http://www.weather.com.cn/adat/sk/" + weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	
	/**
	 * 根据传入网址和天气代号向服务器获取天气信息
	 */
	private void queryFromServer(final String address, final String type) {
		
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				//Log.i("feilin", "onFinish");
				if ("countryCode".equals(type)) {
					//Log.i("feilin", "type countryCode "+type);
					if (!TextUtils.isEmpty(response)) {
						
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);//由服务器查询天气信息
						}
					}
				} else if ("weatherCode".equals(type)) {//获取本地天气信息
					//传入天气代号时
					//Log.i("feilin", "address "+address);
					//Log.i("feilin", "response "+response);
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//显示天气信息
							showWeather(); 
						}
					});
				} 
			}
			
			@Override
			public void onError(Exception e) {
				//Log.i("feilin", "onError");
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publishTimeText.setText("同步失败!");
					}
				});
			}
		});
	}
	
	private void showWeather() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String cityName = preferences.getString("city_name", "null");
		String publishTime = preferences.getString("publish_time", "null");
		String weatherCode = preferences.getString("weather_code", "null");
		String weatherDesp = preferences.getString("weather_desp", "null");
		String temp1 = preferences.getString("temp1", "null");
		String temp2 = preferences.getString("temp2", "null");
		String currentDate = preferences.getString("current_date", "null");
		Log.i("feilin", "city name "+cityName);
		Log.i("feilin", "publist time "+publishTime);
		Log.i("feilin", "weather code "+weatherCode);
		Log.i("feilin", "weather desp "+weatherDesp);
		Log.i("feilin", "min temp "+temp1);
		Log.i("feilin", "max temp "+temp2);
		Log.i("feilin", "current date "+currentDate);
		
		cityNameText.setText(cityName);
		publishTimeText.setText("今天" + publishTime + "发布");
		weatherDespText.setText(weatherDesp);
		temp1Text.setText(temp1);
		temp2Text.setText(temp2);
		currentDateText.setText(currentDate);
		
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
	
	/**
	 * 向百度请求，需要apikey
	 */
	private void queryBaiduWeatherInfo(String countryName) {
		String address = "http://apis.baidu.com/heweather/weather/free?city=" + countryName;
		//Log.i("feilin", "address "+address);
		//向百度请求天气信息
		//queryFromBaiduApi(address);
		HttpUtil.sendBaiduRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				//Log.i("feilin", "response "+response);
				//网络请求
				Utility.handleBaiduWeatherResponse(WeatherActivity.this, response);
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			//Toast.makeText(WeatherActivity.this, "切换城市", 1).show();
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_acitvity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			//Toast.makeText(this, "玩命更新中...", 1).show();
			publishTimeText.setText("玩命更新中...");
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = preferences.getString("weather_code", "");
			queryWeatherInfo(weatherCode);
			break;

		default:
			break;
		}
	}
}
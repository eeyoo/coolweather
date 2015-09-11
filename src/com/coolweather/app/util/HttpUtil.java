package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	private static final String BAIDU_SPI_KEY = "03fc1a3acd7675c7092b1bf843589560";

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(800);
					connection.setReadTimeout(800);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						//回调接口onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						//回调接口onError()方法
						listener.onError(e);
					}
				}finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
	
	/**
	 * 使用百度API商店 - 全球天气
	 * @param address
	 * @param apiKey
	 * @param listener
	 */
	public static void sendBaiduRequest(final String address,
			final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setRequestProperty("apikey", BAIDU_SPI_KEY);
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
						response.append("\r\n");
					}
					if (listener != null) {
						//回调接口onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						//回调接口onError()方法
						listener.onError(e);
					}
				}finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
				
			}
		}).start();
	}
}

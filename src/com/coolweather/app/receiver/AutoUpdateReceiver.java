package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Log.i("feilin", "OK start update weather service");
		Intent updateServie = new Intent(context, AutoUpdateService.class);
		//启动定时更新天气服务
		context.startService(updateServie);
	}

}

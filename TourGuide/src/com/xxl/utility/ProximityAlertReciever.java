package com.xxl.utility;

import com.work.map.SpotIntroductionActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

//临近警告接受器类
public class ProximityAlertReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 获取是否为进入指定区域
		boolean isEnter = intent.getBooleanExtra(
				LocationManager.KEY_PROXIMITY_ENTERING, false);
		if (isEnter) {
			System.out.println("进入广播接收者 ，并且执行到跳转代码");
			Toast.makeText(context, "您已进入鹞子翻身景点", Toast.LENGTH_LONG).show();

			Intent intentToSpotIntroduction = new Intent(context,
					SpotIntroductionActivity.class);
			intentToSpotIntroduction.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentToSpotIntroduction);

		} else {
			System.out.println("进入广播接收者 ，但是没有跳转");
		}
	}
}

package com.xxl.utility;

import com.work.map.SpotIntroductionActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

//�ٽ������������
public class ProximityAlertReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// ��ȡ�Ƿ�Ϊ����ָ������
		boolean isEnter = intent.getBooleanExtra(
				LocationManager.KEY_PROXIMITY_ENTERING, false);
		if (isEnter) {
			System.out.println("����㲥������ ������ִ�е���ת����");
			Toast.makeText(context, "���ѽ������ӷ�����", Toast.LENGTH_LONG).show();

			Intent intentToSpotIntroduction = new Intent(context,
					SpotIntroductionActivity.class);
			intentToSpotIntroduction.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentToSpotIntroduction);

		} else {
			System.out.println("����㲥������ ������û����ת");
		}
	}
}

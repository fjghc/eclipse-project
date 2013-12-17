package com.xxl.notification;

import com.work.map.MyMapActivity;
import com.work.map.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class notificationReciever extends BroadcastReceiver {

	Context context = null;
	@Override
	public void onReceive(Context con, Intent intent) {
		this.context = con;
		String data = intent.getStringExtra("notification");
		showNotification(con,data);
	}
	private void showNotification(Context context,String s) {
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		long when = System.currentTimeMillis();
		Intent notificationIntent = new Intent(context,MyMapActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		Notification notification = new Notification(R.drawable.notification, "管理员通知", when);
		notification.defaults = Notification.DEFAULT_VIBRATE;
		notification.setLatestEventInfo(context, "管理员通知","逍遥游:"+ s,
				contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(0, notification);
	}

}

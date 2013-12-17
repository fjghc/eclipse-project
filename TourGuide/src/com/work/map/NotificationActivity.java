package com.work.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

@SuppressLint("ParserError")
public class NotificationActivity extends Activity {
	private static final String TAG = "NotificationActivityÏûÏ¢";
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setContentView(R.layout.alarmnotification);
	
		Log.d(TAG,"---------------->onCreate");
		intent = this.getIntent();
		String msg = intent.getStringExtra("msg");
		Log.d(TAG, "onCreate---------->"+msg);
		TextView showView = (TextView)this.findViewById(R.id.notification);
		showView.setText(msg);
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "---------------->onRestart");
		String msg = intent.getStringExtra("msg");
		Log.d(TAG, "onRestart---------->"+msg);
		TextView showView = (TextView)this.findViewById(R.id.notification);
		showView.setText(msg);
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "---------------->onResume");
		String msg = intent.getStringExtra("msg");
		Log.d(TAG, "onResume---------->"+msg);
		TextView showView = (TextView)this.findViewById(R.id.notification);
		showView.setText(msg);
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "---------------->onStart");
		String msg = intent.getStringExtra("msg");
		Log.d(TAG, "onStart---------->"+msg);
		TextView showView = (TextView)this.findViewById(R.id.notification);
		showView.setText(msg);
		super.onStart();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return false;
	}	
}

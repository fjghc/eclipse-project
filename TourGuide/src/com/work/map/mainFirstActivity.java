package com.work.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class mainFirstActivity extends Activity {

	private Button intoButton;
	private Button loginButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.work.map.R.layout.main_first);
		intoButton = (Button) findViewById(com.work.map.R.id.main_into_btn);
		loginButton = (Button) findViewById(com.work.map.R.id.main_login_btn);
		intoButton.setOnClickListener(new IntoButtonListener());
		loginButton.setOnClickListener(new LoginButtonListener());
	}

	class IntoButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			// 设置转向地图的intent
			intent.setClass(mainFirstActivity.this, MyMapActivity.class);
			mainFirstActivity.this.startActivity(intent);
			finish();
		}
	}

	class LoginButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(mainFirstActivity.this, FreelyVisitActivity.class);
			mainFirstActivity.this.startActivity(intent);
			finish();
		}

	}
}

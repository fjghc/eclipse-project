package com.work.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class accountManageActivity extends Activity{

	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.account_manage);
		button=(Button) findViewById(R.id.edit_password_btn);
		button.setOnClickListener(new EditButtonListener());
	}
	class EditButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			findViewById(R.id.edit_password_layout).setVisibility(0);
		}
	}
 
}

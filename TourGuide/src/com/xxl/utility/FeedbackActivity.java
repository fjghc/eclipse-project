package com.xxl.utility;

import java.util.HashMap;
import java.util.Map;

import com.work.map.R;
import com.xxl.network.HttpRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ParserError")
public class FeedbackActivity extends Activity {
	private static final int FEEDBACK_ERROR = 1000;
	private static final int FEEDBACK_OK = 1001;
	private static final String TAG = "FeedbackActivity";
	private Button sendBtn = null;
	private EditText feedback = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setContentView(R.layout.feedback_main);
		this.sendBtn = (Button)this.findViewById(R.id.sendFeedbackBtn);
		this.sendBtn.setOnClickListener(new SendFeedbackBtnListener());
		this.feedback = (EditText)this.findViewById(R.id.advice_input);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id){
			case FEEDBACK_ERROR:
			Log.d(TAG, "���ͷ�����Ϣ����");
			Builder sendFeedbackErrorBuilder = new AlertDialog.Builder(this); // ����Builder����
			sendFeedbackErrorBuilder.setTitle("��ʾ");
			sendFeedbackErrorBuilder.setMessage("�����������ʧ�ܣ����Ժ��ԡ�");
			sendFeedbackErrorBuilder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = sendFeedbackErrorBuilder.create(); // ����Dialog����
			break;
			case FEEDBACK_OK:
				Log.d(TAG, "���������Ϣ���ͳɹ�");
				Builder sendFeedbackOKBuilder = new AlertDialog.Builder(this); // ����Builder����
				sendFeedbackOKBuilder.setTitle("��ϲ��");
				sendFeedbackOKBuilder.setMessage("���������Ϣ���ͳɹ���");
				sendFeedbackOKBuilder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				dialog = sendFeedbackOKBuilder.create(); // ����Dialog����
				break;
			default:
				Log.d(TAG, "����������ֶԻ����������");
		}
		
		return dialog;
	}

	class SendFeedbackBtnListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			boolean isSuccessful = false;
			String input = FeedbackActivity.this.feedback.getText().toString();
			if(input.equals("")){
				Toast.makeText(FeedbackActivity.this, "���������ı������", 1).show();
			}else{
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", dataStorage.user_name);
				params.put("feedback", input);

				try {
					isSuccessful = HttpRequest.sendPostRequest(
							GetServerIPAddress.getIpAddress("ipaddress")+FeedbackActivity.this.getResources().getString(
									R.string.sendFeedbackURL), params, "UTF-8");
				} catch (Exception e) {
					Log.d(TAG, "����������ַ���������쳣");
					e.printStackTrace();
				} 
			}
			if(isSuccessful){
				showDialog(FeedbackActivity.FEEDBACK_OK);
			}else{
				showDialog(FeedbackActivity.FEEDBACK_ERROR);
			}
			
		}
		
	}

}

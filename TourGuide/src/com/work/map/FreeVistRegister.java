package com.work.map;

import java.util.HashMap;
import java.util.Map;

import com.work.map.FreelyVisitActivity.myThread;
import com.xxl.network.HttpRequest;
import com.xxl.utility.GetServerIPAddress;
import com.xxl.utility.dataStorage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ParserError", "ParserError" })
public class FreeVistRegister extends Activity {

	private static final String TAG = "Register debug info";
	private static final int PROGRESS_BAR_DIALOG = 1;
	private static final int REGISTER_ERROR_DIALOG = 0;
	public static final int REGISTER_END = 2;
	private static final int TURN_TO_MAP_DIALOG = 3;
//	private ProgressDialog registerProBar;
	private Button registerBtn;// ע�ᰴť
	private EditText userNameInput;
	private EditText telNumInput;
	private EditText passWdInput;
	private EditText conformPassWdInput;// ȷ�����������
	public Handler myHandler;
	private String userName;
	private String telNum;
	private String passWd;
	private String conformPassWd;
	private ProgressDialog register_Dlg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.work.map.R.layout.register);
		registerBtn = (Button) findViewById(R.id.regist_btn);
		userNameInput = (EditText) this.findViewById(R.id.user_name);
		telNumInput = (EditText) this.findViewById(R.id.tel_number);
		passWdInput = (EditText) this.findViewById(R.id.register_password);
		conformPassWdInput = (EditText) this
				.findViewById(R.id.register_password_second);
		registerBtn.setOnClickListener(new registerButtonListener());
	}

	protected Dialog onCreateDialog(int id) {
		Dialog registerDialog = null;
		switch (id) {
		case REGISTER_ERROR_DIALOG:
			Builder b2 = new AlertDialog.Builder(this); // ����Builder����

			b2.setMessage(FreeVistRegister.this.getResources().getString(
					R.string.register_error));
			b2.setTitle("��ʾ"); // ���ñ���
			b2.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			registerDialog = b2.create(); // ����Dialog����
			break;

		case PROGRESS_BAR_DIALOG:
			Log.d(TAG, "case progressbar");
			this.register_Dlg = new ProgressDialog(this);
			this.register_Dlg.setMessage("���ڷ���ע����Ϣ...");
			this.register_Dlg.setTitle("��ȴ� ");
			this.register_Dlg.setCancelable(true);
			this.register_Dlg.show();
			new Thread(new registerThread()).start();

			break;
		case TURN_TO_MAP_DIALOG:
			Log.d(TAG, "Turn to mymapActivity!");
			Builder b3 = new AlertDialog.Builder(this); // ����Builder����

			b3.setMessage(FreeVistRegister.this.getResources().getString(
					R.string.register_success));
			b3.setTitle("��ʾ"); // ���ñ���
			b3.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataStorage.is_login = true;
					dataStorage.user_name = FreeVistRegister.this.userName;
					dialog.dismiss();
				}
			});
			registerDialog = b3.create(); // ����Dialog����
			break;
		}
		return registerDialog;

	}

	class registerButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			FreeVistRegister.this.userName = FreeVistRegister.this.userNameInput
					.getText().toString();
			FreeVistRegister.this.telNum = FreeVistRegister.this.telNumInput
					.getText().toString();
			FreeVistRegister.this.passWd = FreeVistRegister.this.passWdInput
					.getText().toString();
			FreeVistRegister.this.conformPassWd = FreeVistRegister.this.conformPassWdInput
					.getText().toString();
			if (FreeVistRegister.this.userName.equals("")
					|| FreeVistRegister.this.telNum.equals("")
					|| FreeVistRegister.this.passWd.equals("")
					|| FreeVistRegister.this.conformPassWd.equals("")) {
				Toast.makeText(FreeVistRegister.this, "ע�����Ϊ��",
						Toast.LENGTH_LONG).show();
			} else {
				if (!FreeVistRegister.this.passWd
						.equals(FreeVistRegister.this.conformPassWd)) {
					// ������ȷ�����벻��ͬ
					Toast.makeText(FreeVistRegister.this,
							"ȷ���������������벻��ͬ�����������룺", Toast.LENGTH_LONG).show();
				} else {
					if (FreeVistRegister.this.passWd.length() < 6
							|| FreeVistRegister.this.passWd.length() > 15) {
						Toast.makeText(FreeVistRegister.this,
								"������6��15λӢ���ַ�����������ɣ����������룺", Toast.LENGTH_LONG)
								.show();
					} else {
						// ����������׼�������������ע����Ϣ

						FreeVistRegister.this
								.showDialog(FreeVistRegister.PROGRESS_BAR_DIALOG);

						Log.d(TAG, "show dialog has been called");
						myHandler = new Handler() {
							public void handleMessage(Message msg) {
								switch (msg.what) {
								case FreeVistRegister.REGISTER_END:
									// ������Ϣ�����е���Ϣ����ϢӦ��Ϊע��ɹ�����ע��ʧ��
									// �õ�������ȹرս������Ի���
									FreeVistRegister.this.register_Dlg
											.dismiss();
									switch (msg.arg1) {
									case 1:// ע��ɹ�
										Log.d(TAG, "ע��ɹ�");
										showDialog(FreeVistRegister.TURN_TO_MAP_DIALOG);
										Intent intent = new Intent();
										intent.setClass(FreeVistRegister.this,
												MyMapActivity.class);
										FreeVistRegister.this
												.startActivity(intent);
										FreeVistRegister.this.finish();
										break;
									case 2:// �û����Ѵ���
										Log.d(TAG, "�û����Ѵ���");
										showDialog(FreeVistRegister.REGISTER_ERROR_DIALOG);
										break;
									case 3:
										Log.d(TAG, "ע�ᳬʱ���������Ӵ���");
										Toast.makeText(
												FreeVistRegister.this,
												FreeVistRegister.this.getResources()
														.getString(
																R.string.network_error),
												Toast.LENGTH_SHORT).show();
										break;
									default:
										Log.d(TAG, "ע�Ჿ�ִ������̷߳��صĽ���Ǵ����");

									}
									break;
								}
								super.handleMessage(msg);
							}
						};

					}
				}
			}
		}
	}

	// �߳��࣬�����緢��ע����Ϣ
	class registerThread implements Runnable {
		private boolean isRegister;
		private int register_status_code;

		public void run() {
			try {
				isRegister = false;
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", FreeVistRegister.this.userName);
				params.put("phonenumber", FreeVistRegister.this.telNum);
				params.put("password", FreeVistRegister.this.passWd);
				isRegister = HttpRequest.sendPostRequest(GetServerIPAddress.getIpAddress("ipaddress.txt")+FreeVistRegister.this
						.getResources().getString(R.string.RegisterURL), params,
						"UTF-8");

				if (isRegister) {// ��½�ɹ�
					this.register_status_code = 1;
				} else {// �û������������
					this.register_status_code = 2;
				}

			} catch (Exception e) {
				// �������Ӵ���
				register_status_code = 3;
				Log.d(TAG, "�������Ӵ���");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = FreeVistRegister.REGISTER_END;
			message.arg1 = this.register_status_code;
			FreeVistRegister.this.myHandler.sendMessage(message);
		}
	}

}

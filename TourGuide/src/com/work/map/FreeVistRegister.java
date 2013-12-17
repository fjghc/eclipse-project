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
	private Button registerBtn;// 注册按钮
	private EditText userNameInput;
	private EditText telNumInput;
	private EditText passWdInput;
	private EditText conformPassWdInput;// 确认密码输入框
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
			Builder b2 = new AlertDialog.Builder(this); // 创建Builder对象

			b2.setMessage(FreeVistRegister.this.getResources().getString(
					R.string.register_error));
			b2.setTitle("提示"); // 设置标题
			b2.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			registerDialog = b2.create(); // 生成Dialog对象
			break;

		case PROGRESS_BAR_DIALOG:
			Log.d(TAG, "case progressbar");
			this.register_Dlg = new ProgressDialog(this);
			this.register_Dlg.setMessage("正在发送注册信息...");
			this.register_Dlg.setTitle("请等待 ");
			this.register_Dlg.setCancelable(true);
			this.register_Dlg.show();
			new Thread(new registerThread()).start();

			break;
		case TURN_TO_MAP_DIALOG:
			Log.d(TAG, "Turn to mymapActivity!");
			Builder b3 = new AlertDialog.Builder(this); // 创建Builder对象

			b3.setMessage(FreeVistRegister.this.getResources().getString(
					R.string.register_success));
			b3.setTitle("提示"); // 设置标题
			b3.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataStorage.is_login = true;
					dataStorage.user_name = FreeVistRegister.this.userName;
					dialog.dismiss();
				}
			});
			registerDialog = b3.create(); // 生成Dialog对象
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
				Toast.makeText(FreeVistRegister.this, "注册项不能为空",
						Toast.LENGTH_LONG).show();
			} else {
				if (!FreeVistRegister.this.passWd
						.equals(FreeVistRegister.this.conformPassWd)) {
					// 密码与确认密码不相同
					Toast.makeText(FreeVistRegister.this,
							"确认密码与输入密码不相同，请重新输入：", Toast.LENGTH_LONG).show();
				} else {
					if (FreeVistRegister.this.passWd.length() < 6
							|| FreeVistRegister.this.passWd.length() > 15) {
						Toast.makeText(FreeVistRegister.this,
								"密码由6—15位英文字符或者数字组成，请重新输入：", Toast.LENGTH_LONG)
								.show();
					} else {
						// 输入正常，准备向服务器发送注册信息

						FreeVistRegister.this
								.showDialog(FreeVistRegister.PROGRESS_BAR_DIALOG);

						Log.d(TAG, "show dialog has been called");
						myHandler = new Handler() {
							public void handleMessage(Message msg) {
								switch (msg.what) {
								case FreeVistRegister.REGISTER_END:
									// 处理消息队列中的消息，消息应当为注册成功或者注册失败
									// 得到结果后先关闭进度条对话框
									FreeVistRegister.this.register_Dlg
											.dismiss();
									switch (msg.arg1) {
									case 1:// 注册成功
										Log.d(TAG, "注册成功");
										showDialog(FreeVistRegister.TURN_TO_MAP_DIALOG);
										Intent intent = new Intent();
										intent.setClass(FreeVistRegister.this,
												MyMapActivity.class);
										FreeVistRegister.this
												.startActivity(intent);
										FreeVistRegister.this.finish();
										break;
									case 2:// 用户名已存在
										Log.d(TAG, "用户名已存在");
										showDialog(FreeVistRegister.REGISTER_ERROR_DIALOG);
										break;
									case 3:
										Log.d(TAG, "注册超时，网络连接错误");
										Toast.makeText(
												FreeVistRegister.this,
												FreeVistRegister.this.getResources()
														.getString(
																R.string.network_error),
												Toast.LENGTH_SHORT).show();
										break;
									default:
										Log.d(TAG, "注册部分错误！子线程返回的结果是错误的");

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

	// 线程类，向网络发送注册信息
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

				if (isRegister) {// 登陆成功
					this.register_status_code = 1;
				} else {// 用户名或密码错误
					this.register_status_code = 2;
				}

			} catch (Exception e) {
				// 网络连接错误
				register_status_code = 3;
				Log.d(TAG, "网络连接错误");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = FreeVistRegister.REGISTER_END;
			message.arg1 = this.register_status_code;
			FreeVistRegister.this.myHandler.sendMessage(message);
		}
	}

}

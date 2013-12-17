package com.work.map;

import java.util.HashMap;
import java.util.Map;

import com.work.map.MyMapActivity.getMyRouteThread;
import com.xxl.network.*;
import com.xxl.utility.GetServerIPAddress;
import com.xxl.utility.dataStorage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ParserError", "ShowToast", "ParserError",
		"ParserError", "ParserError", "ParserError", "ParserError",
		"ParserError", "NewApi", "NewApi" })
public class FreelyVisitActivity extends Activity {
	/** Called when the activity is first created. */
	private Button registButton;// 注册按钮
	private Button loginButton;// 登陆按钮
	private EditText userName;
	private EditText passWd;
	private String name;
	private String password;
	private ProgressDialog login_Pro_Dlg;
	public Handler myHandler;
	private final static int LOGIN_ERROR_DIALOG = 1;
	private final static String TAG = "FreelyVisitActivity debug info";
	private static final int PROGRESS_BAR_DIALOG = 2;
	private static final int LOGIN_END = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.work.map.R.layout.login_main);
		registButton = (Button) findViewById(com.work.map.R.id.regist);
		loginButton = (Button) findViewById(com.work.map.R.id.login);
		this.userName = (EditText) findViewById(R.id.name);
		this.passWd = (EditText) findViewById(R.id.passwd);
		registButton.setOnClickListener(new RegistButtonListener());
		loginButton.setOnClickListener(new LoginButtonListener());
	}

	//注册按钮响应
	class RegistButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(FreelyVisitActivity.this, FreeVistRegister.class);
			FreelyVisitActivity.this.startActivity(intent);
			FreelyVisitActivity.this.finish();
		}

	}

	@SuppressLint("ParserError")
	// 登录按钮的响应
	class LoginButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
             //隐藏软键盘
//             imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
             //显示软键盘
//             imm.showSoftInputFromInputMethod(tv.getWindowToken(), 0);
             //切换软键盘的显示与隐藏
             imm.toggleSoftInputFromWindow(v.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);

			FreelyVisitActivity.this.name = FreelyVisitActivity.this.userName
					.getText().toString();
			FreelyVisitActivity.this.password = FreelyVisitActivity.this.passWd
					.getText().toString();
			if (FreelyVisitActivity.this.name.equals("")
					|| FreelyVisitActivity.this.password.equals("")) {
				Toast.makeText(FreelyVisitActivity.this, "用户名或密码不能为空，请重新输入！",
						Toast.LENGTH_LONG).show();
			} else {
				FreelyVisitActivity.this
						.showDialog(FreelyVisitActivity.PROGRESS_BAR_DIALOG);

				myHandler = new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case FreelyVisitActivity.LOGIN_END:
							// 处理消息队列中的消息，消息应当为登录成功或者是网络错误或者是用户名或密码错误
							// 得到结果后先关闭进度条对话框
							FreelyVisitActivity.this.login_Pro_Dlg.dismiss();
							switch (msg.arg1) {
							case 1:// 登录成功的响应
								Log.d(TAG, "登录成功");
								Intent intent = new Intent();
								intent.setClass(FreelyVisitActivity.this,
										MyMapActivity.class);
								FreelyVisitActivity.this.startActivity(intent);
								dataStorage.is_login = true;
								dataStorage.user_name = FreelyVisitActivity.this.name;
								finish();
								break;
							case 2:// 用户名或密码错误的响应
								Log.d(TAG, "用户名或密码错误");
								showDialog(LOGIN_ERROR_DIALOG);
								break;
							case 3:
								Log.d(TAG, "登陆超时，网络连接错误");
								Toast.makeText(
										FreelyVisitActivity.this,
										FreelyVisitActivity.this.getResources()
												.getString(
														R.string.network_error),
										Toast.LENGTH_SHORT).show();
								break;
							default:
								Log.d(TAG, "登陆部分错误！子线程返回的结果是错误的");

							}
							break;
						}
						super.handleMessage(msg);
					}
				};

			}

		}

	}

	class myThread implements Runnable {
		private boolean isLogin;
		private int login_status_code;

		public void run() {
			try {
				isLogin = false;
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", FreelyVisitActivity.this.name);
				params.put("password", FreelyVisitActivity.this.password);
				isLogin = HttpRequest.sendPostRequest(GetServerIPAddress.getIpAddress("ipaddress.txt")+
						FreelyVisitActivity.this
						.getResources().getString(R.string.LoginURL), params,
						"UTF-8");

				if (isLogin) {// 登陆成功
					this.login_status_code = 1;
				} else {// 用户名或密码错误
					this.login_status_code = 2;
				}

			} catch (Exception e) {
				// 网络连接错误
				login_status_code = 3;
				Log.d(TAG, "网络连接错误");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = FreelyVisitActivity.LOGIN_END;
			message.arg1 = this.login_status_code;
			FreelyVisitActivity.this.myHandler.sendMessage(message);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		Dialog loginDialog = null;
		switch (id) {
		case LOGIN_ERROR_DIALOG:

			Builder b2 = new AlertDialog.Builder(this); // 创建Builder对象
			b2.setMessage(this.getResources().getString(R.string.login_error));
			b2.setTitle("提示"); // 设置标题
			b2.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			loginDialog = b2.create(); // 生成Dialog对象
			break;

		case PROGRESS_BAR_DIALOG:
			Log.d(TAG, "case progressbar");
			this.login_Pro_Dlg = new ProgressDialog(this);
			this.login_Pro_Dlg.setMessage("正在为您登录...");
			this.login_Pro_Dlg.setTitle("请等待 ");
			this.login_Pro_Dlg.setCancelable(true);
			this.login_Pro_Dlg.show();
			new Thread(new myThread()).start();
			break;
		}
		return loginDialog;
	}
}
package com.work.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.ShareActivity;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class shareActivity extends Activity {
	private ImageView imageView;
	private Button login_button;
	private Weibo mWeibo;
	// private ProgressDialog p_dialog;
	// private ProgressDialog release_dialog;
	private TextView textview;

	private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://TimeLineActivity";
	private static final String FROM = "xweibo";

	// 设置appkey及appsecret，如何获取新浪微博appkey和appsecret请另外查询相关信息，此处不作介绍
	private static final String CONSUMER_KEY = "651127873";// 替换为开发者的appkey，例如"1646212960";
	private static final String CONSUMER_SECRET = "ac332bf651ec8eeca38a7f44c416bd47";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";

	private String username = "";
	private String password = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);

		// this.login_button.setOnClickListener(new loginBtnClickListener());
		// textview = (TextView) findViewById(R.id.share_login_textview);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		Bitmap bmp;
		bmp = (Bitmap) extras.get("data");
		
		this.saveMyBitmap("weibo", bmp);

//		BitmapFactory.Options opt = new BitmapFactory.Options();
//		 这个isjustdecodebounds很重要
//		opt.inJustDecodeBounds = true;
//		opt.inSampleSize = 2;
//		bmp = BitmapFactory.decodeFile(
//				Environment.getExternalStorageDirectory().getAbsolutePath()
//						+ "/photo.jpg", opt);

		// 用imageview显示出bitmap

		imageView = (ImageView) findViewById(R.id.image);
		imageView.setImageBitmap(bmp);

		login_button = (Button) this.findViewById(R.id.sharelogin);
		login_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == login_button) {
					mWeibo = Weibo.getInstance();
					mWeibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

					// Oauth2.0
					// 隐式授权认证方式
					mWeibo.setRedirectUrl("http://hi.baidu.com/focuswenle/ihome/ihomefeed");// 此处回调页内容应该替换为与appkey对应的应用回调页
					// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
					// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
					// 应用回调页不可为空

					mWeibo.authorize(shareActivity.this,
							new AuthDialogListener());

				}
			}
		});

	}

	public void saveMyBitmap(String bitName, Bitmap mBitmap) {
		File f = new File("/sdcard/" + bitName + ".png");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("shareActivity", "在保存图片时出错：" + e.toString());
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onResume() {
		super.onResume();
	}

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			// mToken.setText("access_token : " + token + "  expires_in: "+
			// expires_in);
			Log.d("acc tok:::::::::", "access_token : " + token
					+ "  expires_in: " + expires_in);

			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);

			File file = Environment.getExternalStorageDirectory();
			String sdPath = file.getAbsolutePath();
			// 请保证SD卡根目录下有这张图片文件
			String picPath = sdPath + "/" + "weibo.png";
			File picFile = new File(picPath);
			if (!picFile.exists()) {
				Toast.makeText(shareActivity.this, "图片" + picPath + "不存在！",
						Toast.LENGTH_SHORT).show();
				picPath = null;
			}
			try {
				share2weibo("", picPath);
				Intent i = new Intent(shareActivity.this, ShareActivity.class);
				shareActivity.this.startActivity(i);

			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}

			// Intent intent = new Intent();
			// intent.setClass(shareActivity.this, WeiboActivity.class);
			// startActivity(intent);
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	private void share2weibo(String content, String picPath)
			throws WeiboException {
		Weibo weibo = Weibo.getInstance();
		weibo.share2weibo(this, weibo.getAccessToken().getToken(), weibo
				.getAccessToken().getSecret(), content, picPath);
	}

	private String getPublicTimeline(Weibo weibo) throws MalformedURLException,
			IOException, WeiboException {
		String url = Weibo.SERVER + "statuses/public_timeline.json";
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Weibo.getAppKey());
		String rlt = weibo.request(this, url, bundle, "GET",
				mWeibo.getAccessToken());
		return rlt;
	}

	private String upload(Weibo weibo, String source, String file,
			String status, String lon, String lat) throws WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("pic", file);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		try {
			rlt = weibo.request(this, url, bundle, Utility.HTTPMETHOD_POST,
					mWeibo.getAccessToken());
		} catch (WeiboException e) {
			throw new WeiboException(e);
		}
		return rlt;
	}

	private String update(Weibo weibo, String source, String status,
			String lon, String lat) throws WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/update.json";
		rlt = weibo.request(this, url, bundle, Utility.HTTPMETHOD_POST,
				mWeibo.getAccessToken());
		return rlt;
	}

}

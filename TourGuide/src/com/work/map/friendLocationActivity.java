package com.work.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xxl.network.GetInfoFromServer;
import com.xxl.utility.GetServerIPAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

@SuppressLint({ "ParserError", "ParserError", "ParserError" })
public class friendLocationActivity extends Activity {

	private static final int GET_FRIEND_LOCATION_ERROR = 1000;
	private static final String TAG  = "friendLocationActivity";
	private Button addFriendBtn;// 添加好友按钮
	private ListView lv;// 好友列表ListView控件
	private ArrayList<String> friendNameList;
	private ArrayList<String> picNameList;

	int[] drawableIds = { R.drawable.photo1, R.drawable.photo2,
			R.drawable.photo3, R.drawable.photo4, R.drawable.photo5 };
	int[] accounts = { R.string.fan, R.string.friend_name1, R.string.liujiao,
			R.string.szh, R.string.miy };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.friendlocation);
		lv = (ListView) this.findViewById(R.id.friendLocationListView);

		this.friendNameList = new ArrayList<String>(); 
		String friendListJsonData = this.getIntent().getStringExtra("friendlist");
		this.picNameList = new ArrayList<String>();
		this.friendNameList = new ArrayList<String>();
		if(friendListJsonData.indexOf("nofriends")==-1){
			//此处解析json格式的数据，然后在界面中显示出好友
//			this.friendNameList.add(object);
			Log.d(TAG, "好友列表得到的json数据为："+friendListJsonData);
			try {
				JSONArray friendListJson = new JSONArray(friendListJsonData);
				for(int i=0;i<friendListJson.length();i++){
					JSONObject friend = (JSONObject)friendListJson.get(i); 
					String picName = String.valueOf(friend.getInt("picture"))+".png";
					this.picNameList.add(picName);
					this.friendNameList.add(friend.getString("username"));
				}
				
			} catch (JSONException e) {
				Log.d(TAG,"好友列表返回数据解析错误");
				e.printStackTrace();
			}
			
			
		}else{
			//好友列表为空，告知用户
			Toast.makeText(this, "好友列表为空，请添加好友", 1).show();
		}

		
		
		this.addFriendBtn = (Button) this.findViewById(R.id.addFriendBtn);
		this.addFriendBtn.setOnClickListener(new addFriendBtnOnClickListener());

		class MyBaseAdapter extends BaseAdapter {
			public int getCount() {
				return friendLocationActivity.this.friendNameList.size();
			}// 总共5个选项

			public Object getItem(int arg0) {
				return null;
			}

			public long getItemId(int arg0) {
				return 0;
			}

			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// 动态生成每个下拉项对应的View，每个下拉项View由LinearLayout
				// 中包含一个ImageView及一个TextView构成
				LinearLayout ll = new LinearLayout(friendLocationActivity.this);// 初始化LinearLayout
				ll.setOrientation(LinearLayout.HORIZONTAL); // 设置朝向
				ll.setPadding(5, 5, 5, 5);// 设置四周留白
				ImageView ii = new ImageView(friendLocationActivity.this);// 初始化ImageView
				Bitmap bm = null;
				try {
					Log.d(TAG, "图片的名字为："+friendLocationActivity.this.picNameList.get(arg0));
					InputStream is = friendLocationActivity.this.getResources().getAssets().open(friendLocationActivity.this.picNameList.get(arg0));
					bm = BitmapFactory.decodeStream(is);
					is.close();
				} catch (IOException e) {
					Log.d(TAG, "好友头像图片错误");
					e.printStackTrace();
				}
//				ii.setImageDrawable(getResources().getDrawable(
//						drawableIds[arg0]));// 设置图片
				ii.setImageBitmap(bm);
				ii.setScaleType(ImageView.ScaleType.CENTER);
				ii.setLayoutParams(new Gallery.LayoutParams(40, 40));
				ll.addView(ii);// 添加到LinearLayout中

				TextView tv = new TextView(friendLocationActivity.this);// 初始化TextView
				tv.setText(friendLocationActivity.this.friendNameList.get(arg0));// 设置内容
				tv.setTextSize(16);// 设置字体大小
				tv.setWidth(210);
				// tv.setTextColor(friendLocationActivity.this.getResources().getColor(R.color.white));
				tv.setPadding(15, 10, 5, 5);// 设置四周留白
				tv.setGravity(Gravity.LEFT);
				tv.setTextColor(0xffD75B11);
				ll.addView(tv);// 添加到LinearLayout中

				ImageButton imageButton = new ImageButton(
						friendLocationActivity.this);
				imageButton
						.setImageResource(R.drawable.btn_insert_location_nor_2);
				imageButton.setLayoutParams(new Gallery.LayoutParams(36, 37));
				imageButton.setFocusable(false);
				final int a = arg0;
				imageButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String friendName = friendLocationActivity.this.friendNameList.get(a);
						String path = GetServerIPAddress.getIpAddress("ipaddress.txt")+
								friendLocationActivity.this.getResources().getString(R.string.getFriendLocationURL);
						Map<String, String> params = new HashMap<String, String>();
						String enc = "UTF-8";
						String friendLocation = "";
						double friendLat = 0;
						double friendLng = 0;
						boolean isSuccess = false;
						params.put("friendusername",friendName);
						try {
							friendLocation = GetInfoFromServer.getFriendLocationFromServer(path, params, enc);
							JSONObject locationJson = new JSONObject(friendLocation);
							isSuccess = locationJson.getBoolean("success");
							friendLat = locationJson.getDouble("lat");
							friendLng = locationJson.getDouble("lng");
							
						} catch (Exception e) {
							friendLocation = "failure";
							Log.d(TAG, "连接服务器超时，网络连接错误（获取某位好友位置经纬度错误）");
							Toast.makeText(
									friendLocationActivity.this,
									friendLocationActivity.this.getResources().getString(
											R.string.network_error),
									Toast.LENGTH_SHORT).show();
							
							e.printStackTrace();
						}
						if(isSuccess){
							
						}else{
							Toast.makeText(friendLocationActivity.this,"获取经纬度错误",Toast.LENGTH_SHORT).show();
						}
						if(!friendLocation.equals("failure")){
							
							//JSONTODO 将好友的位置返回到地图的Activity中
							TextView tv = (TextView) findViewById(R.id.TextView01);
							Intent intentBackToMap = new Intent();
							intentBackToMap.putExtra("isClicked", true);
							intentBackToMap.putExtra("latitude", friendLat);
							intentBackToMap.putExtra("longitude", friendLng);
							friendLocationActivity.this.setResult(101,
									intentBackToMap);
							finish();

						}else{
							showDialog(GET_FRIEND_LOCATION_ERROR);
						}
						

					
					}
				});

				ll.addView(imageButton);
				return ll;
			}
		}
		MyBaseAdapter ba = new MyBaseAdapter();

		lv.setAdapter(ba);// 为ListView设置内容适配器
		lv.setCacheColorHint(Color.TRANSPARENT);

		// lv.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

		// lv.setOnItemSelectedListener(//设置选项选中的监听器
		// new OnItemSelectedListener(){
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {//重写选项被选中事件的处理方法
		// /**TextView
		// tv=(TextView)findViewById(R.id.TextView01);//获取主界面TextView
		//
		// tv.setText("select item" + "   pos: " + arg2 + "   id: " +
		// arg3);**///信息设置进主界面TextView
		// }
		// public void onNothingSelected(AdapterView<?> arg0){}
		// }
		// );
		//
		// lv.setOnItemClickListener(//设置选项被单击的监听器
		// new OnItemClickListener(){
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {//重写选项被单击事件的处理方法
		// TextView tv=(TextView)findViewById(R.id.TextView01);//获取主界面TextView
		//
		// tv.setText("on click" + "   pos: " + arg2 + "   id: " +
		// arg3);//信息设置进主界面TextView
		//
		// }
		// }
		// );
//		lv.setOnItemClickListener(listener)
//
//		String friendName = this.friendNameList.get(arg2);

	}

	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog friendLoctionDlg = null;
		switch(id){
		case GET_FRIEND_LOCATION_ERROR:
			Builder getFriendLocationDlgBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			getFriendLocationDlgBuilder.setTitle("网络异常");
			getFriendLocationDlgBuilder.setMessage("对不起，由于当前网络错误，获取好友位置错误");
			getFriendLocationDlgBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			friendLoctionDlg = getFriendLocationDlgBuilder.create(); // 生成Dialog对象
			break;
		}
		return super.onCreateDialog(id);
	}


	class addFriendBtnOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intentToAddFriend = new Intent();
			intentToAddFriend.setClass(friendLocationActivity.this,
					AddFriendActivity.class);
			friendLocationActivity.this.startActivity(intentToAddFriend);
		}

	}
}

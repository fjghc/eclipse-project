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
	private Button addFriendBtn;// ��Ӻ��Ѱ�ť
	private ListView lv;// �����б�ListView�ؼ�
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
			//�˴�����json��ʽ�����ݣ�Ȼ���ڽ�������ʾ������
//			this.friendNameList.add(object);
			Log.d(TAG, "�����б�õ���json����Ϊ��"+friendListJsonData);
			try {
				JSONArray friendListJson = new JSONArray(friendListJsonData);
				for(int i=0;i<friendListJson.length();i++){
					JSONObject friend = (JSONObject)friendListJson.get(i); 
					String picName = String.valueOf(friend.getInt("picture"))+".png";
					this.picNameList.add(picName);
					this.friendNameList.add(friend.getString("username"));
				}
				
			} catch (JSONException e) {
				Log.d(TAG,"�����б������ݽ�������");
				e.printStackTrace();
			}
			
			
		}else{
			//�����б�Ϊ�գ���֪�û�
			Toast.makeText(this, "�����б�Ϊ�գ�����Ӻ���", 1).show();
		}

		
		
		this.addFriendBtn = (Button) this.findViewById(R.id.addFriendBtn);
		this.addFriendBtn.setOnClickListener(new addFriendBtnOnClickListener());

		class MyBaseAdapter extends BaseAdapter {
			public int getCount() {
				return friendLocationActivity.this.friendNameList.size();
			}// �ܹ�5��ѡ��

			public Object getItem(int arg0) {
				return null;
			}

			public long getItemId(int arg0) {
				return 0;
			}

			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// ��̬����ÿ���������Ӧ��View��ÿ��������View��LinearLayout
				// �а���һ��ImageView��һ��TextView����
				LinearLayout ll = new LinearLayout(friendLocationActivity.this);// ��ʼ��LinearLayout
				ll.setOrientation(LinearLayout.HORIZONTAL); // ���ó���
				ll.setPadding(5, 5, 5, 5);// ������������
				ImageView ii = new ImageView(friendLocationActivity.this);// ��ʼ��ImageView
				Bitmap bm = null;
				try {
					Log.d(TAG, "ͼƬ������Ϊ��"+friendLocationActivity.this.picNameList.get(arg0));
					InputStream is = friendLocationActivity.this.getResources().getAssets().open(friendLocationActivity.this.picNameList.get(arg0));
					bm = BitmapFactory.decodeStream(is);
					is.close();
				} catch (IOException e) {
					Log.d(TAG, "����ͷ��ͼƬ����");
					e.printStackTrace();
				}
//				ii.setImageDrawable(getResources().getDrawable(
//						drawableIds[arg0]));// ����ͼƬ
				ii.setImageBitmap(bm);
				ii.setScaleType(ImageView.ScaleType.CENTER);
				ii.setLayoutParams(new Gallery.LayoutParams(40, 40));
				ll.addView(ii);// ��ӵ�LinearLayout��

				TextView tv = new TextView(friendLocationActivity.this);// ��ʼ��TextView
				tv.setText(friendLocationActivity.this.friendNameList.get(arg0));// ��������
				tv.setTextSize(16);// ���������С
				tv.setWidth(210);
				// tv.setTextColor(friendLocationActivity.this.getResources().getColor(R.color.white));
				tv.setPadding(15, 10, 5, 5);// ������������
				tv.setGravity(Gravity.LEFT);
				tv.setTextColor(0xffD75B11);
				ll.addView(tv);// ��ӵ�LinearLayout��

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
							Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨻�ȡĳλ����λ�þ�γ�ȴ���");
							Toast.makeText(
									friendLocationActivity.this,
									friendLocationActivity.this.getResources().getString(
											R.string.network_error),
									Toast.LENGTH_SHORT).show();
							
							e.printStackTrace();
						}
						if(isSuccess){
							
						}else{
							Toast.makeText(friendLocationActivity.this,"��ȡ��γ�ȴ���",Toast.LENGTH_SHORT).show();
						}
						if(!friendLocation.equals("failure")){
							
							//JSONTODO �����ѵ�λ�÷��ص���ͼ��Activity��
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

		lv.setAdapter(ba);// ΪListView��������������
		lv.setCacheColorHint(Color.TRANSPARENT);

		// lv.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

		// lv.setOnItemSelectedListener(//����ѡ��ѡ�еļ�����
		// new OnItemSelectedListener(){
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {//��дѡ�ѡ���¼��Ĵ�����
		// /**TextView
		// tv=(TextView)findViewById(R.id.TextView01);//��ȡ������TextView
		//
		// tv.setText("select item" + "   pos: " + arg2 + "   id: " +
		// arg3);**///��Ϣ���ý�������TextView
		// }
		// public void onNothingSelected(AdapterView<?> arg0){}
		// }
		// );
		//
		// lv.setOnItemClickListener(//����ѡ������ļ�����
		// new OnItemClickListener(){
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {//��дѡ������¼��Ĵ�����
		// TextView tv=(TextView)findViewById(R.id.TextView01);//��ȡ������TextView
		//
		// tv.setText("on click" + "   pos: " + arg2 + "   id: " +
		// arg3);//��Ϣ���ý�������TextView
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
			Builder getFriendLocationDlgBuilder = new AlertDialog.Builder(this); // ����Builder����
			getFriendLocationDlgBuilder.setTitle("�����쳣");
			getFriendLocationDlgBuilder.setMessage("�Բ������ڵ�ǰ������󣬻�ȡ����λ�ô���");
			getFriendLocationDlgBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			friendLoctionDlg = getFriendLocationDlgBuilder.create(); // ����Dialog����
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

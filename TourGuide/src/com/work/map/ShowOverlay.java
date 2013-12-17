package com.work.map;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.xxl.network.GetInfoFromServer;

@SuppressLint("ParserError")
public class ShowOverlay extends ItemizedOverlay<OverlayItem> {
	private final static String TAG = "ShowOverlay";

	private ArrayList<OverlayItem> l;
	private Context context;

	public ShowOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	@SuppressLint({ "ParserError", "ShowToast", "ShowToast", "ParserError" })
	protected boolean onTap(int i){
		OverlayItem item = this.l.get(i);
		int spotNumSelected = 0;

		String param = "spot_id=";
		if (item.getTitle().equals("spot")) {// 选择的是景点
			// 传递景点号码至服务器端，服务器返回给客户端景点详细介绍，加代码
			if (item.getSnippet().equals("金锁关")) {
				// 根据景点不同来给spotSelected赋值
				spotNumSelected = 23;
			} else if (item.getSnippet().equals("鹞子翻身")) {
				spotNumSelected = 26;
			}
			else if (item.getSnippet().equals("东峰")) {
				spotNumSelected = 28;
			}
			else if (item.getSnippet().equals("长空栈道")) {
				spotNumSelected = 30;
			}
			else if (item.getSnippet().equals("北峰")) {
				spotNumSelected = 14;
			}
			else if (item.getSnippet().equals("西峰顶")) {
				spotNumSelected = 34;
			}
			else if (item.getSnippet().equals("石门")) {
				spotNumSelected = 6;
			}
			else if (item.getSnippet().equals("回心石")) {
				spotNumSelected = 10;
			}
			else if (item.getSnippet().equals("五里关")) {
				spotNumSelected = 5;
			}
			else if (item.getSnippet().equals("千尺幢")) {
				spotNumSelected = 11;
			}
			else if (item.getSnippet().equals("苍龙岭")) {
				spotNumSelected = 21;
			}
			else if (item.getSnippet().equals("五云峰")) {
				spotNumSelected = 22;
			}
			else if (item.getSnippet().equals("中锋")) {
				spotNumSelected = 25;
			}
			else if (item.getSnippet().equals("南峰")) {
				spotNumSelected = 31;
			}
			else if (item.getSnippet().equals("舍身崖")) {
				spotNumSelected = 35;
			}
			else if (item.getSnippet().equals("毛女洞")) {
				spotNumSelected = 8;
			}
			else{
				spotNumSelected = -1;
				Log.d(TAG,"点击后景点号获取失败");
			}
			
			// 根据景点号来传送至服务器获得景点介绍内容


				param += String.valueOf(spotNumSelected);
				String spotInfo = "";
				try {
					spotInfo = GetInfoFromServer.getSpotIntroFromServer(param);
				} catch (Exception e) {
					Log.d(TAG, "景点详细信息获取错误");
					spotInfo = "failure";
					e.printStackTrace();
				}
				if(spotInfo.equals("failure")){
					//景点信息获取失败
					Log.d(TAG, "连接服务器超时，网络连接错误（景点详细信息获取错误）");
					Toast.makeText(
							this.context,"由于网络连接故障，景点介绍无法获得",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this.context, item.getSnippet(), 0).show();
					Intent intent = new Intent();
					intent.putExtra("spot_id", spotNumSelected);
					intent.putExtra("jsonString", spotInfo);
					intent.setClass(this.context, SpotIntroductionActivity.class);
					this.context.startActivity(intent);
				}			
		}  else if (item.getTitle().equals("restaurant")) {
			//选择的是饭店
			if(item.getSnippet().equals("望峰饺子馆")){
				spotNumSelected = 68;
			}else if(item.getSnippet().equals("冲灵面馆")){
				spotNumSelected = 69;
			}else if(item.getSnippet().equals("清扬泡馍")){
				spotNumSelected = 70;
			}else if(item.getSnippet().equals("老米家羊肉泡馍")){
				spotNumSelected = 65;
			}else if(item.getSnippet().equals("大刀面馆")){
				spotNumSelected = 66;
			}else if(item.getSnippet().equals("西岳茶庄")){
				spotNumSelected = 67;
			}else{
				spotNumSelected = -1;
				Log.d(TAG, "饭店选择错误");
			}
			String restaurantInfo = "";
			try {
				param += String.valueOf(spotNumSelected);
				restaurantInfo = GetInfoFromServer.getSpotIntroFromServer(param);				
			} catch (Exception e) {
				Log.d(TAG, "得到景点介绍出错了");
				restaurantInfo = "failure";
				e.printStackTrace();
			}
			
			if(restaurantInfo.equals("failure")){
				//景点信息获取失败
				Log.d(TAG, "连接服务器超时，网络连接错误（餐饮详细信息获取错误）");
				Toast.makeText(
						this.context,"由于网络连接故障，餐饮介绍无法获得",
						Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this.context, item.getSnippet(), 0).show();
				Intent intent = new Intent();
				intent.putExtra("spot_id", spotNumSelected);
				intent.putExtra("jsonString", restaurantInfo);
				intent.setClass(this.context, SpotIntroductionActivity.class);
				this.context.startActivity(intent);
			}			
			

		} else if (item.getTitle().equals("hotel")) {
			
			//选择是宾馆
			if(item.getSnippet().equals("金天山庄")){
				spotNumSelected = 61;
			}else if(item.getSnippet().equals("东峰宾馆")){
				spotNumSelected = 62;
			}else if(item.getSnippet().equals("西峰旅社（翠云宫）")){
				spotNumSelected = 63;
			}else{
				spotNumSelected = -1;
				Log.d(TAG, "宾馆选择错误");
			}
			
			String hotelInfo = "";
			try {
				param += String.valueOf(spotNumSelected);
				hotelInfo = GetInfoFromServer.getSpotIntroFromServer(param);				
			} catch (Exception e) {
				Log.d(TAG, "得到景点介绍出错了");
				hotelInfo = "failure";
				e.printStackTrace();
			}
			if(hotelInfo.equals("failure")){
				//景点信息获取失败
				Log.d(TAG, "连接服务器超时，网络连接错误（住宿详细信息获取错误）");
				Toast.makeText(
						this.context,"由于网络连接故障，宾馆详细介绍无法获得",
						Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(this.context, item.getSnippet(), 0).show();
				Intent intent = new Intent();
				intent.putExtra("spot_id", spotNumSelected);
				intent.putExtra("jsonString", hotelInfo);
				intent.setClass(this.context, SpotIntroductionActivity.class);
				this.context.startActivity(intent);
			}			

		} else if (item.getTitle().equals("restpoint")) {
			Toast.makeText(this.context, item.getSnippet(), 0).show();
		} else if(item.getTitle().equals("toilet")) {
			Toast.makeText(this.context, item.getSnippet(), 0).show();
		}
		return true;

	}

	public ShowOverlay(Drawable defaultMarker, ArrayList<OverlayItem> l,
			Context c) {
		super(boundCenterBottom(defaultMarker));
		// 直到使用boundCenterBotton(defaultMarker),我的图标才显示出来
		this.context = c;
		this.l = l;
		// Log.d(TAG, "before populate()");
		populate();
		// Log.d(TAG, "after populate()");
	}

	protected OverlayItem createItem(int i) {
		// Log.d(TAG, "createItem()" + i);
		return l.get(i);
	}

	public int size() {
		// Log.d(TAG, "size = " + l.size());
		return l.size();
	}

	protected int getIndexToDraw(int i) {
		// Log.d(TAG, "getIndexToDraw" + i);
		return super.getIndexToDraw(i);
	}

	// The marker will be drawn twice for each Item in the Overlay--once in the
	// shadow phase, skewed and darkened, then again in the non-shadow phase
	public void draw(Canvas canvas, MapView mapview, boolean flag) {
		// Log.d(TAG, "before super draw");
		super.draw(canvas, mapview, flag);
		// 首先取得size，然后使用getIndexToDraw获得画item的顺序
		// Log.d(TAG, "after super draw");
	}

	// it works just with marker(icon)
	public boolean onKeyUp(int i, KeyEvent keyevent, MapView mapview) {
		// 4Log.d(TAG, "onkeyup");
		return super.onKeyUp(i, keyevent, mapview);
	}
}
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
		if (item.getTitle().equals("spot")) {// ѡ����Ǿ���
			// ���ݾ���������������ˣ����������ظ��ͻ��˾�����ϸ���ܣ��Ӵ���
			if (item.getSnippet().equals("������")) {
				// ���ݾ��㲻ͬ����spotSelected��ֵ
				spotNumSelected = 23;
			} else if (item.getSnippet().equals("���ӷ���")) {
				spotNumSelected = 26;
			}
			else if (item.getSnippet().equals("����")) {
				spotNumSelected = 28;
			}
			else if (item.getSnippet().equals("����ջ��")) {
				spotNumSelected = 30;
			}
			else if (item.getSnippet().equals("����")) {
				spotNumSelected = 14;
			}
			else if (item.getSnippet().equals("���嶥")) {
				spotNumSelected = 34;
			}
			else if (item.getSnippet().equals("ʯ��")) {
				spotNumSelected = 6;
			}
			else if (item.getSnippet().equals("����ʯ")) {
				spotNumSelected = 10;
			}
			else if (item.getSnippet().equals("�����")) {
				spotNumSelected = 5;
			}
			else if (item.getSnippet().equals("ǧ�ߴ�")) {
				spotNumSelected = 11;
			}
			else if (item.getSnippet().equals("������")) {
				spotNumSelected = 21;
			}
			else if (item.getSnippet().equals("���Ʒ�")) {
				spotNumSelected = 22;
			}
			else if (item.getSnippet().equals("�з�")) {
				spotNumSelected = 25;
			}
			else if (item.getSnippet().equals("�Ϸ�")) {
				spotNumSelected = 31;
			}
			else if (item.getSnippet().equals("������")) {
				spotNumSelected = 35;
			}
			else if (item.getSnippet().equals("ëŮ��")) {
				spotNumSelected = 8;
			}
			else{
				spotNumSelected = -1;
				Log.d(TAG,"����󾰵�Ż�ȡʧ��");
			}
			
			// ���ݾ��������������������þ����������


				param += String.valueOf(spotNumSelected);
				String spotInfo = "";
				try {
					spotInfo = GetInfoFromServer.getSpotIntroFromServer(param);
				} catch (Exception e) {
					Log.d(TAG, "������ϸ��Ϣ��ȡ����");
					spotInfo = "failure";
					e.printStackTrace();
				}
				if(spotInfo.equals("failure")){
					//������Ϣ��ȡʧ��
					Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨾�����ϸ��Ϣ��ȡ����");
					Toast.makeText(
							this.context,"�����������ӹ��ϣ���������޷����",
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
			//ѡ����Ƿ���
			if(item.getSnippet().equals("������ӹ�")){
				spotNumSelected = 68;
			}else if(item.getSnippet().equals("�������")){
				spotNumSelected = 69;
			}else if(item.getSnippet().equals("��������")){
				spotNumSelected = 70;
			}else if(item.getSnippet().equals("���׼���������")){
				spotNumSelected = 65;
			}else if(item.getSnippet().equals("�����")){
				spotNumSelected = 66;
			}else if(item.getSnippet().equals("������ׯ")){
				spotNumSelected = 67;
			}else{
				spotNumSelected = -1;
				Log.d(TAG, "����ѡ�����");
			}
			String restaurantInfo = "";
			try {
				param += String.valueOf(spotNumSelected);
				restaurantInfo = GetInfoFromServer.getSpotIntroFromServer(param);				
			} catch (Exception e) {
				Log.d(TAG, "�õ�������ܳ�����");
				restaurantInfo = "failure";
				e.printStackTrace();
			}
			
			if(restaurantInfo.equals("failure")){
				//������Ϣ��ȡʧ��
				Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨲�����ϸ��Ϣ��ȡ����");
				Toast.makeText(
						this.context,"�����������ӹ��ϣ����������޷����",
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
			
			//ѡ���Ǳ���
			if(item.getSnippet().equals("����ɽׯ")){
				spotNumSelected = 61;
			}else if(item.getSnippet().equals("�������")){
				spotNumSelected = 62;
			}else if(item.getSnippet().equals("�������磨���ƹ���")){
				spotNumSelected = 63;
			}else{
				spotNumSelected = -1;
				Log.d(TAG, "����ѡ�����");
			}
			
			String hotelInfo = "";
			try {
				param += String.valueOf(spotNumSelected);
				hotelInfo = GetInfoFromServer.getSpotIntroFromServer(param);				
			} catch (Exception e) {
				Log.d(TAG, "�õ�������ܳ�����");
				hotelInfo = "failure";
				e.printStackTrace();
			}
			if(hotelInfo.equals("failure")){
				//������Ϣ��ȡʧ��
				Log.d(TAG, "���ӷ�������ʱ���������Ӵ���ס����ϸ��Ϣ��ȡ����");
				Toast.makeText(
						this.context,"�����������ӹ��ϣ�������ϸ�����޷����",
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
		// ֱ��ʹ��boundCenterBotton(defaultMarker),�ҵ�ͼ�����ʾ����
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
		// ����ȡ��size��Ȼ��ʹ��getIndexToDraw��û�item��˳��
		// Log.d(TAG, "after super draw");
	}

	// it works just with marker(icon)
	public boolean onKeyUp(int i, KeyEvent keyevent, MapView mapview) {
		// 4Log.d(TAG, "onkeyup");
		return super.onKeyUp(i, keyevent, mapview);
	}
}
package com.work.map;

import java.util.ArrayList;

import com.example.dataobj.SpotInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThemeIntroduction extends Activity {

	private String[] spots = { "���ӷ���", "����ջ��", "���嶥", "ʯ��", "����" };
	private int[] a = {R.string.photography_Text, R.string.wuxia_Text ,R.string.landscape_Text ,R.string.guji_Text, R.string.huamanity_Text};

	private Button button;
	private String title;
	private int imageSource;;

	private ListView listView;

	private SpotInfo currentThemeInfo;

	@SuppressLint("ParserError")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themeintro);
		
		//String[] spots;
		currentThemeInfo = new SpotInfo("������", "������", 1);
		Intent intent = getIntent();
		int whichtheme = intent.getIntExtra("whichtheme", -1);
		Log.d("receive", "which   " + whichtheme);
		switch (whichtheme) {
		case 0:
			title = "��Ӱ��";
			imageSource = R.drawable.themeinfo1;
			currentThemeInfo.setDescription("  ���þ�ͷ��¼����;���������������������û���������̾Ϊ��ֹ�����澰����"
					+ "�������Լ������������漣����;�����ǲ�ȱ������ȱ�ٵ����������Щ����˲�䡣"
					+ "�������е��������˲�䣬��¼���ã�����Щ���ֺ��������ñ��棬�����ǵ����л�ζ���");
			
			break;
		case 1:
			title = "������";
			imageSource = R.drawable.martial;
			currentThemeInfo.setDescription("  ����Ⱥ�۾ۣ�һ�Ե�Ӱ�½��������ʲ���ս��Ц��������ؼ䡣"
					+ "һ���۷�Ľ��㣬һ��ʤ��ܵ�ʮ��·�ڣ��������ǻ��������Ӣ�ۻ�����"
					+ "���ٸ�����Ȼ��Ļ��������ȥ��������ɣ������ǹ������ص��㼣�������������⽣Ӱ�Ľ���������ң���ɼ�����ʣ���սδ֪��δ����");
			break;
		case 2:
			title = "ɽˮ��";
			imageSource = R.drawable.themeinfo3;
			currentThemeInfo.setDescription("  ������ˮ��������ɽ��ɽˮ���飬�������֮�䣬������������ɽ��������ˮ��"
					+ "�Ǹ�Զ����һ����ɽС�������������ٷ�������ȥ����ˮ֮�࣬������ɽɫ�����µ��������泩��"
					+ "�����羰���⣬������ɽ��֮�죬�����ڴ˼䣬�����ڴ���Ȼ���޾����������Σ�");
			break;
		case 3:
			title = "�ż���";
			imageSource = R.drawable.themeinfo4;
			currentThemeInfo.setDescription("  �л�������ǧ����ƾ���ʷ���������������ʤ�ż�����ʷ����������Ļ����������ۡ�"
					+ "������ʷ��ʤ�ż������ܹŴ��������Ʒ����������Ȥ�£����䵱���������¡����������Ļ��Ų���"
					+ "�������˽�Ŵ����Ρ��ڽ̡����롢���֡��Ͷ�����ᡢ���á������ȣ��ֲ����֡���ʷ�ȼ�¼֮���㡣");
			break;
		case 4:
			title = "������";
			imageSource = R.drawable.themeinfo5;
			currentThemeInfo.setDescription("  �ۺ����ģ��Բ�ʱ�䣻�ۺ����ģ��Ի������¡����ľ��ۣ��ֳ��Ļ����ۣ�"
					+ "���������ճ������У�Ϊ������һЩ���ʺ;���ȷ������Ҫ������Ȼ���۵Ļ����ϣ��������Ļ����ʶ����ɵľ��ۡ����ľ��ۣ���Ҫ�����ּ����䡢"
					+ "���Ρ����������ֵȡ��������������ɫ��ӳ���ڽ̽�������.���̽������ۡ�");
			break;

		default:
			Toast.makeText(ThemeIntroduction.this, "���ⲻ����",  Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		}
		
//		currentThemeInfo = new SpotInfo(
//				"���ӷ���",
//				"  ����Ⱥ�۾ۻ�ɽ��һ�Ե�Ӱ�½��������ʲ���ս��Ц��������ؼ䡣"
//						+ "��ɽ��һ���۷�Ľ��㣬һ��ʤ��ܵ�ʮ��·�ڣ��������ǻ��������Ӣ�ۻ�����"
//						+ "���ٸ�����Ȼ��Ļ��������ȥ��������ɣ������ǹ������ص��㼣�������������⽣Ӱ�Ľ���������ң���ɼ�����ʣ���սδ֪��δ����",
//				1);
		TextView spotDescriptionText = (TextView) findViewById(R.id.theme_description);
		spotDescriptionText.setText(currentThemeInfo.getDescription());
		spotDescriptionText.setTextSize(16);
		TextView titleTv = (TextView)findViewById(R.id.TextView01);
		titleTv.setText(title);
		ImageView imageView = (ImageView)findViewById(R.id.theme_infi_iv);
		imageView.setBackgroundResource(imageSource);
		TextView themeTv = (TextView)findViewById(R.id.theme_info);
		themeTv.setText(this.getResources().getString(a[whichtheme]));
		listView = (ListView) findViewById(R.id.spotslistviewbytheme);
		BaseAdapter ba = new BaseAdapter() {
			public int getCount() {
				return 5;
			}// �ܹ�5��ѡ��

			public Object getItem(int arg0) {
				return null;
			}

			public long getItemId(int arg0) {
				return 0;
			}

			public View getView(int arg0, View arg1, ViewGroup arg2) {
				TextView tv = new TextView(ThemeIntroduction.this);
				tv.setText(spots[arg0]);
				tv.setTextSize(16);
				tv.setGravity(Gravity.CENTER);
				return tv;
			}
		};

		listView.setAdapter(ba);
		listView.setCacheColorHint(Color.TRANSPARENT);

		button = (Button) findViewById(R.id.viewThemeOnMap);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentBackToMap = new Intent();
				intentBackToMap.putExtra("themeList",
						ThemeIntroduction.this.spots);

				setResult(203, intentBackToMap);

				Log.d("Theme list", "first step");

				finish();
			}
		});

	}

}

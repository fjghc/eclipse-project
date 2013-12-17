package com.work.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ThemeList extends Activity {

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intentBackToMap) {
		if (requestCode == 202 && resultCode == 203) {
			setResult(201, intentBackToMap);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, intentBackToMap);
	}

	// ������ԴͼƬ��andy��bill��edgar��torvalds��turing��id������
	int[] drawableIds = { R.drawable.theme1, R.drawable.theme2,
			R.drawable.theme3, R.drawable.theme4, R.drawable.theme5 };
	// ������Դ�ַ�����andy��bill��edgar��torvalds��turing��id������
	int[] msgIds = { R.string.photography, R.string.wuxia, R.string.landscape,
			R.string.guji, R.string.huamanity };
	int[] textIds = { R.string.photography_Text, R.string.wuxia_Text,
			R.string.landscape_Text, R.string.guji_Text,
			R.string.huamanity_Text };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themelist);
		ListView lv = (ListView) this.findViewById(R.id.ListView01);// ��ʼ��ListView
		BaseAdapter ba = new BaseAdapter() {// ΪListView׼������������
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
				// ��̬����ÿ���������Ӧ��View��ÿ��������View��LinearLayout
				// �а���һ��ImageView��һ��TextView����
				LinearLayout ll = new LinearLayout(ThemeList.this);// ��ʼ��LinearLayout
				ll.setOrientation(LinearLayout.HORIZONTAL); // ���ó���
				ll.setPadding(5, 5, 5, 5);// ������������
				ImageView ii = new ImageView(ThemeList.this);// ��ʼ��ImageView
				ii.setImageDrawable(getResources().getDrawable(
						drawableIds[arg0]));// ����ͼƬ
				ii.setScaleType(ImageView.ScaleType.FIT_XY);
				ii.setLayoutParams(new Gallery.LayoutParams(60, 60));
				ll.addView(ii);// ��ӵ�LinearLayout��

				LinearLayout l2 = new LinearLayout(ThemeList.this);
				l2.setOrientation(LinearLayout.VERTICAL);
				l2.setPadding(5, 5, 5, 5);

				TextView tv1 = new TextView(ThemeList.this);// ��ʼ��TextView
				tv1.setText(getResources().getText(msgIds[arg0]));// ��������
				tv1.setTextSize(20);
				tv1.setTextColor(0xff4A4A4A);// ���������С
				// tv1.setTextColor(ThemeList.this.getResources().getColor(R.color.white));//
				// ����������ɫ
				// tv1.setPadding(5, 5, 5, 5);// ������������
				tv1.setGravity(Gravity.LEFT);
				l2.addView(tv1);

				TextView tv2 = new TextView(ThemeList.this);// ��ʼ��TextView
				tv2.setText(getResources().getText(textIds[arg0]));// ��������
				tv2.setTextSize(14);// ���������С
				// tv2.setTextColor(Sample_5_4.this.getResources().getColor(R.color.white));//
				// ����������ɫ
				// tv2.setPadding(10, 5, 5, 5);// ������������
				tv2.setGravity(Gravity.LEFT);
				l2.addView(tv2);

				ll.addView(l2);// ��ӵ�LinearLayout��
				return ll;
			}
		};
		lv.setAdapter(ba);// ΪListView��������������
		lv.setCacheColorHint(Color.TRANSPARENT);

		lv.setOnItemClickListener(// ����ѡ������ļ�����
		new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {// ��дѡ������¼��Ĵ�����

				Intent intent = new Intent(ThemeList.this,
						ThemeIntroduction.class);
				intent.putExtra("whichtheme", arg2);
				ThemeList.this.startActivityForResult(intent, 202);
			}
		});
	}

}

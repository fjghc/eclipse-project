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

	// 所有资源图片（andy、bill、edgar、torvalds、turing）id的数组
	int[] drawableIds = { R.drawable.theme1, R.drawable.theme2,
			R.drawable.theme3, R.drawable.theme4, R.drawable.theme5 };
	// 所有资源字符串（andy、bill、edgar、torvalds、turing）id的数组
	int[] msgIds = { R.string.photography, R.string.wuxia, R.string.landscape,
			R.string.guji, R.string.huamanity };
	int[] textIds = { R.string.photography_Text, R.string.wuxia_Text,
			R.string.landscape_Text, R.string.guji_Text,
			R.string.huamanity_Text };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themelist);
		ListView lv = (ListView) this.findViewById(R.id.ListView01);// 初始化ListView
		BaseAdapter ba = new BaseAdapter() {// 为ListView准备内容适配器
			public int getCount() {
				return 5;
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
				LinearLayout ll = new LinearLayout(ThemeList.this);// 初始化LinearLayout
				ll.setOrientation(LinearLayout.HORIZONTAL); // 设置朝向
				ll.setPadding(5, 5, 5, 5);// 设置四周留白
				ImageView ii = new ImageView(ThemeList.this);// 初始化ImageView
				ii.setImageDrawable(getResources().getDrawable(
						drawableIds[arg0]));// 设置图片
				ii.setScaleType(ImageView.ScaleType.FIT_XY);
				ii.setLayoutParams(new Gallery.LayoutParams(60, 60));
				ll.addView(ii);// 添加到LinearLayout中

				LinearLayout l2 = new LinearLayout(ThemeList.this);
				l2.setOrientation(LinearLayout.VERTICAL);
				l2.setPadding(5, 5, 5, 5);

				TextView tv1 = new TextView(ThemeList.this);// 初始化TextView
				tv1.setText(getResources().getText(msgIds[arg0]));// 设置内容
				tv1.setTextSize(20);
				tv1.setTextColor(0xff4A4A4A);// 设置字体大小
				// tv1.setTextColor(ThemeList.this.getResources().getColor(R.color.white));//
				// 设置字体颜色
				// tv1.setPadding(5, 5, 5, 5);// 设置四周留白
				tv1.setGravity(Gravity.LEFT);
				l2.addView(tv1);

				TextView tv2 = new TextView(ThemeList.this);// 初始化TextView
				tv2.setText(getResources().getText(textIds[arg0]));// 设置内容
				tv2.setTextSize(14);// 设置字体大小
				// tv2.setTextColor(Sample_5_4.this.getResources().getColor(R.color.white));//
				// 设置字体颜色
				// tv2.setPadding(10, 5, 5, 5);// 设置四周留白
				tv2.setGravity(Gravity.LEFT);
				l2.addView(tv2);

				ll.addView(l2);// 添加到LinearLayout中
				return ll;
			}
		};
		lv.setAdapter(ba);// 为ListView设置内容适配器
		lv.setCacheColorHint(Color.TRANSPARENT);

		lv.setOnItemClickListener(// 设置选项被单击的监听器
		new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {// 重写选项被单击事件的处理方法

				Intent intent = new Intent(ThemeList.this,
						ThemeIntroduction.class);
				intent.putExtra("whichtheme", arg2);
				ThemeList.this.startActivityForResult(intent, 202);
			}
		});
	}

}

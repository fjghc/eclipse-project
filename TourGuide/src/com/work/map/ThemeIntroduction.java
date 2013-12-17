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

	private String[] spots = { "鹞子翻身", "长空栈道", "西峰顶", "石门", "东峰" };
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
		currentThemeInfo = new SpotInfo("主题游", "主题游", 1);
		Intent intent = getIntent();
		int whichtheme = intent.getIntExtra("whichtheme", -1);
		Log.d("receive", "which   " + whichtheme);
		switch (whichtheme) {
		case 0:
			title = "摄影游";
			imageSource = R.drawable.themeinfo1;
			currentThemeInfo.setDescription("  想用镜头记录下旅途中所见的种种美好吗，想用画面留下那叹为观止的雄奇景象吗，"
					+ "想留存自己看到的美景奇迹吗？旅途中总是不缺少美，缺少的是珍藏下这些美的瞬间。"
					+ "拿起手中的相机定格瞬间，记录美好，让这些欢乐和美好永久保存，让我们的旅行回味无穷。");
			
			break;
		case 1:
			title = "武侠游";
			imageSource = R.drawable.martial;
			currentThemeInfo.setDescription("  天下群雄聚，一试刀影孤剑寒。试问苍穹百战起，笑傲江湖天地间。"
					+ "一个巅峰的交点，一个胜与败的十字路口，多少人徘徊这里，多少英雄魂归这里，"
					+ "多少高手黯然落幕，神伤离去。来这里吧，沿着那古老神秘的足迹，感受曾经刀光剑影的江湖，触摸遥不可及的天际，挑战未知的未来。");
			break;
		case 2:
			title = "山水游";
			imageSource = R.drawable.themeinfo3;
			currentThemeInfo.setDescription("  智者乐水，仁者乐山。山水怡情，徜徉天地之间，尽享这绵绵青山，潺潺流水。"
					+ "登高远眺，一览众山小，快意人生多少烦恼随风而去。丽水之侧，看湖光山色，清新淡雅心自舒畅。"
					+ "畅。风景不殊，正自有山河之异，流连于此间，让心在大自然中无拘无束的遨游！");
			break;
		case 3:
			title = "古迹游";
			imageSource = R.drawable.themeinfo4;
			currentThemeInfo.setDescription("  中华民族五千年的悠久历史留下了数不清的名胜古迹，历史在这里沉淀，文化在这里凝聚。"
					+ "游览历史名胜古迹，感受古代建筑风格，品读古人轶闻趣事，重忆当年伊人逸事。活生生的文化遗产，"
					+ "让我们了解古代政治、宗教、祭祀、娱乐、劳动、社会、经济、教育等，弥补文字、历史等纪录之不足。");
			break;
		case 4:
			title = "人文游";
			imageSource = R.drawable.themeinfo5;
			currentThemeInfo.setDescription("  观乎天文，以察时变；观乎人文，以化成天下。人文景观，又称文化景观，"
					+ "是人们在日常生活中，为了满足一些物质和精神等方面的需要，在自然景观的基础上，叠加了文化特质而构成的景观。人文景观，主要的体现即聚落、"
					+ "服饰、建筑、音乐等。而建筑方面的特色反映即宗教建筑景观.如佛教建筑景观。");
			break;

		default:
			Toast.makeText(ThemeIntroduction.this, "主题不存在",  Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		}
		
//		currentThemeInfo = new SpotInfo(
//				"鹞子翻身",
//				"  天下群雄聚华山，一试刀影孤剑寒。试问苍穹百战起，笑傲江湖天地间。"
//						+ "华山，一个巅峰的交点，一个胜与败的十字路口，多少人徘徊这里，多少英雄魂归这里，"
//						+ "多少高手黯然落幕，神伤离去。来这里吧，沿着那古老神秘的足迹，感受曾经刀光剑影的江湖，触摸遥不可及的天际，挑战未知的未来。",
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
			}// 总共5个选项

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

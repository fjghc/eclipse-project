package com.work.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dataobj.SpotCommentsList;
import com.example.dataobj.SpotInfo;
import com.example.dataobj.SpotPhotos;
import com.xxl.network.GetInfoFromServer;
import com.xxl.network.HttpRequest;
import com.xxl.utility.GetServerIPAddress;
import com.xxl.utility.dataStorage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ParserError", "ParserError", "ParserError" })
public class SpotIntroductionActivity extends Activity implements
	RatingBar.OnRatingBarChangeListener {

	private static final String TAG = "SpotIntroductionActivity";

	/* 用户名 评论时用 */
	private String usernickname;

	/* 描述景点信息的类 */
	private SpotInfo currentSpotInfo;

	/* 景点图片 */
	private SpotPhotos spotPhotos;

	/* 景点评论 */
	private SpotCommentsList commentsList;

	RatingBar mIndicatorRatingBar;

	private AnimationSet manimationSet;
	private int spot_id;
	private String content;
	private ArrayList<String> spotPicIntro;
	private float grade_level;
	private String spotName;
	private boolean isSpot;
	private EditText userNickNameText;
    private ArrayList<String> picPaths;
	private String comment;
	private Button submitCommentBtn;
	private EditText commentContent;
	private ProgressDialog waitingForCommentSubmitProDlg;
	private Button videoBtn;
	private Button musicBtn;
	private ListView priceslist;
	public Handler myHandler;
	private static final int COMMENT_CAN_NOT_NULL_DLG = 1;

	private static final int PLEASE_LOGIN_DIALOG = 2;

	public static final int COMMENT_SUBMIT = 1000;

	private static final int PROGRESS_BAR_DIALOG = 1001;

	protected static final int GET_VIDEO_ERROR_DLG = 1002;

	protected static final int GET_VOICE_ERROR_DLG = 1003;

	@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError", "ParserError" })
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog spotIntroDlg = null;
		switch (id) {
		case COMMENT_CAN_NOT_NULL_DLG:
			Builder spotCommentNullDlgBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			spotCommentNullDlgBuilder.setMessage("评论不能为空，请输入评论后提交");
			spotCommentNullDlgBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = spotCommentNullDlgBuilder.create(); // 生成Dialog对象
			break;
		case PLEASE_LOGIN_DIALOG:
			Builder pleaseLoginDlgBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			pleaseLoginDlgBuilder.setMessage("对不起，您没有权限评论该景点");
			pleaseLoginDlgBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = pleaseLoginDlgBuilder.create(); // 生成Dialog对象
			break;
		case PROGRESS_BAR_DIALOG:
			Log.d(TAG, "case progressbar");
			//发表评论并且显示进度条对话框
			SpotIntroductionActivity.this.waitingForCommentSubmitProDlg = ProgressDialog.show(
					SpotIntroductionActivity.this, "请等待", "正在为您提交评论...", true);
			new Thread(new sendCommentThread()).start();	
			break;
		case GET_VOICE_ERROR_DLG:
			Builder getVoiceErrorDlgBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			getVoiceErrorDlgBuilder.setTitle("网络异常");
			getVoiceErrorDlgBuilder.setMessage("由于当前网络错误，获取音频介绍错误");
			getVoiceErrorDlgBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = getVoiceErrorDlgBuilder.create(); // 生成Dialog对象
			break;
		case GET_VIDEO_ERROR_DLG:
			Builder getVideoErrorDlgBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			getVideoErrorDlgBuilder.setTitle("网络异常");
			getVideoErrorDlgBuilder.setMessage("对不起，由于当前网络错误，获取视频介绍错误");
			getVideoErrorDlgBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = getVideoErrorDlgBuilder.create(); // 生成Dialog对象
			break;
			
		default:
			Log.d(TAG, "传入的对话框类型错误");
		}

		return spotIntroDlg;
	}

	public SpotIntroductionActivity() {

		usernickname = "任我行";
		currentSpotInfo = new SpotInfo(
				"石门",
				"在华山东峰，是通往下棋亭的必由之路，为华山著名的险道之一。\n其路凿于倒坎悬崖上，下视唯见寒索垂于凌空，不见路径。游人至此，须面壁挽索，以脚尖探寻石窝，交替而下,其中几步须如鹰鹞一般	、左右翻转身体才可通过，故名。从视觉冲击上来看，鹞子翻身远远比不上长空栈道。但是真的下去了，才知道这鹞子翻身的难度更大一些！首先它是倾斜的，而且是往里倾。其次铁链太松动，左右摇摆度偏大，不易抓牢。"
						+ "再者人是从上往下攀缘，崖壁往里倾，眼睛看落脚点有点困难。这就需要手、眼、脚、膝的全面配合了。在鹞子翻身的全途中，不能有丝毫的松懈。手要抓紧，眼要看准，膝要顶住，脚要踩稳。全神贯注，集中精神。论难度，这鹞子翻身比长空栈道要多一颗星。小心翼翼，看不见就用脚来试探。一步一个坑的往下攀岩。有一小段只有一边的铁链可以抓握。这一段需要掌握好身体的平衡度。"
						+ "近年华山旅游发展总公司已对鹞子翻身险道全面整修，凿深脚窝、石阶，多处更换了铁索。", 1);
		this.isSpot = false;
		spotPhotos = new SpotPhotos();

		spotPhotos.addPhoto("gallery_photo_1", "大家好！");
		spotPhotos.addPhoto("gallery_photo_2", "我来啦");
		spotPhotos.addPhoto("gallery_photo_3", "神奇");
		spotPhotos.addPhoto("gallery_photo_4", "奇崛险要");
		spotPhotos.addPhoto("gallery_photo_5", "picture5");
		spotPhotos.addPhoto("gallery_photo_6", "picture6");
		spotPhotos.addPhoto("gallery_photo_7", "picture7");
		spotPhotos.addPhoto("gallery_photo_8", "picture8");

		commentsList = new SpotCommentsList();
		picPaths = new ArrayList<String>();
		spotPicIntro = new ArrayList<String>();
//		for (int i = 0; i < 10; i++) {
//			commentsList.addTouristComments("东方不败" + i, "我第" + i + "次来了",new Date().toLocaleString());
//		}
	}

	@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spot_introduction);
		/* 添加音频按钮监听器 */
		this.musicBtn = (Button) findViewById(R.id.voice_button);
		this.musicBtn.setOnClickListener(voiceListener);
		//mIndicatorRatingBar.setMax(5);
		/* 添加视频按钮监听器 */
		this.videoBtn = (Button) findViewById(R.id.video_button);
		this.videoBtn.setOnClickListener(videoListener);
		//设置价格列表
		this.priceslist = (ListView) this.findViewById(R.id.service_price_view);
		/* 设置景点等级ratingbar监听 */
		mIndicatorRatingBar = (RatingBar) findViewById(R.id.small_ratingbar);
//		((RatingBar) findViewById(R.id.ratingbar1))
//				.setOnRatingBarChangeListener(this);
		mIndicatorRatingBar.setNumStars(5);
		//如果得到的景点号为：景点，则界面按钮保持不变，否则，去掉音频视频按钮
	
		/**
		 * 如果是关于服务场所的信息则需要去掉音频视频按钮
		 *  findViewById(R.id.video_button).setVisibility(8);
		 *  findViewById(R.id.voice_button).setVisibility(8);
		 *  findViewById(R.id.price_layout).setVisibility(0);
		 * 
		 */
		this.submitCommentBtn = (Button)this.findViewById(R.id.submit_comment);
		String spotIntroductionInfo = this.getIntent().getStringExtra("jsonString");
		this.spot_id = this.getIntent().getIntExtra("spot_id", -1);
		// 生成适配器的Item和动态数组对应的元素,餐饮住宿价格
		ArrayList<HashMap<String, String>> list = new 
						ArrayList<HashMap<String, String>>();
		Log.d(TAG, "获得的json数据格式为："+spotIntroductionInfo);
		Log.d(TAG, "传过来的景点号为："+this.spot_id);
		if(this.spot_id == 5|| this.spot_id == 6 ||this.spot_id == 8|| this.spot_id == 10||
				this.spot_id == 11|| this.spot_id == 14 ||this.spot_id == 21|| this.spot_id == 22|| 
						this.spot_id == 23 ||this.spot_id == 25|| this.spot_id == 26|| this.spot_id == 28||
								this.spot_id == 30|| this.spot_id == 31 ||this.spot_id == 34 ||this.spot_id == 35){
			this.videoBtn.setVisibility(0);
			this.musicBtn.setVisibility(0);
			this.findViewById(R.id.price_layout).setVisibility(8);
			this.isSpot = true;
		}else{
			this.videoBtn.setVisibility(8);
			this.musicBtn.setVisibility(8);
			this.findViewById(R.id.price_layout).setVisibility(0);
			this.isSpot = false;
		}
		try {
			JSONObject json = new JSONObject(spotIntroductionInfo);
			
			if (this.isSpot) {
				JSONArray jsons = json.getJSONArray("comments");

				for (int i = 0; i < jsons.length(); i++) {
					JSONObject comment = (JSONObject) jsons.opt(i);
					this.commentsList.addTouristComments(
							comment.getString("nickname"),
							comment.getString("time"),
							comment.getString("content"));
				}
				content = json.getString("context");
				grade_level = (float)json.getDouble("grade_level");

				JSONArray picPaths = json.getJSONArray("pics");

				for (int i = 0; i < picPaths.length(); i++) {
					JSONObject path = (JSONObject) picPaths.opt(i);
					this.spotPicIntro.add(path.getString("name"));
					this.picPaths.add(path.getString("path"));
				}

				spotName = json.getString("spot_name");

			} else {
//				TextView picOfHotel = (TextView)this.findViewById(R.id.picOfSpot);
//				picOfHotel.setText("店铺一角");
//				TextView touristComment = (TextView)this.findViewById(R.id.touristComment);
//				touristComment.setText("价格预览");
				JSONArray jsons = json.getJSONArray("comments");

				for (int i = 0; i < jsons.length(); i++) {
					JSONObject comment = (JSONObject) jsons.opt(i);
					this.commentsList.addTouristComments(
							comment.getString("nickname"),
							comment.getString("time"),
							comment.getString("content"));
				}
				this.content = json.getString("context");
				this.grade_level = (float)json.getDouble("grade_level");
				
				JSONArray picPaths = json.getJSONArray("pics");

				for (int i = 0; i < picPaths.length(); i++) {
					JSONObject path = (JSONObject) picPaths.opt(i);
					this.spotPicIntro.add(path.getString("name"));
					this.picPaths.add(path.getString("path"));
				}
				JSONArray productDescriptions = json.getJSONArray("products");
				HashMap<String, String> map1 = new HashMap<String, String>();
				for(int j=0;j<productDescriptions.length();j++){
					JSONObject description = (JSONObject)productDescriptions.getJSONObject(j);
					map1.put("name", description.getString("description"));
					map1.put("price", description.getString("price"));
					list.add(map1);
				}
				spotName = json.getString("spot_name");
				SimpleAdapter pricelistItemAdapter = new SimpleAdapter(this,
						list,// 数据源
						R.layout.service_price,// ListItem的XML实现
						// 动态数组与ImageItem对应的子项
						new String[] { "name", "price" },
						// ImageItem的XML文件里面的一个ImageView,两个TextView ID
						new int[] { R.id.service_name,
								R.id.service_price});
				// 添加并且显示价格
				priceslist.setAdapter(pricelistItemAdapter);

			}

		} catch (JSONException e) {
			Log.d(TAG, "景点介绍json数据解析错误");
			e.printStackTrace();
		}

		Log.d(TAG, "内容为："+content);
		Log.d(TAG, "评价等级为："+String.valueOf(grade_level));
		Log.d(TAG, "景点名字为："+spotName);
		this.commentContent = (EditText)this.findViewById(R.id.spot_me_comment_content);

		for(String path:this.picPaths){
			Log.d(TAG, ""+"获得的所有图片的文件名为："+path);
		}
		

		/* 添加评论 */
		ListView commentslist = (ListView) findViewById(R.id.spot_comment_list_view);
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				commentsList.getComments(),// 数据源
				R.layout.comments,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "name", "time", "content" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.spot_comment_tourist_name,
						R.id.spot_comment_tourist_time,
						R.id.spot_comment_tourist_content });
		// 添加并且显示评论
		commentslist.setAdapter(listItemAdapter);
		
		myHandler = new Handler() {
			@SuppressLint("ParserError")
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case COMMENT_SUBMIT:
					SpotIntroductionActivity.this.waitingForCommentSubmitProDlg.dismiss();
					switch (msg.arg1) {
					case 1:// 登录成功的响应
						Log.d(TAG, "发表评论成功");
						Toast.makeText(SpotIntroductionActivity.this, "发表评论成功", 1).show();
						break;
					case 2:
						Log.d(TAG, "连接服务器成功，但是服务器返回发表评论错误");
						Toast.makeText(SpotIntroductionActivity.this, "对不起，您的发表评论失败", 1).show();
						break;
					case 3:
						Log.d(TAG, "发表评论超时，网络连接错误");
						Toast.makeText(
								SpotIntroductionActivity.this,
								SpotIntroductionActivity.this.getResources()
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
		
		
		/* 设置景点名称TextView空间text属性 */
		TextView spotNameText = (TextView) findViewById(R.id.spot_name);
		spotNameText.setText(this.spotName);
		/* 设置景点介绍TextView空间text属性 */
		TextView spotDescriptionText = (TextView) findViewById(R.id.spot_description);
		spotDescriptionText.setText(this.content);
		
		//设置评分等级
		this.mIndicatorRatingBar.setRating(grade_level);
		
		

		this.userNickNameText = (EditText) findViewById(R.id.spot_me_comment_nickname);
		/* 如果已经登录，显示我的用户名 */
		if (usernickname != null) {
			/* 设置景点名称TextView空间text属性 */
			this.userNickNameText.setText("任我行");
		}
		


		// Reference the Gallery view
		Gallery g = (Gallery) findViewById(R.id.gallery);
		// Set the adapter to our custom adapter (below)
		g.setAdapter(new ImageAdapter(this));

		/* Gallery图片点击放大效果，摘自互联网 */
		// Set a item click listener, and just Toast the clicked position
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				/* 修改，显示图片名字 */
				
				Toast.makeText(SpotIntroductionActivity.this,
						SpotIntroductionActivity.this.spotPicIntro.get(position), Toast.LENGTH_SHORT)
						.show();

				if (position >= 0 && position < spotPhotos.getNumOfPhotos()) {

					AnimationSet animationSet = new AnimationSet(true);

					if (manimationSet != null && manimationSet != animationSet) {
						ScaleAnimation scaleAnimation = new ScaleAnimation(2,
								0.5f, 2, 0.5f, Animation.RELATIVE_TO_SELF,
								0.5f, // 使用动画播放图片
								Animation.RELATIVE_TO_SELF, 0.5f);
						scaleAnimation.setDuration(1000);
						manimationSet.addAnimation(scaleAnimation);
						manimationSet.setFillAfter(true); // 让其保持动画结束时的状态。
						v.startAnimation(manimationSet);
					}

					ScaleAnimation scaleAnimation = new ScaleAnimation(1, 2f,
							1, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnimation.setDuration(1000);
					animationSet.addAnimation(scaleAnimation);
					animationSet.setFillAfter(true);
					v.startAnimation(animationSet);
					manimationSet = animationSet;

				} else {
					if (null != manimationSet)
						manimationSet.setFillAfter(false);
				}
			}
		});

		
		//提交评论部分
		this.submitCommentBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				comment = SpotIntroductionActivity.this.commentContent.getText().toString();
				if(comment.equals("")){
					showDialog(COMMENT_CAN_NOT_NULL_DLG);
				}else if(!dataStorage.is_login){
					showDialog(PLEASE_LOGIN_DIALOG);
				}else{
					//发送景点评论
					showDialog(PROGRESS_BAR_DIALOG);
				}
			}
		});
	}

	
	class sendCommentThread implements Runnable {
		private boolean isCommentSubmitOK;
		private int comment_submit_status_code;
		private String nickName = SpotIntroductionActivity.this.userNickNameText.getText().toString();

		@SuppressLint("ParserError")
		public void run() {
			try {
				isCommentSubmitOK = false;
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", dataStorage.user_name);
				params.put("nickname", nickName);
				params.put("content",comment);
				params.put("spot_id", String.valueOf(SpotIntroductionActivity.this.spot_id));

				isCommentSubmitOK = HttpRequest.sendPostRequest(GetServerIPAddress.getIpAddress("ipaddress.txt")+SpotIntroductionActivity.this
						.getResources().getString(R.string.commentSubmitURL), params,
						"UTF-8");

				if (isCommentSubmitOK) {// 发表评论成功
					this.comment_submit_status_code = 1;
					Log.d(TAG, "发表评论成功");
				} else {// 发表评论失败，但是连接到服务器了
					this.comment_submit_status_code = 2;
					Log.d(TAG, "发表评论失败，但是连接到服务器了");
				}

			} catch (Exception e) {
				// 网络连接错误
				this.comment_submit_status_code = 3;
				Log.d(TAG, "网络连接错误");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = SpotIntroductionActivity.COMMENT_SUBMIT;
			message.arg1 = this.comment_submit_status_code;
			SpotIntroductionActivity.this.myHandler.sendMessage(message);
		}
	}

	@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError" })
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromTouch) {
		final int numStars = ratingBar.getNumStars();
		if (mIndicatorRatingBar.getNumStars() != numStars) {
			mIndicatorRatingBar.setNumStars(numStars);
			// mSmallRatingBar.setNumStars(numStars);
		}
		if (mIndicatorRatingBar.getRating() != rating) {
			mIndicatorRatingBar.setRating(rating);
			// mSmallRatingBar.setRating(rating);
		}
		final float ratingBarStepSize = ratingBar.getStepSize();
		if (mIndicatorRatingBar.getStepSize() != ratingBarStepSize) {
			mIndicatorRatingBar.setStepSize(ratingBarStepSize);
			// mSmallRatingBar.setStepSize(ratingBarStepSize);
		}
	}

	/* 长按下时的浮动菜单 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(R.string.photo_long_press);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		/* 修改，显示图片名字 */
		/*
		 * Toast.makeText(this, "Longpress: " + info.position,
		 * Toast.LENGTH_SHORT).show();
		 */
		Toast.makeText(this, spotPhotos.getPhotoName(info.position),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;

		private Context mContext;
//		
//		private Integer[] mImageIds = { R.drawable.gallery_photo_1,
//				R.drawable.gallery_photo_2, R.drawable.gallery_photo_3,
//				R.drawable.gallery_photo_4, R.drawable.gallery_photo_5,
//				R.drawable.gallery_photo_6, R.drawable.gallery_photo_7,
//				R.drawable.gallery_photo_8 };

		public ImageAdapter(Context c) {
			mContext = c;
			// See res/values/attrs.xml for the <declare-styleable> that defines
			// Gallery1.
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		public int getCount() {
			return SpotIntroductionActivity.this.picPaths.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);


			BitmapFactory.Options options = new BitmapFactory.Options(); 
			options.inSampleSize = 2; 
			Bitmap bm = null;
			try {
				InputStream is = SpotIntroductionActivity.this.getResources().getAssets().open(SpotIntroductionActivity.this.picPaths.get(position));
				bm = BitmapFactory.decodeStream(is);
				is.close();
			} catch (IOException e) {
				Log.d(TAG, "图片错误");
				e.printStackTrace();
			}
			i.setImageBitmap(bm);
			i.setScaleType(ImageView.ScaleType.FIT_XY);
	        i.setLayoutParams(new Gallery.LayoutParams(136, 88));
			// The preferred Gallery item background
			i.setBackgroundResource(mGalleryItemBackground);

			return i;
		}
		
	
	}

	/* 音频按钮监听器 */
	private OnClickListener voiceListener = new OnClickListener() {
		@SuppressLint({ "ParserError", "ParserError", "ParserError" })
		public void onClick(View v) {
			/* to do */
			String voiceName = "";
			String path = GetServerIPAddress.getIpAddress("ipaddress.txt")+SpotIntroductionActivity.this.getResources().getString(R.string.getVoiceURL);
			Map<String, String> params = new HashMap<String, String>();
			params.put("spot_id",String.valueOf(SpotIntroductionActivity.this.spot_id));
			try {
				voiceName = GetInfoFromServer.getVideoOrVoiceFromServer(path, params, "UTF-8");
			} catch (Exception e) {
				voiceName = "failure";
				Log.d(TAG, "连接服务器超时，网络连接错误（获取音频信息错误）");
				Toast.makeText(
						SpotIntroductionActivity.this,
						SpotIntroductionActivity.this.getResources().getString(
								R.string.network_error),
						Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
			if(!voiceName.equals("failure")){
				//如果视频得到正确
				Toast.makeText(
						SpotIntroductionActivity.this,"添加播放音频代码",
						Toast.LENGTH_SHORT).show();

			}else{
				showDialog(GET_VOICE_ERROR_DLG);
			}
		}
	};

	/* 视频按钮监听器 */
	private OnClickListener videoListener = new OnClickListener() {
		@SuppressLint("ParserError")
		public void onClick(View v) {

			String videoName = "";
			String path = GetServerIPAddress.getIpAddress("ipaddress.txt")+SpotIntroductionActivity.this.getResources().getString(R.string.getVideoURL);
			Map<String, String> params = new HashMap<String, String>();
			params.put("spot_id",String.valueOf(SpotIntroductionActivity.this.spot_id));
			try {
				videoName = GetInfoFromServer.getVideoOrVoiceFromServer(path, params, "UTF-8");
				videoName = videoName+".mp4";
			} catch (Exception e) {
				videoName = "failure";
				Log.d(TAG, "连接服务器超时，网络连接错误（获取视频信息错误）");
				Toast.makeText(
						SpotIntroductionActivity.this,
						SpotIntroductionActivity.this.getResources().getString(
								R.string.network_error),
						Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
			if(!videoName.equals("failure")){
				//如果视频得到正确
				Uri uri = Uri.parse(Environment.getExternalStorageDirectory()
						.getPath() + "/" + videoName);
				// 调用系统自带的播放器
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Log.v("URI:::::::::", uri.toString());
				intent.setDataAndType(uri, "video/mp4");
				startActivity(intent);
			}else{
				showDialog(GET_VIDEO_ERROR_DLG);
			}
			
		}
	};
}

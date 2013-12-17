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

	/* �û��� ����ʱ�� */
	private String usernickname;

	/* ����������Ϣ���� */
	private SpotInfo currentSpotInfo;

	/* ����ͼƬ */
	private SpotPhotos spotPhotos;

	/* �������� */
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
			Builder spotCommentNullDlgBuilder = new AlertDialog.Builder(this); // ����Builder����
			spotCommentNullDlgBuilder.setMessage("���۲���Ϊ�գ����������ۺ��ύ");
			spotCommentNullDlgBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = spotCommentNullDlgBuilder.create(); // ����Dialog����
			break;
		case PLEASE_LOGIN_DIALOG:
			Builder pleaseLoginDlgBuilder = new AlertDialog.Builder(this); // ����Builder����
			pleaseLoginDlgBuilder.setMessage("�Բ�����û��Ȩ�����۸þ���");
			pleaseLoginDlgBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = pleaseLoginDlgBuilder.create(); // ����Dialog����
			break;
		case PROGRESS_BAR_DIALOG:
			Log.d(TAG, "case progressbar");
			//�������۲�����ʾ�������Ի���
			SpotIntroductionActivity.this.waitingForCommentSubmitProDlg = ProgressDialog.show(
					SpotIntroductionActivity.this, "��ȴ�", "����Ϊ���ύ����...", true);
			new Thread(new sendCommentThread()).start();	
			break;
		case GET_VOICE_ERROR_DLG:
			Builder getVoiceErrorDlgBuilder = new AlertDialog.Builder(this); // ����Builder����
			getVoiceErrorDlgBuilder.setTitle("�����쳣");
			getVoiceErrorDlgBuilder.setMessage("���ڵ�ǰ������󣬻�ȡ��Ƶ���ܴ���");
			getVoiceErrorDlgBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = getVoiceErrorDlgBuilder.create(); // ����Dialog����
			break;
		case GET_VIDEO_ERROR_DLG:
			Builder getVideoErrorDlgBuilder = new AlertDialog.Builder(this); // ����Builder����
			getVideoErrorDlgBuilder.setTitle("�����쳣");
			getVideoErrorDlgBuilder.setMessage("�Բ������ڵ�ǰ������󣬻�ȡ��Ƶ���ܴ���");
			getVideoErrorDlgBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			spotIntroDlg = getVideoErrorDlgBuilder.create(); // ����Dialog����
			break;
			
		default:
			Log.d(TAG, "����ĶԻ������ʹ���");
		}

		return spotIntroDlg;
	}

	public SpotIntroductionActivity() {

		usernickname = "������";
		currentSpotInfo = new SpotInfo(
				"ʯ��",
				"�ڻ�ɽ���壬��ͨ������ͤ�ı���֮·��Ϊ��ɽ�������յ�֮һ��\n��·���ڵ��������ϣ�����Ψ������������գ�����·�����������ˣ�������������Խż�̽Ѱʯ�ѣ��������,���м�������ӥ��һ��	�����ҷ�ת����ſ�ͨ�������������Ӿ���������������ӷ���ԶԶ�Ȳ��ϳ���ջ�������������ȥ�ˣ���֪�������ӷ�����Ѷȸ���һЩ������������б�ģ������������㡣�������̫�ɶ�������ҡ�ڶ�ƫ�󣬲���ץ�Ρ�"
						+ "�������Ǵ���������Ե���±������㣬�۾�����ŵ��е����ѡ������Ҫ�֡��ۡ��š�ϥ��ȫ������ˡ������ӷ����ȫ;�У�������˿������и����Ҫץ������Ҫ��׼��ϥҪ��ס����Ҫ���ȡ�ȫ���ע�����о������Ѷȣ������ӷ���ȳ���ջ��Ҫ��һ���ǡ�С���������������ý�����̽��һ��һ���ӵ��������ҡ���һС��ֻ��һ�ߵ���������ץ�ա���һ����Ҫ���պ������ƽ��ȡ�"
						+ "���껪ɽ���η�չ�ܹ�˾�Ѷ����ӷ����յ�ȫ�����ޣ�������ѡ�ʯ�ף��ദ������������", 1);
		this.isSpot = false;
		spotPhotos = new SpotPhotos();

		spotPhotos.addPhoto("gallery_photo_1", "��Һã�");
		spotPhotos.addPhoto("gallery_photo_2", "������");
		spotPhotos.addPhoto("gallery_photo_3", "����");
		spotPhotos.addPhoto("gallery_photo_4", "������Ҫ");
		spotPhotos.addPhoto("gallery_photo_5", "picture5");
		spotPhotos.addPhoto("gallery_photo_6", "picture6");
		spotPhotos.addPhoto("gallery_photo_7", "picture7");
		spotPhotos.addPhoto("gallery_photo_8", "picture8");

		commentsList = new SpotCommentsList();
		picPaths = new ArrayList<String>();
		spotPicIntro = new ArrayList<String>();
//		for (int i = 0; i < 10; i++) {
//			commentsList.addTouristComments("��������" + i, "�ҵ�" + i + "������",new Date().toLocaleString());
//		}
	}

	@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spot_introduction);
		/* �����Ƶ��ť������ */
		this.musicBtn = (Button) findViewById(R.id.voice_button);
		this.musicBtn.setOnClickListener(voiceListener);
		//mIndicatorRatingBar.setMax(5);
		/* �����Ƶ��ť������ */
		this.videoBtn = (Button) findViewById(R.id.video_button);
		this.videoBtn.setOnClickListener(videoListener);
		//���ü۸��б�
		this.priceslist = (ListView) this.findViewById(R.id.service_price_view);
		/* ���þ���ȼ�ratingbar���� */
		mIndicatorRatingBar = (RatingBar) findViewById(R.id.small_ratingbar);
//		((RatingBar) findViewById(R.id.ratingbar1))
//				.setOnRatingBarChangeListener(this);
		mIndicatorRatingBar.setNumStars(5);
		//����õ��ľ����Ϊ�����㣬����水ť���ֲ��䣬����ȥ����Ƶ��Ƶ��ť
	
		/**
		 * ����ǹ��ڷ���������Ϣ����Ҫȥ����Ƶ��Ƶ��ť
		 *  findViewById(R.id.video_button).setVisibility(8);
		 *  findViewById(R.id.voice_button).setVisibility(8);
		 *  findViewById(R.id.price_layout).setVisibility(0);
		 * 
		 */
		this.submitCommentBtn = (Button)this.findViewById(R.id.submit_comment);
		String spotIntroductionInfo = this.getIntent().getStringExtra("jsonString");
		this.spot_id = this.getIntent().getIntExtra("spot_id", -1);
		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��,����ס�޼۸�
		ArrayList<HashMap<String, String>> list = new 
						ArrayList<HashMap<String, String>>();
		Log.d(TAG, "��õ�json���ݸ�ʽΪ��"+spotIntroductionInfo);
		Log.d(TAG, "�������ľ����Ϊ��"+this.spot_id);
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
//				picOfHotel.setText("����һ��");
//				TextView touristComment = (TextView)this.findViewById(R.id.touristComment);
//				touristComment.setText("�۸�Ԥ��");
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
						list,// ����Դ
						R.layout.service_price,// ListItem��XMLʵ��
						// ��̬������ImageItem��Ӧ������
						new String[] { "name", "price" },
						// ImageItem��XML�ļ������һ��ImageView,����TextView ID
						new int[] { R.id.service_name,
								R.id.service_price});
				// ��Ӳ�����ʾ�۸�
				priceslist.setAdapter(pricelistItemAdapter);

			}

		} catch (JSONException e) {
			Log.d(TAG, "�������json���ݽ�������");
			e.printStackTrace();
		}

		Log.d(TAG, "����Ϊ��"+content);
		Log.d(TAG, "���۵ȼ�Ϊ��"+String.valueOf(grade_level));
		Log.d(TAG, "��������Ϊ��"+spotName);
		this.commentContent = (EditText)this.findViewById(R.id.spot_me_comment_content);

		for(String path:this.picPaths){
			Log.d(TAG, ""+"��õ�����ͼƬ���ļ���Ϊ��"+path);
		}
		

		/* ������� */
		ListView commentslist = (ListView) findViewById(R.id.spot_comment_list_view);
		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,
				commentsList.getComments(),// ����Դ
				R.layout.comments,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { "name", "time", "content" },
				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.id.spot_comment_tourist_name,
						R.id.spot_comment_tourist_time,
						R.id.spot_comment_tourist_content });
		// ��Ӳ�����ʾ����
		commentslist.setAdapter(listItemAdapter);
		
		myHandler = new Handler() {
			@SuppressLint("ParserError")
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case COMMENT_SUBMIT:
					SpotIntroductionActivity.this.waitingForCommentSubmitProDlg.dismiss();
					switch (msg.arg1) {
					case 1:// ��¼�ɹ�����Ӧ
						Log.d(TAG, "�������۳ɹ�");
						Toast.makeText(SpotIntroductionActivity.this, "�������۳ɹ�", 1).show();
						break;
					case 2:
						Log.d(TAG, "���ӷ������ɹ������Ƿ��������ط������۴���");
						Toast.makeText(SpotIntroductionActivity.this, "�Բ������ķ�������ʧ��", 1).show();
						break;
					case 3:
						Log.d(TAG, "�������۳�ʱ���������Ӵ���");
						Toast.makeText(
								SpotIntroductionActivity.this,
								SpotIntroductionActivity.this.getResources()
										.getString(
												R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "��½���ִ������̷߳��صĽ���Ǵ����");

					}
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		
		/* ���þ�������TextView�ռ�text���� */
		TextView spotNameText = (TextView) findViewById(R.id.spot_name);
		spotNameText.setText(this.spotName);
		/* ���þ������TextView�ռ�text���� */
		TextView spotDescriptionText = (TextView) findViewById(R.id.spot_description);
		spotDescriptionText.setText(this.content);
		
		//�������ֵȼ�
		this.mIndicatorRatingBar.setRating(grade_level);
		
		

		this.userNickNameText = (EditText) findViewById(R.id.spot_me_comment_nickname);
		/* ����Ѿ���¼����ʾ�ҵ��û��� */
		if (usernickname != null) {
			/* ���þ�������TextView�ռ�text���� */
			this.userNickNameText.setText("������");
		}
		


		// Reference the Gallery view
		Gallery g = (Gallery) findViewById(R.id.gallery);
		// Set the adapter to our custom adapter (below)
		g.setAdapter(new ImageAdapter(this));

		/* GalleryͼƬ����Ŵ�Ч����ժ�Ի����� */
		// Set a item click listener, and just Toast the clicked position
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				/* �޸ģ���ʾͼƬ���� */
				
				Toast.makeText(SpotIntroductionActivity.this,
						SpotIntroductionActivity.this.spotPicIntro.get(position), Toast.LENGTH_SHORT)
						.show();

				if (position >= 0 && position < spotPhotos.getNumOfPhotos()) {

					AnimationSet animationSet = new AnimationSet(true);

					if (manimationSet != null && manimationSet != animationSet) {
						ScaleAnimation scaleAnimation = new ScaleAnimation(2,
								0.5f, 2, 0.5f, Animation.RELATIVE_TO_SELF,
								0.5f, // ʹ�ö�������ͼƬ
								Animation.RELATIVE_TO_SELF, 0.5f);
						scaleAnimation.setDuration(1000);
						manimationSet.addAnimation(scaleAnimation);
						manimationSet.setFillAfter(true); // ���䱣�ֶ�������ʱ��״̬��
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

		
		//�ύ���۲���
		this.submitCommentBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				comment = SpotIntroductionActivity.this.commentContent.getText().toString();
				if(comment.equals("")){
					showDialog(COMMENT_CAN_NOT_NULL_DLG);
				}else if(!dataStorage.is_login){
					showDialog(PLEASE_LOGIN_DIALOG);
				}else{
					//���;�������
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

				if (isCommentSubmitOK) {// �������۳ɹ�
					this.comment_submit_status_code = 1;
					Log.d(TAG, "�������۳ɹ�");
				} else {// ��������ʧ�ܣ��������ӵ���������
					this.comment_submit_status_code = 2;
					Log.d(TAG, "��������ʧ�ܣ��������ӵ���������");
				}

			} catch (Exception e) {
				// �������Ӵ���
				this.comment_submit_status_code = 3;
				Log.d(TAG, "�������Ӵ���");
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

	/* ������ʱ�ĸ����˵� */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(R.string.photo_long_press);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		/* �޸ģ���ʾͼƬ���� */
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
				Log.d(TAG, "ͼƬ����");
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

	/* ��Ƶ��ť������ */
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
				Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨻�ȡ��Ƶ��Ϣ����");
				Toast.makeText(
						SpotIntroductionActivity.this,
						SpotIntroductionActivity.this.getResources().getString(
								R.string.network_error),
						Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
			if(!voiceName.equals("failure")){
				//�����Ƶ�õ���ȷ
				Toast.makeText(
						SpotIntroductionActivity.this,"��Ӳ�����Ƶ����",
						Toast.LENGTH_SHORT).show();

			}else{
				showDialog(GET_VOICE_ERROR_DLG);
			}
		}
	};

	/* ��Ƶ��ť������ */
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
				Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨻�ȡ��Ƶ��Ϣ����");
				Toast.makeText(
						SpotIntroductionActivity.this,
						SpotIntroductionActivity.this.getResources().getString(
								R.string.network_error),
						Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
			if(!videoName.equals("failure")){
				//�����Ƶ�õ���ȷ
				Uri uri = Uri.parse(Environment.getExternalStorageDirectory()
						.getPath() + "/" + videoName);
				// ����ϵͳ�Դ��Ĳ�����
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

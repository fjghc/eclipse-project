package com.work.map;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.xxl.network.GetInfoFromServer;
import com.xxl.network.HttpRequest;
import com.xxl.utility.FeedbackActivity;
import com.xxl.utility.GetDataFromFile;
import com.xxl.utility.GetServerIPAddress;
import com.xxl.utility.ProximityAlertReciever;
import com.xxl.utility.RandomGeoPointGenerate;
import com.xxl.utility.WaySolutionOverlay;
import com.xxl.utility.dataStorage;
import com.xxl.utility.hotZoneOverLay;

import java.net.ServerSocket;
import java.net.Socket;


//地图Activity
@SuppressLint({ "ParserError", "ParserError", "ParserError", "HandlerLeak",
		"ParserError", "ParserError", "ParserError", "ParserError",
		"ParserError", "ParserError", "ParserError", "ParserError",
		"ParserError", "ParserError", "ParserError", "ParserError",
		"ParserError", "ShowToast", "ShowToast", "ShowToast", "ShowToast",
		"ShowToast", "ShowToast", "ShowToast", "ShowToast", "ShowToast",
		"ParserError", "ParserError", "ParserError", "ParserError", "NewApi",
		"ParserError", "ParserError", "ParserError", "ParserError" })
public class MyMapActivity extends MapActivity {

	private LocationManager locationManager;
	private PendingIntent pi;
	private MapView mapView;
	private MapController mc;
	private Button phonebtn;// 紧急呼叫按钮
	private Button setModeBtn;// 设置图层标记按钮
	private Button backToCenterBtn;// 返回地图中心按钮
	private Button homeBtn;// 去除地图上多余图层
	private Bitmap bmpmap;
	private int selectedItem; // 规划线路时选择的景点号
	private GeoPoint center_geoPoint, lt_geoPoint, rb_geoPoint;
	// 景点中心坐标以及左上角右下角坐标
	// 地图标志坐标；卫生间坐标；餐厅坐标；旅馆坐标
	private ArrayList<GeoPoint> testSpots;
	private ArrayList<GeoPoint> toilets;
	private ArrayList<GeoPoint> restpoints;
	private ArrayList<GeoPoint> restaurants;
	private ArrayList<GeoPoint> hotels;
	// 行走轨迹所包含的点,在轨迹图层中画出行走轨迹

	private ArrayList<GeoPoint> MyFootsteps;
	private ArrayList<GeoPoint> MyFootstepsFromServer;
	private ArrayList<GeoPoint> WaySolutionGeoPoints;
	private ArrayList<GeoPoint> hotZone;
	// mapView的图层
	private ArrayList<OverlayItem> list;// 景点涂层中OverlayItem列表；
	private ArrayList<OverlayItem> toiletLists;
	private ArrayList<OverlayItem> restpointLists;
	private ArrayList<OverlayItem> restaruantLists;
	private ArrayList<OverlayItem> hotelLists;

	// 数组用来保存图层的标记图标的id
	private int[] imgIds = { R.drawable.footprint, R.drawable.spot,
			R.drawable.restroom, R.drawable.restpoint, R.drawable.restaurant,
			R.drawable.hotel };

	// 展示图层
	private ShowOverlay sol;
	private ShowOverlay tolitSol;
	private ShowOverlay restpointSol;
	private ShowOverlay hotelSol;
	private ShowOverlay restaruantSol;
	private ArrayList<GeoPoint> allTouristsGPs;

	private final int PLEASE_LOGIN_DIALOG = 1010;
	private final int LIST_DIALOG_SINGLE = 1003; // 记录单选列表对话框的id
	private final int LIST_MOOD_SELECT = 1004; // 选择模式时显示的dialog号
	private final int exit_dialog = 1005;
	private final int inteligent_dialog = 1006;
	private final int WAITING_FOR_FRIENDLIST = 1009;
	private final int FRIEND_LIST_END = 1011;
	private static final int TOURIST_DENSITY_DIALOG = 1008;
	private static final int TOURIST_ROUTE_ERROR = 1002;
	private static final int GET_ROUTE_ERROR = 1013;

	private final static int CAMERA_RESULT = 0;
	// 定时发送地理位置消息的变量定义
	private final static int SENDLOCATION = 10000;
	private Handler sendLocationHandler;
	public Handler getNotificationHandler;
	// 一下均为菜单ID
	private final String TAG = "MyMapActivity";
	public static final int RED_MENU_ID = Menu.FIRST;
	public static final int GREEN_MENU_ID = Menu.FIRST + 1;
	public static final int BLUE_MENU_ID = Menu.FIRST + 2;
	public static final int RED_MENU_ID0 = Menu.FIRST + 3;
	public static final int GREEN_MENU_ID0 = Menu.FIRST + 4;
	public static final int BLUE_MENU_ID0 = Menu.FIRST + 5;
	public static final int MODE_CHANGE = Menu.FIRST + 6;
	public static final int WEATHER_INQUIRE1 = Menu.FIRST + 7;
	public static final int HELPER = Menu.FIRST + 8;
	public static final int ACCOUNT_MANEGER = Menu.FIRST + 9;
	public static final int FEEDBACK = Menu.FIRST + 10;
	public static final int EXIT = Menu.FIRST + 11;
	protected static final int WAITIN_FOR_MYROUTE_DLG = 1014;
	private static final int WAITIN_FOR_ALL_THE_TOURISTS_DLG = 1015;
	private static final int GET_TOURISTS_DENSITY = 1016;
	protected static final int WAITING_FOR_ROUTE = 1017;
	private static final int GET_ROUTE_CALC = 1018;

	// 路线规划获得景点
	private int spot_id = -1;
	// 等待服务器返回好友列表的进度条对话框
	private ProgressDialog waitingForFriendListProDlg;
	private ProgressDialog waitingRouteCalcDlg;

	public Handler getFriendListHandler;
	private ProgressDialog waitingForMyrouteDlg;
	private ProgressDialog waitingForAllTouristsDlg;
	private Handler getMyRouteHandler;
	private Handler getAllTouristsHandler;
	private Handler getRouteCalcHandler;
	private Map<String, String> routeCalcParams;
	//广播的intent中的action
	private static final String ACTION = "xxy.notification"; 
	private String serverIP = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.showDialog(inteligent_dialog);
		// 提示您已进入华山分景区
		Toast.makeText(this, "您已进入华山风景区", Toast.LENGTH_SHORT).show();
		// 行走轨迹所包含的点,在轨迹图层中画出行走轨迹
		this.serverIP = GetServerIPAddress.getIpAddress("ipaddress.txt");
		MyFootsteps = new ArrayList<GeoPoint>();
		allTouristsGPs = new ArrayList<GeoPoint>();
		String fileName = this.getResources()
				.getString(R.string.route_filename);
		System.out.println(fileName);
		try {
			MyFootsteps = GetDataFromFile.readTouristRoute(fileName, this);
		} catch (Exception e) {
			Log.d(TAG, "文件读写处异常!!!");
			System.exit(1);
			e.printStackTrace();
		}

		this.WaySolutionGeoPoints = new ArrayList<GeoPoint>();
		this.MyFootstepsFromServer = new ArrayList<GeoPoint>();
		
		// 得到路线规划的线程的handler
		this.getRouteCalcHandler = new Handler() {
			String routeCalcData = "";
			String ToastPrompt = "";

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case GET_ROUTE_CALC:
					switch (msg.arg1) {

					case 1:
						routeCalcData = (String) msg.obj;
						break;
					case 2:
						routeCalcData = "failure";
						Log.d(TAG, "连接服务器超时，网络连接错误（获取游客密度信息）");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "得到游客密度handler出现错误");
					}

					MyMapActivity.this.waitingRouteCalcDlg.dismiss();
					if (routeCalcData.equals("failure")) {
						showDialog(GET_ROUTE_ERROR);
					} else {
						// 解析服务器传过来的json数据格式的获取路线规划信息，
						// JSONTODO
						Bitmap startPoint = BitmapFactory.decodeResource(
								MyMapActivity.this.getResources(),
								R.drawable.endpoint );
						Bitmap endPoint = BitmapFactory.decodeResource(
								MyMapActivity.this.getResources(),
								R.drawable.startpoint);
						// JSONTODO 服务器得到的数据解析成为点集，
						// 存入：MyMapActivity.this.WaySolutionGeoPoints 中
						try {
							JSONObject route_calc = new JSONObject(
									routeCalcData);
							JSONArray routeLists = route_calc
									.getJSONArray("routePointList");
							MyMapActivity.this.WaySolutionGeoPoints = new ArrayList<GeoPoint>();
							for (int i = 0; i < routeLists.length(); i++) {
								JSONObject routePoint = (JSONObject) 
										routeLists
										.get(i);
								double lat = routePoint.getDouble("lat");
								double lng = routePoint.getDouble("lng");
								GeoPoint gpRoute = new GeoPoint(
										(int) (lat * 1E6), (int) (lng * 1E6));
								MyMapActivity.this.WaySolutionGeoPoints
										.add(gpRoute);
							}
							ToastPrompt = route_calc.getString("toast");

						} catch (JSONException e) {
							Log.d(TAG, "已走路线解析错误");
							e.printStackTrace();
						}
						if(MyMapActivity.this.WaySolutionGeoPoints.size()==0){
							Toast.makeText(MyMapActivity.this, ToastPrompt,
									Toast.LENGTH_LONG).show();
						}else{
							WaySolutionOverlay wayOverlay = new WaySolutionOverlay(
									startPoint, endPoint,
									MyMapActivity.this.WaySolutionGeoPoints);
							List<Overlay> ol = new ArrayList<Overlay>();
							ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
							ol.clear();
							ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
									lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
							ol.add(wayOverlay);
							MyMapActivity.this.mapView.invalidate();
							Toast.makeText(MyMapActivity.this, ToastPrompt,
									Toast.LENGTH_LONG).show();
						}
					}
					break;
				}

			}

		};

		// 得到游客消息线程的handler
		this.getAllTouristsHandler = new Handler() {
			String touristsDensityData = "";

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case GET_TOURISTS_DENSITY:
					switch (msg.arg1) {

					case 1:
						touristsDensityData = (String) msg.obj;
						break;
					case 2:
						touristsDensityData = "failure";
						Log.d(TAG, "连接服务器超时，网络连接错误（获取游客密度信息）");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "得到游客密度handler出现错误");
					}

					MyMapActivity.this.waitingForAllTouristsDlg.dismiss();
					if (touristsDensityData.equals("failure")) {
						showDialog(TOURIST_DENSITY_DIALOG);
					} else {
						// 解析服务器传过来的json数据格式的已走路线信息，
						// JSONTODO
						try {
							JSONObject myRoute = new JSONObject(
									touristsDensityData);
							JSONArray routeLists = myRoute
									.getJSONArray("touristsPoints");
							for (int i = 0; i < routeLists.length(); i++) {
								JSONObject routePoint = (JSONObject) routeLists
										.get(i);
								double lat = routePoint.getDouble("lat");
								double lng = routePoint.getDouble("lng");
								GeoPoint gpRoute = new GeoPoint(
										(int) (lat * 1E6), (int) (lng * 1E6));
								MyMapActivity.this.allTouristsGPs.add(gpRoute);
							}
						} catch (JSONException e) {
							Log.d(TAG, "已走路线解析错误");
							e.printStackTrace();
						}
						hotZoneOverLay touristsDensityOverlay = new hotZoneOverLay(
								MyMapActivity.this.allTouristsGPs);
						List<Overlay> ol = MyMapActivity.this.mapView
								.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
						ol.add(touristsDensityOverlay);
						MyMapActivity.this.mapView.invalidate();
					}
					break;
				}

			}

		};

		// 进入地图后设置定时器定时发送位置信息
		this.getMyRouteHandler = new Handler() {
			String jsonTouristRoute = "";

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1100:
					switch (msg.arg1) {
					case 1:// 发送消息成功
						jsonTouristRoute = (String) msg.obj;
						break;
					case 2:
						jsonTouristRoute = "failure";
						Log.d(TAG, "连接服务器超时，网络连接错误（获取已走路线信息）");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "已走路线handler出现错误");
					}

					MyMapActivity.this.waitingForMyrouteDlg.dismiss();
					
					if (jsonTouristRoute.equals("failure")) {
						showDialog(TOURIST_ROUTE_ERROR);
					} else {
						// 解析服务器传过来的json数据格式的已走路线信息，
						// JSONTODO
						MyMapActivity.this.MyFootstepsFromServer = new ArrayList<GeoPoint>();
						try {
							JSONObject myRoute = new JSONObject(
									jsonTouristRoute);
							JSONArray routeLists = myRoute
									.getJSONArray("routePointList");
							for (int i = 0; i < routeLists.length(); i++) {
								JSONObject routePoint = (JSONObject) routeLists
										.get(i);
								double lat = routePoint.getDouble("lat");
								double lng = routePoint.getDouble("lng");
								GeoPoint gpRoute = new GeoPoint(
										(int) (lat * 1E6), (int) (lng * 1E6));
								MyMapActivity.this.MyFootstepsFromServer
										.add(gpRoute);
							}
						} catch (JSONException e) {
							Log.d(TAG, "已走路线解析错误");
							e.printStackTrace();
						}
						FootStepOverLay footOverlay = new FootStepOverLay(
								MyMapActivity.this.MyFootstepsFromServer);
						List<Overlay> ol = MyMapActivity.this.mapView
								.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
						ol.add(footOverlay);
						MyMapActivity.this.mapView.invalidate();

					}
					break;
				}
				if (MyMapActivity.this.MyFootsteps.size() > 1) {
					MyMapActivity.this.MyFootsteps.remove(0);
				}
				// 在onCreate函数中编的当前位置信息
				LocationUtils.addAnimationToMap(MyMapActivity.this.mapView,
						R.drawable.location_anim,
						MyMapActivity.this.MyFootsteps.get(0));
				MyMapActivity.this.mapView.invalidate();
				super.handleMessage(msg);
			}
		};
		MyMapActivity.this.sendLocationHandler = new Handler() {
			@SuppressLint("ParserError")
			String notificationMsg = "";
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SENDLOCATION:

					switch (msg.arg1) {
					case 1:// 发送消息成功
						Log.d(TAG, "发送消息成功");
						LocationUtils.addAnimationToMap(
								MyMapActivity.this.mapView,
								R.drawable.location_anim,
								MyMapActivity.this.MyFootsteps.get(0));

						MyMapActivity.this.mapView.invalidate();
						
						break;
					case 2:// 用户名或密码错误的响应
						Log.d(TAG, "发送消息出错，连接到服务器，服务器端可能出现问题");
						break;
					case 3:
						Log.d(TAG, "连接服务器超时，网络连接错误（定时发送位置信息）");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "发送位置信息错误！定时器返回的结果是错误的");

					}
					notificationMsg = (String)msg.obj;
					break;			
				}
				
				if (MyMapActivity.this.MyFootsteps.size() > 1) {
					MyMapActivity.this.MyFootsteps.remove(0);
				}
				if(!notificationMsg.equals("")){
					showNotification(MyMapActivity.this,notificationMsg);
				}
				// 在onCreate函数中编的当前位置信息
				LocationUtils.addAnimationToMap(MyMapActivity.this.mapView,
						R.drawable.location_anim,
						MyMapActivity.this.MyFootsteps.get(0));
				MyMapActivity.this.mapView.invalidate();
				super.handleMessage(msg);
			}
		};

		Timer timer = new Timer();
		// 每分钟发送一次地理位置信息
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				int send_status_code = 0;
				String receievedMsg = "";
				String notificationMsg = "";
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("username", dataStorage.user_name);
					GeoPoint sendPoint = MyMapActivity.this.MyFootsteps.get(0);
					String latitude = Integer.toString(sendPoint
							.getLatitudeE6());
					String longtitude = Integer.toString(sendPoint
							.getLongitudeE6());
					params.put("currentlat", latitude);
					params.put("currentlng", longtitude);

					receievedMsg = GetInfoFromServer.sendLocationToServer(params, "UTF-8");
					JSONObject json = new JSONObject(receievedMsg);
					boolean isSuccess = json.getBoolean("success");
					notificationMsg = json.getString("message");
					Log.d(TAG,"得到的消息为："+receievedMsg);
					if (isSuccess) {// 服务器接受成功
						send_status_code = 1;
					} else {// 服务器接受失败
						send_status_code = 2;
					}

				} catch (IOException e) {
					// 网络连接错误
					send_status_code = 3;
					Log.d(TAG, "网络连接错误");
					e.printStackTrace();
				} catch (Exception e) {
					Log.d(TAG, "mapActivity中的未知错误");
					e.printStackTrace();
				}
				Message message = new Message();
				message.what = MyMapActivity.SENDLOCATION;
				message.arg1 = send_status_code;
				message.obj = notificationMsg;
				MyMapActivity.this.sendLocationHandler.sendMessage(message);
			}
		}, 0, 10 * 1000);

		MyMapActivity.this.getFriendListHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1011:
					waitingForFriendListProDlg.dismiss();
					switch (msg.arg1) {
					case 1:// 发送消息成功
						Log.d(TAG, "成功获得好友列表的json格式数据");
						String jsonFormat = (String) msg.obj;
						// JSONTODO
						Intent intentToFriendLocation = new Intent(
								MyMapActivity.this,
								friendLocationActivity.class);
						intentToFriendLocation.putExtra("friendlist",
								jsonFormat);
						MyMapActivity.this.startActivityForResult(
								intentToFriendLocation, 100);
						break;
					case 2:// 好友列表状态码2
						Log.d(TAG, "好友列表为空，请自行添加好友");
						String noFriends = "nofriends";

						intentToFriendLocation = new Intent(MyMapActivity.this,
								friendLocationActivity.class);
						intentToFriendLocation
								.putExtra("friendlist", noFriends);
						MyMapActivity.this.startActivityForResult(
								intentToFriendLocation, 100);

						break;
					case 3:
						Log.d(TAG, "连接服务器超时，网络连接错误（好友列表请求）");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "发送好友列表出问题了，未知的异常");
					}

					break;
				}
				super.handleMessage(msg);
			}
		};

		// 显示智能模式的对话框

		this.bmpmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.huashanmap);
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		this.homeBtn = (Button) findViewById(R.id.resetMapViewBtn);
		phonebtn = (Button) findViewById(R.id.phone);
		phonebtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(MyMapActivity.this)
						.setTitle("提示")
						.setMessage("是否发送位置信息请求救援")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										boolean sendOK = false;
										Map<String, String> params = new HashMap<String, String>();
										params.put("username",
												dataStorage.user_name);
										GeoPoint sendPoint = MyMapActivity.this.MyFootsteps
												.get(0);
										String latitude = Integer
												.toString(sendPoint
														.getLatitudeE6());
										String longtitude = Integer
												.toString(sendPoint
														.getLongitudeE6());
										params.put("lat", latitude);
										params.put("lng", longtitude);

										try {
											sendOK = HttpRequest
													.sendPostRequest(
															MyMapActivity.this.serverIP +MyMapActivity.this
																	.getResources()
																	.getString(
																			R.string.rescueToServerletURL),
															params, "UTF-8");
										} catch (Exception e) {
											// 报警时网络连接错误。
											Log.d(TAG, "报警信息发送错误。");
											Toast.makeText(
													MyMapActivity.this,
													MyMapActivity.this
															.getResources()
															.getString(
																	R.string.network_error),
													Toast.LENGTH_SHORT).show();
											e.printStackTrace();
										}
										if (sendOK) {
											AlertDialog dlg = new AlertDialog.Builder(
													MyMapActivity.this)
													.setTitle("提示")
													.setMessage("求救信号已发出！")
													.setPositiveButton(
															"确定",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {

																}
															}).create();
											dlg.show();
										} else {
											// 报警信息未能成功发送提示对话框
											AlertDialog failuredlg = new AlertDialog.Builder(
													MyMapActivity.this)
													.setTitle("提示")
													.setMessage(
															"报警信息发送失败，请稍后重新尝试")
													.setPositiveButton(
															"确定",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {

																}
															}).create();
											failuredlg.show();
										}

									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create();
				dialog.show();

			}
		});

		this.backToCenterBtn = (Button) findViewById(R.id.returnToCenter);

		// 将地图中心位置传入
		String sLong = getResources().getString(R.string.center_Long);
		String sLat = getResources().getString(R.string.center_Lat);
		String sltLong = this.getResources().getString(R.string.lt_Long);
		String sltLat = this.getResources().getString(R.string.lt_Lat);

		String srbLong = this.getResources().getString(R.string.rb_Long);
		String srbLat = this.getResources().getString(R.string.rb_Lat);
		double dLong = Double.parseDouble(sLong);
		double dLat = Double.parseDouble(sLat);

		double dltLong = Double.parseDouble(sltLong);
		double dltLat = Double.parseDouble(sltLat);

		double drbLong = Double.parseDouble(srbLong);
		double drbLat = Double.parseDouble(srbLat);

		// 定位服务的代码编写：
		this.locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		this.locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 1000, 0,
				new UserLocationListener());

		// 添加景点的图层
		// -----------------创建OverlayItem链表----------------
		// 加入所有标志的坐标

		testSpots = new ArrayList<GeoPoint>();
		toilets = new ArrayList<GeoPoint>();
		restpoints = new ArrayList<GeoPoint>();
		restaurants = new ArrayList<GeoPoint>();
		hotels = new ArrayList<GeoPoint>();

		// 景点位置
		list = new ArrayList<OverlayItem>();
		double Lati1 = 34.4854057422299;
		double Longti1 = 110.083544254302;
		GeoPoint jinsuoguanGP = new GeoPoint((int) (Lati1 * 1E6),
				(int) (Longti1 * 1E6));

		double Lati2 = 34.495203722982700;
		double Longti2 = 110.078372955322000;
		GeoPoint qianChiGP = new GeoPoint((int) (Lati2 * 1E6),
				(int) (Longti2 * 1E6));

		double Lati3 = 34.4844152685634;
		double Longti3 = 110.074424743652;
		GeoPoint xifendingGP = new GeoPoint((int) (Lati3 * 1E6),
				(int) (Longti3 * 1E6));

		double Lati4 = 34.512762926499500;
		double Longti4 = 110.074725151062000;
		GeoPoint wuLiGuanGP = new GeoPoint((int) (Lati4 * 1E6),
				(int) (Longti4 * 1E6));

		double Lati5 = 34.4857771668226;
		double Longti5 = 110.087621212005;
		GeoPoint dongfengGP = new GeoPoint((int) (Lati5 * 1E6),
				(int) (Longti5 * 1E6));

		double Lati6 = 34.4771101620668;
		double Longti6 = 110.083973407745;
		GeoPoint changkongzhandaoGP = new GeoPoint((int) (Lati6 * 1E6),
				(int) (Longti6 * 1E6));
		// 鹞子翻身
		double Lati7 = 34.486413890847800;
		double Longti7 = 110.088007450103000;
		GeoPoint yaozifanshengGP = new GeoPoint((int) (Lati7 * 1E6),
				(int) (Longti7 * 1E6));

		double Lati8 = 34.504823724647000;
		double Longti8 = 110.075599551200000;
		GeoPoint shiMenGP = new GeoPoint((int) (Lati8 * 1E6),
				(int) (Longti8 * 1E6));

		double Lati9 = 34.490570164364300;
		double Longti9 = 110.076956748962000;
		GeoPoint huiXinShiGP = new GeoPoint((int) (Lati9 * 1E6),
				(int) (Longti9 * 1E6));

		double Lati10 = 34.498068618957200;
		double Longti10 = 110.085411071777000;
		GeoPoint beiFengGP = new GeoPoint((int) (Lati10 * 1E6),
				(int) (Longti10 * 1E6));

		double Lati11 = 34.490004215897200;
		double Longti11 = 110.082535743713000;
		GeoPoint cangLongGP = new GeoPoint((int) (Lati11 * 1E6),
				(int) (Longti11 * 1E6));
		double Lati12 = 34.48763426497670;
		double Longti12 = 110.083694458007000;
		GeoPoint wuYunFengGP = new GeoPoint((int) (Lati12 * 1E6),
				(int) (Longti12 * 1E6));
		double Lati13 = 34.482418809335500;
		double Longti13 = 110.083171427249000;
		GeoPoint zhongFengGP = new GeoPoint((int) (Lati13 * 1E6),
				(int) (Longti13 * 1E6));
		double Lati14 = 34.476800609529700;
		double Longti14 = 110.080186128616000;
		GeoPoint nanFengGP = new GeoPoint((int) (Lati14 * 1E6),
				(int) (Longti14 * 1E6));
		double Lati15 = 34.482929536011800;
		double Longti15 = 110.073094367980000;
		GeoPoint sheShengYaGP = new GeoPoint((int) (Lati15 * 1E6),
				(int) (Longti15 * 1E6));
		double Lati16 = 34.496326703197200;
		double Longti16 = 110.075293779373000;
		GeoPoint maoNvDongGP = new GeoPoint((int) (Lati16 * 1E6),
				(int) (Longti16 * 1E6));

		this.testSpots.add(jinsuoguanGP);
		this.testSpots.add(qianChiGP);
		this.testSpots.add(xifendingGP);
		this.testSpots.add(dongfengGP);
		this.testSpots.add(changkongzhandaoGP);
		this.testSpots.add(yaozifanshengGP);
		this.testSpots.add(shiMenGP);
		this.testSpots.add(huiXinShiGP);
		this.testSpots.add(beiFengGP);

		this.testSpots.add(cangLongGP);
		this.testSpots.add(wuYunFengGP);
		this.testSpots.add(zhongFengGP);
		this.testSpots.add(nanFengGP);
		this.testSpots.add(sheShengYaGP);
		this.testSpots.add(maoNvDongGP);
		OverlayItem o1 = new OverlayItem(jinsuoguanGP, "spot", "金锁关");
		OverlayItem o2 = new OverlayItem(qianChiGP, "spot", "千尺幢");
		OverlayItem o3 = new OverlayItem(xifendingGP, "spot", "西峰顶");
		OverlayItem o4 = new OverlayItem(wuLiGuanGP, "spot", "五里关");
		OverlayItem o5 = new OverlayItem(dongfengGP, "spot", "东峰");
		OverlayItem o6 = new OverlayItem(changkongzhandaoGP, "spot", "长空栈道");
		OverlayItem o7 = new OverlayItem(yaozifanshengGP, "spot", "鹞子翻身");
		OverlayItem o8 = new OverlayItem(shiMenGP, "spot", "石门");
		OverlayItem o9 = new OverlayItem(huiXinShiGP, "spot", "回心石");
		OverlayItem o10 = new OverlayItem(beiFengGP, "spot", "北峰");

		OverlayItem o11 = new OverlayItem(cangLongGP, "spot", "苍龙岭");
		OverlayItem o12 = new OverlayItem(wuYunFengGP, "spot", "五云峰");
		OverlayItem o13 = new OverlayItem(zhongFengGP, "spot", "中锋");
		OverlayItem o14 = new OverlayItem(nanFengGP, "spot", "南峰");
		OverlayItem o15 = new OverlayItem(sheShengYaGP, "spot", "舍身崖");
		OverlayItem o16 = new OverlayItem(maoNvDongGP, "spot", "毛女洞");

		list.add(o1);
		list.add(o2);
		list.add(o3);
		list.add(o4);
		list.add(o5);
		list.add(o6);
		list.add(o7);
		list.add(o8);
		list.add(o9);
		list.add(o10);
		list.add(o11);
		list.add(o12);
		list.add(o13);
		list.add(o14);
		list.add(o15);
		list.add(o16);

		this.toiletLists = new ArrayList<OverlayItem>();
		Lati1 = 34.4959973267097;
		Longti1 = 110.07516503334;
		GeoPoint t1GP = new GeoPoint((int) (Lati1 * 1E6), (int) (Longti1 * 1E6));

		Lati2 = 34.4965123913818;
		Longti2 = 110.086698532104;
		GeoPoint t2GP = new GeoPoint((int) (Lati2 * 1E6), (int) (Longti2 * 1E6));

		Lati3 = 34.4967953440106;
		Longti3 = 110.084037780761;
		GeoPoint t3GP = new GeoPoint((int) (Lati3 * 1E6), (int) (Longti3 * 1E6));

		Lati4 = 34.5006151105136;
		Longti4 = 110.090990066528;
		GeoPoint t4GP = new GeoPoint((int) (Lati4 * 1E6), (int) (Longti4 * 1E6));

		Lati5 = 34.5086077592845;
		Longti5 = 110.098543167114;
		GeoPoint t5GP = new GeoPoint((int) (Lati5 * 1E6), (int) (Longti5 * 1E6));

		Lati6 = 34.4891552859953;
		Longti6 = 110.083179473876;
		GeoPoint t6GP = new GeoPoint((int) (Lati6 * 1E6), (int) (Longti6 * 1E6));

		this.toilets.add(t1GP);
		this.toilets.add(t2GP);
		this.toilets.add(t3GP);
		this.toilets.add(t4GP);
		this.toilets.add(t5GP);
		this.toilets.add(t6GP);
		o1 = new OverlayItem(t1GP, "toilet", "卫生间1");
		o2 = new OverlayItem(t2GP, "toilet", "卫生间2");
		o3 = new OverlayItem(t3GP, "toilet", "卫生间3");
		o4 = new OverlayItem(t4GP, "toilet", "卫生间4");
		o5 = new OverlayItem(t5GP, "toilet", "卫生间5");
		o6 = new OverlayItem(t6GP, "toilet", "卫生间6");

		this.toiletLists.add(o1);
		this.toiletLists.add(o2);
		this.toiletLists.add(o3);
		this.toiletLists.add(o4);
		this.toiletLists.add(o5);
		this.toiletLists.add(o6);

		this.restaruantLists = new ArrayList<OverlayItem>();
		Lati1 = 34.4971490334463;
		Longti1 = 110.085625648498;
		GeoPoint re1GP = new GeoPoint((int) (Lati1 * 1E6),
				(int) (Longti1 * 1E6));

		Lati2 = 34.4895797520264;
		Longti2 = 110.082836151123;
		GeoPoint re2GP = new GeoPoint((int) (Lati2 * 1E6),
				(int) (Longti2 * 1E6));

		Lati3 = 34.4877403836224;
		Longti3 = 110.083608627319;
		GeoPoint re3GP = new GeoPoint((int) (Lati3 * 1E6),
				(int) (Longti3 * 1E6));

		Lati4 = 34.4937279168388;
		Longti4 = 110.0753575273890;
		GeoPoint re4GP = new GeoPoint((int) (Lati4 * 1E6),
				(int) (Longti4 * 1E6));
		Lati5 = 34.5054258294155;
		Longti5 = 110.0754058071510;
		GeoPoint re5GP = new GeoPoint((int) (Lati5 * 1E6),
				(int) (Longti5 * 1E6));
		Lati6 = 34.4816635545780;
		Longti6 = 110.0790723868750;
		GeoPoint re6GP = new GeoPoint((int) (Lati6 * 1E6),
				(int) (Longti6 * 1E6));
		this.restaurants.add(re1GP);
		this.restaurants.add(re2GP);
		this.restaurants.add(re3GP);
		this.restaurants.add(re4GP);
		this.restaurants.add(re5GP);
		this.restaurants.add(re6GP);

		o1 = new OverlayItem(re1GP, "restaurant", "望峰饺子馆");
		o2 = new OverlayItem(re2GP, "restaurant", "冲灵面馆");
		o3 = new OverlayItem(re3GP, "restaurant", "清扬泡馍");
		o4 = new OverlayItem(re4GP, "restaurant", "大刀面馆");
		o5 = new OverlayItem(re5GP, "restaurant", "老米家羊肉泡馍");
		o6 = new OverlayItem(re6GP, "restaurant", "西岳茶庄");

		this.restaruantLists.add(o1);
		this.restaruantLists.add(o2);
		this.restaruantLists.add(o3);
		this.restaruantLists.add(o4);
		this.restaruantLists.add(o5);
		this.restaruantLists.add(o6);

		this.hotelLists = new ArrayList<OverlayItem>();
		Lati1 = 34.4835662817742;
		Longti1 = 110.08352279663;
		GeoPoint ht1GP = new GeoPoint((int) (Lati1 * 1E6),
				(int) (Longti1 * 1E6));

		Lati2 = 34.4843445199944;
		Longti2 = 110.085926055908;
		GeoPoint ht2GP = new GeoPoint((int) (Lati2 * 1E6),
				(int) (Longti2 * 1E6));

		Lati3 = 34.4828587861826;
		Longti3 = 110.076184272766;
		GeoPoint ht3GP = new GeoPoint((int) (Lati3 * 1E6),
				(int) (Longti3 * 1E6));

		this.hotels.add(ht1GP);
		this.hotels.add(ht2GP);
		this.hotels.add(ht3GP);
		o1 = new OverlayItem(ht1GP, "hotel", "金天山庄");
		o2 = new OverlayItem(ht2GP, "hotel", "东峰宾馆");
		o3 = new OverlayItem(ht3GP, "hotel", "西峰旅社（翠云宫）");

		this.hotelLists.add(o1);
		this.hotelLists.add(o2);
		this.hotelLists.add(o3);

		this.restpointLists = new ArrayList<OverlayItem>();
		Lati1 = 34.496835344010600;
		Longti1 = 110.084007780761000;
		GeoPoint rp1GP = new GeoPoint((int) (Lati1 * 1E6),
				(int) (Longti1 * 1E6));

		Lati2 = 34.508647759284500;
		Longti2 = 110.098513167114000;
		GeoPoint rp2GP = new GeoPoint((int) (Lati2 * 1E6),
				(int) (Longti2 * 1E6));

		Lati3 = 34.4891952859953;
		Longti3 = 110.083149473876;
		GeoPoint rp3GP = new GeoPoint((int) (Lati3 * 1E6),
				(int) (Longti3 * 1E6));

		Lati4 = 34.4936372803491;
		Longti4 = 110.0755989262000;
		GeoPoint rp4GP = new GeoPoint((int) (Lati4 * 1E6),
				(int) (Longti4 * 1E6));

		Lati5 = 34.4904671517366;
		Longti5 = 110.0770634123230;
		GeoPoint rp5GP = new GeoPoint((int) (Lati5 * 1E6),
				(int) (Longti5 * 1E6));

		Lati6 = 34.4821720749298;
		Longti6 = 110.0827416488070;
		GeoPoint rp6GP = new GeoPoint((int) (Lati6 * 1E6),
				(int) (Longti6 * 1E6));

		this.restpoints.add(rp1GP);
		this.restpoints.add(rp2GP);
		this.restpoints.add(rp3GP);
		this.restpoints.add(rp4GP);
		this.restpoints.add(rp5GP);
		this.restpoints.add(rp6GP);
		o1 = new OverlayItem(rp1GP, "restpoint", "休息点1");
		o2 = new OverlayItem(rp2GP, "restpoint", "休息点2");
		o3 = new OverlayItem(rp3GP, "restpoint", "休息点3");

		o4 = new OverlayItem(rp1GP, "restpoint", "休息点4");
		o5 = new OverlayItem(rp2GP, "restpoint", "休息点5");
		o6 = new OverlayItem(rp3GP, "restpoint", "休息点6");
		this.restpointLists.add(o1);
		this.restpointLists.add(o2);
		this.restpointLists.add(o3);
		this.restpointLists.add(o4);
		this.restpointLists.add(o5);
		this.restpointLists.add(o6);
		// -----------------获取图标M-----------------------------
		Drawable drawable = getApplicationContext().getResources().getDrawable(
				R.drawable.spot);
		// -----------------new ShowOverlay对象-------------------
		sol = new ShowOverlay(drawable, list, this);
		drawable = getApplicationContext().getResources().getDrawable(
				R.drawable.restroom);
		this.tolitSol = new ShowOverlay(drawable, this.toiletLists, this);
		drawable = getApplicationContext().getResources().getDrawable(
				R.drawable.restaurant);

		this.restaruantSol = new ShowOverlay(drawable, this.restaruantLists,
				this);

		drawable = getApplicationContext().getResources().getDrawable(
				R.drawable.hotel);
		this.hotelSol = new ShowOverlay(drawable, this.hotelLists, this);

		drawable = getApplicationContext().getResources().getDrawable(
				R.drawable.restpoint);
		this.restpointSol = new ShowOverlay(drawable, this.restpointLists, this);

		// ------------------将图层添加到MapView---------------

		center_geoPoint = new GeoPoint((int) (dLat * 1E6), (int) (dLong * 1E6));
		// 华山地图左下角坐标
		lt_geoPoint = new GeoPoint((int) (dltLat * 1E6), (int) (dltLong * 1E6));
		// 华山地图右下角坐标
		rb_geoPoint = new GeoPoint((int) (drbLat * 1E6), (int) (drbLong * 1E6));
		mc.setCenter(center_geoPoint);
		mc.setZoom(14);
		this.setModeBtn = (Button) findViewById(R.id.set);

		this.homeBtn.setOnClickListener(new ResetMapViewBtnClickListener());
		this.backToCenterBtn
				.setOnClickListener(new BackToCenterOnclickListener());
		// this.setModeBtn.setOnClickListener(new SetBtnClickListener());
		this.mapView.displayZoomControls(true); // 设置显示放大缩小按钮
		this.mc.animateTo(center_geoPoint); // 将地图移动到指定的地理位置
		List<Overlay> ol = this.mapView.getOverlays(); // 获得MapView的

		ol.clear();

		ol.add(new HuaShanOverlay(this.bmpmap, lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay

		this.hotZone = new ArrayList<GeoPoint>();

		// 游客密度的数据，假数据产生
		double Lat = 34.522555482323300;
		double Longti = 110.077908933162000;

		RandomGeoPointGenerate randGen = new RandomGeoPointGenerate(Lat,
				Longti, 0.001, 40);
		this.hotZone.addAll(randGen.getResult());

		Lat = 34.484415268563400;
		Longti = 110.074424743652000;
		randGen = new RandomGeoPointGenerate(Lat, Longti, 0.001, 60);
		this.hotZone.addAll(randGen.getResult());

		Lat = 34.485405742229900;
		Longti = 110.083544254302000;
		randGen = new RandomGeoPointGenerate(Lat, Longti, 0.001, 33);
		this.hotZone.addAll(randGen.getResult());
		Lat = 34.476800609529700;
		Longti = 110.080186128616000;
		randGen = new RandomGeoPointGenerate(Lat, Longti, 0.001, 47);
		this.hotZone.addAll(randGen.getResult());

		Lat = 34.485777166822600;
		Longti = 110.087621212005000;
		randGen = new RandomGeoPointGenerate(Lat, Longti, 0.001, 26);
		this.hotZone.addAll(randGen.getResult());
		Lat = 34.486413890847800;
		Longti = 110.088007450103000;
		randGen = new RandomGeoPointGenerate(Lat, Longti, 0.001, 59);
		this.hotZone.addAll(randGen.getResult());

		setModeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(LIST_MOOD_SELECT);
			}
		});

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, RED_MENU_ID, 0, R.string.menu1).setIcon(R.drawable.path);
		menu.add(0, GREEN_MENU_ID, 0, R.string.menu2).setIcon(
				R.drawable.camera0);
		menu.add(0, BLUE_MENU_ID, 0, R.string.menu3).setIcon(R.drawable.search);
		menu.add(0, RED_MENU_ID0, 0, R.string.menu4).setIcon(R.drawable.friend);
		menu.add(0, GREEN_MENU_ID0, 0, R.string.menu5).setIcon(
				R.drawable.locate);

		menu.add(0, MODE_CHANGE, 0, R.string.menu7).setIcon(
				R.drawable.marker_count_1);
		menu.add(0, WEATHER_INQUIRE1, 0, R.string.menu8).setIcon(
				R.drawable.marker_count_2);
		menu.add(0, HELPER, 0, R.string.menu9).setIcon(
				R.drawable.marker_count_3);
		menu.add(0, ACCOUNT_MANEGER, 0, R.string.menu10).setIcon(
				R.drawable.marker_count_4);
		menu.add(0, FEEDBACK, 0, R.string.menu11).setIcon(
				R.drawable.marker_count_5);
		menu.add(0, BLUE_MENU_ID0, 0, R.string.menu12).setIcon(
				R.drawable.marker_count_6);

		return true;
	}

	@SuppressLint({ "ParserError", "ParserError", "ParserError", "ParserError",
			"ParserError" })
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case RED_MENU_ID:

			// 显示单选按钮对话框
			showDialog(LIST_DIALOG_SINGLE);

			return true;
		case GREEN_MENU_ID:
//			String imageFilePath = Environment.getExternalStorageDirectory() 
//			.getAbsolutePath() + "/photo.jpg"; 
//			File imageFile = new File(imageFilePath); 
//			Uri imageFileUri = Uri.fromFile(imageFile); 
			 
			Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
			//i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri); 
			startActivityForResult(i, CAMERA_RESULT); 
			return true;
		case BLUE_MENU_ID:
			showDialog(WAITIN_FOR_ALL_THE_TOURISTS_DLG);
			return true;
		case RED_MENU_ID0:// 好友列表菜单选项

			if (dataStorage.is_login) {
				showDialog(this.WAITING_FOR_FRIENDLIST);
			} else {
				showDialog(PLEASE_LOGIN_DIALOG);
			}
			return true;

		case GREEN_MENU_ID0:

			Intent intentToThemeList = new Intent(MyMapActivity.this,
					ThemeList.class);
			this.startActivityForResult(intentToThemeList, 200);
			return true;
		case BLUE_MENU_ID0:
			showDialog(exit_dialog);
			return true;
		case MODE_CHANGE:// 智能模式切换菜单的响应
			SharedPreferences pre = MyMapActivity.this.getSharedPreferences(
					"xxy", Context.MODE_PRIVATE);
			// 先取得配置文件中的智能模式值
			Boolean isIntelMode = pre.getBoolean("inteligentMode", false);
			if (isIntelMode) {// 如果是智能模式，则切换为非智能模式
				Editor editor = pre.edit();
				editor.putBoolean("inteligentMode", false);
				editor.commit();
				this.locationManager.removeProximityAlert(this.pi);
				Toast.makeText(this, "智能模式已取消", Toast.LENGTH_SHORT).show();
				NotificationManager notificationManager = (NotificationManager) MyMapActivity.this
						.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
				notificationManager.cancel(0);
			} else {// 如果不是智能模式，切换为智能模式
				Editor editor = pre.edit();
				editor.putBoolean("inteligentMode", true);
				editor.commit();

				double Lati7 = 34.504823724647000;
				double Longti7 = 110.075599551200000;
				float radius = 50;
				Intent intent2 = new Intent(this, ProximityAlertReciever.class);
				// 将Intent包装成PendingIntent

				pi = PendingIntent.getBroadcast(this, -1, intent2, 0);
				// 添加临近警告
				this.locationManager.addProximityAlert(Lati7, Longti7, radius,
						-1, pi);
				Toast.makeText(this, "智能模式切换成功", Toast.LENGTH_SHORT).show();
				this.showNotification();
			}
			return true;
		case WEATHER_INQUIRE1:// 天气预报的Activity
			Intent intentToWeatherActivity = new Intent();
			intentToWeatherActivity.setClass(this,
					com.weather.my.WeatherReport.class);
			this.startActivity(intentToWeatherActivity);

			return true;
		case HELPER:
			return true;
		case ACCOUNT_MANEGER:// 账户管理菜单
			Intent ToAccountManageActivity = new Intent();
			ToAccountManageActivity.setClass(this, accountManageActivity.class);
			this.startActivity(ToAccountManageActivity);
			return true;
		case FEEDBACK:// 反馈菜单项处理
			Intent intentToFeedbackActivity = new Intent();
			intentToFeedbackActivity.setClass(this, FeedbackActivity.class);
			this.startActivity(intentToFeedbackActivity);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	class BackToCenterOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			View toastView = getLayoutInflater().inflate(
					com.work.map.R.layout.map_toast, null);
			TextView t = (TextView) toastView
					.findViewById(com.work.map.R.id.map_toast_text);
			t.setText("景点中心");
			Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(toastView);
			toast.setGravity(Gravity.TOP | Gravity.LEFT, 275, 465);
			toast.show();
			MapController ct = MyMapActivity.this.mapView.getController();
			ct.setZoom(14);
			// 返回地图中心
			ct.animateTo(MyMapActivity.this.center_geoPoint);
			MyMapActivity.this.mapView.invalidate();

		}

	}

	class UserLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {

			MyMapActivity.this.MyFootsteps.add(new GeoPoint((int) (loc
					.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6)));
			Toast.makeText(
					MyMapActivity.this,
					"定位服务" + String.valueOf(loc.getLatitude()) + " "
							+ String.valueOf(loc.getLongitude()), 0);

			LocationUtils.addAnimationToMap(MyMapActivity.this.mapView,
					R.drawable.location_anim,
					MyMapActivity.this.MyFootsteps.get(0));

			MyMapActivity.this.mapView.invalidate();

		}

		public void onProviderDisabled(String arg0) {

		}

		public void onProviderEnabled(String arg0) {

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

		}

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		Log.d("Theme test", "it wroks here ,fuck");
		Log.d("请求码", "请求码" + requestCode);
		Log.d("结果码", "结果码" + resultCode);
		// 好友定位Activity返回的
		if (resultCode == RESULT_OK) {
			Bundle extras = intent.getExtras();
			Intent intent2 = new Intent(MyMapActivity.this, shareActivity.class);
			intent2.putExtras(extras);
			startActivity(intent2);
		}
		if (requestCode == 100 && resultCode == 101) {
			Boolean isClicked = intent.getBooleanExtra("isClicked", false);
			if (isClicked) {
				// Toast.makeText(this, "isClicked", Toast.LENGTH_SHORT).show();

				double lat1 = intent.getDoubleExtra("latitude", -1);
				double lon1 = intent.getDoubleExtra("longitude", -1);
				GeoPoint gp1 = new GeoPoint((int) (lat1 * 1E6),
						(int) (lon1 * 1E6));
				OverlayItem friendOverlayItem = new OverlayItem(gp1,
						"friend Location", "添加说明");
				ArrayList<OverlayItem> friendLocLists = new ArrayList<OverlayItem>();
				friendLocLists.add(friendOverlayItem);

				Drawable drawable = getApplicationContext().getResources()
						.getDrawable(R.drawable.friendloc);
				// -----------------new ShowOverlay对象-------------------
				ShowOverlay friendShowOverlay = new ShowOverlay(drawable,
						friendLocLists, this);
				List<Overlay> ol = this.mapView.getOverlays(); // 获得MapView的

				ol.clear();

				ol.add(new HuaShanOverlay(this.bmpmap, lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
				ol.add(friendShowOverlay);
			} else {
				// Toast.makeText(this, "is not Clicked",
				// Toast.LENGTH_SHORT).show();
			}
			// 照相页面返回的
		}// 主题页面返回的
		else if (requestCode == 200 && resultCode == 201) {
			Log.d("Theme test", "it wroks here, bless me!!!");
			ArrayList<OverlayItem> themeOverlays = new ArrayList<OverlayItem>();
			String[] themes = intent.getExtras().getStringArray("themeList");

			for (int i = 0; i < this.list.size(); i++) {
				OverlayItem ol = this.list.get(i);
				if (ol.getSnippet().equals(themes[0])) {
					themeOverlays.add(ol);
				} else if (ol.getSnippet().equals(themes[1])) {
					themeOverlays.add(ol);
				} else if (ol.getSnippet().equals(themes[2])) {
					themeOverlays.add(ol);
				} else if (ol.getSnippet().equals(themes[3])) {
					themeOverlays.add(ol);
				} else if (ol.getSnippet().equals(themes[4])) {
					themeOverlays.add(ol);
				}
			}

			Log.d("主题景点数目", "主题部分的图层数目：" + themeOverlays.size());
			Drawable themeDrawable = getApplicationContext().getResources()
					.getDrawable(R.drawable.theme);
			ShowOverlay themeShow = new ShowOverlay(themeDrawable,
					themeOverlays, this);
			List<Overlay> ol = this.mapView.getOverlays(); // 获得MapView的
			ol.clear();

			ol.add(new HuaShanOverlay(this.bmpmap, lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay

			ol.add(themeShow);
			this.mapView.invalidate();

		}

		super.onActivityResult(requestCode, resultCode, intent);

	}

	@SuppressLint({ "ParserError", "ParserError" })
	protected Dialog onCreateDialog(int id) { // 重写onCreateDialog方法
		Dialog dialog = null; // 声明一个Dialog对象用于返回
		switch (id) { // 对id进行判断
		case exit_dialog:
			Builder b2 = new AlertDialog.Builder(this); // 创建Builder对象
			b2.setMessage("确认退出吗？");
			b2.setTitle("提示"); // 设置标题
			b2.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataStorage.is_login = false;
					dataStorage.user_name = "anonymous";
					dialog.dismiss();
					MyMapActivity.this.finish();
				}
			});
			b2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			dialog = b2.create(); // 生成Dialog对象
			break;

		case LIST_DIALOG_SINGLE:

			Builder b = new AlertDialog.Builder(this); // 创建Builder对象
			b.setIcon(R.drawable.header); // 设置图标
			b.setTitle(R.string.title); // 设置标题
			b.setSingleChoiceItems( // 设置单选列表选项
					R.array.msa, 0, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MyMapActivity.this.selectedItem = which;
							switch (which) {
							case 0:
								spot_id = 5;
								break;
							case 1:
								spot_id = 6;
								break;
							case 2:
								spot_id = 8;
								break;
							case 3:
								spot_id = 10;
								break;
							case 4:
								spot_id = 11;
								break;
							case 5:
								spot_id = 14;
								break;
							case 6:
								spot_id = 21;
								break;
							case 7:
								spot_id = 22;
								break;
							case 8:
								spot_id = 23;
								break;
							case 9:
								spot_id = 25;
								break;
							case 10:
								spot_id = 26;
								break;
							case 11:
								spot_id = 28;
								break;
							case 12:
								spot_id = 30;
								break;
							case 13:
								spot_id = 31;
								break;
							case 14:
								spot_id = 34;
								break;
							case 15:
								spot_id = 35;
								break;
							default:
								Log.d(TAG, "路线规划时景点错误");
							}
						}
					});
			b.setPositiveButton(R.string.ok, // 按钮显示的文本
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "您选择的景点编号为:"
									+ MyMapActivity.this.spot_id);
							String routeResult = "";
							routeCalcParams = new HashMap<String, String>();

							GeoPoint sendPoint = MyMapActivity.this.MyFootsteps
									.get(0);
							String latitude = Integer.toString(sendPoint
									.getLatitudeE6());
							String longtitude = Integer.toString(sendPoint
									.getLongitudeE6());
							routeCalcParams.put("username",
									dataStorage.user_name);
							routeCalcParams.put("currentlat", latitude);
							routeCalcParams.put("currentlng", longtitude);
							routeCalcParams.put("des_spot_id",
									String.valueOf(MyMapActivity.this.spot_id));
							showDialog(WAITING_FOR_ROUTE);

						}
					});
			dialog = b.create(); // 生成Dialog对象
			break;

		case WAITING_FOR_ROUTE:
			Log.d(TAG, "显示获取路线规划进度条对话框");

			this.waitingRouteCalcDlg = new ProgressDialog(this);
			this.waitingRouteCalcDlg.setMessage("正在为您获取路线规划信息...");
			this.waitingRouteCalcDlg.setTitle("请等待 ");
			this.waitingRouteCalcDlg.setCancelable(true);
			this.waitingRouteCalcDlg.show();
			new Thread(new getRouteCalcThread()).start();
			break;
		case WAITIN_FOR_MYROUTE_DLG:

			Log.d(TAG, "显示获取已走路线进度条对话框");

			this.waitingForMyrouteDlg = new ProgressDialog(this);
			this.waitingForMyrouteDlg.setMessage("正在为您获取已走路线信息...");
			this.waitingForMyrouteDlg.setTitle("请等待 ");
			this.waitingForMyrouteDlg.setCancelable(true);
			this.waitingForMyrouteDlg.show();
			new Thread(new getMyRouteThread()).start();
			break;

		case WAITIN_FOR_ALL_THE_TOURISTS_DLG:

			Log.d(TAG, "显示获取已走路线进度条对话框");
			this.waitingForAllTouristsDlg = new ProgressDialog(this);
			this.waitingForAllTouristsDlg.setMessage("正在为您获取游客密度信息信息...");
			this.waitingForAllTouristsDlg.setTitle("请等待 ");
			this.waitingForAllTouristsDlg.setCancelable(true);
			this.waitingForAllTouristsDlg.show();
			new Thread(new getAllTouristsThread()).start();
			break;
		// case MYROUTE_ERROR_DLG:
		// Log.d(TAG, "显示获取已走路线错误对话框");
		// Builder getMyRouteErrorBuilder = new AlertDialog.Builder(this); //
		// 创建Builder对象
		// getMyRouteErrorBuilder.setTitle("提示");
		// getMyRouteErrorBuilder.setMessage("网络连接异常，无法获得已走路线信息，请稍后尝试");
		// getMyRouteErrorBuilder.setPositiveButton("确认",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// }
		// });
		// dialog = getMyRouteErrorBuilder.create(); // 生成Dialog对象
		// break;
		case LIST_MOOD_SELECT:
			Builder b1 = new AlertDialog.Builder(this);
			b1.setIcon(R.drawable.setting_press);
			b1.setTitle("标记选择");

			BaseAdapter adapter = new ListItemAdapter();
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int which) {
					List<Overlay> ol;
					switch (which) {
					case 0:
						Toast.makeText(MyMapActivity.this, "您选择显示您已走过的所有路线：", 0)
								.show();
						if(dataStorage.is_login){
							showDialog(WAITIN_FOR_MYROUTE_DLG);
						}else{
							showDialog(PLEASE_LOGIN_DIALOG);
						}
						break;

					case 1:
						Toast.makeText(MyMapActivity.this, "您选择显示地图上所有景点标记", 0)
								.show();
						ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
						ol.add(sol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 2:
						Toast.makeText(MyMapActivity.this, "您选择显示地图上所有卫生间标记：",
								0).show();
						ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
						ol.add(MyMapActivity.this.tolitSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 3:
						Toast.makeText(MyMapActivity.this, "您选择显示地图上所有休息点标记：",
								0).show();
						ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
						ol.add(MyMapActivity.this.restpointSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 4:
						Toast.makeText(MyMapActivity.this, "您选择显示地图上所有餐厅标记：", 0)
								.show();
						ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay

						ol.add(MyMapActivity.this.restaruantSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 5:
						Toast.makeText(MyMapActivity.this, "您选择显示地图上所有宾馆标记：", 0)
								.show();
						ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // 添加一个新的Overlay
						ol.add(MyMapActivity.this.hotelSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					default:
						System.out.println("标记选择处传入错误参数");
						break;
					}
				}
			};
			b1.setAdapter(adapter, listener);
			dialog = b1.create();
			break;

		case WAITING_FOR_FRIENDLIST:
			Log.d(TAG, "显示获取好友列表等待对话框");
			// 发表评论并且显示进度条对话框

			MyMapActivity.this.waitingForFriendListProDlg = new ProgressDialog(
					this);
			MyMapActivity.this.waitingForFriendListProDlg
					.setMessage("正在为您获取好友列表...");
			MyMapActivity.this.waitingForFriendListProDlg.setTitle("请等待 ");
			MyMapActivity.this.waitingForFriendListProDlg.setCancelable(true);
			MyMapActivity.this.waitingForFriendListProDlg.show();
			new Thread(new getFriendListThread()).start();
			break;

		case GET_ROUTE_ERROR:
			Log.d(TAG, "获取路线规划错误");
			Builder getRouteErrorBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			getRouteErrorBuilder.setTitle("提示");
			getRouteErrorBuilder.setMessage("网络连接异常，无法获得最佳路线，请稍后尝试");
			getRouteErrorBuilder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = getRouteErrorBuilder.create(); // 生成Dialog对象
			break;
		case PLEASE_LOGIN_DIALOG:
			Builder pleaseLoginDlgBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			pleaseLoginDlgBuilder.setTitle("提示");
			pleaseLoginDlgBuilder.setMessage("对不起，您还未登录");
			pleaseLoginDlgBuilder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = pleaseLoginDlgBuilder.create(); // 生成Dialog对象
			break;
		case TOURIST_DENSITY_DIALOG:
			Builder touristDensityErrorBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			touristDensityErrorBuilder.setMessage("获取游客密度错误，请稍后尝试");
			touristDensityErrorBuilder.setTitle("提示");
			touristDensityErrorBuilder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = touristDensityErrorBuilder.create(); // 生成Dialog对象
			break;
		case TOURIST_ROUTE_ERROR:
			Builder touristRouteErrorBuilder = new AlertDialog.Builder(this); // 创建Builder对象
			touristRouteErrorBuilder.setTitle("提示");
			touristRouteErrorBuilder.setMessage("获取已走路线错误，请稍后尝试");
			touristRouteErrorBuilder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = touristRouteErrorBuilder.create(); // 生成Dialog对象
			break;
		case inteligent_dialog:
			Builder builder = new AlertDialog.Builder(this); // 创建Builder对象
			builder.setMessage("您是否选择智能导游模式？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences pre = MyMapActivity.this
									.getSharedPreferences("xxy",
											Context.MODE_PRIVATE);
							Editor editor = pre.edit();
							editor.putBoolean("inteligentMode", true);
							editor.commit();
							double Lati7 = 34.504823724647000;
							double Longti7 = 110.075599551200000;
							float radius = 50;
							Intent intent = new Intent(MyMapActivity.this,
									ProximityAlertReciever.class);
							// 将Intent包装成PendingIntent
							MyMapActivity.this.pi = PendingIntent.getBroadcast(
									MyMapActivity.this, -1, intent, 0);
							// 添加临近警告
							MyMapActivity.this.locationManager
									.addProximityAlert(Lati7, Longti7, radius,
											-1, MyMapActivity.this.pi);
							System.out.println("用户选择智能模式，临近警告已经打开！");

							MyMapActivity.this.showNotification();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences pre = MyMapActivity.this
									.getSharedPreferences("xxy",
											Context.MODE_PRIVATE);
							Editor editor = pre.edit();
							editor.putBoolean("inteligentMode", false);
							editor.commit();
							NotificationManager notificationManager = (NotificationManager) MyMapActivity.this
									.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
							notificationManager.cancel(0);
						}
					});

			dialog = builder.create(); // 生成Dialog对象
			break;

		}

		return dialog;
	}

	class ListItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return imgIds.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup parent) {
			TextView textView = new TextView(MyMapActivity.this);
			// 获得array.xml中的数组资源getStringArray返回的是一个String数组
			String text = getResources().getStringArray(R.array.moodarray)[position];
			textView.setText(text);
			// 设置字体大小
			textView.setTextSize(24);
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(layoutParams);
			// 设置水平方向上居中
			textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
			textView.setMinHeight(60);
			// 设置文字颜色
			textView.setTextColor(Color.BLACK);
			// 设置图标在文字的左边
			textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position],
					0, 0, 0);
			// 设置textView的左上右下的padding大小
			textView.setPadding(30, 0, 15, 0);
			// 设置文字和图标之间的padding大小
			textView.setCompoundDrawablePadding(60);
			return textView;
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showDialog(exit_dialog);
		}
		return false;
	}

	private void showNotification() {
		// 创建一个NotificationManager的引用
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.notification,
				"智能模式已打开", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;  
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000;

		// 设置通知的事件消息
		CharSequence contentTitle = "逍遥游"; // 通知栏标题
		CharSequence contentText = "逍遥游自助导游智能模式已打开"; // 通知栏内容
		Intent notificationIntent = new Intent(this, MyMapActivity.class); // 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentItent);

		// 把Notification传递给NotificationManager
		notificationManager.notify(0, notification);
	}

	// 清除地图上多余的图层
	class ResetMapViewBtnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			View toastView = getLayoutInflater().inflate(
					com.work.map.R.layout.map_eraser_toast, null);
			TextView t = (TextView) toastView
					.findViewById(com.work.map.R.id.map_eraser);
			t.setText("多余图层已擦除");
			Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(toastView);
			toast.setGravity(Gravity.TOP | Gravity.LEFT, 160, 110);
			toast.show();
			List<Overlay> ol = MyMapActivity.this.mapView.getOverlays(); // 获得MapView的
			ol.clear();
			ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap, lt_geoPoint,
					rb_geoPoint));
			MyMapActivity.this.mapView.invalidate();

		}
	}

	class getFriendListThread implements Runnable {
		private int friend_list_status_code;
		private String jsonFromatFriendList;

		public void run() {
			try {
				String param = "username=";
				param += dataStorage.user_name;
				jsonFromatFriendList = GetInfoFromServer
						.getFriendListFromServer(param);
				if (jsonFromatFriendList.indexOf("success:false") != -1) {
					// 传送失败，好友列表为空，请添加好友
					friend_list_status_code = 2;
				} else {
					// 好友列表传送成功，得到了json格式的数据
					// JSONTODO
					friend_list_status_code = 1;
				}

			} catch (Exception e) {
				// 网络连接错误
				this.friend_list_status_code = 3;
				Log.d(TAG, "网络连接错误");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = MyMapActivity.this.FRIEND_LIST_END;
			message.arg1 = this.friend_list_status_code;
			message.obj = this.jsonFromatFriendList;
			MyMapActivity.this.getFriendListHandler.sendMessage(message);
		}
	}

	class getAllTouristsThread implements Runnable {

		private int all_tourists_status_code;
		private String jsonFromatAllTourist;

		public void run() {
			try {
				jsonFromatAllTourist = GetInfoFromServer
						.getAllTouristsFromServer("");
				all_tourists_status_code = 1;
			} catch (Exception e) {
				Log.d(TAG, "连接服务器超时，网络连接错误（点击游客密度的时候）");
				Toast.makeText(
						MyMapActivity.this,
						MyMapActivity.this.getResources().getString(
								R.string.network_error), Toast.LENGTH_SHORT)
						.show();
				all_tourists_status_code = 2;
				jsonFromatAllTourist = "failure";
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = MyMapActivity.GET_TOURISTS_DENSITY;
			message.arg1 = this.all_tourists_status_code;
			message.obj = this.jsonFromatAllTourist;
			MyMapActivity.this.getAllTouristsHandler.sendMessage(message);
		}
	}

	class getRouteCalcThread implements Runnable {
		private int route_calc_status_code = -1;
		private String RouteCalcData = "";
		private String routeResult = "";

		Map<String, String> params = MyMapActivity.this.routeCalcParams;

		public void run() {
			try {
				routeResult = GetInfoFromServer.getRouteFromServer(params,
						"UTF-8");
				this.route_calc_status_code = 1;
			} catch (Exception e) {
				this.route_calc_status_code = 2;
				routeResult = "failure";
				Log.d(TAG, "路线规划网络错误");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = MyMapActivity.GET_ROUTE_CALC;
			message.arg1 = this.route_calc_status_code;
			message.obj = this.routeResult;
			MyMapActivity.this.getRouteCalcHandler.sendMessage(message);
		}
	}

	class getMyRouteThread implements Runnable {
		private int my_Route_status_code = -1;
		private String jsonFromatMyRoute;
		private String myRouteJson;

		public void run() {
			
			try {
				String param = "username=";
				param = param + dataStorage.user_name;
				myRouteJson = GetInfoFromServer
						.getTouristRouteFromServer(param);
				this.my_Route_status_code = 1;

			} catch (Exception e) {
				// 网络连接错误
				this.my_Route_status_code = 2;
				Log.d(TAG, "网络连接错误");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = 1100;
			message.arg1 = this.my_Route_status_code;
			message.obj = this.myRouteJson;
			MyMapActivity.this.getMyRouteHandler.sendMessage(message);
		}
	}
	
	private void showNotification(Context context,String s) {
		
		// 创建一个NotificationManager的引用
				NotificationManager notificationManager = (NotificationManager) this
						.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

				// 定义Notification的各种属性
				Notification notification = new Notification(R.drawable.notification,
						"管理员消息", System.currentTimeMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
				// notification.flags |= Notification.FLAG_NO_CLEAR; //
				//notification.defaults = Notification.DEFAULT_LIGHTS;
				notification.ledARGB = Color.BLUE;
				notification.ledOnMS = 5000;
				notification.defaults = Notification.DEFAULT_SOUND;  
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				
				// 设置通知的事件消息
				CharSequence contentTitle = "预警信息"; // 通知栏标题
				CharSequence contentText = s; // 通知栏内容
				Intent notificationIntent = new Intent(this, NotificationActivity.class); // 点击该通知后要跳转的Activity
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				notificationIntent.addFlags(Intent.FILL_IN_DATA);
				notificationIntent.putExtra("msg", s);
				PendingIntent contentItent = PendingIntent.getActivity(this, 0,
						notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				Log.d(TAG, "向通知接收Activity传送数据"+s);
				notification.setLatestEventInfo(this, contentTitle, contentText,
						contentItent);	
				// 把Notification传递给NotificationManager
				notificationManager.notify(1, notification);
	}
	
	
}

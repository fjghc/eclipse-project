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


//��ͼActivity
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
	private Button phonebtn;// �������а�ť
	private Button setModeBtn;// ����ͼ���ǰ�ť
	private Button backToCenterBtn;// ���ص�ͼ���İ�ť
	private Button homeBtn;// ȥ����ͼ�϶���ͼ��
	private Bitmap bmpmap;
	private int selectedItem; // �滮��·ʱѡ��ľ����
	private GeoPoint center_geoPoint, lt_geoPoint, rb_geoPoint;
	// �������������Լ����Ͻ����½�����
	// ��ͼ��־���ꣻ���������ꣻ�������ꣻ�ù�����
	private ArrayList<GeoPoint> testSpots;
	private ArrayList<GeoPoint> toilets;
	private ArrayList<GeoPoint> restpoints;
	private ArrayList<GeoPoint> restaurants;
	private ArrayList<GeoPoint> hotels;
	// ���߹켣�������ĵ�,�ڹ켣ͼ���л������߹켣

	private ArrayList<GeoPoint> MyFootsteps;
	private ArrayList<GeoPoint> MyFootstepsFromServer;
	private ArrayList<GeoPoint> WaySolutionGeoPoints;
	private ArrayList<GeoPoint> hotZone;
	// mapView��ͼ��
	private ArrayList<OverlayItem> list;// ����Ϳ����OverlayItem�б�
	private ArrayList<OverlayItem> toiletLists;
	private ArrayList<OverlayItem> restpointLists;
	private ArrayList<OverlayItem> restaruantLists;
	private ArrayList<OverlayItem> hotelLists;

	// ������������ͼ��ı��ͼ���id
	private int[] imgIds = { R.drawable.footprint, R.drawable.spot,
			R.drawable.restroom, R.drawable.restpoint, R.drawable.restaurant,
			R.drawable.hotel };

	// չʾͼ��
	private ShowOverlay sol;
	private ShowOverlay tolitSol;
	private ShowOverlay restpointSol;
	private ShowOverlay hotelSol;
	private ShowOverlay restaruantSol;
	private ArrayList<GeoPoint> allTouristsGPs;

	private final int PLEASE_LOGIN_DIALOG = 1010;
	private final int LIST_DIALOG_SINGLE = 1003; // ��¼��ѡ�б�Ի����id
	private final int LIST_MOOD_SELECT = 1004; // ѡ��ģʽʱ��ʾ��dialog��
	private final int exit_dialog = 1005;
	private final int inteligent_dialog = 1006;
	private final int WAITING_FOR_FRIENDLIST = 1009;
	private final int FRIEND_LIST_END = 1011;
	private static final int TOURIST_DENSITY_DIALOG = 1008;
	private static final int TOURIST_ROUTE_ERROR = 1002;
	private static final int GET_ROUTE_ERROR = 1013;

	private final static int CAMERA_RESULT = 0;
	// ��ʱ���͵���λ����Ϣ�ı�������
	private final static int SENDLOCATION = 10000;
	private Handler sendLocationHandler;
	public Handler getNotificationHandler;
	// һ�¾�Ϊ�˵�ID
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

	// ·�߹滮��þ���
	private int spot_id = -1;
	// �ȴ����������غ����б�Ľ������Ի���
	private ProgressDialog waitingForFriendListProDlg;
	private ProgressDialog waitingRouteCalcDlg;

	public Handler getFriendListHandler;
	private ProgressDialog waitingForMyrouteDlg;
	private ProgressDialog waitingForAllTouristsDlg;
	private Handler getMyRouteHandler;
	private Handler getAllTouristsHandler;
	private Handler getRouteCalcHandler;
	private Map<String, String> routeCalcParams;
	//�㲥��intent�е�action
	private static final String ACTION = "xxy.notification"; 
	private String serverIP = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.showDialog(inteligent_dialog);
		// ��ʾ���ѽ��뻪ɽ�־���
		Toast.makeText(this, "���ѽ��뻪ɽ�羰��", Toast.LENGTH_SHORT).show();
		// ���߹켣�������ĵ�,�ڹ켣ͼ���л������߹켣
		this.serverIP = GetServerIPAddress.getIpAddress("ipaddress.txt");
		MyFootsteps = new ArrayList<GeoPoint>();
		allTouristsGPs = new ArrayList<GeoPoint>();
		String fileName = this.getResources()
				.getString(R.string.route_filename);
		System.out.println(fileName);
		try {
			MyFootsteps = GetDataFromFile.readTouristRoute(fileName, this);
		} catch (Exception e) {
			Log.d(TAG, "�ļ���д���쳣!!!");
			System.exit(1);
			e.printStackTrace();
		}

		this.WaySolutionGeoPoints = new ArrayList<GeoPoint>();
		this.MyFootstepsFromServer = new ArrayList<GeoPoint>();
		
		// �õ�·�߹滮���̵߳�handler
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
						Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨻�ȡ�ο��ܶ���Ϣ��");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "�õ��ο��ܶ�handler���ִ���");
					}

					MyMapActivity.this.waitingRouteCalcDlg.dismiss();
					if (routeCalcData.equals("failure")) {
						showDialog(GET_ROUTE_ERROR);
					} else {
						// ������������������json���ݸ�ʽ�Ļ�ȡ·�߹滮��Ϣ��
						// JSONTODO
						Bitmap startPoint = BitmapFactory.decodeResource(
								MyMapActivity.this.getResources(),
								R.drawable.endpoint );
						Bitmap endPoint = BitmapFactory.decodeResource(
								MyMapActivity.this.getResources(),
								R.drawable.startpoint);
						// JSONTODO �������õ������ݽ�����Ϊ�㼯��
						// ���룺MyMapActivity.this.WaySolutionGeoPoints ��
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
							Log.d(TAG, "����·�߽�������");
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
							ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
							ol.clear();
							ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
									lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
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

		// �õ��ο���Ϣ�̵߳�handler
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
						Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨻�ȡ�ο��ܶ���Ϣ��");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "�õ��ο��ܶ�handler���ִ���");
					}

					MyMapActivity.this.waitingForAllTouristsDlg.dismiss();
					if (touristsDensityData.equals("failure")) {
						showDialog(TOURIST_DENSITY_DIALOG);
					} else {
						// ������������������json���ݸ�ʽ������·����Ϣ��
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
							Log.d(TAG, "����·�߽�������");
							e.printStackTrace();
						}
						hotZoneOverLay touristsDensityOverlay = new hotZoneOverLay(
								MyMapActivity.this.allTouristsGPs);
						List<Overlay> ol = MyMapActivity.this.mapView
								.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
						ol.add(touristsDensityOverlay);
						MyMapActivity.this.mapView.invalidate();
					}
					break;
				}

			}

		};

		// �����ͼ�����ö�ʱ����ʱ����λ����Ϣ
		this.getMyRouteHandler = new Handler() {
			String jsonTouristRoute = "";

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1100:
					switch (msg.arg1) {
					case 1:// ������Ϣ�ɹ�
						jsonTouristRoute = (String) msg.obj;
						break;
					case 2:
						jsonTouristRoute = "failure";
						Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨻�ȡ����·����Ϣ��");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "����·��handler���ִ���");
					}

					MyMapActivity.this.waitingForMyrouteDlg.dismiss();
					
					if (jsonTouristRoute.equals("failure")) {
						showDialog(TOURIST_ROUTE_ERROR);
					} else {
						// ������������������json���ݸ�ʽ������·����Ϣ��
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
							Log.d(TAG, "����·�߽�������");
							e.printStackTrace();
						}
						FootStepOverLay footOverlay = new FootStepOverLay(
								MyMapActivity.this.MyFootstepsFromServer);
						List<Overlay> ol = MyMapActivity.this.mapView
								.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
						ol.add(footOverlay);
						MyMapActivity.this.mapView.invalidate();

					}
					break;
				}
				if (MyMapActivity.this.MyFootsteps.size() > 1) {
					MyMapActivity.this.MyFootsteps.remove(0);
				}
				// ��onCreate�����б�ĵ�ǰλ����Ϣ
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
					case 1:// ������Ϣ�ɹ�
						Log.d(TAG, "������Ϣ�ɹ�");
						LocationUtils.addAnimationToMap(
								MyMapActivity.this.mapView,
								R.drawable.location_anim,
								MyMapActivity.this.MyFootsteps.get(0));

						MyMapActivity.this.mapView.invalidate();
						
						break;
					case 2:// �û���������������Ӧ
						Log.d(TAG, "������Ϣ�������ӵ����������������˿��ܳ�������");
						break;
					case 3:
						Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨶�ʱ����λ����Ϣ��");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "����λ����Ϣ���󣡶�ʱ�����صĽ���Ǵ����");

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
				// ��onCreate�����б�ĵ�ǰλ����Ϣ
				LocationUtils.addAnimationToMap(MyMapActivity.this.mapView,
						R.drawable.location_anim,
						MyMapActivity.this.MyFootsteps.get(0));
				MyMapActivity.this.mapView.invalidate();
				super.handleMessage(msg);
			}
		};

		Timer timer = new Timer();
		// ÿ���ӷ���һ�ε���λ����Ϣ
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
					Log.d(TAG,"�õ�����ϢΪ��"+receievedMsg);
					if (isSuccess) {// ���������ܳɹ�
						send_status_code = 1;
					} else {// ����������ʧ��
						send_status_code = 2;
					}

				} catch (IOException e) {
					// �������Ӵ���
					send_status_code = 3;
					Log.d(TAG, "�������Ӵ���");
					e.printStackTrace();
				} catch (Exception e) {
					Log.d(TAG, "mapActivity�е�δ֪����");
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
					case 1:// ������Ϣ�ɹ�
						Log.d(TAG, "�ɹ���ú����б��json��ʽ����");
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
					case 2:// �����б�״̬��2
						Log.d(TAG, "�����б�Ϊ�գ���������Ӻ���");
						String noFriends = "nofriends";

						intentToFriendLocation = new Intent(MyMapActivity.this,
								friendLocationActivity.class);
						intentToFriendLocation
								.putExtra("friendlist", noFriends);
						MyMapActivity.this.startActivityForResult(
								intentToFriendLocation, 100);

						break;
					case 3:
						Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨺����б�����");
						Toast.makeText(
								MyMapActivity.this,
								MyMapActivity.this.getResources().getString(
										R.string.network_error),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Log.d(TAG, "���ͺ����б�������ˣ�δ֪���쳣");
					}

					break;
				}
				super.handleMessage(msg);
			}
		};

		// ��ʾ����ģʽ�ĶԻ���

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
						.setTitle("��ʾ")
						.setMessage("�Ƿ���λ����Ϣ�����Ԯ")
						.setPositiveButton("ȷ��",
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
											// ����ʱ�������Ӵ���
											Log.d(TAG, "������Ϣ���ʹ���");
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
													.setTitle("��ʾ")
													.setMessage("����ź��ѷ�����")
													.setPositiveButton(
															"ȷ��",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {

																}
															}).create();
											dlg.show();
										} else {
											// ������Ϣδ�ܳɹ�������ʾ�Ի���
											AlertDialog failuredlg = new AlertDialog.Builder(
													MyMapActivity.this)
													.setTitle("��ʾ")
													.setMessage(
															"������Ϣ����ʧ�ܣ����Ժ����³���")
													.setPositiveButton(
															"ȷ��",
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
						.setNegativeButton("ȡ��",
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

		// ����ͼ����λ�ô���
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

		// ��λ����Ĵ����д��
		this.locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		this.locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 1000, 0,
				new UserLocationListener());

		// ��Ӿ����ͼ��
		// -----------------����OverlayItem����----------------
		// �������б�־������

		testSpots = new ArrayList<GeoPoint>();
		toilets = new ArrayList<GeoPoint>();
		restpoints = new ArrayList<GeoPoint>();
		restaurants = new ArrayList<GeoPoint>();
		hotels = new ArrayList<GeoPoint>();

		// ����λ��
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
		// ���ӷ���
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
		OverlayItem o1 = new OverlayItem(jinsuoguanGP, "spot", "������");
		OverlayItem o2 = new OverlayItem(qianChiGP, "spot", "ǧ�ߴ�");
		OverlayItem o3 = new OverlayItem(xifendingGP, "spot", "���嶥");
		OverlayItem o4 = new OverlayItem(wuLiGuanGP, "spot", "�����");
		OverlayItem o5 = new OverlayItem(dongfengGP, "spot", "����");
		OverlayItem o6 = new OverlayItem(changkongzhandaoGP, "spot", "����ջ��");
		OverlayItem o7 = new OverlayItem(yaozifanshengGP, "spot", "���ӷ���");
		OverlayItem o8 = new OverlayItem(shiMenGP, "spot", "ʯ��");
		OverlayItem o9 = new OverlayItem(huiXinShiGP, "spot", "����ʯ");
		OverlayItem o10 = new OverlayItem(beiFengGP, "spot", "����");

		OverlayItem o11 = new OverlayItem(cangLongGP, "spot", "������");
		OverlayItem o12 = new OverlayItem(wuYunFengGP, "spot", "���Ʒ�");
		OverlayItem o13 = new OverlayItem(zhongFengGP, "spot", "�з�");
		OverlayItem o14 = new OverlayItem(nanFengGP, "spot", "�Ϸ�");
		OverlayItem o15 = new OverlayItem(sheShengYaGP, "spot", "������");
		OverlayItem o16 = new OverlayItem(maoNvDongGP, "spot", "ëŮ��");

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
		o1 = new OverlayItem(t1GP, "toilet", "������1");
		o2 = new OverlayItem(t2GP, "toilet", "������2");
		o3 = new OverlayItem(t3GP, "toilet", "������3");
		o4 = new OverlayItem(t4GP, "toilet", "������4");
		o5 = new OverlayItem(t5GP, "toilet", "������5");
		o6 = new OverlayItem(t6GP, "toilet", "������6");

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

		o1 = new OverlayItem(re1GP, "restaurant", "������ӹ�");
		o2 = new OverlayItem(re2GP, "restaurant", "�������");
		o3 = new OverlayItem(re3GP, "restaurant", "��������");
		o4 = new OverlayItem(re4GP, "restaurant", "�����");
		o5 = new OverlayItem(re5GP, "restaurant", "���׼���������");
		o6 = new OverlayItem(re6GP, "restaurant", "������ׯ");

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
		o1 = new OverlayItem(ht1GP, "hotel", "����ɽׯ");
		o2 = new OverlayItem(ht2GP, "hotel", "�������");
		o3 = new OverlayItem(ht3GP, "hotel", "�������磨���ƹ���");

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
		o1 = new OverlayItem(rp1GP, "restpoint", "��Ϣ��1");
		o2 = new OverlayItem(rp2GP, "restpoint", "��Ϣ��2");
		o3 = new OverlayItem(rp3GP, "restpoint", "��Ϣ��3");

		o4 = new OverlayItem(rp1GP, "restpoint", "��Ϣ��4");
		o5 = new OverlayItem(rp2GP, "restpoint", "��Ϣ��5");
		o6 = new OverlayItem(rp3GP, "restpoint", "��Ϣ��6");
		this.restpointLists.add(o1);
		this.restpointLists.add(o2);
		this.restpointLists.add(o3);
		this.restpointLists.add(o4);
		this.restpointLists.add(o5);
		this.restpointLists.add(o6);
		// -----------------��ȡͼ��M-----------------------------
		Drawable drawable = getApplicationContext().getResources().getDrawable(
				R.drawable.spot);
		// -----------------new ShowOverlay����-------------------
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

		// ------------------��ͼ����ӵ�MapView---------------

		center_geoPoint = new GeoPoint((int) (dLat * 1E6), (int) (dLong * 1E6));
		// ��ɽ��ͼ���½�����
		lt_geoPoint = new GeoPoint((int) (dltLat * 1E6), (int) (dltLong * 1E6));
		// ��ɽ��ͼ���½�����
		rb_geoPoint = new GeoPoint((int) (drbLat * 1E6), (int) (drbLong * 1E6));
		mc.setCenter(center_geoPoint);
		mc.setZoom(14);
		this.setModeBtn = (Button) findViewById(R.id.set);

		this.homeBtn.setOnClickListener(new ResetMapViewBtnClickListener());
		this.backToCenterBtn
				.setOnClickListener(new BackToCenterOnclickListener());
		// this.setModeBtn.setOnClickListener(new SetBtnClickListener());
		this.mapView.displayZoomControls(true); // ������ʾ�Ŵ���С��ť
		this.mc.animateTo(center_geoPoint); // ����ͼ�ƶ���ָ���ĵ���λ��
		List<Overlay> ol = this.mapView.getOverlays(); // ���MapView��

		ol.clear();

		ol.add(new HuaShanOverlay(this.bmpmap, lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay

		this.hotZone = new ArrayList<GeoPoint>();

		// �ο��ܶȵ����ݣ������ݲ���
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

			// ��ʾ��ѡ��ť�Ի���
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
		case RED_MENU_ID0:// �����б�˵�ѡ��

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
		case MODE_CHANGE:// ����ģʽ�л��˵�����Ӧ
			SharedPreferences pre = MyMapActivity.this.getSharedPreferences(
					"xxy", Context.MODE_PRIVATE);
			// ��ȡ�������ļ��е�����ģʽֵ
			Boolean isIntelMode = pre.getBoolean("inteligentMode", false);
			if (isIntelMode) {// ���������ģʽ�����л�Ϊ������ģʽ
				Editor editor = pre.edit();
				editor.putBoolean("inteligentMode", false);
				editor.commit();
				this.locationManager.removeProximityAlert(this.pi);
				Toast.makeText(this, "����ģʽ��ȡ��", Toast.LENGTH_SHORT).show();
				NotificationManager notificationManager = (NotificationManager) MyMapActivity.this
						.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
				notificationManager.cancel(0);
			} else {// �����������ģʽ���л�Ϊ����ģʽ
				Editor editor = pre.edit();
				editor.putBoolean("inteligentMode", true);
				editor.commit();

				double Lati7 = 34.504823724647000;
				double Longti7 = 110.075599551200000;
				float radius = 50;
				Intent intent2 = new Intent(this, ProximityAlertReciever.class);
				// ��Intent��װ��PendingIntent

				pi = PendingIntent.getBroadcast(this, -1, intent2, 0);
				// ����ٽ�����
				this.locationManager.addProximityAlert(Lati7, Longti7, radius,
						-1, pi);
				Toast.makeText(this, "����ģʽ�л��ɹ�", Toast.LENGTH_SHORT).show();
				this.showNotification();
			}
			return true;
		case WEATHER_INQUIRE1:// ����Ԥ����Activity
			Intent intentToWeatherActivity = new Intent();
			intentToWeatherActivity.setClass(this,
					com.weather.my.WeatherReport.class);
			this.startActivity(intentToWeatherActivity);

			return true;
		case HELPER:
			return true;
		case ACCOUNT_MANEGER:// �˻�����˵�
			Intent ToAccountManageActivity = new Intent();
			ToAccountManageActivity.setClass(this, accountManageActivity.class);
			this.startActivity(ToAccountManageActivity);
			return true;
		case FEEDBACK:// �����˵����
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
			t.setText("��������");
			Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(toastView);
			toast.setGravity(Gravity.TOP | Gravity.LEFT, 275, 465);
			toast.show();
			MapController ct = MyMapActivity.this.mapView.getController();
			ct.setZoom(14);
			// ���ص�ͼ����
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
					"��λ����" + String.valueOf(loc.getLatitude()) + " "
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
		Log.d("������", "������" + requestCode);
		Log.d("�����", "�����" + resultCode);
		// ���Ѷ�λActivity���ص�
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
						"friend Location", "���˵��");
				ArrayList<OverlayItem> friendLocLists = new ArrayList<OverlayItem>();
				friendLocLists.add(friendOverlayItem);

				Drawable drawable = getApplicationContext().getResources()
						.getDrawable(R.drawable.friendloc);
				// -----------------new ShowOverlay����-------------------
				ShowOverlay friendShowOverlay = new ShowOverlay(drawable,
						friendLocLists, this);
				List<Overlay> ol = this.mapView.getOverlays(); // ���MapView��

				ol.clear();

				ol.add(new HuaShanOverlay(this.bmpmap, lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
				ol.add(friendShowOverlay);
			} else {
				// Toast.makeText(this, "is not Clicked",
				// Toast.LENGTH_SHORT).show();
			}
			// ����ҳ�淵�ص�
		}// ����ҳ�淵�ص�
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

			Log.d("���⾰����Ŀ", "���ⲿ�ֵ�ͼ����Ŀ��" + themeOverlays.size());
			Drawable themeDrawable = getApplicationContext().getResources()
					.getDrawable(R.drawable.theme);
			ShowOverlay themeShow = new ShowOverlay(themeDrawable,
					themeOverlays, this);
			List<Overlay> ol = this.mapView.getOverlays(); // ���MapView��
			ol.clear();

			ol.add(new HuaShanOverlay(this.bmpmap, lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay

			ol.add(themeShow);
			this.mapView.invalidate();

		}

		super.onActivityResult(requestCode, resultCode, intent);

	}

	@SuppressLint({ "ParserError", "ParserError" })
	protected Dialog onCreateDialog(int id) { // ��дonCreateDialog����
		Dialog dialog = null; // ����һ��Dialog�������ڷ���
		switch (id) { // ��id�����ж�
		case exit_dialog:
			Builder b2 = new AlertDialog.Builder(this); // ����Builder����
			b2.setMessage("ȷ���˳���");
			b2.setTitle("��ʾ"); // ���ñ���
			b2.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dataStorage.is_login = false;
					dataStorage.user_name = "anonymous";
					dialog.dismiss();
					MyMapActivity.this.finish();
				}
			});
			b2.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			dialog = b2.create(); // ����Dialog����
			break;

		case LIST_DIALOG_SINGLE:

			Builder b = new AlertDialog.Builder(this); // ����Builder����
			b.setIcon(R.drawable.header); // ����ͼ��
			b.setTitle(R.string.title); // ���ñ���
			b.setSingleChoiceItems( // ���õ�ѡ�б�ѡ��
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
								Log.d(TAG, "·�߹滮ʱ�������");
							}
						}
					});
			b.setPositiveButton(R.string.ok, // ��ť��ʾ���ı�
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "��ѡ��ľ�����Ϊ:"
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
			dialog = b.create(); // ����Dialog����
			break;

		case WAITING_FOR_ROUTE:
			Log.d(TAG, "��ʾ��ȡ·�߹滮�������Ի���");

			this.waitingRouteCalcDlg = new ProgressDialog(this);
			this.waitingRouteCalcDlg.setMessage("����Ϊ����ȡ·�߹滮��Ϣ...");
			this.waitingRouteCalcDlg.setTitle("��ȴ� ");
			this.waitingRouteCalcDlg.setCancelable(true);
			this.waitingRouteCalcDlg.show();
			new Thread(new getRouteCalcThread()).start();
			break;
		case WAITIN_FOR_MYROUTE_DLG:

			Log.d(TAG, "��ʾ��ȡ����·�߽������Ի���");

			this.waitingForMyrouteDlg = new ProgressDialog(this);
			this.waitingForMyrouteDlg.setMessage("����Ϊ����ȡ����·����Ϣ...");
			this.waitingForMyrouteDlg.setTitle("��ȴ� ");
			this.waitingForMyrouteDlg.setCancelable(true);
			this.waitingForMyrouteDlg.show();
			new Thread(new getMyRouteThread()).start();
			break;

		case WAITIN_FOR_ALL_THE_TOURISTS_DLG:

			Log.d(TAG, "��ʾ��ȡ����·�߽������Ի���");
			this.waitingForAllTouristsDlg = new ProgressDialog(this);
			this.waitingForAllTouristsDlg.setMessage("����Ϊ����ȡ�ο��ܶ���Ϣ��Ϣ...");
			this.waitingForAllTouristsDlg.setTitle("��ȴ� ");
			this.waitingForAllTouristsDlg.setCancelable(true);
			this.waitingForAllTouristsDlg.show();
			new Thread(new getAllTouristsThread()).start();
			break;
		// case MYROUTE_ERROR_DLG:
		// Log.d(TAG, "��ʾ��ȡ����·�ߴ���Ի���");
		// Builder getMyRouteErrorBuilder = new AlertDialog.Builder(this); //
		// ����Builder����
		// getMyRouteErrorBuilder.setTitle("��ʾ");
		// getMyRouteErrorBuilder.setMessage("���������쳣���޷��������·����Ϣ�����Ժ���");
		// getMyRouteErrorBuilder.setPositiveButton("ȷ��",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// }
		// });
		// dialog = getMyRouteErrorBuilder.create(); // ����Dialog����
		// break;
		case LIST_MOOD_SELECT:
			Builder b1 = new AlertDialog.Builder(this);
			b1.setIcon(R.drawable.setting_press);
			b1.setTitle("���ѡ��");

			BaseAdapter adapter = new ListItemAdapter();
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int which) {
					List<Overlay> ol;
					switch (which) {
					case 0:
						Toast.makeText(MyMapActivity.this, "��ѡ����ʾ�����߹�������·�ߣ�", 0)
								.show();
						if(dataStorage.is_login){
							showDialog(WAITIN_FOR_MYROUTE_DLG);
						}else{
							showDialog(PLEASE_LOGIN_DIALOG);
						}
						break;

					case 1:
						Toast.makeText(MyMapActivity.this, "��ѡ����ʾ��ͼ�����о�����", 0)
								.show();
						ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
						ol.add(sol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 2:
						Toast.makeText(MyMapActivity.this, "��ѡ����ʾ��ͼ�������������ǣ�",
								0).show();
						ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
						ol.add(MyMapActivity.this.tolitSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 3:
						Toast.makeText(MyMapActivity.this, "��ѡ����ʾ��ͼ��������Ϣ���ǣ�",
								0).show();
						ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
						ol.add(MyMapActivity.this.restpointSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 4:
						Toast.makeText(MyMapActivity.this, "��ѡ����ʾ��ͼ�����в�����ǣ�", 0)
								.show();
						ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay

						ol.add(MyMapActivity.this.restaruantSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					case 5:
						Toast.makeText(MyMapActivity.this, "��ѡ����ʾ��ͼ�����б��ݱ�ǣ�", 0)
								.show();
						ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
						ol.clear();
						ol.add(new HuaShanOverlay(MyMapActivity.this.bmpmap,
								lt_geoPoint, rb_geoPoint)); // ���һ���µ�Overlay
						ol.add(MyMapActivity.this.hotelSol);
						MyMapActivity.this.mapView.invalidate();
						break;

					default:
						System.out.println("���ѡ�񴦴���������");
						break;
					}
				}
			};
			b1.setAdapter(adapter, listener);
			dialog = b1.create();
			break;

		case WAITING_FOR_FRIENDLIST:
			Log.d(TAG, "��ʾ��ȡ�����б�ȴ��Ի���");
			// �������۲�����ʾ�������Ի���

			MyMapActivity.this.waitingForFriendListProDlg = new ProgressDialog(
					this);
			MyMapActivity.this.waitingForFriendListProDlg
					.setMessage("����Ϊ����ȡ�����б�...");
			MyMapActivity.this.waitingForFriendListProDlg.setTitle("��ȴ� ");
			MyMapActivity.this.waitingForFriendListProDlg.setCancelable(true);
			MyMapActivity.this.waitingForFriendListProDlg.show();
			new Thread(new getFriendListThread()).start();
			break;

		case GET_ROUTE_ERROR:
			Log.d(TAG, "��ȡ·�߹滮����");
			Builder getRouteErrorBuilder = new AlertDialog.Builder(this); // ����Builder����
			getRouteErrorBuilder.setTitle("��ʾ");
			getRouteErrorBuilder.setMessage("���������쳣���޷�������·�ߣ����Ժ���");
			getRouteErrorBuilder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = getRouteErrorBuilder.create(); // ����Dialog����
			break;
		case PLEASE_LOGIN_DIALOG:
			Builder pleaseLoginDlgBuilder = new AlertDialog.Builder(this); // ����Builder����
			pleaseLoginDlgBuilder.setTitle("��ʾ");
			pleaseLoginDlgBuilder.setMessage("�Բ�������δ��¼");
			pleaseLoginDlgBuilder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = pleaseLoginDlgBuilder.create(); // ����Dialog����
			break;
		case TOURIST_DENSITY_DIALOG:
			Builder touristDensityErrorBuilder = new AlertDialog.Builder(this); // ����Builder����
			touristDensityErrorBuilder.setMessage("��ȡ�ο��ܶȴ������Ժ���");
			touristDensityErrorBuilder.setTitle("��ʾ");
			touristDensityErrorBuilder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = touristDensityErrorBuilder.create(); // ����Dialog����
			break;
		case TOURIST_ROUTE_ERROR:
			Builder touristRouteErrorBuilder = new AlertDialog.Builder(this); // ����Builder����
			touristRouteErrorBuilder.setTitle("��ʾ");
			touristRouteErrorBuilder.setMessage("��ȡ����·�ߴ������Ժ���");
			touristRouteErrorBuilder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog = touristRouteErrorBuilder.create(); // ����Dialog����
			break;
		case inteligent_dialog:
			Builder builder = new AlertDialog.Builder(this); // ����Builder����
			builder.setMessage("���Ƿ�ѡ�����ܵ���ģʽ��");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��",
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
							// ��Intent��װ��PendingIntent
							MyMapActivity.this.pi = PendingIntent.getBroadcast(
									MyMapActivity.this, -1, intent, 0);
							// ����ٽ�����
							MyMapActivity.this.locationManager
									.addProximityAlert(Lati7, Longti7, radius,
											-1, MyMapActivity.this.pi);
							System.out.println("�û�ѡ������ģʽ���ٽ������Ѿ��򿪣�");

							MyMapActivity.this.showNotification();
						}
					});
			builder.setNegativeButton("ȡ��",
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

			dialog = builder.create(); // ����Dialog����
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
			// ���array.xml�е�������ԴgetStringArray���ص���һ��String����
			String text = getResources().getStringArray(R.array.moodarray)[position];
			textView.setText(text);
			// ���������С
			textView.setTextSize(24);
			AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(layoutParams);
			// ����ˮƽ�����Ͼ���
			textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
			textView.setMinHeight(60);
			// ����������ɫ
			textView.setTextColor(Color.BLACK);
			// ����ͼ�������ֵ����
			textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position],
					0, 0, 0);
			// ����textView���������µ�padding��С
			textView.setPadding(30, 0, 15, 0);
			// �������ֺ�ͼ��֮���padding��С
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
		// ����һ��NotificationManager������
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		// ����Notification�ĸ�������
		Notification notification = new Notification(R.drawable.notification,
				"����ģʽ�Ѵ�", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT; // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;  
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000;

		// ����֪ͨ���¼���Ϣ
		CharSequence contentTitle = "��ң��"; // ֪ͨ������
		CharSequence contentText = "��ң��������������ģʽ�Ѵ�"; // ֪ͨ������
		Intent notificationIntent = new Intent(this, MyMapActivity.class); // �����֪ͨ��Ҫ��ת��Activity
		PendingIntent contentItent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(this, contentTitle, contentText,
				contentItent);

		// ��Notification���ݸ�NotificationManager
		notificationManager.notify(0, notification);
	}

	// �����ͼ�϶����ͼ��
	class ResetMapViewBtnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			View toastView = getLayoutInflater().inflate(
					com.work.map.R.layout.map_eraser_toast, null);
			TextView t = (TextView) toastView
					.findViewById(com.work.map.R.id.map_eraser);
			t.setText("����ͼ���Ѳ���");
			Toast toast = new Toast(getApplicationContext());
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(toastView);
			toast.setGravity(Gravity.TOP | Gravity.LEFT, 160, 110);
			toast.show();
			List<Overlay> ol = MyMapActivity.this.mapView.getOverlays(); // ���MapView��
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
					// ����ʧ�ܣ������б�Ϊ�գ�����Ӻ���
					friend_list_status_code = 2;
				} else {
					// �����б��ͳɹ����õ���json��ʽ������
					// JSONTODO
					friend_list_status_code = 1;
				}

			} catch (Exception e) {
				// �������Ӵ���
				this.friend_list_status_code = 3;
				Log.d(TAG, "�������Ӵ���");
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
				Log.d(TAG, "���ӷ�������ʱ���������Ӵ��󣨵���ο��ܶȵ�ʱ��");
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
				Log.d(TAG, "·�߹滮�������");
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
				// �������Ӵ���
				this.my_Route_status_code = 2;
				Log.d(TAG, "�������Ӵ���");
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
		
		// ����һ��NotificationManager������
				NotificationManager notificationManager = (NotificationManager) this
						.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

				// ����Notification�ĸ�������
				Notification notification = new Notification(R.drawable.notification,
						"����Ա��Ϣ", System.currentTimeMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT; // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
				// notification.flags |= Notification.FLAG_NO_CLEAR; //
				//notification.defaults = Notification.DEFAULT_LIGHTS;
				notification.ledARGB = Color.BLUE;
				notification.ledOnMS = 5000;
				notification.defaults = Notification.DEFAULT_SOUND;  
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				
				// ����֪ͨ���¼���Ϣ
				CharSequence contentTitle = "Ԥ����Ϣ"; // ֪ͨ������
				CharSequence contentText = s; // ֪ͨ������
				Intent notificationIntent = new Intent(this, NotificationActivity.class); // �����֪ͨ��Ҫ��ת��Activity
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				notificationIntent.addFlags(Intent.FILL_IN_DATA);
				notificationIntent.putExtra("msg", s);
				PendingIntent contentItent = PendingIntent.getActivity(this, 0,
						notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				Log.d(TAG, "��֪ͨ����Activity��������"+s);
				notification.setLatestEventInfo(this, contentTitle, contentText,
						contentItent);	
				// ��Notification���ݸ�NotificationManager
				notificationManager.notify(1, notification);
	}
	
	
}

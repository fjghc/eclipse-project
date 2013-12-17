package com.weather.my;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.my.ForecastUtil;
import com.weather.my.ListDataProvider;
import com.weather.my.ConstData;
import com.weather.my.GoogleWeatherHandler;
import com.weather.my.WeatherCurrentCondition;
import com.weather.my.WeatherSet;
import com.work.map.LocationUtils;
import com.work.map.MyMapActivity;
import com.work.map.R;
import com.xxl.network.GetInfoFromServer;
import com.xxl.utility.dataStorage;

/**
 * @description
 * @version 1.0
 */
public class WeatherReport extends ListActivity{

	private ListDataProvider provider = null;
	
	private static List<Map<String,String>> dataSource = new ArrayList<Map<String,String>>();
	
	private String cityVal;
	
	private Handler weatherHandler;
	
	private WeatherSet ws; 
	
	private static final String TAG = "WeatherReportActivity";
	
	private static final int WEATHERREPORT = 100;
	
	private ProgressDialog waitingForWeatherDlg;
	
	private static final int WAITINGFROWEATHER = 1;

	protected static final int WEATHER_ERROR_DLG = 0;
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id){
		case WAITINGFROWEATHER:
			Log.d(TAG, "��ʾ��ȡ������Ϣ�������Ի���");
			// �������۲�����ʾ�������Ի���
			
			this.waitingForWeatherDlg = new ProgressDialog(this);
			this.waitingForWeatherDlg.setMessage("����Ϊ����ȡ������Ϣ...");
			this.waitingForWeatherDlg.setTitle("��ȴ� ");
			this.waitingForWeatherDlg.setCancelable(true);
			this.waitingForWeatherDlg.show();
			
			new Thread(new getWeatherReportThread()).start();
			break;
		case WEATHER_ERROR_DLG:
			Log.d(TAG, "��ʾ��ȡ������Ϣ����Ի���");
			
			Builder getWeatherErrorBuilder = new AlertDialog.Builder(this); // ����Builder����
			getWeatherErrorBuilder.setTitle("��ʾ");
			getWeatherErrorBuilder.setMessage("���������쳣���޷����������Ϣ�����Ժ���");
			getWeatherErrorBuilder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							WeatherReport.this.finish();
						}
					});
			dialog = getWeatherErrorBuilder.create(); // ����Dialog����
			break;
		}
		return dialog;
	}

	@SuppressLint("ParserError")
	class getWeatherReportThread implements Runnable {
		private int weather_report_status_code = -1;

		public void run() {
			try {
				getData();
				weather_report_status_code = 1;
			} catch (Exception e) {
				// �������Ӵ���
				weather_report_status_code = 2;
				Log.d(TAG, "����Ԥ�����ӻ�������������");
				e.printStackTrace();
			}
			Message message = new Message();
			message.what = WEATHERREPORT;
			message.arg1 = weather_report_status_code;
			WeatherReport.this.weatherHandler.sendMessage(message);
		}
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.work.map.R.layout.weather);
		ws = new WeatherSet();
		this.showDialog(WAITINGFROWEATHER);
		WeatherReport.this.weatherHandler = new Handler() {
			@SuppressLint({ "ParserError", "HandlerLeak" })
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case WEATHERREPORT:

					waitingForWeatherDlg.dismiss();
					switch (msg.arg1) {
					case 1:// ������Ϣ�ɹ�
						/** ��ǰ���� */
						try {
								updateWeatherInfoView(ws.getMyCurrentCondition());
								System.out.println(2);
								provider = new ListDataProvider(WeatherReport.this);
								provider.loadData(ws.getMyForecastConditions());
								dataSource = provider.getDataSource();
								ListAdapter adapter = new SimpleAdapter(WeatherReport.this, dataSource, com.work.map.R.layout.weather_items,
						        		new String[]{"icon","day","condition","temp"}, new int[]{com.work.map.R.id.dDetailImage,com.work.map.R.id.ddDayText,com.work.map.R.id.ddConditionText,com.work.map.R.id.ddTempCText});
						        setListAdapter(adapter);

						} catch (Exception e1) {
							Log.d(TAG, "�������ݸ�ʽ����");
							e1.printStackTrace();
						}

						break;
					case 2:// �û���������������Ӧ
						Log.d(TAG, "�õ�����������weatherHandler��");
						showDialog(WEATHER_ERROR_DLG);
						break;
					
					default:
						Log.d(TAG, "δ֪�ش����100000");
					}

					break;
				}
			}
		};
	}
	
	/**
	 * �õ�����
	 */
	
	private void getData() throws Exception
	{
		cityVal = "��ɽ";
		int index = 1;
		//localService.close();
		/** ��ѯ��ǰ���ж�Ӧ��ָ�� */
		for(int i = 0 ; i < ConstData.city.length; i++)
		{
			if(cityVal.equals(ConstData.city[i]))
			{
				index = i;
				break;
			}
		}
		String cityCodeVal = ConstData.cityCode[index];

			URL url = new URL(ConstData.queryString + cityCodeVal);
		    System.out.println(ConstData.queryString + cityCodeVal);
			getCityWeather(url);

	}
	
	/**
	 * ͨ��URL��ȡ������Ϣ ������
	 * @param url
	 * @throws InterruptedException 
	 */
	public void getCityWeather(URL url) throws IOException, InterruptedException
	{
		System.out.println("getCityWeather");

		try {
			System.out.println("getCityWeather--try");

			SAXParserFactory sf = SAXParserFactory.newInstance();
			SAXParser sp = sf.newSAXParser();
			XMLReader xreader = sp.getXMLReader();
			System.out.println(0);

			GoogleWeatherHandler gwh = new GoogleWeatherHandler();
			xreader.setContentHandler(gwh);
			
		
			InputStreamReader isr = new InputStreamReader(url.openStream(), "GBK");
			InputSource is = new InputSource(isr);
			
			xreader.parse(is);
			ws = gwh.getMyWeatherSet();
			
		} 
		catch (ParserConfigurationException e) 
		{
			Log.d(TAG, "ParserConfigurationException");
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			Log.d(TAG, "SAXException");
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) 
		{
			Log.d(TAG, "UnsupportedEncodingException");
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * ��ʾ��ǰ����
	 * @param aResourceID
	 * @param aWCC
	 * @throws MalformedURLException
	 */
	private void updateWeatherInfoView(WeatherCurrentCondition wc) throws MalformedURLException
	{
		System.out.println("updateWeatherInfoView" + cityVal);
		ImageView forecastImage = (ImageView) findViewById(com.work.map.R.id.dForecastImage);
		TextView cityText = (TextView) findViewById(com.work.map.R.id.dCityText);
		TextView conditionText = (TextView) findViewById(com.work.map.R.id.dConditionText);
		TextView humidityText = (TextView) findViewById(com.work.map.R.id.dHumidityText);
		TextView windText = (TextView) findViewById(com.work.map.R.id.dWindText);
		TextView tempcText = (TextView) findViewById(com.work.map.R.id.dTempCText);
		forecastImage.setImageResource(ForecastUtil.getForecastImage(wc.getIcon()));
		cityText.setText(cityVal);
		conditionText.setText(wc.getCondition());
		humidityText.setText(wc.getHumidity());
		windText.setText(wc.getWind_condition());
		tempcText.setText(wc.getTemp_c() + "��");
		/**
		 * ���ö���
		 */
		updateAnimtation(wc.getIcon());
	}
	
	/**
	 * ����Ч��
	 * @param iconDescription
	 */
	@SuppressWarnings("deprecation")
	private void updateAnimtation(String iconDescription) {
		
		/**
		 * ��ȡ����ǰ����״����ͼ���ID
		 */
		int icon = ForecastUtil.getCurrentForecastIcon(iconDescription);
		/**
		 * ��ȡͼ����ʾ�ؼ�
		 */
		ImageView currentIcon = (ImageView)findViewById(com.work.map.R.id.dForecastImage);
		
		/**
		 * ����͸�����ƶ���
		 */
		Animation curIconAnim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.rotatecurrentweather);
		currentIcon.setAnimation(curIconAnim);
		
		/**
		 * ���������ִ�����¶���
		 */
		if (icon == com.work.map.R.drawable.weather_cloudy){
			AbsoluteLayout absLayout = (AbsoluteLayout)findViewById(com.work.map.R.id.imagesLayout);
			ImageView cloud01 = new ImageView(this);
			ImageView cloud02 = new ImageView(this);
			
			/**
			 * ����Ϊtrue����ʾ�ؼ�������ͼ����Դ�Ŀ�߱���
			 */
			cloud01.setAdjustViewBounds(true);
			cloud02.setAdjustViewBounds(true);
			cloud01.setImageResource(com.work.map.R.drawable.layer_cloud1);
			cloud02.setImageResource(com.work.map.R.drawable.layer_cloud2);
			
			/**
			 * ֻ�е�����setAdjustViewBounds(true)��ʱ��
			 *  1) set adjustViewBounds to true 
			 *  2) set maxWidth and maxHeight to �ɱ��� 
			 *  3) set the height and width layout params to WRAP_CONTENT.
			 */
			cloud01.setMaxHeight(48);
			/**
			 * ������С�߶�
			 */
			cloud01.setMinimumHeight(48);
			cloud01.setMaxWidth(100);
			/**
			 * ������С���
			 */
			cloud01.setMinimumWidth(100);
			
			cloud02.setMaxHeight(58);
			cloud02.setMinimumHeight(58);
			cloud02.setMaxWidth(83);
			cloud02.setMinimumWidth(83);		
			/**
			 * ���Ҷ���
			 */
			Animation leftAnim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translatecloudleft);
			Animation rightAnim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translatecloudright);
			
			//leftAnim.setRepeatCount(Animation.INFINITE);
			//rightAnim.setRepeatCount(Animation.INFINITE);
			
			cloud01.setAnimation(leftAnim);
			cloud02.setAnimation(rightAnim);
			
			absLayout.addView(cloud01);
			absLayout.addView(cloud02);			
			
		}
		
		/**
		 * ���������ִ�����¶���
		 */
		if (icon == com.work.map.R.drawable.weather_rain){
			AbsoluteLayout absLayout = (AbsoluteLayout)findViewById(com.work.map.R.id.imagesLayout);
			ImageView rain01 = new ImageView(this);
			ImageView rain02 = new ImageView(this);
			ImageView rain03 = new ImageView(this);
			ImageView rain04 = new ImageView(this);
			ImageView rain05 = new ImageView(this);
			ImageView drop01 = new ImageView(this);
			ImageView drop02 = new ImageView(this);
			ImageView drop03 = new ImageView(this);
			rain01.setAdjustViewBounds(true);
			rain02.setAdjustViewBounds(true);
			rain03.setAdjustViewBounds(true);
			rain04.setAdjustViewBounds(true);
			rain05.setAdjustViewBounds(true);
			drop01.setAdjustViewBounds(true);
			drop02.setAdjustViewBounds(true);
			drop03.setAdjustViewBounds(true);
			rain01.setImageResource(com.work.map.R.drawable.rain1);
			rain02.setImageResource(com.work.map.R.drawable.rain1);
			rain03.setImageResource(com.work.map.R.drawable.rain2);
			rain04.setImageResource(com.work.map.R.drawable.rain3);
			rain05.setImageResource(com.work.map.R.drawable.rain2);
			drop01.setImageResource(com.work.map.R.drawable.layer_drop1);
			drop02.setImageResource(com.work.map.R.drawable.layer_drop5);
			drop03.setImageResource(com.work.map.R.drawable.layer_drop7);
			
			LayoutParams lp01 = new LayoutParams(18, 30, 100, 150);
			LayoutParams lp02 = new LayoutParams(16, 33, 150, 140);
			LayoutParams lp03 = new LayoutParams(19, 30, 200, 150);
			
			Animation rain01Anim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translaterain01);
			Animation rain02Anim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translaterain02);
			Animation rain03Anim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translaterain03);
			Animation rain04Anim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translaterain04);
			Animation rain05Anim = AnimationUtils.loadAnimation(this, com.work.map.R.anim.translaterain05);
						
			rain01.setAnimation(rain01Anim);
			rain02.setAnimation(rain02Anim);
			rain03.setAnimation(rain03Anim);
			rain04.setAnimation(rain04Anim);
			rain05.setAnimation(rain05Anim);
			
			absLayout.addView(rain01);
			absLayout.addView(rain02);
			absLayout.addView(rain03);
			absLayout.addView(rain04);
			absLayout.addView(rain05);
			absLayout.addView(drop01, lp01);
			absLayout.addView(drop02, lp02);
			absLayout.addView(drop03, lp03);		
		}
	}
}

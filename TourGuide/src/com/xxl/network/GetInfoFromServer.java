package com.xxl.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.Log;

import com.xxl.utility.GetServerIPAddress;
import com.xxl.utility.StreamTool;

@SuppressLint("ParserError")
public class GetInfoFromServer {

	private static String TAG = "GetInfoFromServer";
	public static String getSpotIntroFromServer(String param) throws Exception{
		String url = GetServerIPAddress.getIpAddress("ipaddress.txt");
		url = url + "spot_info.action";
		url = url +"?"+param;
		URL SpotIntroURL = new URL(url);
		
		HttpURLConnection conn = (HttpURLConnection)SpotIntroURL.openConnection();
		conn.setReadTimeout(5*1000);
		conn.setRequestMethod("GET");
		Log.d(TAG, "������ܵ������Ѿ����ͣ�");
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String json = new String(data);
		Log.d(TAG, "������������ͺ󴫵�����ϢΪ��"+json);
		return json;

	}
	
	public static String getFriendListFromServer(String param) throws Exception{
		String url = GetServerIPAddress.getIpAddress("ipaddress.txt");
		url = url + "friendlist.action";
		url = url +"?"+param;
		Log.d(TAG, param);
		URL friendListURL = new URL(url);
		
		HttpURLConnection conn = (HttpURLConnection)friendListURL.openConnection();
		conn.setReadTimeout(5*1000);
		conn.setRequestMethod("GET");
		Log.d(TAG, "�����б�������Ѿ����ͣ�");
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String json = new String(data);
		Log.d(TAG, "�����б�������ͺ󴫵�����ϢΪ��"+json);
		return json;
	}
	
	public static String getAllTouristsFromServer(String param) throws Exception{
		String url = GetServerIPAddress.getIpAddress("ipaddress.txt");
		url = url + "density.action";
		Log.d(TAG, "�����ο��ܶ�����ʱû�в���");
		URL touristDensityURL = new URL(url);
		
		HttpURLConnection conn = (HttpURLConnection)touristDensityURL.openConnection();
		conn.setReadTimeout(5*1000);
		conn.setRequestMethod("GET");
		Log.d(TAG, "�ο��ܶȵ������Ѿ����ͣ�");
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String json = new String(data);
		Log.d(TAG, "�ο��ܶ������ͺ�ͻ��˽��յ�����ϢΪ��"+json);
		return json;
	}
	
	public static String getTouristRouteFromServer(String param) throws Exception{
		String url = GetServerIPAddress.getIpAddress("ipaddress.txt");
		url = url + "tourist_route.action";
		Log.d(TAG, "��������·������ʱû�в���");
		url = url +"?"+param;
		Log.d(TAG, param);
		URL touristRouteURL = new URL(url);
		
		HttpURLConnection conn = (HttpURLConnection)touristRouteURL.openConnection();
		conn.setReadTimeout(3*1000);
		conn.setRequestMethod("GET");
		Log.d(TAG, "����·�ߵ������Ѿ����ͣ�");
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String json = new String(data);
		Log.d(TAG, "����·�������ͺ�ͻ��˽��յ�����ϢΪ��"+json);
		return json;
	}
	
	public static String getRouteFromServer(Map<String, String> params, String enc) throws Exception{
		String path = GetServerIPAddress.getIpAddress("ipaddress.txt");
		path = path + "calculate_route.action";
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//�õ�ʵ��Ķ���������
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);//���ͨ��post�ύ���ݣ�����������������������
		//Content-Type: application/x-www-form-urlencoded
		//Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		Log.d(TAG, "·�߹滮�����ͳɹ������͵����ݣ�");
		Log.d(TAG, params.get("username"));
		Log.d(TAG, params.get("currentlat"));
		Log.d(TAG, params.get("currentlng"));
		Log.d(TAG, params.get("des_spot_id"));
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String json = new String(data);
		Log.d(TAG, "·�߹滮��õĲ���Ϊ��"+json);
		return json;
	}
	
	public static String getVideoOrVoiceFromServer(String path,Map<String, String> params, String enc) throws Exception{
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//�õ�ʵ��Ķ���������
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);//���ͨ��post�ύ���ݣ�����������������������
		//Content-Type: application/x-www-form-urlencoded
		//Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		Log.d(TAG, "·�߹滮�����ͳɹ������͵����ݣ�");
		Log.d(TAG, params.get("spot_id"));
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String fileName = new String(data);
		return fileName;
	}
	
	public static String getFriendLocationFromServer(String path,Map<String, String> params, String enc) throws Exception{
		StringBuilder sb = new StringBuilder();
		Log.d(TAG, "ȡ�ú���λ�õ�URLΪ��"+path);
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//�õ�ʵ��Ķ���������
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);//���ͨ��post�ύ���ݣ�����������������������
		//Content-Type: application/x-www-form-urlencoded
		//Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		Log.d(TAG, "·�߹滮�����ͳɹ������͵����ݣ�");
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String friendLocaton = new String(data);
		Log.d(TAG, "��ú��ѵľ�γ����Ϣ"+friendLocaton);
		return friendLocaton;
	}
	
	
	public static String sendLocationToServer(Map<String, String> params, String enc) throws Exception{
		String path = GetServerIPAddress.getIpAddress("ipaddress.txt");
		path = path + "send_loc_servlet.do";
		StringBuilder sb = new StringBuilder();
		if(params!=null && !params.isEmpty()){
			for(Map.Entry<String, String> entry : params.entrySet()){
				sb.append(entry.getKey()).append('=')
					.append(URLEncoder.encode(entry.getValue(), enc)).append('&');
			}
			sb.deleteCharAt(sb.length()-1);
		}
		byte[] entitydata = sb.toString().getBytes();//�õ�ʵ��Ķ���������
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);//���ͨ��post�ύ���ݣ�����������������������
		//Content-Type: application/x-www-form-urlencoded
		//Content-Length: 38
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
		OutputStream outStream = conn.getOutputStream();
		
		outStream.write(entitydata);
		outStream.flush();
		outStream.close();
		Log.d(TAG, "λ����Ϣ���ͳɹ������͵����ݣ�");
		InputStream inStream = conn.getInputStream();
		byte[] data = StreamTool.readInputStream(inStream);
		String json = new String(data);
		Log.d(TAG, "����λ����Ϣ�󷵻ص�����Ϊ��"+json);
		return json;
	}
}

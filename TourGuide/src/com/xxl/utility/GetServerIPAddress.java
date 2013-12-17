package com.xxl.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

public class GetServerIPAddress {
	private static String IpAddress = "";
	private static final String TAG = "GetServerIPAddress";
	
	public static String getIpAddress(String fileName) {		
		try {
			File sdPath = Environment.getExternalStorageDirectory();
			String filePath = sdPath+"/" + fileName;
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			IpAddress = reader.readLine();
			Log.d(TAG, "���ip��ַ���ļ���ȡ���,ip��ַΪ��"+IpAddress);
		} catch (FileNotFoundException e) {
			Log.d(TAG, "��ȡip��ַ�ļ�����");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "��ȡ�ļ�IO����");
			e.printStackTrace();
		} 
		
		return IpAddress;
	}

}

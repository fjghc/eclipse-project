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
			Log.d(TAG, "获得ip地址的文件读取完毕,ip地址为："+IpAddress);
		} catch (FileNotFoundException e) {
			Log.d(TAG, "读取ip地址文件错误");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "读取文件IO错误");
			e.printStackTrace();
		} 
		
		return IpAddress;
	}

}

package com.xxl.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.android.maps.GeoPoint;

@SuppressLint({ "ParserError", "ParserError" })
public class GetDataFromFile {

	private final static String TAG = "getDataFromFile debug info";
	private final static String DELIM = "\t";

	public static ArrayList<GeoPoint> readTouristRoute(String filename,Context context)
			throws Exception {

		double lat = 0;
		double lon = 0;
		String slat = "";
		String slon = "";
		InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(filename) ); 
        BufferedReader bufReader = new BufferedReader(inputReader);
		ArrayList<GeoPoint> MyFootsteps = new ArrayList<GeoPoint>();
		GeoPoint gp = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));

//		BufferedReader fileIn;
//
//		fileIn = new BufferedReader(new FileReader(filename));

		String line = bufReader.readLine();

		while (line != null) {
			StringTokenizer tokenizer = new StringTokenizer(line, DELIM);
			if (tokenizer.countTokens() != 2) {
				Log.d(TAG, "文件中读取的数据格式有误");
			} else {
				slon = tokenizer.nextToken();
				slat = tokenizer.nextToken();
				lat = Double.parseDouble(slat);
				lon = Double.parseDouble(slon);
				gp = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
				MyFootsteps.add(gp);
			}
			line = bufReader.readLine();
		}
		inputReader.close();
		bufReader.close();

		Log.d(TAG, "行走路线加载完毕");
		return MyFootsteps;

	}


}

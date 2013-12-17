package com.xxl.utility;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.maps.GeoPoint;

public class RandomGeoPointGenerate {

	private double centerLat;
	private double centerLong;
	private double baseValue;
	private int num;
	private ArrayList<GeoPoint> resultList;

	public RandomGeoPointGenerate(double lat, double longti, double base, int n) {
		this.num = n;
		this.centerLat = lat;
		this.centerLong = longti;
		this.baseValue = base;
		this.resultList = new ArrayList<GeoPoint>();
	}

	public ArrayList<GeoPoint> getResult() {
		int i = 0;
		Random random = new Random(10);
		GeoPoint gpTmp;
		double tmp = 0;
		double calLat = this.centerLat;
		double calLongt = this.centerLong;
		while (i < num) {
			tmp = random.nextDouble() - 1;
			calLat += baseValue * tmp;
			tmp = random.nextDouble() - 1;
			calLongt += baseValue * tmp;
			gpTmp = new GeoPoint((int) (calLat * 1E6), (int) (calLongt * 1E6));
			this.resultList.add(gpTmp);
			i++;
			calLat = this.centerLat;
			calLongt = this.centerLong;
		}
		return this.resultList;
	}

}

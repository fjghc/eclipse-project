package com.xxl.utility;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

//游客密度图层
public class hotZoneOverLay extends Overlay {

	private ArrayList<GeoPoint> hotDots;

	public hotZoneOverLay(ArrayList<GeoPoint> geoPoints) {

		this.hotDots = geoPoints;

	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);

		if (!shadow) {
			Projection proj = mapView.getProjection();// 获得Projection对象
			Point geoPointTmp = new Point();

			Iterator<GeoPoint> itr = this.hotDots.iterator();

			Paint paint = new Paint();
			paint.setColor(Color.RED);

			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setStrokeCap(Cap.ROUND);
			paint.setStrokeWidth(4);

			while (itr.hasNext()) {
				proj.toPixels(itr.next(), geoPointTmp);
				canvas.drawPoint(geoPointTmp.x, geoPointTmp.y, paint);
			}

		}
	}

}

package com.work.map;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

//����·��ͼ��
public class FootStepOverLay extends Overlay {
	private ArrayList<GeoPoint> footSteps;

	public FootStepOverLay(ArrayList<GeoPoint> ft) {
		super();
		footSteps = new ArrayList<GeoPoint>();
		this.footSteps = ft;
	}

	// ���ظ���draw������
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setTextSize(8);
		if (!shadow) {

			Projection proj = mapView.getProjection(); // ���Projection����
			Point start = new Point();
			Point stop = new Point();

			for (int i = 0; i < this.footSteps.size() - 1; i++) {
				proj.toPixels(this.footSteps.get(i), start);
				proj.toPixels(this.footSteps.get(i + 1), stop);
				// ��ͼ�㻭���ϻ���·��
				canvas.drawLine(start.x, start.y, stop.x, stop.y, paint);
			}
		}

	}
}

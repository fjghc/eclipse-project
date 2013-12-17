package com.xxl.utility;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import com.work.map.R;

//路径规划图层类
public class WaySolutionOverlay extends Overlay {
	private ArrayList<GeoPoint> waySolutionPoints;
	private Context context;
	private Bitmap startBitmap;
	private Bitmap endBitmap;

	public WaySolutionOverlay(Bitmap start, Bitmap end,
			ArrayList<GeoPoint> wayPoints) {
		super();
		this.startBitmap = start;
		this.endBitmap = end;
		this.waySolutionPoints = wayPoints;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		paint.setColor(Color.RED);
		paint.setAntiAlias(true);
		Projection proj = mapView.getProjection(); // 获得Projection对象
		Point sp = new Point();
		Point ep = new Point();
		proj.toPixels(this.waySolutionPoints.get(0), sp);
		proj.toPixels(
				this.waySolutionPoints.get(this.waySolutionPoints.size() - 1),
				ep);
		canvas.drawBitmap(startBitmap, sp.x - 8, sp.y - 30, paint);
		canvas.drawBitmap(endBitmap, ep.x - 8, ep.y - 30, paint);
		if (!shadow) {
			Point start = new Point();
			Point stop = new Point();

			//路线绘制部分
			for (int i = 0; i < this.waySolutionPoints.size() - 1; i++) {
				proj.toPixels(this.waySolutionPoints.get(i), start);
				proj.toPixels(this.waySolutionPoints.get(i + 1), stop);
				canvas.drawLine(start.x, start.y, stop.x, stop.y, paint);
			}
		}

	}
}

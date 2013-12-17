package com.work.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

//�����ͼͼ����
public class HuaShanOverlay extends Overlay {

	private Bitmap map;
	private GeoPoint left_top_GeoPoint;
	private GeoPoint right_bottom_GeoPoint;

	public HuaShanOverlay(Bitmap bmpmap, GeoPoint ltgeoPoint,
			GeoPoint rbgeoPoint) {
		this.map = bmpmap;
		this.left_top_GeoPoint = ltgeoPoint;
		this.right_bottom_GeoPoint = rbgeoPoint;
	}

	// ���ظ���ĵ��ͼ����Ӧ����
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		return super.onTap(arg0, arg1);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {

		super.draw(canvas, mapView, shadow);
		if (!shadow) {
			Rect src;//Դ����
			RectF dst;//Ŀ�����
			src = new Rect();
			dst = new RectF();
			Projection proj = mapView.getProjection(); // ���Projection����ͶӰ����
			Point left_top_point = new Point();
			Point right_bottom_point = new Point();

			proj.toPixels(this.left_top_GeoPoint, left_top_point); // �����ǵ�������ת��Ϊ��Ļ�ϵ�����
			proj.toPixels(this.right_bottom_GeoPoint, right_bottom_point);

			int height = this.map.getHeight();
			int width = this.map.getWidth();

			src.left = 0;
			src.top = 0;
			src.bottom = height;
			src.right = width;
			dst.left = left_top_point.x;
			dst.top = left_top_point.y;
			dst.right = right_bottom_point.x;
			dst.bottom = right_bottom_point.y;

			// ����������½ǵľ�γ��
			// System.out.println(this.left_top_GeoPoint.getLatitudeE6());
			// System.out.println(this.right_bottom_GeoPoint.getLatitudeE6());

			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			//��ͼ�㻭���ϻ��Ƶ�ͼ
			canvas.drawBitmap(map, src, dst, null);
		}
	}

}

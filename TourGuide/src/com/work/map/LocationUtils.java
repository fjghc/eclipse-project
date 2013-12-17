package com.work.map;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

//好友位置动态显示类
public class LocationUtils {

	public static void addAnimationToMap(MapView map, int animationResourceId,
			GeoPoint geoPoint) {

		final ImageView view = new ImageView(map.getContext());
		view.setImageResource(animationResourceId);

		// Post to start animation because it doesn't start if start() method is
		// called in activity OnCreate method.
		view.post(new Runnable() {
			@Override
			public void run() {
				AnimationDrawable animationDrawable = (AnimationDrawable) view
						.getDrawable();
				animationDrawable.start();
			}
		});

		MapView.LayoutParams layoutParams = new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, geoPoint, 0, 0,
				MapView.LayoutParams.CENTER);
		view.setLayoutParams(layoutParams);
		view.setId(100);
		// 删除地图图层上其他的点
		map.removeAllViews();
		map.addView(view);

	}
}

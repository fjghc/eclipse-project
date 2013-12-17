package com.weather.my;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.text.format.Time;



/**
 * ForecastUtil.java Create on 2011-9-29 下午4:15:22
 * 
 * Txin记账本(txin)
 * 
 * Copyright (c) 2011 by MTA.
 * 
 * @author txin0814
 * @Email txin0814@sina.com
 * @description
 * @version 1.0
 */
@SuppressLint("NewApi")
public class ForecastUtil {
	
	/**
     * 定义每天白天的开始时间
     */
    private static final int DAYTIME_BEGIN_HOUR = 8;

    /**
     * 定义每天白天的结束时间
     */
    private static final int DAYTIME_END_HOUR = 20;
    
	/**
	 * 判断当前时间是否属于白天
     */
    public static boolean isDaytime() {
        Time time = new Time();
        time.setToNow();
        System.out.println(time.hour+"nowS");
        return (time.hour >= DAYTIME_BEGIN_HOUR && time.hour <= DAYTIME_END_HOUR);
    }

	private static final Pattern sIconStorm = Pattern.compile("(thunder|tstms)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconSnow = Pattern.compile("(snow|ice|frost|flurries)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconShower = Pattern.compile("(showers)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconSun = Pattern.compile("(sunny|breezy|clear)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconClouds = Pattern.compile("(cloud|overcast)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconPartlyCloudy = Pattern.compile("(partly_cloudy|mostly_sunny)", Pattern.CASE_INSENSITIVE);    
    private static final Pattern sIconMostCloudy = Pattern.compile("(most_cloudy)", Pattern.CASE_INSENSITIVE);    
    private static final Pattern sIconLightrain = Pattern.compile("(lightrain)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconChanceOfRain = Pattern.compile("(chance_of_rain)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconHeavyrain = Pattern.compile("(heavyrain)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconRain = Pattern.compile("(rain|storm)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconHaze = Pattern.compile("(haze)", Pattern.CASE_INSENSITIVE);
    private static final Pattern sIconFog = Pattern.compile("(fog)", Pattern.CASE_INSENSITIVE);    
    
    /**
     * 获取表示当前天气状态的图标
     * @param iconDescription  天气状态图标的描述
     * @return
     */
	public static int getForecastImage(String iconDescription){
		int icon = 0;
		boolean isDaytime = isDaytime();
		if (iconDescription == null){
        	icon = com.work.map.R.drawable.weather_sunny;
		}else if (sIconStorm.matcher(iconDescription).find()) {
            icon = isDaytime? com.work.map.R.drawable.weather_chancestorm : com.work.map.R.drawable.weather_chancestorm_n;
        } else if (sIconSnow.matcher(iconDescription).find()) {
            icon = isDaytime? com.work.map.R.drawable.weather_chancesnow : com.work.map.R.drawable.weather_chancesnow_n;
        } else if (sIconLightrain.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_lightrain;
        } else if (sIconShower.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_rain;
        } else if (sIconPartlyCloudy.matcher(iconDescription).find()) {
            icon = isDaytime ? com.work.map.R.drawable.weather_mostlysunny : com.work.map.R.drawable.weather_mostlysunny_n;
        } else if (sIconMostCloudy.matcher(iconDescription).find()) {
            icon = isDaytime ? com.work.map.R.drawable.weather_mostlycloudy : com.work.map.R.drawable.weather_mostlycloudy_n;
        } else if (sIconSun.matcher(iconDescription).find()) {
            icon = isDaytime ? com.work.map.R.drawable.weather_sunny : com.work.map.R.drawable.weather_sunny_n;
        } else if (sIconClouds.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_cloudy;
        } else if (sIconHeavyrain.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_rain;
        } else if (sIconRain.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_rain;
        } else if (sIconHaze.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_haze;
        } else if (sIconFog.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.weather_fog;
        } else if (sIconChanceOfRain.matcher(iconDescription).find()) {
            icon = isDaytime ? com.work.map.R.drawable.weather_cloudyrain : com.work.map.R.drawable.weather_cloudyrain_n;
        } else {
        	icon = com.work.map.R.drawable.weather;
        }
		
		return icon;
	}
	
	/**
	 * 获取表示天气状态的图标（用于判断是否显示动画）
	 * @param iconDescription 天气状态图标的描述
	 * @return
	 */
	public static int getCurrentForecastIcon(String iconDescription){
		int icon = 0;
		if (iconDescription == null){
			icon = com.work.map.R.drawable.weather_sunny;
		} else if (sIconClouds.matcher(iconDescription).find()){
			icon = com.work.map.R.drawable.weather_cloudy;
		} else if (sIconRain.matcher(iconDescription).find()){
			icon = com.work.map.R.drawable.weather_rain;
		}
		
		return icon;
	}
	
	/**
	 * 获取表示预报天气状态的图标
	 * @param iconDescription 天气状态图标的描述
	 * @return
	 */
	public static int getDetailForecastIcon(String iconDescription){
		int icon = 0;
		
		if (iconDescription == null) {
			icon = com.work.map.R.drawable.sun;
		} else if (sIconPartlyCloudy.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.mostlysunny;
        } else if (sIconSun.matcher(iconDescription).find()) {
			icon = com.work.map.R.drawable.sun;
		} else if (sIconClouds.matcher(iconDescription).find()) {
			icon = com.work.map.R.drawable.cloudy;
		} else if (sIconLightrain.matcher(iconDescription).find()) {
			icon = com.work.map.R.drawable.lightrain;
		} else if (sIconStorm.matcher(iconDescription).find()) {
			icon = com.work.map.R.drawable.storm;
		} else if (sIconChanceOfRain.matcher(iconDescription).find()) {
            icon = com.work.map.R.drawable.cloudyrain;
        } else if (sIconRain.matcher(iconDescription).find()) {
			icon = com.work.map.R.drawable.rain;
		} else if (sIconFog.matcher(iconDescription).find()) {
 		    icon = com.work.map.R.drawable.fog;
		} else if (sIconSnow.matcher(iconDescription).find()) {
			icon = com.work.map.R.drawable.rain;
		}
		
		return icon;
	}

}

package com.weather.my;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weather.my.WeatherForecastCondition;

import android.content.Context;
import android.content.Intent;


/**
 * @description 提供适配器数据
 * @version 1.0
 */
public class ListDataProvider implements DataProvider{
	@SuppressWarnings("unused")
	private Context context=null;
	private List<Map<String,String>> dataSource = new ArrayList<Map<String,String>>();
	
	public ListDataProvider(Context context) {
		this.context = context;
	}

	public void loadData(List<WeatherForecastCondition> myForecastConditions){
		for(int x = 0 ; x < myForecastConditions.size(); x++){
			WeatherForecastCondition wfc = myForecastConditions.get(x);
			Map<String,String> map = new HashMap<String, String>();
			map.put("icon", ForecastUtil.getForecastImage(wfc.getIcon())+"");
			map.put("day", wfc.getDay_of_week());
			map.put("condition", wfc.getCondition());
			map.put("temp", wfc.getLow() + "°/" + wfc.getHigh() + "°");
			loadDataSource(map);
		}
	}
	
	private void loadDataSource(Map<String,String> map){
		dataSource.add(map);
	}
	
	public List<Map<String, String>> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<Map<String, String>> dataSource) {
		this.dataSource = dataSource;
	}
	
}

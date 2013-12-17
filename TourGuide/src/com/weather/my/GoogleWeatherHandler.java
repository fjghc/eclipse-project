package com.weather.my;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


/**
 * GoogleWeatherHandler.java
 * 
 * @author Mi Yaqing
 * @description 返回的天气信息为XML格式,并 解析当前XML
 * 
	 * <xml_api_reply version="1">
	-<weather module_id="0" tab_id="0" mobile_row="0" mobile_zipped="1" row="0" section="0">
	-<forecast_information>
	 	<city data="Chengdu, Sichuan"/>
	 		<postal_code data="chengdu"/>
	 			<latitude_e6 data=""/>
	 			<longitude_e6 data=""/>
	 			<forecast_date data="2009-08-18"/>
	 			<current_date_time data="2009-08-19 00:00:00 +0000"/>
	 			<unit_system data="SI"/>
	 </forecast_information>
	-<current_conditions>
	 	<condition data="多云"/>
	 	<temp_f data="88"/>
	 	<temp_c data="31"/>
	 	<humidity data="湿度： 58%"/>
	 	<icon data="/ig/images/weather/cn_cloudy.gif"/>
	 	<wind_condition data="风向： 东、风速：4 米/秒"/>
	 </current_conditions>
	-<forecast_conditions>
	 	<day_of_week data="周二"/>
	 	<low data="24"/>
	 	<high data="33"/>
	 	<icon data="/ig/images/weather/chance_of_rain.gif"/>
	 	<condition data="可能有雨"/>
	 </forecast_conditions>
	-<forecast_conditions>
	 	<day_of_week data="周三"/>
	 	<low data="21"/>
	 	<high data="31"/>
	 	<icon data="/ig/images/weather/cn_heavyrain.gif"/>
	 	<condition data="雨"/>
	 </forecast_conditions>
	-<forecast_conditions>
	 	<day_of_week data="周四"/>
	 	<low data="19"/>
	 	<high data="29"/>
	 	<icon data="/ig/images/weather/mostly_sunny.gif"/>
	 	<condition data="晴间多云"/>
	 </forecast_conditions>
	-<forecast_conditions>
	 	<day_of_week data="周五"/>
	 	<low data="21"/>
	 	<high data="31"/>
	 	<icon data="/ig/images/weather/chance_of_rain.gif"/>
	 	<condition data="可能有雨"/>
	 </forecast_conditions>
	 </weather>
	 </xml_api_reply>
 * @version 1.0
 */

public class GoogleWeatherHandler extends DefaultHandler
{
	/**天气信息*/
	private WeatherSet		myWeatherSet			= null;
	//实时天气信息
	private boolean			is_Current_Conditions	= false;
	//预报天气信息
	private boolean			is_Forecast_Conditions	= false;
	/**当前天气 */
	private final String	CURRENT_CONDITIONS		= "current_conditions";
	/** 预报天气 */
	private final String	FORECAST_CONDITIONS		= "forecast_conditions";


	public GoogleWeatherHandler()
	{
	}

	//返回天气信息对象
	public WeatherSet getMyWeatherSet()
	{
		return myWeatherSet;
	}

	@Override
	public void endDocument() throws SAXException
	{
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{

		if (localName.equals(CURRENT_CONDITIONS))
		{
			this.is_Current_Conditions = false;
		}
		else if (localName.equals(FORECAST_CONDITIONS))
		{
			this.is_Forecast_Conditions = false;
		}
	}

	@Override
	public void startDocument() throws SAXException
	{
		this.myWeatherSet = new WeatherSet();
	}

	/** 
	 * 解析当前XML文件，并将相关数据保存
	 */
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
	{
		/** 当前天气 */
		if (localName.equals(CURRENT_CONDITIONS))
		{
			Log.i("localName+CURRENT", localName);
			this.myWeatherSet.setMyCurrentCondition(new WeatherCurrentCondition());
			Log.i("localName+CURRENT+1", localName);
			this.is_Current_Conditions = true;
		}
		/**预报天气 */
		else if (localName.equals(FORECAST_CONDITIONS))
		{
			this.myWeatherSet.getMyForecastConditions().add(new WeatherForecastCondition());
			this.is_Forecast_Conditions = true;
		}
		else
		{
			/**分别将得到的信息设置到指定的对象中*/
			if (localName.equals(CURRENT_CONDITIONS))
			{
				Log.i("localName+CURRENT", localName);
			}
			String dataAttribute = attributes.getValue("data");
			/** 设置天气图片地址 */
			if (localName.equals("icon"))
			{
				/** 当前天气 */
				if (this.is_Current_Conditions)
				{
					this.myWeatherSet.getMyCurrentCondition().setIcon(dataAttribute);
				}
				/** 预报天气 */
				else if (this.is_Forecast_Conditions)
				{
					this.myWeatherSet.getLastForecastCondition().setIcon(dataAttribute);
				}
			}
			
//			else if (localName.equals("city"))
//			{
//				this.myWeatherSet.getMyCurrentCondition().setCity(dataAttribute);
//			}
			else if (localName.equals("condition"))
			{
				if (this.is_Current_Conditions)
				{
					this.myWeatherSet.getMyCurrentCondition().setCondition(dataAttribute);
				}
				else if (this.is_Forecast_Conditions)
				{
					this.myWeatherSet.getLastForecastCondition().setCondition(dataAttribute);
				}
			}
			else if (localName.equals("temp_c"))
			{
				this.myWeatherSet.getMyCurrentCondition().setTemp_celcius(dataAttribute);
			}
			else if (localName.equals("temp_f"))
			{
				this.myWeatherSet.getMyCurrentCondition().setTemp_fahrenheit(dataAttribute);
			}
			else if (localName.equals("humidity"))
			{
				this.myWeatherSet.getMyCurrentCondition().setHumidity(dataAttribute);
			}
			else if (localName.equals("wind_condition"))
			{
				this.myWeatherSet.getMyCurrentCondition().setWind_condition(dataAttribute);
			}// Tags is forecast_conditions
			else if (localName.equals("day_of_week"))
			{
				this.myWeatherSet.getLastForecastCondition().setDay_of_week(dataAttribute);
			}
			else if (localName.equals("low"))
			{
				this.myWeatherSet.getLastForecastCondition().setLow(dataAttribute);
			}
			else if (localName.equals("high"))
			{
				this.myWeatherSet.getLastForecastCondition().setHigh(dataAttribute);
			}
		}
	}


	@Override
	public void characters(char ch[], int start, int length)
	{
		/*
		 * Would be called on the following structure:
		 * <element>characters</element>
		 */
	}
}

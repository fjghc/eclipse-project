package com.weather.my;

import java.util.ArrayList;

/**
 * @description ʵʱ������Ϣ����
 * @version 1.0
 */
public class WeatherSet
{
	//ʵʱ������Ϣ
	private WeatherCurrentCondition myCurrentCondition = null; 
	//Ԥ���ĺ������������Ϣ
    private ArrayList<WeatherForecastCondition> myForecastConditions = 
         							new ArrayList<WeatherForecastCondition>();
    
    public WeatherSet()
	{
	}
    
    //�õ�ʵʱ������Ϣ�Ķ���
	public WeatherCurrentCondition getMyCurrentCondition()
	{
		return myCurrentCondition;
	}

	//���õ�ǰ������Ϣ�Ķ���
	public void setMyCurrentCondition(WeatherCurrentCondition myCurrentCondition)
	{
		this.myCurrentCondition = myCurrentCondition;
	}

	//�õ�Ԥ������
	public ArrayList<WeatherForecastCondition> getMyForecastConditions()
	{
		return myForecastConditions;
	}

	//�õ����һ��Ԥ������
	//��������ÿ�����һ�����ݶ��������
	//���Եõ����һ��
	public WeatherForecastCondition getLastForecastCondition()
	{
		return myForecastConditions.get(myForecastConditions.size() - 1);
	}
}


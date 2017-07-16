package com.example.shushuweather.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;
import com.example.shushuweather.models.County;
import com.example.shushuweather.models.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {

	/*
	 * ����������������ص����б���Ϣ
	 * 
	 * */
	public synchronized static boolean handleCountyResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//�ж�json�����Ƿ�ɹ�
			if(jsonObject.getInteger("status")==1)
			{
				JSONArray results = JSON.parseArray(jsonObject.getString("districts"));
				
				JSONObject result2 = JSON.parseObject((results.get(0)).toString());
				
				String citynm = result2.getString("name");//������
				
				JSONArray citys = JSON.parseArray(result2.getString("districts"));

				for(Object city:citys)
				{
					if(citys!=null)
					{
						JSONObject p = (JSONObject)city;
						County countyObj = new County();
						countyObj.setCountynm(p.getString("name"));
						countyObj.setCitynm(citynm);
						shushuWeatherDB.saveCounty(countyObj);
						
						Log.d("saveCounty", "��:"+p.getString("name"));
					}
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/*
	 * ����������������ص����б���Ϣ
	 * 
	 * */
	public synchronized static boolean handleCityResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//�ж�json�����Ƿ�ɹ�
			if(jsonObject.getInteger("status")==1)
			{
				JSONArray results = JSON.parseArray(jsonObject.getString("districts"));
				
				JSONObject result2 = JSON.parseObject((results.get(0)).toString());
				
				String provincenm = result2.getString("name");//ʡ����
				
				JSONArray citys = JSON.parseArray(result2.getString("districts"));

				for(Object city:citys)
				{
					if(city!=null)
					{
						JSONObject c = (JSONObject)city;
						City cityObj = new City();
						cityObj.setCitynm(c.getString("name"));
						cityObj.setProvincenm(provincenm);
						shushuWeatherDB.saveCity(cityObj);
						
						Log.d("saveCity", "��:"+c.getString("name"));
					}
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	/*
	 * ����������������صĳ����б���Ϣ-��
	 * 
	 * */
	public synchronized static boolean handleProvinceResponse(ShushuWeatherDB shushuWeatherDB,String response){
		if(!TextUtils.isEmpty(response))
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//�ж�json�����Ƿ�ɹ�
			if(jsonObject.getInteger("status")==1)
			{
				JSONArray results = JSON.parseArray(jsonObject.getString("districts"));
				
				JSONObject result2 = JSON.parseObject((results.get(0)).toString());
				
				JSONArray provinces = JSON.parseArray(result2.getString("districts"));

				for(Object province:provinces)
				{
					if(province!=null)
					{
						JSONObject p = (JSONObject)province;
						Province provinceObj = new Province();
						provinceObj.setProvincenm(p.getString("name"));
						
						shushuWeatherDB.saveProvince(provinceObj);
						
						Log.d("saveProvince", "ʡ:"+p.getString("name"));
					}
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * ����������Ϣjson
	 * @author Administrator
	 */
	public static void handleWeatherResponse(Context context,String response)
	{
		try
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//�жϷ��ص�json�����Ƿ�ɹ�
			if(jsonObject.getInteger("success")==1)
			{
				JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));
				if(result!=null)
				{
					String weaid = result.getString("weaid");
					String days = result.getString("days");
					String week = result.getString("week");
					String citynm = result.getString("citynm");
					String temperature = result.getString("temperature");
					String temperature_curr = result.getString("temperature_curr");
					String humidity = result.getString("humidity");
					String weather = result.getString("weather");
					String weather_curr = result.getString("weather_curr");
					String wind = result.getString("wind");
					String winp = result.getString("winp");
					String temp_high = result.getString("temp_high");
					String temp_low = result.getString("temp_low");
					String temp_curr = result.getString("temp_curr");
					
					//saveWeatherInfo(context,weaid,days,week,citynm,temperature,temperature_curr,humidity,weather,weather_curr,wind,winp,temp_high,temp_low,temp_curr);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * ����jd������Ϣjson
	 * @author Administrator
	 */
	public static void handleJDWeatherResponse(Context context,String response)
	{
		try
		{
			JSONObject jsonObject = JSON.parseObject(response);
			
			//�жϷ��ص�json�����Ƿ�ɹ�
			if(jsonObject.getInteger("code")==10000)
			{
				JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));
				
				JSONArray result2 = JSONArray.parseArray(result.getString("HeWeather5"));
				
				JSONObject result3 = JSONObject.parseObject((result2.get(0)).toString());
				
				JSONObject result4 = JSONObject.parseObject(result3.getString("now"));
				
				JSONObject result5 = JSONObject.parseObject(result4.getString("cond"));//����״��
				
				JSONObject result6 = JSONObject.parseObject(result4.getString("wind"));//����״��
				
				JSONObject result7 = JSONObject.parseObject(result3.getString("basic"));//��������
				
				String county = result7.getString("city");
				if(result4!=null)
				{
					String hum = result4.getString("hum");//ʪ��
					String vis = result4.getString("vis");//�ܼ���
					String pres = result4.getString("pres");//����ѹǿ
					String pcpn = result4.getString("pcpn");//��ˮ��
					String fl = result4.getString("fl");//��ðָ��
					String tmp = result4.getString("tmp");//�¶�
					String txt="",sc="",spd="",deg="",dir="";
					
					if(result5!=null)
					{
						txt = result5.getString("txt");//����״��
					}
					
					if(result6!=null)
					{
						sc = result6.getString("sc");//�����ȼ�
						spd = result6.getString("spd");//����
						deg = result6.getString("deg");//����(�Ƕ�)
						dir = result6.getString("dir");//����
					}
					
					saveWeatherInfo(context,hum,vis,pres,pcpn,fl,tmp,txt,sc,spd,deg,dir,county);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//��������������
	public static void saveWeatherInfo(Context context,String hum,String vis,String pres,String pcpn,String fl,String tmp,String txt,String sc,String spd,String deg,String dir,String county)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("Hʱmm��",Locale.CHINA);
		SimpleDateFormat date = new SimpleDateFormat("yyyy��MM��dd��",Locale.CHINA);
		SimpleDateFormat week = new SimpleDateFormat("EEEE",Locale.CHINA);
		
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		editor.putBoolean("city_selected", true);
		editor.putString("hum", hum);
		editor.putString("vis", vis);
		editor.putString("pres", pres);
		editor.putString("pcpn", pcpn);
		editor.putString("fl", fl);
		editor.putString("tmp", tmp);
		editor.putString("txt", txt);
		editor.putString("sc", sc);
		editor.putString("spd", spd);
		editor.putString("deg", deg);
		editor.putString("dir", dir);
		editor.putString("county", county);
		editor.putString("publishtime", sdf.format(new Date()));
		editor.putString("date", date.format(new Date()));
		editor.putString("week", week.format(new Date()));
		
		editor.commit();
	}
	
	//��������������ת��
	public static String UrlTranslateToUTF(String address)
	{
		try
		{
			address = URLEncoder.encode(address, "utf-8");
			
			return address;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return "";
			
		}
	}	
}

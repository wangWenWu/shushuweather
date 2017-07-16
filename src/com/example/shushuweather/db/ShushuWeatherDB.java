package com.example.shushuweather.db;
/*
 * ���ݿ⹤����
 * 
 * @author WangBin
 * @time 2017-07-13
 * */

import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.models.City;
import com.example.shushuweather.models.County;
import com.example.shushuweather.models.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShushuWeatherDB {
	public static final String DB_NAME = "shushu_weather";//���ݿ���
	public static final String TB_CITY = "city";//�����б����
	public static final String TB_CITY_NAME = "acity";//���б����
	public static final String TB_PROVINCE_NAME = "aprovince";//ʡ�б����
	public static final String TB_COUNTY_NAME = "acounty";//�����б����
	public static final int DB_VERSION = 1;//���ݿ�汾��
	private static ShushuWeatherDB shushuWeatherDB;
	private SQLiteDatabase db;
	
	/*�����췽��˽�л�*/
	private ShushuWeatherDB(Context context) {
		// TODO Auto-generated constructor stub
		ShushuWeatherOpenHelper dbHelper = new ShushuWeatherOpenHelper(context, DB_NAME, null, DB_VERSION);
		Log.d("ShushuWeatherDB", "�����췽��˽�л�");
		db = dbHelper.getWritableDatabase();
	}
	
	/*��ȡshushuWeatherDB��ʵ��*/
	public synchronized static ShushuWeatherDB getinstance(Context context)
	{
		if(shushuWeatherDB==null)
		{
			shushuWeatherDB = new ShushuWeatherDB(context);
		}
		
		return shushuWeatherDB;
	}
	
	/*
	 * �������Ƿ����
	 * ���ڷ���true�������ڷ���false
	 * */
	public boolean checkCityExist(String cityid)
	{
		Cursor cursor = db.query(TB_CITY, new String[]{"id"},"cityid=?", new String[]{cityid}, null, null, null);
		
		if(cursor.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/*��ʡ��Ϣ����province����*/
	public void saveProvince(Province province)
	{
		if(province!=null)
		{
			ContentValues values = new ContentValues();
			
			values.put("provincenm", province.getProvincenm());
			
			db.insert(TB_PROVINCE_NAME, null, values);
		}
	}
	
	/*������Ϣ����city����*/
	public void saveCity(City city)
	{
		if(city!=null)
		{
			try
			{
				ContentValues values = new ContentValues();
				
				values.put("provincenm", city.getProvincenm());
				values.put("citynm", city.getCitynm());
				
				db.insert(TB_CITY_NAME, null, values);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
	}
	
	/*������Ϣ����county����*/
	public void saveCounty(County county)
	{
		if(county!=null)
		{
			ContentValues values = new ContentValues();
			
			values.put("citynm", county.getCitynm());
			values.put("countynm", county.getCountynm());
			
			db.insert(TB_COUNTY_NAME, null, values);
		}
	}	
	
	/*��ȡ���е�ʡ��*/
	public List<Province> getAllProvinces()
	{
		List<Province> list = new ArrayList<Province>();
		
		Cursor cursor = db.query(TB_PROVINCE_NAME, new String[]{"provincenm"}, null, null, null, null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				Province province = new Province();
				province.setProvincenm(cursor.getString(cursor.getColumnIndex("provincenm")));
				
				list.add(province);
				
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/*
	 * ����ʡ�����ƻ�ȡʡ���������
	 * 
	 * @param String provincenm ʡ������
	 * @return List list �������ڸ�ʡ�ݵ���
	 * */
	public List<City> getAllCity(String provincenm)
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY_NAME, null, "provincenm=?", new String[]{provincenm}, "citynm", null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				
				city.setCitynm(cursor.getString(cursor.getColumnIndex("citynm")));
				
				list.add(city);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/*
	 * ���������ƻ�ȡ�����������
	 * 
	 * @param String municipality ������
	 * @return List list �������ڸ��е�����
	 * */
	public List<County> getAllCounty(String citynm)
	{
		List<County> list = new ArrayList<County>();
		
		Cursor cursor = db.query(TB_COUNTY_NAME, null, "citynm=?", new String[]{citynm}, null, null, null);

		if(cursor.moveToFirst())
		{
			do{
				County county = new County();
				county.setCountynm(cursor.getString(cursor.getColumnIndex("countynm")));
				
				list.add(county);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/**
	 * ���ݳ�������ȡ����ID
	 * */
	public String getWeatherId(String county)
	{
		Cursor cursor = db.query(TB_CITY, new String[]{"weaid"}, "county=?", new String[]{county}, null, null, null);
		
		if(cursor.moveToFirst())
		{
			String weaid = cursor.getString(cursor.getColumnIndex("weaid"));
			
			if(weaid!=null)
			{
				return weaid;
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "";
		}
	}
}

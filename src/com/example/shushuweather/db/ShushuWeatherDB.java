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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShushuWeatherDB {
	public static final String DB_NAME = "shushu_weather";//���ݿ���
	public static final String TB_CITY = "city";//�����б����
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
	
	/*��������Ϣ����city����*/
	public void saveCity(City city)
	{
		if(city != null)
		{
			String cityid = city.getCityid();
			boolean isExist = checkCityExist(cityid);
			
			if(!isExist)
			{
				ContentValues values = new ContentValues();
				
				values.put("weaid", city.getWeaid());
				values.put("citynm", city.getCitynm());
				values.put("cityno", city.getCityno());
				values.put("cityid", city.getCityid());
				values.put("province", city.getProvince());
				values.put("municipality", city.getMunicipality());
				values.put("county", city.getCounty());
				
				db.insert(TB_CITY, null, values);
			}
		}
	}
	
	/*��ȡ���е�ʡ��*/
	public List<City> getAllProvinces()
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, null, null, "province", null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
				
				list.add(city);
				
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
	public List<City> getAllMunicipality(String provincenm)
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, "province=?", new String[]{provincenm}, "municipality", null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				
				city.setMunicipality(cursor.getString(cursor.getColumnIndex("municipality")));
				
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
	public List<City> getAllCounty(String municipality)
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, "municipality=?", new String[]{municipality}, null, null, null);
		
		if(cursor.moveToFirst())
		{
			do{
				City city = new City();
				city.setCounty(cursor.getString(cursor.getColumnIndex("county")));
				
				list.add(city);
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

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
	
	/*��������Ϣ����city����*/
	public void saveCity(City city)
	{
		if(city != null)
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
	
	/*��ȡ���е�ʡ��*/
	public List<City> getAllProvinces()
	{
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query(TB_CITY, null, null, null, null, null, null);
		
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
		
		Cursor cursor = db.query(TB_CITY, null, "province=?", new String[]{provincenm}, null, null, null);
		
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
				
				city.setMunicipality(cursor.getString(cursor.getColumnIndex("county")));
				
				list.add(city);
			}while(cursor.moveToNext());
		}
		
		return list;
	}	
}

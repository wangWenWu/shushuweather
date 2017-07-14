package com.example.shushuweather.activity;

import com.example.shushuweather.R;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Presence;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	
	private LinearLayout weatherInfoLayout;
	private TextView citynm;//��ʾ������
	private TextView publishText;//��ʾ����ʱ��
	private TextView weatherDespText;//��ʾ����
	private TextView tempLow;//��ʾ�������
	private TextView tempHigh;//��ʾ�������
	private TextView currentDate;//��ʾ��ǰ����
	private Button switchCityBtn;//�л����а�ť
	private Button refreshWeather;//����������ť
	private ShushuWeatherDB shushuWeatherDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		citynm = (TextView)findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.publish_text);
		tempLow = (TextView)findViewById(R.id.templ1);
		tempHigh = (TextView)findViewById(R.id.templ2);
		currentDate = (TextView)findViewById(R.id.current_date);
		shushuWeatherDB = ShushuWeatherDB.getinstance(this);
		
		String county = getIntent().getStringExtra("county");

		if(!TextUtils.isEmpty(county))
		{
			//���ؼ����ƾ�ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			citynm.setVisibility(View.INVISIBLE);
			String weaid = shushuWeatherDB.getWeatherId(county);
			queryWeatherCounty(weaid);
		}
		else
		{
			//û����ֱ����ʾ��������
			showWeather();
		}
	}
	
	//ͨ���ؼ���������ȡ����ID����ͨ��ID����ȡ����
	private void queryWeatherCounty(String weaid)
	{
		if(weaid!="")
		{
			String address = "http://api.k780.com/?app=weather.today&weaid="+weaid+"&appkey=26776&sign=42d21df6df1c8068dbd225379b10ac98&format=json";
			
			queryFromServer(address);
		}
	}
	
	//�ӷ������ϲ�ѯ����
	private void queryFromServer(String address)
	{
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showWeather();
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//��ʾ��������
	private void showWeather()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		citynm.setText(prefs.getString("citynm", ""));
		tempLow.setText(prefs.getString("temp_low", ""));
		tempHigh.setText(prefs.getString("temp_high", ""));
		
		weatherInfoLayout.setVisibility(View.VISIBLE);
		citynm.setVisibility(View.VISIBLE);
		Log.d("weather", prefs.toString());
	}
}

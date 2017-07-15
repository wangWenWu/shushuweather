package com.example.shushuweather.activity;

import com.example.shushuweather.R;
import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.service.AutoUpdateWeather;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Presence;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{
	
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
	private String weaid,county;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		citynm = (TextView)findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		tempLow = (TextView)findViewById(R.id.templ1);
		tempHigh = (TextView)findViewById(R.id.templ2);
		currentDate = (TextView)findViewById(R.id.current_date);
		switchCityBtn = (Button)findViewById(R.id.switch_city);
		refreshWeather = (Button)findViewById(R.id.refresh_weather);
		
		switchCityBtn.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		shushuWeatherDB = ShushuWeatherDB.getinstance(this);
		
		county = getIntent().getStringExtra("county");

		if(!TextUtils.isEmpty(county))
		{
			//���ؼ����ƾ�ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			citynm.setVisibility(View.INVISIBLE);
			weaid = shushuWeatherDB.getWeatherId(county);
			queryWeatherCounty(weaid);
		}
		else
		{
			//û����ֱ����ʾ��������
			showWeather();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("ͬ����...");
			//String weaid = shushuWeatherDB.getWeatherId(county);
			queryWeatherCounty(weaid);
			break;
		default:
			break;
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
		
		weaid = prefs.getString("weaid", "");
		citynm.setText(prefs.getString("citynm", ""));
		tempLow.setText(prefs.getString("temp_low", "")+"��");
		tempHigh.setText(prefs.getString("temp_high", "")+"��");
		currentDate.setText(prefs.getString("days", "")+" "+prefs.getString("week", ""));
		weatherDespText.setText(prefs.getString("weather", ""));
		publishText.setText("����"+prefs.getString("publish_time", "")+"����");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		citynm.setVisibility(View.VISIBLE);
		
		Intent i = new Intent(WeatherActivity.this,AutoUpdateWeather.class);
		startService(i);
	}
}

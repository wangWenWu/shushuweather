package com.example.shushuweather.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;
import com.example.shushuweather.models.County;
import com.example.shushuweather.models.Province;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;
import com.example.shushuweather.R;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity implements OnClickListener{
	
	private TextView titleText;//����
	private ListView listView;//�����б�
	//private Button update_city_btn;//���³����б�ť
	private ArrayAdapter<String> adapter;
	private ShushuWeatherDB shushuWeatherDB;
	private List<Province> provinceList;//ʡ�б�
	private List<City> cityList;//���б�
	private List<County> countyList;//�����б�
	private List<String> dataList = new ArrayList<String>();
	private int currentLevel;//��ǰѡ�����ʡ���л�����
	private static final int LEVEL_PROVINCE = 0;//ʡ
	private static final int LEVEL_MUNICIPALITY = 1;//��
	private static final int LEVEL_COUNTY = 2;//����
	private ProgressDialog progressDialog;//����
	private Province provinceSeletced;//ѡ�е�ʡ
	private City citySelected;//ѡ�е���
	private County countySelected;//ѡ�е�����
	private boolean networkavilable = false;//���粻����

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//�鿴����״̬
		networkavilable = Utility.checkNetworkAvailable(ChooseAreaActivity.this);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		//�Ƿ��WeatherActivity��й���;true��ʾ��
		boolean from_weather_activity = getIntent().getBooleanExtra("from_weather_activity", false);
		if(!from_weather_activity)
		{
			//���֮ǰ��ȡ��������Ϣ��ֱ����ת����
			if(prefs.getBoolean("city_selected", false))
			{
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
				finish();
				return;
			}
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		titleText = (TextView)findViewById(R.id.title_text);
		listView = (ListView)findViewById(R.id.list_view);
		//update_city_btn = (Button)findViewById(R.id.update_city_btn);
		
		//update_city_btn.setOnClickListener(this);//���³����б�ť�󶨵���¼�
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		
		listView.setAdapter(adapter);
		
		shushuWeatherDB = ShushuWeatherDB.getinstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel==LEVEL_PROVINCE)
				{
					provinceSeletced = provinceList.get(position);
					queryCity();//��ȡ��ʡ������������
				}
				else if(currentLevel ==LEVEL_MUNICIPALITY)
				{
					citySelected = cityList.get(position);
					queryCounty();//��ȡ������������������
				}
				else if(currentLevel ==LEVEL_COUNTY)
				{
					String county = countyList.get(position).getCountynm();
					
					Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("county", county);
					startActivity(intent);
					finish();
				}
				
				
			}
		});
		
		queryProvinces();//��һ�μ���ʡ������
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*case R.id.update_city_btn:
			queryProvincesfromServer();
			break;*/

		default:
			break;
		}
	}
	
	//��ȡѡ������������������
	public void queryCounty()
	{
		countyList = shushuWeatherDB.getAllCounty(citySelected.getCitynm());
		if(countyList.size()>0)
		{
			dataList.clear();
			
			for(County county:countyList)
			{
				dataList.add(county.getCountynm());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(citySelected.getCitynm());
			currentLevel =LEVEL_COUNTY;
		}
		else
		{
			if(networkavilable)
			{
				queryCountyFromServer();//�ӷ�������ȡ������Ϣ
			}
			else
			{
				Toast.makeText(this, "С��~���粻����Ӵ~", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//��ȡѡ��ʡ��������������
	public void queryCity()
	{
		cityList = shushuWeatherDB.getAllCity(provinceSeletced.getProvincenm());

		if(cityList.size()>0)
		{
			dataList.clear();
			
			for(City city:cityList)
			{
				dataList.add(city.getCitynm());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(provinceSeletced.getProvincenm());
			currentLevel = LEVEL_MUNICIPALITY;
		}
		else
		{
			if(networkavilable)
			{
				queryCityFromServer();
			}
			else
			{
				Toast.makeText(this, "С��~���粻����Ӵ~", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	//��ȡ���е��й���ʡ
	private void queryProvinces()
	{
		provinceList = shushuWeatherDB.getAllProvinces();
		
		if(provinceList.size()>0)
		{
			dataList.clear();
			
			for(Province province:provinceList)
			{
				dataList.add(province.getProvincenm());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);//�б��ƶ���ָ����Position��
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}
		else
		{
			if(networkavilable)
			{
				queryProvincesfromServer();
			}
			else
			{
				Toast.makeText(this, "С��~���粻����Ӵ~", Toast.LENGTH_SHORT).show();
			}
		}
	}

	//�ӷ������ϻ�ȡĳʡ������
	private void queryCountyFromServer()
	{
		showProgressDialog();
		String citynm = citySelected.getCitynm();
		citynm = Utility.UrlTranslateToUTF(citynm);
		String address = "http://restapi.amap.com/v3/config/district?keywords="+citynm+"&subdistrict=1&key=8eee051d08bc42cfc33dbd065a87d60e";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;

				result = Utility.handleCountyResponse(shushuWeatherDB, response);
				
				if(result)
				{
					//ͨ��runOnUiThread�ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							queryCounty();
						}
					});
				}
				else
				{
					//ͨ��runOnUiThread�ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "��ȡ������Ϣʧ�ܣ�", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//�ӷ������ϻ�ȡĳʡ������
	private void queryCityFromServer()
	{
		showProgressDialog();
		String provincenm = provinceSeletced.getProvincenm();
		provincenm = Utility.UrlTranslateToUTF(provincenm);
		String address = "http://restapi.amap.com/v3/config/district?keywords="+provincenm+"&subdistrict=1&key=8eee051d08bc42cfc33dbd065a87d60e";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				result = Utility.handleCityResponse(shushuWeatherDB, response);
				
				if(result)
				{
					//ͨ��runOnUiThread�ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							queryCity();
						}
					});
				}
				else
				{
					//ͨ��runOnUiThread�ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "��ȡ����Ϣʧ�ܣ�", Toast.LENGTH_SHORT).show();
						}
					});
				}				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//�ӷ������ϻ�ȡ����ʡ
	private void queryProvincesfromServer()
	{
		showProgressDialog();
		String address = " http://restapi.amap.com/v3/config/district?subdistrict=1&key=8eee051d08bc42cfc33dbd065a87d60e";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				
				result = Utility.handleProvinceResponse(shushuWeatherDB, response);
				
				if(result)
				{
					//ͨ��runOnUiThread�ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							queryProvinces();
						}
					});
				}
				else
				{
					//ͨ��runOnUiThread�ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							Toast.makeText(ChooseAreaActivity.this, "��ȡʡ��Ϣʧ�ܣ�", Toast.LENGTH_SHORT).show();
						}
					});
				}				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//��ʾ���ȶԻ���
	private void showProgressDialog()
	{
		if(progressDialog ==null)
		{
			progressDialog = new ProgressDialog(this);
			
			progressDialog.setMessage("���ڼ�����...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		
		progressDialog.show();
	}
	
	//�رս��ȶԻ���
	private void closeProgressDialog()
	{
		if(progressDialog!=null)
		{
			progressDialog.dismiss();
		}
	}
	
	//��дBack�����¼������ݵ�ǰ�ļ����жϣ���ʱӦ�÷���ʡ���У������б����˳�
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(currentLevel==LEVEL_MUNICIPALITY)
		{
			queryProvinces();
		}
		else if(currentLevel==LEVEL_COUNTY)
		{
			queryCity();
		}
		else
		{
			finish();
		}
	}
}

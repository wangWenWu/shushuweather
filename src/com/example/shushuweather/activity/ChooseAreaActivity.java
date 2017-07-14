package com.example.shushuweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.shushuweather.db.ShushuWeatherDB;
import com.example.shushuweather.models.City;
import com.example.shushuweather.utils.HttpCallbackListener;
import com.example.shushuweather.utils.HttpUtil;
import com.example.shushuweather.utils.Utility;
import com.example.shushuweather.R;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.os.Bundle;
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
	private Button update_city_btn;//���³����б�ť
	private ArrayAdapter<String> adapter;
	private ShushuWeatherDB shushuWeatherDB;
	private List<City> provinceList;//ʡ�б�
	private List<City> municipalityList;//���б�
	private List<City> countyList;//�����б�
	private List<String> dataList = new ArrayList<String>();
	private int currentLevel;//��ǰѡ�����ʡ���л�����
	private static final int LEVEL_PROVINCE = 0;//ʡ
	private static final int LEVEL_MUNICIPALITY = 1;//��
	private static final int LEVEL_COUNTY = 2;//����
	private ProgressDialog progressDialog;//����
	private City provinceSeletced;//ѡ�е�ʡ
	private City municipalitySelected;//ѡ�е���
	private City countySelected;//ѡ�е�����
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		titleText = (TextView)findViewById(R.id.title_text);
		listView = (ListView)findViewById(R.id.list_view);
		update_city_btn = (Button)findViewById(R.id.update_city_btn);
		
		update_city_btn.setOnClickListener(this);//���³����б�ť�󶨵���¼�
		
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
					queryMunicipality();//��ȡ��ʡ������������
				}
				else if(currentLevel ==LEVEL_MUNICIPALITY)
				{
					municipalitySelected = municipalityList.get(position);
					queryCounty();//��ȡ������������������
				}
				
				
			}
		});
		
		queryProvinces();//��һ�μ���ʡ������
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.update_city_btn:
			queryProvincesfromServer();
			break;

		default:
			break;
		}
	}
	
	//��ȡѡ������������������
	public void queryCounty()
	{
		countyList = shushuWeatherDB.getAllCounty(municipalitySelected.getMunicipality());
		
		if(countyList.size()>0)
		{
			dataList.clear();
			
			for(City county:countyList)
			{
				dataList.add(county.getCounty());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(municipalitySelected.getMunicipality());
		}
	}
	
	//��ȡѡ��ʡ��������������
	public void queryMunicipality()
	{
		municipalityList = shushuWeatherDB.getAllMunicipality(provinceSeletced.getProvince());
		
		if(municipalityList.size()>0)
		{
			dataList.clear();
			
			for(City municipality:municipalityList)
			{
				dataList.add(municipality.getMunicipality());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(provinceSeletced.getProvince());
			currentLevel = LEVEL_MUNICIPALITY;
		}
	}
	
	//��ȡ���е��й���ʡ
	private void queryProvinces()
	{
		provinceList = shushuWeatherDB.getAllProvinces();
		
		if(provinceList.size()>0)
		{
			dataList.clear();
			
			for(City province:provinceList)
			{
				dataList.add(province.getProvince());
			}
			
			adapter.notifyDataSetChanged();
			listView.setSelection(0);//�б��ƶ���ָ����Position��
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}
		else
		{
			//Toast.makeText(ChooseAreaActivity.this, "����ʡ����Ϣ", Toast.LENGTH_SHORT).show();
			queryProvincesfromServer();
		}
	}
	
	//�ӷ������ϻ�ȡ�����б�
	private void queryProvincesfromServer()
	{
		showProgressDialog();
		String address = "http://api.k780.com/?app=weather.city&cou=1&appkey=26776&sign=42d21df6df1c8068dbd225379b10ac98&format=json";
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
							queryProvinces();
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
			queryMunicipality();
		}
		else
		{
			finish();
		}
	}
}

package com.example.shushuweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.shushuweather.utils.MyData;

/**
 * ��������״̬��ʵʱ����MyData�е������ʶ��ȷ��APP֪����ǰ������״��
 * @author WangBin
 *
 */
public class NetworkChangedReceiver extends BroadcastReceiver {
	private MyData mydata;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mydata = (MyData)context.getApplicationContext();
		if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
		{
			Log.d("NetWork", intent.getAction());
			
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
			
			if(networkinfo!=null)
			{
				//����ȥ��������ɳ�ͨ
				if(networkinfo.isConnected())
				{
					if(networkinfo.getType()==ConnectivityManager.TYPE_WIFI)
					{
						//wifi
						mydata.setNetworkIsOk(true);
						mydata.setNetworkType(2);
					}
					else if(networkinfo.getType()==ConnectivityManager.TYPE_MOBILE)
					{
						//GPRS
						mydata.setNetworkIsOk(true);
						mydata.setNetworkType(1);
					}
				}
				else
				{
					Log.d("Network", "���粻����");
				}
			}
			else
			{
				Log.d("Network", "���粻����");
			}
		}
	}

}

package com.example.shushuweather.utils;
/*
 * ������ʻص��ӿ�
 * */
public interface HttpCallbackListener {
	
	void onFinish(String response);
	
	void onError(Exception e);
}

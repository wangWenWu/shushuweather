package com.example.shushuweather.models;
/*
 * ��ģ����
 * 
 * @author WangBin
 * @time 2017-07-13 14:00
 * */
public class City {
	
	private int id;//�����б��ID
	private String citynm;//������������
	private String provincenm;//ʡ

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCitynm() {
		return citynm;
	}
	public void setCitynm(String citynm) {
		this.citynm = citynm;
	}
	public String getProvincenm() {
		return provincenm;
	}
	public void setProvincenm(String provincenm) {
		this.provincenm = provincenm;
	}
	
}

package com.example.shushuweather.models;
/*
 * ����ģ����
 * 
 * @author WangBin
 * @time 2017-07-13 14:00
 * */
public class City {
	
	private int id;//�����б��ID
	private String citynm;//������������
	private String cityno;//��������ƴ��
	private String cityid;//���б��
	private String province;//ʡ
	private String municipality;//��
	private String county;//������
	private String weaid;//����ID
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
	public String getCityno() {
		return cityno;
	}
	public void setCityno(String cityno) {
		this.cityno = cityno;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getMunicipality() {
		return municipality;
	}
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getWeaid() {
		return weaid;
	}
	public void setWeaid(String weaid) {
		this.weaid = weaid;
	}
}

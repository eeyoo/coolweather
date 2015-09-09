package com.coolweather.app.model;

public class Province { //省实体类

	private int id;
	private String provinceName;
	private String proviceCode;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProviceCode() {
		return proviceCode;
	}
	public void setProviceCode(String proviceCode) {
		this.proviceCode = proviceCode;
	}
	
}

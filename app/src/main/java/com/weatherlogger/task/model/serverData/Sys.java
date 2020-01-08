package com.weatherlogger.task.model.serverData;

import com.google.gson.annotations.SerializedName;

public class Sys{

	@SerializedName("country")
	private String country;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}
}
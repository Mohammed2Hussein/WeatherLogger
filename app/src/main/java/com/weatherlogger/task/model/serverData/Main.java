package com.weatherlogger.task.model.serverData;

import com.google.gson.annotations.SerializedName;

public class Main{

	@SerializedName("temp")
	private double temp;

	@SerializedName("temp_min")
	private double tempMin;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("pressure")
	private int pressure;

	@SerializedName("feels_like")
	private double feelsLike;

	@SerializedName("temp_max")
	private double tempMax;

	public void setTemp(double temp){
		this.temp = temp;
	}

	public double getTemp(){
		return temp;
	}

	public void setTempMin(double tempMin){
		this.tempMin = tempMin;
	}

	public double getTempMin(){
		return tempMin;
	}

	public void setHumidity(int humidity){
		this.humidity = humidity;
	}

	public int getHumidity(){
		return humidity;
	}

	public void setPressure(int pressure){
		this.pressure = pressure;
	}

	public int getPressure(){
		return pressure;
	}

	public void setFeelsLike(double feelsLike){
		this.feelsLike = feelsLike;
	}

	public double getFeelsLike(){
		return feelsLike;
	}

	public void setTempMax(double tempMax){
		this.tempMax = tempMax;
	}

	public double getTempMax(){
		return tempMax;
	}
}
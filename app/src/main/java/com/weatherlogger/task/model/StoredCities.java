package com.weatherlogger.task.model;

public class StoredCities {
    private int cityID;
    private String cityName;
    private float cityTemp;
    private String requestTime;

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getCityTemp() {
        return cityTemp;
    }

    public void setCityTemp(float cityTemp) {
        this.cityTemp = cityTemp;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}

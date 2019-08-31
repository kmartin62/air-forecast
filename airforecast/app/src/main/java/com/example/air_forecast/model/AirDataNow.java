package com.example.air_forecast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AirDataNow {

    @SerializedName("aqi")
    @Expose
    private float aqi;

    @SerializedName("pm10")
    @Expose
    private float pm10;

    @SerializedName("pm25")
    @Expose
    private float pm25;

    public float getAqi() {
        return aqi;
    }

    public void setAqi(float aqi) {
        this.aqi = aqi;
    }

    public float getPm10() {
        return pm10;
    }

    public void setPm10(float pm10) {
        this.pm10 = pm10;
    }

    public float getPm25() {
        return pm25;
    }

    public void setPm25(float pm25) {
        this.pm25 = pm25;
    }
}

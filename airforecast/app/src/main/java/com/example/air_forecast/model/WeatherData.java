package com.example.air_forecast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("wind_cdir")
    @Expose
    private String wind_cdir;

    @SerializedName("wind_spd")
    @Expose
    private float wind_spd;

    @SerializedName("uv")
    @Expose
    private float uv;

    @SerializedName("datetime")
    @Expose
    private String datetime;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getWind_cdir() {
        return wind_cdir;
    }

    public void setWind_cdir(String wind_cdir) {
        this.wind_cdir = wind_cdir;
    }

    public float getWind_spd() {
        return wind_spd;
    }

    public void setWind_spd(float wind_spd) {
        this.wind_spd = wind_spd;
    }

    public float getUv() {
        return uv;
    }

    public void setUv(float uv) {
        this.uv = uv;
    }
}

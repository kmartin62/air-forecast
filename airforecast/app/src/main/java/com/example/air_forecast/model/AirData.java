package com.example.air_forecast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AirData {

    @SerializedName("aqi")
    @Expose
    private float aqi;

    @SerializedName("pm10")
    @Expose
    private float pm10;

    @SerializedName("pm25")
    @Expose
    private float pm25;

    @SerializedName("o3")
    @Expose
    private float o3;

    @SerializedName("timestamp_local")
    @Expose
    private String timestamp_local;

    @SerializedName("so2")
    @Expose
    private float so2;

    @SerializedName("no2")
    @Expose
    private float no2;

    @SerializedName("timestamp_utc")
    @Expose
    private String timestamp_utc;

    @SerializedName("datetime")
    @Expose
    private String datetime;

    @SerializedName("co")
    @Expose
    private float co;

    @SerializedName("ts")
    @Expose
    private long ts;

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

    public float getO3() {
        return o3;
    }

    public void setO3(float o3) {
        this.o3 = o3;
    }

    public String getTimestamp_local() {
        return timestamp_local;
    }

    public void setTimestamp_local(String timestamp_local) {
        this.timestamp_local = timestamp_local;
    }

    public float getSo2() {
        return so2;
    }

    public void setSo2(float so2) {
        this.so2 = so2;
    }

    public float getNo2() {
        return no2;
    }

    public void setNo2(float no2) {
        this.no2 = no2;
    }

    public String getTimestamp_utc() {
        return timestamp_utc;
    }

    public void setTimestamp_utc(String timestamp_utc) {
        this.timestamp_utc = timestamp_utc;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public float getCo() {
        return co;
    }

    public void setCo(float co) {
        this.co = co;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "AirData{" +
                "aqi=" + aqi +
                ", pm10=" + pm10 +
                ", pm25=" + pm25 +
                ", o3=" + o3 +
                ", timestamp_local='" + timestamp_local + '\'' +
                ", so2=" + so2 +
                ", no2=" + no2 +
                ", timestamp_utc='" + timestamp_utc + '\'' +
                ", datetime='" + datetime + '\'' +
                ", co=" + co +
                ", ts=" + ts +
                '}';
    }
}

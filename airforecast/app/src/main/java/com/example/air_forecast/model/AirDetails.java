package com.example.air_forecast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AirDetails {

    @SerializedName("data")
    @Expose
    private ArrayList<AirData> data;

    @SerializedName("city_name")
    @Expose
    private String city_name;

    @SerializedName("lon")
    @Expose
    private float lon;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("lat")
    @Expose
    private float lat;

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("state_code")
    @Expose
    private String state_code;

    public ArrayList<AirData> getData() {
        return data;
    }

    public void setData(ArrayList<AirData> data) {
        this.data = data;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    @Override
    public String toString() {
        return "AirDetails{" +
                "data=" + data +
                ", city_name='" + city_name + '\'' +
                ", lon=" + lon +
                ", timezone='" + timezone + '\'' +
                ", lat=" + lat +
                ", country_code='" + country_code + '\'' +
                ", state_code='" + state_code + '\'' +
                '}';
    }
}

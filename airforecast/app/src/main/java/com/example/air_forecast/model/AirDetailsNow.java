package com.example.air_forecast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AirDetailsNow {

    @SerializedName("data")
    @Expose
    private ArrayList<AirDataNow> data;

    @SerializedName("city_name")
    @Expose
    private String city_name;

    @SerializedName("lon")
    @Expose
    private String lon;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("state_code")
    @Expose
    private String state_code;

    public ArrayList<AirDataNow> getData() {
        return data;
    }

    public void setData(ArrayList<AirDataNow> data) {
        this.data = data;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
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
}

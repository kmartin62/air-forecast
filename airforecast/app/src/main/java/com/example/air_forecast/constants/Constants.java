package com.example.air_forecast.constants;

public class Constants {

    private final static String country = "MK";

    private final static String API_KEY = "dba29d449897449e9f73f912b98e5681";

    private final static String URL = "https://api.weatherbit.io/v2.0/";

    public static String getCountry() {
        return country;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getURL() {
        return URL;
    }
}

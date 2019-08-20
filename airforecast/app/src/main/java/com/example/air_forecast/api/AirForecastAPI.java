package com.example.air_forecast.api;

import com.example.air_forecast.model.AirDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AirForecastAPI {

    String URL = "https://api.weatherbit.io/v2.0/";

    public final static String API_key = "dba29d449897449e9f73f912b98e5681";


    @Headers("Content-Type: application/json")
    @GET("forecast/airquality?city=Skopje&country=MK&key=dba29d449897449e9f73f912b98e5681")
    Call<AirDetails> getDetails();

}

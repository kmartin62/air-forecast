package com.example.air_forecast.api;

import com.example.air_forecast.model.AirDetails;
import com.example.air_forecast.model.WeatherDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WeatherForecastAPI {

    @Headers("Content-Type: application/json")
    @GET("forecast/daily")
    Call<WeatherDetails> getDetails(@Query("city") String city,
                                    @Query("country") String country,
                                    @Query("key") String key);
}

package com.example.air_forecast.api;

import com.example.air_forecast.model.AirDetails;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AirForecastAPI {



//    public final static String API_key = "dba29d449897449e9f73f912b98e5681";

//    forecast/airquality?city=Skopje&country=MK&key=dba29d449897449e9f73f912b98e5681


    @Headers("Content-Type: application/json")
    @GET("forecast/airquality")
    Call<AirDetails> getDetails(@Query("city") String city,
                                @Query("country") String country,
                                @Query("key") String key);

}

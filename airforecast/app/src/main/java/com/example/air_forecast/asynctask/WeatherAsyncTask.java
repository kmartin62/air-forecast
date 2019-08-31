package com.example.air_forecast.asynctask;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.air_forecast.api.WeatherForecastAPI;
import com.example.air_forecast.constants.Constants;
import com.example.air_forecast.model.WeatherData;
import com.example.air_forecast.model.WeatherDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAsyncTask extends AsyncTask<Void, Void, Void> {

    private DatabaseReference reference;

    private String city;

    public WeatherAsyncTask(String city) {
        this.city = city;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        reference = FirebaseDatabase.getInstance().getReference().child(city+"W");

        final WeatherData weatherDataObject = new WeatherData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherForecastAPI weatherForecastAPI = retrofit.create(WeatherForecastAPI.class);

        Call<WeatherDetails> weatherDetailsCall = weatherForecastAPI.getDetails(city, Constants.getCountry(), Constants.getApiKey());

        weatherDetailsCall.enqueue(new Callback<WeatherDetails>() {
            @Override
            public void onResponse(Call<WeatherDetails> call, Response<WeatherDetails> response) {
                Log.d("Tag from Weather API", response.body().getCity_name());
                ArrayList<WeatherData> weatherData = response.body().getData();
                for(int i = 0; i < 5; i ++) {

                    weatherDataObject.setUv(weatherData.get(i).getUv());
                    weatherDataObject.setWind_cdir(weatherData.get(i).getWind_cdir());
                    weatherDataObject.setWind_spd(weatherData.get(i).getWind_spd());
                    weatherDataObject.setPres(weatherData.get(i).getPres());
                    weatherDataObject.setTemp(weatherData.get(i).getTemp());

                    reference.child(weatherData.get(i).getDatetime()).setValue(weatherDataObject);

                    Log.d("OnResponse", "onResponse: \n" +
                            "wind direction: " + weatherData.get(i).getWind_cdir() + "\n" +
                            "wind speed: " + weatherData.get(i).getWind_spd() + "\n" +
                            "uv: " + weatherData.get(i).getUv());


                }

            }

            @Override
            public void onFailure(Call<WeatherDetails> call, Throwable t) {
                Log.e("ERROR", t.getMessage());

            }
        });

        return null;
    }
}

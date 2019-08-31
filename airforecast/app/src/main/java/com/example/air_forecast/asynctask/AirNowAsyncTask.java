package com.example.air_forecast.asynctask;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.air_forecast.api.AirForecastAPI;
import com.example.air_forecast.constants.Constants;
import com.example.air_forecast.fragments.HomeFragment;
import com.example.air_forecast.model.AirDataNow;
import com.example.air_forecast.model.AirDetailsNow;
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

public class AirNowAsyncTask extends AsyncTask<Void, Void, Void> {

    private String TAG = "AIRNOW";

    private DatabaseReference reference;

    private String city;

    public AirNowAsyncTask(String city) {
        this.city = city;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        reference = FirebaseDatabase.getInstance().getReference().child(city+"N");

        final AirDataNow airDataNowObject = new AirDataNow();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AirForecastAPI airForecastAPI = retrofit.create(AirForecastAPI.class);
        Call<AirDetailsNow> call = airForecastAPI.getDetailsNow(HomeFragment.sharedCity, Constants.getCountry(), Constants.getApiKey());

        call.enqueue(new Callback<AirDetailsNow>() {
            @Override
            public void onResponse(Call<AirDetailsNow> call, Response<AirDetailsNow> response) {
                Log.d(TAG, "onResponse: Server responses " + response.toString());
                Log.d(TAG, "onResponse: received informations " + response.body().toString());

                ArrayList<AirDataNow> airDataNows = response.body().getData();

                airDataNowObject.setAqi(airDataNows.get(0).getAqi());
                airDataNowObject.setPm10(airDataNows.get(0).getPm10());
                airDataNowObject.setPm25(airDataNows.get(0).getPm25());

                reference.child(getKey()).setValue(airDataNowObject);
            }

            @Override
            public void onFailure(Call<AirDetailsNow> call, Throwable t) {
                Log.e(TAG, "Something went wrong " + t.getMessage());
            }
        });

        return null;
    }

    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));



        String[] h = date.format(currentLocalTime).split(" ");
        String g = h[0]+":00";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(calendar.getTime());

        return strDate + "T" + g;
    }
}

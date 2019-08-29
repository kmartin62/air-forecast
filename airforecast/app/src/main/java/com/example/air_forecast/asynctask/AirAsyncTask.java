package com.example.air_forecast.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.example.air_forecast.api.AirForecastAPI;
import com.example.air_forecast.constants.Constants;
import com.example.air_forecast.model.AirData;
import com.example.air_forecast.model.AirDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AirAsyncTask extends AsyncTask<Void, Void, Void> {

    private String city;

    public AirAsyncTask(String city) {
        this.city = city;
    }

    private static final String TAG = "AirRetrofit";

    private DatabaseReference reference;

    @Override
    protected Void doInBackground(Void... voids) {

        reference = FirebaseDatabase.getInstance().getReference().child(city);
        final AirData airDataObject = new AirData();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.getURL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AirForecastAPI airForecastAPI = retrofit.create(AirForecastAPI.class);
        Call<AirDetails> call = airForecastAPI.getDetails(city, Constants.getCountry(), Constants.getApiKey());

        call.enqueue(new Callback<AirDetails>() {
            @Override
            public void onResponse(Call<AirDetails> call, Response<AirDetails> response) {
                Log.d(TAG, "onResponse: Server responses " + response.toString());
                Log.d(TAG, "onResponse: received informations " + response.body().toString());

                ArrayList<AirData> airData = response.body().getData();
                for (int i = 0; i < 30; i++) {
                    airDataObject.setAqi(airData.get(i).getAqi());
                    airDataObject.setPm10(airData.get(i).getPm10());
                    airDataObject.setPm25(airData.get(i).getPm25());
                    Log.d(TAG, "onResponse: \n" +
                            "aqi: " + airData.get(i).getAqi() + "\n" +
                            "pm10: " + airData.get(i).getPm10() + "\n" +
                            "pm25: " + airData.get(i).getPm25());
                    String key = airData.get(i).getTimestamp_local();
                    reference.child(key).setValue(airDataObject);
                }

            }

            @Override
            public void onFailure(Call<AirDetails> call, Throwable t) {
                Log.e(TAG, "Something went wrong " + t.getMessage());
            }
        });
        return null;
    }
}

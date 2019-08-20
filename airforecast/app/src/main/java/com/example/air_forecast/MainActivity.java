package com.example.air_forecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.air_forecast.api.AirForecastAPI;
import com.example.air_forecast.asynctask.AirAsyncTask;
import com.example.air_forecast.model.AirData;
import com.example.air_forecast.model.AirDetails;
//import com.example.air_forecast.retrofit.AirRetrofit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    TextView txtView;
    DatabaseReference reference;
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = findViewById(R.id.txtView);

//        reference = FirebaseDatabase.getInstance().getReference().child("AirData");

//        AirRetrofit airRetrofit = new AirRetrofit();
//
//        airRetrofit.getRequest();

        AirAsyncTask airAsyncTask = new AirAsyncTask("Skopje");


        airAsyncTask.execute();



    }


}

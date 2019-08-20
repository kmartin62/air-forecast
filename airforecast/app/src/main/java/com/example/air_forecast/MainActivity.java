package com.example.air_forecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.air_forecast.asynctask.AirAsyncTask;


public class MainActivity extends AppCompatActivity {


    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = findViewById(R.id.txtView);

        AirAsyncTask airAsyncTask = new AirAsyncTask("Skopje");

        airAsyncTask.execute();



    }


}

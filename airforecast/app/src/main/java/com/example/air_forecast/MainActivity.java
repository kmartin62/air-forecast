package com.example.air_forecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_forecast.asynctask.AirAsyncTask;
import com.example.air_forecast.service.MyService;


public class MainActivity extends AppCompatActivity {


    TextView txtView;
    Spinner dynamicSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = findViewById(R.id.txtView);

        dynamicSpinner = findViewById(R.id.spinner);

        String[] cities = new String[] {"Skopje", "Veles", "Strumica"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtView.setText(parent.getItemAtPosition(position).toString());
                AirAsyncTask airAsyncTask = new AirAsyncTask(parent.getItemAtPosition(position).toString());

                airAsyncTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
//        serviceIntent.putExtra("City", "Skopje");
//
//        startService(new Intent(MainActivity.this, MyService.class));






    }


}

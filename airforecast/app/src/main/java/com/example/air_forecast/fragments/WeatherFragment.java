package com.example.air_forecast.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_forecast.R;
import com.example.air_forecast.firebase.WeatherForecastRetrieve;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class WeatherFragment extends Fragment {

    private static String[] items = new String[]{"uv", "wind_cdir", "wind_spd"};
    private ArrayAdapter<String> adapter;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.fragment_weather, container, false);
        final WeatherForecastRetrieve weatherForecastRetrieve = new WeatherForecastRetrieve();

        final TextView txtView = myInflatedView.findViewById(R.id.weather_text);
//        txtView.setText(getKey());
        spinner = myInflatedView.findViewById(R.id.spinner2);

        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, items);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weatherForecastRetrieve.retrieveData(HomeFragment.sharedCity, getKey(), (String) parent.getItemAtPosition(position), txtView, getActivity());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return myInflatedView;
    }

    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime());
    }
}

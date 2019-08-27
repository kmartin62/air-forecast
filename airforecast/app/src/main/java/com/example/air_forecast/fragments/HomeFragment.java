package com.example.air_forecast.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_forecast.R;
import com.example.air_forecast.api.WeatherForecastAPI;
import com.example.air_forecast.asynctask.WeatherAsyncTask;
import com.example.air_forecast.firebase.AirForecastRetrieve;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    public static String sharedCity;
    private ArrayAdapter<String> adapter;
    private Spinner dropDown;
    private static String[] items = new String[]{"Skopje", "Veles", "Strumica", "Radovis"};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View myInflatedView = inflater.inflate(R.layout.fragment_home, container, false);

        final AirForecastRetrieve airForecastRetrieve = new AirForecastRetrieve();
        final TextView txtViewAqi = myInflatedView.findViewById(R.id.home_text);
        final TextView txtViewPm10 = myInflatedView.findViewById(R.id.home_text2);
        final TextView txtViewPm25 = myInflatedView.findViewById(R.id.home_text3);

        dropDown = myInflatedView.findViewById(R.id.spinner);

        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, items);

        dropDown.setAdapter(adapter);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedCity = parent.getItemAtPosition(position).toString();
                airForecastRetrieve.retrieveData(sharedCity, getKey(), txtViewAqi, txtViewPm10, txtViewPm25, getActivity());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something
            }
        });




        return myInflatedView;
    }

    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));



        String[] h = date.format(currentLocalTime).split(" ");
        String g = h[0]+":00";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(calendar.getTime());

        return strDate + "T" + g;
    }

    @Override
    public void onStart() {

//        if(!connectedToNetwork) {
//            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_SHORT).show();
//        }

        super.onStart();
    }

    @Override
    public void onStop() {
//        Toast.makeText(getActivity(), "OnStop started", Toast.LENGTH_SHORT).show();

        super.onStop();
    }
}

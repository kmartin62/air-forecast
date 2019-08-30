package com.example.air_forecast.fragments;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_forecast.R;
import com.example.air_forecast.firebase.WeatherForecastRetrieve;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

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

//        CircularSeekBar circularSeekBar = myInflatedView.findViewById(R.id.seekBar);

        final ArcSeekBar seekBar = myInflatedView.findViewById(R.id.seekBarArc);
        seekBar.setClickable(false);
        seekBar.setEnabled(false);
        seekBar.setMaxProgress(100);

        ValueAnimator animator2 = ValueAnimator.ofInt(0, 50);
        animator2.setDuration(2000);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                seekBar.setProgress(currentValue);

            }
        });
        animator2.start();

//        circularSeekBar.setProgressTextFormat("###,###,##0,00");

//        circularSeekBar.setRingColor(Color.GREEN);
//        circularSeekBar.setProgress(45);

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

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

import static com.example.air_forecast.fragments.HomeFragment.sharedCity;

public class WeatherFragment extends Fragment {

    private TextView txtDate;
    private TextView txtHour;

    private TextView txtComfort;
    private TextView txtWind;

    private TextView txtPres;
    private TextView txtTemp;

    private TextView txtUv;
    private TextView txtDirection;
    private TextView txtSpeed;

    private TextView txtInfo;

    private ArcSeekBar seekBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.fragment_weather, container, false);
        final WeatherForecastRetrieve weatherForecastRetrieve = new WeatherForecastRetrieve();

        txtHour = myInflatedView.findViewById(R.id.textHour);
        txtDate = myInflatedView.findViewById(R.id.textDate);
        txtPres = myInflatedView.findViewById(R.id.txtPressure);
        txtTemp = myInflatedView.findViewById(R.id.txtTemp);
        txtUv = myInflatedView.findViewById(R.id.txtUv);
        txtDirection = myInflatedView.findViewById(R.id.txtDirection);
        txtSpeed = myInflatedView.findViewById(R.id.txtSpeed);

        txtComfort = myInflatedView.findViewById(R.id.textComfort);
        txtWind = myInflatedView.findViewById(R.id.textWind);

        txtInfo = myInflatedView.findViewById(R.id.txtInfo);

        seekBar = myInflatedView.findViewById(R.id.seekBarArcWeather);

        init();


        weatherForecastRetrieve.retrieveData(sharedCity, getKey(), txtHour,
                txtDate, txtPres, txtTemp, txtUv, txtDirection, txtSpeed, seekBar, getActivity());


        return myInflatedView;
    }

    private void init(){
        txtInfo.setText("*информации за " + sharedCity);

        txtComfort.setText("НИВО НА КОМФОРТ");
        txtWind.setText("НИВО НА ВЕТEР");

        seekBar.setEnabled(false);
        seekBar.setMaxProgress(23);
    }
    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(calendar.getTime());
    }
}

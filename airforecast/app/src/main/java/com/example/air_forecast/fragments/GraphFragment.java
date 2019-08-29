package com.example.air_forecast.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.air_forecast.R;
import com.example.air_forecast.firebase.AirForecastRetrieve;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GraphFragment extends Fragment {

    private BarChart barChart;
    private Spinner spinner;
    private static String[] items = new String[]{"aqi", "pm10", "pm25"};
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_graph, container, false);

        final AirForecastRetrieve airForecastRetrieve = new AirForecastRetrieve();

        barChart = myInflatedView.findViewById(R.id.barChart);
        spinner = myInflatedView.findViewById(R.id.spinner3);
        final Description description = new Description();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);

        barChart.setDescription(description);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                airForecastRetrieve.checkIfExists(parent.getItemAtPosition(position).toString(), getKey(), getActivity());
                description.setText(parent.getItemAtPosition(position).toString().toUpperCase());
                airForecastRetrieve.stringArray(barChart, parent.getItemAtPosition(position).toString());
                barChart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return myInflatedView;
    }

    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

        String[] h = date.format(currentLocalTime).split(" ");

        return h[0] + "T" + h[1].split(" ")[0] + ":00";
    }
}

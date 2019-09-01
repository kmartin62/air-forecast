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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.air_forecast.R;
import com.example.air_forecast.firebase.AirForecastRetrieve;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
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
    private boolean chartBoolean = false;
    private LineChart lineChart;
    private Spinner spinner;
    private static String[] items = new String[]{"aqi", "pm10", "pm25"};
    private ArrayAdapter<String> adapter;
    private Button btnBar;
    private Button btnLine;
    private String parameter;
    private Description description;
    private Description lineDescription;
    private AirForecastRetrieve airForecastRetrieve = new AirForecastRetrieve();;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_graph, container, false);

        btnBar = myInflatedView.findViewById(R.id.btnBar);
        btnLine = myInflatedView.findViewById(R.id.btnLine);

        barChart = myInflatedView.findViewById(R.id.barChart);
        lineChart = myInflatedView.findViewById(R.id.lineChart);
        spinner = myInflatedView.findViewById(R.id.spinner3);
        description = new Description();
        lineDescription = new Description();

        adapter = new ArrayAdapter<>(getActivity(), R.layout.my_spinner, items);
        spinner.setAdapter(adapter);

        barChart.setDescription(description);
        lineChart.setDescription(lineDescription);

        btnBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barChartClick();
            }
        });

        btnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChartClick();
            }
        });

        return myInflatedView;
    }

    private void barChartClick(){
//        barChart.setActivated(false);
        lineChart.setVisibility(View.INVISIBLE);
        airForecastRetrieve.drawBarChart(barChart, parameter);
        chartBoolean = false;
    }

    private void lineChartClick(){
        barChart.setVisibility(View.INVISIBLE);
        airForecastRetrieve.drawLineChart(lineChart, parameter);
        chartBoolean = true;
    }

    @Override
    public void onStart() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parameter = parent.getItemAtPosition(position).toString();
                if(!chartBoolean) {
                    airForecastRetrieve.drawBarChart(barChart, parent.getItemAtPosition(position).toString());
//                    airForecastRetrieve.checkIfExists(getKey7(), getActivity(), barChart, parent.getItemAtPosition(position).toString());
                    description.setText(parent.getItemAtPosition(position).toString().toUpperCase());
                }
                else {
                    airForecastRetrieve.drawLineChart(lineChart, parent.getItemAtPosition(position).toString());
//                    airForecastRetrieve.checkIfExists(getKey7(), getActivity(), lineChart, parent.getItemAtPosition(position).toString());
                    lineDescription.setText(parent.getItemAtPosition(position).toString().toUpperCase());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        barChart.setActivated(false);
        lineChart.setActivated(false);
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

    private String getKey7(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

        String[] h = date.format(currentLocalTime).split(" ");

        return h[0] + "T" + h[1].split(" ")[0] + ":00";
    }
}

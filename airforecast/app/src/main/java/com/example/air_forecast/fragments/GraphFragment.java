package com.example.air_forecast.fragments;

import android.graphics.Color;
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

import com.example.air_forecast.R;
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

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    private BarChart barChart;
    private Spinner spinner;
    private static String[] items = new String[]{"aqi", "pm10", "pm25"};
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.fragment_graph, container, false);

        barChart = myInflatedView.findViewById(R.id.barChart);
        spinner = myInflatedView.findViewById(R.id.spinner3);

        List<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(1, 10));
        barEntries.add(new BarEntry(2, 12));
        barEntries.add(new BarEntry(3, 15));
        barEntries.add(new BarEntry(4, 400));
        barEntries.add(new BarEntry(5, 20));
        barEntries.add(new BarEntry(6, 22));
        barEntries.add(new BarEntry(7, 5));


        BarDataSet barDataSet = new BarDataSet(barEntries, "Загадување за секој час");

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        barChart.setScaleEnabled(false);
        barChart.setVisibility(View.VISIBLE);
        barChart.animateY(5000);
        barChart.setData(barData);
        barChart.setFitBars(true);

        final Description description = new Description();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);

        barChart.setDescription(description);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);

        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        String[] hours = new String[]{"13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"};

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(hours));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                description.setText(parent.getItemAtPosition(position).toString().toUpperCase());
                barChart.invalidate();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return myInflatedView;
    }
}

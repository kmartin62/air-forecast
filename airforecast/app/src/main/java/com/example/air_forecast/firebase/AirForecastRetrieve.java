package com.example.air_forecast.firebase;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.air_forecast.R;
import com.example.air_forecast.service.AirJobScheduler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import static com.example.air_forecast.fragments.HomeFragment.sharedCity;

public class AirForecastRetrieve {

    private HashMap<String, String> hashMap = new HashMap<>();

    public void drawBarChart(final BarChart barChart, final String param){

        hashMap.put("Skopje", "Скопје");
        hashMap.put("Veles", "Велес");
        hashMap.put("Strumica", "Струмица");
        hashMap.put("Radovis", "Радовиш");

        final ArrayList<String> lista = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(sharedCity);
        final int[] i = {0};

        reference.limitToLast(7).addValueEventListener(new ValueEventListener() {
            List<BarEntry> barEntries = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot c : dataSnapshot.getChildren()) {
                    lista.add(c.child(param).getValue().toString());
                }

                for(int i = 0; i < 7; i ++) {
                    barEntries.add(new BarEntry(i+1, Float.parseFloat(lista.get(i))));

                }
                Log.d("DrawBarChart", String.valueOf(i[0]++));
                BarDataSet barDataSet = new BarDataSet(barEntries, "Загадување за секој час за " + hashMap.get(sharedCity));

                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.9f);

                barChart.setScaleEnabled(false);
                barChart.animateY(1000);
                barChart.setData(barData);
                barChart.setFitBars(true);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);

                xAxis.setCenterAxisLabels(true);
                xAxis.setEnabled(true);
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


                String[] hours = hourLabers();

                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(hours));

                barChart.setVisibility(View.VISIBLE);

                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void drawLineChart(final LineChart lineChart, final String param){
        hashMap.put("Skopje", "Скопје");
        hashMap.put("Veles", "Велес");
        hashMap.put("Strumica", "Струмица");
        hashMap.put("Radovis", "Радовиш");

        final ArrayList<String> lista = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(sharedCity);

        reference.limitToLast(7).addValueEventListener(new ValueEventListener() {
            List<Entry> entries = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot c : dataSnapshot.getChildren()) {
                    lista.add(c.child(param).getValue().toString());
                }

                for(int i = 0; i < 7; i ++) {
                    entries.add(new BarEntry(i+1, Float.parseFloat(lista.get(i))));

                }

                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(false);

                LineDataSet lineDataSet = new LineDataSet(entries, "Загадување за " + hashMap.get(sharedCity));
                lineDataSet.setFillAlpha(110);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);

                LineData lineData = new LineData(dataSets);

                lineChart.setData(lineData);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setGranularity(1f);

                xAxis.setCenterAxisLabels(true);
                xAxis.setEnabled(true);
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


                String[] hours = hourLabers();

                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(hours));

                lineChart.setVisibility(View.VISIBLE);

                lineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String[] hourLabers(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        String[] keys = new String[7];
        for(int i = 0; i < 7; i ++) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Date currentLocalTime = calendar.getTime();
            @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:'00' a");
            date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

            String[] h = date.format(currentLocalTime).split(" ");
            String g = h[0];
            keys[i] = g;
        }

        return keys;
    }

    public void checkIfExists(String key, final Activity activity, final BarChart barChart, final String param){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(sharedCity).child(key);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    scheduleJob(activity);
                }
                else {
                    drawBarChart(barChart, param);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkIfExists(String key, final Activity activity, final LineChart lineChart, final String param){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(sharedCity).child(key);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    scheduleJob(activity);
                }
                else {
                    drawLineChart(lineChart, param);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void scheduleJob(Activity activity) {
        ComponentName componentName = new ComponentName(Objects.requireNonNull(activity), AirJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1456759814, componentName)
                .setPersisted(true)
                .setPeriodic(1000 * 60 * 15) //15 minuti
                .build();

        JobScheduler jobScheduler = (JobScheduler) activity.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if(resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("AA", "Successfully");
        }
    }


}

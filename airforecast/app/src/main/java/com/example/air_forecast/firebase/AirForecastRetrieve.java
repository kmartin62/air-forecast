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

    public void retrieveData(final String city, String key, final TextView textAqi, final TextView pm10, final TextView pm25,
                             final Activity activity, final ArcSeekBar arcSeekBarAqi, final ArcSeekBar arcSeekBarPm10,
                             final ArcSeekBar arcSeekBarPm25) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city).child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    scheduleJob(activity);
                }
                else {
                    String aqi = Objects.requireNonNull(dataSnapshot.child("aqi").getValue()).toString();
                    final int n = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));

                    ValueAnimator animator = ValueAnimator.ofInt(0, n);
                    animator.setDuration(2000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());

                            textAqi.setText(animation.getAnimatedValue().toString());
                            textAqi.setTextColor(Color.WHITE);


                            if(currentValue <= 100) {
                                arcSeekBarAqi.setProgress(currentValue);
                            }

                            if(currentValue < 50 && currentValue > 0) {
                                arcSeekBarAqi.setProgressColor(Color.GREEN);
                            }
                            if(currentValue < 100 && currentValue > 51) {
                                arcSeekBarAqi.setProgressColor(Color.YELLOW);
                            }

                            if(currentValue < 150 && currentValue > 101) {
                                arcSeekBarAqi.setProgressColor(Color.parseColor("#FF4500"));
                            }

                            if(currentValue < 200 && currentValue > 151) {
                                arcSeekBarAqi.setProgressColor(Color.RED);
                            }

                            if(currentValue < 300 && currentValue > 201) {
                                arcSeekBarAqi.setProgressColor(Color.parseColor("#551A8B")); //purple
                            }

                            if(currentValue < 500 && currentValue > 301) {
                                arcSeekBarAqi.setProgressColor(Color.parseColor("#800000")); //maroon

                            }
                        }
                    });
                    animator.start();


                    aqi = Objects.requireNonNull(dataSnapshot.child("pm10").getValue()).toString();
                    final int m = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));

                    ValueAnimator animator1 = ValueAnimator.ofInt(0, m);
                    animator1.setDuration(2000);
                    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            pm10.setTextColor(Color.WHITE);
                            int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                            pm10.setText(animation.getAnimatedValue().toString());
                            if(currentValue <= 50) {
                                arcSeekBarPm10.setProgress(currentValue);
                            }
                            if(currentValue < 20 && currentValue > 0) {
                                arcSeekBarPm10.setProgressColor(Color.GREEN);
                            }
                            if(currentValue < 50 && currentValue > 20) {
                                arcSeekBarPm10.setProgressColor(Color.YELLOW);
                            }

                            if(currentValue > 50) {
                                arcSeekBarPm10.setProgressColor(Color.parseColor("#800000"));

                            }
                        }
                    });
                    animator1.start();

                    aqi = Objects.requireNonNull(dataSnapshot.child("pm25").getValue()).toString();
                    final int b = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));

                    ValueAnimator animator2 = ValueAnimator.ofInt(0, b);
                    animator2.setDuration(2000);
                    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                            pm25.setText(animation.getAnimatedValue().toString());
                            if(currentValue <= 25) {
                                arcSeekBarPm25.setProgress(currentValue);
                            }

                            if(currentValue < 10 && currentValue > 0) {
                                arcSeekBarPm25.setProgressColor(Color.GREEN);
                            }
                            if(currentValue < 25 && currentValue > 10) {
                                arcSeekBarPm25.setProgressColor(Color.YELLOW);
                            }

                            if(currentValue > 25) {
                                arcSeekBarPm25.setProgressColor(Color.parseColor("#800000"));
                            }
                        }
                    });
                    animator2.start();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void drawBarChart(final BarChart barChart, final String param){

        hashMap.put("Skopje", "Скопје");
        hashMap.put("Veles", "Велес");
        hashMap.put("Strumica", "Струмица");
        hashMap.put("Radovis", "Радовиш");

        final ArrayList<String> lista = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(sharedCity);

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
                Log.d("ASASA", String.valueOf(lista.size()));
                Log.d("HHGHGHG", String.valueOf(barEntries.size()));
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

    public void checkIfExists(String key, final Activity activity){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(sharedCity).child(key);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    scheduleJob(activity);
                }
                else {
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void scheduleJob(Activity activity) {
        ComponentName componentName = new ComponentName(Objects.requireNonNull(activity), AirJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1456759824, componentName)
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

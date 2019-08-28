package com.example.air_forecast.firebase;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.air_forecast.service.AirJobScheduler;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AirForecastRetrieve {

    public void retrieveData(final String city, String key, final TextView textAqi, final TextView pm10, final TextView pm25,
                             final FrameLayout frame, final Activity activity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city).child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    scheduleJob(activity);
                }
                else {
                    String aqi = Objects.requireNonNull(dataSnapshot.child("aqi").getValue()).toString();
//                    textAqi.setText(aqi);
                    final int n = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));
//
//                    textAqi.setText(String.valueOf(n));
//
//                    Log.d("AIRFORECAST", aqi);

                    ValueAnimator animator = ValueAnimator.ofInt(0, n);
                    animator.setDuration(5000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                            textAqi.setText(animation.getAnimatedValue().toString());
                            if(currentValue < 50 && currentValue > 0) {
                                textAqi.setTextColor(Color.BLACK);
                                textAqi.setText("AQI: " + String.valueOf(currentValue));
                                frame.setBackgroundColor(Color.GREEN);
                            }
                            if(currentValue < 100 && currentValue > 51) {
                                textAqi.setTextColor(Color.BLACK);
                                textAqi.setText("AQI: " + String.valueOf(currentValue));
                                frame.setBackgroundColor(Color.YELLOW);
                            }

                            if(currentValue < 150 && currentValue > 101) {
                                textAqi.setTextColor(Color.WHITE);
                                textAqi.setText("AQI: " + String.valueOf(currentValue));
                                frame.setBackgroundColor(Color.parseColor("#FFA500"));
                            }
                        }
                    });
                    animator.start();
//
//                    ValueAnimator animator = ValueAnimator.ofInt(0, n);
//                    animator.setDuration(2000);
//                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            int currentValue = (Integer) animation.getAnimatedValue();
//                            Log.d("IMIN", String.valueOf(currentValue));
////                            textAqi.setText(currentValue);
//                            if(currentValue < 50 && currentValue > 0) {
//                                textAqi.setTextColor(Color.BLACK);
//                                textAqi.setText("AQI: " + String.valueOf(currentValue));
//                                frame.setBackgroundColor(Color.GREEN);
//                            }
//                            if(currentValue < 100 && currentValue > 51) {
//                                textAqi.setTextColor(Color.BLACK);
//                                textAqi.setText("AQI: " + String.valueOf(currentValue));
//                                frame.setBackgroundColor(Color.YELLOW);
//                            }
//                            if(currentValue < 150 && currentValue > 101) {
//                                textAqi.setTextColor(Color.WHITE);
//                                textAqi.setText("AQI: " + String.valueOf(currentValue));
//                                frame.setBackgroundColor(Color.parseColor("#FFA500"));
//                            }
//                        }
//                    });
//                    animator.start();
//
//
//                    aqi = Objects.requireNonNull(dataSnapshot.child("pm10").getValue()).toString();
//                    int m = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));
//                    ValueAnimator animator1 = ValueAnimator.ofInt(0, m);
//                    animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            int currentValue = (Integer) animation.getAnimatedValue();
//                            pm10.setText(String.valueOf(currentValue));
//                        }
//                    });
//                    animator1.setDuration(2000);
//                    animator1.start();
//
//                    aqi = Objects.requireNonNull(dataSnapshot.child("pm25").getValue()).toString();
//                    int b = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));
//                    ValueAnimator animator12 = ValueAnimator.ofInt(0, b);
//                    animator12.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            int currentValue = (Integer) animation.getAnimatedValue();
//                            pm25.setText(String.valueOf(currentValue));
//                        }
//                    });
//                    animator12.setDuration(2000);
//                    animator12.start();


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

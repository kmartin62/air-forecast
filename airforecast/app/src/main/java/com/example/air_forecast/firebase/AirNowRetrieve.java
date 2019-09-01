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
import android.widget.TextView;

import com.example.air_forecast.service.AirJobScheduler;
import com.example.air_forecast.service.AirNowJobScheduler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.util.Objects;

import static com.example.air_forecast.fragments.HomeFragment.sharedCity;

public class AirNowRetrieve {
    public void retrieveData(final String city, String key, final TextView textAqi, final TextView pm10, final TextView pm25,
                             final Activity activity, final ArcSeekBar arcSeekBarAqi, final ArcSeekBar arcSeekBarPm10,
                             final ArcSeekBar arcSeekBarPm25) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city+"N").child(key);

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

    private void scheduleJob(Activity activity) {
        ComponentName componentName = new ComponentName(Objects.requireNonNull(activity), AirNowJobScheduler.class);
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

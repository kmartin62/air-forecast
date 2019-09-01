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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.air_forecast.service.AirJobScheduler;
import com.example.air_forecast.service.WeatherJobScheduler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class WeatherForecastRetrieve {

    private static DecimalFormat df = new DecimalFormat("0.00");

    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));



        String[] h = date.format(currentLocalTime).split(" ");
        String g = h[0]+":00";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(calendar.getTime());

        return h[0].split(":")[0];
    }


    public void retrieveData(String city, final String key, final TextView txtHour, final TextView txtDate,
                             final TextView txtPres, final TextView txtTemp, final TextView txtUv,
                             final TextView txtDirection, final TextView txtSpeed,
                             final ArcSeekBar arcSeekBar, final Activity activity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city+"W").child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    scheduleJob(activity);
                }
                else {
                    txtDate.setText(key);
                    txtHour.setText(getKey()+":00 часот");
                    ValueAnimator animator2 = ValueAnimator.ofInt(0, Integer.parseInt(getKey()));
                    animator2.setDuration(2000);
                    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int currentValue = Integer.parseInt(animation.getAnimatedValue().toString());
                            arcSeekBar.setProgress(currentValue);
                            if((currentValue > 0 && currentValue < 6) || currentValue > 20){
                                arcSeekBar.setProgressColor(Color.BLACK);
                            }
                            else {
                                arcSeekBar.setProgressColor(Color.parseColor("#FF4500"));
                            }
                        }
                    });
                    animator2.start();

                    String elemenet = Objects.requireNonNull(dataSnapshot.child("pres").getValue()).toString();
                    final int n = Integer.parseInt(String.valueOf(elemenet.split("\\.")[0]));
                    txtPres.setText("Притисок на воздух: " + n + "hPa");

                    elemenet = Objects.requireNonNull(dataSnapshot.child("temp").getValue()).toString();
                    final int m = Integer.parseInt(String.valueOf(elemenet.split("\\.")[0]));
                    txtTemp.setText("Температура: " + m + "°C");

                    elemenet = Objects.requireNonNull(dataSnapshot.child("uv").getValue()).toString();
                    final int b = Integer.parseInt(String.valueOf(elemenet.split("\\.")[0]));
                    txtUv.setText("UV индекс: " + b);

                    elemenet = Objects.requireNonNull(dataSnapshot.child("wind_cdir").getValue()).toString();
                    txtDirection.setText("Насока: " + elemenet);

                    elemenet = Objects.requireNonNull(dataSnapshot.child("wind_spd").getValue()).toString();
                    float f = Float.parseFloat(elemenet);
                    f = Float.parseFloat(df.format(f));
                    txtSpeed.setText("Брзина: " + f + "m/s");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean tryParseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void scheduleJob(Activity activity) {
        ComponentName componentName = new ComponentName(Objects.requireNonNull(activity), WeatherJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1456759834, componentName)
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

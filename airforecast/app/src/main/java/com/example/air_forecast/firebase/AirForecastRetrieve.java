package com.example.air_forecast.firebase;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.air_forecast.service.AirJobScheduler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AirForecastRetrieve {

    public void retrieveData(final String city, String key, final TextView textAqi, final TextView pm10, final TextView pm25, final Activity activity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city).child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    scheduleJob(activity);
                }
                else {
                    String aqi = Objects.requireNonNull(dataSnapshot.child("aqi").getValue()).toString();
                    int n = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));
//
//                    textView.setText(String.valueOf(n));

                    Log.d("AIRFORECAST", aqi);

                    textAqi.setText(String.valueOf(n));

                    aqi = Objects.requireNonNull(dataSnapshot.child("pm10").getValue()).toString();
                    n = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));
                    pm10.setText(String.valueOf(n));

                    aqi = Objects.requireNonNull(dataSnapshot.child("pm25").getValue()).toString();
                    n = Integer.parseInt(String.valueOf(aqi.split("\\.")[0]));
                    pm25.setText(String.valueOf(n));


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

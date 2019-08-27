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
import com.example.air_forecast.service.WeatherJobScheduler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Objects;

public class WeatherForecastRetrieve {

    private static DecimalFormat df = new DecimalFormat("0.00");


    public void retrieveData(String city, String key, final String parameter, final TextView txtView, final Activity activity) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city+"W").child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    scheduleJob(activity);
                }
                else {
                    float f;

                    if(tryParseFloat((dataSnapshot.child(parameter).getValue().toString()))) {
                        f = Float.parseFloat(dataSnapshot.child(parameter).getValue().toString());
                        f = Float.parseFloat(df.format(f));
                        txtView.setText(String.valueOf(f));
                    }
                    else {
                        txtView.setText(dataSnapshot.child(parameter).getValue().toString());
                    }

//                    Log.d("WEATHERRETRIEVE", String.valueOf(f));
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

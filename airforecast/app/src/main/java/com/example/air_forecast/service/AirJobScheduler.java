package com.example.air_forecast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.air_forecast.asynctask.AirAsyncTask;
import com.example.air_forecast.fragments.HomeFragment;

import static com.example.air_forecast.MainActivity.isClicked;

public class AirJobScheduler extends JobService {

    private static final String TAG = "AirJobScheduler";

    private String city;

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    private void doBackgroundWork(final JobParameters params) {

//        Toast.makeText(AirJobScheduler.this, "City from JobScheduler:" + HomeFragment.sharedCity, Toast.LENGTH_SHORT).show();
//        isClicked = false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                AirAsyncTask airAsyncTask = new AirAsyncTask(HomeFragment.sharedCity);
                airAsyncTask.execute();

                    jobFinished(params, true);

//                Log.d(TAG, "Finished");
//                isClicked = true;
//                Log.d("isClicked from Service", String.valueOf(isClicked));
            }
        }).start();
    }
}

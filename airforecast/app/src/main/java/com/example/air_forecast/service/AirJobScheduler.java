package com.example.air_forecast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.air_forecast.asynctask.AirAsyncTask;
import com.example.air_forecast.asynctask.AirNowAsyncTask;
import com.example.air_forecast.asynctask.WeatherAsyncTask;
import com.example.air_forecast.fragments.HomeFragment;

import static com.example.air_forecast.MainActivity.isClicked;
import static com.example.air_forecast.fragments.HomeFragment.sharedCity;

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


        new Thread(new Runnable() {
            @Override
            public void run() {

                new AirNowAsyncTask(sharedCity).execute();

                AirAsyncTask airAsyncTask = new AirAsyncTask(sharedCity);
                airAsyncTask.execute();

                jobFinished(params, true);

            }
        }).start();
    }
}

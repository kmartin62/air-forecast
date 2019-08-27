package com.example.air_forecast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.air_forecast.asynctask.WeatherAsyncTask;
import com.example.air_forecast.fragments.HomeFragment;

public class WeatherJobScheduler extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        doBackgroundWork(params);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void doBackgroundWork(final JobParameters parameters) {

       new Thread(new Runnable() {
           @Override
           public void run() {
               WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask(HomeFragment.sharedCity);
               weatherAsyncTask.execute();

               jobFinished(parameters, true);
           }
       }).start();
    }
}

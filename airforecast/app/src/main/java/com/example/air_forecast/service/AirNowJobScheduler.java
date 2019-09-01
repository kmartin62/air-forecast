package com.example.air_forecast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.air_forecast.asynctask.AirNowAsyncTask;
import com.example.air_forecast.fragments.HomeFragment;

public class AirNowJobScheduler extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
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


                AirNowAsyncTask airNowAsyncTask = new AirNowAsyncTask(HomeFragment.sharedCity);
                airNowAsyncTask.execute();

                jobFinished(params, true);

            }
        }).start();
    }
}

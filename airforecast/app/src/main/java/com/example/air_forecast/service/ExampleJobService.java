package com.example.air_forecast.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import static com.example.air_forecast.MainActivity.isClicked;

public class ExampleJobService extends JobService {

    private static final String TAG = "ExampleJobService";


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

        isClicked = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 5; i ++) {
                    Log.d(TAG, "run " + i);

                    try {
                        Thread.sleep(1000); //sleep 1 sec
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    jobFinished(params, true);

                }
                Log.d(TAG, "Finished");
                isClicked = true;
                Log.d("isClicked from Service", String.valueOf(isClicked));
            }
        }).start();
    }
}

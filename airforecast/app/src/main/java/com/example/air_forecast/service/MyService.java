package com.example.air_forecast.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.air_forecast.asynctask.AirAsyncTask;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        final Bundle extras = intent.getExtras();
        final Handler handler = new Handler();
        final Runnable runb = new Runnable()
        {
            public void run()
            {
                AirAsyncTask airAsyncTask = new AirAsyncTask("Skopje");

                airAsyncTask.execute();

                Toast.makeText(getApplicationContext(), " Service Started", Toast.LENGTH_LONG).show();
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runb, 0);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


}

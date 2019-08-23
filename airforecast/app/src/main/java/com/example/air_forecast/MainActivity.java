package com.example.air_forecast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_forecast.broadcast.NetworkChangeReceiver;
import com.example.air_forecast.fragments.GraphFragment;
import com.example.air_forecast.fragments.HomeFragment;
import com.example.air_forecast.fragments.WeatherFragment;
import com.example.air_forecast.service.AirJobScheduler;


public class MainActivity extends AppCompatActivity {


    public static boolean connectedToNetwork = true;
    public static boolean isClicked = false;
    private BroadcastReceiver broadcastReceiver;
    BottomNavigationView bottomNav;

    public static void dialog(boolean value) {
        if(value) {
            connectedToNetwork = true;
            Log.d("Boolean", String.valueOf(connectedToNetwork));
        }
        else {
            connectedToNetwork = false;
            Log.d("Boolean", String.valueOf(connectedToNetwork));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        broadcastReceiver = new NetworkChangeReceiver();

        registerNetworkBroadcastForNougat();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.nav_graph:
                    selectedFragment = new GraphFragment();
                    break;

                case R.id.nav_weather:
                    selectedFragment = new WeatherFragment();
                    break;

                    default:
                        selectedFragment = new WeatherFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }



    public void scheduleJob() {

        ComponentName componentName = new ComponentName(this, AirJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1456759824, componentName)
                .setPersisted(true)
                .setPeriodic(1000 * 60 * 15) //15 minuti
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if(resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("AA", "Successfully");
        }
    }


    @Override
    public void onBackPressed() {

        int selected = bottomNav.getSelectedItemId();
        MenuItem menuItem = bottomNav.getMenu().findItem(selected);

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                moveTaskToBack(true);
                break;

            case R.id.nav_graph:
                bottomNav.setSelectedItemId(R.id.nav_home);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_weather:
                bottomNav.setSelectedItemId(R.id.nav_home);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

        }


    }
}

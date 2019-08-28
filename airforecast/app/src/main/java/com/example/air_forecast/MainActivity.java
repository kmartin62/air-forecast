package com.example.air_forecast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.air_forecast.asynctask.WeatherAsyncTask;
import com.example.air_forecast.broadcast.NetworkChangeReceiver;
import com.example.air_forecast.firebase.AirForecastRetrieve;
import com.example.air_forecast.fragments.GraphFragment;
import com.example.air_forecast.fragments.HomeFragment;
import com.example.air_forecast.fragments.WeatherFragment;
import com.example.air_forecast.service.AirJobScheduler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {


    public static boolean connectedToNetwork;
    public static boolean isClicked;
    private BroadcastReceiver broadcastReceiver;
    private BottomNavigationView bottomNav;

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
        if(!isConnected(this)) {
            builder(this).show();
        }

        broadcastReceiver = new NetworkChangeReceiver();

        registerNetworkBroadcastForNougat();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);


        
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Skopje").child(getKey());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    scheduleJob();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        }
        else {
            return false;
        }

    }

    private AlertDialog.Builder builder(Context c) {
        AlertDialog.Builder build = new AlertDialog.Builder(c);
        build.setTitle("No internet connection");

        build.setPositiveButton("Connect to network", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });

        build.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        return build;
    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(Objects.requireNonNull(this), AirJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1456759824, componentName)
                .setPersisted(true)
                .setPeriodic(1000 * 60 * 15) //15 minuti
                .build();

        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if(resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("AA", "Successfully");
        }
    }

    private String getKey(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));



        String[] h = date.format(currentLocalTime).split(" ");
        String g = h[0]+":00";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(calendar.getTime());

        return strDate + "T" + g;
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
//        Toast.makeText(this, String.valueOf(connectedToNetwork), Toast.LENGTH_SHORT).show();

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

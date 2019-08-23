package com.example.air_forecast;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.air_forecast.broadcast.NetworkChangeReceiver;
import com.example.air_forecast.fragments.GraphFragment;
import com.example.air_forecast.fragments.HomeFragment;
import com.example.air_forecast.fragments.WeatherFragment;
import com.example.air_forecast.service.ExampleJobService;


public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static boolean connectedToNetwork;
    public static boolean isClicked = false;
    Intent serviceIntent;
    static TextView txtView;
    Spinner dynamicSpinner;
    private BroadcastReceiver broadcastReceiver;
    String[] cities;
    BottomNavigationView bottomNav;

    public static void dialog(boolean value) {
        if(value) {
            connectedToNetwork = true;
//            txtView.setText("Online");
            Log.d("Boolean", String.valueOf(connectedToNetwork));
        }
        else {
            connectedToNetwork = false;
//            txtView.setText("Offline");
            Log.d("Boolean", String.valueOf(connectedToNetwork));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();



//        Toast.makeText(this, String.valueOf(checkedId), Toast.LENGTH_SHORT).show();


        sharedPreferences = getSharedPreferences("mypref", 0);
        editor = sharedPreferences.edit();

//        txtView = findViewById(R.id.txtView);
        broadcastReceiver = new NetworkChangeReceiver();

        registerNetworkBroadcastForNougat();

//        dynamicSpinner = findViewById(R.id.spinner);

        Log.d("isClicked", String.valueOf(isClicked));

        cities = new String[] {"Skopje", "Veles", "Strumica"};

//        if(connectedToNetwork) {

        boolean b = sharedPreferences.getBoolean("isClicked", false);
//        Toast.makeText(this, String.valueOf(b), Toast.LENGTH_SHORT).show();

        if(!b) {
            scheduleJob();
        }
        else {
//            Toast.makeText(this, "Already clicked", Toast.LENGTH_SHORT).show();
        }
//        }
//        else {
//            Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
//        }





//        serviceIntent = new Intent(MainActivity.this, MyService.class);
//        serviceIntent.putExtra("City", "Skopje");
//
//        startService(new Intent(MainActivity.this, MyService.class));

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

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
//                        break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    @Override
    protected void onStart() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
//
//        dynamicSpinner.setAdapter(adapter);
//
//        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                txtView.setText(parent.getItemAtPosition(position).toString());
//
////                AirAsyncTask airAsyncTask = new AirAsyncTask(parent.getItemAtPosition(position).toString());
////
////                airAsyncTask.execute();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("onStop", "onDestroy called");
        isClicked = true;
        editor.putBoolean("isClicked", isClicked);
        editor.apply();
        Log.d("isClicked from onStop", String.valueOf(isClicked));
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy", "onDestroy called");
        isClicked = true;
        Log.d("isClicked from onDestro", String.valueOf(isClicked));
        super.onDestroy();
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

//    @Override
//    protected void onDestroy() {
//        Toast.makeText(this, "OnDestroy called", Toast.LENGTH_SHORT).show();
////        stopService(serviceIntent);
//        isClicked = true;
//        Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
//        unregisterNetworkChanges();
//        super.onDestroy();
//    }



    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
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

    public void scheduleTheJob(View view) {
        scheduleJob();
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

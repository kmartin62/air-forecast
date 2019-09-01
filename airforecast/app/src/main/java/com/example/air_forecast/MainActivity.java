package com.example.air_forecast;

import android.content.BroadcastReceiver;
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

import com.example.air_forecast.fragments.GraphFragment;
import com.example.air_forecast.fragments.HomeFragment;
import com.example.air_forecast.fragments.WeatherFragment;



public class MainActivity extends AppCompatActivity {


    public static boolean connectedToNetwork;
    public static boolean isClicked;
    private BroadcastReceiver broadcastReceiver;
    private BottomNavigationView bottomNav;
    private int itemId;

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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnNavigationItemSelectedListener(navListener);




        


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment selectedFragment = null;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int selected = menuItem.getItemId();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    if(bottomNav.getSelectedItemId() == selected) {
                        break;
                    }
                    else {
                        selectedFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    break;

                case R.id.nav_graph:
                    if(bottomNav.getSelectedItemId() == selected){
                        break;
                    }
                    selectedFragment = new GraphFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;

                case R.id.nav_weather:
                    if(bottomNav.getSelectedItemId() == selected){
                        break;
                    }
                    selectedFragment = new WeatherFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    break;

                    default:
                        selectedFragment = new HomeFragment();
            }


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

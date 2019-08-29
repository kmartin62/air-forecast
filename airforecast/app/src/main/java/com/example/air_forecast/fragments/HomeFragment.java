package com.example.air_forecast.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_forecast.R;
import com.example.air_forecast.firebase.AirForecastRetrieve;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private HashMap<String, String> hashMap;
    public static String sharedCity;
    private String myCity;
    private ArrayAdapter<String> adapter;
    private Spinner dropDown;
    private FrameLayout aqi_frame, pm10_frame, pm25_frame;
//    private static String[] items = new String[]{"Skopje", "Veles", "Strumica", "Radovis"};
    final AirForecastRetrieve airForecastRetrieve = new AirForecastRetrieve();

    private TextView txtViewAqi;
    private TextView txtViewPm10;
    private TextView txtViewPm25;
    private TextView text_aqi;
    private TextView text_pm10;
    private TextView text_pm25;
    private TextView info_text;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View myInflatedView = inflater.inflate(R.layout.fragment_home, container, false);

        if(!isConnected(getContext())) {
            builder(getContext()).show();
            return inflater.inflate(R.layout.offline, container, false);
        }


        hashMap = new HashMap<>();
        initMap();

        txtViewAqi = myInflatedView.findViewById(R.id.home_text);
        txtViewPm10 = myInflatedView.findViewById(R.id.home_text2);
        txtViewPm25 = myInflatedView.findViewById(R.id.home_text3);
        text_aqi = myInflatedView.findViewById(R.id.text_aqi);
        text_pm10 = myInflatedView.findViewById(R.id.text_pm10);
        text_pm25 = myInflatedView.findViewById(R.id.text_pm25);
        info_text = myInflatedView.findViewById(R.id.infoText);


        aqi_frame = myInflatedView.findViewById(R.id.frame_aqi);
        pm10_frame = myInflatedView.findViewById(R.id.frame_pm10);
        pm25_frame = myInflatedView.findViewById(R.id.frame_pm25);


        dropDown = myInflatedView.findViewById(R.id.spinner);

        return myInflatedView;
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
                getActivity().finish();
            }
        });

        return build;
    }

    private void initMap(){
        hashMap.put("Скопје", "Skopje");
        hashMap.put("Велес", "Veles");
        hashMap.put("Струмица", "Strumica");
        hashMap.put("Радовиш", "Radovis");
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

    @Override
    public void onStart() {

        if(isConnected(getContext())) {

            aqi_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info_text.setText(getString(R.string.aqiinfo));
                    info_text.setTextColor(Color.WHITE);
                }
            });

            pm10_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info_text.setText(getString(R.string.pm10info));
                    info_text.setTextColor(Color.WHITE);
                }
            });

            pm25_frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    info_text.setText(getString(R.string.pm25info));
                    info_text.setTextColor(Color.WHITE);
                }
            });

            init();

            dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    initFrames();
                    sharedCity = hashMap.get(parent.getItemAtPosition(position).toString());
                    myCity = sharedCity;
                    airForecastRetrieve.retrieveData(myCity, getKey(), txtViewAqi, txtViewPm10, txtViewPm25, aqi_frame,
                            pm10_frame, pm25_frame, getActivity());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    //do something
                }
            });

//        if(!connectedToNetwork) {
//            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_SHORT).show();
//        }

//        if(!connectedToNetwork) {
//            Toast.makeText(getActivity(), "Please check your network connection", Toast.LENGTH_SHORT).show();
//        }
        }

        super.onStart();
    }

    private void init(){
        Resources res = getResources();
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.my_spinner, res.getStringArray(R.array.cities));
        dropDown.setAdapter(adapter);

        aqi_frame.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_shape_green));
        pm10_frame.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_shape_green));
        pm25_frame.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_shape_green));


        text_aqi.setText("AQI");
        text_pm10.setText("PM10");
        text_pm25.setText("PM25");
    }

    private void initFrames(){
        aqi_frame.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_shape_green));
        pm10_frame.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_shape_green));
        pm25_frame.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_shape_green));

        txtViewAqi.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtViewPm10.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtViewPm25.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        txtViewAqi.setTextColor(Color.BLACK);
        txtViewPm10.setTextColor(Color.BLACK);
        txtViewPm25.setTextColor(Color.BLACK);
    }

    @Override
    public void onStop() {
//        Toast.makeText(getActivity(), "OnStop started", Toast.LENGTH_SHORT).show();
        myCity = sharedCity;

        super.onStop();
    }
}

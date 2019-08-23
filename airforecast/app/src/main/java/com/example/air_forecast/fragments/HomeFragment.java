package com.example.air_forecast.fragments;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.air_forecast.MainActivity.connectedToNetwork;
import com.example.air_forecast.R;
import com.example.air_forecast.service.AirJobScheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    public static String sharedCity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View myInflatedView = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView txtView = myInflatedView.findViewById(R.id.home_text);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        Date currentLocalTime = calendar.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat date = new SimpleDateFormat("HH:'00' a");
        date.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));

        String[] h = date.format(currentLocalTime).split(" ");
        String g = h[0]+":00";


        Toast.makeText(getActivity(), g, Toast.LENGTH_SHORT).show();

        Spinner dropDown = myInflatedView.findViewById(R.id.spinner);
        String[] items = new String[]{"Skopje", "Veles", "Strumica", "Radovis"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);

        dropDown.setAdapter(adapter);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtView.setText(parent.getItemAtPosition(position).toString());
                sharedCity = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getActivity(), sharedCity, Toast.LENGTH_SHORT).show();
                scheduleJob();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        if(connectedToNetwork) {
////            AirAsyncTask airAsyncTask = new AirAsyncTask("Skopje");
////            airAsyncTask.execute();
////            scheduleJob();
//        }
//        else {
//            //do something
//            txtView.setText("Offline");
//        }


        return myInflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void scheduleJob() {
        ComponentName componentName = new ComponentName(Objects.requireNonNull(getActivity()), AirJobScheduler.class);
        JobInfo jobInfo = new JobInfo.Builder(1456759824, componentName)
                .setPersisted(true)
                .setPeriodic(1000 * 60 * 15) //15 minuti
                .build();

        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if(resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("AA", "Successfully");
        }
    }
}

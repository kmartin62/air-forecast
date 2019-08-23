package com.example.air_forecast.firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.air_forecast.asynctask.AirAsyncTask;
import com.example.air_forecast.fragments.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AirForecastRetrieve {

    public boolean flag;

    public void retrieveData(final String city, String key, final String parameter, final TextView textView) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(city).child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Log.d("DATA RETRIEVED", "SUCCESSFULLY");

                String aqi = dataSnapshot.child(parameter).getValue().toString();
                if(aqi.length() > 5) {
                    textView.setText(aqi.substring(0, 5));
                }
                else {
                    textView.setText(aqi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

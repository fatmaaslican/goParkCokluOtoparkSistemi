package com.example.android.asliengintez;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CarParkInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Otopark Bilgileri");
        }

        final TextView tvName = (TextView) findViewById(R.id.tv_name);
        final TextView tvCapacity = (TextView) findViewById(R.id.tv_capacity);
        final TextView tvCarNumber = (TextView) findViewById(R.id.tv_car_number);
        final TextView tvHourlyFees = (TextView) findViewById(R.id.tv_hourly_fees);
        final TextView tvWorkingHours = (TextView) findViewById(R.id.tv_working_hours);
        final TextView tvDailyFees = (TextView) findViewById(R.id.tv_daily_fees);
        final TextView tvSubscriptionFees = (TextView) findViewById(R.id.tv_subscription_fees);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("carparkObj")) {
            String name = intent.getStringExtra("carparkObj");

            Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL).child("parks").child(name);

            firebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CarPark carPark = dataSnapshot.getValue(CarPark.class);

                    if (carPark != null) {
                        tvName.setText(carPark.getName());
                        tvCapacity.setText(String.valueOf(carPark.getCapacity()));
                        tvCarNumber.setText(String.valueOf(carPark.getCarNumber()));
                        tvHourlyFees.setText(String.valueOf(carPark.getHourlyFees()));
                        tvDailyFees.setText(String.valueOf(carPark.getDailyFees()));
                        tvSubscriptionFees.setText(String.valueOf(carPark.getSubscriptionFees()));
                        tvWorkingHours.setText(carPark.getWorkingHours());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
}

package com.android.travelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tickets extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com/Users");


    TextView name, email, phone, nameTour, vehicle ,totalPeople, totalPrice, tour_date, expiry_view;
    Button btnBack;
    AlertDialog dialog;


    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_NAME_TOUR = "name_tour";
    private static final String KEY_VEHICLE_TOUR = "vehicle_mode";
    private static final String KEY_COUNT_ITEMS = "count_items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        nameTour = findViewById(R.id.name_tour);
        vehicle = findViewById(R.id.vehicle_mode);
        totalPeople = findViewById(R.id.total_people);
        totalPrice = findViewById(R.id.total_price);
        btnBack = findViewById(R.id.btn_back);
        tour_date = findViewById(R.id.tour_date);
        expiry_view  = findViewById(R.id.expiry_tv);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tickets.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences localStore = getSharedPreferences("loginState", MODE_PRIVATE);
        String userName = localStore.getString("UserName", "NA");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameView = snapshot.child(userName).child("Name").getValue(String.class);
                String emailView = snapshot.child(userName).child("Email").getValue(String.class);
                String phoneView = snapshot.child(userName).child("Mobile_No").getValue(String.class);

                String nameTourView = snapshot.child(userName).child("Tickets_Details").child("Tour_Name").getValue(String.class);
                String vehicle_mode = snapshot.child(userName).child("Tickets_Details").child("Tour_Vehicle").getValue(String.class);
                String totalItemsView = snapshot.child(userName).child("Tickets_Details").child("Total_Tickets").getValue(String.class);
                String totalPriceView = snapshot.child(userName).child("Tickets_Details").child("Total_Tickect_Price").getValue(String.class);
                String tourDate = snapshot.child(userName).child("Tickets_Details").child("Tour_Date").getValue(String.class);

                name.setText(nameView);
                email.setText(emailView);
                phone.setText(phoneView);
                nameTour.setText(nameTourView);
                vehicle.setText(vehicle_mode);
                totalPeople.setText(totalItemsView);
                totalPrice.setText("Rs." + totalPriceView);
                tour_date.setText(tourDate);

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                Date currentDate = new Date();
                String curr_day_month_year = dateFormat.format(currentDate);

                // Tour Date is less than current date. (Tickect Expired)
                if(tourDate.compareTo(curr_day_month_year)<0){
                    expiry_view.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
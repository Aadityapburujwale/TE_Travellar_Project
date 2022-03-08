package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tickets extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com/Users");


    TextView name, email, phone, nameTour, totalPeople, totalPrice, tour_date, expiry_view;
    Button btnBack;
    AlertDialog dialog;

    SharedPreferences preferences;

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_NAME_TOUR = "name_tour";
    private static final String KEY_COUNT_ITEMS = "count_items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        nameTour = findViewById(R.id.name_tour);
        totalPeople = findViewById(R.id.total_people);
        totalPrice = findViewById(R.id.total_price);
        btnBack = findViewById(R.id.btn_back);
        tour_date = findViewById(R.id.tour_date);
        expiry_view  = findViewById(R.id.expiry_tv);

        preferences = getSharedPreferences("userInfo", 0);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tickets.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

//        String nameView = preferences.getString(KEY_NAME, null);
//        String emailView = preferences.getString(KEY_EMAIL, null);
//        String phoneView = preferences.getString(KEY_PHONE, null);
//
//        String nameTourView = preferences.getString(KEY_NAME_TOUR, null);
//        String totalItemsView = preferences.getString(KEY_COUNT_ITEMS, null);
//        String totalPriceView = preferences.getString(KEY_TOTAL_PRICE, null);

        SharedPreferences localStore = getSharedPreferences("loginState", MODE_PRIVATE);
        String userName = localStore.getString("UserName", "NA");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameView = snapshot.child(userName).child("Name").getValue(String.class);
                String emailView = snapshot.child(userName).child("Email").getValue(String.class);
                String phoneView = snapshot.child(userName).child("Mobile_No").getValue(String.class);

                String nameTourView = snapshot.child(userName).child("Tickets_Details").child("Tour_Name").getValue(String.class);
                String totalItemsView = snapshot.child(userName).child("Tickets_Details").child("Total_Tickets").getValue(String.class);
                String totalPriceView = snapshot.child(userName).child("Tickets_Details").child("Total_Tickect_Price").getValue(String.class);
                String tourDate = snapshot.child(userName).child("Tickets_Details").child("Tour_Date").getValue(String.class);

                name.setText(nameView);
                email.setText(emailView);
                phone.setText(phoneView);
                nameTour.setText(nameTourView);
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
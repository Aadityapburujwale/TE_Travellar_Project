package com.android.travelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Receipt extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com/Users");


    ImageView imgTour;
    TextView nameTour, totalPeople, priceTour, totalPrice, name, email, phone;
    Button btnConfirm;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        imgTour = findViewById(R.id.img_tour);
        nameTour = findViewById(R.id.name_tour);
        totalPeople = findViewById(R.id.total_people);
        priceTour = findViewById(R.id.price_tour);
        totalPrice = findViewById(R.id.total_price);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        btnConfirm = findViewById(R.id.btn_confirm);

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
                String tourImageURL = snapshot.child(userName).child("Tickets_Details").child("Tour_IMG_URL").getValue(String.class);

                name.setText(nameView);
                email.setText(emailView);
                phone.setText(phoneView);
                nameTour.setText(nameTourView);
                totalPeople.setText(totalItemsView);
                priceTour.setText("Rs."+totalPriceView);
                totalPrice.setText("Rs." + (Integer.parseInt(totalItemsView)*Integer.parseInt(totalPriceView)));
                Glide.with(Receipt.this).asBitmap().load(tourImageURL).into(imgTour);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(Receipt.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Message")
                        .setMessage("\nAre you sure booked this spot?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(Receipt.this, "Success Booked Ticket", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetDetailTour();
                                Toast.makeText(Receipt.this, "Fail Booked Ticket", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Receipt.this, Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    private void resetDetailTour(){

        SharedPreferences localStore = getSharedPreferences("loginState", MODE_PRIVATE);
        String userName = localStore.getString("UserName", "NA");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userName).child("Tickets_Details").removeValue();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
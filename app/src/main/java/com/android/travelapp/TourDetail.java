package com.android.travelapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class TourDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com/Users");
    SharedPreferences localStore;

    ImageView imgTour;
    TextView nameTour, descTour, priceTour, txtCount;
    Button addCount, subCount, btnPay, select_date_btn;
    ImageButton btnLoc;
    int mCount=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        imgTour = findViewById(R.id.img_tour);
        nameTour = findViewById(R.id.name_tour);
        descTour = findViewById(R.id.desc_tour);
        priceTour = findViewById(R.id.price_tour);
        txtCount = findViewById(R.id.txt_count);
        addCount = findViewById(R.id.btn_addCount);
        subCount = findViewById(R.id.btn_subCount);
        btnPay = findViewById(R.id.btn_pay);
        btnLoc = findViewById(R.id.btn_img_loc);
        select_date_btn = findViewById(R.id.select_date_btn);

        localStore = getSharedPreferences("loginState", MODE_PRIVATE);
        txtCount.setText(Integer.toString(mCount));
        getDataAdapter();

        select_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        addCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCount++;
                txtCount.setText(Integer.toString(mCount));
                if (getIntent().hasExtra("priceTour")){
                    int price_tour = getIntent().getIntExtra("priceTour",0);
                    int totalPrice = price_tour * mCount;
                    priceTour.setText(Integer.toString(totalPrice));
                }
            }
        });
        subCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCount > 1){
                    mCount--;
                    txtCount.setText(Integer.toString(mCount));
                    int price_tour = getIntent().getIntExtra("priceTour",0);
                    int totalPrice = price_tour * mCount;
                    priceTour.setText(Integer.toString(totalPrice));
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String date = select_date_btn.getText().toString();
                if(date.length()!=10){
                    select_date_btn.setError("Select Data");
                    Toast.makeText(TourDetail.this,"Select Tour Date",Toast.LENGTH_SHORT).show();
                    return;
                }

                int price_tour = getIntent().getIntExtra("priceTour", 0);
                int tourPrice = price_tour;
                String tourImageURL = getIntent().getStringExtra("imgTour");
                String tourName = nameTour.getText().toString();
                String totalTickets = txtCount.getText().toString();
                String totalTicketsPrice = priceTour.getText().toString();

                String userName = localStore.getString("UserName", "NA");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_IMG_URL").setValue(tourImageURL);
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_Name").setValue(tourName);
                            databaseReference.child(userName).child("Tickets_Details").child("Total_Tickets").setValue(totalTickets);
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_Price").setValue(tourPrice);
                            databaseReference.child(userName).child("Tickets_Details").child("Total_Tickect_Price").setValue(totalTicketsPrice);
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_Date").setValue(date);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(TourDetail.this, Receipt.class);
                intent.putExtra("userName",userName);
                startActivity(intent);
                finish();

            }
        });

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("locTour")){
                    String txtLoc = getIntent().getStringExtra("locTour");
                    Uri uri = Uri.parse("geo:0,0?q="+txtLoc);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if(mapIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(mapIntent);
                    }
                }
            }
        });

    }

    private void getDataAdapter() {
        if (getIntent().hasExtra("imgTour") && getIntent().hasExtra("nameTour") && getIntent().hasExtra("descTour") && getIntent().hasExtra("priceTour")){
            String image_tour = getIntent().getStringExtra("imgTour");
            String name_tour = getIntent().getStringExtra("nameTour");
            String desc_tour = getIntent().getStringExtra("descTour");
            int price_tour = getIntent().getIntExtra("priceTour", 0);

            setDataDetail(image_tour, name_tour, desc_tour, price_tour);
        }
    }

    private void setDataDetail(String image_tour, String name_tour, String desc_tour, int price_tour) {
        Glide.with(this).asBitmap().load(image_tour).into(imgTour);

        nameTour.setText(name_tour);
        descTour.setText(desc_tour);
        priceTour.setText(Integer.toString(price_tour));
    }

    void showDatePicker(){
        DatePickerDialog datePicker = new DatePickerDialog(
                TourDetail.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format("%02d-%02d-%d",dayOfMonth,month+1,year);
        select_date_btn.setText(date);
    }
}
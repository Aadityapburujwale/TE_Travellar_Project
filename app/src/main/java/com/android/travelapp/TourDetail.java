package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TourDetail extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com/Users");
    SharedPreferences localStore;

    ImageView imgTour;
    TextView nameTour, descTour, priceTour, txtCount;
    Button addCount, subCount, btnPay;
    ImageButton btnLoc;
    int mCount=1;


//    SharedPreferences preferences;

    private static final String KEY_IMG_TOUR = "img_tour";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_NAME_TOUR = "name_tour";
    private static final String KEY_LOC = "loc_tour";
    private static final String KEY_COUNT_ITEMS = "count_items";
    private static final String KEY_PRICE_TOUR = "price_tour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
//        preferences = getSharedPreferences("userInfo", 0);

        imgTour = findViewById(R.id.img_tour);
        nameTour = findViewById(R.id.name_tour);
        descTour = findViewById(R.id.desc_tour);
        priceTour = findViewById(R.id.price_tour);
        txtCount = findViewById(R.id.txt_count);
        addCount = findViewById(R.id.btn_addCount);
        subCount = findViewById(R.id.btn_subCount);
        btnPay = findViewById(R.id.btn_pay);
        btnLoc = findViewById(R.id.btn_img_loc);

        localStore = getSharedPreferences("loginState", MODE_PRIVATE);

        txtCount.setText(Integer.toString(mCount));

        getDataAdapter();

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

                int price_tour = getIntent().getIntExtra("priceTour", 0);
                int tourPrice = price_tour;
                String tourImageURL = getIntent().getStringExtra("imgTour");
                String tourName = nameTour.getText().toString();
                String totalTickets = txtCount.getText().toString();
                String totalTicketsPrice = priceTour.getText().toString();

//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString(KEY_IMG_TOUR, imageValue);
//                editor.putString(KEY_NAME_TOUR, nameTourValue);
//                editor.putString(KEY_COUNT_ITEMS, totalItemsValue);
//                editor.putString(KEY_PRICE_TOUR, String.valueOf(priceValue));
//                editor.putString(KEY_TOTAL_PRICE, totalPriceValue);
//                editor.apply();

                String userName = localStore.getString("UserName", "NA");

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
                Date date = new Date();
                String currentDate = dateFormat.format(date);
                // Log.d("DATE PRO", String.valueOf(date.getTime()));

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_IMG_URL").setValue(tourImageURL);
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_Name").setValue(tourName);
                            databaseReference.child(userName).child("Tickets_Details").child("Total_Tickets").setValue(totalTickets);
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_Price").setValue(tourPrice);
                            databaseReference.child(userName).child("Tickets_Details").child("Total_Tickect_Price").setValue(totalTicketsPrice);
                            databaseReference.child(userName).child("Tickets_Details").child("Tour_Date").setValue(currentDate);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent intent = new Intent(TourDetail.this, Receipt.class);
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
}
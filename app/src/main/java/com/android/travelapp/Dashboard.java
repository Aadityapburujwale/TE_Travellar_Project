package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com");
    SharedPreferences localStore;

    TextView txtName, txtEmail;
    Button checkTickets;

    AlertDialog alertDialog;
    MenuInflater inflater;

    private ArrayList<String> al_img_tour = new ArrayList<>();
    private ArrayList<String> al_name_tour = new ArrayList<>();
    private ArrayList<String> al_desc_tour = new ArrayList<>();
    private ArrayList<Integer> al_price_tour = new ArrayList<>();
    private ArrayList<String> al_location = new ArrayList<>();

    SharedPreferences Oldpreferences;

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOTAL_PRICE = "total_price";
    private static final String KEY_NAME_TOUR = "name_tour";
    private static final String KEY_COUNT_ITEMS = "count_items";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtName = findViewById(R.id.tv_fullname);
        txtEmail = findViewById(R.id.tv_email);
        checkTickets = findViewById(R.id.check_ticket);

        checkTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String nameView = Oldpreferences.getString(KEY_NAME, null);
//                String emailView = Oldpreferences.getString(KEY_EMAIL, null);
//                String phoneView = Oldpreferences.getString(KEY_PHONE, null);
//
//                String nameTourView = Oldpreferences.getString(KEY_NAME_TOUR, null);
//                String totalItemsView = Oldpreferences.getString(KEY_COUNT_ITEMS, null);
//                String totalPriceView = Oldpreferences.getString(KEY_TOTAL_PRICE, null);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //databaseReference.child(userName).child("Tickets_Details").child("Tour_IMG_URL").setValue(tourImageURL);
                        String user_Name = localStore.getString("UserName", "NA");
                        if (snapshot.child("Users").child(user_Name).hasChild("Tickets_Details")) {
                            Intent intent = new Intent(Dashboard.this, Tickets.class);
                            startActivity(intent);
                        } else {
                            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                                    .setTitle("Check Tickets")
                                    .setMessage("\nData is Empty")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Dashboard.this, Dashboard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        Oldpreferences = getSharedPreferences("userInfo", 0);
        localStore = getSharedPreferences("loginState", MODE_PRIVATE);

        String namaView = localStore.getString("UserName", "NA");
        String emailView = localStore.getString("Email", "NA");

        if (namaView != null || emailView != null) {
            txtName.setText(namaView);
            txtEmail.setText(emailView);
        }

        getPlaces();

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void RecycleViewAdapterProcess() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecycleViewAdapter adapter = new RecycleViewAdapter(al_img_tour, al_name_tour, al_desc_tour, al_price_tour, al_location, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.bar_call_center:
                callCenter();
                return true;
            case R.id.bar_email:
                emailCenter();
                return true;
            case R.id.bar_loc:
                getLoc();
                return true;
            case R.id.bar_edit_user:
                editUser();
                return true;
            case R.id.bar_logout:
                getLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callCenter() {
        alertDialog = new AlertDialog.Builder(Dashboard.this)
                .setIcon(android.R.drawable.ic_dialog_dialer)
                .setTitle("Call Center")
                .setMessage("\n+91 9834 320 324")
                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("919834320324");
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        intent.setData(Uri.fromParts("tel", String.valueOf(uri), null));

                        if (intent.resolveActivity(getPackageManager()) != null){
                            startActivity(intent);
                        }
                    }
                })
                .show();

    }
    private void emailCenter(){
        alertDialog = new AlertDialog.Builder(Dashboard.this)
                .setIcon(android.R.drawable.ic_dialog_email)
                .setTitle("Email")
                .setMessage("\nA@ditya.ml")
                .setNeutralButton("Go to Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SEND );
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"A@ditya.ml"});
                        intent.putExtra(Intent.EXTRA_SUBJECT , "TES DULS YE BANG");
                        intent.putExtra(Intent.EXTRA_TEXT , "Travel App");
                        intent.setType("message/rfc822");
                        startActivity(Intent.createChooser(intent , "Choose Your Apps : "));
                    }
                })
                .show();

    }
    private void getLoc(){
        alertDialog = new AlertDialog.Builder(Dashboard.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Location")
                .setMessage("\nKota Madiun, Jawa Timur")
                .setNeutralButton("Go to Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri2 = Uri.parse("geo:0,0?q="+"Kota Madiun, Jawa Timur");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri2);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if(mapIntent.resolveActivity(getPackageManager()) != null){
                            startActivity(mapIntent);
                        }
                    }
                })
                .show();

    }

    private void editUser(){
        String userName = localStore.getString("UserName","NA");

        Intent intent = new Intent(Dashboard.this, EditUser.class);
        intent.putExtra("userName",userName);
        startActivity(intent);

    }
    private void getLogout(){
        localStore.edit().clear().apply();
        Intent intent = new Intent(Dashboard.this, LoginPage.class);
        startActivity(intent);
        finish();
    }

    private void getPlaces(){
        databaseReference.child("Places").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String placeDesc = dataSnapshot.child("Place_Desc").getValue().toString();
                    String placeLocation = dataSnapshot.child("Place_Location").getValue().toString();
                    String placeName = dataSnapshot.child("Place_Name").getValue().toString();
                    String tourPrice = dataSnapshot.child("Tour_Price").getValue().toString();
                    String placeImage = dataSnapshot.child("Place_Image").getValue().toString();


                    al_img_tour.add(placeImage);
                    al_name_tour.add(placeName);
                    al_desc_tour.add(placeDesc);
                    al_price_tour.add(Integer.parseInt(tourPrice));
                    al_location.add(placeLocation);

                }
                RecycleViewAdapterProcess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
package com.android.travelapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    SearchView searchView;

    private ArrayList<String> al_img_tour = new ArrayList<>();
    private ArrayList<String> al_name_tour = new ArrayList<>();
    private ArrayList<String> al_desc_tour = new ArrayList<>();
    private ArrayList<Integer> al_price_tour = new ArrayList<>();
    private ArrayList<String> al_location = new ArrayList<>();

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtName = findViewById(R.id.tv_fullname);
        txtEmail = findViewById(R.id.tv_email);
        checkTickets = findViewById(R.id.check_ticket);
        toolbar = findViewById(R.id.main_toolbar);
        searchView = findViewById(R.id.searchView);

        searchView.setQueryHint("Search Location");

        setSupportActionBar(toolbar);
        localStore = getSharedPreferences("loginState", MODE_PRIVATE);

        String nameView = localStore.getString("UserName", "NA");
        String emailView = localStore.getString("Email", "NA");
        txtName.setText(nameView);
        txtEmail.setText(emailView);

        getPlaces();

        checkTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String user_Name = localStore.getString("UserName", "NA");
                        if (snapshot.child("Users").child(user_Name).hasChild("Tickets_Details")) {
                            Intent intent = new Intent(Dashboard.this, Tickets.class);
                            startActivity(intent);
                        } else {
                             alertDialog = new AlertDialog.Builder(Dashboard.this)
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
    }

    private void RecycleViewAdapterProcess() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecycleViewAdapter adapter = new RecycleViewAdapter(al_img_tour, al_name_tour, al_desc_tour, al_price_tour, al_location, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ////
        recyclerView.setAdapter(adapter);
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

        //@Override

        }

//    protected void onStart() {
//        super.onStart();
//        if(databaseReference!=null){
//            databaseReference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//        if(searchView != null){
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    search(s);
//                    return true;
//                }
//            });
//        }
//    }
//    private void search(String str){
//        ArrayList<al_img_tour> myList = new ArrayList<>();
//        for(al_img_tour object : myList){
//
//        }

    public void goToEditUser(View view) {
        String userName = localStore.getString("UserName","NA");

        Intent intent = new Intent(Dashboard.this, EditUser.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
    }
}


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
import android.widget.Toast;

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

    RecycleViewAdapter adapter;

    SearchView searchView;

    private ArrayList<TourModel> tourList;

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

         tourList = new ArrayList<>();

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


        // Search Bar Implementation
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                showFilteredData(newText);
                return false;
            }
        });

    }

    void showFilteredData(String query){
        ArrayList<TourModel> filteredlist = new ArrayList<>();
        for(TourModel tour : tourList){
            if(tour.getTourName().toLowerCase().contains(query.toLowerCase())){
                filteredlist.add(tour);
            }
        }

        if(filteredlist.isEmpty()){
            RecycleViewAdapterProcess();
            Toast.makeText(Dashboard.this,"No place Matched With your input",Toast.LENGTH_SHORT).show();
        }else{
            RecycleViewAdapterProcess(filteredlist);
        }

    }


    private void RecycleViewAdapterProcess() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecycleViewAdapter(tourList, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
    }

    private void RecycleViewAdapterProcess(ArrayList<TourModel> filteredList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecycleViewAdapter(filteredList, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

                    TourModel tour = new TourModel();
                    tour.setTourImg(placeImage);
                    tour.setTourDesc(placeDesc);
                    tour.setTourLoc(placeLocation);
                    tour.setTourName(placeName);
                    tour.setTourPrice(tourPrice);

                    tourList.add(tour);

                }
                RecycleViewAdapterProcess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void goToEditUser(View view) {
        String userName = localStore.getString("UserName","NA");

        Intent intent = new Intent(Dashboard.this, EditUser.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
    }
}


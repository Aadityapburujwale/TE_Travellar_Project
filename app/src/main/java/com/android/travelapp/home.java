package com.android.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class home extends AppCompatActivity{

    //FloatingActionButton main_home_btn = findViewById(R.id.main_home_btn);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton main_home_btn = findViewById(R.id.main_home_btn);
        Button weather_btn = findViewById(R.id.weather_btn);

        main_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, Dashboard.class);
                startActivity(intent);
            }
        });

//        weather_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(home.this, Weather_details.class);
//                startActivity(intent);
//            }
//        });

    }


}
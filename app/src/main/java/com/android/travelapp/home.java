package com.android.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
        FloatingActionButton chatBot_btn = findViewById(R.id.chatbot_btn);
        Button map_btn = findViewById(R.id.map_btn);

        main_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, Dashboard.class);
                startActivity(intent);
            }
        });

        weather_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        chatBot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, ChatBotScreen.class);
                startActivity(intent);
            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse("geo:0,0?q="+"Where am I ?");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if(mapIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(mapIntent);
                };

            }
        });

    }

}
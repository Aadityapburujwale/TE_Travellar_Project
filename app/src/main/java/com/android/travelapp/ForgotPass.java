package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPass extends AppCompatActivity {
    TextInputLayout inpUser;
    Button btnFind;
    TextView btnSignUp;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        inpUser = findViewById(R.id.username_find);
        btnFind = findViewById(R.id.btn_find);
        btnSignUp = findViewById(R.id.btn_signUp);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = inpUser.getEditText().getText().toString();

                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(userName)){

                            showToast("Successfully Find Your Account");
                            finish();

                            Intent intent = new Intent(ForgotPass.this, EditUser.class);
                            intent.putExtra("userName",userName);
                            intent.putExtra("changePassword",true);
                            startActivity(intent);

                        }else{
                            showToast("Username not Found in database.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPass.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    void showToast(String message){
        Toast.makeText(ForgotPass.this,message,Toast.LENGTH_SHORT).show();
    }

    void goToAnotherScreen(Class className){
        Intent intent = new Intent(ForgotPass.this,className);
        startActivity(intent);
    }

}
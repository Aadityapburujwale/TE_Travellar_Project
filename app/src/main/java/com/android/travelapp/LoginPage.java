package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPage extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com");
    SharedPreferences localStore;

    TextView btnSignUp;
    ImageView img;
    TextView txtTitle, txtSub;
    TextInputLayout txtUser, txtPass;
    LinearLayout txtSignUp;
    Button btnLogin, btnForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);
        setContentView(R.layout.activity_login_page);

        localStore = getSharedPreferences("loginState",MODE_PRIVATE);

        // --> Check User Is Already Logged In.
        if(localStore.getBoolean("isLoggedIn",false)){
            goToAnotherScreen(home.class);
            finish();
        }
        // *** Check User Is Already Logged In. <--


        img = findViewById(R.id.img_logo);
        txtTitle = findViewById(R.id.tv_titleLogin);
        txtSub = findViewById(R.id.subtitleLogin);
        txtUser = findViewById(R.id.username_login);
        txtPass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        btnForgotPass = findViewById(R.id.btn_forgotPass);
        btnSignUp = findViewById(R.id.btn_signUp);
        txtSignUp = findViewById(R.id.ll_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, SignUp.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = txtUser.getEditText().getText().toString();
                String password = txtPass.getEditText().getText().toString();

                if(userName.isEmpty() || password.isEmpty()){
                    showToast("Enter All Required Detail");
                }else{
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userName)){
                                final String getPassword = snapshot.child(userName).child("Password").getValue(String.class);
                                final String hashedPassword = getMd5Hash(password);
                                final String email = snapshot.child(userName).child("Email").getValue(String.class);

                                if(getPassword.equals(hashedPassword)){
                                    showToast("Login Success!");
                                    goToAnotherScreen(home.class);
                                    localStore.edit().putBoolean("isLoggedIn",true).apply();
                                    localStore.edit().putString("UserName",userName).apply();
                                    localStore.edit().putString("Email",email).apply();
                                    finish();
                                }else{
                                    showToast("Enter A Correct password.");
                                }

                            }else{
                                showToast("Entered a valid UserName.");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });


        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(LoginPage.this, ForgotPass.class);
                startActivity(intent);
            }
        });
    }

    void showToast(String message){
        Toast.makeText(LoginPage.this,message,Toast.LENGTH_SHORT).show();
    }

    void goToAnotherScreen(Class className){
        Intent intent = new Intent(LoginPage.this,className);
        startActivity(intent);
    }

    public static String getMd5Hash(String input) {
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

}
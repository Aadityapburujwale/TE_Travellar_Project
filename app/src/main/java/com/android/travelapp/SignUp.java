package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class SignUp extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com");

    TextView txtSignIn;
    ImageView img;
    TextView txtTitle, txtSub;
    TextInputLayout inpFullname, inpEmail, inpPhone, inpUser, inpPass, inpRePass;
    Button btnSignUp, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        img = findViewById(R.id.img_logo);
        txtTitle = findViewById(R.id.tv_title_regis);
        inpFullname = findViewById(R.id.name);
        inpEmail= findViewById(R.id.email);
        inpPhone= findViewById(R.id.phone);
        inpUser = findViewById(R.id.username_regis);
        inpPass = findViewById(R.id.password_regis);
        inpRePass = findViewById(R.id.retype_password);
        btnSignUp = findViewById(R.id.btn_signUp);
        txtSignIn = findViewById(R.id.btn_signIn);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inpFullname.getEditText().getText().toString().trim();
                String email = inpEmail.getEditText().getText().toString().trim();
                String mobileNo = inpPhone.getEditText().getText().toString().trim();
                String userName = inpUser.getEditText().getText().toString().trim();
                String password = inpPass.getEditText().getText().toString().trim();
                String rePassword = inpRePass.getEditText().getText().toString().trim();


                if(name.isEmpty() || email.isEmpty() || mobileNo.isEmpty() || userName.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
                    showToast("Enter All Required Details.");
                }else if(password.equals(rePassword)){

                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(userName)){
                                showToast("UserName Already Registered! Try with different one.");
                            }else{
                                databaseReference.child("Users").child(userName).child("Name").setValue(name);
                                databaseReference.child("Users").child(userName).child("Email").setValue(email);
                                databaseReference.child("Users").child(userName).child("Mobile_No").setValue(mobileNo);
                                databaseReference.child("Users").child(userName).child("Password").setValue(getMd5Hash(password));

                                showToast("Registration Successfully.");
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAnotherScreen(LoginPage.class);
            }
        });
    }

    void showToast(String message){
        Toast.makeText(SignUp.this,message,Toast.LENGTH_SHORT).show();
    }

    void goToAnotherScreen(Class className){
        Intent intent = new Intent(SignUp.this,className);
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
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
package com.android.travelapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EditUser extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://travellapplication-default-rtdb.firebaseio.com");


    TextInputLayout inpName, inpEmail, inpPhone, inpUser, inpPass, inpRePass;
    Button btnUpdate, btnReset;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        inpName = findViewById(R.id.name_edit);
        inpEmail = findViewById(R.id.email_edit);
        inpPhone = findViewById(R.id.phone_edit);
        inpUser = findViewById(R.id.username_edit);
        inpPass = findViewById(R.id.password_edit);
        inpRePass = findViewById(R.id.retype_password_edit);
        btnUpdate = findViewById(R.id.btn_update);

        inpUser.setEnabled(false);
        inpUser.setBackgroundColor(R.color.disableColor);

        String userName = getIntent().getStringExtra("userName");
        boolean changePassword = getIntent().getBooleanExtra("changePassword",false);


        if(changePassword){

            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(userName)){
                        String name = snapshot.child(userName).child("Name").getValue(String.class);
                        String email = snapshot.child(userName).child("Email").getValue(String.class);
                        String mobileNo = snapshot.child(userName).child("Mobile_No").getValue(String.class);

                        inpName.getEditText().setText(name);
                        inpEmail.getEditText().setText(email);
                        inpPhone.getEditText().setText(mobileNo);
                        inpUser.getEditText().setText(userName);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            inpName.setEnabled(false);
            inpEmail.setEnabled(false);
            inpPhone.setEnabled(false);
            inpUser.setEnabled(false);
            inpPass.getEditText().setText(null);
            inpRePass.getEditText().setText(null);
        }else{
            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(userName)){
                        String name = snapshot.child(userName).child("Name").getValue(String.class);
                        String email = snapshot.child(userName).child("Email").getValue(String.class);
                        String mobileNo = snapshot.child(userName).child("Mobile_No").getValue(String.class);
                        String password = snapshot.child(userName).child("Password").getValue(String.class);

                        inpName.getEditText().setText(name);
                        inpEmail.getEditText().setText(email);
                        inpPhone.getEditText().setText(mobileNo);
                        inpUser.getEditText().setText(userName);
                        inpPass.getEditText().setText(null);
                        inpRePass.getEditText().setText(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameValue = inpName.getEditText().getText().toString();
                String emailValue = inpEmail.getEditText().getText().toString();
                String phoneValue = inpPhone.getEditText().getText().toString();
                String passValue = inpPass.getEditText().getText().toString();
                String repassValue = inpRePass.getEditText().getText().toString();

                if(nameValue.isEmpty() || emailValue.isEmpty() || phoneValue.isEmpty() || passValue.isEmpty() || repassValue.isEmpty()){
                    Toast.makeText(EditUser.this,"All fields Are Required.",Toast.LENGTH_SHORT).show();
                }else{
                    if(passValue.equals(repassValue)){

                        String hashedPassword = getMd5Hash(passValue);

                        databaseReference.child("Users").child(userName).child("Name").setValue(nameValue);
                        databaseReference.child("Users").child(userName).child("Email").setValue(emailValue);
                        databaseReference.child("Users").child(userName).child("Mobile_No").setValue(phoneValue);
                        databaseReference.child("Users").child(userName).child("Password").setValue(hashedPassword);

                        Toast.makeText(EditUser.this,"Data Updated Succesfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditUser.this,LoginPage.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(EditUser.this,"Password not matched with confirm password.",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

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
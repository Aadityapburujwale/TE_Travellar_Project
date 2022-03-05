package com.android.travelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                        inpPass.getEditText().setText(password);
                        inpRePass.getEditText().setText(password);
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

                if(passValue.equals(repassValue)){
                    databaseReference.child("Users").child(userName).child("Name").setValue(nameValue);
                    databaseReference.child("Users").child(userName).child("Email").setValue(emailValue);
                    databaseReference.child("Users").child(userName).child("Mobile_No").setValue(phoneValue);
                    databaseReference.child("Users").child(userName).child("Password").setValue(passValue);

                    Toast.makeText(EditUser.this,"Data Updated Succesfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditUser.this,LoginPage.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditUser.this,"Password not matched with confirm password.",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}
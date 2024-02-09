package com.example.dailyexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void loadRegistrationPage(View view){
        Intent i = new Intent(Welcome.this, Registration.class);
        startActivity(i);
    }
    public void loadLoginPage(View view){
        Intent i = new Intent(Welcome.this,Login.class);
        startActivity(i);
    }
}
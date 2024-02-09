package com.example.dailyexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Accounts extends AppCompatActivity {
	
	// Tv - TextView
	TextView fullNameTv, emailTv, contactNoTv, genderTv, birthdateTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accounts);

//        finding id
		fullNameTv = findViewById(R.id.fullName);
		emailTv = findViewById(R.id.email);
		contactNoTv = findViewById(R.id.contactNo);
		genderTv = findViewById(R.id.gender);
		birthdateTv = findViewById(R.id.birthdate);
		
		
		SharedPreferences sp = getSharedPreferences("spRegDetails", MODE_PRIVATE);
	
		fullNameTv.setText(sp.getString("spFullName", ""));
		emailTv.setText(sp.getString("spEmail", ""));
		contactNoTv.setText(sp.getString("spContactNo", ""));
		genderTv.setText(sp.getString("spGender", ""));
		birthdateTv.setText(sp.getString("spBirthdate", ""));
		
		
//		Intent iData = getIntent();
//		if (iData != null) {
//			String fullNameE = iData.getStringExtra("FULLNAME");
//			String emailE = iData.getStringExtra("EMAIL");
//			String contactNoE = iData.getStringExtra("CONTACTNO");
//			String genderE = iData.getStringExtra("GENDER");
//			String birthdateE = iData.getStringExtra("BIRTHDATE");;
//
//			fullNameTv.setText(fullNameE);
//			emailTv.setText(emailE);
//			contactNoTv.setText(contactNoE);
//			genderTv.setText(genderE);
//			birthdateTv.setText(birthdateE);
//		}
	}
}
package com.example.dailyexpensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
	
	EditText emailEt, passEt;
	int attempt = 0, valid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		emailEt = findViewById(R.id.email);
		passEt = findViewById(R.id.pass);
//		emailEt.setText("darshannolkha123@gmail.com");
//		passEt.setText("DARSHAN_");
		
		// setting the text fetched from registration page
//		Intent i = getIntent();
//		Bundle b = i.getExtras();
//		if (b != null) {
//			emailEt = findViewById(R.id.email);
//			passEt = findViewById(R.id.pass);
//			emailEt.setText(b.getString("EMAIL"));
//			passEt.setText(b.getString("PASS"));
//		}
		
		// get SP
		SharedPreferences sp = getSharedPreferences("spLoginDetails", MODE_PRIVATE);
		String spEmail = sp.getString("spEmail", "");
		String spPass = sp.getString("spPassword", "");
		
		emailEt.setText(spEmail);
		passEt.setText(spPass);
		
	}
	
	public void loadRegistrationPage(View view) {
		Intent i = new Intent(Login.this, Registration.class);
		startActivity(i);
	}
	
	public void login(View view) {
		//        finding id
		emailEt = findViewById(R.id.email);
		passEt = findViewById(R.id.pass);
		
		String email = emailEt.getText().toString();
		String pass = passEt.getText().toString();
		
		validate(email, pass);
		
		if (valid == 1) {
			if (attempt < 3) {
				DBHelper dbh = new DBHelper(getApplicationContext());
				int result = dbh.authenticateUser(email, pass);
				if (result == 1) {
					Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
					/*
					Intent i = getIntent();
					Intent I = new Intent(getApplicationContext(), HomeScreen.class);
					
					if (i != null && i.getExtras() != null) {
						I.putExtras(getIntent().getExtras());
					}
					startActivity(I);
					*/
					
					// set SP
					Intent I = new Intent(getApplicationContext(), HomeScreen.class);
					
					SharedPreferences sp = getSharedPreferences("spLoginDetails", MODE_PRIVATE);
					SharedPreferences.Editor edit = sp.edit();
					edit.putString("spEmail", email);
					edit.putString("spPassword", pass);
					edit.commit();
					
					startActivity(I);
				} else {
					Toast.makeText(getApplicationContext(), "Email or Password is Incorrect!", Toast.LENGTH_SHORT).show();
					attempt++;
				}
			} else
				Toast.makeText(getApplicationContext(), "Your 3 attempts are failed. Try again later", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void validate(String email, String pass) {
		valid = 1;
		
		if (email.length() == 0) {
			emailEt.requestFocus();
			emailEt.setError("Field cannot be empty");
			valid = 0;
		} else if (!email.matches("^[a-z][a-z0-9]+@(gmail|outlook|hotmail|yahoo|icloud)[.](com|in)$")) {
			emailEt.requestFocus();
			emailEt.setError("Invalid Format");
			valid = 0;
		}
		
		if (pass.length() == 0) {
			passEt.requestFocus();
			passEt.setError("Field cannot be empty");
			valid = 0;
		} else if (!pass.matches("[A-Za-z0-9@#_]{8,10}")) {
			passEt.requestFocus();
			passEt.setError("Must be greater than or equal to 8 characters & less than or equal to 10 characters");
			valid = 0;
		} else if (!pass.contains("@") && !pass.contains("#") && !pass.contains("_")) {
			passEt.requestFocus();
			passEt.setError("Must contain a special character");
			valid = 0;
		}
	}
}
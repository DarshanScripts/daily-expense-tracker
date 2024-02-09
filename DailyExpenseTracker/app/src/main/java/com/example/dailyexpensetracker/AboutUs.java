package com.example.dailyexpensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutUs extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
	}
	
	public void dial(View v){
		Intent iDial = new Intent(Intent.ACTION_DIAL);
		iDial.setData(Uri.parse("tel: +919157510123"));
		startActivity(iDial);
	}
}
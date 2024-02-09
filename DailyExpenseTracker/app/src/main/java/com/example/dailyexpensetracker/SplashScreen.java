package com.example.dailyexpensetracker;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
	private MediaPlayer mediaPlayer;
	private ImageView imgLogo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		imgLogo = findViewById(R.id.imgLogo);
		
		// Load the rotation animation
		Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
		
		// Start the animation on the ImageView
		imgLogo.startAnimation(rotateAnimation);
		
		// Delay for 2 seconds and then play the music
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.softmusic);
				mediaPlayer.start();
			}
		}, 2000); // Delay for 2 seconds
		
		// Delay for a total of 4 seconds and navigate to the next activity
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Stop the music and navigate to the next activity
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					mediaPlayer.release();
				}
				
				Intent intent = new Intent(getApplicationContext(), Welcome.class);
				startActivity(intent);
				finish();
			}
		}, 4000); // Delay for a total of 4 seconds
	}
}
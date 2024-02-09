package com.example.dailyexpensetracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AlarmSettings extends AppCompatActivity {
	private TimePicker timePicker;
	private Button setAlarmButton;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_settings);
		
		timePicker = findViewById(R.id.timePicker);
		setAlarmButton = findViewById(R.id.btnSaveAlarm);
		
		setAlarmButton.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.M)
			@Override
			public void onClick(View view) {
				setAlarm();
			}
		});
	}
	
	@RequiresApi(api = Build.VERSION_CODES.M)
	private void setAlarm() {
		int hour = timePicker.getHour();
		int minute = timePicker.getMinute();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		long triggerTime = calendar.getTimeInMillis();
		
		EditText labelEt = findViewById(R.id.notificationLabel);
		Intent intent = new Intent(this, AlarmReceiver.class);
		if(labelEt != null) {
			intent.putExtra("label", labelEt.getText().toString());
		}
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		// Set the alarm to repeat daily
		alarmManager.setRepeating(
				AlarmManager.RTC_WAKEUP,
				triggerTime,
				AlarmManager.INTERVAL_DAY,
				pendingIntent
		);
		
		Toast.makeText(this, "Daily alarm set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
	}
	
	public void loadHomepage(View view) {
		startActivity(new Intent(this, HomeScreen.class));
	}
}

package com.example.dailyexpensetracker;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence channelName = "My Channel Name";
			String channelId = "channel_id"; // You can customize this ID
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
			NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
		String label = intent.getStringExtra("label");
		showNotification(context, "Income and Expense Reminder", "Don't forget to add your incomes and expenses!", label);
	}
	
	private void showNotification(Context context, String title, String message, String label) {
		String msg = message + "\n" + label;
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
				.setSmallIcon(R.drawable.ic_launcher_logo)
				.setContentTitle(title)
				.setContentText(msg)
				.setPriority(NotificationCompat.PRIORITY_HIGH);
		
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
		if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
		}
		notificationManager.notify(1, builder.build());
	}
}

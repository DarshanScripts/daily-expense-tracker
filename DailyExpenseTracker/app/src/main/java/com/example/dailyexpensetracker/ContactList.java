package com.example.dailyexpensetracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;

public class ContactList extends AppCompatActivity {
	ListView contactList;
	ArrayList<String> listData;
	
	@SuppressLint("Range")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		contactList = findViewById(R.id.contactList);
		listData = new ArrayList<>();
		
		if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
		
		ContentResolver resolver = getContentResolver();
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		
		String order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"; // Sort by display name
		
		Cursor c = resolver.query(uri, null, null, null, order);
		
		HashSet<String> uniqueNumbers = new HashSet<>(); // To store unique phone numbers
		
		if (c.getCount() > 0) {
			String name, number, fullContact;
			while (c.moveToNext()) {
				name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				
				// Check if the number is unique before adding it to the list
				if (!uniqueNumbers.contains(number)) {
					fullContact = name + "\n" + number;
					listData.add(fullContact);
					uniqueNumbers.add(number);
				}
			}
		}
		
		ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listData);
		contactList.setAdapter(adapter);
	}
}
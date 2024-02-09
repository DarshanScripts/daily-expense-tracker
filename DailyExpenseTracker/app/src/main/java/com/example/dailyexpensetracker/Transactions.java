package com.example.dailyexpensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Transactions extends AppCompatActivity {
	List<String> combinedList = new ArrayList<>();
	
	private long selectedItemID = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions);
		
		/*
		// Retrieve stored income and expense entries from DataStorage
		List<String> incomeList = DataStorage.getInstance().getIncomeList();
		List<String> expenseList = DataStorage.getInstance().getExpenseList();
		
		// Combine the lists
		List<String> combinedList = new ArrayList<>();
		combinedList.addAll(incomeList);
		combinedList.addAll(expenseList);
		
		// Create an ArrayAdapter with a custom layout for the list items
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				combinedList
		) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				
				String entry = combinedList.get(position);
				boolean isIncome = entry.contains("Payment:");
				
				// Set background color based on type
				if (!isIncome) {
					view.setBackgroundColor(ContextCompat.getColor(Transactions.this, android.R.color.holo_green_light));
				} else {
					view.setBackgroundColor(ContextCompat.getColor(Transactions.this, android.R.color.holo_red_light));
				}
				return view;
			}
		};
		*/
		
		//oncreate method
		DBHelper dbh = new DBHelper(getApplicationContext());
		
		// get SP
		SharedPreferences sp = getSharedPreferences("spLoginDetails", MODE_PRIVATE);
		String email = sp.getString("spEmail", "");
		int userId = dbh.getUserId(email);
		
		List<String> incomeList = dbh.getAllIncome(this, userId);
		List<String> expenseList = dbh.getAllExpense(this, userId);
		
		// Combine the lists
		combinedList.addAll(incomeList);
		combinedList.addAll(expenseList);
		
		// Create an ArrayAdapter with a custom layout for the list items
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, combinedList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				
				String entry = combinedList.get(position);
				boolean isIncome = entry.contains("Payment:");
				
				// Set background color based on type
				if (!isIncome) {
					view.setBackgroundColor(ContextCompat.getColor(Transactions.this, android.R.color.holo_green_light));
				} else {
					view.setBackgroundColor(ContextCompat.getColor(Transactions.this, android.R.color.holo_red_light));
				}
				
				// Set a long click listener to capture the selected item's ID
				final long itemID = getItemId(position);
				view.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						selectedItemID = itemID; // Store the selected item's ID
						return false;
					}
				});
				
				return view;
			}
		};

//		 Set the adapter to your ListView
		ListView combinedListView = findViewById(R.id.transactionList);
		combinedListView.setAdapter(adapter);
		
		registerForContextMenu(combinedListView);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		DBHelper dbh = new DBHelper(getApplicationContext());
		if (info.id == selectedItemID) {
			String entry = combinedList.get(info.position);
			boolean isIncome = entry.contains("Payment:");
			
			if (!isIncome) {
				dbh.removeIncome(selectedItemID);
			} else {
				dbh.removeExpense(selectedItemID);
			}
			finish();
			startActivity(getIntent());
			
			selectedItemID = -1;
		}
		return super.onContextItemSelected(item);
	}
	
	
	public void loadHomepage(View view) {
		startActivity(new Intent(this, HomeScreen.class));
	}
	
}
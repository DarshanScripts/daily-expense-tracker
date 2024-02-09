package com.example.dailyexpensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {
	public String[] incArr, expArr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
	}
	
	public void loadAddIncome(View v) {
//		startActivity(new Intent(getApplicationContext(), AddIncome.class));
		Intent i = new Intent(this, AddIncome.class);
//		startActivityForResult(i, 2);
		startActivity(i);
	}
	
	public void loadAddExpense(View v) {
//		startActivity(new Intent(getApplicationContext(), AddExpense.class));
		Intent i = new Intent(this, AddExpense.class);
		startActivity(i);
//		startActivityForResult(i, 3);
	}
	
	public void loadTransactions(View v) {
		//startActivity(new Intent(getApplicationContext(), Transactions.class));
		
		Intent i = new Intent(this, Transactions.class);
//		i.putExtra("incArr", incArr);
//		i.putExtra("expArr",expArr);
		startActivity(i);
	}
	
	public void loadReports(View v) {
	
	}
	
	public void loadAccounts(View v) {
		Intent i = getIntent();
		Intent I = new Intent(getApplicationContext(), Accounts.class);
		
		if (i != null && i.getExtras() != null) {
			I.putExtras(getIntent().getExtras());
		}
		startActivity(I);
	}
	
	public void loadAboutUs(View v) {
		startActivity(new Intent(getApplicationContext(), AboutUs.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.home_screen_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.viewIncome) {
			Intent i = new Intent(getApplicationContext(), ViewIncome.class);
			startActivity(i);
		}
//		if (item.getItemId() == R.id.viewExpense) {
//			Intent i = new Intent(getApplicationContext(), ViewExpense.class);
//			startActivity(i);
//		}
		if (item.getItemId() == R.id.addCategoryMenu) {
			Intent i = new Intent(getApplicationContext(), AddCategory.class);
			startActivity(i);
		}
		if (item.getItemId() == R.id.toDoList) {
			Intent i = new Intent(getApplicationContext(), ToDoList.class);
			startActivity(i);
		}
		if (item.getItemId() == R.id.contactListMenu) {
			Intent i = new Intent(getApplicationContext(), ContactList.class);
			startActivity(i);
		}
		if (item.getItemId() == R.id.settings) {
			Intent i = new Intent(getApplicationContext(), Settings.class);
			startActivity(i);
		}
		
		if (item.getItemId() == R.id.logout) {
			AlertDialog.Builder bdr = new AlertDialog.Builder(HomeScreen.this);
			bdr.setTitle("Logout");
			bdr.setMessage("“Are you sure you want to logout?");
			bdr.setCancelable(false);
			bdr.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					finishAndRemoveTask();
				}
			});
			bdr.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
				}
			});
			AlertDialog dialog = bdr.create();
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}
}
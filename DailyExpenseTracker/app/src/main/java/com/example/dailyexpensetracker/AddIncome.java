package com.example.dailyexpensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AddIncome extends AppCompatActivity {
	
	private EditText amountEt, dateEt, notesEt;
	private Button addIncomeBtn;
	private Spinner inCategorySpin;
	private ProgressBar incPB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_income);
		
		amountEt = findViewById(R.id.inAmt);
		inCategorySpin = findViewById(R.id.inCategory);
		dateEt = findViewById(R.id.inDate);
		notesEt = findViewById(R.id.inNotes);
		addIncomeBtn = findViewById(R.id.addIncome);
		incPB = findViewById(R.id.pIncLoad);
		
		
		dateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDatePickerDialog();
				}
			}
		});
		
		DBHelper dbh = new DBHelper(getApplicationContext());
		ArrayList<String> alCategory = dbh.getCategory();
		
		ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, alCategory);
		inCategorySpin.setAdapter(ad);
		
		addIncomeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String amount = amountEt.getText().toString();
				String category = inCategorySpin.getSelectedItem().toString();
				String date = dateEt.getText().toString();
				String notes = notesEt.getText().toString();
				
				if (amount.equals("") || amount == null) {
					amountEt.requestFocus();
					amountEt.setError("Field cannot be empty");
				} else {
//					String str = "Amount: " + amount + "\n" +
//							"Category: " + category + "\n" +
//							"Date: " + date + "\n" +
//							"Note: " + notes + "\n";
					
//					DataStorage.getInstance().addIncome(str);
					
					DBHelper dbh = new DBHelper(getApplicationContext());
					
					// get SP
					SharedPreferences sp = getSharedPreferences("spLoginDetails", MODE_PRIVATE);
					String email = sp.getString("spEmail", "");
					int userId = dbh.getUserId(email);
					
					dbh.addIncome(userId, Double.parseDouble(amount), category, date, notes);
					
					// progress bar logic
					incPB.setVisibility(View.VISIBLE);
					
					// Delay for 3 seconds
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							incPB.setVisibility(View.INVISIBLE);
							
							Intent i = new Intent(getApplicationContext(), Transactions.class);
							startActivity(i);
						}
					}, 3000); // 3000 milliseconds = 3 seconds
				}
			}
		});
	}
	
	private void showDatePickerDialog() {
		// Get the current date
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		// Create a new DatePickerDialog instance
		DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				// Do something with the selected date
				String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
				dateEt = findViewById(R.id.inDate);
				dateEt.setText(selectedDate);
			}
		}, year, month, day);
		
		// disable future dates
		datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		
		// Show the date picker dialog
		datePickerDialog.show();
	}
	
	public void loadHomepage(View view) {
		startActivity(new Intent(this, HomeScreen.class));
	}
	
}
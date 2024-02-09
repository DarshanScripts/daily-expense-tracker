package com.example.dailyexpensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class AddExpense extends AppCompatActivity {
	private EditText amountEt, dateEt, notesEt;
	private Button addExpenseBtn, btnCapture;
	private Spinner exCategorySpin, paymentSpin;
	private ProgressBar expPB;
	private ImageView imgCapture;
	
	private static final int Image_Capture_Code = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_expense);
		
		amountEt = findViewById(R.id.exAmt);
		exCategorySpin = findViewById(R.id.exCategory);
		paymentSpin = findViewById(R.id.payment);
		dateEt = findViewById(R.id.exDate);
		notesEt = findViewById(R.id.exNotes);
		addExpenseBtn = findViewById(R.id.addExpense);
		expPB = findViewById(R.id.pExpLoad);
		
		
		btnCapture =(Button)findViewById(R.id.btnCapture);
		imgCapture = (ImageView) findViewById(R.id.imgCapture);
		btnCapture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cInt,Image_Capture_Code);
			}
		});
		
		DBHelper dbh = new DBHelper(getApplicationContext());
		ArrayList<String> alCategory = dbh.getCategory();
		
		ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, alCategory);
		exCategorySpin.setAdapter(ad);
		
		
		dateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDatePickerDialog();
				}
			}
		});
		
		addExpenseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String amount = amountEt.getText().toString();
				String category = exCategorySpin.getSelectedItem().toString();
				String payment = paymentSpin.getSelectedItem().toString();
				String date = dateEt.getText().toString();
				String notes = notesEt.getText().toString();
				
				if (amount.equals("") || amount == null) {
					amountEt.requestFocus();
					amountEt.setError("Field cannot be empty");
				} else {
//					String str = "Amount: " + amount + "\n" +
//							"Category: " + category + "\n" +
//							"Payment: " + payment + "\n" +
//							"Date: " + date + "\n" +
//							"Note: " + notes + "\n";
//
//					DataStorage.getInstance().addExpense(str);
					
					
					
					// on add income button click
					DBHelper dbh = new DBHelper(getApplicationContext());
					
					// get SP
					SharedPreferences sp = getSharedPreferences("spLoginDetails", MODE_PRIVATE);
					String email = sp.getString("spEmail", "");
					int userId = dbh.getUserId(email);
					
					dbh.addExpense(userId, Double.parseDouble(amount), category, payment, date, notes);
					
					
					// progress bar logic
					expPB.setVisibility(View.VISIBLE);
					
					// Delay for 3 seconds
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							expPB.setVisibility(View.INVISIBLE);
							
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
				dateEt = findViewById(R.id.exDate);
				dateEt.setText(selectedDate);
			}
		}, year, month, day);
		
		// disable future dates
		datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
		
		
		// Show the date picker dialog
		datePickerDialog.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Image_Capture_Code) {
			if (resultCode == RESULT_OK) {
				Bitmap bp = (Bitmap) data.getExtras().get("data");
				imgCapture.setImageBitmap(bp);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void loadHomepage(View view) {
		startActivity(new Intent(this, HomeScreen.class));
	}
}
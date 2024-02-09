package com.example.dailyexpensetracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
	
	//    Et - EditText, Rg - RadioGroup
	EditText fullNameEt, emailEt, contactNoEt, birthdateEt, passEt, conPassEt;
	RadioGroup genderRg;
	
	String url = ApiURI.BASE_URL;
	
	int valid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		EditText birthdateEt = findViewById(R.id.birthdate);
		birthdateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDatePickerDialog();
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
				EditText birthdateEt = findViewById(R.id.birthdate);
				birthdateEt.setText(selectedDate);
			}
		}, year, month, day);
		
//		long maxDate = System.currentTimeMillis();

		// Calculate the maximum date (10 years ago from today)
		Calendar maxDateCalendar = Calendar.getInstance();
		maxDateCalendar.add(Calendar.YEAR, -10);
		long maxDate = maxDateCalendar.getTimeInMillis();

		// Set the date range
		datePickerDialog.getDatePicker().setMaxDate(maxDate);
		
		
		// Show the date picker dialog
		datePickerDialog.show();
	}
	
	public void loadLoginPage(View view) {
		Intent i = new Intent(Registration.this, Login.class);
		startActivity(i);
	}
	
	public void register(View view) {
//        finding id
		fullNameEt = findViewById(R.id.fullName);
		emailEt = findViewById(R.id.email);
		contactNoEt = findViewById(R.id.contactNo);
		genderRg = findViewById(R.id.gender);
		birthdateEt = findViewById(R.id.birthdate);
		passEt = findViewById(R.id.pass);
		conPassEt = findViewById(R.id.conPass);
		
		String fullName = fullNameEt.getText().toString();
		String email = emailEt.getText().toString();
		String contactNo = contactNoEt.getText().toString();
		int gender = genderRg.getCheckedRadioButtonId();
		RadioButton selGender = findViewById(gender);
		
		String birthdate = birthdateEt.getText().toString();
		String pass = passEt.getText().toString();
		String conPass = conPassEt.getText().toString();
		
		
		validate(fullName, email, contactNo, gender, birthdate, pass, conPass);
		
		if (fullName.isEmpty()  && email.isEmpty() && pass.isEmpty() && contactNo.isEmpty()&& birthdate.isEmpty())
		{
			Toast.makeText(this, "Any field empty", Toast.LENGTH_SHORT).show();
		}
		else {
			StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								JSONObject jsonObject = new JSONObject(response);
								String code = jsonObject.getString("code");
								Toast.makeText(getApplicationContext(), "" + code, Toast.LENGTH_SHORT).show();
								if (code.equals("registration_successfully")) {
									Toast.makeText(getApplicationContext(), "you are registered successfully", Toast.LENGTH_LONG).show();
								} else if (code.equals("registration_successfully")) {
									Toast.makeText(getApplicationContext(), "you are not registered", Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(getApplicationContext(), "JSON Parsing error", Toast.LENGTH_LONG).show();
							}
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(getApplicationContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<>();
					params.put("name", fullName);
					params.put("email", email);
					params.put("contact", contactNo);
					params.put("gender", String.valueOf(gender));
					params.put("birthdate", birthdate);
					params.put("password", pass);
					return params;
				}
			};
			MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
		}
		if (valid == 1) {
			Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
			
			
			SharedPreferences pref = getSharedPreferences("spRegDetails", MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			
			editor.putString("spFullName", fullName);
			editor.putString("spEmail", email);
			editor.putString("spContactNo", contactNo);
			editor.putString("spGender", selGender.getText().toString());
			editor.putString("spBirthdate", birthdate);
			
			editor.apply();
			
			DBHelper dbh = new DBHelper(getApplicationContext());
			
			dbh.addUser(fullName, email, contactNo, selGender.getText().toString(), birthdate, pass);
			
			Intent i = new Intent(getApplicationContext(), Login.class);
//			i.putExtra("FULLNAME",fullName);
//			i.putExtra("EMAIL",email);
//			i.putExtra("CONTACTNO",contactNo);
//			i.putExtra("GENDER",selGender.getText().toString());
//			i.putExtra("BIRTHDATE",birthdate);
//			i.putExtra("PASS",pass);
			startActivity(i);
		}
//		else
//			Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
	}
	
	private void validate(String fullName, String email, String contactNo, int gender, String birthdate, String pass, String conPass) {
		valid = 1;
		
		if (fullName.length() == 0) {
			fullNameEt.requestFocus();
			fullNameEt.setError("Field cannot be empty");
			valid = 0;
		} else if (!fullName.matches("^[A-Za-z ]+$")) {
			fullNameEt.requestFocus();
			fullNameEt.setError("Only alphabets are allowed");
			valid = 0;
		}
		
		if (email.length() == 0) {
			emailEt.requestFocus();
			emailEt.setError("Field cannot be empty");
			valid = 0;
		} else if (!email.matches("^[a-z][a-z0-9]+@(gmail|outlook|hotmail|yahoo|icloud)[.](com|in)$")) {
			emailEt.requestFocus();
			emailEt.setError("Invalid format");
			valid = 0;
		}
		
		if (contactNo.length() == 0) {
			contactNoEt.requestFocus();
			contactNoEt.setError("Field cannot be empty");
			valid = 0;
		} else if (contactNo.length() != 10) {
			contactNoEt.requestFocus();
			contactNoEt.setError("Must contain 10 digits");
			valid = 0;
		} else if (!contactNo.matches("^[6-9][0-9]{9}$")) {
			contactNoEt.requestFocus();
			contactNoEt.setError("Incorrect format");
			valid = 0;
		}
		
		if (gender == -1) {
			genderRg.requestFocus();
			Toast.makeText(getApplicationContext(), "Please select gender", Toast.LENGTH_SHORT).show();
			valid = 0;
		}
		
		if (birthdate.length() == 0) {
//			birthdateEt.requestFocus();
			birthdateEt.setError("Field cannot be empty");
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
		
		if (conPass.length() == 0) {
			conPassEt.requestFocus();
			conPassEt.setError("Field cannot be empty");
			valid = 0;
		} else if (!conPass.equals(pass)) {
			conPassEt.requestFocus();
			conPassEt.setError("Password & confirm password must be matched");
			valid = 0;
		}
	}
}
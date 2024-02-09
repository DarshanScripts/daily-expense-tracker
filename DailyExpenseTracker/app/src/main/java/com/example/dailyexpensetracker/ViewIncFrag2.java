package com.example.dailyexpensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ViewIncFrag2 extends Fragment {
	private TextView viewIncTv;
	private EditText amountEt, dateEt, notesEt;
	private Spinner inCategorySpin;
	private Button addIncome;
	
	public ViewIncFrag2() {
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_view_inc_frag2, container, false);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		viewIncTv = getActivity().findViewById(R.id.viewIncDetails);
		
		amountEt = getActivity().findViewById(R.id.vInAmt);
		inCategorySpin = getActivity().findViewById(R.id.vInCategory);
		dateEt = getActivity().findViewById(R.id.vInDate);
		notesEt = getActivity().findViewById(R.id.vInNotes);
		addIncome = getActivity().findViewById(R.id.vAddIncome);
		addIncome.setOnClickListener(new View.OnClickListener() {
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
					String str = "Amount: " + amount + "\n" +
							"Category: " + category + "\n" +
							"Date: " + date + "\n" +
							"Note: " + notes + "\n";
					
					viewIncTv.setText(str);
					
					// clear fields
					amountEt.getText().clear();
					dateEt.getText().clear();
					notesEt.getText().clear();
				}
			}
		});
		
	}
}
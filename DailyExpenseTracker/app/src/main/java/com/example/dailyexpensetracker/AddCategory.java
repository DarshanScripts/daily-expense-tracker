package com.example.dailyexpensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddCategory extends AppCompatActivity {
	private EditText categoryEt;
	private Button addCategoryBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);
		
		categoryEt = findViewById(R.id.category);
		
		addCategoryBtn = findViewById(R.id.addCategory);
		
		addCategoryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String categoryName = categoryEt.getText().toString();
				if (categoryEt == null) {
					categoryEt.requestFocus();
					categoryEt.setError("Field cannot be empty");
				} else {
					DBHelper dbh = new DBHelper(getApplicationContext());
					dbh.addCategory(categoryName);
					Toast.makeText(AddCategory.this, "Category Added!", Toast.LENGTH_SHORT).show();
					categoryEt.setText("");
				}
			}
		});
	}
	
	public void loadHomepage(View view) {
		startActivity(new Intent(this, HomeScreen.class));
	}
	
}
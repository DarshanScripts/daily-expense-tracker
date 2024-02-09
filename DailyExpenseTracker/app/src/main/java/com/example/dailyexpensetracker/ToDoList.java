package com.example.dailyexpensetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;

public class ToDoList extends AppCompatActivity {
	private EditText taskEt;
	private Button addTaskBtn;
	private String filename = "list.txt";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_list);
		
		taskEt = findViewById(R.id.task);
		addTaskBtn = findViewById(R.id.addTask);
		
		addTaskBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String task = taskEt.getText().toString();
				if (task == null) {
					taskEt.requestFocus();
					taskEt.setError("Field cannot be empty");
				} else {
					try {
						FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND);
						String data = task + "\n";
						fos.write(data.getBytes());
						fos.flush();
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(), task + " added in your To-Do List!", Toast.LENGTH_SHORT).show();
					taskEt.setText("");
				}
			}
		});
	}
	
	public void loadHomepage(View view) {
		startActivity(new Intent(this, HomeScreen.class));
	}
}
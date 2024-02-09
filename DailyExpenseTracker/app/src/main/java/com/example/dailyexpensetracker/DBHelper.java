package com.example.dailyexpensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "DailyExpenseTracker";
	private static final int DATABASE_VERSION = 3;
	
	private static final String TABLE_CATEGORIES = "CREATE TABLE Categories" +
			"(" +
			"CategoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"CategoryName TEXT NOT NULL " +
			")";
	private static final String TABLE_USER_DETAILS = "CREATE TABLE User_Details" +
			"(" +
			"UserId INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"UserName TEXT NOT NULL, " +
			"Email TEXT NOT NULL, " +
			"ContactNo TEXT NOT NULL, " +
			"Gender TEXT NOT NULL, " +
			"BirthDate TEXT NOT NULL, " +
			"Password TEXT NOT NULL " +
			")";
	private static final String TABLE_INCOME_DETAILS = "CREATE TABLE Income_Details" +
			"(" +
			"IncomeId INTEGER NOT NULL, " +
			"UserId INTEGER NOT NULL, " +
			"Amount DOUBLE NOT NULL, " +
			"CategoryName TEXT NOT NULL, " +
			"Date TEXT NOT NULL, " +
			"Notes TEXT, " +
			"PRIMARY KEY (IncomeId, UserId), " +
			"FOREIGN KEY(UserId) REFERENCES User_Details(UserId)" +
			")";
	private static final String TABLE_EXPENSE_DETAILS = "CREATE TABLE Expense_Details" +
			"(" +
			"ExpenseId INTEGER NOT NULL, " +
			"UserId INTEGER NOT NULL, " +
			"Amount DOUBLE NOT NULL, " +
			"CategoryName TEXT NOT NULL, " +
			"PaymentMethod TEXT NOT NULL, " +
			"Date TEXT NOT NULL, " +
			"Notes TEXT, " +
			"PRIMARY KEY (ExpenseId, UserId), " +
			"FOREIGN KEY(UserId) REFERENCES User_Details(UserId)" +
			")";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CATEGORIES);
		db.execSQL(TABLE_USER_DETAILS);
		db.execSQL(TABLE_INCOME_DETAILS);
		db.execSQL(TABLE_EXPENSE_DETAILS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1) {
		db.execSQL("DROP TABLE IF EXISTS Categories");
		db.execSQL("DROP TABLE IF EXISTS User_Details");
		db.execSQL("DROP TABLE IF EXISTS Income_Details");
		db.execSQL("DROP TABLE IF EXISTS Expense_Details");
		
		onCreate(db);
	}
	
	public int getUserId(String email) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT userId FROM User_Details WHERE Email = ?";
		Cursor c = db.rawQuery(query, new String[]{email});
		int userId = 0;
		
		if (c != null && c.moveToFirst()) {
			userId = c.getInt(0);
			c.close();
		}
		return userId;
	}
	
	public void addCategory(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("CategoryName", name);
		
		db.insert("Categories", null, cv);
	}
	
	public void addUser(String username, String email, String contact, String gender, String birthdate, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("UserName", username);
		cv.put("Email", email);
		cv.put("ContactNo", contact);
		cv.put("Gender", gender);
		cv.put("BirthDate", birthdate);
		cv.put("Password", password);
		
		db.insert("User_Details", null, cv);
	}
	
	public void addIncome(int userId, double amount, String category, String date, String notes) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Find the last income ID and add one to it for the new record
		String query = "SELECT MAX(IncomeId) FROM Income_Details";
		Cursor c = db.rawQuery(query, null);
		int lastIncomeId = 0;
		
		if (c != null && c.moveToFirst()) {
			lastIncomeId = c.getInt(0);
			lastIncomeId++; // Increment by one for the new record
			c.close();
		}
		
		ContentValues cv = new ContentValues();
		cv.put("IncomeId", lastIncomeId);
		cv.put("UserId", userId);
		cv.put("Amount", amount);
		cv.put("CategoryName", category);
		cv.put("Date", date);
		cv.put("Notes", notes);
		
		db.insert("Income_Details", null, cv);
	}
	
	public List<String> getAllIncome(Context context, int userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT * FROM Income_Details WHERE UserId = ?";
		Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});
		
		List<String> incomeDataList = new ArrayList<>();
		
		if (c != null && c.moveToFirst()) {
			do {
				int incomeId = c.getInt(0);
				double amount = c.getDouble(2);
				String categoryName = c.getString(3);
				String date = c.getString(4);
				String notes = c.getString(5);
				
				// Create a formatted string for each income entry
				String incomeEntry = "IncomeId: " + incomeId +
						"\nAmount: " + amount +
						"\nCategory: " + categoryName +
						"\nDate: " + date +
						"\nNotes: " + notes;
				
				incomeDataList.add(incomeEntry);
			} while (c.moveToNext());
			c.close();
		}
		return incomeDataList;
	}
	
	
	public void addExpense(int userId, double amount, String category, String payment, String date, String notes) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		// Find the last expense ID and add one to it for the new record
		String query = "SELECT MAX(ExpenseId) FROM Expense_Details";
		Cursor c = db.rawQuery(query, null);
		int lastExpenseId = 0;
		
		if (c != null && c.moveToFirst()) {
			lastExpenseId = c.getInt(0);
			lastExpenseId++; // Increment by one for the new record
			c.close();
		}
		
		ContentValues cv = new ContentValues();
		cv.put("ExpenseId", lastExpenseId);
		cv.put("UserId", userId);
		cv.put("Amount", amount);
		cv.put("CategoryName", category);
		cv.put("PaymentMethod", payment);
		cv.put("Date", date);
		cv.put("Notes", notes);
		
		db.insert("Expense_Details", null, cv);
	}
	
	public List<String> getAllExpense(Context context, int userId) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String query = "SELECT * FROM Expense_Details WHERE UserId = ?";
		Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});
		
		List<String> expenseDataList = new ArrayList<>();
		
		if (c != null && c.moveToFirst()) {
			do {
				int expenseId = c.getInt(0);
				double amount = c.getDouble(2);
				String categoryName = c.getString(3);
				String paymentMethod = c.getString(4);
				String date = c.getString(5);
				String notes = c.getString(6);
				
				// Create a formatted string for each income entry
				String expenseEntry = "Expense Id: " + expenseId +
						"\nAmount: " + amount +
						"\nCategory: " + categoryName +
						"\nPayment: " + paymentMethod +
						"\nDate: " + date +
						"\nNotes: " + notes;
				
				expenseDataList.add(expenseEntry);
			} while (c.moveToNext());
			c.close();
		}
		return expenseDataList;
	}
	
	public ArrayList<String> getCategory() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT CategoryName FROM Categories", null);
		
		ArrayList<String> categoryList = new ArrayList<>();
		
		while (c.moveToNext()) {
			String categoryName = c.getString(0);
			categoryList.add(categoryName);
		}
		c.close();
		
		return categoryList;
	}
	
	public int authenticateUser(String email, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT COUNT(*) FROM User_Details WHERE Email = ? AND Password = ?";
		Cursor c = db.rawQuery(query, new String[]{email, password});
		int result = 0;
		
		if (c != null && c.moveToFirst()) {
			result = c.getInt(0); // Get the count (0 or 1)
			c.close();
		}
		return result;
	}
	
	public void removeIncome(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String tableName = "Income_Details";
		String whereClause = "IncomeId = ?";
		String[] whereArgs = {String.valueOf(id)};
		
		db.delete(tableName, whereClause, whereArgs);
	}
	
	public void removeExpense(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		String tableName = "Expense_Details";
		String whereClause = "ExpenseId = ?";
		String[] whereArgs = {String.valueOf(id)};
		
		db.delete(tableName, whereClause, whereArgs);
	}
	
	
	
	
}


//need to close db in all function
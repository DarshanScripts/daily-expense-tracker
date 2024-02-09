package com.example.dailyexpensetracker;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {
	private static DataStorage instance;
	private List<String> incomeList;
	private List<String> expenseList;
	
	private DataStorage() {
		incomeList = new ArrayList<>();
		expenseList = new ArrayList<>();
	}
	
	public static synchronized DataStorage getInstance() {
		if (instance == null) {
			instance = new DataStorage();
		}
		return instance;
	}
	
	public List<String> getIncomeList() {
		return incomeList;
	}
	
	public List<String> getExpenseList() {
		return expenseList;
	}
	
	public void addIncome(String incomeEntry) {
		incomeList.add(incomeEntry);
	}
	
	public void addExpense(String expenseEntry) {
		expenseList.add(expenseEntry);
	}
}

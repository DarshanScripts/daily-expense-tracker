package com.example.dailyexpensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

//pref activity
public class Settings extends PreferenceActivity {
	private int selectedHour = 8; // Default to 8 AM
	private int selectedMinute = 0; // Default to 0 minutes past the hour
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		// Inside your Settings activity
		Preference alarmPreference = findPreference("prefAlarmSound");
		alarmPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// Open the AlarmSettingsActivity
				Intent intent = new Intent(getApplicationContext(), AlarmSettings.class);
				startActivity(intent);
				return true;
			}
		});
		
		ListPreference languagePreference = (ListPreference) findPreference("prefLang");
		ListPreference fontStylePreference = (ListPreference) findPreference("prefFontStyle");
		ListPreference fontSizePreference = (ListPreference) findPreference("prefFontSize");
		
		// Set listeners to save preferences when they change
		if (languagePreference != null) {
			languagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					savePreference("prefLang", newValue.toString());
					return true;
				}
			});
		}
		
		if (fontStylePreference != null) {
			fontStylePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					savePreference("prefFontStyle", newValue.toString());
					return true;
				}
			});
		}
		
		if (fontSizePreference != null) {
			fontSizePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					savePreference("prefFontSize", newValue.toString());
					return true;
				}
			});
		}
	}
	
	// Helper method to save a preference
	private void savePreference(String key, String value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.apply();
	}
}

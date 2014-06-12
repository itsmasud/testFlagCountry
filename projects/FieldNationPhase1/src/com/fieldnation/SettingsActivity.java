package com.fieldnation;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {
	private static final String TAG = "SettingsActivity";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref_general);
		bindPreferenceSummaryToValue(findPreference("sync_frequency"));
	}

	private static Preference.OnPreferenceChangeListener _preference_onChange = new Preference.OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			String stringValue = newValue.toString();

			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;

				int index = listPreference.findIndexOfValue(stringValue);

				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

			}
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(_preference_onChange);

		// Trigger the listener immediately with the preference's
		// current value.
		_preference_onChange.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

}

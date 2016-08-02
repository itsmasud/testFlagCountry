package com.fieldnation.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.crawler.WebCrawlerService;

/**
 * Created by Michael Carver on 4/15/2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //TODO need to call
//        _releaseNoteDialog = ReleaseNoteDialog.getInstance( getSupportFragmentManager(), TAG);
    }


    @Override
    public void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    preferenceGroup.getPreference(j).setOnPreferenceClickListener(preference_onClick_listener);
                    updatePreference(preferenceGroup.getPreference(j));
                }
            } else {
                preference.setOnPreferenceClickListener(preference_onClick_listener);
                updatePreference(preference);
            }
        }
    }


    @Override
    public void onPause() {
        super.onResume();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e("SettingsFragment", "onSharedPreferenceChanged");
        updatePreference(findPreference(key));
    }

    private void updatePreference(Preference preference) {
        if (preference == null)
            return;

        if (String.valueOf(preference.getKey()).equals(getActivity().getResources().getString(R.string.pref_key_sync_enabled)) && preference.isEnabled()) {
            getActivity().startService(new Intent(getActivity(), WebCrawlerService.class));
        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
        }

    }


    private Preference.OnPreferenceClickListener preference_onClick_listener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(Preference preference) {

            if ((preference.getKey()).equals(getActivity().getResources().getString(R.string.pref_key_release_declaration))) {
                Log.e("SettingsFragment", "release things clicked");
                Intent intent = new Intent(App.get(), NewFeatureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                App.get().startActivity(intent);
                return true;
            }
            return false;
        }
    };


//    @Override
//    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//        if ((preference.getKey()).equals(getActivity().getResources().getString(R.string.pref_key_release_declaration))) {
//           Log.e("SettingsFragment", "release things clicked");
//            return true;
//        }
//
//        else {
//            // if the button is anything but the simple toggle preference,
//            // we'll need to disable all preferences to reject all click
//            // events until the sub-activity's UI comes up.
//            preferenceScreen.setEnabled(false);
//            // Let the intents be launched by the Preference manager
//            return false;
//        }
//    }

}

package com.fieldnation.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.crawler.WebCrawlerService;
import com.fieldnation.v2.ui.dialog.WhatsNewDialog;

/**
 * Created by Michael Carver on 4/15/2015.
 */
public class GeneralSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "GeneralSettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        PreferenceCategory prefCat = (PreferenceCategory) findPreference(getString(R.string.pref_category_key_release_declaration));
        prefCat.setTitle(getString(R.string.pref_string_data_release_title, BuildConfig.VERSION_NAME));


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
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e("GeneralSettingsFragment", "onSharedPreferenceChanged");
        updatePreference(findPreference(key));
    }

    private void updatePreference(Preference preference) {
        if (preference == null)
            return;

//        if (String.valueOf(preference.getKey()).equals(getActivity().getResources().getString(R.string.pref_key_sync_enabled)) && preference.isEnabled()) {
//            getActivity().startService(new Intent(getActivity(), WebCrawlerService.class));
//        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
        }

    }

    private Preference.OnPreferenceClickListener preference_onClick_listener = new Preference.OnPreferenceClickListener() {

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey() != null && (preference.getKey()).equals(getActivity().getResources().getString(R.string.pref_key_release_declaration))) {
                Log.e("GeneralSettingsFragment", "release things clicked");
                WhatsNewDialog.show(App.get());
                return true;
            }
            return false;
        }
    };


//    @Override
//    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//        if ((preference.getKey()).equals(getActivity().getResources().getString(R.string.pref_key_release_declaration))) {
//           Log.e("GeneralSettingsFragment", "release things clicked");
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

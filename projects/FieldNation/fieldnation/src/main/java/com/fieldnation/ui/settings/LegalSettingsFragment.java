package com.fieldnation.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 4/15/2015.
 */
public class LegalSettingsFragment extends PreferenceFragment {
    private static final String TAG = "LegalSettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_legal);
    }
}

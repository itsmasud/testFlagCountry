package com.fieldnation.ui;

import android.os.Bundle;

import com.fieldnation.R;

/**
 * Gives the user the ability to configure the app
 *
 * @author michael.carver
 */
public class SettingsActivity extends AuthActionBarActivity {
    private static final String TAG = "SettingsActivity";

    @Override
    public int getLayoutResource() {
        return R.layout.activity_settings;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }
}

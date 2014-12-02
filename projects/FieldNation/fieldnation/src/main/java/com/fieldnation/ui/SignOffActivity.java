package com.fieldnation.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.fieldnation.R;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignOffActivity extends FragmentActivity {
    private static final String TAG = "ui.SignOffActivity";

    // Display Modes
    private static final int DISPLAY_SUMMARY = 1;
    private static final int DISPLAY_SIGNATURE = 2;
    private static final int DISPLAY_THANK_YOU = 3;

    // Intent Params
    public static final String PARAM_WORKORDER = "SignOffActivity.PARAM_WORKORDER";

    // Ui
    private SignOffFragment _signOffFrag;
    private SignatureFragment _sigFrag;

    // Data
    private int _displayMode = DISPLAY_SUMMARY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signoff);

        _signOffFrag = SignOffFragment.getInstance(getSupportFragmentManager(), TAG);
        _signOffFrag.setListener(_signOff_listener);

        _sigFrag = SignatureFragment.getInstance(getSupportFragmentManager(), TAG);

        if (savedInstanceState == null) {
            _signOffFrag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container_view, _signOffFrag).commit();

            _sigFrag.setArguments(getIntent().getExtras());
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private SignOffFragment.Listener _signOff_listener = new SignOffFragment.Listener() {
        @Override
        public void signOffOnClick() {
            _displayMode = DISPLAY_SIGNATURE;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _sigFrag);
            trans.addToBackStack(null);
            trans.commit();
        }
    };

    @Override
    public void onBackPressed() {
        if (_displayMode == DISPLAY_SIGNATURE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        super.onBackPressed();
    }
}

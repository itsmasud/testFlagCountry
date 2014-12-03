package com.fieldnation.ui;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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

    // State
    private static final String STATE_DISPLAY_MODE = "STATE_DISPLAY_MODE";
    private static final String STATE_SUMMARY_BITMAP = "STATE_SUMMARY_BITMAP";
    private static final String STATE_SIGNATURE_BITMAP = "STATE_SIGNATURE_BITMAP";

    // Display Modes
    private static final int DISPLAY_SUMMARY = 1;
    private static final int DISPLAY_SIGNATURE = 2;
    private static final int DISPLAY_THANK_YOU = 3;

    // Intent Params
    public static final String PARAM_WORKORDER = "SignOffActivity.PARAM_WORKORDER";

    // Ui
    private SignOffFragment _signOffFrag;
    private SignatureFragment _sigFrag;
    private ThankYouFragment _thankYouFrag;

    // Data
    private int _displayMode = DISPLAY_SUMMARY;
    private Bitmap _summaryBitmap;
    private Bitmap _signatureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signoff);

        _signOffFrag = SignOffFragment.getInstance(getSupportFragmentManager(), TAG);
        _signOffFrag.setListener(_signOff_listener);

        _sigFrag = SignatureFragment.getInstance(getSupportFragmentManager(), TAG);
        _sigFrag.setListener(_signature_listener);

        _thankYouFrag = ThankYouFragment.getInstance(getSupportFragmentManager(), TAG);
        _thankYouFrag.setListener(_thankyou_listener);

        if (savedInstanceState == null) {
            _signOffFrag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container_view, _signOffFrag).commit();

            _sigFrag.setArguments(getIntent().getExtras());
            _thankYouFrag.setArguments(getIntent().getExtras());
        } else {
            if (savedInstanceState.containsKey(STATE_DISPLAY_MODE))
                _displayMode = savedInstanceState.getInt(STATE_DISPLAY_MODE);

            if (savedInstanceState.containsKey(STATE_SUMMARY_BITMAP))
                _summaryBitmap = savedInstanceState.getParcelable(STATE_SUMMARY_BITMAP);

            if (savedInstanceState.containsKey(STATE_SIGNATURE_BITMAP))
                _signatureBitmap = savedInstanceState.getParcelable(STATE_SIGNATURE_BITMAP);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_DISPLAY_MODE, _displayMode);

        if (_summaryBitmap != null) {
            outState.putParcelable(STATE_SUMMARY_BITMAP, _summaryBitmap);
        }

        if (_signatureBitmap != null) {
            outState.putParcelable(STATE_SIGNATURE_BITMAP, _signatureBitmap);
        }

        super.onSaveInstanceState(outState);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private SignOffFragment.Listener _signOff_listener = new SignOffFragment.Listener() {
        @Override
        public void signOffOnClick(Bitmap bitmap) {
            _summaryBitmap = bitmap;
            _displayMode = DISPLAY_SIGNATURE;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _sigFrag);
            trans.addToBackStack(null);
            trans.commit();
        }
    };

    private SignatureFragment.Listener _signature_listener = new SignatureFragment.Listener() {
        @Override
        public void onBack() {
            onBackPressed();
        }

        @Override
        public void onSubmit(String name, Bitmap signature) {
            _displayMode = DISPLAY_THANK_YOU;
            _signatureBitmap = signature;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            _thankYouFrag.setImages(_summaryBitmap, signature);
            trans.replace(R.id.container_view, _thankYouFrag);
            trans.addToBackStack(null);
            trans.commit();
        }
    };

    private ThankYouFragment.Listener _thankyou_listener = new ThankYouFragment.Listener() {
        @Override
        public void onDoneClick() {
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        if (_displayMode == DISPLAY_SIGNATURE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            _displayMode = DISPLAY_SUMMARY;
        }

        // don't back out of thank you
        if (_displayMode == DISPLAY_THANK_YOU)
            return;

        super.onBackPressed();
    }
}

package com.fieldnation.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignOffActivity extends FragmentActivity {
    private static final String TAG = "ui.SignOffActivity";

    // State
    private static final String STATE_DISPLAY_MODE = "STATE_DISPLAY_MODE";
    private static final String STATE_NAME = "STATE_NAME";
    private static final String STATE_SIGNATURE = "STATE_SIGNATURE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Display Modes
    private static final int DISPLAY_SUMMARY = 1;
    private static final int DISPLAY_SIGNATURE = 2;
    private static final int DISPLAY_THANK_YOU = 3;

    // Intent Params
    public static final String INTENT_PARAM_WORKORDER = "SignOffActivity.INTENT_PARAM_WORKORDER";

    // Ui
    private SignOffFragment _signOffFrag;
    private SignatureFragment _sigFrag;
    private ThankYouFragment _thankYouFrag;

    // Data
    private GlobalState _gs;
    private WorkorderService _service;

    private int _displayMode = DISPLAY_SUMMARY;
    private String _name;
    private String _signatureJson;
    private Workorder _workorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signoff);

        _gs = (GlobalState) getApplicationContext();

        _signOffFrag = SignOffFragment.getInstance(getSupportFragmentManager(), TAG);
        _signOffFrag.setListener(_signOff_listener);

        _sigFrag = SignatureFragment.getInstance(getSupportFragmentManager(), TAG);
        _sigFrag.setListener(_signature_listener);

        _thankYouFrag = ThankYouFragment.getInstance(getSupportFragmentManager(), TAG);
        _thankYouFrag.setListener(_thankyou_listener);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(INTENT_PARAM_WORKORDER)) {
            _workorder = extras.getParcelable(INTENT_PARAM_WORKORDER);
        }

        if (savedInstanceState == null) {
            _signOffFrag.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container_view, _signOffFrag).commit();

            _sigFrag.setArguments(extras);
            _thankYouFrag.setArguments(extras);
        } else {
            if (savedInstanceState.containsKey(STATE_DISPLAY_MODE))
                _displayMode = savedInstanceState.getInt(STATE_DISPLAY_MODE);

            if (savedInstanceState.containsKey(STATE_NAME))
                _name = savedInstanceState.getString(STATE_NAME);

            if (savedInstanceState.containsKey(STATE_SIGNATURE))
                _signatureJson = savedInstanceState.getString(STATE_SIGNATURE);

            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_DISPLAY_MODE, _displayMode);

        if (_name != null)
            outState.putString(STATE_NAME, _name);

        if (_signatureJson != null)
            outState.putString(STATE_SIGNATURE, _signatureJson);

        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private SignOffFragment.Listener _signOff_listener = new SignOffFragment.Listener() {
        @Override
        public void signOffOnClick() {
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
        public void onSubmit(String name, String signatureJson) {
            _displayMode = DISPLAY_THANK_YOU;
            _name = name;
            _signatureJson = signatureJson;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _thankYouFrag);
            trans.addToBackStack(null);
            trans.commit();

            // TODO start upload
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

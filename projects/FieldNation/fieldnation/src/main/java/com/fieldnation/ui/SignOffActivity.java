package com.fieldnation.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignOffActivity extends AuthFragmentActivity {
    private static final String TAG = "ui.SignOffActivity";

    // State
    private static final String STATE_DISPLAY_MODE = "STATE_DISPLAY_MODE";
    private static final String STATE_NAME = "STATE_NAME";
    private static final String STATE_SIGNATURE = "STATE_SIGNATURE";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";
    private static final String STATE_TASK_ID = "STATE_TASK_ID";
    private static final String STATE_COMPLETE_WORKORDER = "COMPLETE_WORKORDER";

    // Display Modes
    private static final int DISPLAY_SUMMARY = 1;
    private static final int DISPLAY_SIGNATURE = 2;
    private static final int DISPLAY_THANK_YOU = 3;
    private static final int DISPLAY_SORRY = 4;


    // Intent Params
    public static final String INTENT_PARAM_WORKORDER = "SignOffActivity.INTENT_PARAM_WORKORDER";
    public static final String INTENT_PARAM_TASK_ID = "SignOffActivity.INTENT_PARAM_TASK_ID";
    public static final String INTENT_COMPLETE_WORKORDER = "SignOffActivity.INTENT_COMPLETE_WORKORDER";

    // Web
    private static final int WEB_COMPLETE_TASK = 1;
    private static final int WEB_UPLOAD_SIGNATURE = 2;
    private static final int WEB_COMPLETE_WORKORDER = 3;

    // Ui
    private SignOffFragment _signOffFrag;
    private SignatureFragment _sigFrag;
    private ThankYouFragment _thankYouFrag;
    private SorryFragment _sorryFrag;

    // Data
    private GlobalState _gs;
    private WorkorderService _service;

    private int _displayMode = DISPLAY_SUMMARY;
    private String _name;
    private String _signatureJson;
    private Workorder _workorder;
    private long _taskId = -1;
    private boolean _completeWorkorder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signoff);

        _gs = (GlobalState) getApplicationContext();
        _gs.requestAuthentication(_authClient);

        _signOffFrag = SignOffFragment.getInstance(getSupportFragmentManager(), TAG);
        _signOffFrag.setListener(_signOff_listener);

        _sigFrag = SignatureFragment.getInstance(getSupportFragmentManager(), TAG);
        _sigFrag.setListener(_signature_listener);

        _thankYouFrag = ThankYouFragment.getInstance(getSupportFragmentManager(), TAG);
        _thankYouFrag.setListener(_thankyou_listener);

        _sorryFrag = SorryFragment.getInstance(getSupportFragmentManager(), TAG);
        _sorryFrag.setListener(_sorry_listener);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INTENT_PARAM_WORKORDER))
                _workorder = extras.getParcelable(INTENT_PARAM_WORKORDER);

            if (extras.containsKey(INTENT_PARAM_TASK_ID))
                _taskId = extras.getLong(INTENT_PARAM_TASK_ID);

            if (extras.containsKey(INTENT_COMPLETE_WORKORDER))
                _completeWorkorder = extras.getBoolean(INTENT_COMPLETE_WORKORDER);
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

            if (savedInstanceState.containsKey(STATE_TASK_ID))
                _taskId = savedInstanceState.getLong(STATE_TASK_ID);

            if (savedInstanceState.containsKey(STATE_COMPLETE_WORKORDER))
                _completeWorkorder = savedInstanceState.getBoolean(STATE_COMPLETE_WORKORDER);
        }
    }

    @Override
    public void onRefresh() {
        // TODO STUB com.fieldnation.ui.SignOffActivity.onRefresh()
        Log.v(TAG, "STUB com.fieldnation.ui.SignOffActivity.onRefresh()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_DISPLAY_MODE, _displayMode);
        outState.putLong(STATE_TASK_ID, _taskId);
        outState.putBoolean(STATE_COMPLETE_WORKORDER, _completeWorkorder);

        if (_name != null)
            outState.putString(STATE_NAME, _name);

        if (_signatureJson != null)
            outState.putString(STATE_SIGNATURE, _signatureJson);

        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    private void sendSignature() {
        // not a task
        if (_taskId == -1) {
            startService(
                    _service.addSignatureJson(WEB_UPLOAD_SIGNATURE, _workorder.getWorkorderId(), _name, _signatureJson));
        } else {
            // is a task
            startService(
                    _service.completeSignatureTaskJson(WEB_COMPLETE_TASK, _workorder.getWorkorderId(),
                            _taskId, _name, _signatureJson));
        }
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

        @Override
        public void rejectOnClick() {
            _displayMode = DISPLAY_SORRY;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _sorryFrag);
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

            sendSignature();
        }
    };

    private ThankYouFragment.Listener _thankyou_listener = new ThankYouFragment.Listener() {
        @Override
        public void onDoneClick() {
            setResult(RESULT_OK);
            finish();
        }
    };

    private SorryFragment.Listener _sorry_listener = new SorryFragment.Listener() {
        @Override
        public void onDoneClick() {
            setResult(RESULT_CANCELED);
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

    /*-******************************-*/
    /*-             Web              -*/
    /*-******************************-*/
    private AuthenticationClient _authClient = new AuthenticationClient() {
        @Override
        public void onAuthentication(String username, String authToken) {
            try {
                _service = new WorkorderService(SignOffActivity.this, username, authToken, _resultReceiver);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {

            if (resultCode == WEB_COMPLETE_WORKORDER) {
                // we completed the workorder... done
                _thankYouFrag.setUploadComplete();

            } else if (resultCode == WEB_COMPLETE_TASK || resultCode == WEB_UPLOAD_SIGNATURE) {
                // we finished uploading the signature
                if (_completeWorkorder) {
                    // if we need to complete, then start that process
                    startService(
                            _service.complete(WEB_COMPLETE_WORKORDER, _workorder.getWorkorderId()));

                } else {
                    // otherwise we're done
                    _thankYouFrag.setUploadComplete();
                }
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_service != null) {
                _gs.invalidateAuthToken(_service.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
            Toast.makeText(SignOffActivity.this, "Could not complete request", Toast.LENGTH_LONG).show();
            _thankYouFrag.setUploadComplete();
        }
    };


}

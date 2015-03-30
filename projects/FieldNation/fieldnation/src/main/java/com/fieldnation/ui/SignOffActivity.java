package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.GlobalState;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderDataClient;
import com.fieldnation.utils.Stopwatch;

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
    private WorkorderDataClient _workorderClient;

    private int _displayMode = DISPLAY_SUMMARY;
    private String _name;
    private String _signatureJson;
    private Workorder _workorder;
    private long _taskId = -1;
    private boolean _completeWorkorder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Stopwatch stopwatch = new Stopwatch();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signoff);

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
            new AsyncTaskEx<Bundle, Object, Object[]>() {
                @Override
                protected Object[] doInBackground(Bundle... params) {
                    Bundle extras = params[0];
                    Workorder workorder = null;
                    Long taskId = _taskId;
                    Boolean completeWorkorder = _completeWorkorder;

                    if (extras.containsKey(INTENT_PARAM_WORKORDER))
                        workorder = extras.getParcelable(INTENT_PARAM_WORKORDER);

                    if (extras.containsKey(INTENT_PARAM_TASK_ID))
                        taskId = extras.getLong(INTENT_PARAM_TASK_ID);

                    if (extras.containsKey(INTENT_COMPLETE_WORKORDER))
                        completeWorkorder = extras.getBoolean(INTENT_COMPLETE_WORKORDER);

                    return new Object[]{workorder, taskId, completeWorkorder};
                }

                @Override
                protected void onPostExecute(Object[] objects) {
                    _workorder = (Workorder) objects[0];
                    _taskId = (Long) objects[1];
                    _completeWorkorder = (Boolean) objects[2];

                }
            }.executeEx(extras);

            if (savedInstanceState == null) {
                _signOffFrag.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.container_view, _signOffFrag).commit();
                GoogleAnalyticsTopicClient.dispatchScreenView(this, "SignOffFragment");
            }
        } else if (savedInstanceState != null) {
            new AsyncTaskEx<Bundle, Object, Object[]>() {
                @Override
                protected Object[] doInBackground(Bundle... params) {
                    Bundle savedInstanceState = params[0];
                    int displayMode = _displayMode;
                    String name = _name;
                    String signatureJson = _signatureJson;
                    Workorder workorder = _workorder;
                    Long taskId = _taskId;
                    Boolean completeWorkorder = _completeWorkorder;

                    if (savedInstanceState.containsKey(STATE_DISPLAY_MODE))
                        displayMode = savedInstanceState.getInt(STATE_DISPLAY_MODE);

                    if (savedInstanceState.containsKey(STATE_NAME))
                        name = savedInstanceState.getString(STATE_NAME);

                    if (savedInstanceState.containsKey(STATE_SIGNATURE))
                        signatureJson = savedInstanceState.getString(STATE_SIGNATURE);

                    if (savedInstanceState.containsKey(STATE_WORKORDER))
                        workorder = savedInstanceState.getParcelable(STATE_WORKORDER);

                    if (savedInstanceState.containsKey(STATE_TASK_ID))
                        taskId = savedInstanceState.getLong(STATE_TASK_ID);

                    if (savedInstanceState.containsKey(STATE_COMPLETE_WORKORDER))
                        completeWorkorder = savedInstanceState.getBoolean(STATE_COMPLETE_WORKORDER);
                    return new Object[]{displayMode, name, signatureJson, workorder, taskId, completeWorkorder};
                }

                @Override
                protected void onPostExecute(Object[] objects) {
                    super.onPostExecute(objects);
                    _displayMode = (Integer) objects[0];
                    _name = (String) objects[1];
                    _signatureJson = (String) objects[2];
                    _workorder = (Workorder) objects[3];
                    _taskId = (Long) objects[4];
                    _completeWorkorder = (Boolean) objects[5];
                }
            }.executeEx(savedInstanceState);
        }

        Log.v(TAG, "onCreate time " + stopwatch.finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workorderClient = new WorkorderDataClient(_workorderClient_listener);
        _workorderClient.connect(this);
    }

    @Override
    protected void onPause() {
        _workorderClient.disconnect(this);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Stopwatch stopwatch = new Stopwatch();
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
        Log.v(TAG, "onSave time " + stopwatch.finish());
    }

    private void sendSignature() {
        // not a task
// TODO remove
        if (_taskId == -1) {
            WorkorderDataClient.requestAddSignatureJson(this, _workorder.getWorkorderId(), _name, _signatureJson);
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
    private final WorkorderDataClient.Listener _workorderClient_listener = new WorkorderDataClient.Listener() {
        @Override
        public void onConnected() {

        }
    };

    private SignOffFragment.Listener _signOff_listener = new SignOffFragment.Listener() {
        @Override
        public void signOffOnClick() {
            _displayMode = DISPLAY_SIGNATURE;
            GoogleAnalyticsTopicClient.dispatchScreenView(SignOffActivity.this, "SignatureFragment");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _sigFrag);
            trans.addToBackStack(null);
            trans.commit();
        }

        @Override
        public void rejectOnClick() {
            _displayMode = DISPLAY_SORRY;
            GoogleAnalyticsTopicClient.dispatchScreenView(SignOffActivity.this, "SorryFragment");
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
// todo remove
            GaTopic.dispatchScreenView(SignOffActivity.this, "ThankYouFragment");
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
    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {

            if (resultCode == WEB_COMPLETE_WORKORDER) {
                // we completed the workorder... done
                _thankYouFrag.setUploadComplete();

                ((GlobalState) getApplication()).setCompletedWorkorder();

            } else if (resultCode == WEB_COMPLETE_TASK || resultCode == WEB_UPLOAD_SIGNATURE) {
                // we finished uploading the signature
                if (_completeWorkorder) {
                    // if we need to complete, then start that process
// todo remove
                    GoogleAnalyticsTopicClient.dispatchEvent(SignOffActivity.this, "WorkorderActivity", GoogleAnalyticsTopicClient.EventAction.COMPLETE_WORK, "SignOffActivity", 1);
                    startService(
                            _service.complete(WEB_COMPLETE_WORKORDER, _workorder.getWorkorderId()));

                } else {
                    // otherwise we're done
                    _thankYouFrag.setUploadComplete();
                }
            }
        }

        @Override
        public Context getContext() {
            return SignOffActivity.this;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            AuthTopicService.requestAuthInvalid(SignOffActivity.this);
            Toast.makeText(SignOffActivity.this, "Could not complete request", Toast.LENGTH_LONG).show();
            _thankYouFrag.setUploadComplete();
        }
    };


    public static void startSignOff(Context context, Workorder workorder) {
        startSignOff(context, workorder, false);
    }

    public static void startSignOff(Context context, Workorder workorder, boolean markComplete) {
        new AsyncTaskEx<Object, Object, Intent>() {
            private Context context;

            @Override
            protected Intent doInBackground(Object... params) {
                context = (Context) params[0];
                Workorder workorder = (Workorder) params[1];
                Boolean markComplete = (Boolean) params[2];

                Intent intent = new Intent(context, SignOffActivity.class);
                intent.putExtra(SignOffActivity.INTENT_PARAM_WORKORDER, workorder);
                if (markComplete)
                    intent.putExtra(INTENT_COMPLETE_WORKORDER, true);

                if (!(context instanceof Activity))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                return intent;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                super.onPostExecute(intent);
                context.startActivity(intent);
            }
        }.executeEx(context, workorder, markComplete);
    }

    public static void startSignOff(Context context, Workorder workorder, long taskId) {
        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                Context context = (Context) params[0];
                Workorder workorder = (Workorder) params[1];
                Long taskId = (Long) params[2];

                Intent intent = new Intent(context, SignOffActivity.class);
                intent.putExtra(INTENT_PARAM_WORKORDER, workorder);
                intent.putExtra(INTENT_PARAM_TASK_ID, taskId);
                if (!(context instanceof Activity))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return null;
            }
        }.executeEx(context, workorder, taskId);
    }

}

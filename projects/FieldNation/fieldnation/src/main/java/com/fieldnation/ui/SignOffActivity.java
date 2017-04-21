package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignOffActivity extends AuthSimpleActivity {
    private static final String TAG = "SignOffActivity";

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

    // Ui
    private SignOffFragment _signOffFrag;
    private SignatureFragment _sigFrag;
    private ThankYouFragment _thankYouFrag;
    private SorryFragment _sorryFrag;

    // Data
    private int _displayMode = DISPLAY_SUMMARY;
    private String _name;
    private String _signatureSvg;
    private WorkOrder _workOrder;
    private int _taskId = -1;
    private boolean _completeWorkorder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        App.get().spUiContext.page = "Collect Signature";
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_signoff;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Stopwatch stopwatch = new Stopwatch();
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
                    WorkOrder workorder = null;
                    Integer taskId = _taskId;
                    Boolean completeWorkorder = _completeWorkorder;

                    if (extras.containsKey(INTENT_PARAM_WORKORDER))
                        workorder = extras.getParcelable(INTENT_PARAM_WORKORDER);

                    if (extras.containsKey(INTENT_PARAM_TASK_ID))
                        taskId = extras.getInt(INTENT_PARAM_TASK_ID);

                    if (extras.containsKey(INTENT_COMPLETE_WORKORDER))
                        completeWorkorder = extras.getBoolean(INTENT_COMPLETE_WORKORDER);

                    return new Object[]{workorder, taskId, completeWorkorder};
                }

                @Override
                protected void onPostExecute(Object[] objects) {
                    _workOrder = (WorkOrder) objects[0];
                    _taskId = (Integer) objects[1];
                    _completeWorkorder = (Boolean) objects[2];
                }
            }.executeEx(extras);

            if (savedInstanceState == null) {
                _signOffFrag.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.container_view, _signOffFrag).commit();
            }
        } else if (savedInstanceState != null) {
            new AsyncTaskEx<Bundle, Object, Object[]>() {
                @Override
                protected Object[] doInBackground(Bundle... params) {
                    Bundle savedInstanceState = params[0];
                    int displayMode = _displayMode;
                    String name = _name;
                    String signatureSvg = _signatureSvg;
                    WorkOrder workOrder = _workOrder;
                    Integer taskId = _taskId;
                    Boolean completeWorkorder = _completeWorkorder;

                    if (savedInstanceState.containsKey(STATE_DISPLAY_MODE))
                        displayMode = savedInstanceState.getInt(STATE_DISPLAY_MODE);

                    if (savedInstanceState.containsKey(STATE_NAME))
                        name = savedInstanceState.getString(STATE_NAME);

                    if (savedInstanceState.containsKey(STATE_SIGNATURE))
                        signatureSvg = savedInstanceState.getString(STATE_SIGNATURE);

                    if (savedInstanceState.containsKey(STATE_WORKORDER))
                        workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);

                    if (savedInstanceState.containsKey(STATE_TASK_ID))
                        taskId = savedInstanceState.getInt(STATE_TASK_ID);

                    if (savedInstanceState.containsKey(STATE_COMPLETE_WORKORDER))
                        completeWorkorder = savedInstanceState.getBoolean(STATE_COMPLETE_WORKORDER);
                    return new Object[]{displayMode, name, signatureSvg, workOrder, taskId, completeWorkorder};
                }

                @Override
                protected void onPostExecute(Object[] objects) {
                    super.onPostExecute(objects);
                    _displayMode = (Integer) objects[0];
                    _name = (String) objects[1];
                    _signatureSvg = (String) objects[2];
                    _workOrder = (WorkOrder) objects[3];
                    _taskId = (int) objects[4];
                    _completeWorkorder = (Boolean) objects[5];
                }
            }.executeEx(savedInstanceState);
        }

        Log.v(TAG, "onCreate time " + stopwatch.finish());
    }

    @Override
    public int getToolbarId() {
        return 0;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Stopwatch stopwatch = new Stopwatch();
        outState.putInt(STATE_DISPLAY_MODE, _displayMode);
        outState.putLong(STATE_TASK_ID, _taskId);
        outState.putBoolean(STATE_COMPLETE_WORKORDER, _completeWorkorder);

        if (_name != null)
            outState.putString(STATE_NAME, _name);

        if (_signatureSvg != null)
            outState.putString(STATE_SIGNATURE, _signatureSvg);

        if (_workOrder != null)
            outState.putParcelable(STATE_WORKORDER, _workOrder);

        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSave time " + stopwatch.finish());
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private void sendSignature() {
        try {
            Signature signature = new Signature();
            signature.name(_name);
            signature.format("svg");
            signature.data(_signatureSvg);
            if (_taskId != -1) {
                signature.task(new Task().id(_taskId));
            }

            WorkordersWebApi.addSignature(App.get(), _workOrder.getId(), signature);

            if (_completeWorkorder) {
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE,
                        WorkOrderTracker.Action.MARK_COMPLETE, _workOrder.getId());
                WorkordersWebApi.completeWorkOrder(App.get(), _workOrder.getId());
                WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getId(), false, false);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        _thankYouFrag.setUploadComplete();
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final SignOffFragment.Listener _signOff_listener = new SignOffFragment.Listener() {
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
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _sorryFrag);
            trans.addToBackStack(null);
            trans.commit();
        }
    };

    private final SignatureFragment.Listener _signature_listener = new SignatureFragment.Listener() {
        @Override
        public void onBack() {
            onBackPressed();
        }

        @Override
        public void onSubmit(String name, String signatureSvg) {
            _displayMode = DISPLAY_THANK_YOU;
            _name = name;
            _signatureSvg = signatureSvg;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container_view, _thankYouFrag);
            trans.addToBackStack(null);
            trans.commit();

            sendSignature();
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);
        }
    };

    private final ThankYouFragment.Listener _thankyou_listener = new ThankYouFragment.Listener() {
        @Override
        public void onDoneClick() {
            Log.v(TAG, "_thankyou_listener.onDone");
            if (getParent() == null) {
                Log.v(TAG, "no Parent");
                setResult(RESULT_OK);
            } else {
                Log.v(TAG, "has parent");
                getParent().setResult(RESULT_OK);
            }
            finish();
        }
    };

    private final SorryFragment.Listener _sorry_listener = new SorryFragment.Listener() {
        @Override
        public void onDoneClick() {
            if (getParent() == null) {
                setResult(RESULT_CANCELED);
            } else {
                getParent().setResult(RESULT_CANCELED);
            }
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
        if (_displayMode == DISPLAY_THANK_YOU || _displayMode == DISPLAY_SORRY)
            return;

        super.onBackPressed();
    }

    public static void startSignOff(Context context, WorkOrder workOrder) {
        startSignOff(context, workOrder, false);
    }

    public static void startSignOff(Context context, WorkOrder workOrder, boolean markComplete) {
        Intent intent = new Intent(context, SignOffActivity.class);
        intent.putExtra(SignOffActivity.INTENT_PARAM_WORKORDER, workOrder);
        if (markComplete)
            intent.putExtra(INTENT_COMPLETE_WORKORDER, true);

        ActivityResultClient.startActivityForResult(context, intent, ActivityResultConstants.RESULT_CODE_GET_SIGNATURE);
    }

    public static void startSignOff(Context context, WorkOrder workOrder, int taskId) {
        Intent intent = new Intent(context, SignOffActivity.class);
        intent.putExtra(INTENT_PARAM_WORKORDER, workOrder);
        intent.putExtra(INTENT_PARAM_TASK_ID, taskId);
        ActivityResultClient.startActivity(context, intent);
    }
}
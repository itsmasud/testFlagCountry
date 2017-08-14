package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
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
    private SignOffScreen _signOffScreen;
    private SignatureScreen _sigScreen;
    private ThankYouScreen _thankYouScreen;
    private SorryScreen _sorryScreen;

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
        App.get().getSpUiContext().page = "Collect Signature";
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_signoff;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        _signOffScreen = (SignOffScreen) findViewById(R.id.signOff_screen);
        _signOffScreen.setListener(_signOff_listener);
        _sigScreen = (SignatureScreen) findViewById(R.id.signature_screen);
        _sigScreen.setListener(_signature_listener);
        _thankYouScreen = (ThankYouScreen) findViewById(R.id.thankYou_screen);
        _thankYouScreen.setListener(_thankyou_listener);
        _sorryScreen = (SorryScreen) findViewById(R.id.sorry_screen);
        _sorryScreen.setListener(_sorry_listener);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(INTENT_PARAM_WORKORDER))
                _workOrder = extras.getParcelable(INTENT_PARAM_WORKORDER);

            if (extras.containsKey(INTENT_PARAM_TASK_ID))
                _taskId = extras.getInt(INTENT_PARAM_TASK_ID);

            if (extras.containsKey(INTENT_COMPLETE_WORKORDER))
                _completeWorkorder = extras.getBoolean(INTENT_COMPLETE_WORKORDER);
        }
        populateUi();
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
        outState.putInt(STATE_DISPLAY_MODE, _displayMode);
        outState.putInt(STATE_TASK_ID, _taskId);
        outState.putBoolean(STATE_COMPLETE_WORKORDER, _completeWorkorder);

        if (_name != null)
            outState.putString(STATE_NAME, _name);

        if (_signatureSvg != null)
            outState.putString(STATE_SIGNATURE, _signatureSvg);

        if (_workOrder != null)
            outState.putParcelable(STATE_WORKORDER, _workOrder);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(STATE_DISPLAY_MODE))
            _displayMode = savedInstanceState.getInt(STATE_DISPLAY_MODE);

        if (savedInstanceState.containsKey(STATE_NAME))
            _name = savedInstanceState.getString(STATE_NAME);

        if (savedInstanceState.containsKey(STATE_SIGNATURE))
            _signatureSvg = savedInstanceState.getString(STATE_SIGNATURE);

        if (savedInstanceState.containsKey(STATE_WORKORDER))
            _workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);

        if (savedInstanceState.containsKey(STATE_TASK_ID))
            _taskId = savedInstanceState.getInt(STATE_TASK_ID);

        if (savedInstanceState.containsKey(STATE_COMPLETE_WORKORDER))
            _completeWorkorder = savedInstanceState.getBoolean(STATE_COMPLETE_WORKORDER);

        populateUi();
    }

    private void populateUi() {
        _sorryScreen.setVisibility(View.GONE);
        _signOffScreen.setVisibility(View.GONE);
        _sigScreen.setVisibility(View.GONE);
        _thankYouScreen.setVisibility(View.GONE);

        switch (_displayMode) {
            case DISPLAY_SIGNATURE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                _sigScreen.setVisibility(View.VISIBLE);
                break;
            case DISPLAY_SORRY:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                _sorryScreen.setVisibility(View.VISIBLE);
                _sorryScreen.startTimer();
                break;
            case DISPLAY_SUMMARY:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                _signOffScreen.setVisibility(View.VISIBLE);
                break;
            case DISPLAY_THANK_YOU:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                _thankYouScreen.setVisibility(View.VISIBLE);
                _thankYouScreen.startTimer();
                break;
        }
        _signOffScreen.setWorkOrder(_workOrder);
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

            SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
            uiContext.page += " - Collect Signature";
            WorkordersWebApi.addSignature(App.get(), _workOrder.getId(), signature, uiContext);

            if (_completeWorkorder) {
                WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.MARK_COMPlETE,
                        WorkOrderTracker.Action.MARK_COMPLETE, _workOrder.getId());
                WorkordersWebApi.completeWorkOrder(App.get(), _workOrder.getId(), uiContext);
                WorkordersWebApi.getWorkOrder(App.get(), _workOrder.getId(), false, false);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final SignOffScreen.Listener _signOff_listener = new SignOffScreen.Listener() {
        @Override
        public void signOffOnClick() {
            _displayMode = DISPLAY_SIGNATURE;
            populateUi();
        }

        @Override
        public void rejectOnClick() {
            _displayMode = DISPLAY_SORRY;
            populateUi();
        }
    };

    private final SignatureScreen.Listener _signature_listener = new SignatureScreen.Listener() {
        @Override
        public void onBack() {
            _displayMode = DISPLAY_SUMMARY;
            populateUi();
        }

        @Override
        public void onSubmit(String name, String signatureSvg) {
            _displayMode = DISPLAY_THANK_YOU;
            _name = name;
            _signatureSvg = signatureSvg;

            sendSignature();
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);

            populateUi();
        }
    };

    private final ThankYouScreen.Listener _thankyou_listener = new ThankYouScreen.Listener() {
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

    private final SorryScreen.Listener _sorry_listener = new SorryScreen.Listener() {
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
            populateUi();
            return;
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

        if (markComplete)
            ActivityResultClient.startActivityForResult(context, intent, ActivityResultConstants.RESULT_CODE_GET_SIGNATURE);
        else
            ActivityResultClient.startActivity(context, intent);
    }

    public static void startSignOff(Context context, WorkOrder workOrder, int taskId) {
        Intent intent = new Intent(context, SignOffActivity.class);
        intent.putExtra(INTENT_PARAM_WORKORDER, workOrder);
        intent.putExtra(INTENT_PARAM_TASK_ID, taskId);
        ActivityResultClient.startActivity(context, intent);
    }
}
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
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityResultConstants;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class SignOffActivity extends AuthSimpleActivity {
    private static final String TAG = "SignOffActivity";

    // Display Modes
    private static final int DISPLAY_SUMMARY = 1;
    private static final int DISPLAY_SIGNATURE = 2;
    private static final int DISPLAY_THANK_YOU = 3;
    private static final int DISPLAY_SORRY = 4;

    // Ui
    private SignOffScreen _signOffScreen;
    private SignatureScreen _sigScreen;
    private ThankYouScreen _thankYouScreen;
    private SorryScreen _sorryScreen;

    // Data
    private int _displayMode = DISPLAY_SUMMARY;
    private String _name;
    private String _signatureSvg;
    private int _workOrderId;
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
        Log.v(TAG, "onFinishCreate");
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
            if (extras.containsKey("_workOrderId"))
                _workOrderId = extras.getInt("_workOrderId");

            if (extras.containsKey("_taskId"))
                _taskId = extras.getInt("_taskId");

            if (extras.containsKey("_completeWorkorder"))
                _completeWorkorder = extras.getBoolean("_completeWorkorder");
        }

        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
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
    protected void onResume() {
        super.onResume();
        _workOrderApi.sub();
    }

    @Override
    protected void onPause() {
        _workOrderApi.unsub();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putInt("_displayMode", _displayMode);
        outState.putInt("_taskId", _taskId);
        outState.putBoolean("_completeWorkorder", _completeWorkorder);
        outState.putInt("_workOrderId", _workOrderId);

        if (_name != null)
            outState.putString("_name", _name);

        if (_signatureSvg != null)
            outState.putString("_signatureSvg", _signatureSvg);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("_displayMode"))
            _displayMode = savedInstanceState.getInt("_displayMode");

        if (savedInstanceState.containsKey("_name"))
            _name = savedInstanceState.getString("_name");

        if (savedInstanceState.containsKey("_signatureSvg"))
            _signatureSvg = savedInstanceState.getString("_signatureSvg");

        if (savedInstanceState.containsKey("_workOrderId"))
            _workOrderId = savedInstanceState.getInt("_workOrderId");

        if (savedInstanceState.containsKey("_taskId"))
            _taskId = savedInstanceState.getInt("_taskId");

        if (savedInstanceState.containsKey("_completeWorkorder"))
            _completeWorkorder = savedInstanceState.getBoolean("_completeWorkorder");

        populateUi();
    }

    private void populateUi() {
        Log.v(TAG, "populateUi");
        _sorryScreen.setVisibility(View.GONE);
        _signOffScreen.setVisibility(View.GONE);
        _sigScreen.setVisibility(View.GONE);
        _thankYouScreen.setVisibility(View.GONE);

        switch (_displayMode) {
            case DISPLAY_SIGNATURE:
                _sigScreen.setVisibility(View.VISIBLE);
                break;
            case DISPLAY_SORRY:
                _sorryScreen.setVisibility(View.VISIBLE);
                _sorryScreen.startTimer();
                break;
            case DISPLAY_SUMMARY:
                _signOffScreen.setVisibility(View.VISIBLE);
                break;
            case DISPLAY_THANK_YOU:
                _thankYouScreen.setVisibility(View.VISIBLE);
                _thankYouScreen.startTimer();
                break;
        }

        if (_workOrder != null)
            _signOffScreen.setWorkOrder(_workOrder);
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private void sendSignature() {
        Log.v(TAG, "sendSignature");
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
            Log.v(TAG, "SignOffScreen.signOffOnClick");
            _displayMode = DISPLAY_SIGNATURE;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            populateUi();
        }

        @Override
        public void rejectOnClick() {
            Log.v(TAG, "SignOffScreen.rejectOnClick");
            _displayMode = DISPLAY_SORRY;
            populateUi();
        }
    };

    private final SignatureScreen.Listener _signature_listener = new SignatureScreen.Listener() {
        @Override
        public void onBack() {
            Log.v(TAG, "SignatureScreen.onBack");
            _displayMode = DISPLAY_SUMMARY;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            populateUi();
        }

        @Override
        public void onSubmit(String name, String signatureSvg) {
            Log.v(TAG, "SignatureScreen.onSubmit");
            _displayMode = DISPLAY_THANK_YOU;
            _name = name;
            _signatureSvg = signatureSvg;

            sendSignature();
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            populateUi();
        }
    };

    private final ThankYouScreen.Listener _thankyou_listener = new ThankYouScreen.Listener() {
        @Override
        public void onDoneClick() {
            Log.v(TAG, "ThankYouScreen.onDoneClick");
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
            Log.v(TAG, "SorryScreen.onDoneClick");
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
        Log.v(TAG, "onBackPressed");
        if (_displayMode == DISPLAY_SIGNATURE) {
            populateUi();
            return;
        }

        // don't back out of thank you
        if (_displayMode == DISPLAY_THANK_YOU || _displayMode == DISPLAY_SORRY)
            return;

        super.onBackPressed();
    }

    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrder");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (success && successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (workOrder.getId() == _workOrderId) {
                    _workOrder = workOrder;
                    populateUi();
                }
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void startSignOff(Context context, int workOrderId) {
        startSignOff(context, workOrderId, false);
    }

    public static void startSignOff(Context context, int workOrderId, boolean markComplete) {
        Intent intent = new Intent(context, SignOffActivity.class);
        intent.putExtra("_workOrderId", workOrderId);
        if (markComplete)
            intent.putExtra("_completeWorkorder", true);

        if (markComplete)
            ActivityClient.startActivityForResult(intent, ActivityResultConstants.RESULT_CODE_GET_SIGNATURE);
        else
            ActivityClient.startActivity(intent);
    }

    public static void startSignOff(Context context, int workOrderId, int taskId) {
        Intent intent = new Intent(context, SignOffActivity.class);
        intent.putExtra("_workOrderId", workOrderId);
        intent.putExtra("_taskId", taskId);
        ActivityClient.startActivity(intent);

    }
}
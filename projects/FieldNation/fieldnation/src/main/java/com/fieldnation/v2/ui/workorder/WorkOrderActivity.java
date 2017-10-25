package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.AttachedFilesDialog;
import com.fieldnation.v2.ui.dialog.ChatDialog;

import java.util.List;
import java.util.UUID;

public class WorkOrderActivity extends AuthSimpleActivity {
    private static final String TAG = "WorkOrderActivity";

    // Intent stuff
    public static final String INTENT_FIELD_WORKORDER_ID = TAG + ".workOrderId";
    public static final String INTENT_FIELD_WORKORDER = TAG + ".workOrder";
    public static final String INTENT_FIELD_ACTION = TAG + ".action";
    public static final String INTENT_UI_UUID = TAG + ".uuid";
    public static final String ACTION_ATTACHMENTS = "ACTION_ATTACHMENTS";
    public static final String ACTION_MESSAGES = "ACTION_MESSAGES";
    public static final String ACTION_CONFIRM = "ACTION_CONFIRM";

    // UI
    private WorkOrderScreen _workOrderScreen;

    // Data
    private int _workOrderId;
    private WorkOrder _workOrder;
    private boolean _showAttachments = false;
    private boolean _attachmentsShown = false;
    private boolean _showMessages = false;
    private boolean _messagesShown = false;
    private String _myUUID;

    /*-*************************************-*/
    /*-				LifeCycle				-*/
    /*-*************************************-*/

    @Override
    public int getLayoutResource() {
        return R.layout.activity_v2_work_order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _workOrderScreen = (WorkOrderScreen) findViewById(R.id.workOrderScreen);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onFinishCreate");
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
                _workOrderId = intent.getIntExtra(INTENT_FIELD_WORKORDER_ID, 0);
            }

            if (intent.hasExtra(INTENT_UI_UUID)) {
                _myUUID = intent.getStringExtra(INTENT_UI_UUID);
            }

            // taking a link from e-mail/browser
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                try {
                    final List<String> segments = intent.getData().getPathSegments();
                    if (segments.size() > 1) {
                        if (segments.get(0).equals("wo")) {
                            _workOrderId = Integer.parseInt(segments.get(1));
                        } else if (segments.get(0).equals("workorder")) {
                            _workOrderId = Integer.parseInt(segments.get(2));
                        } else if (segments.get(0).equals("marketplace")) {
                            _workOrderId = Integer.parseInt(intent.getData().getQueryParameter("workorder_id"));
                        } else if (segments.get(0).equals("w") && segments.get(1).equals("r")) {
                            _workOrderId = Integer.parseInt(segments.get(2));
                        }
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("workOrderId")) {
                _workOrderId = savedInstanceState.getInt("workOrderId");
            }
            if (savedInstanceState.containsKey("attachmentsShown")) {
                _attachmentsShown = savedInstanceState.getBoolean("attachmentsShown");
            }
            if (savedInstanceState.containsKey("messagesShown")) {
                _messagesShown = savedInstanceState.getBoolean("messagesShown");
            }
            if (savedInstanceState.containsKey("uuid")) {
                _myUUID = savedInstanceState.getString("uuid");
            }
        }

        if (_workOrderId == 0) {
            // epic fail!
            Log.e(TAG, "must have a workorder id!");
            finish();
        } else {
            Log.v(TAG, "Opening work order " + _workOrderId);
            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
        }

        if (intent.hasExtra(INTENT_FIELD_ACTION)) {
            if (intent.getStringExtra(INTENT_FIELD_ACTION).equals(ACTION_ATTACHMENTS)) {
                _showAttachments = true;
            } else if (intent.getStringExtra(INTENT_FIELD_ACTION).equals(ACTION_MESSAGES)) {
                _showMessages = true;
            } else if (intent.getStringExtra(INTENT_FIELD_ACTION).equals(ACTION_CONFIRM)) {
                // TODO handle confirm
            }
        }

        if (_myUUID == null)
            _myUUID = UUID.randomUUID().toString();
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
        Log.v(TAG, "onSaveInstanceState");
        if (_workOrderId != 0)
            outState.putInt("workOrderId", _workOrderId);

        outState.putBoolean("attachmentsShown", _attachmentsShown);
        outState.putBoolean("messagesShown", _messagesShown);
        outState.putString("uuid", _myUUID);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        _workOrderScreen.setUUID(_myUUID);
        _workOrderScreen.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workOrderScreen.onResume();
        _workorderApi.sub();
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, true, false);
    }

    @Override
    protected void onPause() {
        _workOrderScreen.onPause();
        _workorderApi.unsub();
        super.onPause();
    }

    @Override
    protected void onStop() {
        _workOrderScreen.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (_workOrderScreen.onBackPressed())
            return;
        super.onBackPressed();
    }

    /*-*****************************-*/
    /*-			Web Events			-*/
    /*-*****************************-*/
    private final WorkordersWebApi _workorderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return !methodName.equals("getWorkOrders");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                //Log.v(TAG, "_workOrderApi_listener.onGetWorkOrder");
                if (!success) {
                    return;
                }

                if (_workOrderId == workOrder.getId()) {
                    Debug.setLong("last_workorder", workOrder.getId());
                    _workOrder = workOrder;

                    if (_showAttachments && !_attachmentsShown) {
                        AttachedFilesDialog.show(App.get(), null, _myUUID, _workOrder.getId());
                        _showAttachments = false;
                        _attachmentsShown = true;
                    }
                    if (_showMessages && !_messagesShown) {
                        ChatDialog.show(App.get(), _workOrderId);
                        _showMessages = false;
                        _messagesShown = true;
                    }

                    _workOrderScreen.setWorkOrder(_workOrder);
                }
            } else if (!methodName.startsWith("get")) {
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
            }

            if (methodName.startsWith("get") || !success)
                return;

            //Log.v(TAG, "onWorkordersWebApi " + methodName);

            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, false);
        }
    };


    public static void startNew(Context context, int workOrderId) {
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        ActivityClient.startActivity(intent);
    }

    public static Intent makeIntentAttachments(Context context, int workOrderId) {
        Log.v(TAG, "makeIntentAttachments");
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_ACTION, ACTION_ATTACHMENTS);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        return intent;
    }

    public static Intent makeIntentMessages(Context context, int workOrderId) {
        Log.v(TAG, "makeIntentMessages");
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_ACTION, ACTION_MESSAGES);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        return intent;
    }

    public static Intent makeIntentConfirm(Context context, int workOrderId) {
        Log.v(TAG, "makeIntentMessages");
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_ACTION, ACTION_CONFIRM);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        return intent;
    }

    public static Intent makeIntentShow(Context context, int workOrderId) {
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(/*Intent.FLAG_ACTIVITY_CLEAR_TOP |*/ Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        return intent;
    }
}
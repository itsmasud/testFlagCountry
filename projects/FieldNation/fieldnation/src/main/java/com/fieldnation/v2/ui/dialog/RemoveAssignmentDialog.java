package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.AssignmentCancelReason;

public class RemoveAssignmentDialog extends FullScreenDialog {
    private static final String TAG = "RemoveAssignmentDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;

    private HintSpinner _hintSpinner;
    private EditText _detailsEditText;

    // Data
    private int _workOrderId;
    private AssignmentCancelReason[] reasons = null;
    private HintArrayAdapter _adapter = null;
    private int _selectedIndex = -1;
    private String reasonString = null;

    public RemoveAssignmentDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_remove_assignment, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.inflateMenu(R.menu.dialog);

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);

        _hintSpinner = v.findViewById(R.id.reason_spinner);
        _detailsEditText = v.findViewById(R.id.details_edittext);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _workOrderClient.sub();
        WorkordersWebApi.getAssignmentCancelReasons(getContext(), true, WebTransaction.Type.NORMAL);

        _toolbar.setTitle("Remove Assignment");
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _hintSpinner.setOnItemSelectedListener(_expireSpinner_selected);

        _detailsEditText.addTextChangedListener(watcher);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _workOrderId = params.getInt("workOrderId");
        populateUi();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey("workOrderId"))
            _workOrderId = savedState.getInt("workOrderId");

        if (savedState.containsKey("index"))
            _selectedIndex = savedState.getInt("index");

        if (savedState.containsKey("reasonString"))
            reasonString = savedState.getString("reasonString");

        populateUi();
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        outState.putInt("workOrderId", _workOrderId);
        outState.putInt("index", _selectedIndex);
        outState.putString("reasonString", reasonString);
    }

    private void populateUi() {
        if (_hintSpinner == null)
            return;

        if (reasons == null)
            return;

        if (_adapter == null) {
            _adapter = HintArrayAdapter.createFromArray(
                    _hintSpinner.getContext(),
                    reasons,
                    R.layout.view_spinner_item);

            _adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _hintSpinner.setAdapter(_adapter);
        }

        if (_selectedIndex != -1 && !misc.isEmptyOrNull(reasonString)) {
            _finishMenu.setEnabled(true);
        } else {
            _finishMenu.setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        _workOrderClient.unsub();
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            AssignmentCancelReason reason = reasons[_selectedIndex];
            AppMessagingClient.setLoading(true);
            _finishMenu.setEnabled(false);
            _hintSpinner.setEnabled(false);
            _detailsEditText.setEnabled(false);

            WorkordersWebApi.cancelAssignment(App.get(), _workOrderId, reason.getEventReasonId(), reasonString, null);
            return true;
        }
    };

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            reasonString = editable.toString();
            populateUi();
        }
    };

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.v(TAG, "onItemSelected " + position);
            _selectedIndex = position;
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final WorkordersWebApi _workOrderClient = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuid, TransactionParams transactionParams, String methodName) {
            return methodName.equals("getAssignmentCancelReasons")
                    || methodName.equals("cancelAssignment");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (methodName.equals("getAssignmentCancelReasons") && success && successObject != null) {
                if (reasons == null) {
                    reasons = (AssignmentCancelReason[]) successObject;
                    populateUi();
                }
            }
            if (methodName.equals("cancelAssignment")) {
                AppMessagingClient.setLoading(false);
                _finishMenu.setEnabled(true);
                _hintSpinner.setEnabled(true);
                _detailsEditText.setEnabled(true);

                if (success) {
                    dismiss(true);
                    AppMessagingClient.finishActivity();
                    ToastClient.toast(App.get(), "Successfully removed from work order.", Toast.LENGTH_LONG);
                } else {
                    // TODO ? not sure we need anything here
                }
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle bundle = new Bundle();
        bundle.putInt("workOrderId", workOrderId);
        Controller.show(context, uid, RemoveAssignmentDialog.class, bundle);
    }

    /*-***********************************-*/
    /*-         Remove Assignment         -*/
    /*-***********************************-*/
    public interface OnRemoveAssignmentListener {
        void onRemoveAssignment();
    }

    private static KeyedDispatcher<OnRemoveAssignmentListener> _onRemoveAssignmentDispatcher = new KeyedDispatcher<OnRemoveAssignmentListener>() {
        @Override
        public void onDispatch(OnRemoveAssignmentListener listener, Object... parameters) {
            listener.onRemoveAssignment();
        }
    };

    public static void addOnRemoveAssignmentListener(String uid, OnRemoveAssignmentListener onRemoveAssignmentListener) {
        _onRemoveAssignmentDispatcher.add(uid, onRemoveAssignmentListener);
    }

    public static void removeOnRemoveAssignmentListener(String uid, OnRemoveAssignmentListener onRemoveAssignmentListener) {
        _onRemoveAssignmentDispatcher.remove(uid, onRemoveAssignmentListener);
    }

    public static void removeAllOnRemoveAssignmentListener(String uid) {
        _onRemoveAssignmentDispatcher.removeAll(uid);
    }

}

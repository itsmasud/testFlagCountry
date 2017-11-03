package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.WorkOrder;

public class ClosingNotesDialog extends SimpleDialog {
    private static final String TAG = "ClosingNotesDialog";

    // State
    private static final String STATE_NOTES = "ClosingNotesDialog:STATE_NOTES";

    // UI
    private EditText _editText;
    private Button _okButton;
    private Button _cancelButton;

    // Data
    private String _notes;
    private int _workOrderId;

    /*-*****************************-*/
    /*-			Life Cycle			-*/
    /*-*****************************-*/
    public ClosingNotesDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_closing_notes, container, false);

        _editText = v.findViewById(R.id.notes_edittext);
        _okButton = v.findViewById(R.id.ok_button);
        _cancelButton = v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _editText.setOnEditorActionListener(_onEditor_listener);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        super.show(payload, animate);

        _notes = payload.getString("notes");
        _workOrderId = payload.getInt("workOrderId");

        if (!misc.isEmptyOrNull(_notes))
            _editText.setText(_notes);
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
        if (savedState.containsKey(STATE_NOTES)) {
            _notes = savedState.getString(STATE_NOTES);
            _editText.setText(_notes);
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        if (_editText != null && !misc.isEmptyOrNull(_editText.getText().toString())) {
            _notes = _editText.getText().toString();
            outState.putString(STATE_NOTES, _notes);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final TextView.OnEditorActionListener _onEditor_listener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                _ok_onClick.onClick(null);
                handled = true;
            }
            return handled;
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                WorkOrder workOrder = new WorkOrder();
                workOrder.closingNotes(_editText.getText().toString());

                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Closing Notes Dialog";

                AppMessagingClient.setLoading(true);
                WorkordersWebApi.updateWorkOrder(App.get(), _workOrderId, workOrder, uiContext);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            WorkOrderTracker.onActionButtonEvent(App.get(), WorkOrderTracker.ActionButton.CLOSING_NOTES, WorkOrderTracker.Action.CLOSING_NOTES, _workOrderId);
            WorkOrderTracker.onEditEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.CLOSING_NOTES);
            AppMessagingClient.setLoading(true);

            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    public static void show(Context context, int workOrderId, String notes) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("notes", notes);

        Controller.show(context, null, ClosingNotesDialog.class, params);
    }
}


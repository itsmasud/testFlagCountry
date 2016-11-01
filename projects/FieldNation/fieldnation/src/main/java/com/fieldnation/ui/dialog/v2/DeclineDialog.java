package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by mc on 10/28/16.
 */

public class DeclineDialog extends SimpleDialog {
    private static final String TAG = "DeclineDialog";

    private static int DECLINE_REASON_OTHER = 7;

    // State
    private static final String STATE_DECLINE_TEXT = "decline";
    private static final String STATE_BLOCK_TEXT = "block";
    private static final String STATE_BLOCK_CHECKBOX = "blockCheckBox";
    private static final String STATE_BLOCK_POSITION = "blockId";
    private static final String STATE_DECLINE_POSITION = "declineId";

    // Params
    private static final String PARAM_TYPE = "type";
    public static final int TYPE_BUNDLE = 0;
    public static final int TYPE_WORK_ORDER = 1;
    private static final String PARAM_WORK_ORDER_ID = "workOrderId";
    private static final String PARAM_COMPANY_ID = "companyId";
    private static final String PARAM_BUNDLE_SIZE = "bundleSize";


    // Ui
    private TextView _bodyTextView;
    private HintSpinner _declineSpinner;
    private EditText _declineEditText;
    private CheckBox _blockCheckBox;
    private HintSpinner _blockSpinner;
    private EditText _blockEditText;
    private Button _cancelButton;
    private Button _okButton;

    // Data
    private int[] _declineReasonIds;
    private int[] _blockReasonIds;
    private int _blockPosition = -1;
    private int _declinePosition = -1;
    private int _type = TYPE_BUNDLE;
    private long _workOrderId = 0;
    private long _companyId = 0;
    private int _bundleSize = 0;

    public DeclineDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_decline, container, false);

        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);

        _declineSpinner = (HintSpinner) v.findViewById(R.id.decline_spinner);
        _declineEditText = (EditText) v.findViewById(R.id.declineDetails_edittext);

        _blockCheckBox = (CheckBox) v.findViewById(R.id.block_checkbox);
        _blockSpinner = (HintSpinner) v.findViewById(R.id.block_spinner);
        _blockEditText = (EditText) v.findViewById(R.id.blockDetails_edittext);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _okButton = (Button) v.findViewById(R.id.ok_button);

        _declineReasonIds = v.getContext().getResources().getIntArray(R.array.dialog_decline_reason_ids);
        _blockReasonIds = v.getContext().getResources().getIntArray(R.array.dialog_block_reason_ids);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        _declineSpinner.setOnItemSelectedListener(_declineSpinner_selected);
        _blockCheckBox.setOnCheckedChangeListener(_blockCheckBox_onChecked);
        _blockSpinner.setOnItemSelectedListener(_blockSpinner_selected);
        _okButton.setOnClickListener(_ok_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);

        // populate the spinners
        getDeclineSpinner();
        getBlockSpinner();
    }

    @Override
    public void onRestoreDialogState(Bundle savedState) {
        if (savedState.containsKey(STATE_BLOCK_POSITION)) {
            _blockPosition = savedState.getInt(STATE_BLOCK_POSITION);
            getBlockSpinner().setSelection(_blockPosition);
        }

        if (savedState.containsKey(STATE_DECLINE_POSITION)) {
            _declinePosition = savedState.getInt(STATE_DECLINE_POSITION);
            getDeclineSpinner().setSelection(_declinePosition);
        }

        _declineEditText.setText(savedState.getString(STATE_DECLINE_TEXT));
        _blockEditText.setText(savedState.getString(STATE_BLOCK_TEXT));

        _blockCheckBox.setChecked(savedState.getBoolean(STATE_BLOCK_CHECKBOX));

        super.onRestoreDialogState(savedState);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _workOrderId = payload.getLong(PARAM_WORK_ORDER_ID);
        _type = payload.getInt(PARAM_TYPE);
        _companyId = payload.getLong(PARAM_COMPANY_ID);
        _bundleSize = payload.getInt(PARAM_BUNDLE_SIZE);

        super.show(payload, animate);

        populateUi();
    }

    private void populateUi() {
        switch (_type) {
            case TYPE_BUNDLE:
                _bodyTextView.setText("This bundle and all " + _bundleSize + " work orders will be hidden from your available and routed work order lists");
                break;
            case TYPE_WORK_ORDER:
                _bodyTextView.setText("This work order will be hidden from your available and routed work order lists");
                break;
        }
    }

    @Override
    public void onSaveDialogState(Bundle outState) {
        super.onSaveDialogState(outState);

        if (_blockPosition != -1) {
            outState.putInt(STATE_BLOCK_POSITION, _blockPosition);
        }

        if (_declinePosition != -1) {
            outState.putInt(STATE_DECLINE_POSITION, _declinePosition);
        }

        outState.putString(STATE_DECLINE_TEXT, _declineEditText.getText().toString());
        outState.putString(STATE_BLOCK_TEXT, _blockEditText.getText().toString());
        outState.putBoolean(STATE_BLOCK_CHECKBOX, _blockCheckBox.isChecked());
    }

    private HintSpinner getDeclineSpinner() {
        if (_declineSpinner != null && _declineSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _declineSpinner.getContext(),
                    R.array.dialog_decline_reasons,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _declineSpinner.setAdapter(adapter);
        }
        return _declineSpinner;
    }

    private HintSpinner getBlockSpinner() {
        if (_blockSpinner != null && _blockSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromResources(
                    _blockSpinner.getContext(),
                    R.array.dialog_block_reasons,
                    R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _blockSpinner.setAdapter(adapter);
        }
        return _blockSpinner;
    }

    @Override
    public void cancel() {
        Bundle payload = new Bundle();
        payload.putString("ACTION", "CANCEL");
        onResult(payload);
        super.cancel();
    }

    private void onDeclined() {
        Bundle payload = new Bundle();
        payload.putString("ACTION", "DECLINED");
        payload.putLong(PARAM_WORK_ORDER_ID, _workOrderId);
        onResult(payload);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_declinePosition != -1
                    && _declineReasonIds[_declinePosition] == DECLINE_REASON_OTHER
                    && misc.isEmptyOrNull(_declineEditText.getText().toString())) {
                ToastClient.toast(App.get(), R.string.toast_missing_decline_explanation, Toast.LENGTH_LONG);
                return;
            }

            if (_blockCheckBox.isChecked()) {
                if (_blockPosition == -1) {
                    ToastClient.toast(App.get(), R.string.toast_missing_blocking_reason, Toast.LENGTH_LONG);
                    return;
                }

                if (_declinePosition == -1) {
                    WorkOrderClient.actionDecline(App.get(), _workOrderId, -1, null);
                    ProfileClient.actionBlockCompany(
                            App.get(),
                            App.get().getProfile().getUserId(),
                            _companyId,
                            _blockReasonIds[_blockPosition],
                            _blockEditText.getText().toString());
                    onDeclined();
                } else {
                    WorkOrderClient.actionDecline(
                            App.get(),
                            _workOrderId,
                            _declineReasonIds[_declinePosition],
                            _declineEditText.getText().toString());

                    ProfileClient.actionBlockCompany(
                            App.get(),
                            App.get().getProfile().getUserId(),
                            _companyId,
                            _blockReasonIds[_blockPosition],
                            _blockEditText.getText().toString());
                    onDeclined();
                }
            } else {
                if (_declinePosition == -1) {
                    WorkOrderClient.actionDecline(App.get(), _workOrderId, -1, null);
                    onDeclined();
                } else {
                    WorkOrderClient.actionDecline(App.get(), _workOrderId, _declineReasonIds[_declinePosition], _declineEditText.getText().toString());
                    onDeclined();
                }
            }
            dismiss(true);
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    private final CompoundButton.OnCheckedChangeListener _blockCheckBox_onChecked = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                _blockSpinner.setVisibility(View.VISIBLE);
                _blockEditText.setVisibility(View.VISIBLE);
            } else {
                _blockSpinner.setVisibility(View.GONE);
                _blockEditText.setVisibility(View.GONE);
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _declineSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _declinePosition = position;
            if (_declineReasonIds[_declinePosition] == DECLINE_REASON_OTHER) {
                _declineEditText.setVisibility(View.VISIBLE);
                _declineEditText.requestFocus();
            } else {
                _declineEditText.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final AdapterView.OnItemSelectedListener _blockSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _blockPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context, String uid) {
            super(context, DeclineDialog.class, uid);
        }

        /**
         * @param context
         * @param uid
         * @param workOrderId
         * @param companyId
         */
        public static void show(Context context, String uid, int bundleSize, long workOrderId, long companyId) {
            Bundle params = new Bundle();
            params.putInt(PARAM_TYPE, TYPE_BUNDLE);
            params.putLong(PARAM_WORK_ORDER_ID, workOrderId);
            params.putLong(PARAM_COMPANY_ID, companyId);
            params.putInt(PARAM_BUNDLE_SIZE, bundleSize);

            show(context, uid, DeclineDialog.class, params);
        }

        /**
         * @param context
         * @param uid
         * @param workOrderId
         * @param companyId
         */
        public static void show(Context context, String uid, long workOrderId, long companyId) {
            Bundle params = new Bundle();
            params.putInt(PARAM_TYPE, TYPE_WORK_ORDER);
            params.putLong(PARAM_WORK_ORDER_ID, workOrderId);
            params.putLong(PARAM_COMPANY_ID, companyId);

            show(context, uid, DeclineDialog.class, params);
        }
    }

    public static abstract class ControllerListener implements Controller.Listener {
        @Override
        public void onComplete(Bundle response) {
            switch (response.getString("ACTION")) {
                case "DECLINED":
                    onDeclined(response.getLong(PARAM_WORK_ORDER_ID));
                    break;
                case "CANCEL":
                    onCancel();
                    break;
            }
        }

        public abstract void onDeclined(long workOrderId);

        public abstract void onCancel();
    }

}

package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.Schedule;
import com.fieldnation.data.workorder.User;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

import java.util.Calendar;

/**
 * This is a copy of RunningLateDialog that supports the v1 work order object. This should be deleted
 * once we move the wod over to v2
 * Created by Michael on 10/5/2016.
 */

public class RunningLateDialogLegacy extends SimpleDialog {
    private static final String TAG = "RunningLateDialogLegacy";

    private static final String PARAM_WORKORDER = "workOrder";

    private static final String[] TIMEFRAMES = new String[]{"5", "10", "15", "Other"};

    // Ui
    private TextView _bodyTextView;
    private HintSpinner _timeframeSpinner;
    private TextInputLayout _timeframeLayout;
    private EditText _timeframeEditText;
    private Button _callButton;
    private Button _cancelButton;
    private Button _sendButton;

    // Data
    private Workorder _workOrder;
    private int _timeframePosition = -1;

    public RunningLateDialogLegacy(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_running_late, container, false);

        _bodyTextView = (TextView) v.findViewById(R.id.body_textview);
        _timeframeSpinner = (HintSpinner) v.findViewById(R.id.timeframe_spinner);
        _timeframeLayout = (TextInputLayout) v.findViewById(R.id.timeframe_layout);
        _timeframeEditText = (EditText) v.findViewById(R.id.timeframe_edittext);
        _callButton = (Button) v.findViewById(R.id.call_button);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _sendButton = (Button) v.findViewById(R.id.send_button);

        return v;
    }

    @Override
    public void onAdded() {
        super.onAdded();

        _timeframeSpinner.setOnItemSelectedListener(_timeframe_onItemClick);
        _timeframeEditText.addTextChangedListener(_timeframeEditText_watcher);

        _callButton.setOnClickListener(_call_onClick);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _sendButton.setOnClickListener(_send_onClick);
    }

    @Override
    public void show(Bundle payload, boolean animate) {
        _workOrder = payload.getParcelable(PARAM_WORKORDER);

        populateUi();

        super.show(payload, animate);
    }

    private void populateUi() {
        if (_workOrder == null)
            return;
        if (_sendButton == null)
            return;

        Calendar cal = null;
        try {
            Schedule schedule = _workOrder.getScheduleV2();
            if (schedule != null) {
                if (schedule.getEstimate() != null && schedule.getEstimate().getArrival() != null) {
                    cal = ISO8601.toCalendar(schedule.getEstimate().getArrival());
                } else if (schedule.getExact() != null) {
                    cal = ISO8601.toCalendar(schedule.getExact());
                } else if (schedule.getBegin() != null) {
                    cal = ISO8601.toCalendar(schedule.getBegin());
                } else if (schedule.getRange() != null && schedule.getRange().getBegin() != null) {
                    cal = ISO8601.toCalendar(schedule.getRange().getBegin());
                }

                _bodyTextView.setText(
                        _workOrder.getTitle()
                                + " is scheduled to begin at "
                                + DateUtils.formatTime2(cal) + ".");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

/*
        if (_workOrder.getWorkorderManagerInfo() == null) {
            _callButton.setVisibility(View.GONE);
        } else {
            _callButton.setVisibility(View.VISIBLE);
        }
*/

        if (_timeframeSpinner != null && _timeframeSpinner.getAdapter() == null) {
            HintArrayAdapter adapter = HintArrayAdapter.createFromArray(getView().getContext(),
                    TIMEFRAMES, R.layout.view_spinner_item);

            adapter.setDropDownViewResource(
                    android.support.design.R.layout.support_simple_spinner_dropdown_item);

            _timeframeSpinner.setAdapter(adapter);
        }

        if (_timeframePosition == -1 || _timeframePosition == 3) {
            _sendButton.setEnabled(false);
        } else {
            _sendButton.setEnabled(true);
        }

        if (_timeframePosition == 3) {
            _timeframeLayout.setVisibility(View.VISIBLE);
            if (misc.isEmptyOrNull(_timeframeEditText.getText().toString())) {
                _sendButton.setEnabled(false);
            } else {
                _sendButton.setEnabled(true);
            }
        } else {
            _timeframeLayout.setVisibility(View.GONE);
        }

    }

    private final View.OnClickListener _call_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                User contact = _workOrder.getWorkorderManagerInfo();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhone()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.get().startActivity(callIntent);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_timeframePosition == 3) {
                try {
                    int delayMin = Integer.parseInt(_timeframeEditText.getText().toString());
                    WorkorderClient.actionRunningLate(App.get(), _workOrder.getWorkorderId(), "Running late. Will be there in " + delayMin + "min", delayMin * 60);
                    ToastClient.toast(App.get(), "Late arrival notification sent", Toast.LENGTH_SHORT);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    ToastClient.toast(App.get(), "Please enter a number for the delay", Toast.LENGTH_LONG);
                }
            } else {
                try {
                    int delayMin = Integer.parseInt(TIMEFRAMES[_timeframePosition]);
                    WorkorderClient.actionRunningLate(App.get(), _workOrder.getWorkorderId(), "Running late. Will be there in " + delayMin + "min", delayMin * 60);
                    ToastClient.toast(App.get(), "Late arrival notification sent", Toast.LENGTH_SHORT);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    ToastClient.toast(App.get(), "Please enter a number for the delay", Toast.LENGTH_LONG);
                }
            }
            dismiss(true);
        }
    };

    private final AdapterView.OnItemSelectedListener _timeframe_onItemClick = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            _timeframePosition = position;
            populateUi();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private final TextWatcher _timeframeEditText_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            populateUi();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    public static class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, RunningLateDialogLegacy.class, null);
        }

        public static void show(Context context, Workorder workOrder) {
            Bundle params = new Bundle();
            params.putParcelable(PARAM_WORKORDER, workOrder);

            show(context, null, RunningLateDialogLegacy.class, params);
        }
    }

}


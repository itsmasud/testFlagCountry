package com.fieldnation.v2.ui.dialog;

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
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.ui.KeyedDispatcher;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Condition;
import com.fieldnation.v2.data.model.Contact;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.ETAStatus;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.Calendar;

/**
 * Created by Michael on 10/5/2016.
 */

public class RunningLateDialog extends SimpleDialog {
    private static final String TAG = "RunningLateDialog";

    private static final String PARAM_WORKORDER = "workOrder";

    private static final String[] TIMEFRAMES = new String[]{"5", "10", "15", "30", "60", "Other"};

    // Ui
    private TextView _bodyTextView;
    private HintSpinner _timeframeSpinner;
    private TextInputLayout _timeframeLayout;
    private EditText _timeframeEditText;
    private Button _callButton;
    private Button _cancelButton;
    private Button _sendButton;

    // Data
    private WorkOrder _workOrder;
    private int _timeframePosition = -1;

    public RunningLateDialog(Context context, ViewGroup container) {
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
    public void onStart() {
        super.onStart();

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
            if (_workOrder.getEta() != null && _workOrder.getEta().getStart() != null) {
                cal = _workOrder.getEta().getStart().getCalendar();
            } else if (_workOrder.getSchedule() != null
                    && _workOrder.getSchedule().getServiceWindow() != null
                    && _workOrder.getSchedule().getServiceWindow().getStart() != null) {
                cal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
            }
            _bodyTextView.setText(
                    _workOrder.getTitle()
                            + " is scheduled to begin at "
                            + DateUtils.formatTime2(cal) + ".");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (_workOrder.getContacts() == null || _workOrder.getContacts().getResults() == null || _workOrder.getContacts().getResults().length == 0) {
            _callButton.setVisibility(View.GONE);
        } else {
            _callButton.setVisibility(View.VISIBLE);
        }

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
                Contact contact = _workOrder.getContacts().getResults()[0];
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
            try {
                int delayMin = 0;

                if (_timeframePosition == 3)
                    delayMin = Integer.parseInt(_timeframeEditText.getText().toString());
                else
                    delayMin = Integer.parseInt(TIMEFRAMES[_timeframePosition]);

                ETA eta = new ETA()
                        .status(new ETAStatus()
                                .condition(new Condition()
                                        .estimatedDelay(delayMin * 60)
                                        .substatus(Condition.SubstatusEnum.DELAYED)));
                WorkordersWebApi.updateETA(App.get(), _workOrder.getId(), eta);

                ToastClient.toast(App.get(), "Late arrival notification sent", Toast.LENGTH_SHORT);
                _onSendDispatcher.dispatch(getUid(), _workOrder.getId());
            } catch (Exception ex) {
                Log.v(TAG, ex);
                ToastClient.toast(App.get(), "Please enter a number for the delay", Toast.LENGTH_LONG);
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

    public static void show(Context context, String uid, WorkOrder workOrder) {
        Bundle params = new Bundle();
        params.putParcelable(PARAM_WORKORDER, workOrder);

        Controller.show(context, uid, RunningLateDialog.class, params);
    }

    /*-************************-*/
    /*-         Send           -*/
    /*-************************-*/
    public interface OnSendListener {
        void onSend(long workOrderId);
    }

    private static KeyedDispatcher<OnSendListener> _onSendDispatcher = new KeyedDispatcher<OnSendListener>() {
        @Override
        public void onDispatch(OnSendListener listener, Object... parameters) {
            listener.onSend((Long) parameters[0]);
        }
    };

    public static void addOnSendListener(String uid, OnSendListener onSendListener) {
        _onSendDispatcher.add(uid, onSendListener);
    }

    public static void removeOnSendListener(String uid, OnSendListener onSendListener) {
        _onSendDispatcher.remove(uid, onSendListener);
    }

    public static void removeAllOnSendListener(String uid) {
        _onSendDispatcher.removeAll(uid);
    }
}


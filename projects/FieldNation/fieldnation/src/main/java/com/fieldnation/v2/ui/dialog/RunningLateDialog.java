package com.fieldnation.v2.ui.dialog;

import android.content.Context;
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
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.SimpleDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.KeyedDispatcher;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Condition;
import com.fieldnation.v2.data.model.Contacts;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.ETAStatus;
import com.fieldnation.v2.data.model.Schedule;

import java.util.Calendar;

/**
 * Created by Michael on 10/5/2016.
 */

public class RunningLateDialog extends SimpleDialog {
    private static final String TAG = "RunningLateDialog";

    // State
    private static final String STATE_BODY = "RunningLateDialog:STATE_BODY";
    private static final String STATE_TIME_FRAME = "RunningLateDialog:TIME_FRAME";
    private static final String STATE_TIME_FRAME_POSITION = "RunningLateDialog:TIME_FRAME_POSITION";

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
    private String _title;
    private int _workOrderId;
    private int _timeframePosition = -1;
    private ETA _eta;
    private Schedule _schedule;
    private Contacts _contacts;

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
    public void onSaveDialogState(Bundle outState) {
        if (_bodyTextView != null && !misc.isEmptyOrNull(_bodyTextView.getText().toString())) {
            outState.putString(STATE_BODY, _bodyTextView.getText().toString());
        }
        if (_timeframeEditText != null && !misc.isEmptyOrNull(_timeframeEditText.getText().toString())) {
            outState.putString(STATE_TIME_FRAME, _timeframeEditText.getText().toString());
        }

        outState.putInt(STATE_TIME_FRAME_POSITION, _timeframePosition);
    }

    public void onRestoreDialogState(Bundle savedState) {
        super.onRestoreDialogState(savedState);
        if (savedState.containsKey(STATE_BODY)) {
            _bodyTextView.setText(savedState.getString(STATE_BODY));
        }
        if (savedState.containsKey(STATE_TIME_FRAME)) {
            _timeframeEditText.setText(savedState.getString(STATE_TIME_FRAME));
        }
        if (savedState.containsKey(STATE_TIME_FRAME_POSITION)) {
            _timeframePosition = savedState.getInt(STATE_TIME_FRAME_POSITION);
            _timeframeSpinner.setSelection(_timeframePosition);
        }

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
        _workOrderId = payload.getInt("workOrderId");
        _eta = payload.getParcelable("eta");
        _schedule = payload.getParcelable("schedule");
        _title = payload.getString("title");
        _contacts = payload.getParcelable("contacts");

        populateUi();

        super.show(payload, animate);
    }

    private void populateUi() {
        if (_eta == null)
            return;
        if (_sendButton == null)
            return;

        Calendar cal = null;
        try {
            if (_eta.getStatus().getName() != null
                    && _eta.getStatus().getName() != ETAStatus.NameEnum.UNCONFIRMED
                    && _eta.getStart().getUtc() != null) {
                cal = _eta.getStart().getCalendar();

            } else if (_schedule.getServiceWindow().getStart().getUtc() != null) {
                cal = _schedule.getServiceWindow().getStart().getCalendar();
            }
            _bodyTextView.setText(_title + " is scheduled to begin at " + DateUtils.formatTime2(cal) + ".");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (_contacts.getResults().length == 0) {
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

        if (_timeframePosition == -1 || _timeframePosition == 5) {
            _sendButton.setEnabled(false);
        } else {
            _sendButton.setEnabled(true);
        }

        if (_timeframePosition == 5) {
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
            ContactListDialog.show(App.get(), "", _contacts);
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

                if (_timeframePosition == 5)
                    delayMin = Integer.parseInt(_timeframeEditText.getText().toString());
                else
                    delayMin = Integer.parseInt(TIMEFRAMES[_timeframePosition]);

                if (delayMin <= 0) {
                    ToastClient.toast(App.get(), "Please enter a delay greater than 0", Toast.LENGTH_LONG);
                    return;
                }

                ETA eta = new ETA()
                        .condition(new Condition()
                                .estimatedDelay(delayMin * 60)
                                .status(Condition.StatusEnum.DELAYED));

                SpUIContext uiContext = (SpUIContext) App.get().getSpUiContext().clone();
                uiContext.page += " - Running Late Dialog";
                WorkordersWebApi.updateETA(App.get(), _workOrderId, eta, uiContext);

                ToastClient.toast(App.get(), "Late arrival notification sent", Toast.LENGTH_SHORT);
                _onSendDispatcher.dispatch(getUid(), _workOrderId);
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

    public static void show(Context context, String uid, int workOrderId, ETA eta, Schedule schedule, Contacts contacts, String title) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putParcelable("eta", eta);
        params.putParcelable("schedule", schedule);
        params.putString("title", title);
        params.putParcelable("contacts", contacts);

        Controller.show(context, uid, RunningLateDialog.class, params);
    }

    /*-************************-*/
    /*-         Send           -*/
    /*-************************-*/
    public interface OnSendListener {
        void onSend(int workOrderId);
    }

    private static KeyedDispatcher<OnSendListener> _onSendDispatcher = new KeyedDispatcher<OnSendListener>() {
        @Override
        public void onDispatch(OnSendListener listener, Object... parameters) {
            listener.onSend((Integer) parameters[0]);
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


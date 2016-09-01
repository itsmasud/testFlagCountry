package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.util.Calendar;

public class EtaDialog extends DialogFragmentBase {
    private static final String TAG = "EtaDialog";

    // State
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // Ui
    private TextView _titleTextView;
    private Button _expirationButton;
    private Button _cancelButton;
    private Button _okButton;
    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;
    private RelativeLayout _requestLayout;
    private RelativeLayout _confirmLayout;

    // Data
    private Calendar _calendar;
    private boolean _isDateSet;
    private Listener _listener;
    private Workorder _workorder;
    private Schedule _schedule;
    private String _okButtonText;
    private String _title;
    private boolean _isRequest = false;
    private boolean _isConfirm = false;
    private final Handler _handler = new Handler();

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/
    public static EtaDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, EtaDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDER))
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
        }
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_eta, container, false);

        _requestLayout = (RelativeLayout) v.findViewById(R.id.request_layout);
        _titleTextView = (TextView) v.findViewById(R.id.title_textview);
        _expirationButton = (Button) v.findViewById(R.id.expiration_button);
        _expirationButton.setOnClickListener(_expiration_onClick);
        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);


        _confirmLayout = (RelativeLayout) v.findViewById(R.id.confirm_layout);

        final Calendar c = Calendar.getInstance();
        _datePicker = new DatePickerDialog(getActivity(), _date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        _timePicker = new TimePickerDialog(getActivity(), _time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

        _calendar = Calendar.getInstance();

        //getDialog().setTitle("Request Workorder");

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Workorder workorder, String titleInitial, String okButtonText) {
        _isDateSet = false;
        _workorder = workorder;
        _okButtonText = okButtonText;
        _title = titleInitial + workorder.getWorkorderId();
        _isRequest = true;

        super.show();
    }


    public void show(Workorder workorder, Schedule schedule, String titleInitial, String okButtonText) {
        _schedule = schedule;
        _workorder = workorder;

        _okButtonText = okButtonText;
        _title = titleInitial + workorder.getWorkorderId();
        _isConfirm = true;

        super.show();
    }


    private void populateUi() {
        if (!misc.isEmptyOrNull(_title))
            _titleTextView.setText(_title);

        if (!misc.isEmptyOrNull(_okButtonText))
            _okButton.setText(_okButtonText);

        if (_isRequest && !_isConfirm) {
            _requestLayout.setVisibility(View.VISIBLE);
            _confirmLayout.setVisibility(View.GONE);
        } else {
            _requestLayout.setVisibility(View.GONE);
            _confirmLayout.setVisibility(View.VISIBLE);
        }


    }



	/*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/

    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _calendar.set(year, monthOfYear, dayOfMonth);
            if (DateUtils.isBeforeToday(_calendar)) {
                ToastClient.toast(App.get(), getString(R.string.toast_previous_date_not_allowed), Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _datePicker.show();
                    }
                }, 100);
            } else {
                _timePicker.show();
            }
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            _calendar.set(_calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH),
                    _calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);

            // truncate milliseconds to seconds
            if (_calendar.getTimeInMillis() / 1000 < System.currentTimeMillis() / 1000) {
                ToastClient.toast(App.get(), getString(R.string.toast_previous_time_not_allowed), Toast.LENGTH_LONG);
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        _timePicker.show();
                    }
                }, 100);
                return;
            }

            _expirationButton.setText(DateUtils.formatDateTimeLong(_calendar));
            _isDateSet = true;
        }
    };

    private final View.OnClickListener _expiration_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show();
        }
    };

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null && _isRequest) {
                if (_isDateSet) {
                    _listener.onOk(_workorder, ISO8601.fromCalendar(_calendar));
                } else {
                    _listener.onOk(_workorder, null);
                }
            }

//            if (_listener != null && _isConfirm) {
//                if (!_tacAccept) {
//                    ToastClient.toast(App.get(), R.string.please_accept_the_terms_and_conditions_to_continue, Toast.LENGTH_LONG);
//                    return;
//                }
//                if (_listener != null) {
//                    _listener.onOk(_workorder, ISO8601.fromCalendar(_startCalendar), _durationMilliseconds);
//                }
//            }

            dismiss();
        }
    };

public interface Listener {
    void onOk(Workorder workorder, String dateTime);

    void onOk(Workorder workorder, String startDate, long durationMilliseconds);

    void onCancel(Workorder workorder);

    void termsOnClick(Workorder workorder);
}

}

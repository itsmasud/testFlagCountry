package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.DateUtils;
import com.fieldnation.utils.ISO8601;

import java.text.ParseException;
import java.util.Calendar;

public class TimeLogRowView extends RelativeLayout {
    private static final String TAG = "TimeLogRowView";

    // UI
    private TextView _dateTextView;
    private TextView _timeTextView;
    private TextView _devicesTextView;
    private TextView _hoursTextView;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private LoggedWork _loggedWork;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public TimeLogRowView(Context context) {
        super(context);
        init();
    }

    public TimeLogRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLogRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_worklog_tile, this);

        if (isInEditMode())
            return;

        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _devicesTextView = (TextView) findViewById(R.id.devices_textview);
        _hoursTextView = (TextView) findViewById(R.id.hours_textview);

        setOnClickListener(_edit_onClick);
        setOnLongClickListener(_delete_onClick);

        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(Workorder workorder, LoggedWork loggedWork) {
        _loggedWork = loggedWork;
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_loggedWork == null)
            return;
        if (_devicesTextView == null)
            return;

        try {
            Calendar cal = ISO8601.toCalendar(_loggedWork.getStartDate());
            Calendar ecal = null;
            if (_loggedWork.getEndDate() != null) {
                ecal = ISO8601.toCalendar(_loggedWork.getEndDate());
            }
            if (ecal != null && !DateUtils.isSameDay(cal, ecal)) {
                _dateTextView.setText(DateUtils.formatDate(cal) + " - " + DateUtils.formatDate(ecal));
            } else {
                _dateTextView.setText(DateUtils.formatDate(cal));
            }
        } catch (ParseException e) {
            Log.v(TAG, e);
        }

        try {
            Calendar cal = ISO8601.toCalendar(_loggedWork.getStartDate());

            String date = DateUtils.formatTime(cal, false);

            if (_loggedWork.getEndDate() != null) {
                cal = ISO8601.toCalendar(_loggedWork.getEndDate());

                date += " - " + DateUtils.formatTime(cal, false);
            } else {
                date += " - ----";
            }

            _timeTextView.setText(date);

            if (_loggedWork.getHours() != null) {
                _hoursTextView.setVisibility(VISIBLE);
                _hoursTextView.setText(String.format("%.2f", _loggedWork.getHours()) + " hours");
            } else {
                _hoursTextView.setVisibility(GONE);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workorder.getPay() != null && _workorder.getPay().isPerDeviceRate()) {
            _devicesTextView.setVisibility(VISIBLE);
            _devicesTextView.setText(_loggedWork.getNoOfDevices() + " devices");
        } else {
            _devicesTextView.setVisibility(GONE);
        }

        if (_workorder.canModifyTimeLog()) {
            setClickable(true);
            setLongClickable(true);
        } else {
            setClickable(false);
            setLongClickable(false);
        }
    }

    /*-******************************-*/
    /*-             Events           -*/
    /*-******************************-*/

    private final View.OnClickListener _edit_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean showdevices = false;
            try {
                showdevices = _workorder.getPay().isPerDeviceRate();
            } catch (Exception ex) {
            }

            if (_listener != null)
                _listener.editWorklog(_workorder, _loggedWork, showdevices);
        }
    };

    private final OnLongClickListener _delete_onClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null) {
                _listener.deleteWorklog(TimeLogRowView.this, _workorder, _loggedWork);
                return true;
            }
            return false;
        }
    };

    public interface Listener {
        void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount);

        void deleteWorklog(TimeLogRowView view, Workorder workorder, LoggedWork loggedWork);
    }
}

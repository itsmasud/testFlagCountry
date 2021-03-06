package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.WorkOrder;

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
    private WorkOrder _workOrder;
    private TimeLog _timeLog;

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

        _dateTextView = findViewById(R.id.date_textview);
        _timeTextView = findViewById(R.id.time_textview);
        _devicesTextView = findViewById(R.id.devices_textview);
        _hoursTextView = findViewById(R.id.hours_textview);

        populateUi();
    }

    public void setData(WorkOrder workOrder, TimeLog timeLog) {
        _timeLog = timeLog;
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_timeLog == null)
            return;
        if (_devicesTextView == null)
            return;

        try {
            Calendar cal = _timeLog.getIn().getCreated().getCalendar();
            Calendar ecal = null;
            if (_timeLog.getOut().getCreated().getUtc() != null) {
                ecal = _timeLog.getOut().getCreated().getCalendar();
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
            Calendar cal = _timeLog.getIn().getCreated().getCalendar();

            String date = DateUtils.formatTime(cal, false);

            if (_timeLog.getOut().getCreated().getUtc() != null) {
                cal = _timeLog.getOut().getCreated().getCalendar();

                date += " - " + DateUtils.formatTime(cal, false);
            } else {
                date += " - ----";
            }

            _timeTextView.setText(date + DateUtils.getDeviceTimezone(cal));

            if (_timeLog.getHours() != null) {
                _hoursTextView.setVisibility(VISIBLE);
                _hoursTextView.setText(String.format("%.2f", _timeLog.getHours()) + " hours");
            } else {
                _hoursTextView.setVisibility(GONE);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workOrder.getPay().getType() == Pay.TypeEnum.DEVICE) {
            _devicesTextView.setVisibility(VISIBLE);
            _devicesTextView.setText(_timeLog.getDevices() + " devices");
        } else {
            _devicesTextView.setVisibility(GONE);
        }
    }
}

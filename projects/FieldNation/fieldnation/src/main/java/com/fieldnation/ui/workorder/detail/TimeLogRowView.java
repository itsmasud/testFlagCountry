package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
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
    private Listener _listener;
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
            if (_timeLog.getOut() != null
                    && _timeLog.getOut().getCreated() != null) {
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

            if (_timeLog.getOut() != null
                    && _timeLog.getOut().getCreated() != null
                    && _timeLog.getOut().getCreated().getCalendar() != null) {
                cal = _timeLog.getOut().getCreated().getCalendar();

                date += " - " + DateUtils.formatTime(cal, false);
            } else {
                date += " - ----";
            }

            _timeTextView.setText(date);

            if (_timeLog.getHours() != null) {
                _hoursTextView.setVisibility(VISIBLE);
                _hoursTextView.setText(String.format("%.2f", _timeLog.getHours()) + " hours");
            } else {
                _hoursTextView.setVisibility(GONE);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workOrder.getPay() != null && _workOrder.getPay().getType().equals(Pay.TypeEnum.DEVICE)) {
            _devicesTextView.setVisibility(VISIBLE);
            _devicesTextView.setText(_timeLog.getDevices() + " devices");
        } else {
            _devicesTextView.setVisibility(GONE);
        }

        if (_workOrder.getTimeLogs().getActionsSet().contains(TimeLogs.ActionsEnum.EDIT)) {
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
                showdevices = _workOrder.getPay().getType().equals(Pay.TypeEnum.DEVICE);
            } catch (Exception ex) {
            }

            if (_listener != null)
                _listener.editTimeLog(_workOrder, _timeLog, showdevices);
        }
    };

    private final OnLongClickListener _delete_onClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null
                    && _timeLog.getActionsSet() != null
                    && _timeLog.getActionsSet().contains(TimeLog.ActionsEnum.REMOVE)) {
                _listener.deleteTimeLog(TimeLogRowView.this, _workOrder, _timeLog);
                return true;
            }
            return false;
        }
    };

    public interface Listener {
        void editTimeLog(WorkOrder workOrder, TimeLog timeLog, boolean showDeviceCount);

        void deleteTimeLog(TimeLogRowView view, WorkOrder workOrder, TimeLog timeLog);
    }
}

package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.util.Calendar;

public class ScheduleDetailView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.ScheduleSummaryView";

    private static final int WEB_SUBMIT_WORKLOG = 1;

    // UI
    private TextView _hoursTextView;
    private TextView _dateTextView;
    private TextView _startTextView;
    private TextView _endTextView;
    private TextView _devicesTextView;

    // Data
    private Listener _listener;
    private Workorder _workorder;
    private LoggedWork _loggedWork;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ScheduleDetailView(Context context) {
        super(context);
        init();
    }

    public ScheduleDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScheduleDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_schedule_detail, this);

        if (isInEditMode())
            return;

        _hoursTextView = (TextView) findViewById(R.id.hours_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _startTextView = (TextView) findViewById(R.id.start_textview);
        _endTextView = (TextView) findViewById(R.id.end_textview);
        _devicesTextView = (TextView) findViewById(R.id.devices_textview);

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

    private void enableDevices(boolean enabled) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) _devicesTextView.getLayoutParams();
        if (enabled) {
            params.weight = 1;
            _devicesTextView.setLayoutParams(params);
            _devicesTextView.setVisibility(View.VISIBLE);
        } else {
            params.weight = 0;
            _devicesTextView.setLayoutParams(params);
            _devicesTextView.setVisibility(View.GONE);
        }
    }

    private void populateUi() {
        if (_loggedWork == null)
            return;
        if (_devicesTextView == null)
            return;

        String startDate = _loggedWork.getStartDate();
        Calendar startCal = null;
        String date;

        try {
            startCal = ISO8601.toCalendar(startDate);
            _dateTextView.setText(misc.formatDate(startCal));
            date = misc.formatTime2(startCal);
            date += startCal.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM";
            _startTextView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            Calendar endCal = ISO8601.toCalendar(_loggedWork.getEndDate());
            date = misc.formatTime2(endCal);
            date += endCal.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM";
            _endTextView.setText(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (_loggedWork.getHours() != null) {
            _hoursTextView.setText(String.format("%.2f", _loggedWork.getHours()));
        }

        if (_workorder.canModifyTimeLog()) {
            setClickable(true);
        } else {
            setClickable(false);
        }

        if (_workorder.getPay().isPerDeviceRate()) {
            enableDevices(true);
            _devicesTextView.setText(_loggedWork.getNoOfDevices() + "");
        } else {
            enableDevices(false);
        }
    }

    /*-******************************-*/
    /*-             Events           -*/
    /*-******************************-*/

    private View.OnClickListener _edit_onClick = new View.OnClickListener() {
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

    private OnLongClickListener _delete_onClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null) {
                _listener.deleteWorklog(_workorder, _loggedWork);
                return true;
            }
            return false;
        }
    };

    public interface Listener {
        public void editWorklog(Workorder workorder, LoggedWork loggedWork, boolean showDeviceCount);

        public void deleteWorklog(Workorder workorder, LoggedWork loggedWork);
    }
}

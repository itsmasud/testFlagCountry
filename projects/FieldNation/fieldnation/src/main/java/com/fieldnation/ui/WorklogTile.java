package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.LoggedWork;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 12/2/2014.
 */
public class WorklogTile extends RelativeLayout {
    private static final String TAG = "WorklogTile";

    // Ui
    private TextView _dateTextView;
    private TextView _timeTextView;
    private TextView _devicesTextView;

    // Data
    private LoggedWork _log;
    private boolean _showDevices = true;

    /*-*************************************-*/
    /*-             Life Cycle              -*/
    /*-*************************************-*/


    public WorklogTile(Context context) {
        super(context);
        init();
    }

    public WorklogTile(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorklogTile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_worklog_tile, this);

        if (isInEditMode())
            return;

        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _devicesTextView = (TextView) findViewById(R.id.devices_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);

        populateUi();
    }

    public void setWorklog(LoggedWork log, boolean showDevices) {
        _log = log;
        _showDevices = showDevices;
        populateUi();
    }

    private void populateUi() {
        if (_log == null)
            return;

        if (_timeTextView == null)
            return;

        try {
            _dateTextView.setText(misc.formatDate(ISO8601.toCalendar(_log.getStartDate())));
        } catch (Exception ex) {
            _dateTextView.setText("NA");
            ex.printStackTrace();
        }

        if (_showDevices) {
            _devicesTextView.setText(_log.getNoOfDevices() + " " + getContext().getString(R.string.devices));
            _devicesTextView.setVisibility(View.VISIBLE);
        } else {
            _devicesTextView.setVisibility(View.GONE);
        }

        _timeTextView.setText(_log.getStartTime() + " - " + _log.getEndTime() + " (" + _log.getHours() + " hrs)");
    }
}

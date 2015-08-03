package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

public class ScheduleSummaryView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ScheduleSummaryView";

    // UI
    private TextView _dateTextView;
    private TextView _timeTextView;
    private LinearLayout _durationLayout;
    private TextView _durationTextView;

    // Data
    private Workorder _workorder;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public ScheduleSummaryView(Context context) {
        super(context);
        init();
    }

    public ScheduleSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wd_schedule_summary, this);

        if (isInEditMode())
            return;

        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _durationLayout = (LinearLayout) findViewById(R.id.duration_layout);
        _durationTextView = (TextView) findViewById(R.id.duration_textview);


        setVisibility(View.GONE);
    }

	/*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        if (_workorder == null)
            return;

        if (_workorder.getEstimatedSchedule() != null) {
            _dateTextView.setText(_workorder.getEstimatedSchedule().getFormatedDate());
            _timeTextView.setText(_workorder.getEstimatedSchedule().getFormatedTime());
            if (_workorder.getEstimatedSchedule().getDuration() != null) {
                _durationLayout.setVisibility(VISIBLE);
                _durationTextView.setText(_workorder.getEstimatedSchedule().getDuration() + " hours");
            } else {
                _durationLayout.setVisibility(GONE);
            }
        } else {
            _dateTextView.setText(_workorder.getSchedule().getFormatedDate());
            _timeTextView.setText(_workorder.getSchedule().getFormatedTime());
            if (_workorder.getSchedule().getDuration() != null) {
                _durationLayout.setVisibility(VISIBLE);
                _durationTextView.setText(_workorder.getSchedule().getDuration() + " hours");
            } else {
                _durationLayout.setVisibility(GONE);
            }
        }

        setVisibility(View.VISIBLE);
    }
}

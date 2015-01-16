package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;

public class ScheduleSummaryView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.ScheduleSummaryView";

    private static final int WEB_SUBMIT_WORKLOG = 1;

    // UI
    private TextView _arriveTimeTextView;
    private TextView _coLabelTextView;
    private TextView _coTextView;

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

        _arriveTimeTextView = (TextView) findViewById(R.id.arrivetime_view);
        _coLabelTextView = (TextView) findViewById(R.id.colabel_textview);
        _coTextView = (TextView) findViewById(R.id.co_textview);

        setVisibility(View.GONE);
    }

	/*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        if (_workorder == null)
            return;
        {
            Schedule schedule = _workorder.getSchedule();
            String display = schedule.getDiaplsyString();
            if (display != null)
                _arriveTimeTextView.setText(display);
        }

        _coLabelTextView.setVisibility(View.GONE);
        _coTextView.setVisibility(View.GONE);

        if (_workorder.getCounterOfferInfo() != null && _workorder.getCounterOfferInfo().getSchedule() != null) {
            Schedule schedule = _workorder.getCounterOfferInfo().getSchedule();

            String display = schedule.getDiaplsyString();
            if (display != null) {
                _coTextView.setText(schedule.getDiaplsyString());
                _coTextView.setVisibility(View.VISIBLE);
                _coLabelTextView.setVisibility(View.VISIBLE);
            } else {
                _coTextView.setVisibility(View.GONE);
                _coLabelTextView.setVisibility(View.GONE);
            }
        }
        setVisibility(View.VISIBLE);
    }
}

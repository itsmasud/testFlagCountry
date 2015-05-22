package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;

public class ScheduleSummaryView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ScheduleSummaryView";

    // UI

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

        {
            String display = null;
            if (_workorder.getEstimatedSchedule() != null)
                display = _workorder.getEstimatedSchedule().getDisplayString(true);
            else
                display = _workorder.getSchedule().getDisplayString(false);

/*
            if (display != null)
                _arriveTimeTextView.setText(display);
*/
        }

//        _coLabelTextView.setVisibility(View.GONE);
//        _coTextView.setVisibility(View.GONE);

        if (_workorder.getCounterOfferInfo() != null
                && _workorder.getCounterOfferInfo().getSchedule() != null) {
            Schedule schedule = _workorder.getCounterOfferInfo().getSchedule();

            String display = schedule.getDisplayString(false);
            if (display != null) {
//                _coTextView.setText(schedule.getDisplayString(false));
//                _coTextView.setVisibility(View.VISIBLE);
//                _coLabelTextView.setVisibility(View.VISIBLE);
            } else {
//                _coTextView.setVisibility(View.GONE);
//                _coLabelTextView.setVisibility(View.GONE);
            }
        }
        setVisibility(View.VISIBLE);
    }
}

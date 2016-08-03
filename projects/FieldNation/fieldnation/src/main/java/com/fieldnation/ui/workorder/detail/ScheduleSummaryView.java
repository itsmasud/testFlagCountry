package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.ISO8601;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ScheduleSummaryView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ScheduleSummaryView";

    // UI
    private TextView _type1TextView;
    private TextView _date1TextView;
    private TextView _type2TextView;
    private TextView _date2TextView;

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

        _type1TextView = (TextView) findViewById(R.id.type1_textview);
        _date1TextView = (TextView) findViewById(R.id.date1_textview);
        _type2TextView = (TextView) findViewById(R.id.type2_textview);
        _date2TextView = (TextView) findViewById(R.id.date2_textview);


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
        setVisibility(View.VISIBLE);

        try {
            switch (_workorder.getScheduleType()) {
                case 1: // exact schedule
                    _type2TextView.setVisibility(GONE);
                    _date2TextView.setVisibility(GONE);

                    _type1TextView.setText("Exactly on");
                    _date1TextView.setText(
                            new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault())
                                    .format(ISO8601.toCalendar(_workorder.getSchedule().getStartTime()).getTime()));
                    break;
                case 2: // Buseness hours
                    break;
                case 3: // Open range
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        if (_workorder.getEstimatedSchedule() != null) {
//            _dateTextView.setText(_workorder.getEstimatedSchedule().getFormatedDate());
//            _timeTextView.setText(_workorder.getEstimatedSchedule().getFormatedTime());
//            if (_workorder.getEstimatedSchedule().getDuration() != null) {
//                _durationLayout.setVisibility(VISIBLE);
//                _durationTextView.setText(_workorder.getEstimatedSchedule().getDuration() + " hours");
//            } else {
//                _durationLayout.setVisibility(GONE);
//            }
//            setVisibility(View.VISIBLE);
//        } else if (_workorder.getSchedule() != null) {
//            _dateTextView.setText(_workorder.getSchedule().getFormatedDate());
//            _timeTextView.setText(_workorder.getSchedule().getFormatedTime());
//            if (_workorder.getSchedule().getDuration() != null) {
//                _durationLayout.setVisibility(VISIBLE);
//                _durationTextView.setText(_workorder.getSchedule().getDuration() + " hours");
//            } else {
//                _durationLayout.setVisibility(GONE);
//            }
//            setVisibility(View.VISIBLE);
//        } else {
//            setVisibility(View.GONE);
//        }
    }
}

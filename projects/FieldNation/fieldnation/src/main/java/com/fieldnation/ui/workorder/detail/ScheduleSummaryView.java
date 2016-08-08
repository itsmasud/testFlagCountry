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
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        Schedule schedule = _workorder.getSchedule();

        if (_workorder.getEstimatedSchedule() != null) {
            schedule = _workorder.getEstimatedSchedule();
        }

        try {
            Calendar sCal = null;
            Calendar eCal = null;

            try {
                if (schedule != null && !misc.isEmptyOrNull(schedule.getStartTime()))
                    sCal = ISO8601.toCalendar(schedule.getStartTime());
            } catch (Exception ex) {
            }

            try {
                if (schedule != null && !misc.isEmptyOrNull(schedule.getEndTime()))
                    eCal = ISO8601.toCalendar(schedule.getEndTime());
            } catch (Exception ex) {
            }

            if (sCal == null) {
                setVisibility(GONE);
                return;
            }

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(new String[]{"am", "pm"});
            switch (_workorder.getScheduleType()) {
                case 1: { // exact schedule
                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);

                    _type1TextView.setText(R.string.exactly_on);
                    _date1TextView.setText(sdf.format(sCal.getTime()));

                    _type2TextView.setVisibility(GONE);
                    _date2TextView.setVisibility(GONE);
                    break;
                }
                case 2: { // Business hours
                    SimpleDateFormat sdf1 = new SimpleDateFormat("E, MMM dd", Locale.getDefault());
                    sdf1.setDateFormatSymbols(symbols);
                    SimpleDateFormat sdf2 = new SimpleDateFormat("E, MMM dd, yyyy", Locale.getDefault());
                    sdf2.setDateFormatSymbols(symbols);

                    _type1TextView.setText(R.string.between);
                    _date1TextView.setText(getContext().getString(R.string.schedule_business_hours_format1,
                            sdf1.format(sCal.getTime()),
                            sdf2.format(eCal.getTime())));

                    sdf1 = new SimpleDateFormat("hh:mma", Locale.getDefault());
                    sdf1.setDateFormatSymbols(symbols);

                    _type2TextView.setVisibility(VISIBLE);
                    _date2TextView.setVisibility(VISIBLE);
                    _date2TextView.setText(getContext().getString(R.string.schedule_business_hours_format2,
                            sdf1.format(sCal.getTime()),
                            sdf1.format(eCal.getTime())));
                    break;
                }
                case 3: { // Open range
                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                    sdf.setDateFormatSymbols(symbols);
                    _type1TextView.setText(R.string.between);
                    _date1TextView.setText(getContext().getString(R.string.schedule_open_range_format,
                            sdf.format(sCal.getTime()),
                            sdf.format(eCal.getTime())));

                    _type2TextView.setVisibility(GONE);
                    _date2TextView.setVisibility(GONE);
                    break;
                }
                default:
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

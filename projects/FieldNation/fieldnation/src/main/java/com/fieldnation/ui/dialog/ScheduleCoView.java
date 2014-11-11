package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class ScheduleCoView extends RelativeLayout {
    private static final String TAG = "ui.dialog.ScheduleCoView";

    // Ui
    private TextView _statusTextView;
    private TextView _bodyTextView;

    private Button _clearButton;
    private Button _changeButton;

    // Data
    private Schedule _schedule;
    private boolean _isCounter;
    private Listener _listener;


    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public ScheduleCoView(Context context) {
        super(context);
        init();
    }

    public ScheduleCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScheduleCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_schedule_tile, this);

        if (isInEditMode())
            return;

        _statusTextView = (TextView) findViewById(R.id.status_textview);
        _bodyTextView = (TextView) findViewById(R.id.body_textview);

        _clearButton = (Button) findViewById(R.id.clear_button);
        _clearButton.setOnClickListener(_clear_onClick);

        _changeButton = (Button) findViewById(R.id.change_button);
        _changeButton.setOnClickListener(_change_onClick);

        populateUi();
    }

    public void setSchedule(Schedule schedule, boolean isCounterOffer) {
        _schedule = schedule;
        _isCounter = isCounterOffer;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_schedule == null)
            return;

        if (_statusTextView == null)
            return;

        if (_bodyTextView == null)
            return;

        if (_isCounter) {
            _statusTextView.setText("Your Schedule");
        } else {
            _statusTextView.setText("Buyer's Schedule");
        }

        if (_schedule.isExact()) {
            try {
                String dayDate;
                String time = "";
                Calendar cal;

                cal = ISO8601.toCalendar(_schedule.getStartTime());
                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                time = misc.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

                _bodyTextView.setText("Arrive exactly on " + dayDate + " at " + time + ".");

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (_schedule != null) {
            try {
                Calendar cal = ISO8601.toCalendar(_schedule.getStartTime());
                String dayDate;
                String time = "";

                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                time = misc.formatTime(cal, false);

                String msg = "Arrive between " + dayDate + " at " + time + " and ";

                Calendar cal2 = ISO8601.toCalendar(_schedule.getEndTime());

                // same day
                if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                    time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    msg += time + ".";

                } else {
                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + misc.formatDateLong(cal2);
                    time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    msg += dayDate + " at " + time + ".";
                }

                _bodyTextView.setText(msg);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/

    private View.OnClickListener _clear_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClear();
        }
    };

    private View.OnClickListener _change_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onChange(_schedule);
        }
    };

    public interface Listener {
        public void onClear();

        public void onChange(Schedule schedule);
    }
}

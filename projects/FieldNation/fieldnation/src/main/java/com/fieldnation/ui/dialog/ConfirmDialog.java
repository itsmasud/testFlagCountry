package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ConfirmDialog extends DialogFragment {
    private static final String TAG = "ui.dialog.ConfirmDialog";

    // State
    private static final String STATE_DURATION = "STATE_DURATION";
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";

    // Ui
    private Button _startDateButton;
    private Button _durationButton;
    private Button _okButton;
    private Button _cancelButton;
    private TextView _schedule1TextView;
    private TextView _schedule2TextView;
    private CheckBox _tacCheckBox;
    private Button _tacButton;

    // Data
    private Listener _listener;
    private Calendar _startCalendar;
    private FragmentManager _fm;
    private long _durationMilliseconds;
    private Schedule _schedule;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public static ConfirmDialog getInstance(FragmentManager fm, String tag) {
        ConfirmDialog d = null;
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (int i = 0; i < frags.size(); i++) {
                Fragment frag = frags.get(i);
                if (frag instanceof ConfirmDialog && frag.getTag().equals(tag)) {
                    d = (ConfirmDialog) frag;
                    break;
                }
            }
        }
        if (d == null)
            d = new ConfirmDialog();
        d._fm = fm;
        return d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_DURATION))
                _durationMilliseconds = savedInstanceState.getLong(STATE_DURATION);

            if (savedInstanceState.containsKey(STATE_SCHEDULE))
                _schedule = savedInstanceState.getParcelable(STATE_SCHEDULE);
        }

        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(STATE_DURATION, _durationMilliseconds);

        if (_schedule != null)
            outState.putParcelable(STATE_SCHEDULE, _schedule);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_confirm, container, false);

        _startDateButton = (Button) v.findViewById(R.id.start_date_button);
        _startDateButton.setOnClickListener(_startDate_onClick);

        _durationButton = (Button) v.findViewById(R.id.duration_button);
        _durationButton.setOnClickListener(_duration_onClick);

        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _okButton.setEnabled(false);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        _schedule1TextView = (TextView) v.findViewById(R.id.schedule1_textview);
        _schedule2TextView = (TextView) v.findViewById(R.id.schedule2_textview);

        _tacButton = (Button) v.findViewById(R.id.tac_button);
        _tacButton.setOnClickListener(_terms_onClick);

        _tacCheckBox = (CheckBox) v.findViewById(R.id.tac_checkbox);
        _tacCheckBox.setOnCheckedChangeListener(_tacCheck_change);

        _startCalendar = Calendar.getInstance();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    private void populateUi() {
        if (_schedule == null)
            return;

        if (_schedule1TextView == null)
            return;

        if (_schedule.isExact()) {
            _schedule2TextView.setVisibility(View.GONE);
            try {
                Calendar cal = ISO8601.toCalendar(_schedule.getStartTime());
                String dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                String time = misc.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false,
                        TimeZone.SHORT);

                _schedule1TextView.setText("You will need to arrive exactly on " + dayDate + " at " + time + ".");

                _startCalendar = cal;
                _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
                _startDateButton.setEnabled(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            _schedule2TextView.setVisibility(View.GONE);
            try {
                Calendar cal = ISO8601.toCalendar(_schedule.getStartTime());
                String dayDate;
                String time = "";

                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                time = misc.formatTime(cal, false);

                String msg = "You will need to arrive between " + dayDate + " at " + time + " and ";

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

                _schedule1TextView.setText(msg);

                _startCalendar = cal;
                _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
                _startDateButton.setEnabled(true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setDuration(long timeMilliseconds) {
        _durationMilliseconds = timeMilliseconds;
        _durationButton.setText(misc.convertMsToHuman(_durationMilliseconds));
    }

    public void setTime(Calendar time) {
        try {
            _startCalendar.set(time.get(Calendar.YEAR), time.get(Calendar.MONTH),
                    time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY),
                    time.get(Calendar.MINUTE));

            long start = ISO8601.toUtc(_schedule.getStartTime());
            long end = ISO8601.toUtc(_schedule.getEndTime());

            long input = _startCalendar.getTimeInMillis();

            if (input < start || input > end) {
                Toast.makeText(getActivity(), "Arrival time is out of range. Please try again", Toast.LENGTH_LONG).show();
                _startCalendar = ISO8601.toCalendar(_schedule.getStartTime());
            }

            _startDateButton.setText(misc.formatDateTimeLong(_startCalendar));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void show(String tag, Schedule schedule) {
        _schedule = schedule;
        show(_fm, tag);
    }

    /*-*****************************-*/
    /*-				Events			-*/
    /*-*****************************-*/
    private CompoundButton.OnCheckedChangeListener _tacCheck_change = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            _okButton.setEnabled(isChecked);
        }
    };

    private View.OnClickListener _terms_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.termsOnClick();
        }
    };

    private View.OnClickListener _startDate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Calendar start = ISO8601.toCalendar(_schedule.getStartTime());

                if (!_schedule.isExact()) {
                    Calendar stop = ISO8601.toCalendar(_schedule.getEndTime());

                    if (start.get(Calendar.YEAR) == stop.get(Calendar.YEAR)
                            && start.get(Calendar.DAY_OF_YEAR) == stop.get(Calendar.DAY_OF_YEAR)) {
                        _listener.getTime(start);
                    } else {
                        _listener.getDateTime(start);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private View.OnClickListener _ok_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onOk(ISO8601.fromCalendar(_startCalendar), _durationMilliseconds);
            }
            dismiss();
        }
    };
    private View.OnClickListener _duration_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.getDuration(_durationMilliseconds);
        }
    };

    public interface Listener {
        public void onOk(String startDate, long durationMilliseconds);

        public void onCancel();

        public void termsOnClick();

        public void getDateTime(Calendar start);

        public void getTime(Calendar start);

        public void getDuration(long duration);
    }

}
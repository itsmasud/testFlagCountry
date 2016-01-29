package com.fieldnation.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.ui.FnSpinner;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

public class ScheduleDialog extends DialogFragmentBase {
    private static final String TAG = "ScheduleDialog";

    // State
    private static final String STATE_SCHEDULE = "STATE_SCHEDULE";

    // Modes
    private static final int MODE_RANGE = 0;
    private static final int MODE_EXACT = 1;

    // UI
    private FnSpinner _typeSpinner;

    private LinearLayout _rangeLayout;

    private Button _startDateButton;
    private Button _endDateButton;

    private LinearLayout _exactLayout;
    private Button _dateTimeButton;

    private Button _cancelButton;
    private Button _okButton;

    private DatePickerDialog _datePicker;
    private TimePickerDialog _timePicker;

    // Data
    private int _mode;
    private Schedule _sched;
    private Calendar _startCal;
    private Calendar _endCal;
    private boolean _startIsSet = false;
    private boolean _endIsSet = false;
    private Listener _listener;


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static ScheduleDialog getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ScheduleDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SCHEDULE)) {
                _sched = savedInstanceState.getParcelable(STATE_SCHEDULE);
            }
        }
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (_sched != null)
            outState.putParcelable(STATE_SCHEDULE, _sched);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_schedule, container, false);

        _typeSpinner = (FnSpinner) v.findViewById(R.id.type_spinner);
        _typeSpinner.setOnItemClickListener(_type_selected);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.schedule_types,
                R.layout.view_spinner_item);
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _typeSpinner.setAdapter(adapter);

        _rangeLayout = (LinearLayout) v.findViewById(R.id.range_layout);
        _exactLayout = (LinearLayout) v.findViewById(R.id.exact_layout);

        _startDateButton = (Button) v.findViewById(R.id.start_date_button);
        _startDateButton.setOnClickListener(_startDateButton_onClick);
        _endDateButton = (Button) v.findViewById(R.id.end_date_button);
        _endDateButton.setOnClickListener(_endDateButton_onClick);

        _dateTimeButton = (Button) v.findViewById(R.id.date_time_button);
        _dateTimeButton.setOnClickListener(_dateTimeButton_onClick);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancelButton_onClick);
        _okButton = (Button) v.findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_okButton_onClick);

        final Calendar c = Calendar.getInstance();
        _datePicker = DatePickerDialog.newInstance(_date_onSet, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        _datePicker.setCloseOnSingleTapDay(true);
        _timePicker = TimePickerDialog.newInstance(_time_onSet, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                false, false);

        _startCal = Calendar.getInstance();
        _endCal = Calendar.getInstance();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void init() {
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void show(Schedule schedule) {
        _sched = schedule;
        super.show();
        populateUi();
    }

    private Schedule makeSchedule() {
        switch (_mode) {
            case MODE_EXACT:
                return new Schedule(ISO8601.fromCalendar(_startCal));
            case MODE_RANGE:
                return new Schedule(ISO8601.fromCalendar(_startCal), ISO8601.fromCalendar(_endCal));
        }
        return null;
    }

    private void populateUi() {
        if (_typeSpinner == null)
            return;

        try {
            _startCal = ISO8601.toCalendar(_sched.getStartTime());
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        try {
            _endCal = ISO8601.toCalendar(_sched.getEndTime());
        } catch (Exception e) {
            Log.v(TAG, e);
        }

        if (_sched != null) {
            if (_sched.isExact()) {
                setMode(MODE_EXACT);
            } else {
                setMode(MODE_RANGE);
            }
        }
    }

    private void setMode(int mode) {
        Log.e(TAG, "setMode");
        _mode = mode;

        _typeSpinner.setSelection(_mode);

        switch (_mode) {
            case MODE_EXACT:
                _rangeLayout.setVisibility(View.GONE);
                _exactLayout.setVisibility(View.VISIBLE);

                _dateTimeButton.setText(misc.formatDateTimeLong(_startCal));
                break;
            case MODE_RANGE:
                _rangeLayout.setVisibility(View.VISIBLE);
                _exactLayout.setVisibility(View.GONE);

                _startDateButton.setText(misc.formatDateTimeLong(_startCal));
                _endDateButton.setText(misc.formatDateTimeLong(_endCal));

                break;
        }
    }

    /*-*****************************-*/
    /*-			UI Events			-*/
    /*-*****************************-*/
    private final DatePickerDialog.OnDateSetListener _date_onSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            String tag = datePickerDialog.getTag();
            if (tag.equals("start")) {
                _startCal.set(year, month, day);
            } else if (tag.equals("end")) {
                _endCal.set(year, month, day);
            }

            _timePicker.show(_fm, datePickerDialog.getTag());
        }
    };

    private final TimePickerDialog.OnTimeSetListener _time_onSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute) {
            String tag = view.getTag();
            if (tag.equals("start")) {
                _startCal.set(_startCal.get(Calendar.YEAR), _startCal.get(Calendar.MONTH),
                        _startCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _startIsSet = true;

                if (_mode == MODE_EXACT) {
                    _dateTimeButton.setText(misc.formatDateTimeLong(_startCal));
                } else {
                    _startDateButton.setText(misc.formatDateTimeLong(_startCal));
                }

            } else if (tag.equals("end")) {
                _endCal.set(_endCal.get(Calendar.YEAR), _endCal.get(Calendar.MONTH),
                        _endCal.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                _endIsSet = true;
                _endDateButton.setText(misc.formatDateTimeLong(_endCal));
            }
        }
    };

    private final AdapterView.OnItemClickListener _type_selected = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setMode(position);
        }

    };

    private final View.OnClickListener _okButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_startIsSet || _endIsSet) {
                dismiss();
                if (_listener != null) {
                    _listener.onComplete(makeSchedule());
                }
            } else {
                Toast.makeText(getActivity(), R.string.toast_change_schedule_or_cancel, Toast.LENGTH_LONG).show();
            }
        }
    };
    private final View.OnClickListener _cancelButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            if (_listener != null) {
                _listener.onCancel();
            }
        }
    };

    private final View.OnClickListener _dateTimeButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "start");
        }
    };

    private final View.OnClickListener _startDateButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "start");
        }
    };

    private final View.OnClickListener _endDateButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _datePicker.show(_fm, "end");
        }
    };

    public interface Listener {
        void onComplete(Schedule schedule);

        void onCancel();
    }

}

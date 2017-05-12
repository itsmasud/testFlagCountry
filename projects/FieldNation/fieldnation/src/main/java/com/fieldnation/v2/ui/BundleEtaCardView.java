package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.WorkOrder;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BundleEtaCardView extends RelativeLayout {


    private TextView _titleTextView;
    private TextView _dateTextView;
    private TextView _addressTextView;
    private TextView _locationTextView;
    private OnClickListener _listener;

    // Data
    private WorkOrder _workOrder;

    public BundleEtaCardView(Context context) {
        super(context);
        init();
    }

    public BundleEtaCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BundleEtaCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_bundle_eta_card, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _dateTextView = (TextView) findViewById(R.id.schedule_textview);
        _addressTextView = (TextView) findViewById(R.id.address_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);


        setOnClickListener(_this_onClick);
    }

    public void setData(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void setListener(OnClickListener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_workOrder.getTitle() != null)
            _titleTextView.setText(_workOrder.getTitle());

        final String scheduleDisplayText = getScheduleDisplayText();
        if (scheduleDisplayText == null) {
            _dateTextView.setVisibility(View.GONE);
        } else
            _dateTextView.setText(scheduleDisplayText);


        if (_workOrder.getLocation() != null) {
            _addressTextView.setVisibility((misc.isEmptyOrNull(_workOrder.getLocation().getAddress1()) || misc.isEmptyOrNull(_workOrder.getLocation().getAddress2())) ? VISIBLE : GONE);
            _addressTextView.setText(
                    (misc.isEmptyOrNull(_workOrder.getLocation().getAddress1()) ? "" : _workOrder.getLocation().getAddress1() + ", ")
                            + (misc.isEmptyOrNull(_workOrder.getLocation().getAddress2()) ? "" : _workOrder.getLocation().getAddress2()));
        } else {
            _addressTextView.setVisibility(GONE);
        }

        if (_workOrder.getLocation() != null) {
            _locationTextView.setVisibility((misc.isEmptyOrNull(_workOrder.getLocation().getCity()) || misc.isEmptyOrNull(_workOrder.getLocation().getState())) ? VISIBLE : GONE);
            _locationTextView.setText(
                    (_workOrder.getLocation().getCity() == null ? "" : _workOrder.getLocation().getCity() + ", ")
                            + (_workOrder.getLocation().getState() == null ? "" : _workOrder.getLocation().getState() + " ")
                            + (_workOrder.getLocation().getZip() == null ? "" : _workOrder.getLocation().getZip()));
        } else {
            _locationTextView.setVisibility(GONE);
        }


    }


    private String getScheduleDisplayText() {
        String dateTimeString = "";
        try {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(App.get().getResources().getStringArray(R.array.schedule_capital_case_am_pm_array));

            if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                Calendar sCal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                Calendar eCal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf1 = new SimpleDateFormat("E, MMM dd", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);
                SimpleDateFormat sdf2 = new SimpleDateFormat("E, MMM dd, yyyy", Locale.getDefault());
                sdf2.setDateFormatSymbols(symbols);

                dateTimeString = App.get().getString(R.string.schedule_business_hours_format1,
                        sdf1.format(sCal.getTime()),
                        sdf2.format(eCal.getTime()));

                sdf1 = new SimpleDateFormat("hh:mma", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);


                dateTimeString += App.get().getString(R.string.schedule_business_hours_format3,
                        sdf1.format(sCal.getTime()),
                        sdf1.format(eCal.getTime()));

                return dateTimeString;

            } else if (_workOrder.getSchedule().getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                Calendar sCal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();
                Calendar eCal = _workOrder.getSchedule().getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                dateTimeString = App.get().getString(R.string.schedule_open_range_format2,
                        sdf.format(sCal.getTime()),
                        sdf.format(eCal.getTime()));

                return dateTimeString;

            } else { //if (schedule.getType() == Schedule.Type.EXACT) {
                Calendar sCal = _workOrder.getSchedule().getServiceWindow().getStart().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);
                dateTimeString = sdf.format(sCal.getTime());

                return dateTimeString;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(BundleEtaCardView.this, _workOrder);
        }
    };

    public interface OnClickListener {
        void onClick(BundleEtaCardView row, WorkOrder workOrder);
    }
}

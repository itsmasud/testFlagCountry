package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ScheduleSummaryView";

    private static final String DIALOG_ETA = TAG + ".etaDialog";

    // UI
    private TextView _type1TextView;
    private TextView _date1TextView;
    private TextView _type2TextView;
    private TextView _date2TextView;
    private Button _editEtaButton;

    // Data
    private WorkOrder _workOrder;

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
        _editEtaButton = (Button) findViewById(R.id.add_button);
        _editEtaButton.setOnClickListener(_editEta_onClick);

        setVisibility(View.GONE);
    }

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        refresh();
    }

    private void refresh() {
        if (_workOrder == null)
            return;
        setVisibility(View.VISIBLE);

        /*
1    Created
2    Published
3    Assigned
4    Work Done
5    Approved
6    Paid
7    Cancelled
8    Postponed
9    Routed
10    Deleted
         */

        if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ADD)) {
            _editEtaButton.setVisibility(VISIBLE);
            _editEtaButton.setText(R.string.btn_set_eta);
        } else if (_workOrder.getEta() != null
                && _workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.EDIT)) {
            _editEtaButton.setVisibility(VISIBLE);
            _editEtaButton.setText(R.string.btn_edit_eta);
        } else {
            _editEtaButton.setVisibility(GONE);
        }

        Schedule schedule = _workOrder.getSchedule();

        try {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(getResources().getStringArray(R.array.schedule_small_case_am_pm_array));

            if (schedule == null) {
                setVisibility(GONE);
                return;
            }

            if (_workOrder.getEta() != null && _workOrder.getEta().getUser() != null
                    && _workOrder.getEta().getUser().getId() > 0) {

                Log.v(TAG, "ETA!!");
                ETA eta = _workOrder.getEta();
                Calendar sCal = eta.getStart().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                _type1TextView.setText(R.string.exactly_on);
                _date1TextView.setText(sdf.format(sCal.getTime()));
                _type2TextView.setVisibility(GONE);
                _date2TextView.setVisibility(GONE);

            } else if (schedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                Calendar sCal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar eCal = schedule.getServiceWindow().getEnd().getCalendar();

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
                        sdf1.format(sCal.getTime()), sdf1.format(eCal.getTime())));

            } else if (schedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                Calendar sCal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar eCal = schedule.getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);
                _type1TextView.setText(R.string.between);
                _date1TextView.setText(
                        getContext().getString(R.string.schedule_open_range_format,
                                sdf.format(sCal.getTime()),
                                sdf.format(eCal.getTime())));

                _type2TextView.setVisibility(GONE);
                _date2TextView.setVisibility(GONE);
            } else { //if (schedule.getType() == Schedule.Type.EXACT) {
                Calendar sCal = schedule.getServiceWindow().getStart().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy @ hh:mma", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                _type1TextView.setText(R.string.exactly_on);
                _date1TextView.setText(sdf.format(sCal.getTime()));
                _type2TextView.setVisibility(GONE);
                _date2TextView.setVisibility(GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final View.OnClickListener _editEta_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EtaDialog.show(App.get(), DIALOG_ETA, _workOrder);
        }
    };
}

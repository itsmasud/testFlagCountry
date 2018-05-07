package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.ETAStatus;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizView;
import com.fieldnation.v2.ui.dialog.EtaDialog;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "ScheduleSummaryView";

    private static final String DIALOG_ETA = TAG + ".etaDialog";

    // UI
    private ListItemTwoHorizView _firstView;
    private ListItemTwoHorizView _secondView;
    private ListItemTwoHorizView _etaView;

    // Data
    private WorkOrder _workOrder;
    private boolean _dirty = true;

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

        _firstView = findViewById(R.id.first_view);
        _secondView = findViewById(R.id.second_view);
        _etaView = findViewById(R.id.eta_view);
        setVisibility(View.GONE);
        refresh();
    }

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _dirty = true;
        _workOrder = workOrder;
        refresh();
    }

    private void refresh() {
        if (_workOrder == null)
            return;

        if (_firstView == null)
            return;

        if (!_dirty)
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

/*        if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.ADD)) {
            _editEtaButton.setVisibility(VISIBLE);
            _editEtaButton.setText(R.string.btn_set_eta);
            _editEtaButton.setOnClickListener(_setEta_onClick);
        } else*/
        if (_workOrder.getEta().getActionsSet().contains(ETA.ActionsEnum.EDIT)) {
            _etaView.setEnabled(true);
            _etaView.setOnClickListener(_editEta_onClick);
        } else {
            _etaView.setOnClickListener(null);
            _etaView.setEnabled(false);
        }

        if (App.get().getOfflineState() == App.OfflineState.OFFLINE
                || App.get().getOfflineState() == App.OfflineState.UPLOADING) {
            ((TextView) _etaView.findViewById(R.id.key))
                    .setTextColor(getContext().getResources()
                            .getColor(R.color.fn_disabled_text));
            ((TextView) _etaView.findViewById(R.id.value))
                    .setTextColor(getContext().getResources()
                            .getColor(R.color.fn_disabled_text));
        } else {
            ((TextView) _etaView.findViewById(R.id.key))
                    .setTextColor(getContext().getResources()
                            .getColor(R.color.fn_light_text_statefull));
            ((TextView) _etaView.findViewById(R.id.value))
                    .setTextColor(getContext().getResources()
                            .getColor(R.color.fn_light_text_statefull));

        }

        Schedule schedule = _workOrder.getSchedule();
        try {
            if (schedule == null) {
                setVisibility(GONE);
                return;
            }

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(getResources().getStringArray(R.array.schedule_small_case_am_pm_array));

            if (_workOrder.getEta().getStatus().getName() != null
                    && _workOrder.getEta().getStatus().getName() != ETAStatus.NameEnum.UNCONFIRMED) {

                ETA eta = _workOrder.getEta();
                Calendar sCal = eta.getStart().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy @ h:mm a", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                _etaView.set("ETA", sdf.format(sCal.getTime()) + DateUtils.getDeviceTimezone(sCal));
                _etaView.setVisibility(VISIBLE);
            } else {
                _etaView.setVisibility(GONE);
            }


            if (schedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.HOURS) {
                Calendar sCal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar eCal = schedule.getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf1 = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
                sdf1.setDateFormatSymbols(symbols);
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d", Locale.getDefault());
                sdf2.setDateFormatSymbols(symbols);
                SimpleDateFormat sdf3 = new SimpleDateFormat("h:mm a", Locale.getDefault());
                sdf3.setDateFormatSymbols(symbols);

                if (sCal.get(Calendar.YEAR) == eCal.get(Calendar.YEAR)) {
                    _firstView.set("Between", sdf2.format(sCal.getTime()) + " - " + sdf1.format(eCal.getTime()));
                } else {
                    _firstView.set("Between", sdf1.format(sCal.getTime()) + " - " + sdf1.format(eCal.getTime()));
                }
                _secondView.set("During", sdf3.format(sCal.getTime()) + " - " + sdf3.format(eCal.getTime()) + DateUtils.getDeviceTimezone(eCal));

                // range
            } else if (schedule.getServiceWindow().getMode() == ScheduleServiceWindow.ModeEnum.BETWEEN) {
                Calendar sCal = schedule.getServiceWindow().getStart().getCalendar();
                Calendar eCal = schedule.getServiceWindow().getEnd().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM d, yyyy\n@ h:mm a", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                _firstView.set("Start After", sdf.format(sCal.getTime()) + DateUtils.getDeviceTimezone(sCal));
                _secondView.set("Finish By", sdf.format(eCal.getTime()) + DateUtils.getDeviceTimezone(eCal));
            } else { //if (schedule.getType() == Schedule.Type.EXACT) {
                Calendar sCal = schedule.getServiceWindow().getStart().getCalendar();

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                _firstView.set("Date", sdf.format(sCal.getTime()));

                sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                sdf.setDateFormatSymbols(symbols);

                _secondView.set("Time", sdf.format(sCal.getTime()) + DateUtils.getDeviceTimezone(sCal));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _dirty = false;
    }

    private final View.OnClickListener _editEta_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (App.get().getOfflineState() != App.OfflineState.OFFLINE
                    && App.get().getOfflineState() != App.OfflineState.UPLOADING) {
                EtaDialog.show(App.get(), DIALOG_ETA, _workOrder.getId(), _workOrder.getSchedule(),
                        _workOrder.getEta(), EtaDialog.PARAM_DIALOG_TYPE_EDIT);
            } else {
                TwoButtonDialog.show(App.get(), null, v.getContext().getResources().getString(R.string.not_available),
                        v.getContext().getResources().getString(R.string.not_available_body_text),
                        v.getContext().getResources().getString(R.string.btn_close), null, true, null);
            }
        }
    };
}

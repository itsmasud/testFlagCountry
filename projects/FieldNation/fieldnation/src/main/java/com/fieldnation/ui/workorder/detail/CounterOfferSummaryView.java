package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ExpenseCounterOfferView;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.ScheduleServiceWindow;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class CounterOfferSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "CounterOfferSummaryView";

    // UI
    private LinearLayout _payLayout;
    private TextView _payTextView;
    private LinearLayout _scheduleLayout;
    private TextView _scheduleTextView;
    private LinearLayout _expenseLayout;
    private Button _counterOfferButton;

    // Data
    private WorkOrder _workOrder;
    private Listener _listener;

    public CounterOfferSummaryView(Context context) {
        super(context);
        init();
    }

    public CounterOfferSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CounterOfferSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_counter_offer_summary, this);

        if (isInEditMode())
            return;

        _payLayout = (LinearLayout) findViewById(R.id.pay_layout);
        _payTextView = (TextView) findViewById(R.id.pay_textview);
        _scheduleLayout = (LinearLayout) findViewById(R.id.schedule_layout);
        _scheduleTextView = (TextView) findViewById(R.id.schedule_textview);
        _expenseLayout = (LinearLayout) findViewById(R.id.expense_layout);

        _counterOfferButton = (Button) findViewById(R.id.counterOffer_button);
        _counterOfferButton.setOnClickListener(_counterOffer_onClick);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_payLayout == null)
            return;

        Requests requests = _workOrder.getRequests();

        setVisibility(GONE);

        if (requests == null)
            return;

        if (requests.getOpenRequest() == null)
            return;


        if (requests.getOpenRequest().getPay() == null) {
            _payLayout.setVisibility(GONE);
        } else {
            final Pay pay = requests.getOpenRequest().getPay();
            String[] paytext = toDisplayStringLong(pay);
            String data = "";

            if (paytext[0] != null) {
                data = paytext[0];
            }

            if (paytext[1] != null) {
                data += "\n" + paytext[1];
            }

            if (misc.isEmptyOrNull(data)) {
                _payLayout.setVisibility(GONE);
            } else {
                _payTextView.setText(data);
                setVisibility(VISIBLE);
                _payLayout.setVisibility(VISIBLE);
            }
        }

        if (requests.getOpenRequest().getSchedule() == null) {
            _scheduleLayout.setVisibility(GONE);
        } else {
            Schedule schedule = requests.getOpenRequest().getSchedule();
            _scheduleTextView.setText(getDisplayString(schedule));
            _scheduleLayout.setVisibility(VISIBLE);
            setVisibility(VISIBLE);
        }

        // expense
        final Expense[] expenses = requests.getOpenRequest().getExpenses();
        if (expenses == null || expenses.length == 0) {
            _expenseLayout.setVisibility(GONE);
        } else {
            _expenseLayout.setVisibility(VISIBLE);
            _expenseLayout.removeAllViews();
            for (Expense expense : expenses) {
                ExpenseCounterOfferView v = new ExpenseCounterOfferView(getContext());
                v.setExpense(expense);
                _expenseLayout.addView(v);
            }
        }
    }


    public String getDisplayString(Schedule schedule) {

        if (schedule.getServiceWindow().getMode().equals(ScheduleServiceWindow.ModeEnum.EXACT)) {
            try {
                String dayDate;
                String time = "";

                Calendar cal = schedule.getServiceWindow().getStart().getCalendar();
                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                time = DateUtils.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

                return "Exactly on " + dayDate + " @ " + time;

            } catch (ParseException e) {
                Log.v(TAG, e);
            }
        } else {
            try {
                Calendar cal = schedule.getServiceWindow().getStart().getCalendar();
                String dayDate;
                String time = "";

                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                time = DateUtils.formatTime(cal, false);

                String msg = "Between " + dayDate + " @ " + time + "\nand";

                Calendar cal2 = schedule.getServiceWindow().getEnd().getCalendar();

                // same day
                if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                    time = DateUtils.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    msg += time;

                } else {
                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + DateUtils.formatDateLong(cal2);
                    time = DateUtils.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                    msg += dayDate + " @ " + time;
                }

                return msg;

            } catch (ParseException e) {
                Log.v(TAG, e);
            }
        }

        return null;
    }


    public String[] toDisplayStringLong(Pay pay) {
        String line1 = null;
        String line2 = null;

        switch (pay.getType()) {
            case FIXED:
                line1 = misc.toCurrency(pay.getBase().getAmount()) + " Fixed";
                break;
            case HOURLY:
                line1 = misc.toCurrency(pay.getBase().getAmount()) + " per hr up to " + pay.getBase().getUnits() + " hours.";
                break;
            case BLENDED:
                line1 = misc.toCurrency(pay.getBase().getAmount()) + " for the first " + pay.getBase().getUnits() + " hours.";
                line2 = "Then " + misc.toCurrency(pay.getAdditional().getAmount()) + " per hr up to " + pay.getAdditional().getUnits() + " hours.";
                break;
            case DEVICE:
                line1 = misc.toCurrency(pay.getBase().getAmount()) + " per device up to " + pay.getBase().getUnits() + " devices.";
                break;
        }

        return new String[]{line1, line2};
    }



    private final View.OnClickListener _counterOffer_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onCounterOffer();
        }
    };

    public interface Listener {
        void onCounterOffer();
    }

}

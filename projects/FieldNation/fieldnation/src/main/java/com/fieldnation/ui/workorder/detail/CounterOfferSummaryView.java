package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CounterOfferInfo;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class CounterOfferSummaryView extends LinearLayout {
    private static final String TAG = "CounterOfferSummaryView";

    // UI
    private LinearLayout _payLayout;
    private TextView _payTextView;
    private LinearLayout _scheduleLayout;
    private TextView _scheduleTextView;
    private LinearLayout _expenseLayout;
    private TextView _expenseTextView;

    // Data
    private Workorder _workorder;

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
        _expenseTextView = (TextView) findViewById(R.id.expense_description);
    }

    public void setData(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_payLayout == null)
            return;

        CounterOfferInfo co = _workorder.getCounterOfferInfo();

        setVisibility(GONE);

        if (co == null) {
            return;
        }

        if (co.getPay() == null) {
            _payLayout.setVisibility(GONE);
        } else {
            Pay pay = co.getPay();
            String[] paytext = pay.toDisplayStringLong();
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

        if (co.getSchedule() == null) {
            _scheduleLayout.setVisibility(GONE);
        } else {
            Schedule schedule = co.getSchedule();

            String data = schedule.getFormatedDate() + "\n" + schedule.getFormatedTime();

            if (schedule.getDuration() != null) {
                data += "\n" + schedule.getDuration() + " hours";
            }

            _scheduleTextView.setText(data);
            _scheduleLayout.setVisibility(VISIBLE);
            setVisibility(VISIBLE);
        }

        // expense
        Expense[] expenses = co.getExpense();
        if (expenses == null || expenses.length == 0) {
            _expenseLayout.setVisibility(GONE);
        } else {
            String data = "";
            for (Expense expense : expenses) {
                data += expense.getDescription() + "        " + misc.toCurrency(expense.getPrice()) + "\n";
            }

            if (misc.isEmptyOrNull(data)) {
                _expenseLayout.setVisibility(GONE);
            } else {
                _expenseLayout.setVisibility(VISIBLE);
                _expenseTextView.setText(data);
            }
        }
    }
}

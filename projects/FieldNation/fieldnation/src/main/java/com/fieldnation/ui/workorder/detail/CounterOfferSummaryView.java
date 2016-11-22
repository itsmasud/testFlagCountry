package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.CounterOfferInfo;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntools.misc;
import com.fieldnation.ui.ExpenseCounterOfferView;

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
    private Button _counterOfferButton;

    // Data
    private Workorder _workorder;
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

    public void setData(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
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
            _scheduleTextView.setText(schedule.getDisplayString(false));
            _scheduleLayout.setVisibility(VISIBLE);
            setVisibility(VISIBLE);
        }

        // expense
        Expense[] expenses = co.getExpense();
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

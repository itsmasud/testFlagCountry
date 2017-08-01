package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Michael Carver on 12/16/2015.
 */
public class MonthHeaderView extends RelativeLayout {
    private static final String TAG = "MonthHeaderView";

    // UI
    private TextView _monthTextView;
    private TextView _yearTextView;
//    private TextView _moneyTextView;

    // Data

    /*-*****************************-*/
    /*-			Life cycle			-*/
    /*-*****************************-*/
    public MonthHeaderView(Context context) {
        super(context);
        init();
    }

    public MonthHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_month_header, this);

        if (isInEditMode())
            return;

        _monthTextView = (TextView) findViewById(R.id.month_textview);
//        _moneyTextView = (TextView) findViewById(R.id.money_textview);
        _yearTextView = (TextView) findViewById(R.id.year_textview);
    }

    public void setData(Header header) {
        if (header.startDate == null) {
            _monthTextView.setText("Pending");
            _yearTextView.setVisibility(GONE);
//            _moneyTextView.setText(misc.toCurrency(header.amount));
            return;
        }
        try {
            Calendar calendar = ISO8601.toCalendar(header.startDate);
            Calendar now = Calendar.getInstance();

            if (misc.isEmptyOrNull(header.endDate)) {
                _monthTextView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()) + " " + new SimpleDateFormat("yyyy").format(calendar.getTime()));
            } else {
                Calendar end = ISO8601.toCalendar(header.endDate);
                _monthTextView.setText(
                        new SimpleDateFormat("MMMM").format(end.getTime())
                                + " - "
                                + new SimpleDateFormat("MMMM").format(calendar.getTime())
                );
            }

            if (calendar.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
                _yearTextView.setVisibility(VISIBLE);
                _yearTextView.setText(new SimpleDateFormat("y").format(calendar.getTime()));
            } else {
                _yearTextView.setVisibility(GONE);
            }

//            _moneyTextView.setText(misc.toCurrency(header.amount));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static class Header {
        public String startDate;
        public String endDate;
        public double amount = 0.0;
        public int hash = 0;
        public boolean isRange = false;

        public Header(String startDate, double amount, int hash) {
            this.startDate = startDate;
            this.amount = amount;
            this.hash = hash;
            this.isRange = false;
        }

        public Header(String startDate, String endDate, double amount, int hash) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.amount = amount;
            this.hash = hash;
            this.isRange = false;
        }
    }


}

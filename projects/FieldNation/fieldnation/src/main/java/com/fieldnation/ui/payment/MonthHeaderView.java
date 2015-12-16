package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

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
    private TextView _moneyTextView;

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
        _moneyTextView = (TextView) findViewById(R.id.money_textview);
        _yearTextView = (TextView) findViewById(R.id.year_textview);
    }

    public void setData(String iso8601, double amount) {
        try {
            Calendar calendar = ISO8601.toCalendar(iso8601);
            Calendar now = Calendar.getInstance();

            _monthTextView.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()));

            if (calendar.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
                _yearTextView.setVisibility(VISIBLE);
                _yearTextView.setText(new SimpleDateFormat("y").format(calendar));
            } else {
                _yearTextView.setVisibility(GONE);
            }

            _moneyTextView.setText(misc.toCurrency(amount));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}

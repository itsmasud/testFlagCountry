package com.fieldnation.v2.ui.worecycler;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.DateUtils;

import java.util.Calendar;

/**
 * Created by Michael on 3/10/2016.
 */
public class ListTimeHeader extends RelativeLayout {
    private static final String TAG = "ListTimeHeader";

    // Ui
    private TextView _dayTextView;
    private TextView _dateTextView;
    private TextView _dotTextView;

    // Data
    private Calendar _cal = null;

    public ListTimeHeader(Context context) {
        super(context);
        init();
    }

    public ListTimeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListTimeHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_list_time_header, this);

        if (isInEditMode())
            return;

        _dayTextView = (TextView) findViewById(R.id.day_textView);
        _dateTextView = (TextView) findViewById(R.id.date_textView);
        _dotTextView = (TextView) findViewById(R.id.dot_textView);

        populateUi();
    }

    private void populateUi() {
        if (_cal == null)
            return;

        if (_dotTextView == null || _dayTextView == null || _dateTextView == null)
            return;

        Calendar _now = Calendar.getInstance();

        if (DateUtils.isToday(_cal)) {
            _dotTextView.setVisibility(VISIBLE);
            _dayTextView.setVisibility(VISIBLE);
            _dayTextView.setText(R.string.today);
            _dayTextView.setTypeface(_dayTextView.getTypeface(), Typeface.BOLD);
        } else if (DateUtils.isTomorrow(_cal)) {
            _dotTextView.setVisibility(VISIBLE);
            _dayTextView.setVisibility(VISIBLE);
            _dayTextView.setText(R.string.tomorrow);
            _dayTextView.setTypeface(_dayTextView.getTypeface(), Typeface.NORMAL);
        } else {
            _dotTextView.setVisibility(GONE);
            _dayTextView.setVisibility(GONE);
        }

        long days = (_cal.getTimeInMillis() - System.currentTimeMillis()) / 86400000L;
        if (_now.get(Calendar.YEAR) != _cal.get(Calendar.YEAR)) {
            _dateTextView.setText(DateUtils.formatDateLong(_cal));
        } else if (days >= 0 && days < 6) {
            _dateTextView.setText(DateUtils.formatDateReallyLongNoYear(_cal));
        } else {
            _dateTextView.setText(DateUtils.formatDateLongNoYear(_cal));
        }
    }

    public void setDate(Calendar utc) {
        _cal = utc;

        populateUi();
    }
}

package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

public class BundleEtaCardView extends RelativeLayout {


    private TextView _titleTextView;
    private TextView _dateTextView;
    private TextView _timeTextView;
    private TextView _hyphenTextView;
    private TextView _date2TextView;
    private TextView _time2TextView;
    private TextView _addressTextView;
    private TextView _locationTextView;
    private OnClickListener _listener;

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
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _hyphenTextView = (TextView) findViewById(R.id.hyphen_textview);
        _time2TextView = (TextView) findViewById(R.id.time2_textview);
        _date2TextView = (TextView) findViewById(R.id.date2_textview);
        _addressTextView = (TextView) findViewById(R.id.address_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);


        setOnClickListener(_this_onClick);
    }

    public void setListener(OnClickListener listener) {
        _listener = listener;
    }


    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClick(BundleEtaCardView.this);
        }
    };

    public interface OnClickListener {
        void onClick(BundleEtaCardView row);
    }
}

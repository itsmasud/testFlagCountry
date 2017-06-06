package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

public class HoldReasonCard extends RelativeLayout {

    private TextView _holdReasonTextView;

    // Passed Data
    private String _reason;

    public HoldReasonCard(Context context) {
        super(context);
        init();
    }

    public HoldReasonCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HoldReasonCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_hold_reason_card, this);

        if (isInEditMode())
            return;

        _holdReasonTextView = (TextView) findViewById(R.id.hold_reason_textview);


        setOnClickListener(null);
    }

    public void setData(String reason) {
        _reason = reason;
        populateUi();
    }


    private void populateUi() {
        if (_reason == null)
            return;

        _holdReasonTextView.setText(_reason);
    }
}

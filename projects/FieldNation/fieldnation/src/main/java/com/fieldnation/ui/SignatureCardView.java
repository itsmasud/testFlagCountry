package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.v2.data.model.Signature;

/**
 * Created by michael.carver on 12/5/2014.
 */
public class SignatureCardView extends RelativeLayout {
    private static final String TAG = "SignatureCardView";

    // Ui
    private TextView _nameTextView;
    private TextView _dateTextView;

    // Data
    private Signature _sig;

    public SignatureCardView(Context context) {
        super(context);
        init();
    }

    public SignatureCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_signature_card, this);

        if (isInEditMode())
            return;

        _nameTextView = findViewById(R.id.name_textview);
        _dateTextView = findViewById(R.id.date_textview);

        populateUi();
    }

    public void setSignature(Signature sig) {
        _sig = sig;

        populateUi();
    }

    public Signature getSignature() {
        return _sig;
    }

    private void populateUi() {
        if (_dateTextView == null)
            return;

        if (_sig == null)
            return;

        _nameTextView.setText(_sig.getName());
        try {
            _dateTextView.setText("Signed by " + _sig.getName()
                    + " on " + DateUtils.formatDateLong(_sig.getCreated().getCalendar()));
            _dateTextView.setVisibility(VISIBLE);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            _dateTextView.setVisibility(GONE);
        }
    }
}

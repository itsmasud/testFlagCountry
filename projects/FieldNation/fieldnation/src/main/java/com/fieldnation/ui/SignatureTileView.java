package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

/**
 * Created by michael.carver on 12/5/2014.
 */
public class SignatureTileView extends RelativeLayout {
    private static final String TAG = "SignatureTileView";

    // Ui
    private TextView _nameTextView;
    private TextView _dateTextView;

    // Data
    private Signature _sig;

    public SignatureTileView(Context context) {
        super(context);
        init();
    }

    public SignatureTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_signature_tile, this);

        if (isInEditMode())
            return;

        _nameTextView = (TextView) findViewById(R.id.name_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);

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

        _nameTextView.setText(_sig.getPrintName());
        try {
            _dateTextView.setText("Signed by " + _sig.getPrintName()
                    + " on " + misc.formatDateLong(ISO8601.toCalendar(_sig.getDateSaved())));
            _dateTextView.setVisibility(VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
            _dateTextView.setVisibility(GONE);
        }
    }


}

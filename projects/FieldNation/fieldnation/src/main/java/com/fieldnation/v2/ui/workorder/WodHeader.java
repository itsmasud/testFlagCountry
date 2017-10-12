package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 10/12/17.
 */

public class WodHeader extends RelativeLayout {
    private static final String TAG = "WodHeader";

    // Ui
    private TextView _textview;

    // Data
    private String text;

    public WodHeader(Context context) {
        super(context);
        init();
    }

    public WodHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(attrs);
        init();
    }

    public WodHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wod_header, this, true);

        if (isInEditMode()) return;

        _textview = findViewById(R.id.title_textview);

        populateUi();
    }

    private void handleAttributes(AttributeSet attributeSet) {
        if (isInEditMode()) return;

        TypedArray ta = getContext().obtainStyledAttributes(attributeSet, new int[]{android.R.attr.text});
        try {
            setText(ta.getString(0));
            Log.v(TAG, "handleAttributes: " + ta.getString(0));
        } finally {
            ta.recycle();
        }
    }

    public void setText(String text) {
        this.text = text;
        populateUi();
    }

    private void populateUi() {
        if (_textview == null)
            return;

        if (text == null)
            return;

        _textview.setText(text);
    }
}

package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by mc on 6/29/17.
 */

public class ListItemTwoHorizView extends RelativeLayout {
    private static final String TAG = "ListItemTwoHorizView";

    // Ui
    private TextView _keyTextView;
    private TextView _valueTextView;

    // Data
    private String _key;
    private String _value;

    public ListItemTwoHorizView(Context context) {
        super(context);
        init();
    }

    public ListItemTwoHorizView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemTwoHorizView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v3_list_two_horiz, this);

        if (isInEditMode())
            return;

        _keyTextView = findViewById(R.id.key);
        _valueTextView = findViewById(R.id.value);

        populateUi();
    }

    public void set(String key, String value) {
        _key = key;
        _value = value;

        populateUi();
    }

    private void populateUi() {
        if (_valueTextView == null || _keyTextView == null)
            return;

        if (_key == null)
            _keyTextView.setText("");
        else
            _keyTextView.setText(_key);

        if (_value == null)
            _valueTextView.setText("");
        else
            _valueTextView.setText(_value);
    }
}

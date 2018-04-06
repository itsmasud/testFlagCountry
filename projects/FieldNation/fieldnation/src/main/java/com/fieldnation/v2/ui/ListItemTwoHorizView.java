package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;

import org.w3c.dom.Text;

/**
 * Created by mc on 6/29/17.
 */

public class ListItemTwoHorizView extends RelativeLayout {
    private static final String TAG = "ListItemTwoHorizView";

    // Ui
    private TextView _keyTextView;
    private TextView _valueTextView;
    private TextView _alertView;

    // Data
    private String _key;
    private String _value;
    private boolean _alert = false;
    private String _alertText = null;

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
        _alertView = findViewById(R.id.alert);

        populateUi();
    }

    public void set(String key, String value) {
        _key = key;
        _value = value;

        populateUi();
    }

    public void setAlert(boolean enabled) {
        _alert = enabled;

        populateUi();
    }

    public void setAlertIcon(String alert) {
        _alertText = alert;

        populateUi();
    }

    public String getValue() {
        return _value;
    }

    public String getKey() {
        return _key;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _keyTextView.setEnabled(enabled);
        _valueTextView.setEnabled(enabled);
    }

    private void populateUi() {
        if (_valueTextView == null || _keyTextView == null)
            return;

        if (misc.isEmptyOrNull(_key))
            _keyTextView.setText("");
        else
            _keyTextView.setText(_key);

        if (misc.isEmptyOrNull(_value))
            _valueTextView.setText("");
        else
            _valueTextView.setText(_value);

        if (_alertText != null)
            _alertView.setText(_alertText);

        _alertView.setVisibility(_alert ? VISIBLE : GONE);
    }
}

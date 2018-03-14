package com.fieldnation.v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;

/**
 * Created by Shoaib on 11/07/17.
 */

public class ListItemTwoHorizTwoVertView extends RelativeLayout {
    private static final String TAG = "ListItemTwoHorizTwoVertView";

    // Ui
    private TextView _keyTitleTextView;
    private TextView _valueTitleTextView;
    private TextView _keyDescriptionTextView;
    private TextView _valueDescriptionTextView;
    private TextView _alertTextView;

    // Data
    private String _keyTitle;
    private String _keyDescription;
    private String _valueTitle;
    private String _valueDescription;
    private boolean _alertVisible = false;

    public ListItemTwoHorizTwoVertView(Context context) {
        super(context);
        init();
    }

    public ListItemTwoHorizTwoVertView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListItemTwoHorizTwoVertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v3_list_two_horiz_two_vertz, this);

        if (isInEditMode())
            return;

        _keyTitleTextView = findViewById(R.id.keyTitle_textview);
        _valueTitleTextView = findViewById(R.id.valueTitle_textview);
        _keyDescriptionTextView = findViewById(R.id.keyDescription_textview);
        _valueDescriptionTextView = findViewById(R.id.valueDescription_textview);
        _alertTextView = findViewById(R.id.alert);

        populateUi();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _keyTitleTextView.setEnabled(enabled);
        _valueTitleTextView.setEnabled(enabled);
        _keyDescriptionTextView.setEnabled(enabled);
        _valueDescriptionTextView.setEnabled(enabled);
    }

    public void set(String keyTitle, String keyDescription, String valueTitle, String valueDescription) {
        _keyTitle = keyTitle;
        _keyDescription = keyDescription;
        _valueTitle = valueTitle;
        _valueDescription = valueDescription;

        populateUi();
    }

    public void set(String keyTitle, String valueTitle) {
        set(keyTitle, null, valueTitle, null);

        populateUi();
    }

    public void setAlertVisible(boolean visible) {
        _alertVisible = visible;

        populateUi();
    }

    private void populateUi() {
        if (_valueTitleTextView == null || _keyTitleTextView == null)
            return;

        if (misc.isEmptyOrNull(_keyTitle))
            _keyTitleTextView.setText("");
        else
            _keyTitleTextView.setText(_keyTitle);

        if (_valueTitle == null)
            _valueTitleTextView.setText("");
        else
            _valueTitleTextView.setText(_valueTitle);

        if (misc.isEmptyOrNull(_keyDescription))
            _keyDescriptionTextView.setVisibility(GONE);
        else {
            _keyDescriptionTextView.setVisibility(VISIBLE);
            _keyDescriptionTextView.setText(_keyDescription);
        }

        if (misc.isEmptyOrNull(_valueDescription))
            _valueDescriptionTextView.setVisibility(GONE);
        else {
            _valueDescriptionTextView.setVisibility(VISIBLE);
            _valueDescriptionTextView.setText(_valueDescription);
        }

        if (_alertVisible) {
            _alertTextView.setVisibility(VISIBLE);
        } else {
            _alertTextView.setVisibility(GONE);
        }


    }
}

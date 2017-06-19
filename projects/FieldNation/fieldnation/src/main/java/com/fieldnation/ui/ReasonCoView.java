package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.HintArrayAdapter;
import com.fieldnation.ui.HintSpinner;

/**
 * Created by shoaib.ahmed on 06/07/2017.
 */
public class ReasonCoView extends RelativeLayout {
    private static final String TAG = "ReasonCoView";

    // Ui
    private EditText _requestReasonEditText;
    private HintSpinner _expireDurationSpinner;

    // Data
    private int _currentPosition = 1;
    private int[] _durations;
    private long expireMillisecond = -1;

    public ReasonCoView(Context context) {
        super(context);
        init();
    }

    public ReasonCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReasonCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.v(TAG, "init");
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_reasons_tile, this);

        if (isInEditMode())
            return;

        _requestReasonEditText = (EditText) findViewById(R.id.request_reason_edittext);

        _expireDurationSpinner = (HintSpinner) findViewById(R.id.expire_duration_spinner);
        _expireDurationSpinner.setOnItemSelectedListener(_expireSpinner_selected);
        HintArrayAdapter adapter = HintArrayAdapter.createFromResources(getContext(),
                R.array.co_expire_duration_titles,
                R.layout.view_spinner_item_gray);
        adapter.setDropDownViewResource(android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _expireDurationSpinner.setAdapter(adapter);

        _durations = getContext().getResources().getIntArray(R.array.co_expire_duration_values);

    }

    public String getReason() {
        return _requestReasonEditText.getText().toString();
    }

    public long getExpiryTime() {
        return expireMillisecond;
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/

    private final AdapterView.OnItemSelectedListener _expireSpinner_selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position != _currentPosition) {
                _currentPosition = position;

                if (position != 0)
                    expireMillisecond = System.currentTimeMillis() + (_durations[_currentPosition] * 1000);
                else
                    expireMillisecond = -1;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            _currentPosition = 0;
            expireMillisecond = -1;
        }
    };

}
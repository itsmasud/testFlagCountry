package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.v2.data.model.Schedule;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class ScheduleCoView extends RelativeLayout {
    private static final String TAG = "ScheduleCoView";

    // Ui
    private TextView _statusTextView;
    private TextView _bodyTextView;

    private Button _clearButton;
    private Button _changeButton;

    // Data
    private Schedule _schedule;
    private boolean _isCounter;
    private Listener _listener;


    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public ScheduleCoView(Context context) {
        super(context);
        init();
    }

    public ScheduleCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScheduleCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_schedule_tile, this);

        if (isInEditMode())
            return;

        _statusTextView = (TextView) findViewById(R.id.status_textview);
        _bodyTextView = (TextView) findViewById(R.id.body_textview);

        _clearButton = (Button) findViewById(R.id.clear_button);
        _clearButton.setOnClickListener(_clear_onClick);

        _changeButton = (Button) findViewById(R.id.change_button);
        _changeButton.setOnClickListener(_change_onClick);

        populateUi();
    }

    public void setSchedule(Schedule schedule, boolean isCounterOffer) {
        _schedule = schedule;
        _isCounter = isCounterOffer;
        populateUi();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
        if (_schedule == null)
            return;

        if (_statusTextView == null)
            return;

        if (_bodyTextView == null)
            return;

        if (_isCounter) {
            _statusTextView.setText("Your Schedule");
        } else {
            _statusTextView.setText("Buyer's Schedule");
        }

        String display = _schedule.getServiceWindow().getDisplayString(false);
        _bodyTextView.setText(display);
    }

    /*-*****************************-*/
    /*-             Events          -*/
    /*-*****************************-*/

    private final View.OnClickListener _clear_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onClear();
        }
    };

    private final View.OnClickListener _change_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onChange(_schedule);
        }
    };

    public interface Listener {
        void onClear();

        void onChange(Schedule schedule);
    }
}

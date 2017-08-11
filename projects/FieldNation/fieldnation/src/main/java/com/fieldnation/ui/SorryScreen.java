package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class SorryScreen extends RelativeLayout {
    private static final String TAG = "SorryScreen";

    // Ui
    private Button _doneButton;

    // Data
    private Listener _listener;

    private boolean _timerComplete = false;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public SorryScreen(Context context) {
        super(context);
        init();
    }

    public SorryScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SorryScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_reject_work, this);

        if (isInEditMode()) return;

        _doneButton = findViewById(R.id.done_button);
        _doneButton.setOnClickListener(_done_onClick);
    }

    public void startTimer() {
        _timerComplete = false;

        _doneButton.setVisibility(View.GONE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                _timerComplete = true;
                showDoneButton();
            }
        }, 5000);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void showDoneButton() {
        if (!_timerComplete)
            return;

        if (_doneButton != null) {
            _doneButton.setVisibility(View.VISIBLE);
        }
    }

    /*-*********************************-*/
    /*-             Events              -*/
    /*-*********************************-*/
    private final View.OnClickListener _done_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _listener.onDoneClick();
        }
    };

    public interface Listener {
        void onDoneClick();
    }
}

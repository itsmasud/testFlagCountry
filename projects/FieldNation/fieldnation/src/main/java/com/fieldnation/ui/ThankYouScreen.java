package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by michael.carver on 12/3/2014.
 */
public class ThankYouScreen extends RelativeLayout {
    private static final String TAG = "ThankYouScreen";

    // Ui
    private FnToolBarView _fnToolbarView;
    private Button _doneButton;

    // Data
    private Listener _listener;

    private String _name;
    private String _signatureJson;
    private String _workorder;

    private boolean _timerComplete = false;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public ThankYouScreen(Context context) {
        super(context);
        init();
    }

    public ThankYouScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThankYouScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_thank_you, this);

        if (isInEditMode()) return;

        _fnToolbarView = findViewById(R.id.fnToolbar);
        _fnToolbarView.getToolbar().setNavigationIcon(null);
        _fnToolbarView.getToolbar().setVisibility(GONE);

        _doneButton = findViewById(R.id.done_button);
        _doneButton.setOnClickListener(_done_onClick);

        _timerComplete = false;
    }

    public void startTimer() {
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
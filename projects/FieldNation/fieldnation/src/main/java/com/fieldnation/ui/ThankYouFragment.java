package com.fieldnation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fieldnation.R;

/**
 * Created by michael.carver on 12/3/2014.
 */
public class ThankYouFragment extends FragmentBase {
    private static final String TAG = "ThankYouFragment";

    // Ui
    private Button _doneButton;

    // Data
    private Handler _handler;
    private Listener _listener;

    private String _name;
    private String _signatureJson;
    private String _workorder;

    private boolean _uploadComplete = false;
    private boolean _timerComplete = false;

    /*-*********************************-*/
    /*-             Life Cycle          -*/
    /*-*********************************-*/
    public static ThankYouFragment getInstance(FragmentManager fm, String tag) {
        return getInstance(fm, tag, ThankYouFragment.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_thank_you, container, false);

        _doneButton = (Button) v.findViewById(R.id.done_button);
        _doneButton.setOnClickListener(_done_onClick);

        _handler = new Handler();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        _timerComplete = false;
        _uploadComplete = false;

        _doneButton.setVisibility(View.GONE);
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _timerComplete = true;
                showDoneButton();
            }
        }, 5000);
    }

    public void setUploadComplete() {
        _uploadComplete = true;
        showDoneButton();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void showDoneButton() {
//        if (!_uploadComplete)
//            return;

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
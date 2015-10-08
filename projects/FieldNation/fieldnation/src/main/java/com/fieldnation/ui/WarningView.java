package com.fieldnation.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;

/**
 * Created by michael.carver on 12/22/2014.
 */
public class WarningView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("WarningView");

    // Ui
    private Button _retryButton;
    private TextView _titleTextView;

    // Data
    private GlobalTopicClient _globalClient;

    private String _title = null;
    private String _buttonText = null;
    private Bundle _callback;


    public WarningView(Context context) {
        super(context);
        init();
    }

    public WarningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WarningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_warning, this);

        if (isInEditMode())
            return;

        _retryButton = (Button) findViewById(R.id.retry_button);
        _retryButton.setOnClickListener(_retry_onClick);

        _titleTextView = (TextView) findViewById(R.id.title_textview);

        _globalClient = new GlobalTopicClient(_globalTopic_listener);
        _globalClient.connect(getContext());

        populateUi();
    }

    @Override
    protected void onDetachedFromWindow() {
        _globalClient.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString("title", _title);
        bundle.putString("button", _buttonText);
        bundle.putBundle("callback", _callback);
        bundle.putParcelable("super", super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !(state instanceof Bundle)) {
            return;
        }
        Bundle bundle = (Bundle) state;

        if (bundle.containsKey("super")) {
            super.onRestoreInstanceState(bundle.getParcelable("super"));
        }

        if (bundle.containsKey("title")) {
            _title = bundle.getString("title");
        }

        if (bundle.containsKey("button")) {
            _buttonText = bundle.getString("button");
        }

        if (bundle.containsKey("callback")) {
            _callback = bundle.getBundle("callback");
        }
    }

    private void populateUi() {
        if (_retryButton == null)
            return;

        if (_title == null)
            return;

        _retryButton.setText(_buttonText);
        _titleTextView.setText(_title);
    }

    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.subNetworkState();
        }

        @Override
        public void onNetworkDisconnected() {
            _title = "Can't connect to servers.";
            _buttonText = "RETRY";
            WarningView.this.setVisibility(View.VISIBLE);
            populateUi();
        }

        @Override
        public void onNetworkConnected() {
            WarningView.this.setVisibility(View.GONE);
        }
    };

    private final OnClickListener _retry_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WarningView.this.setVisibility(View.GONE);
            GlobalTopicClient.networkConnect(getContext());
        }
    };
}

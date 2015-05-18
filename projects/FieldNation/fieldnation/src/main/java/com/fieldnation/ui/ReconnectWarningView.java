package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;

/**
 * Created by michael.carver on 12/22/2014.
 */
public class ReconnectWarningView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ReconnectWarningView");


    // Ui
    private Button _retryButton;

    // Data
    private GlobalTopicClient _globalClient;


    public ReconnectWarningView(Context context) {
        super(context);
        init();
    }

    public ReconnectWarningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReconnectWarningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_reconnect, this);

        if (isInEditMode())
            return;

        _retryButton = (Button) findViewById(R.id.retry_button);
        _retryButton.setOnClickListener(_retry_onClick);

        _globalClient = new GlobalTopicClient(_globalTopic_listener);
        _globalClient.connect(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        _globalClient.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.registerNetworkState();
        }

        @Override
        public void onNetworkDisconnected() {
            ReconnectWarningView.this.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNetworkConnected() {
            ReconnectWarningView.this.setVisibility(View.GONE);
        }

    };

    private final OnClickListener _retry_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalTopicClient.dispatchNetworkConnect(getContext());
            ReconnectWarningView.this.setVisibility(View.GONE);
        }
    };
}

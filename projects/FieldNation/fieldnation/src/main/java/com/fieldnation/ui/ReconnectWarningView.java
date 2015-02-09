package com.fieldnation.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.Topics;

/**
 * Created by michael.carver on 12/22/2014.
 */
public class ReconnectWarningView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ui.ReconnectWarningView");


    // Ui
    private Button _retryButton;


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

        TopicService.registerListener(getContext(), 0, TAG, Topics.TOPIC_NETWORK_DOWN, _networkTopic);
        AuthTopicService.subscribeAuthState(getContext(), 0, TAG + ":AUTH", _authReceiver);
    }

    @Override
    protected void onDetachedFromWindow() {
        TopicService.delete(getContext(), TAG);
        TopicService.delete(getContext(), TAG + ":AUTH");
        super.onDetachedFromWindow();
    }

    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            ReconnectWarningView.this.setVisibility(View.GONE);
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            if (networkDown)
                ReconnectWarningView.this.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAuthenticationInvalidated() {
        }
    };

    private TopicReceiver _networkTopic = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "_networkTopic: " + topicId)
            ReconnectWarningView.this.setVisibility(View.VISIBLE);
        }
    };

    private OnClickListener _retry_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Topics.dispatchNetworkUp(getContext());
            ReconnectWarningView.this.setVisibility(View.GONE);
        }
    };
}

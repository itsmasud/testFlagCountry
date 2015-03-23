package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;

public class NotificationActionBarView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ui.NotificationActionBarView");

    // UI
    private TextView _countTextView;

    // data
    private Profile _profile = null;
    private GlobalTopicClient _client;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public NotificationActionBarView(Context context) {
        super(context);
        init();
    }

    public NotificationActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NotificationActionBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_notification_action_bar, this);

        _countTextView = (TextView) findViewById(R.id.count_textview);

        if (isInEditMode())
            return;

        setOnClickListener(_this_onClick);

        _client = new GlobalTopicClient(_topicClient_listener);
        _client.connect(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        _client.disconnect(getContext());
        super.onDetachedFromWindow();
    }


    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), NotificationListActivity.class);
            getContext().startActivity(intent);
        }
    };

    private GlobalTopicClient.Listener _topicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _client.registerGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            refresh();
        }
    };

    private void refresh() {
        if (_profile == null)
            return;

        int count = _profile.getNewNotificationCount();

        if (count == 0) {
            _countTextView.setVisibility(GONE);
        } else {
            _countTextView.setVisibility(VISIBLE);
            if (count >= 99) {
                _countTextView.setText("!!");
            } else {
                _countTextView.setText(count + "");
            }
        }

        //TODO if is cached consider requesting a new version
    }
}
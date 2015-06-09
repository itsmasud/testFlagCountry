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

public class MessagesActionBarView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("MessagesActionBarView");
    // UI
    private TextView _countTextView;

    // Data
    private Profile _profile = null;
    private GlobalTopicClient _client;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public MessagesActionBarView(Context context) {
        super(context);
        init();
    }

    public MessagesActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessagesActionBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_messages_action_bar, this);

        _countTextView = (TextView) findViewById(R.id.count_textview);

        if (isInEditMode())
            return;

        setOnClickListener(_this_onClickListener);

        _client = new GlobalTopicClient(_client_listener);
        _client.connect(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        _client.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    private final View.OnClickListener _this_onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), MessageListActivity.class);
            getContext().startActivity(intent);
        }
    };

    private final GlobalTopicClient.Listener _client_listener = new GlobalTopicClient.Listener() {
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

        int count = _profile.getUnreadMessageCount();

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
        // TODO consider requesting updated information if this is cached
    }

}
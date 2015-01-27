package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.Topics;

public class NotificationActionBarView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ui.NotificationActionBarView");

    // UI
    private TextView _countTextView;

    // data
    private Profile _profile = null;

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

        Topics.subscrubeProfileUpdated(getContext(), TAG + ":TopicService", _topicReceiver);
    }

    @Override
    protected void finalize() throws Throwable {
        TopicService.delete(getContext(), TAG + ":AuthTopicService");
        TopicService.delete(getContext(), TAG + ":TopicService");
        super.finalize();
    }

    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), NotificationListActivity.class);
            getContext().startActivity(intent);
        }
    };

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onRegister(int resultCode, String topicId) {
        }

        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (Topics.TOPIC_PROFILE_UPDATE.equals(topicId)) {
                parcel.setClassLoader(getContext().getClassLoader());
                _profile = parcel.getParcelable(Topics.TOPIC_PROFILE_PARAM_PROFILE);
            }
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
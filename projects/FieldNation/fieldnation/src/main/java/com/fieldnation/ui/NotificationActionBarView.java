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
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;

public class NotificationActionBarView extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("ui.NotificationActionBarView");

    // UI
    private TextView _countTextView;

    // data
    private ProfileService _profileService;
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

        AuthTopicService.subscribeAuthState(getContext(), 0, TAG + ":AuthTopicService", _authReceiver);

        setOnClickListener(_this_onClick);

        TopicService.registerListener(getContext(), 1, TAG + ":TopicService", ProfileService.TOPIC_PROFILE_INVALIDATED, _topicReceiver);
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

    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_profileService == null || isNew) {
                _profileService = new ProfileService(getContext(), username, authToken, _resultReciever);
                getContext().startService(_profileService.getMyUserInformation(0, true));
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _profileService = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _profileService = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(getContext());
        }
    };


    private TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onRegister(int resultCode, String topicId) {
        }

        @Override
        public void onUnregister(int resultCode, String topicId) {
        }

        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (topicId.equals(ProfileService.TOPIC_PROFILE_INVALIDATED)) {
                if (_profileService != null)
                    getContext().startService(_profileService.getMyUserInformation(0, false));
            }
        }
    };

    private WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            //Log.v(TAG, "WebServiceResultReceiver.onSuccess");
            String raw;
            try {
                raw = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                //Log.v(TAG, raw);
                JsonObject obj = new JsonObject(raw);
                _profile = Profile.fromJson(obj);
                refresh(resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public Context getContext() {
            return NotificationActionBarView.this.getContext();
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            AuthTopicService.requestAuthInvalid(getContext());
            _profileService = null;
        }
    };

    private void refresh(boolean isCached) {
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
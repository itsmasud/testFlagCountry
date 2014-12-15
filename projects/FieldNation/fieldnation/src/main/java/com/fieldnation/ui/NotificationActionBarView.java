package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;

public class NotificationActionBarView extends RelativeLayout {
    private static final String TAG = "ui.NotificationActionBarView";

    // UI
    private TextView _countTextView;

    // data
    private GlobalState _gs;
    private ProfileService _profileService;
    private Profile _profile = null;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public NotificationActionBarView(Context context) {
        this(context, null, -1);
    }

    public NotificationActionBarView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NotificationActionBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_notification_action_bar, this);

        _countTextView = (TextView) findViewById(R.id.count_textview);

        if (isInEditMode())
            return;

        _gs = (GlobalState) context.getApplicationContext();
        setOnClickListener(_this_onClick);

        _gs.requestAuthentication(_authclient);

        TopicService.registerListener(getContext(), 1, TAG, "NOTIFICATION_TEST", _topicReceiver);
    }

    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), NotificationListActivity.class);
            getContext().startActivity(intent);
        }
    };

    private AuthenticationClient _authclient = new AuthenticationClient() {

        @Override
        public void onAuthenticationFailed(Exception ex) {
            //Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
            _gs.requestAuthenticationDelayed(_authclient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _profileService = new ProfileService(getContext(), username, authToken, _resultReciever);
            getContext().startService(_profileService.getMyUserInformation(0, true));
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private TopicReceiver _topicReceiver = new TopicReceiver() {
        @Override
        public void onRegister(int resultCode, String topicId) {
            // TODO STUB TopicReceiver.onRegister()
            Log.v(TAG, "STUB .onRegister()");

        }

        @Override
        public void onUnregister(int resultCode, String topicId) {
            // TODO STUB TopicReceiver.onUnregister()
            Log.v(TAG, "STUB .onUnregister()");

        }

        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            // TODO STUB TopicReceiver.onTopic()
            Log.v(TAG, "STUB .onTopic()");

        }

        @Override
        public void onDelete(int resultCode, String topicId) {

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
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_profileService != null) {
                _gs.invalidateAuthToken(_profileService.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authclient);
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
    }
}
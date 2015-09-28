package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by Michael Carver on 9/22/2015.
 */
public class SwitchUserActivity extends AuthFragmentActivity {
    private static final String TAG = "SwitchUserActivity";

    public static final String INTENT_FIELD_TARGET_USER_ID = "SwitchUserActivity:INTENT_FIELD_TARGET_USER_ID";

    // Data
    private long _userId;

    // Serivces
    private GlobalTopicClient _globalTopicClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_switching);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(INTENT_FIELD_TARGET_USER_ID)) {
                _userId = intent.getLongExtra(INTENT_FIELD_TARGET_USER_ID, 0);
            }
        }

        ProfileClient.switchUser(this, _userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(this);
    }

    @Override
    protected void onPause() {
        if (_globalTopicClient != null && _globalTopicClient.isConnected())
            _globalTopicClient.disconnect(this);

        super.onPause();
    }

    private final GlobalTopicClient.Listener _globalTopicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subUserSwitched();
        }

        @Override
        public void onUserSwitched(Profile profile) {
            MyWorkActivity.startNew(SwitchUserActivity.this);
        }
    };

    public static void startNew(Context context, long userId) {
        Intent intent = new Intent(context, SwitchUserActivity.class);
        intent.putExtra(INTENT_FIELD_TARGET_USER_ID, userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

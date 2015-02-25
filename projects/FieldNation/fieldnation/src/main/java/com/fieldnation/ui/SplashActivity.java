package com.fieldnation.ui;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by michael.carver on 12/18/2014.
 */
public class SplashActivity extends AuthFragmentActivity {
    private static final String TAG = "ui.SplashActivity";

    private static final String STATE_PROFILE = "STATE_PROFILE";
    private static final String STATE_IS_AUTH = "STATE_IS_AUTH";
    private static final String STATE_SHOWING_DIALOG = "STATE_SHOWING_DIALOG";

    private Profile _profile = null;
    private boolean _isAuth = false;
    private boolean _calledMyWork = false;

    public SplashActivity() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_IS_AUTH)) {
                _isAuth = savedInstanceState.getBoolean(STATE_IS_AUTH);
            }
            if (savedInstanceState.containsKey(STATE_PROFILE)) {
                _profile = savedInstanceState.getParcelable(STATE_PROFILE);
            }
        }

        Log.v(TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_IS_AUTH, _isAuth);
        if (_profile != null) {
            outState.putParcelable(STATE_PROFILE, _profile);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        AuthTopicService.subscribeNeedUsernameAndPassword(this, TAG, _topicReceiver);
        AuthTopicService.requestAuthentication(this);
    }

    @Override
    protected void onDestroy() {
        TopicService.unRegisterListener(this, 0, TAG, AuthTopicService.TOPIC_AUTH_COMMAND);
        super.onDestroy();
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        Log.v(TAG, "onAuthentication");
        _isAuth = true;
        doNextStep();
    }

    @Override
    public void onAuthenticationFailed(boolean networkDown) {
        Log.v(TAG, "onAuthenticationFailed");
        if (!networkDown)
            AuthTopicService.requestAuthentication(this);
    }

    @Override
    public void onAuthenticationInvalidated() {
        Log.v(TAG, "onAuthenticationInvalidated");
        AuthTopicService.requestAuthentication(this);
    }

    private void doNextStep() {
        if (!_isAuth)
            return;

        if (_profile == null)
            return;

        Log.v(TAG, "doNextStep");

        if (_profile.isProvider()) {
            if (!_calledMyWork) {
                _calledMyWork = true;
                MyWorkActivity.startNew(this);
                finish();
            }
        }

    }

    @Override
    public void onProfile(Profile profile) {
        _profile = profile;
        doNextStep();
    }


    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "onTopic");

            if (AuthTopicService.TOPIC_AUTH_STARTUP.equals(topicId)) {
                if (parcel.getString(AuthTopicService.BUNDLE_PARAM_TYPE)
                        .equals(AuthTopicService.BUNDLE_PARAM_TYPE_NEED_PASSWORD)
                        && parcel.containsKey(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)) {

                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);

                    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                            parcel.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE));

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }
    };


    @Override
    public void onRefresh() {
    }

    public static void startNew(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

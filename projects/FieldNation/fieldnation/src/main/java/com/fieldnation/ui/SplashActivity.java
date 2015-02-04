package com.fieldnation.ui;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.auth.server.AuthActivity;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by michael.carver on 12/18/2014.
 */
public class SplashActivity extends AuthFragmentActivity {
    private static final String TAG = "ui.SplashActivity";

    public SplashActivity() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.v(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        AuthTopicService.requestAuthInvalid(this);
        AuthTopicService.subscribeNeedUsernameAndPassword(this, TAG, _topicReceiver);
        AuthTopicService.requestAuthentication(this);
    }

    @Override
    protected void onDestroy() {
        TopicService.unRegisterListener(this, 0, TAG + ":PASSWORD", AuthTopicService.TOPIC_AUTH_COMMAND);
        super.onDestroy();
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        Log.v(TAG, "onAuthentication");
        Intent intent = new Intent(SplashActivity.this, MyWorkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "onTopic");
            Log.v(TAG, parcel.toString());
            if (AuthTopicService.TOPIC_AUTH_STARTUP.equals(topicId)
                    && parcel.getString(AuthTopicService.BUNDLE_PARAM_TYPE)
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
    };


    @Override
    public void onRefresh() {
    }
}

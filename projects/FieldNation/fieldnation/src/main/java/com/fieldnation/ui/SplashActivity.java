package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by michael.carver on 12/18/2014.
 */
public class SplashActivity extends AuthFragmentActivity {
    private static final String TAG = "SplashActivity";

    private static final String STATE_PROFILE = "STATE_PROFILE";
    private static final String STATE_IS_AUTH = "STATE_IS_AUTH";
    private static final String STATE_SHOWING_DIALOG = "STATE_SHOWING_DIALOG";

    private Profile _profile = null;
    private boolean _isAuth = false;
    private boolean _calledMyWork = false;
    private GlobalTopicClient _globalClient;
    private AuthTopicClient _authClient;

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
        startService(new Intent(this, AuthTopicService.class));
        _globalClient = new GlobalTopicClient(_globalTopic_listener);
        _globalClient.connect(this);
        _authClient = new AuthTopicClient(_authTopic_listener);
        _authClient.connect(this);

        AuthTopicClient.dispatchRequestCommand(this);
    }

    @Override
    protected void onPause() {
        _globalClient.disconnect(this);
        _authClient.disconnect(this);
        super.onPause();
    }


    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_globalTopic_listener.onConnected");
            _globalClient.registerGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            Log.v(TAG, "_globalTopic_listener.onGotProfile");
            if (profile != null)
                Log.v(TAG, profile.toJson().display());
            _profile = profile;
            doNextStep();
        }
    };

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authClient.registerAuthState();
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            _isAuth = true;
            doNextStep();
        }

        @Override
        public void onNotAuthenticated() {
            AuthTopicClient.dispatchRequestCommand(SplashActivity.this);
        }
    };

    private void doNextStep() {
        if (!_isAuth)
            return;

        if (_profile == null)
            return;

        Log.v(TAG, "doNextStep");

        _profile.isProvider();
//        if (_profile.isProvider()) {
        if (!_calledMyWork) {
            _calledMyWork = true;
            MyWorkActivity.startNew(this);
            finish();
        }
//        }

    }

    public static void startNew(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

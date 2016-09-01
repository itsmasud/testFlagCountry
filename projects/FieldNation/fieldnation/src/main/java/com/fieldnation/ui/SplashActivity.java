package com.fieldnation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.ui.nav.NavActivity;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.fntools.MemUtils;

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

        final ImageView fnLogo = (ImageView) findViewById(R.id.logo_imageview);
            final int reqHeight = fnLogo.getLayoutParams().height;
            fnLogo.setImageBitmap(MemUtils.getMemoryEfficientBitmap(this, R.drawable.fn_logo, reqHeight));
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
        _calledMyWork = false;
        startService(new Intent(this, AuthTopicService.class));

        _globalClient = new GlobalTopicClient(_globalTopic_listener);
        _globalClient.connect(App.get());

        _authClient = new AuthTopicClient(_authTopic_listener);
        _authClient.connect(App.get());

        AuthTopicClient.requestCommand(App.get());
    }

    @Override
    protected void onStop() {
        try {
            if (_globalClient != null && _globalClient.isConnected())
                _globalClient.disconnect(App.get());
            if (_authClient != null && _authClient.isConnected())
                _authClient.disconnect(App.get());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        super.onStop();
    }

    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_globalTopic_listener.onConnected");
            _globalClient.subGotProfile();
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
            _authClient.subAuthStateChange();
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "onAuthenticated");
            _isAuth = true;
            doNextStep();
        }

        @Override
        public void onNotAuthenticated() {
            Log.v(TAG, "onNotAuthenticated");
            AuthTopicClient.requestCommand(SplashActivity.this);
        }
    };

    private void doNextStep() {
        Log.v(TAG, "doNextStep 1");
        if (!_isAuth)
            return;

        Log.v(TAG, "doNextStep 2");
        if (_profile == null)
            return;

        Log.v(TAG, "doNextStep 3");

        if (_profile.isProvider()) {
            Log.v(TAG, "doNextStep 4");
            if (!_calledMyWork) {
                Log.v(TAG, "doNextStep 5");
                _calledMyWork = true;
                NavActivity.startNew(this);
                finish();
            }
        }
    }

    public static void startNew(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

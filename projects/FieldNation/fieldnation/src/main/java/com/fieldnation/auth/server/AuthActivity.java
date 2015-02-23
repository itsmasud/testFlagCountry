package com.fieldnation.auth.server;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.AccountAuthenticatorSupportFragmentActivity;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.rpc.client.AuthService;
import com.fieldnation.rpc.server.ClockService;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.TopicShutdownReciever;
import com.fieldnation.topics.Topics;
import com.fieldnation.ui.SplashActivity;
import com.fieldnation.ui.dialog.UpdateDialog;

/**
 * Provides an authentication UI for the field nation user. This will be called
 * by {@link Authenticator} via requets made to the {@link AccountManager}. It
 * does not need to be called explicitly by the application.
 *
 * @author michael.carver
 */
public class AuthActivity extends AccountAuthenticatorSupportFragmentActivity {
    private static final String TAG = "auth.server.AuthActivity";
    // UI
    private LinearLayout _contentLayout;
    private EditText _usernameEditText;
    private EditText _passwordEditText;
    private View _fader;
    private ImageView _background;
    private Button _signupButton;
    private Button _loginButton;
    private UpdateDialog _updateDialog;

    // data
    private String _username;
    private String _password;
    private boolean _authcomplete = false;

    // animation
    private Animation _fadeout;

    // Services
    private TopicShutdownReciever _shutdownService;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFormat(PixelFormat.UNKNOWN);

        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        _contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        _contentLayout.setVisibility(View.GONE);
        _loginButton = (Button) findViewById(R.id.login_button);
        _loginButton.setOnClickListener(_loginButton_onClick);
        _usernameEditText = (EditText) findViewById(R.id.username_edittext);
        _passwordEditText = (EditText) findViewById(R.id.password_edittext);
        _signupButton = (Button) findViewById(R.id.signup_button);
        _signupButton.setOnClickListener(_signup_onClick);
        _signupButton.setVisibility(View.GONE);

        _background = (ImageView) findViewById(R.id.background_image);
        _fader = findViewById(R.id.fader);

        _fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        _fadeout.setAnimationListener(_fadeout_listener);

        _updateDialog = UpdateDialog.getInstance(getSupportFragmentManager(), TAG);

        _authcomplete = false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _contentLayout.setVisibility(View.VISIBLE);
                _signupButton.setVisibility(View.VISIBLE);
                _fader.startAnimation(_fadeout);
            }
        }, 1000);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        _shutdownService = new TopicShutdownReciever(this, new Handler(), TAG);
        TopicService.registerListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE, _topicReceiver);
        AuthTopicService.dispatchGettingUsernameAndPassword(this);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
        TopicService.unRegisterListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE);
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        Topics.dispatchShutdown(this);
        if (!_authcomplete) {
            AuthTopicService.dispatchAuthCancelled(this);
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        _shutdownService.onPause();
        if (!_authcomplete) {
            //AuthTopicService.dispatchAuthCancelled(this);
        }
        super.onDestroy();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final Animation.AnimationListener _fadeout_listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            _fader.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (Topics.TOPIC_NEED_UPDATE.equals(topicId)) {
                _updateDialog.show();
            }
        }
    };

    private final View.OnClickListener _signup_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.fieldnation.com/signup-provider"));
            startActivity(intent);
        }
    };

    private final View.OnClickListener _loginButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _username = _usernameEditText.getText().toString();
            _password = _passwordEditText.getText().toString();

            String hostname = AuthActivity.this.getString(R.string.web_fn_hostname);
            String grantType = AuthActivity.this.getString(R.string.auth_fn_grant_type);
            String clientId = AuthActivity.this.getString(R.string.auth_fn_client_id);
            String clientSecret = AuthActivity.this.getString(R.string.auth_fn_client_secret);

            AuthService authserve = new AuthService(AuthActivity.this);
            Intent intent = authserve.authenticateWeb(_rpcReceiver, 1, hostname, grantType, clientId, clientSecret,
                    _username, _password);

            startService(intent);

            _contentLayout.setVisibility(View.GONE);
            _signupButton.setVisibility(View.GONE);
        }
    };

    private final ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            Log.v(TAG, "onReceiveResult");
            try {
                String authToken = resultData.getString(AccountManager.KEY_AUTHTOKEN);
                String error = resultData.getString("error");

                if (getString(R.string.login_error_update_app).equals(error)) {
                    Topics.dispatchNeedUpdate(AuthActivity.this);
                } else if (authToken != null) {
                    Log.v(TAG, "have authtoken");
                    Account account = new Account(_username, getString(R.string.auth_account_type));
                    AccountManager am = AccountManager.get(AuthActivity.this);
                    am.addAccountExplicitly(account, _password, null);
                    am.setAuthToken(account, getString(R.string.auth_account_type), authToken);

                    Intent intent = new Intent();
                    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
                    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.auth_account_type));
                    // intent.putExtra(AccountManager.KEY_AUTHTOKEN,
                    // Constants.FIELD_NATION_ACCOUNT_TYPE);
                    intent.putExtra(AccountManager.KEY_AUTHTOKEN, resultData.getString(AccountManager.KEY_AUTHTOKEN));

                    AuthTopicService.dispatchAuthComplete(AuthActivity.this);
                    _authcomplete = true;

                    AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());
                    AuthActivity.this.setResult(RESULT_OK, intent);
                    AuthActivity.this.finish();

                    SplashActivity.startNew(AuthActivity.this);

                    ClockService.enableClock(AuthActivity.this);
                } else {
                    _contentLayout.setVisibility(View.VISIBLE);
                    _signupButton.setVisibility(View.VISIBLE);
                }

                if (!error.equals(getString(R.string.login_error_no_error))) {
                    Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(AuthActivity.this, R.string.toast_could_not_connect,
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

}

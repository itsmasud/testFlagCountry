package com.fieldnation.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.AccountAuthenticatorSupportFragmentActivity;
import com.fieldnation.AsyncTaskEx;
import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.data.profile.ProfileService;
import com.fieldnation.service.transaction.WebTransactionService;
import com.fieldnation.ui.dialog.UpdateDialog;

/**
 * Provides an authentication UI for the field nation user. This will be called
 * by {@link com.fieldnation.service.auth.Authenticator} via requets made to the {@link AccountManager}. It
 * does not need to be called explicitly by the application.
 *
 * @author michael.carver
 */
public class AuthActivity extends AccountAuthenticatorSupportFragmentActivity {
    private static final String TAG = "AuthActivity";
    // UI
    private LinearLayout _contentLayout;
    private EditText _usernameEditText;
    private EditText _passwordEditText;
    private View _fader;
    private Button _signupButton;
    private Button _loginButton;
    private Button _forgotButton;
    private UpdateDialog _updateDialog;
    private View _stiltView;
    private TextView _versionTextView;

    // data
    private String _username;
    private String _password;
    private boolean _authcomplete = false;

    // animation
    private Animation _fadeout;

    // Services
    private GlobalTopicClient _globalClient;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        setContentView(R.layout.activity_login);

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        _contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        _contentLayout.setVisibility(View.GONE);

        _usernameEditText = (EditText) findViewById(R.id.username_edittext);
        _passwordEditText = (EditText) findViewById(R.id.password_edittext);

        _loginButton = (Button) findViewById(R.id.login_button);
        _loginButton.setOnClickListener(_loginButton_onClick);

        _forgotButton = (Button) findViewById(R.id.forgot_button);
        _forgotButton.setOnClickListener(_forgot_onClick);

        _signupButton = (Button) findViewById(R.id.signup_button);
        _signupButton.setOnClickListener(_signup_onClick);
        _signupButton.setVisibility(View.GONE);

        _stiltView = findViewById(R.id.stilt_view);
        _stiltView.setVisibility(View.GONE);

        _fader = findViewById(R.id.fader);

        _versionTextView = (TextView) findViewById(R.id.version_textview);

        _versionTextView.setText("Version " + BuildConfig.VERSION_NAME);

        _fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        _fadeout.setAnimationListener(_fadeout_listener);

        _updateDialog = UpdateDialog.getInstance(getSupportFragmentManager(), TAG);

        _authcomplete = false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _stiltView.setVisibility(View.VISIBLE);
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
        _globalClient = new GlobalTopicClient(_globalClient_listener);
        _globalClient.connect(this);
//        _shutdownService = new TopicShutdownReciever(this, new Handler(), TAG);
//        TopicService.registerListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE, _topicReceiver);
//        AuthTopicService.dispatchGettingUsernameAndPassword(this);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        _globalClient.disconnect(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        GlobalTopicClient.dispatchAppShutdown(this);
        super.onBackPressed();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final GlobalTopicClient.Listener _globalClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.registerUpdateApp();
            _globalClient.registerGotProfile();
        }

        @Override
        public void onNeedAppUpdate() {
            _updateDialog.show();
        }
    };

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
            startService(new Intent(AuthActivity.this, ProfileService.class));
            startService(new Intent(AuthActivity.this, WebTransactionService.class));

            _username = _usernameEditText.getText().toString();
            _password = _passwordEditText.getText().toString();

            new AsyncTaskEx<Object, Object, OAuth>() {
                @Override
                protected OAuth doInBackground(Object... params) {
                    Context context = (Context) params[0];
                    String hostname = AuthActivity.this.getString(R.string.web_fn_hostname);
                    String grantType = AuthActivity.this.getString(R.string.auth_fn_grant_type);
                    String clientId = AuthActivity.this.getString(R.string.auth_fn_client_id);
                    String clientSecret = AuthActivity.this.getString(R.string.auth_fn_client_secret);
                    String username = (String) params[1];
                    String password = (String) params[2];

                    try {
                        OAuth auth = OAuth.authenticate(context, hostname,
                                "/authentication/api/oauth/token", grantType, clientId, clientSecret,
                                username, password);

                        return auth;
                    } catch (Exception ex) {
                        // TODO, when we get here, app hangs at login screen. Need to do something
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(OAuth auth) {
                    if (auth == null) {
                        Toast.makeText(AuthActivity.this, R.string.toast_could_not_connect, Toast.LENGTH_LONG).show();
                        GlobalTopicClient.dispatchNetworkDisconnected(AuthActivity.this);
                        _contentLayout.setVisibility(View.VISIBLE);
                        _signupButton.setVisibility(View.VISIBLE);
                        _stiltView.setVisibility(View.VISIBLE);
                        return;
                    }
                    String authToken = auth.getAccessToken();
                    String error = auth.getErrorType();

                    if ("invalid_client".equals(error)) {
                        GlobalTopicClient.dispatchUpdateApp(AuthActivity.this);
                    } else if (authToken != null) {
                        Log.v(TAG, "have authtoken");
                        Account account = new Account(_username, getString(R.string.auth_account_type));
                        AccountManager am = AccountManager.get(AuthActivity.this);
                        am.addAccountExplicitly(account, _password, null);
                        am.setAuthToken(account, getString(R.string.auth_account_type), authToken);

                        Intent intent = new Intent();
                        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
                        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.auth_account_type));
                        intent.putExtra(AccountManager.KEY_AUTHTOKEN, auth.getAccessToken());

                        _authcomplete = true;

                        AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());
                        AuthActivity.this.setResult(RESULT_OK, intent);
                        AuthActivity.this.finish();

                        AuthTopicClient.dispatchAddedAccountCommand(AuthActivity.this);

                        SplashActivity.startNew(AuthActivity.this);
                    } else {
                        _contentLayout.setVisibility(View.VISIBLE);
                        _signupButton.setVisibility(View.VISIBLE);
                        _stiltView.setVisibility(View.VISIBLE);
                    }
                    if (error != null && !getString(R.string.login_error_no_error).equals(error)) {
                        Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                }
            }.executeEx(AuthActivity.this, _username, _password);

            _contentLayout.setVisibility(View.GONE);
            _signupButton.setVisibility(View.GONE);
            _stiltView.setVisibility(View.GONE);
        }
    };

    private final View.OnClickListener _forgot_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/forgot_password.php"));
            startActivity(intent);
        }
    };
}


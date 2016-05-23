package com.fieldnation.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.AccountAuthenticatorSupportFragmentActivity;
import com.fieldnation.App;
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
import com.fieldnation.utils.misc;

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
        _usernameEditText.setOnEditorActionListener(_onEditorUserName);

        _passwordEditText = (EditText) findViewById(R.id.password_edittext);
        _passwordEditText.setOnEditorActionListener(_onEditorPassword);


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

        _versionTextView.setText("Version " +
                (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());

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
        _globalClient.connect(App.get());
//        _shutdownService = new TopicShutdownReciever(this, new Handler(), TAG);
//        TopicService.registerListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE, _topicReceiver);
//        AuthTopicService.dispatchGettingUsernameAndPassword(this);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        _globalClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        GlobalTopicClient.appShutdown(this);
        super.onBackPressed();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final GlobalTopicClient.Listener _globalClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.subUpdateApp();
            _globalClient.subGotProfile();
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
            misc.hideKeyboard(v);
            startService(new Intent(AuthActivity.this, ProfileService.class));
            startService(new Intent(AuthActivity.this, WebTransactionService.class));

            _username = _usernameEditText.getText().toString();
            _password = _passwordEditText.getText().toString();

            if (misc.isEmptyOrNull(_username) || misc.isEmptyOrNull(_password)) {
                Toast.makeText(AuthActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                return;
            }

            new AsyncTaskEx<Object, Object, OAuth>() {
                @Override
                protected OAuth doInBackground(Object... params) {
                    String hostname = AuthActivity.this.getString(R.string.web_fn_hostname);
                    String grantType = AuthActivity.this.getString(R.string.auth_fn_grant_type);
                    String clientId = AuthActivity.this.getString(R.string.auth_fn_client_id);
                    String clientSecret = AuthActivity.this.getString(R.string.auth_fn_client_secret);
                    String username = (String) params[0];
                    String password = (String) params[1];

                    try {
                        OAuth auth = OAuth.authenticate(hostname, "/authentication/api/oauth/token",
                                grantType, clientId, clientSecret, username, password);

                        GlobalTopicClient.networkConnected(AuthActivity.this);
                        return auth;
                    } catch (Exception ex) {
                        // TODO, when we get here, app hangs at login screen. Need to do something
                        Log.v(TAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(OAuth auth) {
                    if (auth == null) {
                        Toast.makeText(AuthActivity.this, R.string.toast_could_not_connect, Toast.LENGTH_LONG).show();
                        GlobalTopicClient.networkDisconnected(AuthActivity.this);
                        _contentLayout.setVisibility(View.VISIBLE);
                        _signupButton.setVisibility(View.VISIBLE);
                        _stiltView.setVisibility(View.VISIBLE);
                        return;
                    }
                    String authToken = auth.getAccessToken();
                    String error = auth.getErrorType();

                    if ("invalid_client".equals(error)) {
                        GlobalTopicClient.updateApp(AuthActivity.this);
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

                        AuthTopicClient.addedAccountCommand(AuthActivity.this);

                        SplashActivity.startNew(AuthActivity.this);
                    } else {
                        _contentLayout.setVisibility(View.VISIBLE);
                        _signupButton.setVisibility(View.VISIBLE);
                        _stiltView.setVisibility(View.VISIBLE);
                    }
                    if (error != null && !getString(R.string.login_error_no_error).equals(error)) {
                        Toast.makeText(AuthActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    }
                }
            }.executeEx(_username, _password);

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

    private final TextView.OnEditorActionListener _onEditorUserName = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                _passwordEditText.requestFocus();
                handled = true;
            }
            return handled;
        }
    };

    private final TextView.OnEditorActionListener _onEditorPassword = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                _loginButton_onClick.onClick(null);
                handled = true;
            }
            return handled;
        }
    };
}


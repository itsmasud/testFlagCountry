package com.fieldnation.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.UserManager;
import android.support.annotation.NonNull;
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
import com.fieldnation.AppMessagingClient;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnactivityresult.ActivityRequestHandler;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fnpermissions.PermissionsRequestHandler;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.DefaultAnimationListener;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.auth.AuthTopicConstants;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.v2.ui.dialog.UpdateDialog;

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
    private View _stiltView;
    private TextView _versionTextView;
    private DialogManager _dialogManager;

    // data
    private String _username;
    private String _password;
    private boolean _authcomplete = false;

    // animation
    private Animation _fadeout;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        PigeonRoost.clearAddressCache(AuthTopicConstants.ADDRESS_AUTH_COMMAND_NEED_PASSWORD);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        setContentView(R.layout.activity_login);

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        _contentLayout = findViewById(R.id.content_layout);
        _contentLayout.setVisibility(View.GONE);

        _usernameEditText = findViewById(R.id.username_edittext);
        _usernameEditText.setOnEditorActionListener(_onEditorUserName);

        _passwordEditText = findViewById(R.id.password_edittext);
        _passwordEditText.setOnEditorActionListener(_onEditorPassword);

        _loginButton = findViewById(R.id.login_button);
        _loginButton.setOnClickListener(_loginButton_onClick);

        _forgotButton = findViewById(R.id.forgot_button);
        _forgotButton.setOnClickListener(_forgot_onClick);

        _signupButton = findViewById(R.id.signup_button);
        _signupButton.setOnClickListener(_signup_onClick);
        _signupButton.setVisibility(View.GONE);

        _stiltView = findViewById(R.id.stilt_view);
        _stiltView.setVisibility(View.GONE);

        _fader = findViewById(R.id.fader);

        _versionTextView = findViewById(R.id.version_textview);
        _versionTextView.setText("Version " +
                (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());

        _fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        _fadeout.setAnimationListener(_fadeout_listener);

        _dialogManager = findViewById(R.id.dialogManager);

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
    protected void onStart() {
        super.onStart();

        _dialogManager.onStart();

        _permissionsListener.sub();
        PermissionsClient.checkSelfPermissionAndRequest(this, App.getPermissions(), App.getPermissionsRequired());
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        _appMessagingClient.subUpdateApp();
        _activityRequestHandler.sub();

        _dialogManager.onResume();

        App.logout();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");

        _appMessagingClient.unsubUpdateApp();
        _activityRequestHandler.unsub();
        _dialogManager.onPause();

        super.onPause();
    }

    @Override
    protected void onStop() {
        _permissionsListener.unsub();

        super.onStop();

        _dialogManager.onStop();
    }

    @Override
    public void finish() {
        Log.v(TAG, "finish");
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");

        if (_dialogManager.onBackPressed()) {
            return;
        }

        AppMessagingClient.appShutdown();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityRequestHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.v(TAG, "onRequestPermissionsResult");
        PermissionsClient.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final AppMessagingClient _appMessagingClient = new AppMessagingClient() {
        @Override
        public void onNeedAppUpdate() {
            UpdateDialog.show(App.get());
        }
    };

    private final Animation.AnimationListener _fadeout_listener = new DefaultAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            _fader.setVisibility(View.GONE);
        }
    };

    private final PermissionsRequestHandler _permissionsListener = new PermissionsRequestHandler() {
        @Override
        public Activity getActivity() {
            return AuthActivity.this;
        }
    };

    private final ActivityRequestHandler _activityRequestHandler = new ActivityRequestHandler() {
        @Override
        public Activity getActivity() {
            return AuthActivity.this;
        }
    };

    private final View.OnClickListener _signup_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.fieldnation.com/signup-provider"));

            if (intent.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(AuthActivity.this, "Could not open web browser", Toast.LENGTH_LONG).show();
                return;
            }

            startActivity(intent);
        }
    };

    private final View.OnClickListener _loginButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "Login Button");
            misc.hideKeyboard(getCurrentFocus());

            try {
                Tracker.event(App.get(),
                        new SimpleEvent.Builder()
                                .tag(AnswersWrapper.TAG)
                                .category("isUserAMonkey")
                                .action(ActivityManager.isUserAMonkey() ? "true" : "false")
                                .build());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    UserManager um = (UserManager) getSystemService(Context.USER_SERVICE);
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("isUserAGoat")
                                    .action(um.isUserAGoat() ? "true" : "false")
                                    .build());
                } else {
                    Tracker.event(App.get(),
                            new SimpleEvent.Builder()
                                    .tag(AnswersWrapper.TAG)
                                    .category("isUserAGoat")
                                    .action("false")
                                    .build());
                }
            } catch (Exception ex) {
            }

            _username = _usernameEditText.getText().toString();
            _password = _passwordEditText.getText().toString();

            if (misc.isEmptyOrNull(_username) || misc.isEmptyOrNull(_password)) {
                Toast.makeText(AuthActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                return;
            }

            new LoginAsync(_loginListener).executeEx(_username, _password, App.get());

            _contentLayout.setVisibility(View.GONE);
            _signupButton.setVisibility(View.GONE);
            _stiltView.setVisibility(View.GONE);
        }
    };

    private static class LoginAsync extends AsyncTaskEx<Object, Object, OAuth> {
        private Listener _loginListener;

        LoginAsync(Listener listener) {
            super();
            _loginListener = listener;
        }

        @Override
        protected OAuth doInBackground(Object... params) {
            String username = (String) params[0];
            String password = (String) params[1];
            Context context = (Context) params[2];
            String hostname = context.getString(R.string.web_fn_hostname);
            String grantType = context.getString(R.string.auth_fn_grant_type);
            String clientId = context.getString(R.string.auth_fn_client_id);
            String clientSecret = context.getString(R.string.auth_fn_client_secret);

            try {
                OAuth auth = OAuth.authenticate(hostname, "/authentication/api/oauth/token",
                        grantType, clientId, clientSecret, username, password);
                return auth;
            } catch (Exception ex) {
                // TODO, when we get here, app hangs at login screen. Need to do something
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(OAuth auth) {
            _loginListener.onComplete(auth);
        }

        public interface Listener {
            void onComplete(OAuth oauth);
        }
    }

    private final LoginAsync.Listener _loginListener = new LoginAsync.Listener() {
        @Override
        public void onComplete(OAuth oauth) {
            if (oauth == null) {
                Toast.makeText(AuthActivity.this, R.string.toast_could_not_connect, Toast.LENGTH_LONG).show();
                AppMessagingClient.networkDisconnected();
                _contentLayout.setVisibility(View.VISIBLE);
                _signupButton.setVisibility(View.VISIBLE);
                _stiltView.setVisibility(View.VISIBLE);
                return;
            }
            String authToken = oauth.getAccessToken();
            String error = oauth.getErrorType();

            if ("invalid_client".equals(error)) {
                AppMessagingClient.updateApp();
            } else if (authToken != null) {
                Log.v(TAG, "have authtoken");
                Account account = new Account(_username, getString(R.string.auth_account_type));
                AccountManager am = AccountManager.get(AuthActivity.this);
                am.addAccountExplicitly(account, _password, null);
                am.setAuthToken(account, getString(R.string.auth_account_type), authToken);
                AuthClient.addedAccountCommand();

                Intent intent = new Intent();
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.auth_account_type));
                intent.putExtra(AccountManager.KEY_AUTHTOKEN, oauth.getAccessToken());

                _authcomplete = true;

                AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());
                AuthActivity.this.setResult(RESULT_OK, intent);
                AuthActivity.this.finish();

                //SplashActivity.startNew(AuthActivity.this);
            } else {
                _contentLayout.setVisibility(View.VISIBLE);
                _signupButton.setVisibility(View.VISIBLE);
                _stiltView.setVisibility(View.VISIBLE);
            }
            if (error != null && !getString(R.string.login_error_no_error).equals(error)) {
                Toast.makeText(AuthActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
            }
        }
    };

    private final View.OnClickListener _forgot_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://app.fieldnation.com/forgot_password.php"));

            if (intent.resolveActivity(getPackageManager()) == null) {
                Toast.makeText(AuthActivity.this, "Could not open web browser", Toast.LENGTH_LONG).show();
                return;
            }

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

    public static void startNewWithResponse(Context context, Parcelable authenticatorResponse) {
        Intent intent = new Intent(context, AuthActivity.class);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, authenticatorResponse);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                /*| Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK*/);

        ActivityClient.startActivity(intent, R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public static Intent startNewWithResponseIntent(Context context, Parcelable authenticatorResponse) {
        Intent intent = new Intent(context, AuthActivity.class);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, authenticatorResponse);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                /*| Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK*/);

        return intent;
    }
}

package com.fieldnation.auth.server;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.rpc.client.AuthService;
import com.fieldnation.rpc.server.ClockService;
import com.fieldnation.topics.TopicShutdownReciever;
import com.fieldnation.topics.Topics;

/**
 * Provides an authentication UI for the field nation user. This will be called
 * by {@link Authenticator} via requets made to the {@link AccountManager}. It
 * does not need to be called explicitly by the application.
 *
 * @author michael.carver
 */
public class AuthActivity extends AccountAuthenticatorActivity {
    private static final String TAG = "auth.server.AuthActivity";
    // UI
    private LinearLayout _contentLayout;
    private Button _loginButton;
    private EditText _usernameEditText;
    private EditText _passwordEditText;
    private View _fader;

    private VideoView _videoView;


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
        _videoView = (VideoView) findViewById(R.id.video_view);
        _fader = (View) findViewById(R.id.fader);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.login_vid);
        _videoView.setVideoURI(video);
        _videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        _videoView.start();

        _fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        _fadeout.setAnimationListener(_fadeout_listener);

        _authcomplete = false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _contentLayout.setVisibility(View.VISIBLE);
                _fader.startAnimation(_fadeout);
            }
        }, 1000);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    protected void onResume() {
        super.onResume();
        _shutdownService = new TopicShutdownReciever(this, new Handler(), TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _videoView.stopPlayback();
        _shutdownService.onPause();
    }

    @Override
    public void onBackPressed() {
        Topics.dispatchShutdown(this);
        return;
    }

    @Override
    protected void onDestroy() {
        if (!_authcomplete) {
            AuthTopicService.dispatchAuthCancelled(this);
        }
        super.onDestroy();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private Animation.AnimationListener _fadeout_listener = new Animation.AnimationListener() {
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

    private View.OnClickListener _loginButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _username = _usernameEditText.getText().toString();
            _password = _passwordEditText.getText().toString();

            String hostname = AuthActivity.this.getString(R.string.fn_hostname);
            String grantType = AuthActivity.this.getString(R.string.fn_grant_type);
            String clientId = AuthActivity.this.getString(R.string.fn_client_id);
            String clientSecret = AuthActivity.this.getString(R.string.fn_client_secret);

            AuthService authserve = new AuthService(AuthActivity.this);
            Intent intent = authserve.authenticateWeb(_rpcReceiver, 1, hostname, grantType, clientId, clientSecret,
                    _username, _password);

            startService(intent);

            _contentLayout.setVisibility(View.GONE);
        }
    };

    private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            try {
                String authToken = resultData.getString("authtoken");
                String error = resultData.getString("error");

                if (authToken != null) {
                    Account account = new Account(_username, getString(R.string.accounttype));
                    AccountManager am = AccountManager.get(AuthActivity.this);
                    am.addAccountExplicitly(account, _password, null);
                    am.setAuthToken(account, getString(R.string.accounttype), authToken);

                    Intent intent = new Intent();
                    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
                    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, getString(R.string.accounttype));
                    // intent.putExtra(AccountManager.KEY_AUTHTOKEN,
                    // Constants.FIELD_NATION_ACCOUNT_TYPE);
                    intent.putExtra(AccountManager.KEY_AUTHTOKEN, resultData.getString("authtoken"));

                    AuthTopicService.dispatchAuthComplete(AuthActivity.this);
                    _authcomplete = true;

                    AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());
                    AuthActivity.this.setResult(RESULT_OK, intent);
                    AuthActivity.this.finish();

                    ClockService.enableClock(AuthActivity.this);
                } else {
                    _contentLayout.setVisibility(View.VISIBLE);
                }

                Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(AuthActivity.this, "An unexpected error occurred. Could not connect to account.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

}

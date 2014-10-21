package com.fieldnation.auth.server;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.rpc.client.AuthService;
import com.fieldnation.rpc.server.ClockService;

/**
 * Provides an authentication UI for the field nation user. This will be called
 * by {@link Authenticator} via requets made to the {@link AccountManager}. It
 * does not need to be called explicitly by the application.
 *
 * @author michael.carver
 */
public class AuthActivity extends AccountAuthenticatorActivity {
    // UI
    private RelativeLayout _contentLayout;
    private ProgressBar _loadingProgressBar;
    private Button _loginButton;
    private EditText _usernameEditText;
    private EditText _passwordEditText;

    // data
    private String _username;
    private String _password;
    private GlobalState _gs;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
	/*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        _gs = (GlobalState) getApplicationContext();

        _contentLayout = (RelativeLayout) findViewById(R.id.content_layout);
        _loginButton = (Button) findViewById(R.id.login_button);
        _loginButton.setOnClickListener(_loginButton_onClick);
        _usernameEditText = (EditText) findViewById(R.id.username_edittext);
        _passwordEditText = (EditText) findViewById(R.id.password_edittext);
        _loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        _loadingProgressBar.setVisibility(View.GONE);
        _contentLayout.setVisibility(View.VISIBLE);

    }

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

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

            _loadingProgressBar.setVisibility(View.VISIBLE);
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
                    Account account = new Account(_username, _gs.accountType);
                    AccountManager am = AccountManager.get(AuthActivity.this);
                    am.addAccountExplicitly(account, _password, null);
                    am.setAuthToken(account, _gs.accountType, authToken);

                    Intent intent = new Intent();
                    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
                    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, _gs.accountType);
                    // intent.putExtra(AccountManager.KEY_AUTHTOKEN,
                    // Constants.FIELD_NATION_ACCOUNT_TYPE);
                    intent.putExtra(AccountManager.KEY_AUTHTOKEN, resultData.getString("authtoken"));

                    AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());
                    AuthActivity.this.setResult(RESULT_OK, intent);
                    AuthActivity.this.finish();

                    ClockService.enableClock(AuthActivity.this);
                } else {
                    _loadingProgressBar.setVisibility(View.GONE);
                    _contentLayout.setVisibility(View.VISIBLE);
                }

                Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(AuthActivity.this, "An unexpected error occurred. Could not connect to account.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        ;
    };

}

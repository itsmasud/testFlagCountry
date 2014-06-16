package com.fieldnation.auth;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.service.rpc.AuthRpc;
import com.fieldnation.service.rpc.ClockRpc;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AuthActivity extends AccountAuthenticatorActivity {
	// UI
	private LinearLayout _contentLayout;
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
		setContentView(R.layout.activity_login);

		_gs = (GlobalState) getApplicationContext();

		_contentLayout = (LinearLayout) findViewById(R.id.content_layout);
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

			Intent intent = AuthRpc.makeIntent(AuthActivity.this, _rpcReceiver,
					1, hostname, grantType, clientId, clientSecret, _username,
					_password);

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
					intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
							_gs.accountType);
					// intent.putExtra(AccountManager.KEY_AUTHTOKEN,
					// Constants.FIELD_NATION_ACCOUNT_TYPE);
					intent.putExtra(AccountManager.KEY_AUTHTOKEN,
							resultData.getString("authtoken"));

					AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());
					AuthActivity.this.setResult(RESULT_OK, intent);
					AuthActivity.this.finish();

					ClockRpc.enableClock(AuthActivity.this);
				} else {
					_loadingProgressBar.setVisibility(View.GONE);
					_contentLayout.setVisibility(View.VISIBLE);
				}
				Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				// TODO handle properly
				e.printStackTrace();
			}
		};
	};

}

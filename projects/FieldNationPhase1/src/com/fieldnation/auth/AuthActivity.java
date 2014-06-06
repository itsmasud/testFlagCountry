package com.fieldnation.auth;

import java.text.ParseException;

import com.fieldnation.Constants;
import com.fieldnation.R;
import com.fieldnation.service.ClockReceiver;
import com.fieldnation.service.rpc.AuthRpc;
import com.fieldnation.webapi.AccessToken;

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

public class AuthActivity extends AccountAuthenticatorActivity {
	private Button _loginButton;
	private EditText _usernameEditText;
	private EditText _passwordEditText;

	private String _username;
	private String _password;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		_loginButton = (Button) findViewById(R.id.login_button);
		_loginButton.setOnClickListener(_loginButton_onClick);
		_usernameEditText = (EditText) findViewById(R.id.username_edittext);
		_passwordEditText = (EditText) findViewById(R.id.password_edittext);
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
			String grantType = AuthActivity.this
					.getString(R.string.fn_grant_type);
			String clientId = AuthActivity.this
					.getString(R.string.fn_client_id);
			String clientSecret = AuthActivity.this
					.getString(R.string.fn_client_secret);

			Intent intent = AuthRpc.makeIntent(AuthActivity.this, _rpcReceiver,
					1, hostname, grantType, clientId, clientSecret, _username,
					_password);

			startService(intent);
		}
	};

	private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			try {
				Account account = new Account(_username,
						Constants.FIELD_NATION_ACCOUNT_TYPE);
				AccountManager am = AccountManager.get(AuthActivity.this);
				am.addAccountExplicitly(account, _password, null);

				Intent intent = new Intent();
				intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
				intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.FIELD_NATION_ACCOUNT_TYPE);
				intent.putExtra(AccountManager.KEY_AUTHTOKEN, resultData.getString("authtoken"));
				AuthActivity.this.setAccountAuthenticatorResult(intent.getExtras());

				// TODO read the clock delay from somewhere
				ClockReceiver.registerClock(AuthActivity.this, 5000);

				AuthActivity.this.setResult(RESULT_OK);
				AuthActivity.this.finish();
			} catch (Exception e) {
				// TODO handle properly
				e.printStackTrace();
			}
		};
	};

}

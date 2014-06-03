package com.fieldnation.auth;

import java.text.ParseException;

import com.fieldnation.AccessTokenAsyncTask;
import com.fieldnation.AccessTokenAsyncTaskListener;
import com.fieldnation.R;
import com.fieldnation.R.id;
import com.fieldnation.R.layout;
import com.fieldnation.webapi.AccessToken;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthActivity extends AccountAuthenticatorActivity {
	private Button _loginButton;
	private EditText _usernameEditText;
	private EditText _passwordEditText;

	private String _username;
	private String _password;

	private AccessTokenAsyncTask _atTask;

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

			// TODO display wait dialog
			_atTask = new AccessTokenAsyncTask(_accessTokenListener);
			_atTask.execute(AuthActivity.this, _username, _password);

		}
	};

	private AccessTokenAsyncTaskListener _accessTokenListener = new AccessTokenAsyncTaskListener() {
		@Override
		public void onFail(Exception e) {
			e.printStackTrace();
		}

		@Override
		public void onComplete(AccessToken token) {
			// TODO close wait dialog
			try {

				Account account = new Account(_username, "fieldnation_provider");
				AccountManager am = AccountManager.get(AuthActivity.this);
				am.addAccountExplicitly(account, _password, null);

				Intent intent = new Intent();
				intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, _username);
				intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
						"fieldnation_provider");
				intent.putExtra(AccountManager.KEY_AUTHTOKEN, token.toJson()
						.toString());
				AuthActivity.this.setAccountAuthenticatorResult(intent
						.getExtras());
				AuthActivity.this.setResult(RESULT_OK);
				AuthActivity.this.finish();
			} catch (ParseException e) {
				// TODO handle properly
				e.printStackTrace();
			}
		}
	};
}

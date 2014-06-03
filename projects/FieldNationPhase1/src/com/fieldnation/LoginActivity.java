package com.fieldnation;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AccountAuthenticatorActivity {
	private Button _loginButton;
	private EditText _usernameEditText;
	private EditText _passwordEditText;

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
			String username = _usernameEditText.getText().toString();
			String password = _passwordEditText.getText().toString();

			Account account = new Account(username, "fieldnation_provider");
			AccountManager am = AccountManager.get(LoginActivity.this);
			am.addAccountExplicitly(account, password, null);
		}
	};
}

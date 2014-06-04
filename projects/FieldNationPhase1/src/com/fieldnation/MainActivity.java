package com.fieldnation;

import com.fieldnation.auth.AuthWaitAsyncTask;
import com.fieldnation.auth.AuthWaitAsyncTaskListener;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	private ActionBarDrawerToggle _drawerToggle;
	private AuthWaitAsyncTask _authWaitAsyncTask;
	private DrawerLayout _drawerLayout;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		_drawerLayout = (DrawerLayout) findViewById(R.id.container);
		_drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
				R.drawable.ic_action_expand, R.string.launcher_open,
				R.string.launcher_open) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(
						getResources().getString(R.string.app_name));
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(
						getResources().getString(R.string.launcher_open));
			}
		};

		_drawerLayout.setDrawerListener(_drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		_authWaitAsyncTask = new AuthWaitAsyncTask(_authWaitAsyncTaskListener);

		AccountManager am = AccountManager.get(this);
		Account[] accounts = am
				.getAccountsByType(Constants.FIELD_NATION_ACCOUNT_TYPE);
		if (accounts.length == 0) {
			am.addAccount(Constants.FIELD_NATION_ACCOUNT_TYPE,
					Constants.FIELD_NATION_ACCOUNT_TYPE, null, new Bundle(),
					this, amc, null);
		} else {

			am.getAuthToken(accounts[0], Constants.FIELD_NATION_ACCOUNT_TYPE,
					new Bundle(), this, amc, null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		_drawerToggle.syncState();
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AuthWaitAsyncTaskListener _authWaitAsyncTaskListener = new AuthWaitAsyncTaskListener() {
		@Override
		public void onFail(Exception ex) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onFail()");
		}

		@Override
		public void onComplete(Bundle bundle) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onComplete()");
		}
	};

	private AccountManagerCallback<Bundle> amc = new AccountManagerCallback<Bundle>() {
		@Override
		public void run(AccountManagerFuture<Bundle> future) {
			_authWaitAsyncTask.execute(future);
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		if (_drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		_drawerToggle.onConfigurationChanged(newConfig);
	}

}

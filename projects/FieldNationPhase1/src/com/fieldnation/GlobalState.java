package com.fieldnation;

import com.fieldnation.service.ClockReceiver;
import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Ws;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class GlobalState extends Application {
	private static final String TAG = "GlobalState";

	public AccessToken accessToken = null;
	public String accountType;
	public String authority;

	public GlobalState() {
		super();
		Ws.USE_HTTPS = true;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		accountType = getString(R.string.accounttype);
		authority = getString(R.string.authority);

		Log.v(TAG, "Method Stub: onCreate()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Method Stub: onConfigurationChanged()
		Log.v(TAG, "Method Stub: onConfigurationChanged()");
		super.onConfigurationChanged(newConfig);
	}
}

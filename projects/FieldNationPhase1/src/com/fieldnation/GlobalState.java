package com.fieldnation;

import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Ws;

import android.app.Application;

/**
 * Defines some global values that will be shared between all objects.
 * 
 * @author michael.carver
 * 
 */
public class GlobalState extends Application {
	private static final String TAG = "GlobalState";

	public String accountType;
	public String authority;
	public String username = null;
	public String accessToken = null;

	public GlobalState() {
		super();
		Ws.USE_HTTPS = true;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		accountType = getString(R.string.accounttype);
		authority = getString(R.string.authority);
	}
}

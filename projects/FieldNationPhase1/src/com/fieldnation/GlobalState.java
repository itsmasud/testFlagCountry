package com.fieldnation;

import com.fieldnation.service.ClockReceiver;
import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Ws;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Defines some global values that will be shared between all objects.
 * 
 * @author michael.carver
 * 
 */
public class GlobalState extends Application {
	private static final String TAG = "GlobalState";

	public OAuth oAuth = null;
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
	}
}

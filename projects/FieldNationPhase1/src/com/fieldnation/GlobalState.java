package com.fieldnation;

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

	private AuthenticationServer _authServer = null;

	public static final int USER_ID = 375;

	public String authority;
	public String accountType;

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

	/**
	 * Call this to connect an authentication server. Note, only one server can
	 * be registered at a time.
	 * 
	 * @param server
	 */
	public void setAuthenticationServer(AuthenticationServer server) {
		_authServer = server;
	}

	public void requestAuthentication(AuthenticationClient client) {
		if (_authServer == null) {
			client.waitForObject(this, "_authServer");
		} else {
			_authServer.requestAuthentication(client);
		}
	}

	public void invalidateAuthToken(String token) {
		_authServer.invalidateToken(token);
	}
}

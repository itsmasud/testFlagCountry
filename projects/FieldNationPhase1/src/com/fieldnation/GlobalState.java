package com.fieldnation;

import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.auth.client.AuthenticationServer;
import com.fieldnation.rpc.server.DataCacheNode;
import com.fieldnation.rpc.server.PhotoCacheNode;
import com.fieldnation.rpc.server.Ws;

import android.app.Application;
import android.os.Build;

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
		DataCacheNode.flush(this);
		PhotoCacheNode.flush(this);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepalive", "false");
		}
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

	public void requestAuthenticationDelayed(AuthenticationClient client) {
		if (_authServer == null) {
			client.waitForObject(this, "_authServer");
		} else {
			client.waitForTime(5000);
		}
	}

	public void requestRemoveAccount(AuthenticationClient client) {
		if (_authServer == null) {
			client.waitForObject(this, "_authServer");
		} else {
			_authServer.removeAccount(client);
		}
	}

	public void invalidateAuthToken(String token) {
		_authServer.invalidateToken(token);
	}
}

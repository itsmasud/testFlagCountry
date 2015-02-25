package com.fieldnation.rpc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Called by the OS when a user needs authenticating. This class passes the
 * request to the Authenticator
 * 
 * @author michael.carver
 * 
 */
public class AuthService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		Authenticator auth = new Authenticator(this);
		return auth.getIBinder();
	}

}

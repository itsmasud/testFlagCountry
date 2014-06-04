package com.fieldnation.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		Authenticator auth = new Authenticator(this);
		return auth.getIBinder();
	}

}

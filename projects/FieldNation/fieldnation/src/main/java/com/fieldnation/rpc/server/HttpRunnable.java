package com.fieldnation.rpc.server;

import android.content.Context;
import android.content.Intent;

abstract class HttpRunnable implements Runnable {
	protected Context _context;
	protected Intent _intent;
	protected AuthToken _auth;

	public HttpRunnable(Context context, Intent intent, AuthToken at) {
		_context = context.getApplicationContext();
		_intent = intent;
		_auth = at;
	}

}
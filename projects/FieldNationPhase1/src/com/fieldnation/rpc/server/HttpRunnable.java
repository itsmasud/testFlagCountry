package com.fieldnation.rpc.server;

import android.content.Context;
import android.content.Intent;

abstract class HttpRunnable implements Runnable {
	protected Context _context;
	protected Intent _intent;
	protected OAuth _auth;

	public HttpRunnable(Context context, Intent intent, OAuth at) {
		_context = context;
		_intent = intent;
		_auth = at;
	}

}
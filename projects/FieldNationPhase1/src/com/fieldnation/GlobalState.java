package com.fieldnation;

import com.fieldnation.webapi.Ws;

import android.app.Application;

public class GlobalState extends Application {

	public GlobalState() {
		super();
		Ws.USE_HTTPS = true;
	}
}

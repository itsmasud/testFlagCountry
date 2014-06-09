package com.fieldnation;

import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Ws;

import android.app.Application;

public class GlobalState extends Application {

	public AccessToken accessToken = null;

	public GlobalState() {
		super();
		Ws.USE_HTTPS = true;
	}
}

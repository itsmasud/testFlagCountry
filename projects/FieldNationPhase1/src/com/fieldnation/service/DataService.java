package com.fieldnation.service;

import java.util.HashMap;

import com.fieldnation.service.rpc.AuthRpc;
import com.fieldnation.service.rpc.WebRpc;
import com.fieldnation.service.rpc.RpcInterface;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DataService extends IntentService {
	private static final String TAG = "service.DataService";

	private HashMap<String, RpcInterface> _rpcs = new HashMap<String, RpcInterface>();

	public DataService() {
		this(null);
	}

	public DataService(String name) {
		super(name);

		// fill in the hashmap
		new AuthRpc(_rpcs);
		new WebRpc(_rpcs);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate()");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();

		if ("CLOCK_PULSE".equals(action)) {
			// TODO, handle the clock pulse
			Log.v(TAG, "CLOCK_PULSE");
		} else if ("RPC".equals(action)) {
			_rpcs.get(intent.getStringExtra("SERVICE")).execute(this, intent);
		}
	}

}

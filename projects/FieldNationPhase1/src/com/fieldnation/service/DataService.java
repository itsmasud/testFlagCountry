package com.fieldnation.service;

import java.util.HashMap;

import com.fieldnation.service.rpc.AuthRpc;
import com.fieldnation.service.rpc.WebRpc;
import com.fieldnation.service.rpc.RpcInterface;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class DataService extends IntentService {
	public static final String ACTION_RPC = "RPC";
	public static final String ACTION_CLOCK_PULSE = "CLOCK_PULSE";

	public static final String KEY_SERVICE = "SERVICE";

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

		if (ACTION_CLOCK_PULSE.equals(action)) {
			// TODO, handle the clock pulse
			Log.v(TAG, ACTION_CLOCK_PULSE);
		} else if (ACTION_RPC.equals(action)) {
			_rpcs.get(intent.getStringExtra(KEY_SERVICE)).execute(this, intent);
		}
	}

}

package com.fieldnation.service;

import java.util.HashMap;

import com.fieldnation.service.rpc.AuthRpc;
import com.fieldnation.service.rpc.WorkorderGetRequestedRpc;
import com.fieldnation.service.rpc.RpcInterface;

import android.app.IntentService;
import android.content.Intent;

public class BackgroundService extends IntentService {

	private HashMap<String, RpcInterface> _rpcs = new HashMap<String, RpcInterface>();

	public BackgroundService() {
		this(null);
	}

	public BackgroundService(String name) {
		super(name);

		// fill in the hashmap
		new WorkorderGetRequestedRpc(_rpcs);
		new AuthRpc(_rpcs);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();

		if ("CLOCK_PULSE".equals(action)) {
			// TODO, handle the clock pulse
		} else if ("RPC".equals(action)) {
			_rpcs.get(intent.getStringExtra("METHOD")).execute(this, intent);
		}
	}

}

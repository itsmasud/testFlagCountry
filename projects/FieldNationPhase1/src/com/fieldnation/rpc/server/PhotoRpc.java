package com.fieldnation.rpc.server;

import java.util.HashMap;

import com.fieldnation.rpc.common.PhotoServiceConstants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PhotoRpc extends RpcInterface implements PhotoServiceConstants {
	private static final String TAG = "rpc.server.PhotoRpc";

	PhotoRpc(HashMap<String, RpcInterface> map) {
		super(map, ACTION_NAME);
	}

	@Override
	public void execute(Context context, Intent intent) {
		// TODO Method Stub: execute()
		Log.v(TAG, "Method Stub: execute()");

	}

}

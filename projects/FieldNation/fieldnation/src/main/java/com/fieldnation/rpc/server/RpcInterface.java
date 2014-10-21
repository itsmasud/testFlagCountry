package com.fieldnation.rpc.server;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;

public abstract class RpcInterface {

	RpcInterface(HashMap<String, RpcInterface> map, String name) {
		map.put(name, this);
	}

	public abstract void execute(Context context, Intent intent);

}

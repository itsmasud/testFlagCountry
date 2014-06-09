package com.fieldnation.service.rpc;

import java.util.HashMap;

import com.fieldnation.service.ClockReceiver;
import com.fieldnation.service.DataService;

import android.content.Context;
import android.content.Intent;

public class ClockRpc extends RpcInterface {

	public ClockRpc(HashMap<String, RpcInterface> map) {
		super(map, "clock");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			boolean enable = intent.getBooleanExtra("PARAM_ENABLE", true);

			// TODO, ANDR-9 pull down a duration
			if (enable)
				ClockReceiver.registerClock(context, 5000);
			else
				ClockReceiver.unregisterOneSecondTickAlarm(context);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void enableClock(Context context, boolean enable) {
		Intent intent = new Intent(context, DataService.class);

		intent.setAction("RPC");
		intent.putExtra("METHOD", "clock");
		intent.putExtra("PARAM_ENABLE", enable);

		context.startService(intent);
	}

}

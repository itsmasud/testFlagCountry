package com.fieldnation.service.rpc;

import com.fieldnation.service.ClockReceiver;
import com.fieldnation.service.DataService;

import android.content.Context;
import android.content.Intent;

public class ClockRpc {
	private static final String TAG = "service.rpc.ClockRpc";

	public static void enableClock(Context context) {
		ClockReceiver.registerClock(context);
	}

	public static void pulseClock(Context context) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction(DataService.ACTION_CLOCK_PULSE);
		context.startService(intent);
	}
}

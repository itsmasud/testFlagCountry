package com.fieldnation.service;

import com.fieldnation.service.rpc.ClockRpc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ClockRpc.enableClock(context);
	}

}

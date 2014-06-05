package com.fieldnation.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// boot up just happened. start the clocks
		// TODO, need to pull this value from somwhere
		ClockReceiver.registerClock(context, 5000);
		System.out.println("BootReciever.onReceive();");

	}

}

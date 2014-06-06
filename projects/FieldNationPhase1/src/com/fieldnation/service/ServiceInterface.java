package com.fieldnation.service;

import android.content.Context;
import android.content.Intent;

public class ServiceInterface {

	public static void pulseClock(Context context) {
		Intent intent = new Intent(context, DataService.class);
		intent.setAction("CLOCK_PULSE");
		context.startService(intent);
	}
}

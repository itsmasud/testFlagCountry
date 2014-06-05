package com.fieldnation.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ClockReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ServiceInterface.pulseClock(context);
	}

	public static void registerClock(Context context, long delay) {
		PendingIntent pintent = getOneSecondTickAlarmIntent(context);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		alarm.cancel(pintent);

		alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), delay, pintent);
	}

	public static void unregisterOneSecondTickAlarm(Context context) {
		PendingIntent pintent = getOneSecondTickAlarmIntent(context);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		alarm.cancel(pintent);
	}

	private static PendingIntent getOneSecondTickAlarmIntent(Context context) {
		Intent intent = new Intent(context, ClockReceiver.class);
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return pintent;
	}

}

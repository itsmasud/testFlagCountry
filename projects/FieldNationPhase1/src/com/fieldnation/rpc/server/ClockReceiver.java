package com.fieldnation.rpc.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

//TODO unlink from client side app?
public class ClockReceiver extends BroadcastReceiver {
	private static final String TAG = "rpc.server.ClockReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		ClockService.pulseClock(context);

		// TODO, setup next clock
		if (intent.hasExtra("IS_ONE_TIME")) {
			registerClock(context);
		}
	}

	// repeating alarm
	public static void registerClock(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		long duration_min = 15;

		try {
			duration_min = Long.parseLong(sp.getString("sync_frequency", "15"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Log.v(TAG, "Duration: " + duration_min);
		if (duration_min != -1) {
			registerOneTimeAlarm(context, duration_min * 60000);
		}
	}

	private static void registerClock(Context context, long delay) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putString("sync_frequency", (delay / 60000) + "").commit();

		PendingIntent pintent = getClockIntent(context);

		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		alarm.cancel(pintent);

		alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				delay, pintent);
	}

	public static void unregisterClock(Context context) {
		PendingIntent pintent = getClockIntent(context);

		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		alarm.cancel(pintent);
	}

	private static PendingIntent getClockIntent(Context context) {
		Intent intent = new Intent(context, ClockReceiver.class);
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		return pintent;
	}

	// single alarm
	private static void registerOneTimeAlarm(Context context, long duration) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putString("sync_frequency", (duration / 60000) + "").commit();

		Intent intent = new Intent(context, ClockReceiver.class);
		intent.putExtra("IS_ONE_TIME", true);
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		alarm.cancel(pintent);

		alarm.set(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + duration, pintent);
	}

	public static void unregisterOneTimeAlarm(Context context) {
		Intent intent = new Intent(context, ClockReceiver.class);
		intent.putExtra("IS_ONE_TIME", true);
		PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		alarm.cancel(pintent);
	}

}

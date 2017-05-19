package com.fieldnation.ui.ncns;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 5/19/17.
 */

public class RemindMeBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "RemindMeBroadcastReceiver";
    private static final long ALARM_TIMER = 1800000; // 30 min
    //private static final long ALARM_TIMER = 10000; // 10 seconds


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive");
        // query the list, if received, then set the flag
        context.startService(new Intent(context, RemindMeService.class));
    }

    public static void registerAlarm(Context context) {
        Log.v(TAG, "registerAlarm");
        Intent intent = new Intent(context, RemindMeBroadcastReceiver.class);
        PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ALARM_TIMER, pintent);
    }
}
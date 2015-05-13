package com.fieldnation.service.crawler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Michael Carver on 5/13/2015.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra("ALARM_CRAWLER")) {
            Intent service = new Intent(context, WebCrawlerService.class);
            service.putExtra("IS_ALARM", true);
            context.startService(service);
        }
    }

    public static void registerCrawlerAlarm(Context context, long alarmTime) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra("ALARM_CRAWLER", "ALARM_CRAWLER");
        PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(pintent);

        alarm.set(AlarmManager.RTC_WAKEUP, alarmTime, pintent);
    }


    public static void unregisterCrawlerAlarm(Context context) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra("ALARM_CRAWLER", "ALARM_CRAWLER");
        PendingIntent pintent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(pintent);
    }
}

package com.fieldnation.rpc.server;

import android.content.Context;

public class ClockService {
    private static final String TAG = "rpc.server.ClockService";

    public static void enableClock(Context context) {
//        ClockReceiver.registerClock(context);
    }

/*
    public static void pulseClock(Context context) {
        Intent intent = new Intent(context, DataService.class);
        intent.setAction(DataServiceConstants.ACTION_CLOCK_PULSE);
        context.startService(intent);
    }
*/
}

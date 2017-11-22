package com.fieldnation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 11/21/17.
 */

public class CalendarSyncService extends Service {
    private static final String TAG = "CalendarSyncService";

    private static CalendarSyncAdapter _syncAdapter = null;
    private static final Object LOCK = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        synchronized (LOCK) {
            if (_syncAdapter == null)
                _syncAdapter = new CalendarSyncAdapter(getApplicationContext(), true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return _syncAdapter.getSyncAdapterBinder();
    }
}

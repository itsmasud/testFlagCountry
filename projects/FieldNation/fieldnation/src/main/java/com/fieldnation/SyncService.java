package com.fieldnation;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {
    private static final String TAG = "SyncService";

    private static SyncAdapter _syncAdapter = null;

    private static final Object LOCK = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (LOCK) {
            if (_syncAdapter == null) {
                _syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _syncAdapter.getSyncAdapterBinder();
    }

}

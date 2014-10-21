package com.fieldnation.rpc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {
	private static final String TAG = "rpc.server.SyncService";

	private static SyncAdapter _syncAdapter = null;

	private static final Object _lock = new Object();

	@Override
	public void onCreate() {
		super.onCreate();

		synchronized (_lock) {
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

package com.fieldnation.service.crawler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.utils.misc;

/**
 * Created by Michael Carver on 4/21/2015.
 */
public class WebCrawlerService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        new AsyncTaskEx<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                misc.flushLogs(WebCrawlerService.this, 86400000);
                StoredObject.flush(WebCrawlerService.this,604800000);
                return null;
            }
        }.executeEx();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

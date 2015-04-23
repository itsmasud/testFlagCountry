package com.fieldnation.service.data.photo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoDataService extends Service implements PhotoConstants {
    public static final String TAG = "PhotoDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    private List<Intent> _intents = new LinkedList<>();
    private List<WorkerThread> _threads = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (_intents) {
            for (int i = 0; i < 10; i++) {
                WorkerThread thread = new WorkerThread(this, _intents);
                thread.start();
                _threads.add(thread);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            synchronized (_intents) {
                _intents.add(intent);
            }
        }

        return START_STICKY;
    }

    private class WorkerThread extends Thread {
        private List<Intent> _intents;
        private Context _context;

        public WorkerThread(Context context, List<Intent> intents) {
            _intents = intents;
            _context = context;
        }

        @Override
        public void run() {
            Intent intent = null;
            Context context = _context;

            while (true) {
                synchronized (_intents) {
                    if (_intents.size() > 0) {
                        intent = _intents.remove(0);
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                synchronized (LOCK) {
                    COUNT++;
                }
                processImage(context, intent);
                synchronized (LOCK) {
                    COUNT--;
                    if (COUNT == 0) {
                        stopSelf();
                    }
                }
            }
        }
    }

    public void processImage(Context context, Intent intent) {
        if (context == null)
            return;

        Log.v(TAG, intent.getExtras().toString());
        String url = intent.getStringExtra(PARAM_URL);
        boolean getCircle = intent.getBooleanExtra(PARAM_CIRCLE, false);
        String objectName = "PhotoCache" + (getCircle ? "Circle" : "");
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        // check cache
        StoredObject obj = StoredObject.get(context, objectName, url);

        // if exists, then send result via broadcast
        // this code keeps images for ever
        if (obj != null) {
            PhotoDataDispatch.photo(context, obj.getFile(), url, getCircle);
        }

        if (obj == null) {
            // doesn't exist, try to grab it.
            PhotoTransactionBuilder.getPhoto(context, objectName, url, getCircle, isSync);
        }
    }
}

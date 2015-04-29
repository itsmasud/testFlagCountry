package com.fieldnation.service.data.photo;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;

import java.util.List;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoDataService extends MSService implements PhotoConstants {
    public static final String TAG = "PhotoDataService";

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public WorkerThread getNewWorker(ThreadManager manager, List<Intent> intents) {
        return new MyWorkerThread(manager, this, intents);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyWorkerThread extends WorkerThread {
        private String TAG = UniqueTag.makeTag("PhotoDataServiceThread");
        private Context _context;

        public MyWorkerThread(ThreadManager manager, Context context, List<Intent> intents) {
            super(manager, intents);
            setName(TAG);
            _context = context;
        }

        @Override
        public void processIntent(Intent intent) {
            processImage(_context, intent);
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

package com.fieldnation.service.data.photo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionBuilder;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoDataService extends Service implements PhotoConstants {
    public static final String TAG = "PhotoDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new PhotoProcessingRunnable(this, intent)).start();

        return START_STICKY;
    }

    private class PhotoProcessingRunnable implements Runnable {
        private WeakReference<Context> _context;
        private Intent _intent;

        PhotoProcessingRunnable(Context context, Intent intent) {
            _intent = intent;
            _context = new WeakReference<Context>(context);
        }

        @Override
        public void run() {
            synchronized (LOCK) {
                COUNT++;
            }
            processImage();
            synchronized (LOCK) {
                COUNT--;
                if (COUNT == 0) {
                    stopSelf();
                }
            }
        }

        public void processImage() {
            Context context = _context.get();
            if (context == null)
                return;

            Log.v(TAG, _intent.getExtras().toString());
            String url = _intent.getStringExtra(PARAM_URL);
            boolean getCircle = _intent.getBooleanExtra(PARAM_CIRCLE, false);
            String objectName = "PhotoCache" + (getCircle ? "Circle" : "");

            // check cache
            StoredObject obj = StoredObject.get(context, objectName, url);

            // if exists, then send result via broadcast?
            if (obj != null) {
                Bundle bundle = _intent.getExtras();
                bundle.putSerializable(RESULT_IMAGE_FILE, obj.getFile());
                bundle.putBoolean(PARAM_CIRCLE, getCircle);
                bundle.putString(PARAM_URL, url);
                Log.v(TAG, "url: " + url);
                Log.v(TAG, "path: " + obj.getFile().getAbsolutePath());
                TopicService.dispatchEvent(context, TOPIC_ID_PHOTO_READY + "/" + url, bundle, true);
                return;
            } else {
                // doesn't exist, try to grab it.
                try {
                    WebTransactionBuilder.builder(context)
                            .key(objectName + ":" + url)
                            .priority(WebTransaction.Priority.LOW)
                            .handler(PhotoTransactionHandler.class)
                            .handlerParams(PhotoTransactionHandler.generateParams(url, getCircle))
                            .request(
                                    new HttpJsonBuilder()
                                            .method("GET")
                                            .path(url)
                            ).send();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

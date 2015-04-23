package com.fieldnation.service.data.workorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Transform;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/24/2015.
 */
public class WorkorderDataService extends Service implements WorkorderDataConstants {
    private static final String TAG = "WorkorderDataService";
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
        Log.v(TAG, "onStartCommand");
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
                if (context != null) {
                    String action = intent.getStringExtra(PARAM_ACTION);
                    if (action.equals(PARAM_ACTION_LIST)) {
                        listWorkorders(context, intent);
                    } else if (action.equals(PARAM_ACTION_DETAILS)) {
                        details(context, intent);
                    } else if (action.equals(PARAM_ACTION_GET_SIGNATURE)) {
                        getSignature(context, intent);
                    } else if (action.equals(PARAM_ACTION_GET_BUNDLE)) {
                        getBundle(context, intent);
                    } else if (action.equals(PARAM_ACTION_UPLOAD_DELIVERABLE)) {
                        uploadDeliverable(context, intent);
                    }
                }
                synchronized (LOCK) {
                    COUNT--;
                    if (COUNT == 0) {
                        stopSelf();
                    }
                }
            }
        }

    }


    // commands
    private static void listWorkorders(Context context, Intent intent) {
        Log.v(TAG, "listWorkorders");
        String selector = intent.getStringExtra(PARAM_LIST_SELECTOR);
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER_LIST + selector, page);
        if (obj != null) {
            try {
                JsonArray ja = new JsonArray(obj.getData());
                for (int i = 0; i < ja.size(); i++) {
                    JsonObject json = ja.getJsonObject(i);
                    Transform.applyTransform(context, json, PSO_WORKORDER, json.getLong("workorderId"));
                }

                WorkorderDispatch.workorderList(context, ja, page, selector);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        WorkorderTransactionBuilder.getWorkorderList(context, selector, page, isSync);
    }

    private static void details(Context context, Intent intent) {
        Log.v(TAG, "details");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        WorkorderTransactionBuilder.getWorkorder(context, workorderId, isSync);

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER, workorderId);
        if (obj != null) {
            try {
                JsonObject workorder = new JsonObject(obj.getData());

                Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

                WorkorderDispatch.workorder(context, workorder, workorderId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void getSignature(Context context, Intent intent) {
        Log.v(TAG, "getSignature");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        long signatureId = intent.getLongExtra(PARAM_SIGNATURE_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_SIGNATURE, signatureId);
        if (obj != null) {
            try {
                WorkorderDispatch.signature(context, new JsonObject(obj.getData()), workorderId, signatureId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (obj == null || isSync) {
            WorkorderTransactionBuilder.getSignature(context, workorderId, signatureId, isSync);
        }
    }

    private static void getBundle(Context context, Intent intent) {
        Log.v(TAG, "getBundle");
        long bundleId = intent.getLongExtra(PARAM_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        WorkorderTransactionBuilder.getBundle(context, bundleId, isSync);

        StoredObject obj = StoredObject.get(context, PSO_BUNDLE, bundleId);
        if (obj != null) {
            try {
                WorkorderDispatch.bundle(context, new JsonObject(obj.getData()), bundleId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void uploadDeliverable(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        long uploadSlotId = intent.getLongExtra(PARAM_UPLOAD_SLOT_ID, 0);
        String filePath = intent.getStringExtra(PARAM_LOCAL_PATH);
        String filename = intent.getStringExtra(PARAM_FILE_NAME);

        WorkorderTransactionBuilder.postDeliverable(context, filePath, filename, workorderId, uploadSlotId);
    }
}

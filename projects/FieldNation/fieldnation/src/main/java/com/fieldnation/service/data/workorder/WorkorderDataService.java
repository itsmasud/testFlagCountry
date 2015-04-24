package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Transform;

import java.util.List;

/**
 * Created by Michael Carver on 3/24/2015.
 */
public class WorkorderDataService extends MSService implements WorkorderDataConstants {
    private static final String TAG = "WorkorderDataService";

    public WorkorderDataService() {
        super();
        Log.v(TAG, "WorkorderDataService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public WorkerThread getNewWorker(List<Intent> intents) {
        return new MyWorkerThread(this, intents);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class MyWorkerThread extends WorkerThread {
        private Context _context;

        public MyWorkerThread(Context context, List<Intent> intents) {
            super("MyWorkerThread", intents);
            _context = context;
        }

        @Override
        public void processIntent(Intent intent) {
            Log.v(TAG, "MyWorkerThread, processIntent");
            if (_context != null) {
                String action = intent.getStringExtra(PARAM_ACTION);
                if (action.equals(PARAM_ACTION_LIST)) {
                    listWorkorders(_context, intent);
                } else if (action.equals(PARAM_ACTION_DETAILS)) {
                    details(_context, intent);
                } else if (action.equals(PARAM_ACTION_GET_SIGNATURE)) {
                    getSignature(_context, intent);
                } else if (action.equals(PARAM_ACTION_GET_BUNDLE)) {
                    getBundle(_context, intent);
                } else if (action.equals(PARAM_ACTION_UPLOAD_DELIVERABLE)) {
                    uploadDeliverable(_context, intent);
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

                WorkorderDispatch.workorderList(context, ja, page, selector, isSync);
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

                WorkorderDispatch.workorder(context, workorder, workorderId, isSync);
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
                WorkorderDispatch.signature(context, new JsonObject(obj.getData()), workorderId, signatureId, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null || isSync) {
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
                WorkorderDispatch.bundle(context, new JsonObject(obj.getData()), bundleId, isSync);
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

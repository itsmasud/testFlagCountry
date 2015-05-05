package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.Log;
import com.fieldnation.ThreadManager;
import com.fieldnation.UniqueTag;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Transform;

import java.util.List;

/**
 * Created by Michael Carver on 3/24/2015.
 */
public class WorkorderService extends MSService implements WorkorderConstants {
    private static final String TAG = "WorkorderDataService";

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }

    @Override
    public WorkerThread getNewWorker(ThreadManager manager, List<Intent> intents) {
        return new MyWorkerThread(manager, this, intents);
    }

    private class MyWorkerThread extends WorkerThread {
        private String TAG = UniqueTag.makeTag("WorkorderDataServiceThread");
        private Context _context;

        public MyWorkerThread(ThreadManager manager, Context context, List<Intent> intents) {
            super(manager, intents);
            setName(TAG);
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
                } else if (action.equals(PARAM_ACTION_DELIVERABLE)) {
                    getDeliverable(_context, intent);
                } else if (action.equals(PARAM_ACTION_DOWNLOAD_DELIVERABLE)) {
                    downloadDeliverable(_context, intent);
                } else if (action.equals(PARAM_ACTION_DELIVERABLE_LIST))
                    listDeliverables(_context, intent);
            }
        }
    }

    private static void listDeliverables(Context context, Intent intent) {
        Log.v(TAG, "listDeliverables");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_DELIVERABLE_LIST, workorderId);
        if (obj != null) {
            try {
                WorkorderDispatch.deliverableList(context, new JsonArray(obj.getData()), workorderId, isSync);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        WorkorderTransactionBuilder.listDeliverables(context, workorderId, isSync);
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

                WorkorderDispatch.list(context, ja, page, selector, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        WorkorderTransactionBuilder.list(context, selector, page, isSync);
    }

    private static void details(Context context, Intent intent) {
        Log.v(TAG, "details");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        WorkorderTransactionBuilder.get(context, workorderId, isSync);

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER, workorderId);
        if (obj != null) {
            try {
                JsonObject workorder = new JsonObject(obj.getData());

                Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

                WorkorderDispatch.get(context, workorder, workorderId, isSync);
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

    private static void getDeliverable(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        long deliverableId = intent.getLongExtra(PARAM_DELIVERABLE_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_DELIVERABLE, deliverableId);

        if (obj != null) {
            try {
                WorkorderDispatch.deliverable(context, new JsonObject(obj.getData()), workorderId, deliverableId, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (obj == null || isSync) {
            WorkorderTransactionBuilder.getDeliverable(context, workorderId, deliverableId, isSync);
        }
    }

    private static void downloadDeliverable(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        long deliverableId = intent.getLongExtra(PARAM_DELIVERABLE_ID, 0);
        String url = intent.getStringExtra(PARAM_URL);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_DELIVERABLE_FILE, deliverableId);

        if (obj != null) {
            try {
                WorkorderDispatch.deliverableFile(context, workorderId, deliverableId, obj.getFile(), isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null || isSync) {
            WorkorderTransactionBuilder.downloadDeliverable(context, workorderId, deliverableId, url, isSync);
        }
    }
}

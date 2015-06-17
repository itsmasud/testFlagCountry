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
        private String TAG = UniqueTag.makeTag("WorkorderServiceThread");
        private Context _context;

        public MyWorkerThread(ThreadManager manager, Context context, List<Intent> intents) {
            super(manager, intents);
            setName(TAG);
            _context = context.getApplicationContext();
        }

        @Override
        public void processIntent(Intent intent) {
            Log.v(TAG, "MyWorkerThread, processIntent");
            if (_context != null) {
                String action = intent.getStringExtra(PARAM_ACTION);
                switch (action) {
                    case PARAM_ACTION_GET:
                        get(_context, intent);
                        break;
                    case PARAM_ACTION_LIST:
                        list(_context, intent);
                        break;
                    case PARAM_ACTION_GET_SIGNATURE:
                        getSignature(_context, intent);
                        break;
                    case PARAM_ACTION_GET_BUNDLE:
                        getBundle(_context, intent);
                        break;
                    case PARAM_ACTION_UPLOAD_DELIVERABLE:
                        uploadDeliverable(_context, intent);
                        break;
                    case PARAM_ACTION_GET_DELIVERABLE:
                        getDeliverable(_context, intent);
                        break;
//                    case PARAM_ACTION_DOWNLOAD_DELIVERABLE:
//                        downloadDeliverable(_context, intent);
//                        break;
                    case PARAM_ACTION_LIST_MESSAGES:
                        listMessages(_context, intent);
                        break;
                    case PARAM_ACTION_LIST_NOTIFICATIONS:
                        listAlerts(_context, intent);
                        break;
                    case PARAM_ACTION_LIST_TASKS:
                        listTasks(_context, intent);
                }
            }
        }
    }

    private static void get(Context context, Intent intent) {
        Log.v(TAG, "get");
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);

        if (allowCache && !isSync) {
            StoredObject obj = StoredObject.get(context, PSO_WORKORDER, workorderId);
            if (obj != null) {
                try {
                    JsonObject workorder = new JsonObject(obj.getData());

                    Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

                    WorkorderDispatch.get(context, workorder, workorderId, false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.get(context, workorderId, isSync);
    }

    private static void list(Context context, Intent intent) {
        Log.v(TAG, "list");
        String selector = intent.getStringExtra(PARAM_LIST_SELECTOR);
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(context, PSO_WORKORDER_LIST + selector, page);
            if (obj != null) {
                try {
                    JsonArray ja = new JsonArray(obj.getData());
                    for (int i = 0; i < ja.size(); i++) {
                        JsonObject json = ja.getJsonObject(i);
                        Transform.applyTransform(context, json, PSO_WORKORDER, json.getLong("workorderId"));
                    }

                    WorkorderDispatch.list(context, ja, page, selector, false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.list(context, selector, page, isSync);
    }

    private static void getSignature(Context context, Intent intent) {
        Log.v(TAG, "getSignature");
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long signatureId = intent.getLongExtra(PARAM_SIGNATURE_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_SIGNATURE, signatureId);
        if (obj != null) {
            try {
                WorkorderDispatch.signature(context, new JsonObject(obj.getData()), workorderId, signatureId, false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null) {
            WorkorderTransactionBuilder.getSignature(context, workorderId, signatureId, isSync);
        }
    }

    private static void listMessages(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(context, PSO_MESSAGE_LIST, workorderId);
            if (obj != null) {
                try {
                    WorkorderDispatch.listMessages(context, workorderId, new JsonArray(obj.getData()), false, isSync);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.listMessages(context, workorderId, false, isSync);
    }

    private static void listAlerts(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(context, PSO_ALERT_LIST, workorderId);
            if (obj != null) {
                try {
                    WorkorderDispatch.listAlerts(context, workorderId, new JsonArray(obj.getData()), false, isSync);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.listAlerts(context, workorderId, false, isSync);
    }

    private static void listTasks(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(context, PSO_TASK_LIST, workorderId);
            if (obj != null) {
                try {
                    WorkorderDispatch.listTasks(context, workorderId, new JsonArray(obj.getData()), false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.listTasks(context, workorderId, isSync);
    }

    private static void getBundle(Context context, Intent intent) {
        Log.v(TAG, "getBundle");
        long bundleId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(context, PSO_BUNDLE, bundleId);
            if (obj != null) {
                try {
                    WorkorderDispatch.bundle(context, new JsonObject(obj.getData()), bundleId, false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.getBundle(context, bundleId, isSync);
    }

    private static void uploadDeliverable(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long uploadSlotId = intent.getLongExtra(PARAM_UPLOAD_SLOT_ID, 0);
        String filePath = intent.getStringExtra(PARAM_LOCAL_PATH);
        String filename = intent.getStringExtra(PARAM_FILE_NAME);

        WorkorderTransactionBuilder.uploadDeliverable(context, filePath, filename, workorderId, uploadSlotId);
    }

    private static void getDeliverable(Context context, Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long deliverableId = intent.getLongExtra(PARAM_DELIVERABLE_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(context, PSO_DELIVERABLE, deliverableId);

        if (obj != null) {
            try {
                WorkorderDispatch.getDeliverable(context, new JsonObject(obj.getData()), workorderId, deliverableId, false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (obj == null || isSync) {
            WorkorderTransactionBuilder.getDeliverable(context, workorderId, deliverableId, isSync);
        }
    }

//    private static void downloadDeliverable(Context context, Intent intent) {
//        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
//        long deliverableId = intent.getLongExtra(PARAM_DELIVERABLE_ID, 0);
//        String url = intent.getStringExtra(PARAM_URL);
//        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
//
//        StoredObject obj = StoredObject.get(context, PSO_DELIVERABLE_FILE, deliverableId);
//        if (obj != null) {
//            try {
//                WorkorderDispatch.downloadDeliverable(context, workorderId, deliverableId, obj.getFile(), isSync);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        if (obj == null || isSync) {
//            WorkorderTransactionBuilder.downloadDeliverable(context, workorderId, deliverableId, url, isSync);
//        }
//    }
}

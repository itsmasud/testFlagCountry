package com.fieldnation.service.data.workorder;

import android.content.Intent;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Transform;


/**
 * Created by Michael Carver on 3/24/2015.
 */
public class WorkorderService extends MSService implements WorkorderConstants {
    private static final String TAG = "WorkorderDataService";

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int getMaxWorkerCount() {
        return 2;
    }


    @Override
    public void processIntent(Intent intent) {
        if (intent.hasExtra(PARAM_ACTION)) {
            Log.v(TAG, "MyWorkerThread, processIntent");
            String action = intent.getStringExtra(PARAM_ACTION);
            switch (action) {
                case PARAM_ACTION_GET:
                    get(intent);
                    break;
                case PARAM_ACTION_LIST:
                    list(intent);
                    break;
                case PARAM_ACTION_GET_SIGNATURE:
                    getSignature(intent);
                    break;
                case PARAM_ACTION_GET_BUNDLE:
                    getBundle(intent);
                    break;
                case PARAM_ACTION_UPLOAD_DELIVERABLE:
                    uploadDeliverable(intent);
                    break;
                case PARAM_ACTION_GET_DELIVERABLE:
                    getDeliverable(intent);
                    break;
//                    case PARAM_ACTION_DOWNLOAD_DELIVERABLE:
//                        downloadDeliverable( intent);
//                        break;
                case PARAM_ACTION_LIST_MESSAGES:
                    listMessages(intent);
                    break;
                case PARAM_ACTION_LIST_NOTIFICATIONS:
                    listAlerts(intent);
                    break;
                case PARAM_ACTION_LIST_TASKS:
                    listTasks(intent);
            }
        }
    }

    private void get(Intent intent) {
        Log.v(TAG, "get");
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);

        if (allowCache && !isSync) {
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_WORKORDER, workorderId);
            if (obj != null) {
                try {
                    JsonObject workorder = new JsonObject(obj.getData());

                    Transform.applyTransform(this, workorder, PSO_WORKORDER, workorderId);

                    WorkorderDispatch.get(this, workorder, workorderId, false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.get(this, workorderId, isSync);
    }

    private void list(Intent intent) {
        Log.v(TAG, "list");
        String selector = intent.getStringExtra(PARAM_LIST_SELECTOR);
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);

        if (allowCache && !isSync) {
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_WORKORDER_LIST + selector, page);
            if (obj != null) {
                try {
                    JsonArray ja = new JsonArray(obj.getData());
                    for (int i = 0; i < ja.size(); i++) {
                        JsonObject json = ja.getJsonObject(i);
                        Transform.applyTransform(this, json, PSO_WORKORDER, json.getLong("workorderId"));
                    }

                    WorkorderDispatch.list(this, ja, page, selector, false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.list(this, selector, page, isSync);
    }

    private void getSignature(Intent intent) {
        Log.v(TAG, "getSignature");
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long signatureId = intent.getLongExtra(PARAM_SIGNATURE_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(App.getProfileId(), PSO_SIGNATURE, signatureId);
        if (obj != null) {
            try {
                WorkorderDispatch.signature(this, new JsonObject(obj.getData()), workorderId, signatureId, false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (obj == null) {
            WorkorderTransactionBuilder.getSignature(this, workorderId, signatureId, isSync);
        }
    }

    private void listMessages(Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);

        if (!isSync && allowCache) {
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_MESSAGE_LIST, workorderId);
            if (obj != null) {
                try {
                    WorkorderDispatch.listMessages(this, workorderId, new JsonArray(obj.getData()), false, isSync);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        WorkorderTransactionBuilder.listMessages(this, workorderId, false, isSync);
    }

    private void listAlerts(Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_ALERT_LIST, workorderId);
            if (obj != null) {
                try {
                    WorkorderDispatch.listAlerts(this, workorderId, new JsonArray(obj.getData()), false, isSync);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.listAlerts(this, workorderId, false, isSync);
    }

    private void listTasks(Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_TASK_LIST, workorderId);
            if (obj != null) {
                try {
                    WorkorderDispatch.listTasks(this, workorderId, new JsonArray(obj.getData()), false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.listTasks(this, workorderId, isSync);
    }

    private void getBundle(Intent intent) {
        Log.v(TAG, "getBundle");
        long bundleId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        if (!isSync) {
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_BUNDLE, bundleId);
            if (obj != null) {
                try {
                    WorkorderDispatch.bundle(this, new JsonObject(obj.getData()), bundleId, false, isSync);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        WorkorderTransactionBuilder.getBundle(this, bundleId, isSync);
    }

    private void uploadDeliverable(Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long uploadSlotId = intent.getLongExtra(PARAM_UPLOAD_SLOT_ID, 0);
        String filePath = intent.getStringExtra(PARAM_LOCAL_PATH);
        String filename = intent.getStringExtra(PARAM_FILE_NAME);
        Uri uri = (Uri) intent.getParcelableExtra(PARAM_URI);

        if (uri != null)
            WorkorderTransactionBuilder.uploadDeliverable(this, uri, filename, workorderId, uploadSlotId);
        else
            WorkorderTransactionBuilder.uploadDeliverable(this, filePath, filename, workorderId, uploadSlotId);
    }

    private void getDeliverable(Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long deliverableId = intent.getLongExtra(PARAM_DELIVERABLE_ID, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);

        StoredObject obj = StoredObject.get(App.getProfileId(), PSO_DELIVERABLE, deliverableId);

        if (obj != null) {
            try {
                WorkorderDispatch.getDeliverable(this, new JsonObject(obj.getData()), workorderId, deliverableId, false, isSync);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (obj == null || isSync) {
            WorkorderTransactionBuilder.getDeliverable(this, workorderId, deliverableId, isSync);
        }
    }

//    private void downloadDeliverable(Intent intent) {
//        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
//        long deliverableId = intent.getLongExtra(PARAM_DELIVERABLE_ID, 0);
//        String url = intent.getStringExtra(PARAM_URL);
//        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
//
//        StoredObject obj = StoredObject.get(this, PSO_DELIVERABLE_FILE, deliverableId);
//        if (obj != null) {
//            try {
//                WorkorderDispatch.downloadDeliverable(this, workorderId, deliverableId, obj.getFile(), isSync);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        if (obj == null || isSync) {
//            WorkorderTransactionBuilder.downloadDeliverable(this, workorderId, deliverableId, url, isSync);
//        }
//    }
}

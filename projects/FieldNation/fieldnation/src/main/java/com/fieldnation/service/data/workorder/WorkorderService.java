package com.fieldnation.service.data.workorder;

import android.content.Intent;
import android.net.Uri;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.MSService;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.transaction.Transform;


/**
 * Created by Michael Carver on 3/24/2015.
 */
public class WorkorderService extends MSService implements WorkorderConstants {
    private static final String TAG = "WorkorderService";

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
            String action = intent.getStringExtra(PARAM_ACTION);
            Log.v(TAG, "MyWorkerThread, processIntent:{action:" + action + "}");
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
                case PARAM_ACTION_CACHE_DELIVERABLE:
                    cacheDeliverable(intent);
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

                    Transform.applyTransform(workorder, PSO_WORKORDER, workorderId);

                    WorkorderDispatch.get(this, workorder, workorderId, false, isSync, true);

                    if (workorder.has("_action")) {
                        Log.v(TAG, "get._action=" + workorder.get("_action"));

                        try {
                            Log.v(TAG, "_action=" + Workorder.fromJson(workorder).getActions());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        WorkorderTransactionBuilder.get(this, workorderId, isSync);
    }

    private void list(Intent intent) {
        String selector = intent.getStringExtra(PARAM_LIST_SELECTOR);
        int page = intent.getIntExtra(PARAM_PAGE, 0);
        boolean isSync = intent.getBooleanExtra(PARAM_IS_SYNC, false);
        boolean allowCache = intent.getBooleanExtra(PARAM_ALLOW_CACHE, true);
        Log.v(TAG, "list:{selector:" + selector + ", page:" + page + ", isSync:" + isSync + ", allowCache:" + allowCache + "}");

        if (allowCache && !isSync) {
            Log.v(TAG, "list: checking cache");
            StoredObject obj = StoredObject.get(App.getProfileId(), PSO_WORKORDER_LIST + selector, page);
            if (obj != null) {
                try {
                    JsonArray ja = new JsonArray(obj.getData());
                    for (int i = 0; i < ja.size(); i++) {
                        JsonObject json = ja.getJsonObject(i);
                        Transform.applyTransform(json, PSO_WORKORDER, json.getLong("workorderId"));
                    }

                    WorkorderDispatch.list(this, ja, page, selector, false, isSync, true);
                    Log.v(TAG, "list: dispatched cached result");

                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        Log.v(TAG, "list: query server");
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
                Log.v(TAG, ex);
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
                    Log.v(TAG, e);
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
                    Log.v(TAG, e);
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
                    Log.v(TAG, ex);
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
                    Log.v(TAG, ex);
                }
            }
        }

        WorkorderTransactionBuilder.getBundle(this, bundleId, isSync);
    }

    private void cacheDeliverable(Intent intent) {
        WorkorderDispatch.cacheDeliverableStart(App.get());
        Uri uri = intent.getParcelableExtra(PARAM_URI);
        try {
            StoredObject upFile = StoredObject.put(App.getProfileId(), "CacheFile", uri.toString(),
                    this.getContentResolver().openInputStream(uri), "uploadTemp.dat");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        } finally {
            WorkorderDispatch.cacheDeliverableEnd(App.get());
        }
    }

    private void uploadDeliverable(Intent intent) {
        long workorderId = intent.getLongExtra(PARAM_WORKORDER_ID, 0);
        long uploadSlotId = intent.getLongExtra(PARAM_UPLOAD_SLOT_ID, 0);
        String filePath = intent.getStringExtra(PARAM_LOCAL_PATH);
        String filename = intent.getStringExtra(PARAM_FILE_NAME);
        String photoDescription = intent.getStringExtra(PARAM_FILE_DESCRIPTION);
        Uri uri = intent.getParcelableExtra(PARAM_URI);

        if (uri != null) {
            try {
                StoredObject cache = StoredObject.get(App.getProfileId(), "CacheFile", uri.toString());
                if (cache != null) {
                    WorkorderTransactionBuilder.uploadDeliverable(this, cache, filename, photoDescription, workorderId, uploadSlotId);
                } else {
                    WorkorderTransactionBuilder.uploadDeliverable(this, this.getContentResolver().openInputStream(uri), filename, photoDescription, workorderId, uploadSlotId);
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else
            WorkorderTransactionBuilder.uploadDeliverable(this, filePath, filename, photoDescription, workorderId, uploadSlotId);
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
                Log.v(TAG, ex);
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
//                Log.v(TAG, ex);
//            }
//        }
//
//        if (obj == null || isSync) {
//            WorkorderTransactionBuilder.downloadDeliverable(this, workorderId, deliverableId, url, isSync);
//        }
//    }
}

package com.fieldnation.service.data.workorder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.fieldnation.Log;
import com.fieldnation.data.transfer.WorkorderTransfer;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransactionBuilder;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Michael Carver on 3/24/2015.
 */
public class WorkorderDataService extends Service implements WorkorderDataConstants {
    private static final String TAG = "WorkorderDataService";

    private static final Object LOCK = new Object();
    private static int COUNT = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        if (intent != null) {
            new Thread(new WorkorderProcessingRunnable(this, intent)).start();
        }
        return START_STICKY;
    }

    // commands
    private static void listWorkorders(Context context, Intent intent) {
        Log.v(TAG, "listWorkorders");
        String selector = intent.getStringExtra(PARAM_LIST_SELECTOR);
        int page = intent.getIntExtra(PARAM_PAGE, 0);

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER_LIST + selector, page);
        if (obj != null) {
            try {
                JsonArray ja = new JsonArray(obj.getData());
                for (int i = 0; i < ja.size(); i++) {
                    JsonObject json = ja.getJsonObject(i);
                    Transform.applyTransform(context, json, PSO_WORKORDER, json.getLong("workorderId"));
                }

                WorkorderDataDispatch.workorderList(context, ja, page, selector);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkorderListTransactionHandler.class)
                    .handlerParams(WorkorderListTransactionHandler.generateParams(page, selector))
                    .key("WorkorderList/" + selector + "/" + page)
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + selector)
                            .urlParams("?page=" + page))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private static void details(Context context, Intent intent) {
        Log.v(TAG, "details");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);

        WorkorderDataClient.detailsWebRequest(context, workorderId);

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER, workorderId);
        if (obj != null) {
            try {
                JsonObject workorder = new JsonObject(obj.getData());

                Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

                WorkorderDataDispatch.workorder(context, workorder, workorderId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void getSignature(Context context, Intent intent) {
        Log.v(TAG, "getSignature");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        long signatureId = intent.getLongExtra(PARAM_SIGNATURE_ID, 0);
        StoredObject obj = StoredObject.get(context, PSO_SIGNATURE, signatureId);
        if (obj != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
                bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonObject(obj.getData()));
                bundle.putLong(PARAM_ID, workorderId);
                bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
                TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE + "/" + signatureId, bundle, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                WebTransactionBuilder.builder(context)
                        .priority(Priority.HIGH)
                        .handler(WorkorderTransactionHandler.class)
                        .handlerParams(WorkorderTransactionHandler.pGetSignature(workorderId, signatureId))
                        .key("GetSignature/" + signatureId)
                        .useAuth()
                        .request(new HttpJsonBuilder()
                                .protocol("https")
                                .method("GET")
                                .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId))
                        .send();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void getBundle(Context context, Intent intent) {
        Log.v(TAG, "getBundle");
        long bundleId = intent.getLongExtra(PARAM_ID, 0);
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(BundleTransactionHandler.class)
                    .handlerParams(BundleTransactionHandler.generateGetParams(bundleId))
                    .key("GetBundle/" + bundleId)
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/bundle/" + bundleId))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StoredObject obj = StoredObject.get(context, PSO_BUNDLE, bundleId);
        if (obj != null) {
            try {
                Bundle bundle = new Bundle();
                bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
                bundle.putParcelable(PARAM_DATA_PARCELABLE, new JsonObject(obj.getData()));
                bundle.putLong(PARAM_ID, bundleId);
                TopicService.dispatchEvent(context, PARAM_ACTION_GET_BUNDLE + "/" + bundleId, bundle, true);
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

        StoredObject upFile = StoredObject.put(context, "TempFile", filePath, new File(filePath));

        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables")
                    .multipartFile("file", filename, upFile)
                    .doNotRead();

            if (uploadSlotId != 0) {
                builder.path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + uploadSlotId);
            }

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(DeliverableDeleteTransactionHandler.class)
                    .handlerParams(DeliverableDeleteTransactionHandler.generateParams(workorderId))
                    .useAuth()
                    .request(builder)
                    .transform(Transform.makeTransformQuery(
                            "Workorder",
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeUploadDeliverable(uploadSlotId, filename).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class WorkorderProcessingRunnable implements Runnable {
        private WeakReference<Context> _context;
        private Intent _intent;

        WorkorderProcessingRunnable(Context context, Intent intent) {
            _context = new WeakReference<>(context);
            _intent = intent;
        }

        @Override
        public void run() {
            synchronized (LOCK) {
                COUNT++;
            }
            Context context = _context.get();
            if (context != null) {
                String action = _intent.getStringExtra(PARAM_ACTION);
                if (action.equals(PARAM_ACTION_LIST)) {
                    listWorkorders(context, _intent);
                } else if (action.equals(PARAM_ACTION_DETAILS)) {
                    details(context, _intent);
                } else if (action.equals(PARAM_ACTION_GET_SIGNATURE)) {
                    getSignature(context, _intent);
                } else if (action.equals(PARAM_ACTION_GET_BUNDLE)) {
                    getBundle(context, _intent);
                } else if (action.equals(PARAM_ACTION_UPLOAD_DELIVERABLE)) {
                    uploadDeliverable(context, _intent);
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

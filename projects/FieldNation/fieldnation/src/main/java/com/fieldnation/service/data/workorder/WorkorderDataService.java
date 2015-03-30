package com.fieldnation.service.data.workorder;

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

        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderListTransactionHandler.class)
                    .handlerParams(
                            WorkorderListTransactionHandler.generateParams(page, selector)
                    )
                    .key("WorkorderList" + selector + page)
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/workorder/" + selector)
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER_LIST + selector, page + "");
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putByteArray(PARAM_DATA, obj.getData());
            bundle.putInt(PARAM_PAGE, page);
            bundle.putString(PARAM_LIST_SELECTOR, selector);
            bundle.putString(PARAM_ACTION, PARAM_ACTION_LIST);
            TopicService.dispatchEvent(context, PARAM_ACTION_LIST + "/" + selector, bundle, true);
        }
    }

    private static void details(Context context, Intent intent) {
        Log.v(TAG, "details");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderDetailsTransactionHandler.class)
                    .handlerParams(WorkorderDetailsTransactionHandler.generateParams(workorderId))
                    .key("Workorder/" + workorderId)
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/workorder/" + workorderId + "/details")
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StoredObject obj = StoredObject.get(context, PSO_WORKORDER, workorderId + "");
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ACTION, PARAM_ACTION_DETAILS);
            bundle.putByteArray(PARAM_DATA, obj.getData());
            bundle.putLong(PARAM_ID, workorderId);
            TopicService.dispatchEvent(context, PARAM_ACTION_DETAILS + "/" + workorderId, bundle, true);
        }
    }

    private static void getSignature(Context context, Intent intent) {
        Log.v(TAG, "getSignature");
        long workorderId = intent.getLongExtra(PARAM_ID, 0);
        long signatureId = intent.getLongExtra(PARAM_SIGNATURE_ID, 0);
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(SignatureTransactionHandler.class)
                    .handlerParams(SignatureTransactionHandler.generateParams(workorderId, signatureId))
                    .key("GetSignature/" + signatureId)
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/workorder/" + workorderId + "/signature/" + signatureId)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StoredObject obj = StoredObject.get(context, PSO_SIGNATURE, signatureId + "");
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
            bundle.putByteArray(PARAM_DATA, obj.getData());
            bundle.putLong(PARAM_ID, workorderId);
            bundle.putLong(PARAM_SIGNATURE_ID, signatureId);
            TopicService.dispatchEvent(context, PARAM_ACTION_GET_SIGNATURE + "/" + signatureId, bundle, true);
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

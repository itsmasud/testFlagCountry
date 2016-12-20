package com.fieldnation.service.data.v2.workorder;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.App;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderTransactionBuilder implements WorkOrderConstants {
    private static final String TAG = "WorkOrderTransactionBuilder";

    public static void search(Context context, SavedSearchParams searchParams, int page) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/v2/workorders")
                    .priority(Priority.HIGH)
                    .listener(WorkOrderTransactionListener.class)
                    .listenerParams(WorkOrderTransactionListener.pSearch(searchParams))
                    .key(searchParams.toKey())
                    .useAuth(true)
                    .isSyncCall(false)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .urlParams(searchParams.toUrlParams() + "&page=" + page)
                            .path("/v2/workorders"))
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the details
    public static void actionDecline(Context context, long workorderId, int declineReasonId, String declineExplanation) {
        action(context, workorderId, "decline", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                (declineReasonId == -1 ? "" : "workorder_decline_reason_id=" + declineReasonId)
                        + (misc.isEmptyOrNull(declineExplanation) ? "" : "&reason_other=" + misc.escapeForURL(declineExplanation)));
    }

    public static void actionEta(Context context, long workorderId, String startTime, String endTime, String note) {
        action(context, workorderId, "confirm-eta", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "start_time=" + startTime
                        + "&end_time=" + endTime
                        + (misc.isEmptyOrNull(note) ? "" : "&note=" + misc.escapeForURL(note)));
    }

    public static void actionOnMyWay(Context context, long workOrderId, Double lat, Double lon) {
        App.get().setInteractedWorkorder();
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", "on-my-way");

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/on-my-way")
                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED);

            if (lat != null && lon != null) {
                http.body("{\"lat\"=" + lat + ", \"lon\"=" + lon + "}");
            }

            WebTransaction.Builder builder = new WebTransaction.Builder()
                    .timingKey("POST/api/rest/v2/workorders/[workorderId]/on-my-way")
                    .priority(Priority.HIGH)
                    .listener(WorkOrderTransactionListener.class)
                    .listenerParams(WorkOrderTransactionListener.pAction(workOrderId, "on-my-way"))
                    .useAuth(true)
                    .key("Workorders/" + workOrderId + "/on-my-way")
                    .request(http)
                    .addTransform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workOrderId,
                            "merges",
                            _action.toByteArray()));

            WebTransactionService.queueTransaction(context, builder.build());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    private static void action(Context context, long workorderId, String action, String params,
                               String contentType, String body) {
        Log.e(TAG, "body: " + body);
        action(context, workorderId, action, params, contentType, body, true);
    }

    private static void action(Context context, long workorderId, String action, String params,
                               String contentType, String body, boolean useKey) {
        context.startService(
                action(context, workorderId, "POST", action, params, contentType, body,
                        "POST/v2/workorder/[workorderId]/" + action,
                        WorkOrderTransactionListener.class,
                        WorkOrderTransactionListener.pAction(workorderId, action), useKey));
    }


    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, Class<? extends WebTransactionListener> clazz,
                                 byte[] handlerParams) {
        return action(context, workorderId, method, action, params, contentType, body,
                method + "/v2/workorder/[workorderId]/" + action, clazz, handlerParams, true);
    }

    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, String timingKey, Class<? extends WebTransactionListener> clazz,
                                 byte[] handlerParams, boolean useKey) {
        App.get().setInteractedWorkorder();
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", action);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method(method)
                    .path("/v2/workorder/" + workorderId + "/" + action);

            if (params != null) {
                http.urlParams(params);
            }

            if (body != null) {
                http.body(body);

                if (contentType != null) {
                    http.header(HttpJsonBuilder.HEADER_CONTENT_TYPE, contentType);
                }
            }

            WebTransaction.Builder builder = new WebTransaction.Builder()
                    .timingKey(timingKey)
                    .priority(Priority.HIGH)
                    .listener(clazz)
                    .listenerParams(handlerParams)
                    .useAuth(true)
                    .key("Workorder/" + workorderId + "/" + action)
                    .request(http)
                    .addTransform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()));

            if (useKey)
                builder.key("Workorder/" + workorderId + "/" + action);

            return WebTransactionService.makeQueueTransactionIntent(context, builder.build());
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

}

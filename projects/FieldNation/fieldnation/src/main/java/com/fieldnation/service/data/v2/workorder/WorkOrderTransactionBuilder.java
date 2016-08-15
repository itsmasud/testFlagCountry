package com.fieldnation.service.data.v2.workorder;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrderTransactionBuilder implements WorkOrderConstants {
    private static final String TAG = "WorkOrderTransactionBuilder";

    public static void search(Context context, SearchParams searchParams, int page) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(WorkOrderTransactionHandler.class)
                    .handlerParams(WorkOrderTransactionHandler.pSearch(searchParams))
                    .key(searchParams.toKey())
                    .useAuth(true)
                    .isSyncCall(false)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .timingKey("GET/v2/workorders")
                            .urlParams(searchParams.toUrlParams() + "&page=" + page)
                            .path("/v2/workorders"))
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // returns the details
    public static void actionDecline(Context context, long workorderId, int declineReasonId, String declineExplanation) {
        action(context, workorderId, "decline", null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                (declineReasonId == -1 ? "" : "workorder_decline_reason_id=" + declineReasonId)
                        + (misc.isEmptyOrNull(declineExplanation) ? "" : "&explanation=" + misc.escapeForURL(declineExplanation)));
    }

    /*-*********************************-*/
    /*-             Actions             -*/
    /*-*********************************-*/
    private static void action(Context context, long workorderId, String action, String params,
                               String contentType, String body) {
        action(context, workorderId, action, params, contentType, body, true);
    }

    private static void action(Context context, long workorderId, String action, String params,
                               String contentType, String body, boolean useKey) {
        context.startService(
                action(context, workorderId, "POST", action, params, contentType, body,
                        "POST/v2/workorder/[workorderId]/" + action,
                        WorkOrderTransactionHandler.class,
                        WorkOrderTransactionHandler.pAction(workorderId, action), useKey));
    }


    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, Class<? extends WebTransactionHandler> clazz,
                                 byte[] handlerParams) {
        return action(context, workorderId, method, action, params, contentType, body,
                method + "/v2/workorder/[workorderId]/" + action, clazz, handlerParams, true);
    }

    private static Intent action(Context context, long workorderId, String method, String action, String params,
                                 String contentType, String body, String timingKey, Class<? extends WebTransactionHandler> clazz,
                                 byte[] handlerParams, boolean useKey) {
        App.get().setInteractedWorkorder();
        try {
            JsonObject _action = new JsonObject();
            _action.put("_action[0].action", action);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .protocol("https")
                    .method(method)
                    .timingKey(timingKey)
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

            WebTransactionBuilder builder = WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(clazz)
                    .handlerParams(handlerParams)
                    .useAuth(true)
                    .key("Workorder/" + workorderId + "/" + action)
                    .request(http)
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            _action.toByteArray()));

            if (useKey)
                builder.key("Workorder/" + workorderId + "/" + action);

            return builder.makeIntent();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

}

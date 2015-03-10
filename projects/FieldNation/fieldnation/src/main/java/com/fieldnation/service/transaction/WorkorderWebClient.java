package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.transaction.handlers.WorkorderDeclineTransactionHandler;
import com.fieldnation.service.transaction.handlers.WorkorderDetailsTransactionHandler;
import com.fieldnation.service.transaction.handlers.WorkorderListTransactionHandler;
import com.fieldnation.service.transaction.handlers.WorkorderRequestHandler;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderWebClient {

    public void requestList(Context context, WorkorderDataSelector selector, int page) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .method("GET")
                .path("/api/rest/v1/workorder/" + selector.getCall())
                .urlParams("?page=" + page)
                .key("Workorder" + selector.getCall() + "ListPage" + page)
                .extra("page", page)
                .extra("listName", selector.getCall())
                .handler(WorkorderListTransactionHandler.class)
                .send();
    }

    public void details(Context context, long workorderId) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .method("GET")
                .path("/api/rest/v1/workorder/" + workorderId + "/details")
                .key("Workorder:" + workorderId)
                .handler(WorkorderDetailsTransactionHandler.class)
                .send();
    }

    public void decline(Context context, long workorderId) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .method("POST")
                .path("/api/rest/v1/workorder/" + workorderId + "/decline")
                .header("Content Type", "application/x-www-form-urlencoded")
                .extra("workorderId", workorderId)
                .handler(WorkorderDeclineTransactionHandler.class)
                .transform(Transform.makeTransformQuery(
                        "Workorder",
                        workorderId + "",
                        Transform.Action.DELETE, null
                ))
                .send();
    }

    public void request(Context context, long workorderId, long expireInSeconds) throws ParseException {
        JsonObject transform = new JsonObject();
        transform.put("status/status", "STATUS_AVAILABLE");
        transform.put("status/colorIntent", "waiting");
        transform.put("status/subStatus", "SUBSTATUS_REQUESTED");

        JsonArray listTransform = new JsonArray();
        listTransform.add(workorderId);

        WebTransactionBuilder builder = WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .method("POST")
                .path("/api/rest/v1/workorder/" + workorderId + "/request")
                .header("Content Type", "application/x-www-form-urlencoded");

        if (expireInSeconds != -1)
            builder.urlParams("expiration=" + expireInSeconds);

        builder.extra("workorderId", workorderId)
                .handler(WorkorderRequestHandler.class)
                .transform(Transform.makeTransformQuery(
                        "Workorder",
                        workorderId + "",
                        Transform.Action.MERGE,
                        transform.toByteArray()
                ))
                .transform(Transform.makeTransformQuery(
                        "WorkorderAvailableList",
                        "0",
                        Transform.Action.CULL,
                        listTransform.toByteArray()
                ))
                .transform(Transform.makeTransformQuery(
                        "WorkorderRequestedList",
                        "0",
                        Transform.Action.MERGE,
                        listTransform.toByteArray()

                ))
                .send();
    }

    public void uploadDeliverable(Context context, long workorderId, long deliverableSlotId, String localFilename) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .method("POST")
                .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableSlotId)
                .multipartFile("file", localFilename, null, "app/jpeg")
                .handler(WorkorderListTransactionHandler.class)
                .send();
    }
}

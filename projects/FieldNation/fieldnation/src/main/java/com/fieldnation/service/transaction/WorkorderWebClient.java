package com.fieldnation.service.transaction;

import android.content.Context;

import com.fieldnation.data.workorder.StatusIntent;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.handlers.WorkorderDeclineTransactionHandler;
import com.fieldnation.service.transaction.handlers.WorkorderDetailsTransactionHandler;
import com.fieldnation.service.transaction.handlers.WorkorderListTransactionHandler;
import com.fieldnation.service.transaction.handlers.WorkorderRequestHandler;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.ISO8601;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderWebClient {

    private static final String API_REV = "/api/rest/v1";

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_TYPE_FORM_ENCODED = "application/x-www-form-urlencoded";

    public void requestList(Context context, WorkorderDataSelector selector, int page) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .key("Workorder" + selector.getCall() + "ListPage" + page)
                .handler(WorkorderListTransactionHandler.class)
                .handlerParams(WorkorderListTransactionHandler.generateParams(page, selector.getCall()))
                .request(new HttpJsonBuilder()
                                .method("GET")
                                .path(API_REV + "/workorder/" + selector.getCall())
                                .urlParams("?page=" + page)
                )
                .send();
    }

    public void details(Context context, long workorderId) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .key("WorkorderDetails:" + workorderId)
                .handler(WorkorderDetailsTransactionHandler.class)
                .handlerParams(WorkorderDetailsTransactionHandler.generateParams())
                .request(new HttpJsonBuilder()
                        .method("GET")
                        .path(API_REV + "/workorder/" + workorderId + "/details"))
                .send();
    }

    public void decline(Context context, long workorderId) throws ParseException {
        WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .key("WorkorderDecline:" + workorderId)
                .handler(WorkorderDeclineTransactionHandler.class)
                .handlerParams(WorkorderDeclineTransactionHandler.generateParams(workorderId))
                .request(
                        new HttpJsonBuilder()
                                .method("POST")
                                .header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_FORM_ENCODED)
                                .path(API_REV + "/workorder/" + workorderId + "/decline")
                )
                .send();
    }

    public static JsonObject setStatus(WorkorderStatus status, WorkorderSubstatus substatus, StatusIntent intent) throws ParseException {
        JsonObject t = new JsonObject();
        t.put("status/status", status.getValue());
        t.put("status/colorIntent", intent.getValue());
        t.put("status/subStatus", substatus.getValue());
        return t;
    }

    public void request(Context context, long workorderId, long expireInSeconds) throws ParseException {
        WebTransactionBuilder builder = WebTransactionBuilder.builder(context)
                .priority(WebTransaction.Priority.NORMAL)
                .key("WorkorderRequest:" + workorderId)
                .handler(WorkorderRequestHandler.class)
                .handlerParams(WorkorderRequestHandler.generateParams(workorderId));

        HttpJsonBuilder hb = new HttpJsonBuilder();
        hb.method("POST")
                .path("/api/rest/v1/workorder/" + workorderId + "/request")
                .header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_FORM_ENCODED);

        if (expireInSeconds != -1) {
            hb.body("expiration=" + expireInSeconds);
        }

        builder.request(hb).send();
    }

    public void confirmAssignment(Context context, long workorderId, String startStringIso8601, String endTimeIso8601) throws ParseException {
        WebTransactionBuilder builder = new WebTransactionBuilder(context);
        builder.priority(WebTransaction.Priority.NORMAL)
                .key("WorkorderConfirm:" + workorderId)
                .handler(WorkorderDetailsTransactionHandler.class)
                .handlerParams(WorkorderDeclineTransactionHandler.generateParams(workorderId))
                .request(
                        new HttpJsonBuilder()
                                .method("POST")
                                .path(API_REV + "/workorder/" + workorderId + "/assignment")
                                .header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_FORM_ENCODED)
                                .body("start_time=" + startStringIso8601 + "&end_time=" + endTimeIso8601)
                )
                .send();
    }

    public void checkin(Context context, long workorderId) throws ParseException {
        JsonObject status = setStatus(WorkorderStatus.ASSIGNED, WorkorderSubstatus.CHECKEDIN, StatusIntent.NORMAL);

        WebTransactionBuilder
                .builder(context)
                .handler(WorkorderDetailsTransactionHandler.class)
                .handlerParams(WorkorderDeclineTransactionHandler.generateParams(workorderId))
                .request(
                        new HttpJsonBuilder()
                                .method("POST")
                                .path(API_REV + "/workorder/" + workorderId + "/checkin")
                                .header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_FORM_ENCODED)
                                .body("checkin_time=" + ISO8601.now())
                )
                .transform(Transform.makeTransformQuery(
                        "Workorder",
                        workorderId + "",
                        Transform.Action.MERGE,
                        status.toByteArray()
                ))
                .send();
    }

//    public void uploadDeliverable(Context context, long workorderId, long deliverableSlotId, String localFilename) throws ParseException {
//        WebTransactionBuilder.builder(context)
//                .priority(WebTransaction.Priority.NORMAL)
//                .method("POST")
//                .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + deliverableSlotId)
//                .multipartFile("file", localFilename, null, "app/jpeg")
//                .handler(WorkorderListTransactionHandler.class)
//                .send();
//    }
}

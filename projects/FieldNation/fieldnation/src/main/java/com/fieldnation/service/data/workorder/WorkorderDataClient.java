package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.FileHelper;
import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.transfer.WorkorderTransfer;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionBuilder;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class WorkorderDataClient extends TopicClient implements WorkorderDataConstants {
    public static final String STAG = "WorkorderDataClient";
    public final String TAG = UniqueTag.makeTag(STAG);

    public WorkorderDataClient(Listener listener) {
        super(listener);
    }

    // list
    public static void requestList(Context context, WorkorderDataSelector selector, int page) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST);
        intent.putExtra(PARAM_LIST_SELECTOR, selector.getCall());
        intent.putExtra(PARAM_PAGE, page);
        context.startService(intent);
    }

    public boolean registerList(WorkorderDataSelector selector) {
        if (!isConnected())
            return false;

        Log.v(TAG, "registerWorkorderList");

        return register(PARAM_ACTION_LIST + "/" + selector.getCall(), TAG);
    }

    // details
    public static void detailsWebRequest(Context context, long workorderId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .key("Workorder/" + workorderId)
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/details"))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestDetails(Context context, long id) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DETAILS);
        intent.putExtra(PARAM_ID, id);
        context.startService(intent);
    }

    public boolean registerDetails(long id) {
        if (!isConnected())
            return false;

        return register(PARAM_ACTION_DETAILS + "/" + id, TAG);
    }

    // get signature
    public static void requestGetSignature(Context context, long workorderId, long signatureId) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_SIGNATURE_ID, signatureId);
        context.startService(intent);
    }

    public boolean registerGetSignature(long signatureId) {
        if (!isConnected())
            return false;

        return register(PARAM_ACTION_GET_SIGNATURE + "/" + signatureId, TAG);
    }

    // add signature json
    public static void requestAddSignatureJson(Context context, long workorderId, String name, String signatureJson) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .path("/api/rest/v1/workorder/" + workorderId + "/signature")
                            .body("signatureFormat=json"
                                    + "&printName=" + misc.escapeForURL(name)
                                    + "&signature=" + signatureJson))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeAddSignatureTransfer(name).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // complete signature
    public static void requestCompleteSignatureTaskJson(Context context, long workorderId, long taskId, String printName, String signatureJson) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/tasks/complete/" + taskId)
                            .body("print_name=" + misc.escapeForURL(printName)
                                    + "&signature_json=" + signatureJson))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCompletingTaskTransfer("signature", taskId, printName).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // complete workorder
    public static void requestComplete(Context context, long workorderId) {
        try {
            JsonObject _proc = new JsonObject();
            _proc.put("_proc.complete", "working");

            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/complete"))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeMarkCompleteTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // checkin workorder
    public static void requestCheckin(Context context, long workorderId) {
        Log.v(STAG, "requestCheckin");
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/1111/api/rest/v1/workorder/" + workorderId + "/checkin")
                            .body("checkin_time=" + ISO8601.now()))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCheckinTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestCheckin(Context context, long workorderId, Location location) {
        Log.v(STAG, "requestCheckin");
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/11111/api/rest/v1/workorder/" + workorderId + "/checkin")
                            .body("checkin_time=" + ISO8601.now()
                                    + "&gps_lat=" + location.getLatitude()
                                    + "&gps_lon=" + location.getLongitude()))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCheckinTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        requestDetails(context, workorderId);
    }

    // checkout
    public static void requestCheckout(Context context, long workorderId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/checkout")
                            .body("checkout_time=" + ISO8601.now()))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCheckoutTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestCheckout(Context context, long workorderId, Location location) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/checkout")
                            .body("checkout_time=" + ISO8601.now()
                                    + "&gps_lat=" + location.getLatitude()
                                    + "&gps_lon=" + location.getLongitude()))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCheckoutTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestSetClosingNotes(Context context, long workorderId, String closingNotes) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .key("Workorder/" + workorderId + "/closingNotes")
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/closing-notes")
                            .body("notes=" + (closingNotes == null ? "" : misc.escapeForURL(closingNotes))))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeClosingNotesTransfer(closingNotes).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestCheckout(Context context, long workorderId, int deviceCount) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/checkout")
                            .body("device_count=" + deviceCount
                                    + "&checkout_time=" + ISO8601.now()))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCheckoutTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestCheckout(Context context, long workorderId, int deviceCount, Location location) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/checkout")
                            .body("device_count=" + deviceCount
                                    + "&checkout_time=" + ISO8601.now()
                                    + "&gps_lat=" + location.getLatitude()
                                    + "&gps_lon=" + location.getLongitude()))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCheckoutTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // acknowledge hold
    public static void requestAcknowledgeHold(Context context, long workorderId) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/api/rest/v1/workorder/" + workorderId + "/acknowledge-hold"))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeAckHoldTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // counter offer
    public static void requestCounterOffer(Context context, long workorderId, boolean expires,
                                           String reason, int expiresAfterInSecond, Pay pay,
                                           Schedule schedule, Expense[] expenses) {
        String payload = "";
        // reason/expire
        if (expires)
            payload += "expires=true&expiresAfterInSecond=" + expiresAfterInSecond;
        else
            payload += "expires=false";

        if (!misc.isEmptyOrNull(reason)) {
            payload += "&providerExplanation=" + misc.escapeForURL(reason);
        }

        // pay counter
        if (pay != null) {
            if (pay.isPerDeviceRate()) {
                payload += "&payBasis=per_device";
                payload += "&payPerDevice=" + pay.getPerDevice();
                payload += "&maxDevices=" + pay.getMaxDevice();
            } else if (pay.isBlendedRate()) {
                payload += "&payBasis=blended";
                payload += "&hourlyRate=" + pay.getBlendedStartRate();
                payload += "&maxHours=" + pay.getBlendedFirstHours();
                payload += "&additionalHourRate=" + pay.getBlendedAdditionalRate();
                payload += "&additionalMaxHours=" + pay.getBlendedAdditionalHours();
            } else if (pay.isFixedRate()) {
                payload += "&payBasis=fixed";
                payload += "&fixedTotalAmount=" + pay.getFixedAmount();
            } else if (pay.isHourlyRate()) {
                payload += "&payBasis=per_hour";
                payload += "&hourlyRate=" + pay.getPerHour();
                payload += "&maxHours=" + pay.getMaxHour();
            }
        }
        // schedule counter
        if (schedule != null) {
            if (schedule.isExact()) {
                payload += "&startTime=" + schedule.getStartTime();
            } else {
                payload += "&startTime=" + schedule.getStartTime();
                payload += "&endTime=" + schedule.getEndTime();
            }
        }
        // expenses counter
        if (expenses != null && expenses.length > 0) {
            StringBuilder json = new StringBuilder();
            json.append("[");
            for (int i = 0; i < expenses.length; i++) {
                Expense expense = expenses[i];
                json.append("{\"description\":\"").append(expense.getDescription()).append("\",");
                json.append("\"price\":\"").append(expense.getPrice()).append("\"}");
            }
            json.append("]");

            payload += "&expenses=" + json.toString();
        }

        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/counter_offer")
                            .body(payload))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeCounterOfferTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // request
    public static void request(Context context, long workorderId, long expireInSeconds) {
        HttpJsonBuilder builder;
        try {
            builder = new HttpJsonBuilder()
                    .protocol("https")
                    .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                    .method("POST")
                    .path("/api/rest/v1/workorder/" + workorderId + "/request");

            if (expireInSeconds != -1) {
                builder.body("expiration=" + expireInSeconds);
            }
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(builder)
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeRequestTransfer().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void requestConfirmAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        try {
            JsonObject _proc = new JsonObject();
            _proc.put("_proc.confirm", "working");

            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(WorkorderTransactionHandler.class)
                    .handlerParams(WorkorderTransactionHandler.pDetails(workorderId))
                    .useAuth()
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .header(HttpJsonBuilder.HEADER_CONTENT_TYPE, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED)
                            .method("POST")
                            .path("/api/rest/v1/workorder/" + workorderId + "/assignment")
                            .body("start_time=" + startTimeIso8601 + "&end_time=" + endTimeIso8601))
                    .transform(Transform.makeTransformQuery(
                            PSO_WORKORDER,
                            workorderId,
                            "merges",
                            WorkorderTransfer.makeConfirmAssignment().getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Bundle
    public static void requestBundle(Context context, long bundleId) {
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        intent.putExtra(PARAM_ID, bundleId);
        context.startService(intent);
    }

    public boolean registerBundle(long bundleId) {
        if (!isConnected())
            return false;

        Log.v(TAG, "registerBundle");

        return register(PARAM_ACTION_GET_BUNDLE + "/" + bundleId, TAG);
    }

    public static void requestUploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, String filePath) {
        Log.v(STAG, "requestUploadDeliverable");
        Intent intent = new Intent(context, WorkorderDataService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_LOCAL_PATH, filePath);
        intent.putExtra(PARAM_FILE_NAME, filename);
        context.startService(intent);
    }

    public static void requestUploadDeliverable(final Context context, final long workorderId, final long uploadSlotId, Intent data) {
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fileReady(String filename, File file) {
                requestUploadDeliverable(context, workorderId, uploadSlotId, filename, file.getPath());
            }

            @Override
            public void fail(String reason) {
                Log.v("WorkorderDataClient.requestUploadDeliverable", reason);
            }
        });
    }

    public static void requestDeleteDeliverable(Context context, long workorderId, long workorderUploadId, String filename) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(WebTransaction.Priority.HIGH)
                    .handler(DeliverableDeleteTransactionHandler.class)
                    .handlerParams(DeliverableDeleteTransactionHandler.generateParams(workorderId))
                    .useAuth()
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("DELETE")
                                    .path("/api/rest/v1/workorder/" + workorderId + "/deliverables/" + workorderUploadId))
                    .transform(
                            Transform.makeTransformQuery(
                                    PSO_WORKORDER,
                                    workorderId,
                                    "merges",
                                    WorkorderTransfer.makeDeleteDeliverable(workorderUploadId, filename).getBytes()))
                    .send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*-******************************-*/
    /*-         Listener             -*/
    /*-******************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(PARAM_ACTION_LIST)) {
                preOnWorkorderList((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_DETAILS)) {
                preOnDetails((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_GET_SIGNATURE)) {
                preOnGetSignature((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_GET_BUNDLE)) {
                preOnGetBundle((Bundle) payload);
            }
        }

        // list
        protected void preOnWorkorderList(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, List<Workorder>>() {
                private WorkorderDataSelector selector;
                private int page;

                @Override
                protected List<Workorder> doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        selector = WorkorderDataSelector.fromName(bundle.getString(PARAM_LIST_SELECTOR));
                        page = bundle.getInt(PARAM_PAGE);
                        List<Workorder> list = new LinkedList<>();
                        JsonArray ja = new JsonArray(bundle.getByteArray(PARAM_DATA));
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Workorder.fromJson(ja.getJsonObject(i)));
                        }
                        return list;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<Workorder> workorders) {
                    onWorkorderList(workorders, selector, page);
                }
            }.executeEx(payload);
        }

        public void onWorkorderList(List<Workorder> list, WorkorderDataSelector selector, int page) {
        }

        // details
        protected void preOnDetails(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, Workorder>() {
                @Override
                protected Workorder doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        return Workorder.fromJson(new JsonObject(bundle.getByteArray(PARAM_DATA)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Workorder workorder) {
                    if (workorder.getTransfer() != null)
                        Log.v(STAG, workorder.getTransfer().toJson().display());
                    else
                        Log.v(STAG, "no _proc");

                    onDetails(workorder);
                }
            }.executeEx(payload);
        }

        public void onDetails(Workorder workorder) {
        }

        // get signature
        private void preOnGetSignature(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, Signature>() {

                @Override
                protected Signature doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        return Signature.fromJson(new JsonObject(bundle.getByteArray(PARAM_DATA)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Signature signature) {
                    onGetSignature(signature);
                }
            }.executeEx(payload);
        }

        public void onGetSignature(Signature signature) {
        }

        private void preOnGetBundle(Bundle payload) {
            new AsyncTaskEx<Bundle, Object, com.fieldnation.data.workorder.Bundle>() {
                @Override
                protected com.fieldnation.data.workorder.Bundle doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    try {
                        return com.fieldnation.data.workorder.Bundle.fromJson(new JsonObject(bundle.getByteArray(PARAM_DATA)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(com.fieldnation.data.workorder.Bundle bundle) {
                    onGetBundle(bundle);
                }
            }.executeEx(payload);
        }

        public void onGetBundle(com.fieldnation.data.workorder.Bundle bundle) {
        }
    }
}

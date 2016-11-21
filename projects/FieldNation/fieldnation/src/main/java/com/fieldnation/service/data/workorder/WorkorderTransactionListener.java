package com.fieldnation.service.data.workorder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.tracker.UploadTrackerClient;
import com.fieldnation.service.transaction.TransactionException;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionListener;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderTransactionListener extends WebTransactionListener implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionListener";

    // parameter generators
    public static byte[] pDetails(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pDetails");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pList(int page, WorkorderDataSelector selector) {
        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("page", page);
            obj.put("selector", selector.ordinal());
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pAction(long workorderId, String action) {
        try {
            JsonObject obj = new JsonObject("action", "pAction");
            obj.put("workorderId", workorderId);
            obj.put("param", action);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pTimeLog(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pTimeLog");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pCheckIn(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pCheckIn");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pCheckOut(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pCheckOut");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pActionCreateShipment(long workorderId, String description, boolean isToSite,
                                               String carrier, String carrierName, String trackingNumber, long taskId) {
        try {
            JsonObject obj = new JsonObject("action", "pActionCreateShipment");
            obj.put("workorderId", workorderId);
            obj.put("description", description);
            obj.put("isToSite", isToSite);
            obj.put("carrier", carrier);
            obj.put("carrierName", carrierName);
            obj.put("trackingNumber", trackingNumber);
            obj.put("taskId", taskId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pActionCompleteShipmentTask(long workorderId, long shipmentId, long taskId) {
        try {
            JsonObject obj = new JsonObject("action", "pActionCompleteShipmentTask");
            obj.put("workorderId", workorderId);
            obj.put("shipmentId", shipmentId);
            obj.put("taskId", taskId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pActionRequest(long workorderId, long expireInSeconds, String startTime, String endTime, String note) {
        try {
            JsonObject obj = new JsonObject("action", "pActionRequest");
            obj.put("workorderId", workorderId);
            obj.put("expireInSeconds", expireInSeconds);
            obj.put("startTime", startTime);
            obj.put("endTime", endTime);
            obj.put("note", note);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pAssignment(long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
        try {
            JsonObject obj = new JsonObject("action", "pAssignment");
            obj.put("workorderId", workorderId);
            obj.put("startTimeIso8601", startTimeIso8601);
            obj.put("endTimeIso8601", endTimeIso8601);
            obj.put("note", note);
            obj.put("isEditEta", isEditEta);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pCounterOffer(long workorderId, boolean expires,
                                       String reason, int expiresAfterInSecond, Pay pay,
                                       Schedule schedule, Expense[] expenses) {
        try {
            JsonObject obj = new JsonObject("action", "pCounterOffer");
            obj.put("workorderId", workorderId);
            obj.put("expires", expires);
            obj.put("reason", reason);
            obj.put("expiresAfterInSecond", expiresAfterInSecond);

            if (pay != null)
                obj.put("pay", pay.toJson());

            if (schedule != null)
                obj.put("schedule", schedule.toJson());

            if (expenses != null && expenses.length > 0) {
                JsonArray ja = new JsonArray();
                for (Expense expense : expenses) {
                    ja.add(expense.toJson());
                }
                obj.put("expenses", ja);
            }
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static byte[] pGetSignature(long workorderId, long signatureId) {
        try {
            JsonObject obj = new JsonObject("action", "pGetSignature");
            obj.put("workorderId", workorderId);
            obj.put("signatureId", signatureId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pMessageList(long workorderId, boolean isRead) {
        try {
            JsonObject obj = new JsonObject("action", "pMessageList");
            obj.put("workorderId", workorderId);
            obj.put("isRead", isRead);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pAlertList(long workorderId, boolean isRead) {
        try {
            JsonObject obj = new JsonObject("action", "pAlertList");
            obj.put("workorderId", workorderId);
            obj.put("isRead", isRead);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pTaskList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pTaskList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pGetBundle(long bundleId) {
        try {
            JsonObject obj = new JsonObject();
            obj.put("action", "pGetBundle");
            obj.put("bundleId", bundleId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static byte[] pUploadDeliverable(long workorderId, long slotId, String filename) {
        try {
            JsonObject obj = new JsonObject("action", "pUploadDeliverable");
            obj.put("workorderId", workorderId);
            obj.put("slotId", slotId);
            obj.put("filename", filename);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }


    public static byte[] pRating(int satisfactionRating, int scopeRating,
                                 int respectRating, int respectComment, boolean recommendBuyer, String otherComments, long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pRating");
            obj.put("satisfactionRating", satisfactionRating);
            obj.put("scopeRating", scopeRating);
            obj.put("respectRating", respectRating);
            obj.put("respectComment", respectComment);
            obj.put("recommendBuyer", recommendBuyer);
            obj.put("otherComments", otherComments);
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }


    /*-*********************************-*/
    /*-             Start               -*/
    /*-*********************************-*/
    @Override
    public Result onStart(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pUploadDeliverable":
                    return onStartUploadDeliverable(context, transaction, params);

            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result onStartUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, false, false);

        UploadTrackerClient.uploadStarted(context);

        return Result.CONTINUE;
    }
    /*-************************************-*/
    /*-             Progress               -*/
    /*-************************************-*/

    @Override
    public void onProgress(Context context, WebTransaction transaction, long pos, long size, long time) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pUploadDeliverable":
                    onProgressUploadDeliverable(context, transaction, params, pos, size, time);
                    break;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private Result onProgressUploadDeliverable(Context context, WebTransaction transaction, JsonObject params, long pos, long size, long time) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverableProgress(context, workorderId, slotId, filename, pos, size, time);

        return Result.CONTINUE;
    }

    /*-**********************************-*/
    /*-             Result               -*/
    /*-**********************************-*/
    @Override
    public Result onComplete(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDetails":
                    return onDetails(context, result, transaction, params, httpResult, throwable);
                case "pList":
                    return onList(context, result, transaction, params, httpResult, throwable);
                case "pGetSignature":
                    return onGetSignature(context, result, transaction, params, httpResult, throwable);
                case "pAction":
                    return onAction(context, result, transaction, params, httpResult, throwable);
                case "pTimeLog":
                    return onTimeLog(context, result, transaction, params, httpResult, throwable);
                case "pCheckIn":
                    return onCheckIn(context, result, transaction, params, httpResult, throwable);
                case "pCheckOut":
                    return onCheckOut(context, result, transaction, params, httpResult, throwable);
                case "pActionRequest":
                    return onActionRequest(context, result, transaction, params, httpResult, throwable);
                case "pMessageList":
                    return onMessageList(context, result, transaction, params, httpResult, throwable);
                case "pAlertList":
                    return onAlertList(context, result, transaction, params, httpResult, throwable);
                case "pTaskList":
                    return onTaskList(context, result, transaction, params, httpResult, throwable);
                case "pGetBundle":
                    return onSuccessGetBundle(context, result, transaction, params, httpResult, throwable);
                case "pUploadDeliverable":
                    return onSuccessUploadDeliverable(context, result, transaction, params, httpResult, throwable);
                case "pAssignment":
                    return onSuccessAssignment(context, result, transaction, params, httpResult, throwable);
                case "pActionCreateShipment":
                    return onSuccessCreateShipment(context, result, transaction, params, httpResult, throwable);
                case "pActionSuccessShipmentTask":
                    return onSuccessShipmentTask(context, result, transaction, params, httpResult, throwable);
                case "pCounterOffer":
                    return onCounterOffer(context, result, transaction, params, httpResult, throwable);
                case "pRating":
                    return onSuccessRating(context, result, transaction, params, httpResult, throwable);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    // individual commands
    private Result onDetails(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onDetails " + transaction.getId());
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = httpResult.getByteArray();

        Log.v(TAG, "onDetails workorderId:" + workorderId);
        if (result != Result.CONTINUE) {
            ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            if (httpResult.getResponseCode() == 400 && !httpResult.isFile() && httpResult.getString() != null
                    && httpResult.getString().equals("You don't have permission to see this workorder")) {
                return Result.DELETE;
            } else {
                Log.v(TAG, new TransactionException(httpResult.getString()));
                return Result.DELETE;
            }

        } else { // if result == CONTINUE
            JsonObject workorder = new JsonObject(workorderData);
            Transform.applyTransform(workorder, PSO_WORKORDER, workorderId);
            WorkorderDispatch.get(context, workorder, workorderId, false, transaction.isSync(), false);
            if (workorder.has("_action"))
                Log.v(TAG, "onDetails _action=" + workorder.get("_action"));
            // store it in the store
            StoredObject.put(context, App.getProfileId(), PSO_WORKORDER, workorderId, workorderData);
            return Result.CONTINUE;
        }
    }

    private Result onList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) {
        Stopwatch watch = new Stopwatch(true);
        Log.v(TAG, "onSuccessList");
        // get the basics, send out the event
        int page = 0;
        WorkorderDataSelector selector = null;

        try {
            if (result != Result.CONTINUE) {
                WorkorderDispatch.list(context, null, params.getInt("page"), WorkorderDataSelector.values()[params.getInt("selector")], true, transaction.isSync(), false);
                return result;
            }

            page = params.getInt("page");
            selector = WorkorderDataSelector.values()[params.getInt("selector")];
            byte[] bdata = httpResult.getByteArray();
            Log.v(TAG, "onSuccessList:{selector:" + selector + ", page: " + page + "}");

            StoredObject.put(context, App.getProfileId(), PSO_WORKORDER_LIST + selector, page, bdata);

            Log.v(TAG, "onSuccessList 1");
            JsonArray ja = new JsonArray(bdata);
            Log.v(TAG, "onSuccessList 2");
            for (int i = 0; i < ja.size(); i++) {
                JsonObject json = ja.getJsonObject(i);

                Transform.applyTransform(json, PSO_WORKORDER, json.getLong("workorderId"));
            }
            Log.v(TAG, "onSuccessList 3");

            WorkorderDispatch.list(context, ja, page, selector, false, transaction.isSync(), false);

            Log.v(TAG, "onSuccessList 4");

            return Result.CONTINUE;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        Log.v(TAG, "onSuccessList time: " + watch.finish());
        return Result.RETRY;
    }

    private Result onGetSignature(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");
        byte[] data = httpResult.getByteArray();

        if (result != Result.CONTINUE) {
            // TODO toast error message?
            WorkorderDispatch.signature(context, null, workorderId, signatureId, true, transaction.isSync());
            return result;
        } else {
            WorkorderDispatch.signature(context, new JsonObject(data), workorderId, signatureId, false, transaction.isSync());

            //store the signature data
            StoredObject.put(context, App.getProfileId(), PSO_SIGNATURE, signatureId, data);

            return Result.CONTINUE;
        }
    }

    private Result onAction(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        if (result != Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, params.getString("param"), true);
            WorkorderClient.get(context, workorderId, true, false);

            if (!httpResult.isFile() && httpResult.getByteArray().length < 1024) {
                ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            } else {
                ToastClient.toast(context, "Action failed on work order " + workorderId + " with code " + httpResult.getResponseCode(), Toast.LENGTH_LONG);
            }
            return result;
        }

        WorkorderDispatch.action(context, workorderId, action, false);
        WorkorderClient.listTasks(context, workorderId, false);

        if (action.equals("acknowledge-hold")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("closing-notes")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("complete")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("decline")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("delete_request")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("DELETE_LOG")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("incomplete")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("messages/new")) {
            WorkorderClient.listMessages(context, workorderId, false, false);

        } else if (action.equals("pay-change")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (action.equals("ready")) {
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else {
            WorkorderClient.get(context, workorderId, false);
        }

        return Result.CONTINUE;
    }

    private Result onTimeLog(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            Intent intent = new Intent(context, WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorderId);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (intent != null) {
                PendingIntent pi = PendingIntent.getActivity(App.get(), 0, intent, 0);
                ToastClient.snackbar(App.get(), httpResult.getString(), "VIEW", pi, Snackbar.LENGTH_INDEFINITE);
            }

            WorkorderDispatch.action(context, workorderId, "pTimeLog", true);
            return result;
        } else {

            WorkorderDispatch.action(context, workorderId, "pTimeLog", false);
            return onDetails(context, result, transaction, params, httpResult, throwable);
        }
    }

    private Result onCheckIn(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onCheckIn");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "checkin", true);
            ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            return result;
        }

        try {

            WorkorderDispatch.action(context, workorderId, "checkin", false);
            WorkorderClient.listTasks(context, workorderId, false);
            return onDetails(context, result, transaction, params, httpResult, throwable);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            WorkorderClient.get(context, workorderId, false);
            try {
                Intent intent = WorkorderActivity.makeIntentShow(context, workorderId);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                ToastClient.snackbar(context, "Checkin failed: " + httpResult.getString(), "VIEW", pendingIntent, Snackbar.LENGTH_LONG);
                WorkorderDispatch.action(context, workorderId, "checkin", true);
                return Result.DELETE;
            } catch (Exception ex1) {
                Log.v(TAG, ex1);
            }
            WorkorderDispatch.action(context, workorderId, "checkin", true);
            return Result.DELETE;
        }
    }

    private Result onCheckOut(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onCheckOut");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "checkout", true);
            ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            return result;
        }

        try {
            WorkorderClient.listTasks(context, workorderId, false);
            WorkorderDispatch.action(context, workorderId, "checkout", false);
            return onDetails(context, result, transaction, params, httpResult, throwable);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            WorkorderClient.get(context, workorderId, false);
            try {
                Intent intent = WorkorderActivity.makeIntentShow(context, workorderId);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                ToastClient.snackbar(context, "Checkout failed: " + httpResult.getString(), "VIEW", pendingIntent, Snackbar.LENGTH_LONG);
                Log.v(TAG, "Sent snackbar");
                WorkorderDispatch.action(context, workorderId, "checkout", true);
                return Result.DELETE;
            } catch (Exception ex1) {
                Log.v(TAG, ex1);
            }
            WorkorderDispatch.action(context, workorderId, "checkout", true);
            return Result.DELETE;
        }
    }

    private Result onActionRequest(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onActionRequest");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            long expireInSeconds = params.getLong("expireInSeconds");
            String startTime = params.getString("startTime");
            String endTime = params.getString("endTime");
            String note = params.getString("note");

            WorkorderDispatch.action(context, workorderId, "request", true);

            Intent intent = WorkorderTransactionBuilder.actionRequestIntent(context, workorderId, expireInSeconds, startTime, endTime, note);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

            try {
                ToastClient.snackbar(context, httpResult.getString(),
                        "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            } catch (Exception ex) {
                ToastClient.snackbar(context, "Unable to request work order. Please check your connection.",
                        "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            }

            return result;

        } else {

            WorkorderDispatch.action(context, workorderId, "request", false);
            ToastClient.snackbar(context, "Success! You have requested this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);

            return onDetails(context, result, transaction, params, httpResult, throwable);
        }
    }

    private Result onMessageList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessMessageList");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            WorkorderDispatch.listMessages(context, workorderId, null, true, transaction.isSync());
            return result;
        } else {
            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.listMessages(context, workorderId, new JsonArray(data), false, transaction.isSync());

            if (params.has("isRead") && params.getBoolean("isRead")) {
                GlobalTopicClient.profileInvalid(context);
                WorkorderClient.get(context, workorderId, false);
            }

            StoredObject.put(context, App.getProfileId(), PSO_MESSAGE_LIST, workorderId, data);

            return Result.CONTINUE;
        }
    }

    private Result onAlertList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessAlertList");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            WorkorderDispatch.listAlerts(context, workorderId, null, true, transaction.isSync());
            return result;
        } else {

            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.listAlerts(context, workorderId, new JsonArray(data), false, transaction.isSync());

            if (params.has("isRead") && params.getBoolean("isRead")) {
                GlobalTopicClient.profileInvalid(context);
                WorkorderClient.get(context, workorderId, false);
            }

            StoredObject.put(context, App.getProfileId(), PSO_ALERT_LIST, workorderId, data);

            return Result.CONTINUE;
        }
    }

    private Result onTaskList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessTaskList");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            WorkorderDispatch.listTasks(context, workorderId, null, true, transaction.isSync());
            return result;
        } else {
            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.listTasks(context, workorderId, new JsonArray(data), false, transaction.isSync());
            StoredObject.put(context, App.getProfileId(), PSO_TASK_LIST, workorderId, data);

            return Result.CONTINUE;
        }
    }

    private Result onCounterOffer(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessCounterOffer");
        long workorderId = params.getLong("workorderId");

        if (result != Result.CONTINUE) {
            boolean expires = params.getBoolean("expires");
            String reason = params.getString("reason");
            int expiresAfterInSecond = params.getInt("expiresAfterInSecond");

            Pay pay = null;
            if (params.has("pay")) {
                pay = Pay.fromJson(params.getJsonObject("pay"));
            }

            Schedule schedule = null;
            if (params.has("schedule")) {
                schedule = Schedule.fromJson(params.getJsonObject("schedule"));
            }

            Expense[] expenses = null;
            if (params.has("expenses")) {
                JsonArray ja = params.getJsonArray("expenses");
                if (ja != null && ja.size() > 0) {
                    expenses = new Expense[ja.size()];
                    for (int i = 0; i < ja.size(); i++) {
                        expenses[i] = Expense.fromJson(ja.getJsonObject(i));
                    }
                }
            }

            Intent intent = WorkorderTransactionBuilder.actionCounterOfferIntent(context, workorderId, expires, reason, expiresAfterInSecond, pay, schedule, expenses);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

            try {
                ToastClient.snackbar(context, httpResult.getString(),
                        "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            } catch (Exception ex) {
                ToastClient.snackbar(context, "Could not send counter offer. Please check your connection.",
                        "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            }

            return result;
        } else {

            WorkorderDispatch.action(context, workorderId, "counter_offer", false);
            ToastClient.snackbar(context, "Success! Your counter offer has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);
            WorkorderClient.get(context, workorderId, false);

            return Result.CONTINUE;
        }
    }

    private Result onSuccessRating(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) {
        ToastClient.snackbar(context, "Success! Your rating has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }

    private Result onSuccessShipmentTask(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessShipmentTask");
        long workorderId = params.getLong("workorderId");
        long shipmentId = params.getLong("shipmentId");
        long taskId = params.getLong("taskId");

        WorkorderDispatch.action(context, workorderId, "complete_shipment_task", false);

        ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

        WorkorderClient.get(context, workorderId, false);
        WorkorderClient.listTasks(context, workorderId, false);

        return Result.CONTINUE;
    }

    private Result onSuccessAssignment(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessAssignment");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "assignment", false);

        boolean isEditEta = params.getBoolean("isEditEta");

        if (!isEditEta)
            ToastClient.snackbar(context, "Success! You have accepted this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);
        else
            ToastClient.snackbar(context, "Success! Edited ETA is saved.", "DISMISS", null, Snackbar.LENGTH_LONG);


        return onDetails(context, result, transaction, params, httpResult, throwable);
    }

    private Result onSuccessCreateShipment(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessCreateShipment");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "create_shipment", false);

        ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

        WorkorderClient.get(context, workorderId, false);

        return Result.CONTINUE;
    }

    public Result onSuccessGetBundle(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long bundleId = params.getLong("bundleId");
        byte[] data = httpResult.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_BUNDLE, bundleId, data);

        WorkorderDispatch.bundle(context, new JsonObject(data), bundleId, false, transaction.isSync());

        return Result.CONTINUE;
    }

    private Result onSuccessUploadDeliverable(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, true, false);

        WorkorderClient.get(context, workorderId, false);

        UploadTrackerClient.uploadSuccess(context);

        return Result.CONTINUE;
    }

    /*-********************************-*/
    /*-             Fail               -*/
    /*-********************************-*/
    public Result onFail(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) {
        try {
            try {
                if (!httpResult.isFile() && httpResult.getByteArray().length < 1000)
                    Log.v(TAG, "Error message: " + httpResult.getString());
            } catch (Exception ex) {
            }

            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pTaskList":
                    return onFailTaskList(context, result, transaction, httpResult, throwable);
                case "pGetBundle":
                    return onFailGetBundle(context, result, transaction, httpResult, throwable);
                case "pUploadDeliverable":
                    return onFailUploadDeliverable(context, result, transaction, httpResult, throwable);
                case "pAssignment":
                    return onFailAssignment(context, result, transaction, params, httpResult);
                case "pActionCreateShipment":
                    return onFailCreateShipment(context, result, transaction, params, httpResult);
                case "pActionCompleteShipmentTask":
                    return onFailShipmentTask(context, result, transaction, params, httpResult);
                case "pCounterOffer":
                    return onFailCounterOffer(context, result, transaction, params, httpResult);
                case "pRating":
                    return onFailRating(context, result, transaction, httpResult, params);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return result;
    }

    private Result onFailGetBundle(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) throws ParseException {
        JsonObject params = new JsonObject(transaction.getListenerParams());

        WorkorderDispatch.bundle(context, null, params.getLong("bundleId"), true, transaction.isSync());

        return result;
    }

    private Result onFailUploadDeliverable(Context context, Result result, WebTransaction transaction, HttpResult httpResult, Throwable throwable) throws ParseException {
        JsonObject params = new JsonObject(transaction.getListenerParams());

        UploadTrackerClient.uploadFailed(context, params.getLong("workorderId"));
        if (throwable != null && throwable instanceof SecurityException) {
            ToastClient.toast(context, "Failed to upload file. " + params.getString("filename") + " Read permission denied. Please try again", Toast.LENGTH_LONG);
        } else {
            ToastClient.toast(context, "Failed to upload file. " + params.getString("filename") + " Please try again", Toast.LENGTH_LONG);
        }
        WorkorderDispatch.uploadDeliverable(context, params.getLong("workorderId"), params.getLong("slotId"), params.getString("filename"), false, true);


        return result;
    }

    private Result onFailAssignment(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult) throws ParseException {
        Log.v(TAG, "onFailAssignment");

        long workorderId = params.getLong("workorderId");
        String startTimeIso8601 = params.getString("startTimeIso8601");
        String endTimeIso8601 = params.getString("endTimeIso8601");
        String note = params.getString("note");
        boolean isEditEta = params.getBoolean("isEditEta");

        WorkorderDispatch.action(context, workorderId, "assignment", true);

        Intent intent = WorkorderTransactionBuilder.actionConfirmAssignmentIntent(context, workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        try {
            ToastClient.snackbar(context, httpResult.getString(),
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
        } catch (Exception ex) {
            ToastClient.snackbar(context, "Unable to accept work order. " + httpResult.getString(),
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
        }

        return result;
    }

    private Result onFailCreateShipment(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult) throws ParseException {
        Log.v(TAG, "onFailCreateShipment");

        long workorderId = params.getLong("workorderId");
        String description = params.getString("description");
        boolean isToSite = params.getBoolean("isToSite");
        String carrier = params.getString("carrier");
        String carrierName = params.getString("carrierName");
        String trackingNumber = params.getString("trackingNumber");
        long taskId = params.getLong("taskId");

        WorkorderDispatch.action(context, workorderId, "request", true);

        Intent intent = null;
        if (taskId == -1)
            intent = WorkorderTransactionBuilder.postShipmentIntent(context, workorderId, description, isToSite, carrier, carrierName, trackingNumber);
        else
            intent = WorkorderTransactionBuilder.postShipmentIntent(context, workorderId, description, isToSite, carrier, carrierName, trackingNumber, taskId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.toast(App.get(), "Could not add your shipment. Please check your connection.", Toast.LENGTH_LONG);

        // TODO the snackbar is not appearing
        ToastClient.snackbar(context, "Could not add your shipment. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return result;
    }

    private Result onFailShipmentTask(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult) throws ParseException {
        Log.v(TAG, "onFailShipmentTask");

        long workorderId = params.getLong("workorderId");
        long shipmentId = params.getLong("shipmentId");
        long taskId = params.getLong("taskId");

        WorkorderDispatch.action(context, workorderId, "request", true);

        Intent intent = WorkorderTransactionBuilder.actionCompleteShipmentTaskIntent(context, workorderId, shipmentId, taskId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.snackbar(context, "Could not complete your shipment. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return result;
    }

    private Result onFailRating(Context context, Result result, WebTransaction transaction, HttpResult httpResult, JsonObject params) {
        try {
            Intent intent = WorkorderTransactionBuilder.actionPostRatingIntent(context,
                    params.getLong("workorderId"), params.getInt("satisfactionRating"), params.getInt("scopeRating"), params.getInt("respectRating"),
                    params.getInt("respectComment"), params.getBoolean("recommendBuyer"), params.getString("otherComments"));

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


            ToastClient.snackbar(context, "Could not send your rating. Please check your connection.",
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        } catch (Exception ex) {
            Log.v(TAG, ex);
            ToastClient.snackbar(context, "Failed. Your rating could not be sent.", Toast.LENGTH_LONG);
        }
        return result;
    }
}

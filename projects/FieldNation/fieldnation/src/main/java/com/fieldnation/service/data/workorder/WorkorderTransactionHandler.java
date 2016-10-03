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
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.text.ParseException;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderTransactionHandler extends WebTransactionHandler implements WorkorderConstants {
    private static final String TAG = "WorkorderTransactionHandler";

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
    public Result handleStart(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pUploadDeliverable":
                    return handleStartUploadDeliverable(context, transaction, params);

            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleStartUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, false, false);

        UploadTrackerClient.uploadStarted(context);

        return Result.CONTINUE;
    }

    /*-**********************************-*/
    /*-             Requeue              -*/
    /*-**********************************-*/

/*
    @Override
    public Result handleRequeued(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pUploadDeliverable":
                    return handleRequeueUploadDeliverable(context, transaction, params);

            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleRequeueUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        UploadTrackerClient.uploadRequeued(context);
        return Result.CONTINUE;
    }
*/

    /*-**********************************-*/
    /*-             Result               -*/
    /*-**********************************-*/
    @Override
    public Result handleResult(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDetails":
                    return handleDetails(context, transaction, params, resultData);
                case "pList":
                    return handleList(context, transaction, params, resultData);
                case "pGetSignature":
                    return handleGetSignature(context, transaction, params, resultData);
                case "pAction":
                    return handleAction(context, transaction, params, resultData);
                case "pTimeLog":
                    WorkorderDispatch.action(context, params.getLong("workorderId"), action, false);
                    return handleDetails(context, transaction, params, resultData);
                case "pCheckIn":
                    return handleCheckIn(context, transaction, params, resultData);
                case "pCheckOut":
                    return handleCheckOut(context, transaction, params, resultData);
                case "pActionRequest":
                    return handleActionRequest(context, transaction, params, resultData);
                case "pMessageList":
                    return handleMessageList(context, transaction, params, resultData);
                case "pAlertList":
                    return handleAlertList(context, transaction, params, resultData);
                case "pTaskList":
                    return handleTaskList(context, transaction, params, resultData);
                case "pGetBundle":
                    return handleGetBundle(context, transaction, params, resultData);
                case "pUploadDeliverable":
                    return handleFinishUploadDeliverable(context, transaction, params);
                case "pAssignment":
                    return handleConfirmAssignment(context, transaction, params, resultData);
                case "pActionCreateShipment":
                    return handleCreateShipment(context, transaction, params, resultData);
                case "pActionCompleteShipmentTask":
                    return handleCompleteShipmentTask(context, transaction, params, resultData);
                case "pCounterOffer":
                    return handleCounterOffer(context, transaction, params, resultData);
                case "pRating":
                    return handleRating(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleCounterOffer(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCounterOffer");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "counter_offer", false);
        ToastClient.snackbar(context, "Success! Your counter offer has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);
        WorkorderClient.get(context, workorderId, false);

        return Result.CONTINUE;
    }

    private Result handleRating(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        ToastClient.snackbar(context, "Success! Your rating has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }

    private Result handleCompleteShipmentTask(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCompleteShipmentTask");
        long workorderId = params.getLong("workorderId");
        long shipmentId = params.getLong("shipmentId");
        long taskId = params.getLong("taskId");

        WorkorderDispatch.action(context, workorderId, "complete_shipment_task", false);

        ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

        WorkorderClient.get(context, workorderId, false);
        WorkorderClient.listTasks(context, workorderId, false);

        return Result.CONTINUE;
    }

    private Result handleConfirmAssignment(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleConfirmAssignment");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "assignment", false);

        boolean isEditEta = params.getBoolean("isEditEta");

        if (!isEditEta)
            ToastClient.snackbar(context, "Success! You have accepted this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);
        else
            ToastClient.snackbar(context, "Success! Edited ETA is saved.", "DISMISS", null, Snackbar.LENGTH_LONG);


        return handleDetails(context, transaction, params, resultData);
    }

    private Result handleCreateShipment(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCreateShiptment");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "create_shipment", false);

        ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

        WorkorderClient.get(context, workorderId, false);

        return Result.CONTINUE;
    }

    private Result handleCheckIn(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCheckIn");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "checkin", false);
        try {
            WorkorderClient.listTasks(context, workorderId, false);
            return handleDetails(context, transaction, params, resultData);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            WorkorderClient.get(context, workorderId, false);
            try {
                Intent intent = WorkorderActivity.makeIntentShow(context, workorderId);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                ToastClient.snackbar(context, "Checkin failed: " + resultData.getString(), "VIEW", pendingIntent, Snackbar.LENGTH_LONG);
                return Result.DELETE;
            } catch (Exception ex1) {
                Log.v(TAG, ex1);
            }
            return Result.DELETE;
        }
    }

    private Result handleCheckOut(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCheckOut");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "checkout", false);
        try {
            WorkorderClient.listTasks(context, workorderId, false);
            return handleDetails(context, transaction, params, resultData);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            WorkorderClient.get(context, workorderId, false);
            try {
                Intent intent = WorkorderActivity.makeIntentShow(context, workorderId);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                ToastClient.snackbar(context, "Checkout failed: " + resultData.getString(), "VIEW", pendingIntent, Snackbar.LENGTH_LONG);
                Log.v(TAG, "Sent snackbar");
                return Result.DELETE;
            } catch (Exception ex1) {
                Log.v(TAG, ex1);
            }
            return Result.DELETE;
        }
    }

    private Result handleAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        WorkorderDispatch.action(context, workorderId, action, false);
        WorkorderClient.listTasks(context, workorderId, false);

        if (action.equals("acknowledge-hold")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("closing-notes")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("complete")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("decline")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("delete_request")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("DELETE_LOG")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("incomplete")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("messages/new")) {
            WorkorderClient.listMessages(context, workorderId, false, false);
        } else if (action.equals("pay-change")) {
            return handleDetails(context, transaction, params, resultData);
        } else if (action.equals("ready")) {
            return handleDetails(context, transaction, params, resultData);
        } else {
            WorkorderClient.get(context, workorderId, false);
        }

        return Result.CONTINUE;
    }

    private Result handleActionRequest(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleActionRequest");

        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "request", false);

        ToastClient.snackbar(context, "Success! You have requested this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return handleDetails(context, transaction, params, resultData);
    }

    private Result handleList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) {
        Stopwatch watch = new Stopwatch(true);
        Log.v(TAG, "handleList");
        // get the basics, send out the event
        int page = 0;
        WorkorderDataSelector selector = null;

        try {
            page = params.getInt("page");
            selector = WorkorderDataSelector.values()[params.getInt("selector")];
            byte[] bdata = resultData.getByteArray();
            Log.v(TAG, "handleList:{selector:" + selector + ", page: " + page + "}");

            StoredObject.put(context, App.getProfileId(), PSO_WORKORDER_LIST + selector, page, bdata);

            Log.v(TAG, "handleList 1");
            JsonArray ja = new JsonArray(bdata);
            Log.v(TAG, "handleList 2");
            for (int i = 0; i < ja.size(); i++) {
                JsonObject json = ja.getJsonObject(i);

                Transform.applyTransform(json, PSO_WORKORDER, json.getLong("workorderId"));
            }
            Log.v(TAG, "handleList 3");

            WorkorderDispatch.list(context, ja, page, selector, false, transaction.isSync(), false);

            Log.v(TAG, "handleList 4");

            return Result.CONTINUE;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        Log.v(TAG, "handleList time: " + watch.finish());
        return Result.REQUEUE;
    }

    private Result handleMessageList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleMessageList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listMessages(context, workorderId, new JsonArray(data), false, transaction.isSync());

        if (params.has("isRead") && params.getBoolean("isRead")) {
            GlobalTopicClient.profileInvalid(context);
            WorkorderClient.get(context, workorderId, false);
        }

        StoredObject.put(context, App.getProfileId(), PSO_MESSAGE_LIST, workorderId, data);

        return Result.CONTINUE;
    }

    private Result handleAlertList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleAlertList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listAlerts(context, workorderId, new JsonArray(data), false, transaction.isSync());

        if (params.has("isRead") && params.getBoolean("isRead")) {
            GlobalTopicClient.profileInvalid(context);
            WorkorderClient.get(context, workorderId, false);
        }

        StoredObject.put(context, App.getProfileId(), PSO_ALERT_LIST, workorderId, data);

        return Result.CONTINUE;
    }

    private Result handleTaskList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleTaskList");
        long workorderId = params.getLong("workorderId");
        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listTasks(context, workorderId, new JsonArray(data), false, transaction.isSync());

        StoredObject.put(context, App.getProfileId(), PSO_TASK_LIST, workorderId, data);

        return Result.CONTINUE;
    }

    // individual commands
    private Result handleDetails(Context context, WebTransaction transaction,
                                 JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleDetails " + transaction.getId());
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = resultData.getByteArray();

        Log.v(TAG, "handleDetails workorderId:" + workorderId);

        JsonObject workorder = new JsonObject(workorderData);

        Transform.applyTransform(workorder, PSO_WORKORDER, workorderId);

        // dispatch the event
        WorkorderDispatch.get(context, workorder, workorderId, false, transaction.isSync(), false);

        if (workorder.has("_action"))
            Log.v(TAG, "handleDetails _action=" + workorder.get("_action"));

        // store it in the store
        StoredObject.put(context, App.getProfileId(), PSO_WORKORDER, workorderId, workorderData);

        return Result.CONTINUE;
    }

    private Result handleGetSignature(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");
        byte[] data = resultData.getByteArray();

        WorkorderDispatch.signature(context, new JsonObject(data), workorderId, signatureId, false, transaction.isSync());

        //store the signature data
        StoredObject.put(context, App.getProfileId(), PSO_SIGNATURE, signatureId, data);

        return Result.CONTINUE;
    }

    public Result handleGetBundle(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long bundleId = params.getLong("bundleId");
        byte[] data = resultData.getByteArray();

        StoredObject.put(context, App.getProfileId(), PSO_BUNDLE, bundleId, data);

        WorkorderDispatch.bundle(context, new JsonObject(data), bundleId, false, transaction.isSync());

        return Result.CONTINUE;
    }

    private Result handleFinishUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
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
    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData, Throwable throwable) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDetails":
                    WorkorderDispatch.get(context, null, params.getLong("workorderId"), true, transaction.isSync(), false);
                    break;
                case "pList":
                    WorkorderDispatch.list(context, null, params.getInt("page"), WorkorderDataSelector.values()[params.getInt("selector")], true, transaction.isSync(), false);
                    break;
                case "pGetSignature":
                    WorkorderDispatch.signature(context, null, params.getLong("workorderId"), params.getLong("signatureId"), true, transaction.isSync());
                    break;
                case "pAction":
                    WorkorderDispatch.action(context, params.getLong("workorderId"), params.getString("param"), true);
                    WorkorderClient.get(context, params.getLong("workorderId"), true, false);
                    break;
                case "pTimeLog":
                    handleTimeLogFail(context, transaction, params, resultData);
                    break;
                case "pMessageList":
                    WorkorderDispatch.listMessages(context, params.getLong("workorderId"), null, true, transaction.isSync());
                    break;
                case "pAlertList":
                    WorkorderDispatch.listAlerts(context, params.getLong("workorderId"), null, true, transaction.isSync());
                    break;
                case "pTaskList":
                    WorkorderDispatch.listTasks(context, params.getLong("workorderId"), null, true, transaction.isSync());
                    break;
                case "pGetBundle":
                    WorkorderDispatch.bundle(context, null, params.getLong("bundleId"), true, transaction.isSync());
                    break;
                case "pUploadDeliverable":
                    UploadTrackerClient.uploadFailed(context, params.getLong("workorderId"));
                    if (throwable != null && throwable instanceof SecurityException) {
                        ToastClient.toast(context, "Failed to upload file. " + params.getString("filename") + " Read permission denied. Please try again", Toast.LENGTH_LONG);
                    } else {
                        ToastClient.toast(context, "Failed to upload file. " + params.getString("filename") + " Please try again", Toast.LENGTH_LONG);
                    }
                    WorkorderDispatch.uploadDeliverable(context, params.getLong("workorderId"), params.getLong("slotId"), params.getString("filename"), false, true);
                    break;
                case "pActionRequest":
                    return handleActionRequestFail(context, transaction, params, resultData);
                case "pAssignment":
                    return handleConfirmAssignmentFail(context, transaction, params, resultData);
                case "pActionCreateShipment":
                    return handleCreateShipmentFail(context, transaction, params, resultData);
                case "pActionCompleteShipmentTask":
                    return handleCompleteShipmentTaskFail(context, transaction, params, resultData);
                case "pCounterOffer":
                    return handleCounterOfferFail(context, transaction, params, resultData);
                case "pRating":
                    return handleRatingFail(context, transaction, resultData, params);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return Result.CONTINUE;
    }

    private Result handleTimeLogFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Intent intent = new Intent(context, WorkorderActivity.class);
        intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, params.getLong("workorderId"));
        intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (intent != null) {
            PendingIntent pi = PendingIntent.getActivity(App.get(), 0, intent, 0);
            ToastClient.snackbar(App.get(), resultData.getString(), "VIEW", pi, Snackbar.LENGTH_INDEFINITE);
        }

        WorkorderDispatch.action(context, params.getLong("workorderId"), "pTimeLog", true);

        return Result.CONTINUE;
    }

    private Result handleCounterOfferFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCounterOfferFail");

        long workorderId = params.getLong("workorderId");
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

        ToastClient.snackbar(context, "Could not send counter offer. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }

    private Result handleRatingFail(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
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
        return Result.CONTINUE;
    }


    private Result handleCompleteShipmentTaskFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCompleteShipmentTask");

        long workorderId = params.getLong("workorderId");
        long shipmentId = params.getLong("shipmentId");
        long taskId = params.getLong("taskId");

        WorkorderDispatch.action(context, workorderId, "request", true);

        Intent intent = WorkorderTransactionBuilder.actionCompleteShipmentTaskIntent(context, workorderId, shipmentId, taskId);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.snackbar(context, "Could not complete your shipment. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }

    private Result handleCreateShipmentFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCreateShipmentFail");

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

        return Result.CONTINUE;
    }

    private Result handleActionRequestFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleActionRequestFail");

        long workorderId = params.getLong("workorderId");
        long expireInSeconds = params.getLong("expireInSeconds");
        String startTime = params.getString("startTime");
        String endTime = params.getString("endTime");
        String note = params.getString("note");

        WorkorderDispatch.action(context, workorderId, "request", true);

        Intent intent = WorkorderTransactionBuilder.actionRequestIntent(context, workorderId, expireInSeconds, startTime, endTime, note);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.snackbar(context, "Unable to request work order. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }

    private Result handleConfirmAssignmentFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleConfirmAssignmentFail");

        long workorderId = params.getLong("workorderId");
        String startTimeIso8601 = params.getString("startTimeIso8601");
        String endTimeIso8601 = params.getString("endTimeIso8601");
        String note = params.getString("note");
        boolean isEditEta = params.getBoolean("isEditEta");

        WorkorderDispatch.action(context, workorderId, "assignment", true);

        Intent intent = WorkorderTransactionBuilder.actionConfirmAssignmentIntent(context, workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.snackbar(context, "Unable to accept work order. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.CONTINUE;
    }
}

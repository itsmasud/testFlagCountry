package com.fieldnation.service.data.workorder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.objectstore.StoredObject;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.service.transaction.Transform;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;
import com.fieldnation.utils.Stopwatch;

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
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pList(int page, String selector) {

        try {
            JsonObject obj = new JsonObject("action", "pList");
            obj.put("page", page);
            obj.put("selector", selector);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pActionRequest(long workorderId, long expireInSeconds) {
        try {
            JsonObject obj = new JsonObject("action", "pActionRequest");
            obj.put("workorderId", workorderId);
            obj.put("expireInSeconds", expireInSeconds);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] pAssignment(long workorderId, String startTimeIso8601, String endTimeIso8601) {
        try {
            JsonObject obj = new JsonObject("action", "pAssignment");
            obj.put("workorderId", workorderId);
            obj.put("startTimeIso8601", startTimeIso8601);
            obj.put("endTimeIso8601", endTimeIso8601);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pMessageList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pMessageList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pAlertList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pAlertList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] pTaskList(long workorderId) {
        try {
            JsonObject obj = new JsonObject("action", "pTaskList");
            obj.put("workorderId", workorderId);
            return obj.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
        return null;
    }


    public static byte[] pRating( int satisfactionRating, int scopeRating,
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
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleStartUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, false, false);

        return Result.FINISH;
    }

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
            ex.printStackTrace();
        }
        return Result.FINISH;
    }

    private Result handleCounterOffer(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCounterOffer");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "counter_offer", false);

        ToastClient.snackbar(context, "Success! Your counter offer has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleRating(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        ToastClient.snackbar(context, "Success! Your rating has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleCompleteShipmentTask(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCompleteShipmentTask");
        long workorderId = params.getLong("workorderId");
        long shipmentId = params.getLong("shipmentId");
        long taskId = params.getLong("taskId");

        WorkorderDispatch.action(context, workorderId, "complete_shipment_task", false);

        ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleConfirmAssignment(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleConfirmAssignment");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "assignment", false);

        ToastClient.snackbar(context, "Success! You have accepted this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleCreateShipment(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleCreateShiptment");
        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "create_shipment", false);

        ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleAction(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        WorkorderDispatch.action(context, workorderId, action, false);

        return Result.FINISH;
    }

    private Result handleActionRequest(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleActionRequest");

        long workorderId = params.getLong("workorderId");

        WorkorderDispatch.action(context, workorderId, "request", false);

        ToastClient.snackbar(context, "Success! You have requested this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) {
        Stopwatch watch = new Stopwatch(true);
        Log.v(TAG, "handleResult");
        // get the basics, send out the event
        int page = 0;
        String selector = "";

        try {
            page = params.getInt("page");
            selector = params.getString("selector");
            byte[] bdata = resultData.getByteArray();
            Log.v(TAG, "page: " + page + " selector:" + selector);

            StoredObject.put(App.getProfileId(), PSO_WORKORDER_LIST + selector, page, bdata);

            JsonArray ja = new JsonArray(bdata);

            for (int i = 0; i < ja.size(); i++) {
                JsonObject json = ja.getJsonObject(i);

                Transform.applyTransform(context, json, PSO_WORKORDER, json.getLong("workorderId"));
            }

            WorkorderDispatch.list(context, ja, page, selector, false, transaction.isSync());

            return Result.FINISH;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.v(TAG, "handleResult time: " + watch.finish());
        return Result.REQUEUE;

    }

    private Result handleMessageList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleMessageList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listMessages(context, workorderId, new JsonArray(data), false, transaction.isSync());

        StoredObject.put(App.getProfileId(), PSO_MESSAGE_LIST, workorderId, data);

        return Result.FINISH;
    }

    private Result handleAlertList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleAlertList");
        long workorderId = params.getLong("workorderId");

        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listAlerts(context, workorderId, new JsonArray(data), false, transaction.isSync());

        StoredObject.put(App.getProfileId(), PSO_ALERT_LIST, workorderId, data);

        return Result.FINISH;
    }

    private Result handleTaskList(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleTaskList");
        long workorderId = params.getLong("workorderId");
        byte[] data = resultData.getByteArray();

        WorkorderDispatch.listTasks(context, workorderId, new JsonArray(data), false, transaction.isSync());

        StoredObject.put(App.getProfileId(), PSO_TASK_LIST, workorderId, data);

        return Result.FINISH;
    }

    // individual commands
    private Result handleDetails(Context context, WebTransaction transaction,
                                 JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleDetails " + transaction.getId());
        long workorderId = params.getLong("workorderId");
        byte[] workorderData = resultData.getByteArray();

        Log.v(TAG, "handleDetails workorderId:" + workorderId);

        JsonObject workorder = new JsonObject(workorderData);

        Transform.applyTransform(context, workorder, PSO_WORKORDER, workorderId);

        // dispatch the event
        WorkorderDispatch.get(context, workorder, workorderId, false, transaction.isSync());

        // store it in the store
        StoredObject.put(App.getProfileId(), PSO_WORKORDER, workorderId, workorderData);

        return Result.FINISH;
    }

    private Result handleGetSignature(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");
        byte[] data = resultData.getByteArray();


        WorkorderDispatch.signature(context, new JsonObject(data), workorderId, signatureId, false, transaction.isSync());

        //store the signature data
        StoredObject.put(App.getProfileId(), PSO_SIGNATURE, signatureId, data);

        return Result.FINISH;
    }

    public Result handleGetBundle(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        long bundleId = params.getLong("bundleId");
        byte[] data = resultData.getByteArray();

        StoredObject.put(App.getProfileId(), PSO_BUNDLE, bundleId, data);

        WorkorderDispatch.bundle(context, new JsonObject(data), bundleId, false, transaction.isSync());

        return Result.FINISH;
    }

    private Result handleFinishUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, true, false);

        return Result.FINISH;
    }

    /*-********************************-*/
    /*-             Fail               -*/
    /*-********************************-*/
    @Override
    public Result handleFail(Context context, WebTransaction transaction, HttpResult resultData) {
        try {
            JsonObject params = new JsonObject(transaction.getHandlerParams());
            String action = params.getString("action");
            switch (action) {
                case "pDetails":
                    WorkorderDispatch.get(context, null, params.getLong("workorderId"), true, transaction.isSync());
                    break;
                case "pList":
                    WorkorderDispatch.list(context, null, params.getInt("page"), params.getString("selector"), true, transaction.isSync());
                    break;
                case "pGetSignature":
                    WorkorderDispatch.signature(context, null, params.getLong("workorderId"), params.getLong("signatureId"), true, transaction.isSync());
                    break;
                case "pAction":
                    WorkorderDispatch.action(context, params.getLong("workorderId"), params.getString("param"), true);
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
            ex.printStackTrace();
        }
        return Result.FINISH;
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

        return Result.FINISH;
    }

    private Result handleRatingFail(Context context, WebTransaction transaction, HttpResult resultData, JsonObject params) {
        try {
            Intent intent = WorkorderTransactionBuilder.actionPostRatingIntent(context,
                    params.getInt("satisfactionRating"), params.getInt("scopeRating"), params.getInt("respectRating"),
                    params.getInt("respectComment"), params.getBoolean("recommendBuyer"),params.getString("otherComments") ,params.getLong("workorderId"));

            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);


            ToastClient.snackbar(context, "Could not send your rating. Please check your connection.",
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        } catch (Exception ex) {
            ex.printStackTrace();
            ToastClient.snackbar(context, "Failed. Your rating could not be sent.", Toast.LENGTH_LONG);
        }
        return Result.FINISH;
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

        return Result.FINISH;
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

        ToastClient.snackbar(context, "Could not add your shipment. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleActionRequestFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleActionRequestFail");

        long workorderId = params.getLong("workorderId");
        long expireInSeconds = params.getLong("expireInSeconds");

        WorkorderDispatch.action(context, workorderId, "request", true);

        Intent intent = WorkorderTransactionBuilder.actionRequestIntent(context, workorderId, expireInSeconds);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.snackbar(context, "Unable to request work order. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }

    private Result handleConfirmAssignmentFail(Context context, WebTransaction transaction, JsonObject params, HttpResult resultData) throws ParseException {
        Log.v(TAG, "handleConfirmAssignmentFail");

        long workorderId = params.getLong("workorderId");
        String startTimeIso8601 = params.getString("startTimeIso8601");
        String endTimeIso8601 = params.getString("endTimeIso8601");

        WorkorderDispatch.action(context, workorderId, "assignment", true);

        Intent intent = WorkorderTransactionBuilder.actionConfirmAssignmentIntent(context, workorderId, startTimeIso8601, endTimeIso8601);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        ToastClient.snackbar(context, "Unable to accept work order. Please check your connection.",
                "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

        return Result.FINISH;
    }
}

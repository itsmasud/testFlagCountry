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
import com.fieldnation.fnhttpjson.HttpResult;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.tracker.UploadTrackerClient;
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

    public static byte[] pComplete(long workOrderId) {
        try {
            JsonObject obj = new JsonObject("action", "pComplete");
            obj.put("workorderId", workOrderId);
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

    public static byte[] pAccept(long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
        try {
            JsonObject obj = new JsonObject("action", "pAccept");
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

    public static byte[] pConfirm(long workorderId, String startTimeIso8601, String endTimeIso8601, String note) {
        try {
            JsonObject obj = new JsonObject("action", "pConfirm");
            obj.put("workorderId", workorderId);
            obj.put("startTimeIso8601", startTimeIso8601);
            obj.put("endTimeIso8601", endTimeIso8601);
            obj.put("note", note);
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


    /*-***********************************-*/
    /*-             onStart               -*/
    /*-***********************************-*/
    @Override
    public void onStart(Context context, WebTransaction transaction) {
        try {
            JsonObject params = new JsonObject(transaction.getListenerParams());
            String action = params.getString("action");
            switch (action) {
                case "pUploadDeliverable":
                    onStartUploadDeliverable(context, transaction, params);
                    break;

            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private void onStartUploadDeliverable(Context context, WebTransaction transaction, JsonObject params) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long slotId = params.getLong("slotId");
        String filename = params.getString("filename");

        WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, false, false);

        UploadTrackerClient.uploadStarted(context);
    }
    /*-**************************************-*/
    /*-             onProgress               -*/
    /*-**************************************-*/

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

    /*-**************************************-*/
    /*-             onComplete               -*/
    /*-**************************************-*/
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
                case "pComplete":
                    return onMarkComplete(context, result, transaction, params, httpResult, throwable);
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
                    return onGetBundle(context, result, transaction, params, httpResult, throwable);
                case "pUploadDeliverable":
                    return onUploadDeliverable(context, result, transaction, params, httpResult, throwable);
                case "pAccept":
                    return onAccept(context, result, transaction, params, httpResult, throwable);
                case "pConfirm":
                    return onConfirm(context, result, transaction, params, httpResult, throwable);
                case "pActionCreateShipment":
                    return onCreateShipment(context, result, transaction, params, httpResult, throwable);
                case "pActionSuccessShipmentTask":
                    return onShipmentTask(context, result, transaction, params, httpResult, throwable);
                case "pCounterOffer":
                    return onCounterOffer(context, result, transaction, params, httpResult, throwable);
                case "pRating":
                    return onRating(context, result, transaction, params, httpResult, throwable);
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

        Log.v(TAG, "onDetails workorderId:" + workorderId);
        if (result == Result.CONTINUE) {
            byte[] workorderData = httpResult.getByteArray();
            JsonObject workorder = new JsonObject(workorderData);
            Transform.applyTransform(workorder, PSO_WORKORDER, workorderId);
            WorkorderDispatch.get(context, workorder, workorderId, false, transaction.isSync(), false);
            if (workorder.has("_action"))
                Log.v(TAG, "onDetails _action=" + workorder.get("_action"));
            // store it in the store
            StoredObject.put(context, App.getProfileId(), PSO_WORKORDER, workorderId, workorderData);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not get work order from server"), Toast.LENGTH_LONG);
            WorkorderDispatch.get(context, null, workorderId, true, transaction.isSync(), false);
            return Result.DELETE;

        } else { // RETRY
            return Result.RETRY;
        }
    }

    private Result onList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onList");
        // get the basics, send out the event
        int page = params.getInt("page");
        WorkorderDataSelector selector = WorkorderDataSelector.values()[params.getInt("selector")];

        if (result == Result.CONTINUE) {
            Log.v(TAG, "onSuccessList:{selector:" + selector + ", page: " + page + "}");
            byte[] bdata = httpResult.getByteArray();

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

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not get list"), Toast.LENGTH_LONG);
            WorkorderDispatch.list(context, null, page, selector, true, transaction.isSync(), false);
            return Result.DELETE;

        } else { // RETRY
            return Result.RETRY;
        }
    }

    private Result onGetSignature(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");
        long signatureId = params.getLong("signatureId");

        if (result == Result.CONTINUE) {
            byte[] data = httpResult.getByteArray();
            WorkorderDispatch.signature(context, new JsonObject(data), workorderId, signatureId, false, transaction.isSync());

            //store the signature data
            StoredObject.put(context, App.getProfileId(), PSO_SIGNATURE, signatureId, data);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not get signature"), Toast.LENGTH_LONG);
            WorkorderDispatch.signature(context, null, workorderId, signatureId, true, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onMarkComplete(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onMarkComplete");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "complete", false);
            WorkorderClient.listTasks(context, workorderId, false);
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (result == Result.DELETE) {
            WorkorderDispatch.action(context, workorderId, "complete", true);
            WorkorderClient.get(context, workorderId, true, false);

            if (haveErrorMessage(httpResult)) {
                ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            } else {
                // probably a formatted error message


                ToastClient.toast(context, "Could not mark complete.", Toast.LENGTH_LONG);
            }
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onAction(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAction");
        long workorderId = params.getLong("workorderId");
        String action = params.getString("param");

        if (result == Result.CONTINUE) {
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

        } else if (result == Result.DELETE) {
            WorkorderDispatch.action(context, workorderId, params.getString("param"), true);
            WorkorderClient.get(context, workorderId, true, false);

            if (haveErrorMessage(httpResult)) {
                ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            } else {
                ToastClient.toast(context, "Could not update work order", Toast.LENGTH_LONG);
            }
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onTimeLog(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "pTimeLog", false);
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (result == Result.DELETE) {
            Intent intent = new Intent(context, WorkorderActivity.class);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, workorderId);
            intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (intent != null) {
                PendingIntent pi = PendingIntent.getActivity(App.get(), App.secureRandom.nextInt(), intent, 0);
                if (haveErrorMessage(httpResult)) {
                    ToastClient.snackbar(App.get(), httpResult.getString(), "VIEW", pi, Snackbar.LENGTH_INDEFINITE);
                } else {
                    ToastClient.snackbar(App.get(), "Could not change time log", "VIEW", pi, Snackbar.LENGTH_INDEFINITE);
                }
            }

            WorkorderDispatch.action(context, workorderId, "pTimeLog", true);
            return result;
        } else {
            return Result.RETRY;
        }
    }

    private Result onCheckIn(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onCheckIn");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            try {
                WorkorderDispatch.action(context, workorderId, "checkin", false);
                WorkorderClient.listTasks(context, workorderId, false);
                return onDetails(context, result, transaction, params, httpResult, throwable);
            } catch (Exception ex) {
                Log.v(TAG, ex);
                WorkorderClient.get(context, workorderId, false);
                try {
                    Intent intent = WorkorderActivity.makeIntentShow(context, workorderId);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, App.secureRandom.nextInt(), intent, 0);

                    ToastClient.snackbar(context, "Checkin failed: " + httpResult.getString(), "VIEW", pendingIntent, Snackbar.LENGTH_LONG);
                    WorkorderDispatch.action(context, workorderId, "checkin", true);
                    return Result.DELETE;
                } catch (Exception ex1) {
                    Log.v(TAG, ex1);
                }
                WorkorderDispatch.action(context, workorderId, "checkin", true);
                return Result.DELETE;
            }

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not complete checkin"), Toast.LENGTH_LONG);
            WorkorderDispatch.action(context, workorderId, "checkin", true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onCheckOut(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onCheckOut");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            try {
                WorkorderClient.listTasks(context, workorderId, false);
                WorkorderDispatch.action(context, workorderId, "checkout", false);
                return onDetails(context, result, transaction, params, httpResult, throwable);
            } catch (Exception ex) {
                Log.v(TAG, ex);
                WorkorderClient.get(context, workorderId, false);
                try {
                    Intent intent = WorkorderActivity.makeIntentShow(context, workorderId);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, App.secureRandom.nextInt(), intent, 0);

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

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not complete checkout"), Toast.LENGTH_LONG);
            WorkorderDispatch.action(context, workorderId, "checkout", true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onActionRequest(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onActionRequest");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "request", false);
            ToastClient.toast(context, "Success! You have requested this work order.", Toast.LENGTH_LONG);
            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (result == Result.DELETE) {
            long expireInSeconds = params.getLong("expireInSeconds");
            String startTime = params.getString("startTime");
            String endTime = params.getString("endTime");
            String note = params.getString("note");

            WorkorderDispatch.action(context, workorderId, "request", true);

            Intent intent = WorkorderTransactionBuilder.actionRequestIntent(context, workorderId, expireInSeconds, startTime, endTime, note);
            PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Unable to request work order"),
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onMessageList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onMessageList");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.listMessages(context, workorderId, new JsonArray(data), false, transaction.isSync());

            if (params.has("isRead") && params.getBoolean("isRead")) {
                GlobalTopicClient.profileInvalid(context);
                WorkorderClient.get(context, workorderId, false);
            }

            StoredObject.put(context, App.getProfileId(), PSO_MESSAGE_LIST, workorderId, data);

            return Result.CONTINUE;
        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not get messages"), Toast.LENGTH_LONG);
            WorkorderDispatch.listMessages(context, workorderId, null, true, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onAlertList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAlertList");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.listAlerts(context, workorderId, new JsonArray(data), false, transaction.isSync());
            if (params.has("isRead") && params.getBoolean("isRead")) {
                GlobalTopicClient.profileInvalid(context);
                WorkorderClient.get(context, workorderId, false);
            }

            StoredObject.put(context, App.getProfileId(), PSO_ALERT_LIST, workorderId, data);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not get alerts"), Toast.LENGTH_LONG);
            WorkorderDispatch.listAlerts(context, workorderId, null, true, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onTaskList(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onTaskList");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.listTasks(context, workorderId, new JsonArray(data), false, transaction.isSync());
            StoredObject.put(context, App.getProfileId(), PSO_TASK_LIST, workorderId, data);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not get tasks"), Toast.LENGTH_LONG);
            WorkorderDispatch.listTasks(context, workorderId, null, true, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onGetBundle(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long bundleId = params.getLong("bundleId");

        if (result == Result.CONTINUE) {
            byte[] data = httpResult.getByteArray();

            WorkorderDispatch.bundle(context, new JsonObject(data), bundleId, false, transaction.isSync());
            StoredObject.put(context, App.getProfileId(), PSO_BUNDLE, bundleId, data);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            ToastClient.toast(context, pickErrorMessage(httpResult, "Could not load bundle details"), Toast.LENGTH_LONG);
            WorkorderDispatch.bundle(context, null, bundleId, true, transaction.isSync());
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onUploadDeliverable(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        long workorderId = params.getLong("workorderId");
        String filename = params.getString("filename");
        long slotId = params.getLong("slotId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, true, false);
            WorkorderClient.get(context, workorderId, false);
            UploadTrackerClient.uploadSuccess(context);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            UploadTrackerClient.uploadFailed(context, workorderId);

            if (haveErrorMessage(httpResult)) {
                ToastClient.toast(context, httpResult.getString(), Toast.LENGTH_LONG);
            } else if (throwable != null && throwable instanceof SecurityException) {
                ToastClient.toast(context, "Failed to upload file. " + filename + " Read permission denied. Please try again", Toast.LENGTH_LONG);
            } else {
                ToastClient.toast(context, "Failed to upload file. " + filename + " Please try again", Toast.LENGTH_LONG);
            }
            WorkorderDispatch.uploadDeliverable(context, workorderId, slotId, filename, false, true);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onAccept(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onAccept");
        long workorderId = params.getLong("workorderId");
        boolean isEditEta = params.getBoolean("isEditEta");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "assignment", false);

            if (!isEditEta)
                ToastClient.snackbar(context, "Success! You have accepted this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);
            else
                ToastClient.snackbar(context, "Success! Edited ETA is saved.", "DISMISS", null, Snackbar.LENGTH_LONG);

            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (result == Result.DELETE) {
            String startTimeIso8601 = params.getString("startTimeIso8601");
            String endTimeIso8601 = params.getString("endTimeIso8601");
            String note = params.getString("note");

            WorkorderDispatch.action(context, workorderId, "assignment", true);

            Intent intent = WorkorderTransactionBuilder.actionAcceptIntent(context, workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta);
            PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Unable to accept work order"), "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onConfirm(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onConfirm");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "assignment", false);

            ToastClient.snackbar(context, "Success! You have confirmed this work order.", "DISMISS", null, Snackbar.LENGTH_LONG);

            return onDetails(context, result, transaction, params, httpResult, throwable);

        } else if (result == Result.DELETE) {
            String startTimeIso8601 = params.getString("startTimeIso8601");
            String endTimeIso8601 = params.getString("endTimeIso8601");
            String note = params.getString("note");

            WorkorderDispatch.action(context, workorderId, "assignment", true);

            Intent intent = WorkorderTransactionBuilder.actionConfirmIntent(context, workorderId, startTimeIso8601, endTimeIso8601, note);
            PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Unable to confirm work order"), "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onCreateShipment(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onCreateShipment");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "create_shipment", false);
            ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);
            WorkorderClient.get(context, workorderId, false);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
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
            PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not add your shipment"), "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onShipmentTask(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onShipmentTask");
        long workorderId = params.getLong("workorderId");
        long shipmentId = params.getLong("shipmentId");
        long taskId = params.getLong("taskId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "complete_shipment_task", false);

            ToastClient.snackbar(context, "Success! Your shipment has been added.", "DISMISS", null, Snackbar.LENGTH_LONG);

            WorkorderClient.get(context, workorderId, false);
            WorkorderClient.listTasks(context, workorderId, false);

            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            WorkorderDispatch.action(context, workorderId, "request", true);

            Intent intent = WorkorderTransactionBuilder.actionCompleteShipmentTaskIntent(context, workorderId, shipmentId, taskId);
            PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not complete your shipment."),
                    "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onCounterOffer(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) throws ParseException {
        Log.v(TAG, "onSuccessCounterOffer");
        long workorderId = params.getLong("workorderId");

        if (result == Result.CONTINUE) {
            WorkorderDispatch.action(context, workorderId, "counter_offer", false);
            ToastClient.snackbar(context, "Success! Your counter offer has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);
            WorkorderClient.get(context, workorderId, false);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
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
            PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

            ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not send counter offer"), "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);
            return Result.DELETE;

        } else {
            return Result.RETRY;
        }
    }

    private Result onRating(Context context, Result result, WebTransaction transaction, JsonObject params, HttpResult httpResult, Throwable throwable) {
        if (result == Result.CONTINUE) {
            ToastClient.snackbar(context, "Success! Your rating has been sent.", "DISMISS", null, Snackbar.LENGTH_LONG);
            return Result.CONTINUE;

        } else if (result == Result.DELETE) {
            try {
                Intent intent = WorkorderTransactionBuilder.actionPostRatingIntent(
                        context,
                        params.getLong("workorderId"),
                        params.getInt("satisfactionRating"),
                        params.getInt("scopeRating"),
                        params.getInt("respectRating"),
                        params.getInt("respectComment"),
                        params.getBoolean("recommendBuyer"),
                        params.getString("otherComments"));

                PendingIntent pendingIntent = PendingIntent.getService(context, App.secureRandom.nextInt(), intent, 0);

                ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not send your rating"), "TRY AGAIN", pendingIntent, Snackbar.LENGTH_LONG);

            } catch (Exception ex) {
                Log.v(TAG, ex);
                ToastClient.snackbar(context, pickErrorMessage(httpResult, "Could not send your rating"), Toast.LENGTH_LONG);
            }
            return Result.DELETE;
        } else {
            return Result.RETRY;
        }
    }
}

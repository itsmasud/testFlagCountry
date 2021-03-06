package com.fieldnation.service.data.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.transaction.WebTransactionSystem;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class WorkorderClient extends TopicClient implements WorkorderConstants {
    private static final String STAG = "WorkorderClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public WorkorderClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    /*-*****************************-*/
    /*-             list            -*/
    /*-*****************************-*/
/*
    public static void list(Context context, WorkorderDataSelector selector, int page, boolean isSync, boolean allowCache) {
        Log.v(STAG, "list:{selector:" + selector + ", page:" + page + ", isSync:" + isSync + ", allowCache:" + allowCache + "}");

        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST);
        intent.putExtra(PARAM_LIST_SELECTOR, selector.ordinal());
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }

    public boolean subList(boolean isSync) {
        return subList(null, isSync);
    }

    public boolean subList(WorkorderDataSelector selector) {
        return subList(selector, false);
    }

    public boolean subList(WorkorderDataSelector selector, boolean isSync) {
        String topicId = TOPIC_ID_LIST;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (selector != null) {
            topicId += "/" + selector.ordinal() + "_" + selector.getCall();
        }

        return register(topicId);
    }
*/

    /*-********************************-*/
    /*-             details            -*/
    /*-********************************-*/
    public static void get(Context context, long id, boolean allowCache) {
        get(context, id, allowCache, false);
    }

    public static void get(Context context, long id, boolean allowCache, boolean isSync) {
        Log.v("WorkorderClient", "get");

        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_WORKORDER_ID, id);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }

    public boolean subGet(boolean isSync) {
        return subGet(0, isSync);
    }

    public boolean unsubGet(boolean isSync) {
        return unsubGet(0, isSync);
    }

    public boolean subGet(long workorderId) {
        return subGet(workorderId, false);
    }

    public boolean unsubGet(long workorderId) {
        return unsubGet(workorderId, false);
    }

    public boolean subGet(long workorderId, boolean isSync) {
        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId);
    }

    public boolean unsubGet(long workorderId, boolean isSync) {
        String topicId = TOPIC_ID_GET;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return unregister(topicId);
    }

/*
    public static void listAlerts(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subListAlerts(boolean isSync) {
        return subListAlerts(0, isSync);
    }

    public boolean subListAlerts(long workorderId, boolean isSync) {
        String topicId = TOPIC_ID_LIST_ALERTS;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId);
    }
*/

    public boolean subActions() {
        return subActions(0);
    }

    public boolean subActions(long workorderId) {
        String topicId = TOPIC_ID_ACTION_COMPLETE;

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId);
    }

    /*-*********************************-*/
    /*-             Tasks               -*/
    /*-*********************************-*/
/*
    public static void listTasks(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_TASKS);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subListTasks(boolean isSync) {
        return subListTasks(0, isSync);
    }

    public boolean subListTasks(long workorderId, boolean isSync) {
        String topicId = TOPIC_ID_LIST_TASKS;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }
        return register(topicId);
    }
*/

/*
    public static void actionCompleteTask(Context context, long workorderId, long taskId) {
        WorkorderTransactionBuilder.actionCompleteTask(context, workorderId, taskId);
    }
*/

/*
    public static void actionReportProblem(Context context, long workorderId, String explanation, ReportProblemType type) {
        WorkorderTransactionBuilder.actionReportProblem(context, workorderId, explanation, type, null);
    }
*/

/*
    public static void actionRunningLate(Context context, long workorderId, String explanation, Integer delayInSeconds) {
        WorkorderTransactionBuilder.actionReportProblem(context, workorderId, explanation, ReportProblemType.WILL_BE_LATE, delayInSeconds);
    }
*/

    /*-*********************************-*/
    /*-             Messages            -*/
    /*-*********************************-*/
/*
    public static void listMessages(Context context, long workorderId, boolean isSync, boolean allowCache) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }

    public boolean subListMessages(boolean isSync) {
        return subListMessages(0, isSync);
    }

    public boolean subListMessages(long workorderId, boolean isSync) {
        String topicId = TOPIC_ID_LIST_MESSAGES;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId);
    }

    public static void actionAddMessage(Context context, long workorderId, String message) {
        WorkorderTransactionBuilder.actionAddMessage(context, workorderId, message);
    }

    public static void actionMarkMessagesRead(Context context, long workorderId) {
        WorkorderTransactionBuilder.listMessages(context, workorderId, true, false);
    }
*/

    /*-*********************************-*/
    /*-             Notification        -*/
    /*-*********************************-*/
/*
    public static void actionMarkNotificationRead(Context context, long workorderId) {
        WorkorderTransactionBuilder.listAlerts(context, workorderId, true, false);
    }
*/

    /*-*********************************-*/
    /*-             Discounts           -*/
    /*-*********************************-*/
/*
    public static void createDiscount(Context context, long workorderId, String description, double price) {
        WorkorderTransactionBuilder.createDiscount(context, workorderId, description, price);
    }

    public static void deleteDiscount(Context context, long workorderId, long discountId) {
        WorkorderTransactionBuilder.deleteDiscount(context, workorderId, discountId);
    }
*/

    /*-*********************************-*/
    /*-             Expense             -*/
    /*-*********************************-*/
/*
    public static void createExpense(Context context, long workorderId, String description, double price,
                                     ExpenseCategory category) {
        WorkorderTransactionBuilder.createExpense(context, workorderId,
                description, price, category);
    }

    public static void deleteExpense(Context context, long workorderId, long expenseId) {
        WorkorderTransactionBuilder.deleteExpense(context, workorderId, expenseId);
    }
*/

    /*-*************************************-*/
    /*-             Shipments               -*/
    /*-*************************************-*/
/*
    public static void createShipment(Context context, long workorderId, String description,
                                      boolean isToSite, String carrier, String carrierName,
                                      String trackingNumber) {
        WorkorderTransactionBuilder.postShipment(context, workorderId, description, isToSite,
                carrier, carrierName, trackingNumber);
    }

    public static void createShipment(Context context, long workorderId, String description,
                                      boolean isToSite, String carrier, String carrierName,
                                      String trackingNumber, long taskId) {
        WorkorderTransactionBuilder.postShipment(context, workorderId, description, isToSite,
                carrier, carrierName, trackingNumber, taskId);
    }

    public static void deleteShipment(Context context, long workorderId, long shipmentId) {
        WorkorderTransactionBuilder.deleteShipment(context, workorderId, shipmentId);
    }


    public static void actionCompleteShipmentTask(Context context, long workorderId, long shipmentId, long taskId) {
        WorkorderTransactionBuilder.actionCompleteShipmentTask(context, workorderId, shipmentId, taskId);
    }

    public static void actionSetShipmentDetails(
            Context context, long workorderId, String description, boolean isToSite, String carrier,
            String carrierName, String trackingNumber) {
        WorkorderTransactionBuilder.postShipment(context, workorderId, description,
                isToSite, carrier, carrierName, trackingNumber);
    }

    public static void actionSetShipmentDetails(
            Context context, long workorderId, String description, boolean isToSite, String carrier,
            String carrierName, String trackingNumber, long taskId) {

        WorkorderTransactionBuilder.postShipment(context, workorderId, description,
                isToSite, carrier, carrierName, trackingNumber, taskId);
    }
*/

    /*-*************************************-*/
    /*-             Ratings               -*/
    /*-*************************************-*/
/*
    public static void sendRating(Context context, long workorderId, int satisfactionRating, int scopeRating,
                                  int respectRating, int respectComment, boolean recommendBuyer, String otherComments) {
        context.startService(
                WorkorderTransactionBuilder.actionPostRatingIntent(context, workorderId, satisfactionRating, scopeRating,
                        respectRating, respectComment, recommendBuyer, otherComments));
    }
*/

    public static void sendRating(Context context, long workorderId, int satisfactionRating, int scopeRating,
                                  int respectRating, String otherComments) {
        WebTransactionSystem.queueTransaction(context,
                WorkorderTransactionBuilder.actionPostRatingIntent(workorderId, satisfactionRating, scopeRating,
                        respectRating, -1, false, otherComments));
    }


    /*-**********************************-*/
    /*-             signature            -*/
    /*-**********************************-*/
/*
    public static void getSignature(Context context, long workorderId, long signatureId) {
        getSignature(context, workorderId, signatureId, false);
    }

    public static void getSignature(Context context, long workorderId, long signatureId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_SIGNATURE_ID, signatureId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }
*/

//    public static void actionCompleteSignatureTask(Context context, long workorderId, long taskId, String printName, String signatureJson) {
//        WorkorderTransactionBuilder.actionCompleteShipmentTask(context, workorderId, taskId, printName, signatureJson);
//    }

/*
    public boolean subGetSignature(boolean isSync) {
        return subGetSignature(0, 0, isSync);
    }

    public boolean subGetSignature(long workorderId, long signatureId, boolean isSync) {
        String topicId = TOPIC_ID_GET_SIGNATURE;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;

            if (signatureId > 0) {
                topicId += "/" + signatureId;
            }
        }

        return register(topicId);
    }
*/

    // add signature json
/*
    public static void addSignatureJson(Context context, long workorderId, String name, String json) {
        WorkorderTransactionBuilder.addSignatureJson(context, workorderId, name, json);
    }
*/

    // complete signature
/*
    public static void addSignatureJsonTask(Context context, long workorderId, long taskId, String name, String json) {
        WorkorderTransactionBuilder.addSignatureJsonTask(context, workorderId, taskId, name, json);
    }
*/

/*
    public static void addSignatureSvg(Context context, long workorderId, String name, String svg) {
        WorkorderTransactionBuilder.addSignatureSvg(context, workorderId, name, svg);
    }

    public static void addSignatureSvgTask(Context context, long workorderId, long taskId, String name, String svg) {
        WorkorderTransactionBuilder.addSignatureSvgTask(context, workorderId, taskId, name, svg);
    }

    public static void deleteSignature(Context context, long workorderId, long signatureId) {
        WorkorderTransactionBuilder.deleteSignature(context, workorderId, signatureId);
    }
*/


    /*-******************************************-*/
    /*-             workorder actions            -*/
    /*-******************************************-*/
/*
    public static void actionReadyToGo(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionReady(context, workorderId);
    }
*/

/*
    public static void actionChangePay(Context context, long workorderId, Pay pay, String explanation) {
        WorkorderTransactionBuilder.actionChangePay(context, workorderId, pay, explanation);
    }

    public static void actionCustomField(Context context, long workorderId, long customFieldId, String value) {
        WorkorderTransactionBuilder.actionCustomField(context, workorderId, customFieldId, value);
    }

    // complete workorder
    public static void actionComplete(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionComplete(context, workorderId);
    }

    // incomplete workorder
    public static void actionIncomplete(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionIncomplete(context, workorderId);
    }

    public static void actionSetClosingNotes(Context context, long workorderId, String closingNotes) {
        WorkorderTransactionBuilder.actionClosingNotes(context, workorderId, closingNotes);
    }
*/

    // acknowledge hold
/*
    public static void actionAcknowledgeHold(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionAcknowledgeHold(context, workorderId);
    }
*/

    // counter offer
/*
    public static void actionCounterOffer(Context context, long workorderId, boolean expires,
                                          String reason, int expiresAfterInSecond, Pay pay,
                                          Schedule schedule, Expense[] expenses) {
        WorkorderTransactionBuilder.actionCounterOffer(context, workorderId, expires, reason,
                expiresAfterInSecond, pay, schedule, expenses);
    }
*/

    // request
    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        WorkorderTransactionBuilder.actionRequest(context, workorderId, expireInSeconds);
    }

/*
    public static void actionRequest(Context context, long workorderId, long expireInSeconds, String startTime, String endTime, String note) {
        WorkorderTransactionBuilder.actionRequest(context, workorderId, expireInSeconds, startTime, endTime, note);
    }
*/
/*
    public static void actionAcceptAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601, String note, boolean isEditEta) {
        WorkorderTransactionBuilder.actionAccept(context, workorderId, startTimeIso8601, endTimeIso8601, note, isEditEta);
    }
*/
/*
    public static void actionConfirm(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601, String note) {
        WorkorderTransactionBuilder.actionConfirm(context, workorderId, startTimeIso8601, endTimeIso8601, note);
    }
*/

    public static void actionDecline(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionDecline(context, workorderId);
    }

    public static void actionWithdrawRequest(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionWithdrawRequest(context, workorderId);
    }

/*
    public static void actionReeadyToGo(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionReady(context, workorderId);
    }
*/

    /*-******************************************-*/
    /*-             workorder checkin            -*/
    /*-******************************************-*/
/*
    public static void actionCheckin(Context context, long workorderId, String dateTime) {
        WorkorderTransactionBuilder.actionCheckin(context, workorderId, dateTime);
    }

    public static void actionCheckin(Context context, long workorderId, String dateTime, Location location) {
        WorkorderTransactionBuilder.actionCheckin(context, workorderId, location, dateTime);
    }
*/

    /*-*******************************************-*/
    /*-             workorder checkout            -*/
    /*-*******************************************-*/

/*
    public static void actionCheckout(Context context, long workorderId, String dateTime) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, dateTime);
    }

    public static void actionCheckout(Context context, long workorderId, String dateTime, Location location) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, dateTime, location);
    }

    public static void actionCheckout(Context context, long workorderId, String dateTime, int deviceCount) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, dateTime, deviceCount);
    }

    public static void actionCheckout(Context context, long workorderId, String dateTime, int deviceCount, Location location) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, dateTime, deviceCount, location);
    }
*/


    /*-*****************************************-*/
    /*-             workorder bundle            -*/
    /*-*****************************************-*/
/*
    public static void getBundle(Context context, long bundleId) {
        getBundle(context, bundleId, true, false);
    }
*/

/*
    public static void getBundle(Context context, long bundleId, boolean allowCache, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        intent.putExtra(PARAM_WORKORDER_ID, bundleId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        intent.putExtra(PARAM_ALLOW_CACHE, allowCache);
        context.startService(intent);
    }
*/

/*
    public boolean subBundle() {
        return subBundle(false);
    }

    public boolean subBundle(boolean isSync) {
        String topicId = TOPIC_ID_GET_BUNDLE;

        if (isSync) {
            topicId += "_SYNC";
        }

        return register(topicId);
    }
*/

    /*-*************************************-*/
    /*-             deliverables            -*/
    /*-*************************************-*/

/*
    public static void uploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, String filePath) {
        Log.v(STAG, "requestUploadDeliverable");

        WorkorderDispatch.uploadDeliverable(context, workorderId, uploadSlotId, filename, false, false);

        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_LOCAL_PATH, filePath);
        intent.putExtra(PARAM_FILE_NAME, filename);
        context.startService(intent);
    }
*/

/*
    public static void uploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, String filePath, String photoDescription) {
        Log.v(STAG, "requestUploadDeliverable");

        WorkorderDispatch.uploadDeliverable(context, workorderId, uploadSlotId, filename, photoDescription, false, false);

        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_LOCAL_PATH, filePath);
        intent.putExtra(PARAM_FILE_NAME, filename);
        intent.putExtra(PARAM_FILE_DESCRIPTION, photoDescription);
        context.startService(intent);
    }
*/


/*    public static void uploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, Uri uri) {
        Log.v(STAG, "requestUploadDeliverable");

        WorkorderDispatch.uploadDeliverable(context, workorderId, uploadSlotId, filename, false, false);

        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_URI, uri);
        intent.putExtra(PARAM_FILE_NAME, filename);
        context.startService(intent);
    }

    public static void uploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, Uri uri, String photoDescription) {
        Log.v(STAG, "requestUploadDeliverable");

        WorkorderDispatch.uploadDeliverable(context, workorderId, uploadSlotId, filename, false, false);

        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_URI, uri);
        intent.putExtra(PARAM_FILE_NAME, filename);
        intent.putExtra(PARAM_FILE_DESCRIPTION, photoDescription);
        context.startService(intent);
    }

    public static void uploadDeliverable(final Context context, final long workorderId, final long uploadSlotId, Intent data) {
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fromUri(String filename, Uri uri) {
                uploadDeliverable(context, workorderId, uploadSlotId, filename, uri);
            }

            @Override
            public void fail(String reason) {
                Log.v("WorkorderDataClient.requestUploadDeliverable", reason);
                ToastClient.toast(context, R.string.toast_could_not_upload_file, Toast.LENGTH_LONG);
            }
        });
    }

    public static void uploadDeliverable(final Context context, final long workorderId, final long uploadSlotId, Intent data, final String photoDescription) {
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fromUri(String filename, Uri uri) {
                uploadDeliverable(context, workorderId, uploadSlotId, filename, uri, photoDescription);
            }

            @Override
            public void fail(String reason) {
                Log.v("WorkorderDataClient.requestUploadDeliverable", reason);
                ToastClient.toast(context, R.string.toast_could_not_upload_file, Toast.LENGTH_LONG);
            }
        });
    }*/

/*    public boolean subDeliverableUpload() {
        return register(TOPIC_ID_UPLOAD_DELIVERABLE);
    }

    public boolean subDeliverableUpload(long workorderId, long uploadSlotId) {
        String topicId = TOPIC_ID_UPLOAD_DELIVERABLE;

        topicId += "/" + workorderId + "/" + uploadSlotId;

        return register(topicId);
    }

    public boolean subDeliverableProgress(long workorderId, long uploadSlotId) {
        String topicId = TOPIC_ID_UPLOAD_DELIVERABLE_PROGRESS;

        topicId += "/" + workorderId + "/" + uploadSlotId;

        return register(topicId);
    }*/

//    public static void deleteDeliverable(Context context, long workorderId, long workorderUploadId) {
//        WorkorderTransactionBuilder.deleteDeliverable(context, workorderId, workorderUploadId);
//    }


//    public static void getDeliverable(Context context, long workorderId, long deliverableId, boolean isSync) {
//        Intent intent = new Intent(context, WorkorderService.class);
//        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_DELIVERABLE);
//        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
//        intent.putExtra(PARAM_DELIVERABLE_ID, deliverableId);
//        intent.putExtra(PARAM_IS_SYNC, isSync);
//        context.startService(intent);
//    }
//
//    public static void downloadDeliverable(Context context, long workorderId, long deliverableId, String url, boolean isSync) {
//        Intent intent = new Intent(context, WorkorderService.class);
//        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DELIVERABLE);
//        intent.putExtra(PARAM_WORKORDER_ID, workorderId);
//        intent.putExtra(PARAM_DELIVERABLE_ID, deliverableId);
//        intent.putExtra(PARAM_URL, url);
//        intent.putExtra(PARAM_IS_SYNC, isSync);
//        context.startService(intent);
//    }

    /*-*************************************-*/
    /*-             Time Log                -*/
    /*-*************************************-*/
/*
    public static void addTimeLog(Context context, long workorderId, long startDate, long endDate) {
        WorkorderTransactionBuilder.postTimeLog(context, workorderId, startDate, endDate);
    }

    public static void addTimeLog(Context context, long workorderId, long startDate, long endDate, int numberOfDevices) {
        WorkorderTransactionBuilder.postTimeLog(context, workorderId, startDate, endDate, numberOfDevices);
    }

    public static void updateTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate) {
        WorkorderTransactionBuilder.postTimeLog(context, workorderId, loggedHoursId, startDate, endDate);
    }

    public static void updateTimeLog(Context context, long workorderId, long loggedHoursId, long startDate, long endDate, int numberOfDevices) {
        WorkorderTransactionBuilder.postTimeLog(context, workorderId, loggedHoursId, startDate, endDate, numberOfDevices);
    }

    public static void deleteTimeLog(Context context, long workorderId, long loggedHoursId) {
        WorkorderTransactionBuilder.deleteTimeLog(context, workorderId, loggedHoursId);
    }
*/

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "topicId " + topicId);
            if (topicId.startsWith(TOPIC_ID_LIST)) {
                preList((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_GET)) {
                preGet((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_GET_SIGNATURE)) {
                preGetSignature((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_GET_BUNDLE)) {
                preGetBundle((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_LIST_MESSAGES)) {
                preListMessages((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_LIST_ALERTS)) {
                preListAlerts((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_LIST_TASKS)) {
                preListTasks((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_ACTION_COMPLETE)) {
                preAction((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_UPLOAD_DELIVERABLE_PROGRESS)) {
                preUploadDeliverableProgress((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_UPLOAD_DELIVERABLE)) {
                preUploadDeliverable((Bundle) payload);
            }
        }

        private void preUploadDeliverableProgress(Bundle payload) {
            onUploadDeliverableProgress(
                    payload.getLong(PARAM_WORKORDER_ID),
                    payload.getLong(PARAM_UPLOAD_SLOT_ID),
                    payload.getString(PARAM_FILE_NAME),
                    payload.getLong(PARAM_POS),
                    payload.getLong(PARAM_SIZE),
                    payload.getLong(PARAM_TIME));
        }

        public void onUploadDeliverableProgress(long workorderId, long slotId, String filename, long pos, long size, long time) {

        }

        private void preUploadDeliverable(Bundle payload) {
            if (payload.containsKey(PARAM_ERROR) && payload.getBoolean(PARAM_ERROR)) {
                onUploadDeliverable(
                        payload.getLong(PARAM_WORKORDER_ID),
                        payload.getLong(PARAM_UPLOAD_SLOT_ID),
                        payload.getString(PARAM_FILE_NAME),
                        payload.getBoolean(PARAM_IS_COMPLETE), true);

            } else {
                onUploadDeliverable(
                        payload.getLong(PARAM_WORKORDER_ID),
                        payload.getLong(PARAM_UPLOAD_SLOT_ID),
                        payload.getString(PARAM_FILE_NAME),
                        payload.getBoolean(PARAM_IS_COMPLETE), false);
            }
        }

        public void onUploadDeliverable(long workorderId, long slotId, String filename, boolean isComplete, boolean failed) {
        }

        private void preAction(Bundle payload) {
            Log.v(STAG, "preAction "
                    + payload.getLong(PARAM_WORKORDER_ID) + " "
                    + payload.getString(PARAM_ACTION));

            onAction(payload.getLong(PARAM_WORKORDER_ID),
                    payload.getString(PARAM_ACTION),
                    payload.getBoolean(PARAM_ERROR));
        }

        public void onAction(long workorderId, String action, boolean failed) {
        }

        private void preListTasks(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onTaskList(payload.getLong(PARAM_WORKORDER_ID), null, true);
            } else {
                Log.v(STAG, "preListTasks");
                new AsyncTaskEx<Object, Object, List<Task>>() {
                    private long workorderId;

                    @Override
                    protected List<Task> doInBackground(Object... params) {
                        Bundle payload = (Bundle) params[0];

                        workorderId = payload.getLong(PARAM_WORKORDER_ID);
                        JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);
                        List<Task> list = new LinkedList<>();
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Task.fromJson(ja.getJsonObject(i)));
                        }

                        return list;
                    }

                    @Override
                    protected void onPostExecute(List<Task> tasks) {
                        onTaskList(workorderId, tasks, false);
                    }
                }.executeEx(payload);
            }
        }

        public void onTaskList(long workorderId, List<Task> tasks, boolean failed) {
        }

        private void preListAlerts(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onAlertList(payload.getLong(PARAM_WORKORDER_ID), null, true);
            } else {
                Log.v(STAG, "preListAlerts");
                new AsyncTaskEx<Object, Object, List<Notification>>() {
                    private long workorderId;

                    @Override
                    protected List<Notification> doInBackground(Object... params) {
                        Bundle payload = (Bundle) params[0];

                        workorderId = payload.getLong(PARAM_WORKORDER_ID);
                        JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);
                        List<Notification> list = new LinkedList<>();
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Notification.fromJson(ja.getJsonObject(i)));
                        }

                        return list;
                    }

                    @Override
                    protected void onPostExecute(List<Notification> alerts) {
                        onAlertList(workorderId, alerts, false);
                    }
                }.executeEx(payload);
            }
        }

        public void onAlertList(long workorderId, List<Notification> alerts, boolean failed) {
        }

        private void preListMessages(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onMessageList(payload.getLong(PARAM_WORKORDER_ID), null, true);
            } else {
                new AsyncTaskEx<Object, Object, List<Message>>() {
                    private long workorderId;

                    @Override
                    protected List<Message> doInBackground(Object... params) {
                        Bundle payload = (Bundle) params[0];

                        workorderId = payload.getLong(PARAM_WORKORDER_ID);
                        JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);
                        List<Message> list = new LinkedList<>();
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Message.fromJson(ja.getJsonObject(i)));
                        }

                        return list;
                    }

                    @Override
                    protected void onPostExecute(List<Message> messages) {
                        onMessageList(workorderId, messages, false);
                    }
                }.executeEx(payload);
            }
        }

        public void onMessageList(long workorderId, List<Message> messages, boolean failed) {
        }

        // list
        protected void preList(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onList(null, WorkorderDataSelector.values()[payload.getInt(PARAM_LIST_SELECTOR)],
                        payload.getInt(PARAM_PAGE), true, payload.getBoolean(PARAM_IS_CACHED));
            } else {
                new AsyncTaskEx<Bundle, Object, List<Workorder>>() {
                    private WorkorderDataSelector selector;
                    private int page;
                    private boolean isCached;

                    @Override
                    protected List<Workorder> doInBackground(Bundle... params) {
                        Bundle bundle = params[0];
                        try {
                            selector = WorkorderDataSelector.values()[bundle.getInt(PARAM_LIST_SELECTOR)];
                            Log.v(STAG, "Selector " + selector);
                            page = bundle.getInt(PARAM_PAGE);
                            isCached = bundle.getBoolean(PARAM_IS_CACHED);
                            List<Workorder> list = new LinkedList<>();
                            JsonArray ja = bundle.getParcelable(PARAM_DATA_PARCELABLE);
                            for (int i = 0; i < ja.size(); i++) {
                                list.add(Workorder.fromJson(ja.getJsonObject(i)));
                            }
                            return list;
                        } catch (Exception ex) {
//                        Log.v(STAG, selector.name());
                            Log.v(STAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<Workorder> workorders) {
                        onList(workorders, selector, page, false, isCached);
                    }
                }.executeEx(payload);
            }
        }

        public void onList(List<Workorder> list, WorkorderDataSelector selector, int page, boolean failed, boolean isCached) {
        }

        // details
        protected void preGet(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onGet(payload.getLong(PARAM_WORKORDER_ID), null, true, payload.getBoolean(PARAM_IS_CACHED));
            } else {
                new AsyncTaskEx<Bundle, Object, Workorder>() {
                    private boolean _isCached = false;
                    private long _workorderId = 0;

                    @Override
                    protected Workorder doInBackground(Bundle... params) {
                        Bundle bundle = params[0];
                        _isCached = bundle.getBoolean(PARAM_IS_CACHED);
                        _workorderId = bundle.getLong(PARAM_WORKORDER_ID);
                        try {
                            return Workorder.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                        } catch (Exception ex) {
                            Log.v(STAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Workorder workorder) {
                        onGet(_workorderId, workorder, false, _isCached);
                    }
                }.executeEx(payload);
            }
        }

        public void onGet(long workorderId, Workorder workorder, boolean failed, boolean isCached) {
        }

        // get signature
        private void preGetSignature(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onGetSignature(null, true);
            } else {
                new AsyncTaskEx<Bundle, Object, Signature>() {
                    @Override
                    protected Signature doInBackground(Bundle... params) {
                        Bundle bundle = params[0];
                        try {
                            return Signature.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                        } catch (Exception ex) {
                            Log.v(STAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Signature signature) {
                        onGetSignature(signature, false);
                    }
                }.executeEx(payload);
            }
        }

        public void onGetSignature(Signature signature, boolean failed) {
        }

        private void preGetBundle(Bundle payload) {
            if (payload.getBoolean(PARAM_ERROR)) {
                onGetBundle(null, true);
            } else {
                new AsyncTaskEx<Bundle, Object, com.fieldnation.data.workorder.Bundle>() {
                    @Override
                    protected com.fieldnation.data.workorder.Bundle doInBackground(Bundle... params) {
                        Bundle bundle = params[0];
                        try {
                            return com.fieldnation.data.workorder.Bundle.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                        } catch (Exception ex) {
                            Log.v(STAG, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(com.fieldnation.data.workorder.Bundle bundle) {
                        onGetBundle(bundle, false);
                    }
                }.executeEx(payload);
            }
        }

        public void onGetBundle(com.fieldnation.data.workorder.Bundle bundle, boolean failed) {
        }
    }
}

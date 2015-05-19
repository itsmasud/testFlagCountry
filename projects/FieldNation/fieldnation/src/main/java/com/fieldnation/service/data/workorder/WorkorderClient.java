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
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Signature;
import com.fieldnation.data.workorder.Task;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Carver on 3/13/2015.
 */
public class WorkorderClient extends TopicClient implements WorkorderConstants {
    public static final String STAG = "WorkorderClient";
    public final String TAG = UniqueTag.makeTag(STAG);

    public WorkorderClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    /*-*****************************-*/
    /*-             list            -*/
    /*-*****************************-*/
    public static void list(Context context, WorkorderDataSelector selector, int page) {
        list(context, selector, page, false);
    }

    public static void list(Context context, WorkorderDataSelector selector, int page, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST);
        intent.putExtra(PARAM_LIST_SELECTOR, selector.getCall());
        intent.putExtra(PARAM_PAGE, page);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subList(boolean isSync) {
        return subList(null, isSync);
    }

    public boolean subList(WorkorderDataSelector selector) {
        return subList(selector, false);
    }

    public boolean subList(WorkorderDataSelector selector, boolean isSync) {
        String topicId = PARAM_ACTION_LIST;

        if (isSync) {
            topicId += "-SYNC";
        }

        if (selector != null) {
            topicId += "/" + selector.getCall();
        }

        return register(topicId, TAG);
    }

    /*-********************************-*/
    /*-             details            -*/
    /*-********************************-*/
    public static void get(Context context, long id) {
        get(context, id, false);
    }

    public static void get(Context context, long id, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DETAILS);
        intent.putExtra(PARAM_ID, id);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subGet(boolean isSync) {
        return subGet(0, isSync);
    }

    public boolean subGet(long workorderId) {
        return subGet(workorderId, false);
    }

    public boolean subGet(long workorderId, boolean isSync) {
        String topicId = PARAM_ACTION_DETAILS;

        if (isSync) {
            topicId += "-SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId, TAG);
    }

    public static void listAlerts(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_NOTIFICATIONS);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subListAlerts(boolean isSync) {
        return subListAlerts(0, isSync);
    }

    public boolean subListAlerts(long workorderId, boolean isSync) {
        String topicId = PARAM_ACTION_LIST_NOTIFICATIONS;

        if (isSync) {
            topicId += "/SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId, TAG);
    }

    public boolean subActions() {
        return subActions(0);
    }

    public boolean subActions(long workorderId) {
        String topicId = PARAM_ACTION;

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId, TAG);
    }

    /*-*********************************-*/
    /*-             Tasks               -*/
    /*-*********************************-*/
    public static void listTasks(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_TASKS);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subListTasks(boolean isSync) {
        return subListTasks(0, isSync);
    }

    public boolean subListTasks(long workorderId, boolean isSync) {
        String topicId = PARAM_ACTION_LIST_TASKS;

        if (isSync) {
            topicId += "/SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }
        return register(topicId, TAG);
    }

    public static void actionCompleteTask(Context context, long workorderId, long taskId) {
        WorkorderTransactionBuilder.actionCompleteTask(context, workorderId, taskId);
    }

    /*-*********************************-*/
    /*-             Messages            -*/
    /*-*********************************-*/
    public static void listMessages(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_LIST_MESSAGES);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subListMessages(boolean isSync) {
        return subListMessages(0, isSync);
    }

    public boolean subListMessages(long workorderId, boolean isSync) {
        String topicId = PARAM_ACTION_LIST_MESSAGES;

        if (isSync) {
            topicId += "/SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;
        }

        return register(topicId, TAG);
    }

    public static void actionAddMessage(Context context, long workorderId, String message) {
        WorkorderTransactionBuilder.action(context, workorderId,
                "messages/new", null, HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "message=" + misc.escapeForURL(message));
    }

    public static void actionMarkMessagesRead(Context context, long workorderId) {
        WorkorderTransactionBuilder.listMessages(context, workorderId, true, false);
    }

    /*-*********************************-*/
    /*-             Discounts           -*/
    /*-*********************************-*/
    public static void createDiscount(Context context, long workorderId, String description, double price) {
        WorkorderTransactionBuilder.createDiscount(context, workorderId, description, price);
    }

    public static void deleteDiscount(Context context, long workorderId, long discountId) {
        WorkorderTransactionBuilder.deleteDiscount(context, workorderId, discountId);
    }

    /*-*********************************-*/
    /*-             Expense             -*/
    /*-*********************************-*/
    public static void createExpense(Context context, long workorderId, String description, double price,
                                     ExpenseCategory category) {
        WorkorderTransactionBuilder.createExpense(context, workorderId,
                description, price, category);
    }

    public static void deleteExpense(Context context, long workorderId, long expenseId) {
        WorkorderTransactionBuilder.deleteExpense(context, workorderId, expenseId);
    }

    /*-*************************************-*/
    /*-             Shipments               -*/
    /*-*************************************-*/
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

    public static void actionCompleteShipmentTask(Context context, long workorderId, long taskId, String printName, String signatureJson) {
        WorkorderTransactionBuilder.actionCompleteShipmentTask(context, workorderId, taskId, printName, signatureJson);
    }

    public static void actionCompleteShipmentTask(Context context, long workorderId, long shipmentId, long taskId) {
        WorkorderTransactionBuilder.actionCompleteShipmentTask(context, workorderId, shipmentId, taskId);
    }

    public static void actionSetShipmentDetails(
            Context context, long workorderId, String description, boolean isToSite, String carrier,
            String carrierName, String trackingNumber) {
        WorkorderTransactionBuilder.actionSetShipmentDetails(context, workorderId, description,
                isToSite, carrier, carrierName, trackingNumber);
    }

    public static void actionSetShipmentDetails(
            Context context, long workorderId, String description, boolean isToSite, String carrier,
            String carrierName, String trackingNumber, long taskId) {

        WorkorderTransactionBuilder.actionSetShipmentDetails(context, workorderId, description,
                isToSite, carrier, carrierName, trackingNumber, taskId);
    }

    /*-**********************************-*/
    /*-             signature            -*/
    /*-**********************************-*/
    public static void getSignature(Context context, long workorderId, long signatureId) {
        getSignature(context, workorderId, signatureId, false);
    }

    public static void getSignature(Context context, long workorderId, long signatureId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_SIGNATURE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_SIGNATURE_ID, signatureId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subGetSignature(boolean isSync) {
        return subGetSignature(0, 0, isSync);
    }

    public boolean subGetSignature(long workorderId, long signatureId, boolean isSync) {
        String topicId = "";

        if (isSync) {
            topicId += "-SYNC";
        }

        if (workorderId > 0) {
            topicId += "/" + workorderId;

            if (signatureId > 0) {
                topicId += "/" + signatureId;
            }
        }

        return register(topicId, TAG);
    }

    // add signature json
    public static void addSignatureJson(Context context, long workorderId, String name, String json) {
        WorkorderTransactionBuilder.addSignatureJson(context, workorderId, name, json);
    }

    // complete signature
    public static void addSignatureJsonTask(Context context, long workorderId, long taskId, String name, String json) {
        WorkorderTransactionBuilder.addSignatureJsonTask(context, workorderId, taskId, name, json);
    }
    /*-******************************************-*/
    /*-             workorder actions            -*/
    /*-******************************************-*/

    public static void actionCustomField(Context context, long workorderId, long customFieldId, String value) {
        WorkorderTransactionBuilder.action(context,
                workorderId, "custom-fields/" + customFieldId, null,
                HttpJsonBuilder.HEADER_CONTENT_TYPE_FORM_ENCODED,
                "value=" + misc.escapeForURL(value));
    }

    // complete workorder
    public static void actionComplete(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionComplete(context, workorderId);
    }

    public static void actionSetClosingNotes(Context context, long workorderId, String closingNotes) {
        WorkorderTransactionBuilder.actionClosingNotes(context, workorderId, closingNotes);
    }

    // acknowledge hold
    public static void actionAcknowledgeHold(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionAcknowledgeHold(context, workorderId);
    }

    // counter offer
    public static void actionCounterOffer(Context context, long workorderId, boolean expires,
                                          String reason, int expiresAfterInSecond, Pay pay,
                                          Schedule schedule, Expense[] expenses) {
        WorkorderTransactionBuilder.actionCounterOffer(context, workorderId, expires, reason,
                expiresAfterInSecond, pay, schedule, expenses);
    }

    // request
    public static void actionRequest(Context context, long workorderId, long expireInSeconds) {
        WorkorderTransactionBuilder.actionRequest(context, workorderId, expireInSeconds);
    }

    public static void actionConfirmAssignment(Context context, long workorderId, String startTimeIso8601, String endTimeIso8601) {
        WorkorderTransactionBuilder.actionConfirmAssignment(context, workorderId, startTimeIso8601, endTimeIso8601);
    }

    public static void actionDecline(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionDecline(context, workorderId);
    }

    public static void actionWithdrawRequest(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionWithdrawRequest(context, workorderId);
    }

    /*-******************************************-*/
    /*-             workorder checkin            -*/
    /*-******************************************-*/
    public static void actionCheckin(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionCheckin(context, workorderId);
    }

    public static void actionCheckin(Context context, long workorderId, Location location) {
        WorkorderTransactionBuilder.actionCheckin(context, workorderId, location);
    }

    /*-*******************************************-*/
    /*-             workorder checkout            -*/
    /*-*******************************************-*/
    public static void actionCheckout(Context context, long workorderId) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId);
    }

    public static void actionCheckout(Context context, long workorderId, Location location) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, location);
    }

    public static void actionCheckout(Context context, long workorderId, int deviceCount) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, deviceCount);
    }

    public static void actionCheckout(Context context, long workorderId, int deviceCount, Location location) {
        WorkorderTransactionBuilder.actionCheckout(context, workorderId, deviceCount, location);
    }

    /*-*****************************************-*/
    /*-             workorder bundle            -*/
    /*-*****************************************-*/
    public static void getBundle(Context context, long bundleId) {
        getBundle(context, bundleId, false);
    }

    public static void getBundle(Context context, long bundleId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET_BUNDLE);
        intent.putExtra(PARAM_ID, bundleId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subBundle() {
        return subBundle(false);
    }

    public boolean subBundle(boolean isSync) {
        String topicId = PARAM_ACTION_GET_BUNDLE;

        if (isSync) {
            topicId += "-SYNC";
        }

        return register(topicId, TAG);
    }

    /*-*************************************-*/
    /*-             deliverables            -*/
    /*-*************************************-*/
    public static void uploadDeliverable(Context context, long workorderId, long uploadSlotId, String filename, String filePath) {
        Log.v(STAG, "requestUploadDeliverable");
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_UPLOAD_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_UPLOAD_SLOT_ID, uploadSlotId);
        intent.putExtra(PARAM_LOCAL_PATH, filePath);
        intent.putExtra(PARAM_FILE_NAME, filename);
        context.startService(intent);
    }

    public static void uploadDeliverable(final Context context, final long workorderId, final long uploadSlotId, Intent data) {
        FileHelper.getFileFromActivityResult(context, data, new FileHelper.Listener() {
            @Override
            public void fileReady(String filename, File file) {
                uploadDeliverable(context, workorderId, uploadSlotId, filename, file.getPath());
            }

            @Override
            public void fail(String reason) {
                Log.v("WorkorderDataClient.requestUploadDeliverable", reason);
            }
        });
    }

    public static void deleteDeliverable(Context context, long workorderId, long workorderUploadId, String filename) {
        WorkorderTransactionBuilder.deleteDeliverable(context, workorderId, workorderUploadId, filename);
    }

    public static void getDeliverable(Context context, long workorderId, long deliverableId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_DELIVERABLE_ID, deliverableId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public static void getDeliverable(Context context, long workorderId, long deliverableId, String url, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DOWNLOAD_DELIVERABLE);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_DELIVERABLE_ID, deliverableId);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public static void listDeliverables(Context context, long workorderId, boolean isSync) {
        Intent intent = new Intent(context, WorkorderService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_DELIVERABLE_LIST);
        intent.putExtra(PARAM_ID, workorderId);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subDeliverableList(boolean isSync) {
        String topicId = PARAM_ACTION_DELIVERABLE_LIST;

        if (isSync) {
            topicId += "-SYNC";
        }

        return register(topicId, TAG);
    }

    /*-*************************************-*/
    /*-             Time Log                -*/
    /*-*************************************-*/
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

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "topicId " + topicId);
            if (topicId.startsWith(PARAM_ACTION_LIST)) {
                preOnWorkorderList((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_DETAILS)) {
                preOnDetails((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_GET_SIGNATURE)) {
                preOnGetSignature((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_GET_BUNDLE)) {
                preOnGetBundle((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_DELIVERABLE_LIST)) {
                preDeliverableList((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_LIST_MESSAGES)) {
                preMessageList((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_LIST_NOTIFICATIONS)) {
                preAlertList((Bundle) payload);
            } else if (topicId.startsWith(PARAM_ACTION_LIST_TASKS)) {
                preTaskList((Bundle) payload);

                // WARN this must be the last check because it will match just about anything.
            } else if (topicId.startsWith(PARAM_ACTION)) {
                preOnAction((Bundle) payload);
            }
        }

        private void preOnAction(Bundle payload) {
            Log.v(STAG, "preOnAction " + payload.getLong(PARAM_ID) + " "
                    + payload.getString(PARAM_ACTION));
            onAction(payload.getLong(PARAM_ID),
                    payload.getString(PARAM_ACTION));
        }

        public void onAction(long workorderId, String action) {
        }

        private void preTaskList(Bundle payload) {
            Log.v(STAG, "preTaskList");
            new AsyncTaskEx<Object, Object, List<Task>>() {
                private long workorderId;

                @Override
                protected List<Task> doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];

                    workorderId = payload.getLong(PARAM_ID);
                    JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);
                    List<Task> list = new LinkedList<>();
                    for (int i = 0; i < ja.size(); i++) {
                        list.add(Task.fromJson(ja.getJsonObject(i)));
                    }

                    return list;
                }

                @Override
                protected void onPostExecute(List<Task> tasks) {
                    onTaskList(workorderId, tasks);
                }
            }.executeEx(payload);
        }

        public void onTaskList(long workorderId, List<Task> tasks) {
        }

        private void preAlertList(Bundle payload) {
            Log.v(STAG, "preAlertList");
            new AsyncTaskEx<Object, Object, List<Notification>>() {
                private long workorderId;

                @Override
                protected List<Notification> doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];

                    workorderId = payload.getLong(PARAM_ID);
                    JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);
                    List<Notification> list = new LinkedList<>();
                    for (int i = 0; i < ja.size(); i++) {
                        list.add(Notification.fromJson(ja.getJsonObject(i)));
                    }

                    return list;
                }

                @Override
                protected void onPostExecute(List<Notification> alerts) {
                    onAlertList(workorderId, alerts);
                }
            }.executeEx(payload);
        }

        public void onAlertList(long workorderId, List<Notification> alerts) {
        }

        private void preMessageList(Bundle payload) {
            new AsyncTaskEx<Object, Object, List<Message>>() {
                private long workorderId;

                @Override
                protected List<Message> doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];

                    workorderId = payload.getLong(PARAM_ID);
                    JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);
                    List<Message> list = new LinkedList<>();
                    for (int i = 0; i < ja.size(); i++) {
                        list.add(Message.fromJson(ja.getJsonObject(i)));
                    }

                    return list;
                }

                @Override
                protected void onPostExecute(List<Message> messages) {
                    onMessageList(workorderId, messages);
                }
            }.executeEx(payload);
        }

        public void onMessageList(long workorderId, List<Message> messages) {
        }

        private void preDeliverableList(Bundle payload) {
            new AsyncTaskEx<Object, Object, List<Deliverable>>() {
                private long workorderId;

                @Override
                protected List<Deliverable> doInBackground(Object... params) {
                    Bundle payload = (Bundle) params[0];

                    workorderId = payload.getLong(PARAM_ID);
                    JsonArray ja = payload.getParcelable(PARAM_DATA_PARCELABLE);

                    List<Deliverable> list = new LinkedList<>();
                    for (int i = 0; i < ja.size(); i++) {
                        list.add(Deliverable.fromJson(ja.getJsonObject(i)));
                    }

                    return list;
                }

                @Override
                protected void onPostExecute(List<Deliverable> o) {
                    onDeliverableList(o, workorderId);
                }
            }.executeEx(payload);

        }

        public void onDeliverableList(List<Deliverable> list, long workorderId) {
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
                        selector = WorkorderDataSelector.fromCall(bundle.getString(PARAM_LIST_SELECTOR));
                        Log.v(STAG, "Selector " + bundle.getString(PARAM_LIST_SELECTOR));
                        page = bundle.getInt(PARAM_PAGE);
                        List<Workorder> list = new LinkedList<>();
                        JsonArray ja = bundle.getParcelable(PARAM_DATA_PARCELABLE);
                        for (int i = 0; i < ja.size(); i++) {
                            list.add(Workorder.fromJson(ja.getJsonObject(i)));
                        }
                        return list;
                    } catch (Exception ex) {
//                        Log.v(STAG, selector.name());
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
                        return Workorder.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Workorder workorder) {
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
                        return Signature.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
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
                        return com.fieldnation.data.workorder.Bundle.fromJson((JsonObject) bundle.getParcelable(PARAM_DATA_PARCELABLE));
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

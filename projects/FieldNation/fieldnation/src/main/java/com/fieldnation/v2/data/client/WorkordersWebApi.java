package com.fieldnation.v2.data.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.tracker.TrackerEnum;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;
import com.fieldnation.v2.data.listener.CacheDispatcher;
import com.fieldnation.v2.data.listener.TransactionListener;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Assignee;
import com.fieldnation.v2.data.model.Attachment;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.AttachmentFolders;
import com.fieldnation.v2.data.model.Cancellation;
import com.fieldnation.v2.data.model.Contact;
import com.fieldnation.v2.data.model.Contacts;
import com.fieldnation.v2.data.model.CustomField;
import com.fieldnation.v2.data.model.CustomFields;
import com.fieldnation.v2.data.model.ETA;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.EtaMassAccept;
import com.fieldnation.v2.data.model.EtaMassAcceptWithLocation;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Expenses;
import com.fieldnation.v2.data.model.Hold;
import com.fieldnation.v2.data.model.Holds;
import com.fieldnation.v2.data.model.Location;
import com.fieldnation.v2.data.model.Message;
import com.fieldnation.v2.data.model.Messages;
import com.fieldnation.v2.data.model.Milestones;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayIncrease;
import com.fieldnation.v2.data.model.PayIncreases;
import com.fieldnation.v2.data.model.PayModifier;
import com.fieldnation.v2.data.model.PayModifiers;
import com.fieldnation.v2.data.model.Problem;
import com.fieldnation.v2.data.model.Problems;
import com.fieldnation.v2.data.model.Qualifications;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Signatures;
import com.fieldnation.v2.data.model.SwapResponse;
import com.fieldnation.v2.data.model.Tag;
import com.fieldnation.v2.data.model.Tags;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.TaskAlert;
import com.fieldnation.v2.data.model.Tasks;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.Users;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrderOverview;
import com.fieldnation.v2.data.model.WorkOrders;

/**
 * Created by dmgen from swagger.
 */

public class WorkordersWebApi extends TopicClient {
    private static final String STAG = "WorkordersWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static int connectCount = 0;

    public WorkordersWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public void connect(Context context) {
        super.connect(context);
        connectCount++;
        Log.v(STAG + ".state", "connect " + connectCount);
    }

    @Override
    public void disconnect(Context context) {
        super.disconnect(context);
        connectCount--;
        Log.v(STAG + ".state", "disconnect " + connectCount);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subWorkordersWebApi() {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi");
    }

    /**
     * Swagger operationId: acceptIncreaseByWorkOrder
     * Accept pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void acceptIncrease(Context context, Integer workOrderId, Integer increaseId) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "acceptIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: acceptSwapRequest
     * Accept work order swap request.
     */
    public static void acceptSwapRequest(Context context) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "acceptSwapRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addAlertToWorkOrderAndTask
     * Sets up an alert to be fired upon the completion of a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param json        JSON Model
     */
    public static void addAlertToWorkOrderAndTask(Context context, Integer workOrderId, Integer taskId, TaskAlert json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addAlertToWorkOrderAndTask", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addAttachmentByWorkOrderAndFolder
     * Uploads a file by an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param attachment  Folder
     * @param file        File
     */
    public static void addAttachment(Context context, Integer workOrderId, Integer folderId, Attachment attachment, java.io.File file) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .multipartField("attachment", attachment.getJson(), "application/json")
                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("attachment", attachment.getJson());
            methodParams.put("file", Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addAttachment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .setTrack(true)
                    .setTrackType(TrackerEnum.DELIVERABLES)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addAttachmentByWorkOrderAndFolder
     * Uploads a file by an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param attachment  Folder
     * @param file        File
     * @param async       Async (Optional)
     */
    public static void addAttachment(Context context, Integer workOrderId, Integer folderId, Attachment attachment, java.io.File file, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async)
                    .multipartField("attachment", attachment.getJson(), "application/json")
                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("async", async);
            methodParams.put("attachment", attachment.getJson());
            methodParams.put("file", Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addAttachment", methodParams))
                    .useAuth(true)
                    .setTrack(true)
                    .setTrackType(TrackerEnum.DELIVERABLES)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addBonusByWorkOrderAndBonus
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId     Bonus ID
     */
    public static void addBonus(Context context, Integer workOrderId, Integer bonusId) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("bonusId", bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addBonus", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addBonusByWorkOrderAndBonus
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId     Bonus ID
     * @param bonus       Bonus (Optional)
     */
    public static void addBonus(Context context, Integer workOrderId, Integer bonusId, PayModifier bonus) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            if (bonus != null)
                builder.body(bonus.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("bonusId", bonusId);
            if (bonus != null)
                methodParams.put("bonus", bonus.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addBonus", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addContactByWorkOrder
     * Adds a contact to a work order
     *
     * @param workOrderId Work order id
     * @param contact     JSON Model
     */
    public static void addContact(Context context, Integer workOrderId, Contact contact) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/contacts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts");

            if (contact != null)
                builder.body(contact.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (contact != null)
                methodParams.put("contact", contact.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/contacts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addContact", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addDiscountByWorkOrder
     * Assigned provider route to adds and apply a discount to a work order which reduces the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param json        Payload of the discount
     */
    public static void addDiscount(Context context, Integer workOrderId, PayModifier json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/discounts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/discounts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addDiscount", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addExpenseByWorkOrder
     * Adds an expense on a work order
     *
     * @param workOrderId ID of work order
     * @param expense     Expense
     */
    public static void addExpense(Context context, Integer workOrderId, Expense expense) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/expenses");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses");

            if (expense != null)
                builder.body(expense.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (expense != null)
                methodParams.put("expense", expense.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addExpense", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addExpenseByWorkOrder
     * Adds an expense on a work order
     *
     * @param workOrderId ID of work order
     * @param expense     Expense
     * @param async       Asynchroneous (Optional)
     */
    public static void addExpense(Context context, Integer workOrderId, Expense expense, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/expenses?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses")
                    .urlParams("?async=" + async);

            if (expense != null)
                builder.body(expense.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (expense != null)
                methodParams.put("expense", expense.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addExpense", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addFolderByWorkOrder
     * Adds an attachment folder
     *
     * @param workOrderId Work order id
     * @param folder      Folder
     */
    public static void addFolder(Context context, Integer workOrderId, AttachmentFolder folder) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments");

            if (folder != null)
                builder.body(folder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (folder != null)
                methodParams.put("folder", folder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addFolder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addFolderByWorkOrder
     * Adds an attachment folder
     *
     * @param workOrderId Work order id
     * @param folder      Folder
     * @param async       Async (Optional)
     */
    public static void addFolder(Context context, Integer workOrderId, AttachmentFolder folder, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments")
                    .urlParams("?async=" + async);

            if (folder != null)
                builder.body(folder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (folder != null)
                methodParams.put("folder", folder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addFolder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addHoldByWorkOrder
     * Adds a hold on a work order
     *
     * @param workOrderId ID of work order
     * @param hold        Hold object with updates
     */
    public static void addHold(Context context, Integer workOrderId, Hold hold) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/holds");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds");

            if (hold != null)
                builder.body(hold.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (hold != null)
                methodParams.put("hold", hold.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addHold", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addHoldByWorkOrder
     * Adds a hold on a work order
     *
     * @param workOrderId ID of work order
     * @param hold        Hold object with updates
     * @param async       Async (Optional)
     */
    public static void addHold(Context context, Integer workOrderId, Hold hold, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/holds?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds")
                    .urlParams("?async=" + async);

            if (hold != null)
                builder.body(hold.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (hold != null)
                methodParams.put("hold", hold.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addHold", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addIncreaseByWorkOrder
     * Create pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increase    Increase structure for update
     */
    public static void addIncrease(Context context, Integer workOrderId, PayIncrease increase) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/increases");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases");

            if (increase != null)
                builder.body(increase.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (increase != null)
                methodParams.put("increase", increase.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addIncreaseByWorkOrder
     * Create pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increase    Increase structure for update
     * @param async       Async (Optional)
     */
    public static void addIncrease(Context context, Integer workOrderId, PayIncrease increase, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/increases?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases")
                    .urlParams("?async=" + async);

            if (increase != null)
                builder.body(increase.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (increase != null)
                methodParams.put("increase", increase.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addMessageByWorkOrder
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     */
    public static void addMessage(Context context, Integer workOrderId, Message json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addMessage", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addMessageByWorkOrder
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     * @param async       Async (Optional)
     */
    public static void addMessage(Context context, Integer workOrderId, Message json, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages")
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addMessage", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addPenaltyByWorkOrderAndPenalty
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     */
    public static void addPenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("penaltyId", penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addPenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addPenaltyByWorkOrderAndPenalty
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     * @param penalty     Penalty (Optional)
     */
    public static void addPenalty(Context context, Integer workOrderId, Integer penaltyId, PayModifier penalty) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            if (penalty != null)
                builder.body(penalty.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("penaltyId", penaltyId);
            if (penalty != null)
                methodParams.put("penalty", penalty.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addPenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addProblemByWorkOrder
     * Reports a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problem     Problem
     */
    public static void addProblem(Context context, Integer workOrderId, Problem problem) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/problems");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems");

            if (problem != null)
                builder.body(problem.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (problem != null)
                methodParams.put("problem", problem.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/problems")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addProblem", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addProblemByWorkOrder
     * Reports a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problem     Problem
     * @param async       Async (Optional)
     */
    public static void addProblem(Context context, Integer workOrderId, Problem problem, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/problems?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems")
                    .urlParams("?async=" + async);

            if (problem != null)
                builder.body(problem.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (problem != null)
                methodParams.put("problem", problem.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/problems")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addProblem", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addQualificationByWorkOrder
     * Add qualification for work order.
     *
     * @param workOrderId    ID of work order
     * @param qualifications Qualification information
     */
    public static void addQualification(Context context, Integer workOrderId, Qualifications qualifications) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/qualifications");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/qualifications");

            if (qualifications != null)
                builder.body(qualifications.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (qualifications != null)
                methodParams.put("qualifications", qualifications.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/qualifications")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addQualification", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addShipmentByWorkOrder
     * Adds a shipment to a work order
     *
     * @param workOrderId Work order id
     * @param shipment    Shipment
     */
    public static void addShipment(Context context, Integer workOrderId, Shipment shipment) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/shipments");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments");

            if (shipment != null)
                builder.body(shipment.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (shipment != null)
                methodParams.put("shipment", shipment.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addShipment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addShipmentByWorkOrder
     * Adds a shipment to a work order
     *
     * @param workOrderId Work order id
     * @param shipment    Shipment
     * @param async       Async (Optional)
     */
    public static void addShipment(Context context, Integer workOrderId, Shipment shipment, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/shipments?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments")
                    .urlParams("?async=" + async);

            if (shipment != null)
                builder.body(shipment.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (shipment != null)
                methodParams.put("shipment", shipment.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addShipment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addSignatureByWorkOrder
     * Add signature by work order
     *
     * @param workOrderId ID of work order
     * @param signature   Signature JSON
     */
    public static void addSignature(Context context, Integer workOrderId, Signature signature) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/signatures");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures");

            if (signature != null)
                builder.body(signature.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (signature != null)
                methodParams.put("signature", signature.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addSignature", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addSignatureByWorkOrder
     * Add signature by work order
     *
     * @param workOrderId ID of work order
     * @param signature   Signature JSON
     * @param async       async (Optional)
     */
    public static void addSignature(Context context, Integer workOrderId, Signature signature, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/signatures?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures")
                    .urlParams("?async=" + async);

            if (signature != null)
                builder.body(signature.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (signature != null)
                methodParams.put("signature", signature.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addSignature", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addTagByWorkOrder
     * Adds a tag to a work order
     *
     * @param workOrderId ID of work order
     * @param tag         Tag
     */
    public static void addTag(Context context, Integer workOrderId, Tag tag) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tags");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags");

            if (tag != null)
                builder.body(tag.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (tag != null)
                methodParams.put("tag", tag.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tags")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addTagByWorkOrder
     * Adds a tag to a work order
     *
     * @param workOrderId ID of work order
     * @param tag         Tag
     * @param async       Async (Optional)
     */
    public static void addTag(Context context, Integer workOrderId, Tag tag, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tags?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags")
                    .urlParams("?async=" + async);

            if (tag != null)
                builder.body(tag.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (tag != null)
                methodParams.put("tag", tag.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tags")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addTaskByWorkOrder
     * Adds a task to a work order
     *
     * @param workOrderId Work order id
     * @param json        JSON Model
     */
    public static void addTask(Context context, Integer workOrderId, Task json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tasks");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks");

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addTask", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addTimeLogByWorkOrder
     * Add time log for work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog     Check in information
     */
    public static void addTimeLog(Context context, Integer workOrderId, TimeLog timeLog) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/time_logs");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs");

            if (timeLog != null)
                builder.body(timeLog.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (timeLog != null)
                methodParams.put("timeLog", timeLog.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: addWorkOrder
     * Create a work order
     *
     * @param workOrder Work order
     */
    public static void addWorkOrder(Context context, WorkOrder workOrder) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders");

            if (workOrder != null)
                builder.body(workOrder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            if (workOrder != null)
                methodParams.put("workOrder", workOrder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: approveWorkOrderByWorkOrder
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     */
    public static void approveWorkOrder(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/approve");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "approveWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: approveWorkOrderByWorkOrder
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     * @param async       Async (Optional)
     */
    public static void approveWorkOrder(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/approve?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "approveWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: assignUserByWorkOrder
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee    JSON Model
     */
    public static void assignUser(Context context, Integer workOrderId, Assignee assignee) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/assignee");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee");

            if (assignee != null)
                builder.body(assignee.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (assignee != null)
                methodParams.put("assignee", assignee.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "assignUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: assignUserByWorkOrder
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee    JSON Model
     * @param async       Async (Optional)
     */
    public static void assignUser(Context context, Integer workOrderId, Assignee assignee, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/assignee?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee")
                    .urlParams("?async=" + async);

            if (assignee != null)
                builder.body(assignee.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (assignee != null)
                methodParams.put("assignee", assignee.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "assignUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: cancelSwapRequest
     * Cancel work order swap request.
     */
    public static void cancelSwapRequest(Context context) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "cancelSwapRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: completeWorkOrderByWorkOrder
     * Marks a work order complete and moves it to work done status
     *
     * @param workOrderId ID of work order
     */
    public static void completeWorkOrder(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/complete");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "completeWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: completeWorkOrderByWorkOrder
     * Marks a work order complete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param async       Async (Optional)
     */
    public static void completeWorkOrder(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/complete?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "completeWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: declineByWorkOrder
     * Decline a work order
     *
     * @param workOrderId Work order id
     */
    public static void decline(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/declines");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/declines");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/declines")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "decline", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: declineByWorkOrder
     * Decline a work order
     *
     * @param workOrderId Work order id
     * @param async       Async (Optional)
     */
    public static void decline(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/declines?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/declines")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/declines")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "decline", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: declineRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param userId      User id
     */
    public static void declineRequest(Context context, Integer workOrderId, Integer userId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/declines/" + userId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/declines/" + userId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("userId", userId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/declines/{user_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "declineRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: declineRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param userId      User id
     * @param async       Async (Optional)
     */
    public static void declineRequest(Context context, Integer workOrderId, Integer userId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/declines/" + userId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/declines/" + userId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("userId", userId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/declines/{user_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "declineRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: declineSwapRequest
     * Decline work order swap request.
     */
    public static void declineSwapRequest(Context context) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "declineSwapRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteAlertByWorkOrderAndTask
     * Delete a single alert associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param alertId     Alert id
     */
    public static void deleteAlert(Context context, Integer workOrderId, Integer taskId, Integer alertId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);
            methodParams.put("alertId", alertId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts/{alert_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteAlert", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteAlertsByWorkOrderAndTask
     * Delete all alerts associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void deleteAlerts(Context context, Integer workOrderId, Integer taskId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteAlerts", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteAttachmentByWorkOrderAndFolderAndAttachment
     * Deletes an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     */
    public static void deleteAttachment(Context context, Integer workOrderId, Integer folderId, Integer attachmentId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("attachmentId", attachmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteAttachment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteAttachmentByWorkOrderAndFolderAndAttachment
     * Deletes an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     * @param async        Async (Optional)
     */
    public static void deleteAttachment(Context context, Integer workOrderId, Integer folderId, Integer attachmentId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("attachmentId", attachmentId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteAttachment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteBonusByWorkOrderAndBonus
     * Deletes a bonus from a work order
     *
     * @param workOrderId Work Order ID
     * @param bonusId     Bonus ID
     */
    public static void deleteBonus(Context context, Integer workOrderId, Integer bonusId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("bonusId", bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteBonus", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteContactByWorkOrderAndContact
     * Delete a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId   Contact id
     */
    public static void deleteContact(Context context, Integer workOrderId, Integer contactId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("contactId", contactId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteContact", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteDiscountByWorkOrder
     * Allows an assigned provider to removes a discount they previously applied from a work order, increasing the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param discountId  ID of the discount
     */
    public static void deleteDiscount(Context context, Integer workOrderId, Integer discountId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("discountId", discountId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteDiscount", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteExpenseByWorkOrderAndExpense
     * Delete an expense from a work order
     *
     * @param workOrderId ID of work order
     * @param expenseId   ID of expense
     */
    public static void deleteExpense(Context context, Integer workOrderId, Integer expenseId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("expenseId", expenseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteExpense", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteExpenseByWorkOrderAndExpense
     * Delete an expense from a work order
     *
     * @param workOrderId ID of work order
     * @param expenseId   ID of expense
     * @param async       Asynchroneous (Optional)
     */
    public static void deleteExpense(Context context, Integer workOrderId, Integer expenseId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("expenseId", expenseId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteExpense", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteFolderByWorkOrderAndFolder
     * Deletes an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     */
    public static void deleteFolder(Context context, Integer workOrderId, Integer folderId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteFolder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteFolderByWorkOrderAndFolder
     * Deletes an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param async       Async (Optional)
     */
    public static void deleteFolder(Context context, Integer workOrderId, Integer folderId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteFolder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteHoldByWorkOrderAndHold
     * Deletes a hold on a work order
     *
     * @param workOrderId ID of work order
     * @param holdId      ID of hold to update
     */
    public static void deleteHold(Context context, Integer workOrderId, Integer holdId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("holdId", holdId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteHold", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteHoldByWorkOrderAndHold
     * Deletes a hold on a work order
     *
     * @param workOrderId ID of work order
     * @param holdId      ID of hold to update
     * @param async       Async (Optional)
     */
    public static void deleteHold(Context context, Integer workOrderId, Integer holdId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("holdId", holdId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteHold", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteIncreaseByWorkOrderAndIncrease
     * Delete pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void deleteIncrease(Context context, Integer workOrderId, Integer increaseId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteIncreaseByWorkOrderAndIncrease
     * Delete pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     * @param async       Async (Optional)
     */
    public static void deleteIncrease(Context context, Integer workOrderId, Integer increaseId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteMessageByWorkOrder
     * Deletes a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     */
    public static void deleteMessage(Context context, Integer workOrderId, Integer messageId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("messageId", messageId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteMessage", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deletePenaltyByWorkOrderAndPenalty
     * Removes a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId ID of Work Order
     * @param penaltyId   Penalty ID
     */
    public static void deletePenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("penaltyId", penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deletePenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteProblemByWorkOrder
     * Deletes a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problemId   ID of work order
     */
    public static void deleteProblem(Context context, Integer workOrderId, Integer problemId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("problemId", problemId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteProblem", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteProblemByWorkOrder
     * Deletes a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problemId   ID of work order
     * @param async       Async (Optional)
     */
    public static void deleteProblem(Context context, Integer workOrderId, Integer problemId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("problemId", problemId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteProblem", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param requestId   ID of work order request/counter offer
     */
    public static void deleteRequest(Context context, Integer workOrderId, Integer requestId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("requestId", requestId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param requestId   ID of work order request/counter offer
     * @param async       Async (Optional)
     */
    public static void deleteRequest(Context context, Integer workOrderId, Integer requestId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("requestId", requestId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteRequest", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteShipmentByWorkOrderAndShipment
     * Deletes a shipment from a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId  Shipment id
     */
    public static void deleteShipment(Context context, Integer workOrderId, Integer shipmentId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("shipmentId", shipmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteShipment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteShipmentByWorkOrderAndShipment
     * Deletes a shipment from a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId  Shipment id
     * @param async       Async (Optional)
     */
    public static void deleteShipment(Context context, Integer workOrderId, Integer shipmentId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("shipmentId", shipmentId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteShipment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteSignatureByWorkOrderAndSignature
     * Delete signature by work order and signature
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
     */
    public static void deleteSignature(Context context, Integer workOrderId, Integer signatureId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("signatureId", signatureId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteSignature", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteSignatureByWorkOrderAndSignature
     * Delete signature by work order and signature
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
     * @param async       async (Optional)
     */
    public static void deleteSignature(Context context, Integer workOrderId, Integer signatureId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("signatureId", signatureId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteSignature", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteTagByWorkOrderAndTag
     * Deletes a tag on a work order
     *
     * @param workOrderId ID of work order
     * @param tagId       ID of work order
     */
    public static void deleteTag(Context context, Integer workOrderId, Integer tagId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("tagId", tagId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tags/{tag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteTagByWorkOrderAndTag
     * Deletes a tag on a work order
     *
     * @param workOrderId ID of work order
     * @param tagId       ID of work order
     * @param async       Async (Optional)
     */
    public static void deleteTag(Context context, Integer workOrderId, Integer tagId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("tagId", tagId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tags/{tag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteTaskByWorkOrder
     * Delete a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void deleteTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteTask", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteTimeLogByWorkOrder
     * Remove time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     */
    public static void deleteTimeLog(Context context, Integer workOrderId, Integer workorderHoursId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("workorderHoursId", workorderHoursId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteTimeLogByWorkOrder
     * Remove time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async            Return the model in the response (slower) (Optional)
     */
    public static void deleteTimeLog(Context context, Integer workOrderId, Integer workorderHoursId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("workorderHoursId", workorderHoursId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteWorkOrderByWorkOrder
     * Deletes a work order by its id
     *
     * @param workOrderId  ID of work order
     * @param cancellation Cancellation reasons
     */
    public static void deleteWorkOrder(Context context, Integer workOrderId, Cancellation cancellation) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId);

            if (cancellation != null)
                builder.body(cancellation.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (cancellation != null)
                methodParams.put("cancellation", cancellation.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: deleteWorkOrderByWorkOrder
     * Deletes a work order by its id
     *
     * @param workOrderId  ID of work order
     * @param cancellation Cancellation reasons
     * @param async        Async (Optional)
     */
    public static void deleteWorkOrder(Context context, Integer workOrderId, Cancellation cancellation, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId)
                    .urlParams("?async=" + async);

            if (cancellation != null)
                builder.body(cancellation.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (cancellation != null)
                methodParams.put("cancellation", cancellation.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "deleteWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: denyIncreaseByWorkOrder
     * Deny pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void denyIncrease(Context context, Integer workOrderId, Integer increaseId) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/deny")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "denyIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getAssigneeByWorkOrder
     * Get assignee of a work order
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getAssignee(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/assignee");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getAssignee", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getAttachmentsByWorkOrder
     * Gets a list of attachment folders which contain files and deliverables for the work order
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getAttachments(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/attachments");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getAttachments", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getBonusByWorkOrderAndBonus
     * Gets a bonus for a work order
     *
     * @param workOrderId  ID of work order
     * @param bonusId      Bonus ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getBonus(Context context, Integer workOrderId, Integer bonusId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("bonusId", bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getBonus", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getBonusByWorkOrderAndBonus
     * Gets a bonus for a work order
     *
     * @param workOrderId  ID of work order
     * @param bonusId      Bonus ID
     * @param bonus        Bonus (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void getBonus(Context context, Integer workOrderId, Integer bonusId, PayModifier bonus, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            if (bonus != null)
                builder.body(bonus.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("bonusId", bonusId);
            if (bonus != null)
                methodParams.put("bonus", bonus.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getBonus", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getBonusesByWorkOrder
     * Get a list of available bonuses on a work order that can be applied to increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId  Work Order ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getBonuses(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/bonuses");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getBonuses", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getContactsByWorkOrder
     * Get a list of contacts on a work order
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getContacts(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/contacts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/contacts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getContacts", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getCustomFieldByWorkOrderAndCustomField
     * Get a custom field by work order and custom field
     *
     * @param workOrderId   ID of work order
     * @param customFieldId Custom field id
     * @param isBackground  indicates that this call is low priority
     */
    public static void getCustomField(Context context, Integer workOrderId, Integer customFieldId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("customFieldId", customFieldId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getCustomField", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getCustomFieldsByWorKOrder
     * Get a list of custom fields and their values for a work order.
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomFields(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/custom_fields");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getCustomFields", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getDiscountsByWorkOrder
     * Returns a list of discounts applied by the assigned provider to reduce the payout of the work order.
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getDiscounts(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/discounts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/discounts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getDiscounts", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getETAByWorkOrder
     * Gets the eta for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getETA(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/eta");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/eta");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/eta")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getETA", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getExpensesByWorkOrder
     * Get all expenses of a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getExpenses(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/expenses");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getExpenses", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getFileByWorkOrderAndFolderAndFile
     * Gets an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     * @param isBackground indicates that this call is low priority
     */
    public static void getFile(Context context, Integer workOrderId, Integer folderId, Integer attachmentId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("attachmentId", attachmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getFile", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getFolderByWorkOrderAndFolder
     * Gets an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param isBackground indicates that this call is low priority
     */
    public static void getFolder(Context context, Integer workOrderId, Integer folderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getFolder", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getHoldByWorkOrderAndHold
     * Gets a hold on a work order
     *
     * @param workOrderId  ID of work order
     * @param holdId       ID of hold to update
     * @param isBackground indicates that this call is low priority
     */
    public static void getHold(Context context, Integer workOrderId, Integer holdId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("holdId", holdId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getHold", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getHoldByWorkOrderAndHold
     * Gets a hold on a work order
     *
     * @param workOrderId  ID of work order
     * @param holdId       ID of hold to update
     * @param async        Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void getHold(Context context, Integer workOrderId, Integer holdId, Boolean async, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("holdId", holdId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getHold", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getHoldsByWorkOrder
     * Get holds on work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getHolds(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/holds");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getHolds", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getIncreaseByWorkOrderAndIncrease
     * Get pay increase for assigned work order.
     *
     * @param workOrderId  ID of work order
     * @param increaseId   ID of work order increase
     * @param isBackground indicates that this call is low priority
     */
    public static void getIncrease(Context context, Integer workOrderId, Integer increaseId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getIncrease", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getIncreaseByWorkOrderAndIncrease
     * Get pay increase for assigned work order.
     *
     * @param workOrderId  ID of work order
     * @param increaseId   ID of work order increase
     * @param async        Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void getIncrease(Context context, Integer workOrderId, Integer increaseId, Boolean async, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getIncrease", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getIncreasesByWorkOrder
     * Returns a list of pay increases requested by the assigned provider.
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getIncreases(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/increases");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getIncreases", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getLocationByWorkOrder
     * Gets the address and geo information for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getLocation(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/location");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/location");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/location")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getLocation", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getMessagesByWorkOrder
     * Gets a list of work order messages
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getMessages(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/messages");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getMessages", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getMilestonesByWorkOrder
     * Get the milestones of a work order
     *
     * @param workOrderId  ID of Work Order
     * @param isBackground indicates that this call is low priority
     */
    public static void getMilestones(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/milestones");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/milestones");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/milestones")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getMilestones", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getOverviewByWorkOrder
     * Gets overview by work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getOverview(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/overview");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/overview");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/overview")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getOverview", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getPayByWorkOrder
     * Gets the pay for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getPay(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/pay");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/pay");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getPay", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getPenaltiesByWorkOrder
     * Get a list of penalties and their applied status for a work order
     *
     * @param workOrderId  Work Order ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getPenalties(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/penalties");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/penalties")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getPenalties", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getPenaltyByWorkOrderAndPenalty
     * Gets a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId  Work Order ID
     * @param penaltyId    Penalty ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getPenalty(Context context, Integer workOrderId, Integer penaltyId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("penaltyId", penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getPenalty", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getProblemByWorkOrderAndProblem
     * Gets information about a problem on a work order
     *
     * @param workOrderId  ID of work order
     * @param problemId    ID of problem
     * @param isBackground indicates that this call is low priority
     */
    public static void getProblem(Context context, Integer workOrderId, Integer problemId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("problemId", problemId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProblem", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getProblemsByWorkOrder
     * Gets problems reported on a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getProblems(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/problems");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/problems")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProblems", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getProvidersByWorkOrder
     * Gets list of providers available for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getProviders(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/providers");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/providers");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProviders", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getProvidersByWorkOrder
     * Gets list of providers available for a work order
     *
     * @param workOrderId         ID of work order
     * @param getProvidersOptions Additional optional parameters
     * @param isBackground        indicates that this call is low priority
     */
    public static void getProviders(Context context, Integer workOrderId, GetProvidersOptions getProvidersOptions, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/providers" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                    + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                    + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/providers")
                    .urlParams("" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                            + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                            + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                    );

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProviders", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getQualificationsByWorkOrder
     * Returns a list of qualifications applied to work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getQualifications(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/qualifications");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/qualifications");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/qualifications")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getQualifications", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getRequestByWorkOrderAndRequest
     * Get request/ counter offer for assigned work order.
     *
     * @param workOrderId  ID of work order
     * @param requestId    ID of work order request/counter offer
     * @param isBackground indicates that this call is low priority
     */
    public static void getRequest(Context context, Integer workOrderId, Integer requestId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("requestId", requestId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getRequest", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getRequestByWorkOrderAndRequest
     * Get request/ counter offer for assigned work order.
     *
     * @param workOrderId  ID of work order
     * @param requestId    ID of work order request/counter offer
     * @param async        Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void getRequest(Context context, Integer workOrderId, Integer requestId, Boolean async, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId)
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("requestId", requestId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getRequest", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getRequestsByWorkOrder
     * Returns a list of work order requests or counter offers requested by the assigned provider.
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getRequests(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/requests");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getRequests", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: GetScheduleAndLocation
     * Get schedule and location for a list of work orders by work orders
     *
     * @param workOrderId  array of work order ids
     * @param isBackground indicates that this call is low priority
     */
    public static void GetScheduleAndLocation(Context context, Integer[] workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/mass-accept?work_order_id=" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/mass-accept")
                    .urlParams("?work_order_id=" + workOrderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/mass-accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "GetScheduleAndLocation", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getScheduleByWorkOrder
     * Gets the service schedule for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getSchedule(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/schedule");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/schedule");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getSchedule", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getShipmentsByWorkOrder
     * Get a list of shipments on a work order
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getShipments(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/shipments");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getShipments", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getSignatureByWorkOrderAndSignature
     * Gets a single signature uploaded by the assigned provider
     *
     * @param workOrderId  ID of work order
     * @param signatureId  ID of signature
     * @param isBackground indicates that this call is low priority
     */
    public static void getSignature(Context context, Integer workOrderId, Integer signatureId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("signatureId", signatureId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getSignature", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getSignaturesByWorkOrder
     * Returns a list of signatures uploaded by the assigned provider
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getSignatures(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/signatures");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getSignatures", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getStatusByWorkOrder
     * Gets the current real-time status for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getStatus(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/status");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/status");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/status")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getStatus", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getTagByWorkOrderAndTag
     * Gets a tag on a work order
     *
     * @param workOrderId  ID of work order
     * @param tagId        ID of tag
     * @param isBackground indicates that this call is low priority
     */
    public static void getTag(Context context, Integer workOrderId, Integer tagId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("tagId", tagId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tags/{tag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTag", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getTagsByWorkOrder
     * Gets tags/labels reported on a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getTags(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/tags");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tags")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTags", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getTaskByWorkOrder
     * Get a task by work order
     *
     * @param workOrderId  Work order id
     * @param taskId       Task id
     * @param isBackground indicates that this call is low priority
     */
    public static void getTask(Context context, Integer workOrderId, Integer taskId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTask", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getTasksByWorkOrder
     * Get a list of a work order's tasks
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getTasks(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/tasks");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tasks")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTasks", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getTimeLogsByWorkOrder
     * Returns a list of time logs applied by the assigned provider
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getTimeLogs(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/time_logs");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTimeLogs", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getWorkOrderByWorkOrder
     * Gets a work order by its id
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getWorkOrder(Context context, Integer workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrder", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getWorkOrderLists
     * Pre-filters results by a certain category or type, settings by list are persisted with 'sticky' by user.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getWorkOrderLists(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/lists");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/lists");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/lists")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrderLists", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getWorkOrders
     * Returns a list of work orders.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getWorkOrders(Context context, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders");

            JsonObject methodParams = new JsonObject();

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrders", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: getWorkOrders
     * Returns a list of work orders.
     *
     * @param getWorkOrdersOptions Additional optional parameters
     * @param isBackground         indicates that this call is low priority
     */
    public static void getWorkOrders(Context context, GetWorkOrdersOptions getWorkOrdersOptions, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders"
                    + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
                    + (getWorkOrdersOptions.getColumns() != null ? "&columns=" + getWorkOrdersOptions.getColumns() : "")
                    + (getWorkOrdersOptions.getPage() != null ? "&page=" + getWorkOrdersOptions.getPage() : "")
                    + (getWorkOrdersOptions.getPerPage() != null ? "&per_page=" + getWorkOrdersOptions.getPerPage() : "")
                    + (getWorkOrdersOptions.getView() != null ? "&view=" + getWorkOrdersOptions.getView() : "")
                    + (getWorkOrdersOptions.getSticky() != null ? "&sticky=" + getWorkOrdersOptions.getSticky() : "")
                    + (getWorkOrdersOptions.getSort() != null ? "&sort=" + getWorkOrdersOptions.getSort() : "")
                    + (getWorkOrdersOptions.getOrder() != null ? "&order=" + getWorkOrdersOptions.getOrder() : "")
                    + (getWorkOrdersOptions.getF() != null ? "&f_=" + getWorkOrdersOptions.getF() : "")
                    + (getWorkOrdersOptions.getFMaxApprovalTime() != null ? "&f_max_approval_time=" + getWorkOrdersOptions.getFMaxApprovalTime() : "")
                    + (getWorkOrdersOptions.getFRating() != null ? "&f_rating=" + getWorkOrdersOptions.getFRating() : "")
                    + (getWorkOrdersOptions.getFFlightboardTomorrow() != null ? "&f_flightboard_tomorrow=" + getWorkOrdersOptions.getFFlightboardTomorrow() : "")
                    + (getWorkOrdersOptions.getFRequests() != null ? "&f_requests=" + getWorkOrdersOptions.getFRequests() : "")
                    + (getWorkOrdersOptions.getFCounterOffers() != null ? "&f_counter_offers=" + getWorkOrdersOptions.getFCounterOffers() : "")
                    + (getWorkOrdersOptions.getFHourly() != null ? "&f_hourly=" + getWorkOrdersOptions.getFHourly() : "")
                    + (getWorkOrdersOptions.getFFixed() != null ? "&f_fixed=" + getWorkOrdersOptions.getFFixed() : "")
                    + (getWorkOrdersOptions.getFDevice() != null ? "&f_device=" + getWorkOrdersOptions.getFDevice() : "")
                    + (getWorkOrdersOptions.getFPay() != null ? "&f_pay=" + getWorkOrdersOptions.getFPay() : "")
                    + (getWorkOrdersOptions.getFTemplates() != null ? "&f_templates=" + getWorkOrdersOptions.getFTemplates() : "")
                    + (getWorkOrdersOptions.getFTypeOfWork() != null ? "&f_type_of_work=" + getWorkOrdersOptions.getFTypeOfWork() : "")
                    + (getWorkOrdersOptions.getFTimeZone() != null ? "&f_time_zone=" + getWorkOrdersOptions.getFTimeZone() : "")
                    + (getWorkOrdersOptions.getFMode() != null ? "&f_mode=" + getWorkOrdersOptions.getFMode() : "")
                    + (getWorkOrdersOptions.getFCompany() != null ? "&f_company=" + getWorkOrdersOptions.getFCompany() : "")
                    + (getWorkOrdersOptions.getFWorkedWith() != null ? "&f_worked_with=" + getWorkOrdersOptions.getFWorkedWith() : "")
                    + (getWorkOrdersOptions.getFManager() != null ? "&f_manager=" + getWorkOrdersOptions.getFManager() : "")
                    + (getWorkOrdersOptions.getFClient() != null ? "&f_client=" + getWorkOrdersOptions.getFClient() : "")
                    + (getWorkOrdersOptions.getFProject() != null ? "&f_project=" + getWorkOrdersOptions.getFProject() : "")
                    + (getWorkOrdersOptions.getFApprovalWindow() != null ? "&f_approval_window=" + getWorkOrdersOptions.getFApprovalWindow() : "")
                    + (getWorkOrdersOptions.getFReviewWindow() != null ? "&f_review_window=" + getWorkOrdersOptions.getFReviewWindow() : "")
                    + (getWorkOrdersOptions.getFNetwork() != null ? "&f_network=" + getWorkOrdersOptions.getFNetwork() : "")
                    + (getWorkOrdersOptions.getFAutoAssign() != null ? "&f_auto_assign=" + getWorkOrdersOptions.getFAutoAssign() : "")
                    + (getWorkOrdersOptions.getFSchedule() != null ? "&f_schedule=" + getWorkOrdersOptions.getFSchedule() : "")
                    + (getWorkOrdersOptions.getFCreated() != null ? "&f_created=" + getWorkOrdersOptions.getFCreated() : "")
                    + (getWorkOrdersOptions.getFPublished() != null ? "&f_published=" + getWorkOrdersOptions.getFPublished() : "")
                    + (getWorkOrdersOptions.getFRouted() != null ? "&f_routed=" + getWorkOrdersOptions.getFRouted() : "")
                    + (getWorkOrdersOptions.getFPublishedRouted() != null ? "&f_published_routed=" + getWorkOrdersOptions.getFPublishedRouted() : "")
                    + (getWorkOrdersOptions.getFCompleted() != null ? "&f_completed=" + getWorkOrdersOptions.getFCompleted() : "")
                    + (getWorkOrdersOptions.getFApprovedCancelled() != null ? "&f_approved_cancelled=" + getWorkOrdersOptions.getFApprovedCancelled() : "")
                    + (getWorkOrdersOptions.getFConfirmed() != null ? "&f_confirmed=" + getWorkOrdersOptions.getFConfirmed() : "")
                    + (getWorkOrdersOptions.getFAssigned() != null ? "&f_assigned=" + getWorkOrdersOptions.getFAssigned() : "")
                    + (getWorkOrdersOptions.getFSavedLocation() != null ? "&f_saved_location=" + getWorkOrdersOptions.getFSavedLocation() : "")
                    + (getWorkOrdersOptions.getFSavedLocationGroup() != null ? "&f_saved_location_group=" + getWorkOrdersOptions.getFSavedLocationGroup() : "")
                    + (getWorkOrdersOptions.getFCity() != null ? "&f_city=" + getWorkOrdersOptions.getFCity() : "")
                    + (getWorkOrdersOptions.getFState() != null ? "&f_state=" + getWorkOrdersOptions.getFState() : "")
                    + (getWorkOrdersOptions.getFPostalCode() != null ? "&f_postal_code=" + getWorkOrdersOptions.getFPostalCode() : "")
                    + (getWorkOrdersOptions.getFCountry() != null ? "&f_country=" + getWorkOrdersOptions.getFCountry() : "")
                    + (getWorkOrdersOptions.getFFlags() != null ? "&f_flags=" + getWorkOrdersOptions.getFFlags() : "")
                    + (getWorkOrdersOptions.getFAssignment() != null ? "&f_assignment=" + getWorkOrdersOptions.getFAssignment() : "")
                    + (getWorkOrdersOptions.getFConfirmation() != null ? "&f_confirmation=" + getWorkOrdersOptions.getFConfirmation() : "")
                    + (getWorkOrdersOptions.getFFinancing() != null ? "&f_financing=" + getWorkOrdersOptions.getFFinancing() : "")
                    + (getWorkOrdersOptions.getFRemoteWork() != null ? "&f_remote_work=" + getWorkOrdersOptions.getFRemoteWork() : "")
                    + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                    + (getWorkOrdersOptions.getFLocationRadius() != null ?
                    ("&f_location_radius[]=" + getWorkOrdersOptions.getFLocationRadius()[0]
                            + "&f_location_radius[]=" + getWorkOrdersOptions.getFLocationRadius()[1]) : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders")
                    .urlParams("" + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
                            + (getWorkOrdersOptions.getColumns() != null ? "&columns=" + getWorkOrdersOptions.getColumns() : "")
                            + (getWorkOrdersOptions.getPage() != null ? "&page=" + getWorkOrdersOptions.getPage() : "")
                            + (getWorkOrdersOptions.getPerPage() != null ? "&per_page=" + getWorkOrdersOptions.getPerPage() : "")
                            + (getWorkOrdersOptions.getView() != null ? "&view=" + getWorkOrdersOptions.getView() : "")
                            + (getWorkOrdersOptions.getSticky() != null ? "&sticky=" + getWorkOrdersOptions.getSticky() : "")
                            + (getWorkOrdersOptions.getSort() != null ? "&sort=" + getWorkOrdersOptions.getSort() : "")
                            + (getWorkOrdersOptions.getOrder() != null ? "&order=" + getWorkOrdersOptions.getOrder() : "")
                            + (getWorkOrdersOptions.getF() != null ? "&f_=" + getWorkOrdersOptions.getF() : "")
                            + (getWorkOrdersOptions.getFMaxApprovalTime() != null ? "&f_max_approval_time=" + getWorkOrdersOptions.getFMaxApprovalTime() : "")
                            + (getWorkOrdersOptions.getFRating() != null ? "&f_rating=" + getWorkOrdersOptions.getFRating() : "")
                            + (getWorkOrdersOptions.getFFlightboardTomorrow() != null ? "&f_flightboard_tomorrow=" + getWorkOrdersOptions.getFFlightboardTomorrow() : "")
                            + (getWorkOrdersOptions.getFRequests() != null ? "&f_requests=" + getWorkOrdersOptions.getFRequests() : "")
                            + (getWorkOrdersOptions.getFCounterOffers() != null ? "&f_counter_offers=" + getWorkOrdersOptions.getFCounterOffers() : "")
                            + (getWorkOrdersOptions.getFHourly() != null ? "&f_hourly=" + getWorkOrdersOptions.getFHourly() : "")
                            + (getWorkOrdersOptions.getFFixed() != null ? "&f_fixed=" + getWorkOrdersOptions.getFFixed() : "")
                            + (getWorkOrdersOptions.getFDevice() != null ? "&f_device=" + getWorkOrdersOptions.getFDevice() : "")
                            + (getWorkOrdersOptions.getFPay() != null ? "&f_pay=" + getWorkOrdersOptions.getFPay() : "")
                            + (getWorkOrdersOptions.getFTemplates() != null ? "&f_templates=" + getWorkOrdersOptions.getFTemplates() : "")
                            + (getWorkOrdersOptions.getFTypeOfWork() != null ? "&f_type_of_work=" + getWorkOrdersOptions.getFTypeOfWork() : "")
                            + (getWorkOrdersOptions.getFTimeZone() != null ? "&f_time_zone=" + getWorkOrdersOptions.getFTimeZone() : "")
                            + (getWorkOrdersOptions.getFMode() != null ? "&f_mode=" + getWorkOrdersOptions.getFMode() : "")
                            + (getWorkOrdersOptions.getFCompany() != null ? "&f_company=" + getWorkOrdersOptions.getFCompany() : "")
                            + (getWorkOrdersOptions.getFWorkedWith() != null ? "&f_worked_with=" + getWorkOrdersOptions.getFWorkedWith() : "")
                            + (getWorkOrdersOptions.getFManager() != null ? "&f_manager=" + getWorkOrdersOptions.getFManager() : "")
                            + (getWorkOrdersOptions.getFClient() != null ? "&f_client=" + getWorkOrdersOptions.getFClient() : "")
                            + (getWorkOrdersOptions.getFProject() != null ? "&f_project=" + getWorkOrdersOptions.getFProject() : "")
                            + (getWorkOrdersOptions.getFApprovalWindow() != null ? "&f_approval_window=" + getWorkOrdersOptions.getFApprovalWindow() : "")
                            + (getWorkOrdersOptions.getFReviewWindow() != null ? "&f_review_window=" + getWorkOrdersOptions.getFReviewWindow() : "")
                            + (getWorkOrdersOptions.getFNetwork() != null ? "&f_network=" + getWorkOrdersOptions.getFNetwork() : "")
                            + (getWorkOrdersOptions.getFAutoAssign() != null ? "&f_auto_assign=" + getWorkOrdersOptions.getFAutoAssign() : "")
                            + (getWorkOrdersOptions.getFSchedule() != null ? "&f_schedule=" + getWorkOrdersOptions.getFSchedule() : "")
                            + (getWorkOrdersOptions.getFCreated() != null ? "&f_created=" + getWorkOrdersOptions.getFCreated() : "")
                            + (getWorkOrdersOptions.getFPublished() != null ? "&f_published=" + getWorkOrdersOptions.getFPublished() : "")
                            + (getWorkOrdersOptions.getFRouted() != null ? "&f_routed=" + getWorkOrdersOptions.getFRouted() : "")
                            + (getWorkOrdersOptions.getFPublishedRouted() != null ? "&f_published_routed=" + getWorkOrdersOptions.getFPublishedRouted() : "")
                            + (getWorkOrdersOptions.getFCompleted() != null ? "&f_completed=" + getWorkOrdersOptions.getFCompleted() : "")
                            + (getWorkOrdersOptions.getFApprovedCancelled() != null ? "&f_approved_cancelled=" + getWorkOrdersOptions.getFApprovedCancelled() : "")
                            + (getWorkOrdersOptions.getFConfirmed() != null ? "&f_confirmed=" + getWorkOrdersOptions.getFConfirmed() : "")
                            + (getWorkOrdersOptions.getFAssigned() != null ? "&f_assigned=" + getWorkOrdersOptions.getFAssigned() : "")
                            + (getWorkOrdersOptions.getFSavedLocation() != null ? "&f_saved_location=" + getWorkOrdersOptions.getFSavedLocation() : "")
                            + (getWorkOrdersOptions.getFSavedLocationGroup() != null ? "&f_saved_location_group=" + getWorkOrdersOptions.getFSavedLocationGroup() : "")
                            + (getWorkOrdersOptions.getFCity() != null ? "&f_city=" + getWorkOrdersOptions.getFCity() : "")
                            + (getWorkOrdersOptions.getFState() != null ? "&f_state=" + getWorkOrdersOptions.getFState() : "")
                            + (getWorkOrdersOptions.getFPostalCode() != null ? "&f_postal_code=" + getWorkOrdersOptions.getFPostalCode() : "")
                            + (getWorkOrdersOptions.getFCountry() != null ? "&f_country=" + getWorkOrdersOptions.getFCountry() : "")
                            + (getWorkOrdersOptions.getFFlags() != null ? "&f_flags=" + getWorkOrdersOptions.getFFlags() : "")
                            + (getWorkOrdersOptions.getFAssignment() != null ? "&f_assignment=" + getWorkOrdersOptions.getFAssignment() : "")
                            + (getWorkOrdersOptions.getFConfirmation() != null ? "&f_confirmation=" + getWorkOrdersOptions.getFConfirmation() : "")
                            + (getWorkOrdersOptions.getFFinancing() != null ? "&f_financing=" + getWorkOrdersOptions.getFFinancing() : "")
                            + (getWorkOrdersOptions.getFRemoteWork() != null ? "&f_remote_work=" + getWorkOrdersOptions.getFRemoteWork() : "")
                            + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                            + (getWorkOrdersOptions.getFLocationRadius() != null ? ("&f_location_radius[]=" + getWorkOrdersOptions.getFLocationRadius()[0]
                            + "&f_location_radius[]=" + getWorkOrdersOptions.getFLocationRadius()[1]) : "")
                    );

            JsonObject methodParams = new JsonObject();
            methodParams.put("getWorkOrdersOptions", getWorkOrdersOptions.toJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrders", methodParams))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);

            if (allowCacheResponse) new CacheDispatcher(context, key);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: groupTaskByWorkOrderAndTask
     * Regroups a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param group       New group
     * @param destination beginning or end (position in new group)
     */
    public static void groupTask(Context context, Integer workOrderId, Integer taskId, String group, String destination) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);
            methodParams.put("group", group);
            methodParams.put("destination", destination);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/group/{group}/{destination}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "groupTask", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: incompleteWorkOrderByWorkOrder
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId ID of work order
     */
    public static void incompleteWorkOrder(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/complete");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "incompleteWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: incompleteWorkOrderByWorkOrder
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId                ID of work order
     * @param incompleteWorkOrderOptions Additional optional parameters
     */
    public static void incompleteWorkOrder(Context context, Integer workOrderId, IncompleteWorkOrderOptions incompleteWorkOrderOptions) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/complete" + (incompleteWorkOrderOptions.getReason() != null ? "?reason=" + incompleteWorkOrderOptions.getReason() : "")
                    + (incompleteWorkOrderOptions.getAsync() != null ? "&async=" + incompleteWorkOrderOptions.getAsync() : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete")
                    .urlParams("" + (incompleteWorkOrderOptions.getReason() != null ? "?reason=" + incompleteWorkOrderOptions.getReason() : "")
                            + (incompleteWorkOrderOptions.getAsync() != null ? "&async=" + incompleteWorkOrderOptions.getAsync() : "")
                    );

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "incompleteWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: MassAcceptWorkOrderByWorkOrder
     * Mass Accept with ETA
     *
     * @param eta JSON Payload
     */
    public static void MassAcceptWorkOrder(Context context, EtaMassAccept eta) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/mass-accept");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/mass-accept");

            if (eta != null)
                builder.body(eta.getJson().toString());

            JsonObject methodParams = new JsonObject();
            if (eta != null)
                methodParams.put("eta", eta.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/mass-accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "MassAcceptWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: MassAcceptWorkOrderByWorkOrder
     * Mass Accept with ETA
     *
     * @param eta   JSON Payload
     * @param async Async (Optional)
     */
    public static void MassAcceptWorkOrder(Context context, EtaMassAccept eta, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/mass-accept?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/mass-accept")
                    .urlParams("?async=" + async);

            if (eta != null)
                builder.body(eta.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("async", async);
            if (eta != null)
                methodParams.put("eta", eta.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/mass-accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "MassAcceptWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: massRequests
     * Work order mass requests
     *
     * @param requests JSON Payload
     */
    public static void massRequests(Context context, String requests) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/mass-requests");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/mass-requests");

            if (requests != null)
                builder.body(requests);

            JsonObject methodParams = new JsonObject();
            if (requests != null)
                methodParams.put("requests", requests);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/mass-requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "massRequests", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: publishByWorkOrder
     * Publishes a work order to the marketplace where it can garner requests.
     *
     * @param workOrderId ID of work order
     */
    public static void publish(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/publish");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "publish", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: publishByWorkOrder
     * Publishes a work order to the marketplace where it can garner requests.
     *
     * @param workOrderId ID of work order
     * @param async       Async (Optional)
     */
    public static void publish(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/publish?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "publish", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeQualificationByWorkOrder
     * Remove qualification work order
     *
     * @param workOrderId     ID of work order
     * @param qualificationId ID of qualification
     */
    public static void removeQualification(Context context, Integer workOrderId, Integer qualificationId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/qualifications/" + qualificationId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/qualifications/" + qualificationId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("qualificationId", qualificationId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/qualifications/{qualification_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeQualification", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: reorderTaskByWorkOrderAndTaskAndTargetTask
     * Reorders a task associated with a work order to a position before or after a target task
     *
     * @param workOrderId  Work order id
     * @param taskId       Task id
     * @param targetTaskId Target task id
     * @param position     before or after target task
     */
    public static void reorderTask(Context context, Integer workOrderId, Integer taskId, Integer targetTaskId, String position) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);
            methodParams.put("targetTaskId", targetTaskId);
            methodParams.put("position", position);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/reorder/{position}/{target_task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "reorderTask", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: replyMessageByWorkOrder
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void replyMessage(Context context, Integer workOrderId, Integer messageId, Message json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("messageId", messageId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "replyMessage", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: replyMessageByWorkOrder
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     * @param async       Async (Optional)
     */
    public static void replyMessage(Context context, Integer workOrderId, Integer messageId, Message json, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId)
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("messageId", messageId);
            methodParams.put("async", async);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "replyMessage", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: requestByWorkOrder
     * Request or un-hide a request for a work order
     *
     * @param workOrderId Work order id
     * @param request     JSON Model
     */
    public static void request(Context context, Integer workOrderId, Request request) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/requests");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests");

            if (request != null)
                builder.body(request.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (request != null)
                methodParams.put("request", request.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "request", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: requestByWorkOrder
     * Request or un-hide a request for a work order
     *
     * @param workOrderId Work order id
     * @param request     JSON Model
     * @param async       Async (Optional)
     */
    public static void request(Context context, Integer workOrderId, Request request, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/requests?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests")
                    .urlParams("?async=" + async);

            if (request != null)
                builder.body(request.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (request != null)
                methodParams.put("request", request.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "request", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: revertWorkOrderToDraftByWorkOrder
     * Reverts a work order to draft status
     *
     * @param workOrderId ID of work order
     */
    public static void revertWorkOrderToDraft(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/draft");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/draft");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/draft")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "revertWorkOrderToDraft", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: revertWorkOrderToDraftByWorkOrder
     * Reverts a work order to draft status
     *
     * @param workOrderId ID of work order
     * @param async       Async (Optional)
     */
    public static void revertWorkOrderToDraft(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/draft?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/draft")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/draft")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "revertWorkOrderToDraft", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: routeUserByWorkOrder
     * Route a user to a work order
     *
     * @param workOrderId Work order id
     * @param route       JSON Model
     */
    public static void routeUser(Context context, Integer workOrderId, Route route) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/route");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route");

            if (route != null)
                builder.body(route.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (route != null)
                methodParams.put("route", route.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "routeUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: routeUserByWorkOrder
     * Route a user to a work order
     *
     * @param workOrderId Work order id
     * @param route       JSON Model
     * @param async       Async (Optional)
     */
    public static void routeUser(Context context, Integer workOrderId, Route route, Boolean async) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/route?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route")
                    .urlParams("?async=" + async);

            if (route != null)
                builder.body(route.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (route != null)
                methodParams.put("route", route.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "routeUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unapproveWorkOrderByWorkOrder
     * Unapproves a completed work order and moves it to work done status
     *
     * @param workOrderId ID of work order
     */
    public static void unapproveWorkOrder(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/approve");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unapproveWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unapproveWorkOrderByWorkOrder
     * Unapproves a completed work order and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param async       Async (Optional)
     */
    public static void unapproveWorkOrder(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/approve?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unapproveWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unassignUserByWorkOrder
     * Unassign user from a work order
     *
     * @param workOrderId Work order id
     * @param assignee    JSON Model
     */
    public static void unassignUser(Context context, Integer workOrderId, Assignee assignee) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/assignee");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee");

            if (assignee != null)
                builder.body(assignee.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (assignee != null)
                methodParams.put("assignee", assignee.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unassignUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unassignUserByWorkOrder
     * Unassign user from a work order
     *
     * @param workOrderId Work order id
     * @param assignee    JSON Model
     * @param async       Async (Optional)
     */
    public static void unassignUser(Context context, Integer workOrderId, Assignee assignee, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/assignee?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee")
                    .urlParams("?async=" + async);

            if (assignee != null)
                builder.body(assignee.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (assignee != null)
                methodParams.put("assignee", assignee.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unassignUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unpublishByWorkOrder
     * Unpublishes a work order from the marketplace so that no requests or counter-offers can be made. Moves to draft unless it was also routed.
     *
     * @param workOrderId ID of work order
     */
    public static void unpublish(Context context, Integer workOrderId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/publish");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unpublish", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unpublishByWorkOrder
     * Unpublishes a work order from the marketplace so that no requests or counter-offers can be made. Moves to draft unless it was also routed.
     *
     * @param workOrderId ID of work order
     * @param async       Async (Optional)
     */
    public static void unpublish(Context context, Integer workOrderId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/publish?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unpublish", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unRouteUserByWorkOrder
     * Unroute a user from a work order
     *
     * @param workOrderId Work order id
     * @param route       JSON Model
     */
    public static void unRouteUser(Context context, Integer workOrderId, Route route) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/route");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route");

            if (route != null)
                builder.body(route.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (route != null)
                methodParams.put("route", route.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unRouteUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: unRouteUserByWorkOrder
     * Unroute a user from a work order
     *
     * @param workOrderId Work order id
     * @param route       JSON Model
     * @param async       Async (Optional)
     */
    public static void unRouteUser(Context context, Integer workOrderId, Route route, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/route?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route")
                    .urlParams("?async=" + async);

            if (route != null)
                builder.body(route.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (route != null)
                methodParams.put("route", route.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "unRouteUser", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateAllTimeLogsByWorkOrder
     * Update all time logs for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog     Check in information
     */
    public static void updateAllTimeLogs(Context context, Integer workOrderId, TimeLog timeLog) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs");

            if (timeLog != null)
                builder.body(timeLog.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (timeLog != null)
                methodParams.put("timeLog", timeLog.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateAllTimeLogs", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateAllTimeLogsByWorkOrder
     * Update all time logs for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog     Check in information
     * @param async       Return the model in the response (slower) (Optional)
     */
    public static void updateAllTimeLogs(Context context, Integer workOrderId, TimeLog timeLog, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs")
                    .urlParams("?async=" + async);

            if (timeLog != null)
                builder.body(timeLog.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (timeLog != null)
                methodParams.put("timeLog", timeLog.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateAllTimeLogs", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateAttachmentByWorkOrderAndFolderAndAttachment
     * Updates an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     * @param attachment   Attachment
     */
    public static void updateAttachment(Context context, Integer workOrderId, Integer folderId, Integer attachmentId, Attachment attachment) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            if (attachment != null)
                builder.body(attachment.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("attachmentId", attachmentId);
            if (attachment != null)
                methodParams.put("attachment", attachment.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateAttachment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateAttachmentByWorkOrderAndFolderAndAttachment
     * Updates an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     * @param attachment   Attachment
     * @param async        Async (Optional)
     */
    public static void updateAttachment(Context context, Integer workOrderId, Integer folderId, Integer attachmentId, Attachment attachment, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                    .urlParams("?async=" + async);

            if (attachment != null)
                builder.body(attachment.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("attachmentId", attachmentId);
            methodParams.put("async", async);
            if (attachment != null)
                methodParams.put("attachment", attachment.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateAttachment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateBonusByWorkOrderAndBonus
     * Updates a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId     Bonus ID
     * @param bonus       Bonus
     */
    public static void updateBonus(Context context, Integer workOrderId, Integer bonusId, PayModifier bonus) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            if (bonus != null)
                builder.body(bonus.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("bonusId", bonusId);
            if (bonus != null)
                methodParams.put("bonus", bonus.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateBonus", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateContactByWorkOrderAndContact
     * Update contact of a work order
     *
     * @param workOrderId Work order id
     * @param contactId   Contact id
     * @param contact     JSON Model
     */
    public static void updateContact(Context context, Integer workOrderId, Integer contactId, Contact contact) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            if (contact != null)
                builder.body(contact.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("contactId", contactId);
            if (contact != null)
                methodParams.put("contact", contact.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateContact", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateCustomFieldByWorkOrderAndCustomField
     * Update a custom field value on a work order
     *
     * @param workOrderId   Work Order ID
     * @param customFieldId Custom field ID
     * @param customField   Custom field
     */
    public static void updateCustomField(Context context, Integer workOrderId, Integer customFieldId, CustomField customField) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            if (customField != null)
                builder.body(customField.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("customFieldId", customFieldId);
            if (customField != null)
                methodParams.put("customField", customField.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateCustomField", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateCustomFieldByWorkOrderAndCustomField
     * Update a custom field value on a work order
     *
     * @param workOrderId   Work Order ID
     * @param customFieldId Custom field ID
     * @param customField   Custom field
     * @param async         Async (Optional)
     */
    public static void updateCustomField(Context context, Integer workOrderId, Integer customFieldId, CustomField customField, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId)
                    .urlParams("?async=" + async);

            if (customField != null)
                builder.body(customField.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("customFieldId", customFieldId);
            methodParams.put("async", async);
            if (customField != null)
                methodParams.put("customField", customField.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateCustomField", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateDiscountByWorkOrder
     * Updates the amount or description of a discount applied to the work order.
     *
     * @param workOrderId ID of work order
     * @param discountId  ID of the discount
     * @param json        Payload of the discount
     */
    public static void updateDiscount(Context context, Integer workOrderId, Integer discountId, PayModifier json) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("discountId", discountId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateDiscount", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateETAByWorkOrder
     * Updates the eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param eta         JSON Payload
     */
    public static void updateETA(Context context, Integer workOrderId, ETA eta) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/eta");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/eta");

            if (eta != null)
                builder.body(eta.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (eta != null)
                methodParams.put("eta", eta.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/eta")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateETA", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateETAByWorkOrder
     * Updates the eta of a work order (depending on your role)
     *
     * @param workOrderId      ID of work order
     * @param eta              JSON Payload
     * @param updateETAOptions Additional optional parameters
     */
    public static void updateETA(Context context, Integer workOrderId, ETA eta, UpdateETAOptions updateETAOptions) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/eta" + (updateETAOptions.getConfirm() != null ? "?confirm=" + updateETAOptions.getConfirm() : "")
                    + (updateETAOptions.getUpdateFromIvr() != null ? "&update_from_ivr=" + updateETAOptions.getUpdateFromIvr() : "")
                    + (updateETAOptions.getAsync() != null ? "&async=" + updateETAOptions.getAsync() : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/eta")
                    .urlParams("" + (updateETAOptions.getConfirm() != null ? "?confirm=" + updateETAOptions.getConfirm() : "")
                            + (updateETAOptions.getUpdateFromIvr() != null ? "&update_from_ivr=" + updateETAOptions.getUpdateFromIvr() : "")
                            + (updateETAOptions.getAsync() != null ? "&async=" + updateETAOptions.getAsync() : "")
                    );

            if (eta != null)
                builder.body(eta.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (eta != null)
                methodParams.put("eta", eta.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/eta")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateETA", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateExpenseByWorkOrderAndExpense
     * Update an Expense of a Work order
     *
     * @param workOrderId ID of work order
     * @param expenseId   ID of expense
     */
    public static void updateExpense(Context context, Integer workOrderId, Integer expenseId) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("expenseId", expenseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateExpense", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateExpenseByWorkOrderAndExpense
     * Update an Expense of a Work order
     *
     * @param workOrderId          ID of work order
     * @param expenseId            ID of expense
     * @param updateExpenseOptions Additional optional parameters
     */
    public static void updateExpense(Context context, Integer workOrderId, Integer expenseId, UpdateExpenseOptions updateExpenseOptions) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "" + (updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
            );

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId)
                    .urlParams("" + (updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
                    );

            if (updateExpenseOptions.getExpense() != null)
                builder.body(updateExpenseOptions.getExpense().getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("expenseId", expenseId);
            if (updateExpenseOptions.getExpense() != null)
                methodParams.put("expense", updateExpenseOptions.getExpense().getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateExpense", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateFolderByWorkOrderAndFolder
     * Updates an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param folder      Folder
     */
    public static void updateFolder(Context context, Integer workOrderId, Integer folderId, AttachmentFolder folder) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            if (folder != null)
                builder.body(folder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            if (folder != null)
                methodParams.put("folder", folder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateFolder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateFolderByWorkOrderAndFolder
     * Updates an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param folder      Folder
     * @param async       Async (Optional)
     */
    public static void updateFolder(Context context, Integer workOrderId, Integer folderId, AttachmentFolder folder, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async);

            if (folder != null)
                builder.body(folder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("folderId", folderId);
            methodParams.put("async", async);
            if (folder != null)
                methodParams.put("folder", folder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateFolder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateHoldByWorkOrderAndHold
     * Updates a hold on a work order
     *
     * @param workOrderId ID of work order
     * @param holdId      ID of hold to update
     * @param hold        Hold object with updates
     */
    public static void updateHold(Context context, Integer workOrderId, Integer holdId, Hold hold) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId);

            if (hold != null)
                builder.body(hold.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("holdId", holdId);
            if (hold != null)
                methodParams.put("hold", hold.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateHold", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateHoldByWorkOrderAndHold
     * Updates a hold on a work order
     *
     * @param workOrderId ID of work order
     * @param holdId      ID of hold to update
     * @param hold        Hold object with updates
     * @param async       Async (Optional)
     */
    public static void updateHold(Context context, Integer workOrderId, Integer holdId, Hold hold, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds/" + holdId)
                    .urlParams("?async=" + async);

            if (hold != null)
                builder.body(hold.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("holdId", holdId);
            methodParams.put("async", async);
            if (hold != null)
                methodParams.put("hold", hold.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateHold", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateIncreaseByWorkOrderAndIncrease
     * Update pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     * @param increase    Increase structure for update
     */
    public static void updateIncrease(Context context, Integer workOrderId, Integer increaseId, PayIncrease increase) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            if (increase != null)
                builder.body(increase.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);
            if (increase != null)
                methodParams.put("increase", increase.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateIncreaseByWorkOrderAndIncrease
     * Update pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     * @param increase    Increase structure for update
     * @param async       Async (Optional)
     */
    public static void updateIncrease(Context context, Integer workOrderId, Integer increaseId, PayIncrease increase, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId)
                    .urlParams("?async=" + async);

            if (increase != null)
                builder.body(increase.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("increaseId", increaseId);
            methodParams.put("async", async);
            if (increase != null)
                methodParams.put("increase", increase.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateIncrease", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateLocationByWorkOrder
     * Updates the location of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param location    JSON Payload
     */
    public static void updateLocation(Context context, Integer workOrderId, Location location) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/location");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/location");

            if (location != null)
                builder.body(location.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (location != null)
                methodParams.put("location", location.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/location")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateLocation", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateLocationByWorkOrder
     * Updates the location of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param location    JSON Payload
     * @param async       Async (Optional)
     */
    public static void updateLocation(Context context, Integer workOrderId, Location location, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/location?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/location")
                    .urlParams("?async=" + async);

            if (location != null)
                builder.body(location.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (location != null)
                methodParams.put("location", location.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/location")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateLocation", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateMessageByWorkOrder
     * Updates a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void updateMessage(Context context, Integer workOrderId, Integer messageId, Message json) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("messageId", messageId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateMessage", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updatePayByWorkOrder
     * Updates the pay of a work order, or requests an adjustment
     *
     * @param workOrderId ID of work order
     * @param pay         Pay
     */
    public static void updatePay(Context context, Integer workOrderId, Pay pay) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/pay");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/pay");

            if (pay != null)
                builder.body(pay.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (pay != null)
                methodParams.put("pay", pay.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updatePay", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updatePayByWorkOrder
     * Updates the pay of a work order, or requests an adjustment
     *
     * @param workOrderId ID of work order
     * @param pay         Pay
     * @param async       Async (Optional)
     */
    public static void updatePay(Context context, Integer workOrderId, Pay pay, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/pay?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/pay")
                    .urlParams("?async=" + async);

            if (pay != null)
                builder.body(pay.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (pay != null)
                methodParams.put("pay", pay.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updatePay", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updatePenaltyByWorkOrderAndPenalty
     * Updates a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     */
    public static void updatePenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("penaltyId", penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updatePenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updatePenaltyByWorkOrderAndPenalty
     * Updates a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     * @param penalty     Penalty (Optional)
     */
    public static void updatePenalty(Context context, Integer workOrderId, Integer penaltyId, PayModifier penalty) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            if (penalty != null)
                builder.body(penalty.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("penaltyId", penaltyId);
            if (penalty != null)
                methodParams.put("penalty", penalty.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updatePenalty", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateProblemByWorkOrderAndProblem
     * Updates a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problemId   ID of work order
     * @param problem     Problem
     */
    public static void updateProblem(Context context, Integer workOrderId, Integer problemId, Problem problem) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            if (problem != null)
                builder.body(problem.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("problemId", problemId);
            if (problem != null)
                methodParams.put("problem", problem.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateProblem", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateProblemByWorkOrderAndProblem
     * Updates a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problemId   ID of work order
     * @param problem     Problem
     * @param async       Async (Optional)
     */
    public static void updateProblem(Context context, Integer workOrderId, Integer problemId, Problem problem, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId)
                    .urlParams("?async=" + async);

            if (problem != null)
                builder.body(problem.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("problemId", problemId);
            methodParams.put("async", async);
            if (problem != null)
                methodParams.put("problem", problem.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateProblem", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateQualificationByWorkOrder
     * Update qualification for work order.
     *
     * @param workOrderId    ID of work order
     * @param qualifications Qualification information
     */
    public static void updateQualification(Context context, Integer workOrderId, Qualifications qualifications) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/qualifications");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/qualifications");

            if (qualifications != null)
                builder.body(qualifications.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (qualifications != null)
                methodParams.put("qualifications", qualifications.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/qualifications")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateQualification", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateScheduleByWorkOrder
     * Updates the service schedule or eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param schedule    JSON Payload
     */
    public static void updateSchedule(Context context, Integer workOrderId, Schedule schedule) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/schedule");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/schedule");

            if (schedule != null)
                builder.body(schedule.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (schedule != null)
                methodParams.put("schedule", schedule.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateSchedule", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateScheduleByWorkOrder
     * Updates the service schedule or eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param schedule    JSON Payload
     * @param async       Async (Optional)
     */
    public static void updateSchedule(Context context, Integer workOrderId, Schedule schedule, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/schedule?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/schedule")
                    .urlParams("?async=" + async);

            if (schedule != null)
                builder.body(schedule.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (schedule != null)
                methodParams.put("schedule", schedule.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateSchedule", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateShipmentByWorkOrderAndShipment
     * Updates a shipment attached to a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId  Shipment id
     * @param shipment    Shipment
     */
    public static void updateShipment(Context context, Integer workOrderId, Integer shipmentId, Shipment shipment) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            if (shipment != null)
                builder.body(shipment.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("shipmentId", shipmentId);
            if (shipment != null)
                methodParams.put("shipment", shipment.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateShipment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateShipmentByWorkOrderAndShipment
     * Updates a shipment attached to a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId  Shipment id
     * @param shipment    Shipment
     * @param async       Async (Optional)
     */
    public static void updateShipment(Context context, Integer workOrderId, Integer shipmentId, Shipment shipment, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId)
                    .urlParams("?async=" + async);

            if (shipment != null)
                builder.body(shipment.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("shipmentId", shipmentId);
            methodParams.put("async", async);
            if (shipment != null)
                methodParams.put("shipment", shipment.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateShipment", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTagByWorkOrderAndProblem
     * Updates a tag on a work order
     *
     * @param workOrderId ID of work order
     * @param tagId       ID of work order
     * @param tag         Tag
     */
    public static void updateTag(Context context, Integer workOrderId, Integer tagId, Tag tag) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId);

            if (tag != null)
                builder.body(tag.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("tagId", tagId);
            if (tag != null)
                methodParams.put("tag", tag.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tags/{tag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTagByWorkOrderAndProblem
     * Updates a tag on a work order
     *
     * @param workOrderId ID of work order
     * @param tagId       ID of work order
     * @param tag         Tag
     * @param async       Async (Optional)
     */
    public static void updateTag(Context context, Integer workOrderId, Integer tagId, Tag tag, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tags/" + tagId)
                    .urlParams("?async=" + async);

            if (tag != null)
                builder.body(tag.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("tagId", tagId);
            methodParams.put("async", async);
            if (tag != null)
                methodParams.put("tag", tag.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tags/{tag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateTag", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTaskByWorkOrder
     * Updates a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param json        JSON Model
     */
    public static void updateTask(Context context, Integer workOrderId, Integer taskId, Task json) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            if (json != null)
                builder.body(json.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("taskId", taskId);
            if (json != null)
                methodParams.put("json", json.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateTask", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTimeLogByWorkOrder
     * Update time log for assigned work order.
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param timeLog          Check in information
     */
    public static void updateTimeLog(Context context, Integer workOrderId, Integer workorderHoursId, TimeLog timeLog) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            if (timeLog != null)
                builder.body(timeLog.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("workorderHoursId", workorderHoursId);
            if (timeLog != null)
                methodParams.put("timeLog", timeLog.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateTimeLogByWorkOrder
     * Update time log for assigned work order.
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param timeLog          Check in information
     * @param async            Return the model in the response (slower) (Optional)
     */
    public static void updateTimeLog(Context context, Integer workOrderId, Integer workorderHoursId, TimeLog timeLog, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                    .urlParams("?async=" + async);

            if (timeLog != null)
                builder.body(timeLog.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("workorderHoursId", workorderHoursId);
            methodParams.put("async", async);
            if (timeLog != null)
                methodParams.put("timeLog", timeLog.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateWorkOrderByWorkOrder
     * Updates a work order by its id
     *
     * @param workOrderId ID of work order
     * @param workOrder   Work order model
     */
    public static void updateWorkOrder(Context context, Integer workOrderId, WorkOrder workOrder) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId);

            if (workOrder != null)
                builder.body(workOrder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            if (workOrder != null)
                methodParams.put("workOrder", workOrder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateWorkOrderByWorkOrder
     * Updates a work order by its id
     *
     * @param workOrderId ID of work order
     * @param workOrder   Work order model
     * @param async       Asynchroneous (Optional)
     */
    public static void updateWorkOrder(Context context, Integer workOrderId, WorkOrder workOrder, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId)
                    .urlParams("?async=" + async);

            if (workOrder != null)
                builder.body(workOrder.getJson().toString());

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("async", async);
            if (workOrder != null)
                methodParams.put("workOrder", workOrder.getJson());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateWorkOrder", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: verifyTimeLogByWorkOrder
     * Verify time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     */
    public static void verifyTimeLog(Context context, Integer workOrderId, Integer workorderHoursId) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify");

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("workorderHoursId", workorderHoursId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}/verify")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "verifyTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: verifyTimeLogByWorkOrder
     * Verify time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async            Return the model in the response (slower) (Optional)
     */
    public static void verifyTimeLog(Context context, Integer workOrderId, Integer workorderHoursId, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify")
                    .urlParams("?async=" + async);

            JsonObject methodParams = new JsonObject();
            methodParams.put("workOrderId", workOrderId);
            methodParams.put("workorderHoursId", workorderHoursId);
            methodParams.put("async", async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}/verify")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "verifyTimeLog", methodParams))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "Listener " + topicId);

            String type = ((Bundle) payload).getString("type");
            switch (type) {
                case "progress": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onProgress(transactionParams, transactionParams.apiFunction, bundle.getLong("pos"), bundle.getLong("size"), bundle.getLong("time"));
                    break;
                }
                case "start": {
                    Bundle bundle = (Bundle) payload;
                    TransactionParams transactionParams = bundle.getParcelable("params");
                    onStart(transactionParams, transactionParams.apiFunction);
                    break;
                }
                case "complete": {
                    new AsyncParser(this, (Bundle) payload);
                    break;
                }
            }
        }

        public void onStart(TransactionParams transactionParams, String methodName) {
        }

        public void onProgress(TransactionParams transactionParams, String methodName, long pos, long size, long time) {
        }

        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
        }
    }

    private static class AsyncParser extends AsyncTaskEx<Object, Object, Object> {
        private static final String TAG = "WorkordersWebApi.AsyncParser";

        private Listener listener;
        private TransactionParams transactionParams;
        private boolean success;
        private byte[] data;

        private Object successObject;
        private Object failObject;

        public AsyncParser(Listener listener, Bundle bundle) {
            this.listener = listener;
            transactionParams = bundle.getParcelable("params");
            success = bundle.getBoolean("success");
            data = bundle.getByteArray("data");

            executeEx();
        }

        @Override
        protected Object doInBackground(Object... params) {
            Log.v(TAG, "Start doInBackground");
            Stopwatch watch = new Stopwatch(true);
            try {
                if (success) {
                    switch (transactionParams.apiFunction) {
                        case "getRequests":
                            successObject = Requests.fromJson(new JsonObject(data));
                            break;
                        case "getWorkOrders":
                            successObject = WorkOrders.fromJson(new JsonObject(data));
                            break;
                        case "getIncreases":
                            successObject = PayIncreases.fromJson(new JsonObject(data));
                            break;
                        case "getStatus":
                            successObject = com.fieldnation.v2.data.model.Status.fromJson(new JsonObject(data));
                            break;
                        case "getCustomField":
                            successObject = CustomField.fromJson(new JsonObject(data));
                            break;
                        case "getIncrease":
                            successObject = PayIncrease.fromJson(new JsonObject(data));
                            break;
                        case "acceptSwapRequest":
                        case "cancelSwapRequest":
                        case "declineSwapRequest":
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                            break;
                        case "getAttachments":
                            successObject = AttachmentFolders.fromJson(new JsonObject(data));
                            break;
                        case "getSignatures":
                            successObject = Signatures.fromJson(new JsonObject(data));
                            break;
                        case "MassAcceptWorkOrder":
                        case "massRequests":
                            successObject = data;
                            break;
                        case "getWorkOrderLists":
                            successObject = SavedList.fromJsonArray(new JsonArray(data));
                            break;
                        case "getBonus":
                        case "getPenalty":
                            successObject = PayModifier.fromJson(new JsonObject(data));
                            break;
                        case "getTasks":
                            successObject = Tasks.fromJson(new JsonObject(data));
                            break;
                        case "getShipments":
                            successObject = Shipments.fromJson(new JsonObject(data));
                            break;
                        case "getHold":
                            successObject = Hold.fromJson(new JsonObject(data));
                            break;
                        case "getTags":
                            successObject = Tags.fromJson(new JsonObject(data));
                            break;
                        case "getOverview":
                            successObject = WorkOrderOverview.fromJson(new JsonObject(data));
                            break;
                        case "getAssignee":
                            successObject = Assignee.fromJson(new JsonObject(data));
                            break;
                        case "getHolds":
                            successObject = Holds.fromJson(new JsonObject(data));
                            break;
                        case "getTag":
                            successObject = Tag.fromJson(new JsonObject(data));
                            break;
                        case "getExpenses":
                            successObject = Expenses.fromJson(new JsonObject(data));
                            break;
                        case "getTask":
                            successObject = Task.fromJson(new JsonObject(data));
                            break;
                        case "getBonuses":
                        case "getDiscounts":
                        case "getPenalties":
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                            break;
                        case "getFolder":
                            successObject = AttachmentFolder.fromJson(new JsonObject(data));
                            break;
                        case "getMilestones":
                            successObject = Milestones.fromJson(new JsonObject(data));
                            break;
                        case "getQualifications":
                            successObject = Qualifications.fromJson(new JsonObject(data));
                            break;
                        case "acceptIncrease":
                        case "addAlertToWorkOrderAndTask":
                        case "addAttachment":
                        case "addBonus":
                        case "addContact":
                        case "addDiscount":
                        case "addExpense":
                        case "addFolder":
                        case "addHold":
                        case "addIncrease":
                        case "addMessage":
                        case "addPenalty":
                        case "addProblem":
                        case "addQualification":
                        case "addShipment":
                        case "addSignature":
                        case "addTag":
                        case "addTask":
                        case "addTimeLog":
                        case "addWorkOrder":
                        case "approveWorkOrder":
                        case "assignUser":
                        case "completeWorkOrder":
                        case "decline":
                        case "declineRequest":
                        case "deleteAlert":
                        case "deleteAlerts":
                        case "deleteAttachment":
                        case "deleteBonus":
                        case "deleteContact":
                        case "deleteDiscount":
                        case "deleteExpense":
                        case "deleteFolder":
                        case "deleteHold":
                        case "deleteIncrease":
                        case "deleteMessage":
                        case "deletePenalty":
                        case "deleteProblem":
                        case "deleteRequest":
                        case "deleteShipment":
                        case "deleteSignature":
                        case "deleteTag":
                        case "deleteTask":
                        case "deleteTimeLog":
                        case "deleteWorkOrder":
                        case "denyIncrease":
                        case "getWorkOrder":
                        case "groupTask":
                        case "incompleteWorkOrder":
                        case "publish":
                        case "removeQualification":
                        case "reorderTask":
                        case "replyMessage":
                        case "request":
                        case "revertWorkOrderToDraft":
                        case "routeUser":
                        case "unapproveWorkOrder":
                        case "unassignUser":
                        case "unpublish":
                        case "unRouteUser":
                        case "updateAllTimeLogs":
                        case "updateAttachment":
                        case "updateBonus":
                        case "updateContact":
                        case "updateCustomField":
                        case "updateDiscount":
                        case "updateETA":
                        case "updateExpense":
                        case "updateFolder":
                        case "updateHold":
                        case "updateIncrease":
                        case "updateLocation":
                        case "updateMessage":
                        case "updatePay":
                        case "updatePenalty":
                        case "updateProblem":
                        case "updateQualification":
                        case "updateSchedule":
                        case "updateShipment":
                        case "updateTag":
                        case "updateTask":
                        case "updateTimeLog":
                        case "updateWorkOrder":
                        case "verifyTimeLog":
                            successObject = WorkOrder.fromJson(new JsonObject(data));
                            break;
                        case "getPay":
                            successObject = Pay.fromJson(new JsonObject(data));
                            break;
                        case "getFile":
                            successObject = Attachment.fromJson(new JsonObject(data));
                            break;
                        case "GetScheduleAndLocation":
                            successObject = EtaMassAcceptWithLocation.fromJson(new JsonObject(data));
                            break;
                        case "getContacts":
                            successObject = Contacts.fromJson(new JsonObject(data));
                            break;
                        case "getProviders":
                            successObject = Users.fromJsonArray(new JsonArray(data));
                            break;
                        case "getLocation":
                            successObject = Location.fromJson(new JsonObject(data));
                            break;
                        case "getMessages":
                            successObject = Messages.fromJson(new JsonObject(data));
                            break;
                        case "getSignature":
                            successObject = Signature.fromJson(new JsonObject(data));
                            break;
                        case "getProblem":
                        case "getProblems":
                            successObject = Problems.fromJson(new JsonObject(data));
                            break;
                        case "getETA":
                            successObject = ETA.fromJson(new JsonObject(data));
                            break;
                        case "getCustomFields":
                            successObject = CustomFields.fromJson(new JsonObject(data));
                            break;
                        case "getSchedule":
                            successObject = Schedule.fromJson(new JsonObject(data));
                            break;
                        case "getTimeLogs":
                            successObject = TimeLogs.fromJson(new JsonObject(data));
                            break;
                        case "getRequest":
                            successObject = Request.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                } else {
                    switch (transactionParams.apiFunction) {
                        case "acceptIncrease":
                        case "acceptSwapRequest":
                        case "addAlertToWorkOrderAndTask":
                        case "addAttachment":
                        case "addBonus":
                        case "addContact":
                        case "addDiscount":
                        case "addExpense":
                        case "addFolder":
                        case "addHold":
                        case "addIncrease":
                        case "addMessage":
                        case "addPenalty":
                        case "addProblem":
                        case "addQualification":
                        case "addShipment":
                        case "addSignature":
                        case "addTag":
                        case "addTask":
                        case "addTimeLog":
                        case "addWorkOrder":
                        case "approveWorkOrder":
                        case "assignUser":
                        case "cancelSwapRequest":
                        case "completeWorkOrder":
                        case "decline":
                        case "declineRequest":
                        case "declineSwapRequest":
                        case "deleteAlert":
                        case "deleteAlerts":
                        case "deleteAttachment":
                        case "deleteBonus":
                        case "deleteContact":
                        case "deleteDiscount":
                        case "deleteExpense":
                        case "deleteFolder":
                        case "deleteHold":
                        case "deleteIncrease":
                        case "deleteMessage":
                        case "deletePenalty":
                        case "deleteProblem":
                        case "deleteRequest":
                        case "deleteShipment":
                        case "deleteSignature":
                        case "deleteTag":
                        case "deleteTask":
                        case "deleteTimeLog":
                        case "deleteWorkOrder":
                        case "denyIncrease":
                        case "getAssignee":
                        case "getAttachments":
                        case "getBonus":
                        case "getBonuses":
                        case "getContacts":
                        case "getCustomField":
                        case "getCustomFields":
                        case "getDiscounts":
                        case "getETA":
                        case "getExpenses":
                        case "getFile":
                        case "getFolder":
                        case "getHold":
                        case "getHolds":
                        case "getIncrease":
                        case "getIncreases":
                        case "getLocation":
                        case "getMessages":
                        case "getMilestones":
                        case "getOverview":
                        case "getPay":
                        case "getPenalties":
                        case "getPenalty":
                        case "getProblem":
                        case "getProblems":
                        case "getProviders":
                        case "getQualifications":
                        case "getRequest":
                        case "getRequests":
                        case "GetScheduleAndLocation":
                        case "getSchedule":
                        case "getShipments":
                        case "getSignature":
                        case "getSignatures":
                        case "getStatus":
                        case "getTag":
                        case "getTags":
                        case "getTask":
                        case "getTasks":
                        case "getTimeLogs":
                        case "getWorkOrder":
                        case "getWorkOrderLists":
                        case "getWorkOrders":
                        case "groupTask":
                        case "incompleteWorkOrder":
                        case "MassAcceptWorkOrder":
                        case "massRequests":
                        case "publish":
                        case "removeQualification":
                        case "reorderTask":
                        case "replyMessage":
                        case "request":
                        case "revertWorkOrderToDraft":
                        case "routeUser":
                        case "unapproveWorkOrder":
                        case "unassignUser":
                        case "unpublish":
                        case "unRouteUser":
                        case "updateAllTimeLogs":
                        case "updateAttachment":
                        case "updateBonus":
                        case "updateContact":
                        case "updateCustomField":
                        case "updateDiscount":
                        case "updateETA":
                        case "updateExpense":
                        case "updateFolder":
                        case "updateHold":
                        case "updateIncrease":
                        case "updateLocation":
                        case "updateMessage":
                        case "updatePay":
                        case "updatePenalty":
                        case "updateProblem":
                        case "updateQualification":
                        case "updateSchedule":
                        case "updateShipment":
                        case "updateTag":
                        case "updateTask":
                        case "updateTimeLog":
                        case "updateWorkOrder":
                        case "verifyTimeLog":
                            failObject = Error.fromJson(new JsonObject(data));
                            break;
                        default:
                            Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                            break;
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            } finally {
                Log.v(TAG, "doInBackground: " + transactionParams.apiFunction + " time: " + watch.finish());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                if (failObject != null && failObject instanceof Error) {
                    ToastClient.toast(App.get(), ((Error) failObject).getMessage(), Toast.LENGTH_SHORT);
                }
                listener.onComplete(transactionParams, transactionParams.apiFunction, successObject, success, failObject);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }
}

package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.bv2.listener.TransactionListener;
import com.fieldnation.data.bv2.listener.TransactionParams;
import com.fieldnation.data.bv2.model.AaaaPlaceholder;
import com.fieldnation.data.bv2.model.Assignee;
import com.fieldnation.data.bv2.model.Attachment;
import com.fieldnation.data.bv2.model.AttachmentFolder;
import com.fieldnation.data.bv2.model.AttachmentFolders;
import com.fieldnation.data.bv2.model.Cancellation;
import com.fieldnation.data.bv2.model.Contact;
import com.fieldnation.data.bv2.model.Contacts;
import com.fieldnation.data.bv2.model.CustomField;
import com.fieldnation.data.bv2.model.CustomFields;
import com.fieldnation.data.bv2.model.Error;
import com.fieldnation.data.bv2.model.Expense;
import com.fieldnation.data.bv2.model.Expenses;
import com.fieldnation.data.bv2.model.IdResponse;
import com.fieldnation.data.bv2.model.Location;
import com.fieldnation.data.bv2.model.Message;
import com.fieldnation.data.bv2.model.Messages;
import com.fieldnation.data.bv2.model.Milestones;
import com.fieldnation.data.bv2.model.Pay;
import com.fieldnation.data.bv2.model.PayIncrease;
import com.fieldnation.data.bv2.model.PayIncreases;
import com.fieldnation.data.bv2.model.PayModifier;
import com.fieldnation.data.bv2.model.PayModifiers;
import com.fieldnation.data.bv2.model.Problems;
import com.fieldnation.data.bv2.model.Request;
import com.fieldnation.data.bv2.model.Route;
import com.fieldnation.data.bv2.model.SavedList;
import com.fieldnation.data.bv2.model.Schedule;
import com.fieldnation.data.bv2.model.Shipment;
import com.fieldnation.data.bv2.model.Shipments;
import com.fieldnation.data.bv2.model.Signature;
import com.fieldnation.data.bv2.model.Status;
import com.fieldnation.data.bv2.model.SwapResponse;
import com.fieldnation.data.bv2.model.Task;
import com.fieldnation.data.bv2.model.TaskAlert;
import com.fieldnation.data.bv2.model.Tasks;
import com.fieldnation.data.bv2.model.TimeLog;
import com.fieldnation.data.bv2.model.TimeLogs;
import com.fieldnation.data.bv2.model.Users;
import com.fieldnation.data.bv2.model.WorkOrder;
import com.fieldnation.data.bv2.model.WorkOrders;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class WorkordersWebApi extends TopicClient {
    private static final String STAG = "WorkordersWebApi";
    private final String TAG = UniqueTag.makeTag(STAG);


    public WorkordersWebApi(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public boolean subWorkordersWebApi() {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi");
    }

    /**
     * Swagger operationId: revertWorkOrderToDraftByWorkOrder
     * Reverts a work order to draft status
     *
     * @param workOrderId ID of work order
     */
    public static void revertWorkOrderToDraft(Context context, Integer workOrderId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/draft");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/draft")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/draft"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/draft",
                                    WorkordersWebApi.class, "revertWorkOrderToDraft"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRevertWorkOrderToDraft(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/draft");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/draft")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/draft")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/draft?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/draft",
                                    WorkordersWebApi.class, "revertWorkOrderToDraft"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: incompleteTaskByWorkOrderAndTask
     * Marks a task associated with a work order as incomplete
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void incompleteTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/incomplete");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/incomplete")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/incomplete"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/incomplete",
                                    WorkordersWebApi.class, "incompleteTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subIncompleteTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/incomplete");
    }

    /**
     * Swagger operationId: getCustomFieldByWorkOrderAndCustomField
     * Get a custom field by work order and custom field
     *
     * @param workOrderId   ID of work order
     * @param customFieldId Custom field id
     * @param isBackground  indicates that this call is low priority
     */
    public static void getCustomField(Context context, Integer workOrderId, Integer customFieldId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields/" + customFieldId,
                                    WorkordersWebApi.class, "getCustomField"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetCustomField(Integer workOrderId, Integer customFieldId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields/" + customFieldId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            if (customField != null)
                builder.body(customField.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields/" + customFieldId,
                                    WorkordersWebApi.class, "updateCustomField"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomField(Integer workOrderId, Integer customFieldId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields/" + customFieldId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId)
                    .urlParams("?async=" + async);

            if (customField != null)
                builder.body(customField.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields/" + customFieldId,
                                    WorkordersWebApi.class, "updateCustomField"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/complete"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/complete",
                                    WorkordersWebApi.class, "completeWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCompleteWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/complete");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/complete?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/complete",
                                    WorkordersWebApi.class, "completeWorkOrder"))
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
     * @param reason      Reason
     */
    public static void incompleteWorkOrder(Context context, Integer workOrderId, String reason) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete")
                    .urlParams("?reason=" + reason);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/complete?reason=" + reason))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/complete",
                                    WorkordersWebApi.class, "incompleteWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subIncompleteWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/complete");
    }

    /**
     * Swagger operationId: incompleteWorkOrderByWorkOrder
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param reason      Reason
     * @param async       Async (Optional)
     */
    public static void incompleteWorkOrder(Context context, Integer workOrderId, String reason, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/complete")
                    .urlParams("?reason=" + reason + "&async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/complete?reason=" + reason + "&async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/complete",
                                    WorkordersWebApi.class, "incompleteWorkOrder"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses");

            if (expense != null)
                builder.body(expense.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/expenses"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses",
                                    WorkordersWebApi.class, "addExpense"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddExpense(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses")
                    .urlParams("?async=" + async);

            if (expense != null)
                builder.body(expense.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/expenses?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses",
                                    WorkordersWebApi.class, "addExpense"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getExpenses(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/expenses"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses",
                                    WorkordersWebApi.class, "getExpenses"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetExpenses(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses");
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
    public static void addAttachment(Context context, Integer workOrderId, Integer folderId, String attachment, java.io.File file) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .multipartField("attachment", attachment).multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "addAttachment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAttachment(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId);
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
    public static void addAttachment(Context context, Integer workOrderId, Integer folderId, String attachment, java.io.File file, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async)
                    .multipartField("attachment", attachment).multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "addAttachment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getFolder(Context context, Integer workOrderId, Integer folderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "getFolder"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetFolder(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "deleteFolder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteFolder(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "deleteFolder"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            if (folder != null)
                builder.body(folder.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "updateFolder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateFolder(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async);

            if (folder != null)
                builder.body(folder.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId,
                                    WorkordersWebApi.class, "updateFolder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getWorkOrders(Context context, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(misc.md5("GET//api/rest/v2/workorders"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/workorders",
                                    WorkordersWebApi.class, "getWorkOrders"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetWorkOrders() {
        return register("TOPIC_ID_WEB_API_V2/workorders");
    }

    /**
     * Swagger operationId: getWorkOrders
     * Returns a list of work orders.
     *
     * @param getWorkOrdersOptions Additional optional parameters
     * @param isBackground         indicates that this call is low priority
     */
    public static void getWorkOrders(Context context, GetWorkOrdersOptions getWorkOrdersOptions, boolean isBackground) {
        try {
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
                            + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                            + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                    );

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(misc.md5("GET//api/rest/v2/workorders" + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
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
                            + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                            + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                    ))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/workorders",
                                    WorkordersWebApi.class, "getWorkOrders"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}/verify")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify",
                                    WorkordersWebApi.class, "verifyTimeLog"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subVerifyTimeLog(Integer workOrderId, Integer workorderHoursId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}/verify")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify",
                                    WorkordersWebApi.class, "verifyTimeLog"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeContactByWorkOrderAndContactId
     * Removes a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId   Contact id
     */
    public static void removeContact(Context context, Integer workOrderId, Integer contactId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts/" + contactId,
                                    WorkordersWebApi.class, "removeContact"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveContact(Integer workOrderId, Integer contactId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts/" + contactId);
    }

    /**
     * Swagger operationId: updateContactByWorkOrderAndContactId
     * Updates a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId   Contact id
     * @param json        JSON Model
     */
    public static void updateContact(Context context, Integer workOrderId, Integer contactId, Contact json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts/" + contactId,
                                    WorkordersWebApi.class, "updateContact"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateContact(Integer workOrderId, Integer contactId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts/" + contactId);
    }

    /**
     * Swagger operationId: getIncreaseByWorkOrderAndIncrease
     * Get pay increase for assigned work order.
     *
     * @param workOrderId  ID of work order
     * @param increaseId   ID of work order increase
     * @param isBackground indicates that this call is low priority
     */
    public static void getIncrease(Context context, Integer workOrderId, Integer increaseId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId,
                                    WorkordersWebApi.class, "getIncrease"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId);
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
    public static void getIncrease(Context context, Integer workOrderId, Integer increaseId, Boolean async, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId,
                                    WorkordersWebApi.class, "getIncrease"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId,
                                    WorkordersWebApi.class, "deleteIncrease"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId,
                                    WorkordersWebApi.class, "deleteIncrease"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            if (increase != null)
                builder.body(increase.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId,
                                    WorkordersWebApi.class, "updateIncrease"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId)
                    .urlParams("?async=" + async);

            if (increase != null)
                builder.body(increase.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId,
                                    WorkordersWebApi.class, "updateIncrease"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses/" + expenseId,
                                    WorkordersWebApi.class, "deleteExpense"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteExpense(Integer workOrderId, Integer expenseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses/" + expenseId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses/" + expenseId,
                                    WorkordersWebApi.class, "deleteExpense"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses/" + expenseId,
                                    WorkordersWebApi.class, "updateExpense"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateExpense(Integer workOrderId, Integer expenseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses/" + expenseId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId)
                    .urlParams("" + (updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
                    );

            if (updateExpenseOptions.getExpense() != null)
                builder.body(updateExpenseOptions.getExpense().toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "" + (updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
                    ))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/expenses/" + expenseId,
                                    WorkordersWebApi.class, "updateExpense"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getIncreases(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/increases"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases",
                                    WorkordersWebApi.class, "getIncreases"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetIncreases(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases");
    }

    /**
     * Swagger operationId: getPayByWorkOrder
     * Gets the pay for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getPay(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/pay");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/pay"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/pay",
                                    WorkordersWebApi.class, "getPay"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetPay(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/pay");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/pay");

            if (pay != null)
                builder.body(pay.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/pay"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/pay",
                                    WorkordersWebApi.class, "updatePay"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePay(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/pay");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/pay")
                    .urlParams("?async=" + async);

            if (pay != null)
                builder.body(pay.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/pay?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/pay",
                                    WorkordersWebApi.class, "updatePay"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tasks"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks",
                                    WorkordersWebApi.class, "addTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddTask(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks");
    }

    /**
     * Swagger operationId: getTasksByWorkOrder
     * Get a list of a work order's tasks
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getTasks(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tasks")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/tasks"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks",
                                    WorkordersWebApi.class, "getTasks"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetTasks(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks");
    }

    /**
     * Swagger operationId: getMilestonesByWorkOrder
     * Get the milestones of a work order
     *
     * @param workOrderId  ID of Work Order
     * @param isBackground indicates that this call is low priority
     */
    public static void getMilestones(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/milestones");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/milestones")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/milestones"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/milestones",
                                    WorkordersWebApi.class, "getMilestones"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetMilestones(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/milestones");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures");

            if (signature != null)
                builder.body(signature.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/signatures"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures",
                                    WorkordersWebApi.class, "addSignature"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddSignature(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures")
                    .urlParams("?async=" + async);

            if (signature != null)
                builder.body(signature.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/signatures?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures",
                                    WorkordersWebApi.class, "addSignature"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getSignatures(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/signatures"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures",
                                    WorkordersWebApi.class, "getSignatures"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetSignatures(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures");
    }

    /**
     * Swagger operationId: getProvidersByWorkOrder
     * Gets list of providers available for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getProviders(Context context, String workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/providers");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/providers"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/providers",
                                    WorkordersWebApi.class, "getProviders"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetProviders(String workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/providers");
    }

    /**
     * Swagger operationId: getProvidersByWorkOrder
     * Gets list of providers available for a work order
     *
     * @param workOrderId         ID of work order
     * @param getProvidersOptions Additional optional parameters
     * @param isBackground        indicates that this call is low priority
     */
    public static void getProviders(Context context, String workOrderId, GetProvidersOptions getProvidersOptions, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/providers")
                    .urlParams("" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                            + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                            + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                    );

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/providers" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                            + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                            + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                    ))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/providers",
                                    WorkordersWebApi.class, "getProviders"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
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
    public static void addMessage(Context context, String workOrderId, Message json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages",
                                    WorkordersWebApi.class, "addMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddMessage(String workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages");
    }

    /**
     * Swagger operationId: addMessageByWorkOrder
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     * @param async       Async (Optional)
     */
    public static void addMessage(Context context, String workOrderId, Message json, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages")
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages",
                                    WorkordersWebApi.class, "addMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getMessages(Context context, String workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/messages"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages",
                                    WorkordersWebApi.class, "getMessages"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetMessages(String workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages");
    }

    /**
     * Swagger operationId: cancelSwapRequest
     * Cancel work order swap request.
     */
    public static void cancelSwapRequest(Context context) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel")
                    .key(misc.md5("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel",
                                    WorkordersWebApi.class, "cancelSwapRequest"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCancelSwapRequest() {
        return register("TOPIC_ID_WEB_API_V2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs");

            if (timeLog != null)
                builder.body(timeLog.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/time_logs"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs",
                                    WorkordersWebApi.class, "addTimeLog"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddTimeLog(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs");
    }

    /**
     * Swagger operationId: getTimeLogsByWorkOrder
     * Returns a list of time logs applied by the assigned provider
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getTimeLogs(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/time_logs"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs",
                                    WorkordersWebApi.class, "getTimeLogs"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetTimeLogs(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs");

            if (timeLog != null)
                builder.body(timeLog.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs",
                                    WorkordersWebApi.class, "updateAllTimeLogs"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateAllTimeLogs(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs")
                    .urlParams("?async=" + async);

            if (timeLog != null)
                builder.body(timeLog.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs",
                                    WorkordersWebApi.class, "updateAllTimeLogs"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getWorkOrder(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId,
                                    WorkordersWebApi.class, "getWorkOrder"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId);

            if (cancellation != null)
                builder.body(cancellation.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId,
                                    WorkordersWebApi.class, "deleteWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId)
                    .urlParams("?async=" + async);

            if (cancellation != null)
                builder.body(cancellation.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId,
                                    WorkordersWebApi.class, "deleteWorkOrder"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId);

            if (workOrder != null)
                builder.body(workOrder.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId,
                                    WorkordersWebApi.class, "updateWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId)
                    .urlParams("?async=" + async);

            if (workOrder != null)
                builder.body(workOrder.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId,
                                    WorkordersWebApi.class, "updateWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getSignature(Context context, Integer workOrderId, Integer signatureId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures/" + signatureId,
                                    WorkordersWebApi.class, "getSignature"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetSignature(Integer workOrderId, Integer signatureId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures/" + signatureId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures/" + signatureId,
                                    WorkordersWebApi.class, "deleteSignature"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteSignature(Integer workOrderId, Integer signatureId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures/" + signatureId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/signatures/" + signatureId,
                                    WorkordersWebApi.class, "deleteSignature"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments");

            if (folder != null)
                builder.body(folder.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments",
                                    WorkordersWebApi.class, "addFolder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddFolder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments")
                    .urlParams("?async=" + async);

            if (folder != null)
                builder.body(folder.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments",
                                    WorkordersWebApi.class, "addFolder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getAttachments(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/attachments"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments",
                                    WorkordersWebApi.class, "getAttachments"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetAttachments(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments");
    }

    /**
     * Swagger operationId: completeTaskByWorkOrderAndTask
     * Completes a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void completeTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/complete");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/complete")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/complete"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/complete",
                                    WorkordersWebApi.class, "completeTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCompleteTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/complete");
    }

    /**
     * Swagger operationId: removeDiscountByWorkOrder
     * Allows an assigned provider to removes a discount they previously applied from a work order, increasing the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param discountId  ID of the discount
     */
    public static void removeDiscount(Context context, Integer workOrderId, Integer discountId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts/" + discountId,
                                    WorkordersWebApi.class, "removeDiscount"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveDiscount(Integer workOrderId, Integer discountId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts/" + discountId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts/" + discountId,
                                    WorkordersWebApi.class, "updateDiscount"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateDiscount(Integer workOrderId, Integer discountId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts/" + discountId);
    }

    /**
     * Swagger operationId: removeTimeLogByWorkOrder
     * Remove time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     */
    public static void removeTimeLog(Context context, Integer workOrderId, Integer workorderHoursId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId,
                                    WorkordersWebApi.class, "removeTimeLog"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveTimeLog(Integer workOrderId, Integer workorderHoursId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId);
    }

    /**
     * Swagger operationId: removeTimeLogByWorkOrder
     * Remove time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async            Return the model in the response (slower) (Optional)
     */
    public static void removeTimeLog(Context context, Integer workOrderId, Integer workorderHoursId, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId,
                                    WorkordersWebApi.class, "removeTimeLog"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            if (timeLog != null)
                builder.body(timeLog.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId,
                                    WorkordersWebApi.class, "updateTimeLog"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTimeLog(Integer workOrderId, Integer workorderHoursId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                    .urlParams("?async=" + async);

            if (timeLog != null)
                builder.body(timeLog.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/time_logs/" + workorderHoursId,
                                    WorkordersWebApi.class, "updateTimeLog"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getFile(Context context, Integer workOrderId, Integer folderId, Integer attachmentId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId,
                                    WorkordersWebApi.class, "getFile"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetFile(Integer workOrderId, Integer folderId, Integer attachmentId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId,
                                    WorkordersWebApi.class, "deleteAttachment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteAttachment(Integer workOrderId, Integer folderId, Integer attachmentId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId,
                                    WorkordersWebApi.class, "deleteAttachment"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);

            if (attachment != null)
                builder.body(attachment.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId,
                                    WorkordersWebApi.class, "updateAttachment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateAttachment(Integer workOrderId, Integer folderId, Integer attachmentId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                    .urlParams("?async=" + async);

            if (attachment != null)
                builder.body(attachment.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId,
                                    WorkordersWebApi.class, "updateAttachment"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee");

            if (assignee != null)
                builder.body(assignee.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/assignee"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee",
                                    WorkordersWebApi.class, "assignUser"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAssignUser(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee")
                    .urlParams("?async=" + async);

            if (assignee != null)
                builder.body(assignee.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/assignee?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee",
                                    WorkordersWebApi.class, "assignUser"))
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
    public static void getAssignee(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/assignee"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee",
                                    WorkordersWebApi.class, "getAssignee"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetAssignee(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee");

            if (assignee != null)
                builder.body(assignee.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/assignee"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee",
                                    WorkordersWebApi.class, "unassignUser"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnassignUser(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/assignee")
                    .urlParams("?async=" + async);

            if (assignee != null)
                builder.body(assignee.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/assignee?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/assignee",
                                    WorkordersWebApi.class, "unassignUser"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/publish"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/publish",
                                    WorkordersWebApi.class, "publish"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subPublish(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/publish");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/publish?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/publish",
                                    WorkordersWebApi.class, "publish"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/publish"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/publish",
                                    WorkordersWebApi.class, "unpublish"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnpublish(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/publish");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/publish")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/publish?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/publish",
                                    WorkordersWebApi.class, "unpublish"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getStatus(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/status");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/status")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/status"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/status",
                                    WorkordersWebApi.class, "getStatus"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetStatus(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/status");
    }

    /**
     * Swagger operationId: approveWorkOrderByWorkOrder
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     */
    public static void approveWorkOrder(Context context, Integer workOrderId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/approve"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/approve",
                                    WorkordersWebApi.class, "approveWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subApproveWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/approve");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/approve?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/approve",
                                    WorkordersWebApi.class, "approveWorkOrder"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/approve"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/approve",
                                    WorkordersWebApi.class, "unapproveWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnapproveWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/approve");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/approve")
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/approve?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/approve",
                                    WorkordersWebApi.class, "unapproveWorkOrder"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/accept")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId + "/accept",
                                    WorkordersWebApi.class, "acceptIncrease"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAcceptIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId + "/accept");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments/" + shipmentId,
                                    WorkordersWebApi.class, "deleteShipment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteShipment(Integer workOrderId, Integer shipmentId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments/" + shipmentId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments/" + shipmentId,
                                    WorkordersWebApi.class, "deleteShipment"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            if (shipment != null)
                builder.body(shipment.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments/" + shipmentId,
                                    WorkordersWebApi.class, "updateShipment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateShipment(Integer workOrderId, Integer shipmentId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments/" + shipmentId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId)
                    .urlParams("?async=" + async);

            if (shipment != null)
                builder.body(shipment.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments/" + shipmentId,
                                    WorkordersWebApi.class, "updateShipment"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId,
                                    WorkordersWebApi.class, "addPenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            if (penalty != null)
                builder.body(penalty.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId,
                                    WorkordersWebApi.class, "addPenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getPenalty(Context context, Integer workOrderId, Integer penaltyId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId,
                                    WorkordersWebApi.class, "getPenalty"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetPenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId);
    }

    /**
     * Swagger operationId: removePenaltyByWorkOrderAndPenalty
     * Removes a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId ID of Work Order
     * @param penaltyId   Penalty ID
     */
    public static void removePenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId,
                                    WorkordersWebApi.class, "removePenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemovePenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId,
                                    WorkordersWebApi.class, "updatePenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            if (penalty != null)
                builder.body(penalty.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties/" + penaltyId,
                                    WorkordersWebApi.class, "updatePenalty"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline")
                    .key(misc.md5("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/workorders/{work_order_id}/swaps/{swap_request_id}/decline",
                                    WorkordersWebApi.class, "declineSwapRequest"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeclineSwapRequest() {
        return register("TOPIC_ID_WEB_API_V2/workorders/{work_order_id}/swaps/{swap_request_id}/decline");
    }

    /**
     * Swagger operationId: replyMessageByWorkOrder
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void replyMessage(Context context, String workOrderId, String messageId, Message json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId,
                                    WorkordersWebApi.class, "replyMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReplyMessage(String workOrderId, String messageId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId);
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
    public static void replyMessage(Context context, String workOrderId, String messageId, Message json, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId)
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId,
                                    WorkordersWebApi.class, "replyMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeMessageByWorkOrder
     * Removes a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     */
    public static void removeMessage(Context context, String workOrderId, String messageId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId,
                                    WorkordersWebApi.class, "removeMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveMessage(String workOrderId, String messageId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId);
    }

    /**
     * Swagger operationId: updateMessageByWorkOrder
     * Updates a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void updateMessage(Context context, String workOrderId, String messageId, Message json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId,
                                    WorkordersWebApi.class, "updateMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateMessage(String workOrderId, String messageId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/messages/" + messageId);
    }

    /**
     * Swagger operationId: getTaskByWorkOrder
     * Get a task by work order
     *
     * @param workOrderId  Work order id
     * @param taskId       Task id
     * @param isBackground indicates that this call is low priority
     */
    public static void getTask(Context context, Integer workOrderId, Integer taskId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId,
                                    WorkordersWebApi.class, "getTask"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId);
    }

    /**
     * Swagger operationId: removeTaskByWorkOrder
     * Remove a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void removeTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId,
                                    WorkordersWebApi.class, "removeTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId,
                                    WorkordersWebApi.class, "updateTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/deny")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId + "/deny",
                                    WorkordersWebApi.class, "denyIncrease"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDenyIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/increases/" + increaseId + "/deny");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/alerts",
                                    WorkordersWebApi.class, "addAlertToWorkOrderAndTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAlertToWorkOrderAndTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/alerts");
    }

    /**
     * Swagger operationId: removeAlertsByWorkOrderAndTask
     * Removes all alerts associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void removeAlerts(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/alerts",
                                    WorkordersWebApi.class, "removeAlerts"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveAlerts(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/alerts");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests");

            if (request != null)
                builder.body(request.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/requests"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/requests",
                                    WorkordersWebApi.class, "request"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRequest(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/requests");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests")
                    .urlParams("?async=" + async);

            if (request != null)
                builder.body(request.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/requests?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/requests",
                                    WorkordersWebApi.class, "request"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param request     JSON Model
     */
    public static void removeRequest(Context context, Integer workOrderId, Request request) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests");

            if (request != null)
                builder.body(request.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/requests"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/requests",
                                    WorkordersWebApi.class, "removeRequest"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveRequest(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/requests");
    }

    /**
     * Swagger operationId: removeRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param request     JSON Model
     * @param async       Async (Optional)
     */
    public static void removeRequest(Context context, Integer workOrderId, Request request, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests")
                    .urlParams("?async=" + async);

            if (request != null)
                builder.body(request.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/requests?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/requests",
                                    WorkordersWebApi.class, "removeRequest"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept")
                    .key(misc.md5("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/workorders/{work_order_id}/swaps/{swap_request_id}/accept",
                                    WorkordersWebApi.class, "acceptSwapRequest"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAcceptSwapRequest() {
        return register("TOPIC_ID_WEB_API_V2/workorders/{work_order_id}/swaps/{swap_request_id}/accept");
    }

    /**
     * Swagger operationId: getCustomFieldsByWorKOrder
     * Get a list of custom fields and their values for a work order.
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomFields(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/custom_fields"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields",
                                    WorkordersWebApi.class, "getCustomFields"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetCustomFields(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/custom_fields");
    }

    /**
     * Swagger operationId: removeAlertByWorkOrderAndTask
     * Removes a single alert associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param alertId     Alert id
     */
    public static void removeAlert(Context context, Integer workOrderId, Integer taskId, Integer alertId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts/{alert_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId,
                                    WorkordersWebApi.class, "removeAlert"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveAlert(Integer workOrderId, Integer taskId, Integer alertId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);
    }

    /**
     * Swagger operationId: getScheduleByWorkOrder
     * Gets the service schedule for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getSchedule(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/schedule");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/schedule"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/schedule",
                                    WorkordersWebApi.class, "getSchedule"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetSchedule(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/schedule");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/schedule");

            if (schedule != null)
                builder.body(schedule.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/schedule"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/schedule",
                                    WorkordersWebApi.class, "updateSchedule"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateSchedule(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/schedule");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/schedule")
                    .urlParams("?async=" + async);

            if (schedule != null)
                builder.body(schedule.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/schedule?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/schedule",
                                    WorkordersWebApi.class, "updateSchedule"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateHoldsByWorkOrder
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds       Holds
     */
    public static void updateHolds(Context context, Integer workOrderId, String holds) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds");

            if (holds != null)
                builder.body(holds);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/holds"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/holds",
                                    WorkordersWebApi.class, "updateHolds"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateHolds(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/holds");
    }

    /**
     * Swagger operationId: updateHoldsByWorkOrder
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds       Holds
     * @param async       Async (Optional)
     */
    public static void updateHolds(Context context, Integer workOrderId, String holds, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds")
                    .urlParams("?async=" + async);

            if (holds != null)
                builder.body(holds);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/holds?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/holds",
                                    WorkordersWebApi.class, "updateHolds"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: resolveReopenReportProblemByWorkOrder
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId      ID of report problem flag
     * @param json        JSON payload
     */
    public static void resolveReopenReportProblem(Context context, Integer workOrderId, Integer flagId, Message json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/report-problem/{flag_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/report-problem/" + flagId,
                                    WorkordersWebApi.class, "resolveReopenReportProblem"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subResolveReopenReportProblem(Integer workOrderId, Integer flagId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/report-problem/" + flagId);
    }

    /**
     * Swagger operationId: resolveReopenReportProblemByWorkOrder
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId      ID of report problem flag
     * @param json        JSON payload
     * @param async       Async (Optional)
     */
    public static void resolveReopenReportProblem(Context context, Integer workOrderId, Integer flagId, Message json, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId)
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/report-problem/{flag_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/report-problem/" + flagId,
                                    WorkordersWebApi.class, "resolveReopenReportProblem"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/discounts")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/discounts"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts",
                                    WorkordersWebApi.class, "addDiscount"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddDiscount(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts");
    }

    /**
     * Swagger operationId: getDiscountsByWorkOrder
     * Returns a list of discounts applied by the assigned provider to reduce the payout of the work order.
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getDiscounts(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/discounts")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/discounts"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts",
                                    WorkordersWebApi.class, "getDiscounts"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetDiscounts(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/discounts");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/reorder/{position}/{target_task_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId,
                                    WorkordersWebApi.class, "reorderTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReorderTask(Integer workOrderId, Integer taskId, Integer targetTaskId, String position) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/group/{group}/{destination}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination,
                                    WorkordersWebApi.class, "groupTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGroupTask(Integer workOrderId, Integer taskId, String group, String destination) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination);
    }

    /**
     * Swagger operationId: addContactByWorkOrder
     * Adds a contact to a work order
     *
     * @param workOrderId Work order id
     * @param json        JSON Model
     */
    public static void addContact(Context context, Integer workOrderId, Contact json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/contacts")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/contacts"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts",
                                    WorkordersWebApi.class, "addContact"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddContact(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts");
    }

    /**
     * Swagger operationId: getContactsByWorkOrder
     * Get a list of contacts on a work order
     *
     * @param workOrderId  Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getContacts(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/contacts")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/contacts"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts",
                                    WorkordersWebApi.class, "getContacts"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetContacts(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/contacts");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments");

            if (shipment != null)
                builder.body(shipment.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/shipments"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments",
                                    WorkordersWebApi.class, "addShipment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddShipment(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments")
                    .urlParams("?async=" + async);

            if (shipment != null)
                builder.body(shipment.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/shipments?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments",
                                    WorkordersWebApi.class, "addShipment"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getShipments(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/shipments"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments",
                                    WorkordersWebApi.class, "getShipments"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetShipments(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/shipments");
    }

    /**
     * Swagger operationId: getPenaltiesByWorkOrder
     * Get a list of penalties and their applied status for a work order
     *
     * @param workOrderId  Work Order ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getPenalties(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/penalties")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/penalties"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties",
                                    WorkordersWebApi.class, "getPenalties"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetPenalties(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/penalties");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route");

            if (route != null)
                builder.body(route.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/route")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/route"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/route",
                                    WorkordersWebApi.class, "routeUser"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRouteUser(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/route");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route")
                    .urlParams("?async=" + async);

            if (route != null)
                builder.body(route.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/route")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/route?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/route",
                                    WorkordersWebApi.class, "routeUser"))
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route");

            if (route != null)
                builder.body(route.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/route")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/route"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/route",
                                    WorkordersWebApi.class, "unRouteUser"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnRouteUser(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/route");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/route")
                    .urlParams("?async=" + async);

            if (route != null)
                builder.body(route.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/route")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/route?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/route",
                                    WorkordersWebApi.class, "unRouteUser"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getWorkOrderLists(Context context, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/lists");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/lists")
                    .key(misc.md5("GET//api/rest/v2/workorders/lists"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/workorders/lists",
                                    WorkordersWebApi.class, "getWorkOrderLists"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetWorkOrderLists() {
        return register("TOPIC_ID_WEB_API_V2/workorders/lists");
    }

    /**
     * Swagger operationId: getProblemReasonsByWorkOrder
     * Gets list of problem reasons by work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getProblemReasons(Context context, String workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/messages");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/problems/messages")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/problems/messages"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/problems/messages",
                                    WorkordersWebApi.class, "getProblemReasons"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetProblemReasons(String workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/problems/messages");
    }

    /**
     * Swagger operationId: reportProblemByWorkOrder
     * Report a problem to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     */
    public static void reportProblem(Context context, String workOrderId, Message json) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/report-problem/messages");

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/report-problem/messages")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/report-problem/messages"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/report-problem/messages",
                                    WorkordersWebApi.class, "reportProblem"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReportProblem(String workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/report-problem/messages");
    }

    /**
     * Swagger operationId: reportProblemByWorkOrder
     * Report a problem to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     * @param async       Async (Optional)
     */
    public static void reportProblem(Context context, String workOrderId, Message json, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/report-problem/messages")
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/report-problem/messages")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/report-problem/messages?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/report-problem/messages",
                                    WorkordersWebApi.class, "reportProblem"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getBonuses(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/bonuses"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses",
                                    WorkordersWebApi.class, "getBonuses"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetBonuses(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses");
    }

    /**
     * Swagger operationId: getLocationByWorkOrder
     * Gets the address and geo information for a work order
     *
     * @param workOrderId  ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getLocation(Context context, Integer workOrderId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/location");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/location")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/location"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/location",
                                    WorkordersWebApi.class, "getLocation"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetLocation(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/location");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/location");

            if (location != null)
                builder.body(location.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/location")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/location"))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/location",
                                    WorkordersWebApi.class, "updateLocation"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateLocation(Integer workOrderId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/location");
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/location")
                    .urlParams("?async=" + async);

            if (location != null)
                builder.body(location.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/location")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/location?async=" + async))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/location",
                                    WorkordersWebApi.class, "updateLocation"))
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
     */
    public static void addBonus(Context context, Integer workOrderId, Integer bonusId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId,
                                    WorkordersWebApi.class, "addBonus"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            if (bonus != null)
                builder.body(bonus.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId,
                                    WorkordersWebApi.class, "addBonus"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
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
    public static void getBonus(Context context, Integer workOrderId, Integer bonusId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId,
                                    WorkordersWebApi.class, "getBonus"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId);
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
    public static void getBonus(Context context, Integer workOrderId, Integer bonusId, PayModifier bonus, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            if (bonus != null)
                builder.body(bonus.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId,
                                    WorkordersWebApi.class, "getBonus"))
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeBonusByWorkOrderAndBonus
     * Removes a bonus from a work order
     *
     * @param workOrderId Work Order ID
     * @param bonusId     Bonus ID
     */
    public static void removeBonus(Context context, Integer workOrderId, Integer bonusId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId,
                                    WorkordersWebApi.class, "removeBonus"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId);
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
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            if (bonus != null)
                builder.body(bonus.toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId,
                                    WorkordersWebApi.class, "updateBonus"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_WEB_API_V2/WorkordersWebApi/" + workOrderId + "/bonuses/" + bonusId);
    }


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            new AsyncParser(this, (Bundle) payload);
        }

        public void onWorkordersWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onRevertWorkOrderToDraft(boolean success, Error error) {
        }

        public void onIncompleteTask(boolean success, Error error) {
        }

        public void onGetCustomField(CustomField customField, boolean success, Error error) {
        }

        public void onUpdateCustomField(CustomField customField, boolean success, Error error) {
        }

        public void onCompleteWorkOrder(boolean success, Error error) {
        }

        public void onIncompleteWorkOrder(boolean success, Error error) {
        }

        public void onAddExpense(Expense expense, boolean success, Error error) {
        }

        public void onGetExpenses(Expenses expenses, boolean success, Error error) {
        }

        public void onAddAttachment(Attachment attachment, boolean success, Error error) {
        }

        public void onGetFolder(AttachmentFolder attachmentFolder, boolean success, Error error) {
        }

        public void onDeleteFolder(boolean success, Error error) {
        }

        public void onUpdateFolder(boolean success, Error error) {
        }

        public void onGetWorkOrders(WorkOrders workOrders, boolean success, Error error) {
        }

        public void onVerifyTimeLog(boolean success, Error error) {
        }

        public void onRemoveContact(boolean success, Error error) {
        }

        public void onUpdateContact(boolean success, Error error) {
        }

        public void onGetIncrease(PayIncrease payIncrease, boolean success, Error error) {
        }

        public void onDeleteIncrease(boolean success, Error error) {
        }

        public void onUpdateIncrease(PayIncrease payIncrease, boolean success, Error error) {
        }

        public void onDeleteExpense(boolean success, Error error) {
        }

        public void onUpdateExpense(Expense expense, boolean success, Error error) {
        }

        public void onGetIncreases(PayIncreases payIncreases, boolean success, Error error) {
        }

        public void onGetPay(Pay pay, boolean success, Error error) {
        }

        public void onUpdatePay(boolean success, Error error) {
        }

        public void onAddTask(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetTasks(Tasks tasks, boolean success, Error error) {
        }

        public void onGetMilestones(Milestones milestones, boolean success, Error error) {
        }

        public void onAddSignature(Signature signature, boolean success, Error error) {
        }

        public void onGetSignatures(Signature[] signatures, boolean success, Error error) {
        }

        public void onGetProviders(Users[] users, boolean success, Error error) {
        }

        public void onAddMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetMessages(Messages[] messages, boolean success, Error error) {
        }

        public void onCancelSwapRequest(SwapResponse swapResponse, boolean success, Error error) {
        }

        public void onAddTimeLog(AaaaPlaceholder aaaaPlaceholder, boolean success, Error error) {
        }

        public void onGetTimeLogs(TimeLogs timeLogs, boolean success, Error error) {
        }

        public void onUpdateAllTimeLogs(boolean success, Error error) {
        }

        public void onGetWorkOrder(WorkOrder workOrder, boolean success, Error error) {
        }

        public void onDeleteWorkOrder(boolean success, Error error) {
        }

        public void onUpdateWorkOrder(boolean success, Error error) {
        }

        public void onGetSignature(Signature signature, boolean success, Error error) {
        }

        public void onDeleteSignature(boolean success, Error error) {
        }

        public void onAddFolder(boolean success, Error error) {
        }

        public void onGetAttachments(AttachmentFolders attachmentFolders, boolean success, Error error) {
        }

        public void onCompleteTask(boolean success, Error error) {
        }

        public void onRemoveDiscount(boolean success, Error error) {
        }

        public void onUpdateDiscount(IdResponse idResponse, boolean success, Error error) {
        }

        public void onRemoveTimeLog(boolean success, Error error) {
        }

        public void onUpdateTimeLog(boolean success, Error error) {
        }

        public void onGetFile(Attachment attachment, boolean success, Error error) {
        }

        public void onDeleteAttachment(boolean success, Error error) {
        }

        public void onUpdateAttachment(boolean success, Error error) {
        }

        public void onAssignUser(boolean success, Error error) {
        }

        public void onGetAssignee(Assignee assignee, boolean success, Error error) {
        }

        public void onUnassignUser(boolean success, Error error) {
        }

        public void onPublish(boolean success, Error error) {
        }

        public void onUnpublish(boolean success, Error error) {
        }

        public void onGetStatus(Status status, boolean success, Error error) {
        }

        public void onApproveWorkOrder(boolean success, Error error) {
        }

        public void onUnapproveWorkOrder(boolean success, Error error) {
        }

        public void onAcceptIncrease(boolean success, Error error) {
        }

        public void onDeleteShipment(boolean success, Error error) {
        }

        public void onUpdateShipment(boolean success, Error error) {
        }

        public void onAddPenalty(boolean success, Error error) {
        }

        public void onGetPenalty(PayModifier payModifier, boolean success, Error error) {
        }

        public void onRemovePenalty(boolean success, Error error) {
        }

        public void onUpdatePenalty(boolean success, Error error) {
        }

        public void onDeclineSwapRequest(SwapResponse swapResponse, boolean success, Error error) {
        }

        public void onReplyMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onRemoveMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onUpdateMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetTask(Task task, boolean success, Error error) {
        }

        public void onRemoveTask(boolean success, Error error) {
        }

        public void onUpdateTask(boolean success, Error error) {
        }

        public void onDenyIncrease(boolean success, Error error) {
        }

        public void onAddAlertToWorkOrderAndTask(boolean success, Error error) {
        }

        public void onRemoveAlerts(boolean success, Error error) {
        }

        public void onRequest(boolean success, Error error) {
        }

        public void onRemoveRequest(boolean success, Error error) {
        }

        public void onAcceptSwapRequest(SwapResponse swapResponse, boolean success, Error error) {
        }

        public void onGetCustomFields(CustomFields customFields, boolean success, Error error) {
        }

        public void onRemoveAlert(boolean success, Error error) {
        }

        public void onGetSchedule(Schedule schedule, boolean success, Error error) {
        }

        public void onUpdateSchedule(boolean success, Error error) {
        }

        public void onUpdateHolds(boolean success, Error error) {
        }

        public void onResolveReopenReportProblem(boolean success, Error error) {
        }

        public void onAddDiscount(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetDiscounts(PayModifiers payModifiers, boolean success, Error error) {
        }

        public void onReorderTask(boolean success, Error error) {
        }

        public void onGroupTask(boolean success, Error error) {
        }

        public void onAddContact(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetContacts(Contacts contacts, boolean success, Error error) {
        }

        public void onAddShipment(IdResponse idResponse, boolean success, Error error) {
        }

        public void onGetShipments(Shipments shipments, boolean success, Error error) {
        }

        public void onGetPenalties(PayModifiers payModifiers, boolean success, Error error) {
        }

        public void onRouteUser(boolean success, Error error) {
        }

        public void onUnRouteUser(boolean success, Error error) {
        }

        public void onGetWorkOrderLists(SavedList[] savedList, boolean success, Error error) {
        }

        public void onGetProblemReasons(Problems[] problems, boolean success, Error error) {
        }

        public void onReportProblem(byte[] data, boolean success, Error error) {
        }

        public void onGetBonuses(PayModifiers payModifiers, boolean success, Error error) {
        }

        public void onGetLocation(Location location, boolean success, Error error) {
        }

        public void onUpdateLocation(boolean success, Error error) {
        }

        public void onAddBonus(boolean success, Error error) {
        }

        public void onGetBonus(PayModifier payModifier, boolean success, Error error) {
        }

        public void onRemoveBonus(boolean success, Error error) {
        }

        public void onUpdateBonus(boolean success, Error error) {
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
            try {
                switch (transactionParams.apiFunction) {
                    case "revertWorkOrderToDraft":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "incompleteTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getCustomField":
                        if (success)
                            successObject = CustomField.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateCustomField":
                        if (success)
                            successObject = CustomField.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "completeWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "incompleteWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addExpense":
                        if (success)
                            successObject = Expense.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getExpenses":
                        if (success)
                            successObject = Expenses.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addAttachment":
                        if (success)
                            successObject = Attachment.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getFolder":
                        if (success)
                            successObject = AttachmentFolder.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteFolder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateFolder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getWorkOrders":
                        if (success)
                            successObject = WorkOrders.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "verifyTimeLog":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeContact":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateContact":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getIncrease":
                        if (success)
                            successObject = PayIncrease.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteIncrease":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateIncrease":
                        if (success)
                            successObject = PayIncrease.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteExpense":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateExpense":
                        if (success)
                            successObject = Expense.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getIncreases":
                        if (success)
                            successObject = PayIncreases.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPay":
                        if (success)
                            successObject = Pay.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updatePay":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addTask":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTasks":
                        if (success)
                            successObject = Tasks.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getMilestones":
                        if (success)
                            successObject = Milestones.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addSignature":
                        if (success)
                            successObject = Signature.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getSignatures":
                        if (success)
                            successObject = Signature.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getProviders":
                        if (success)
                            successObject = Users.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getMessages":
                        if (success)
                            successObject = Messages.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "cancelSwapRequest":
                        if (success)
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addTimeLog":
                        if (success)
                            successObject = AaaaPlaceholder.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTimeLogs":
                        if (success)
                            successObject = TimeLogs.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateAllTimeLogs":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getWorkOrder":
                        if (success)
                            successObject = WorkOrder.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getSignature":
                        if (success)
                            successObject = Signature.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteSignature":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addFolder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getAttachments":
                        if (success)
                            successObject = AttachmentFolders.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "completeTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeDiscount":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateDiscount":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeTimeLog":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateTimeLog":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getFile":
                        if (success)
                            successObject = Attachment.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteAttachment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateAttachment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "assignUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getAssignee":
                        if (success)
                            successObject = Assignee.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unassignUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "publish":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unpublish":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getStatus":
                        if (success)
                            successObject = com.fieldnation.data.bv2.model.Status.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "approveWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unapproveWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "acceptIncrease":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteShipment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateShipment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addPenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPenalty":
                        if (success)
                            successObject = PayModifier.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removePenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updatePenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "declineSwapRequest":
                        if (success)
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "replyMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTask":
                        if (success)
                            successObject = Task.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "denyIncrease":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addAlertToWorkOrderAndTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeAlerts":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "request":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeRequest":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "acceptSwapRequest":
                        if (success)
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getCustomFields":
                        if (success)
                            successObject = CustomFields.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeAlert":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getSchedule":
                        if (success)
                            successObject = Schedule.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateSchedule":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateHolds":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "resolveReopenReportProblem":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addDiscount":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getDiscounts":
                        if (success)
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "reorderTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "groupTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addContact":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getContacts":
                        if (success)
                            successObject = Contacts.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addShipment":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getShipments":
                        if (success)
                            successObject = Shipments.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPenalties":
                        if (success)
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "routeUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unRouteUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getWorkOrderLists":
                        if (success)
                            successObject = SavedList.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getProblemReasons":
                        if (success)
                            successObject = Problems.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "reportProblem":
                        if (success)
                            successObject = data;
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getBonuses":
                        if (success)
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getLocation":
                        if (success)
                            successObject = Location.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateLocation":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addBonus":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getBonus":
                        if (success)
                            successObject = PayModifier.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeBonus":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateBonus":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {
                listener.onWorkordersWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "revertWorkOrderToDraft":
                        listener.onRevertWorkOrderToDraft(success, (Error) failObject);
                        break;
                    case "incompleteTask":
                        listener.onIncompleteTask(success, (Error) failObject);
                        break;
                    case "getCustomField":
                        listener.onGetCustomField((CustomField) successObject, success, (Error) failObject);
                        break;
                    case "updateCustomField":
                        listener.onUpdateCustomField((CustomField) successObject, success, (Error) failObject);
                        break;
                    case "completeWorkOrder":
                        listener.onCompleteWorkOrder(success, (Error) failObject);
                        break;
                    case "incompleteWorkOrder":
                        listener.onIncompleteWorkOrder(success, (Error) failObject);
                        break;
                    case "addExpense":
                        listener.onAddExpense((Expense) successObject, success, (Error) failObject);
                        break;
                    case "getExpenses":
                        listener.onGetExpenses((Expenses) successObject, success, (Error) failObject);
                        break;
                    case "addAttachment":
                        listener.onAddAttachment((Attachment) successObject, success, (Error) failObject);
                        break;
                    case "getFolder":
                        listener.onGetFolder((AttachmentFolder) successObject, success, (Error) failObject);
                        break;
                    case "deleteFolder":
                        listener.onDeleteFolder(success, (Error) failObject);
                        break;
                    case "updateFolder":
                        listener.onUpdateFolder(success, (Error) failObject);
                        break;
                    case "getWorkOrders":
                        listener.onGetWorkOrders((WorkOrders) successObject, success, (Error) failObject);
                        break;
                    case "verifyTimeLog":
                        listener.onVerifyTimeLog(success, (Error) failObject);
                        break;
                    case "removeContact":
                        listener.onRemoveContact(success, (Error) failObject);
                        break;
                    case "updateContact":
                        listener.onUpdateContact(success, (Error) failObject);
                        break;
                    case "getIncrease":
                        listener.onGetIncrease((PayIncrease) successObject, success, (Error) failObject);
                        break;
                    case "deleteIncrease":
                        listener.onDeleteIncrease(success, (Error) failObject);
                        break;
                    case "updateIncrease":
                        listener.onUpdateIncrease((PayIncrease) successObject, success, (Error) failObject);
                        break;
                    case "deleteExpense":
                        listener.onDeleteExpense(success, (Error) failObject);
                        break;
                    case "updateExpense":
                        listener.onUpdateExpense((Expense) successObject, success, (Error) failObject);
                        break;
                    case "getIncreases":
                        listener.onGetIncreases((PayIncreases) successObject, success, (Error) failObject);
                        break;
                    case "getPay":
                        listener.onGetPay((Pay) successObject, success, (Error) failObject);
                        break;
                    case "updatePay":
                        listener.onUpdatePay(success, (Error) failObject);
                        break;
                    case "addTask":
                        listener.onAddTask((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getTasks":
                        listener.onGetTasks((Tasks) successObject, success, (Error) failObject);
                        break;
                    case "getMilestones":
                        listener.onGetMilestones((Milestones) successObject, success, (Error) failObject);
                        break;
                    case "addSignature":
                        listener.onAddSignature((Signature) successObject, success, (Error) failObject);
                        break;
                    case "getSignatures":
                        listener.onGetSignatures((Signature[]) successObject, success, (Error) failObject);
                        break;
                    case "getProviders":
                        listener.onGetProviders((Users[]) successObject, success, (Error) failObject);
                        break;
                    case "addMessage":
                        listener.onAddMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getMessages":
                        listener.onGetMessages((Messages[]) successObject, success, (Error) failObject);
                        break;
                    case "cancelSwapRequest":
                        listener.onCancelSwapRequest((SwapResponse) successObject, success, (Error) failObject);
                        break;
                    case "addTimeLog":
                        listener.onAddTimeLog((AaaaPlaceholder) successObject, success, (Error) failObject);
                        break;
                    case "getTimeLogs":
                        listener.onGetTimeLogs((TimeLogs) successObject, success, (Error) failObject);
                        break;
                    case "updateAllTimeLogs":
                        listener.onUpdateAllTimeLogs(success, (Error) failObject);
                        break;
                    case "getWorkOrder":
                        listener.onGetWorkOrder((WorkOrder) successObject, success, (Error) failObject);
                        break;
                    case "deleteWorkOrder":
                        listener.onDeleteWorkOrder(success, (Error) failObject);
                        break;
                    case "updateWorkOrder":
                        listener.onUpdateWorkOrder(success, (Error) failObject);
                        break;
                    case "getSignature":
                        listener.onGetSignature((Signature) successObject, success, (Error) failObject);
                        break;
                    case "deleteSignature":
                        listener.onDeleteSignature(success, (Error) failObject);
                        break;
                    case "addFolder":
                        listener.onAddFolder(success, (Error) failObject);
                        break;
                    case "getAttachments":
                        listener.onGetAttachments((AttachmentFolders) successObject, success, (Error) failObject);
                        break;
                    case "completeTask":
                        listener.onCompleteTask(success, (Error) failObject);
                        break;
                    case "removeDiscount":
                        listener.onRemoveDiscount(success, (Error) failObject);
                        break;
                    case "updateDiscount":
                        listener.onUpdateDiscount((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "removeTimeLog":
                        listener.onRemoveTimeLog(success, (Error) failObject);
                        break;
                    case "updateTimeLog":
                        listener.onUpdateTimeLog(success, (Error) failObject);
                        break;
                    case "getFile":
                        listener.onGetFile((Attachment) successObject, success, (Error) failObject);
                        break;
                    case "deleteAttachment":
                        listener.onDeleteAttachment(success, (Error) failObject);
                        break;
                    case "updateAttachment":
                        listener.onUpdateAttachment(success, (Error) failObject);
                        break;
                    case "assignUser":
                        listener.onAssignUser(success, (Error) failObject);
                        break;
                    case "getAssignee":
                        listener.onGetAssignee((Assignee) successObject, success, (Error) failObject);
                        break;
                    case "unassignUser":
                        listener.onUnassignUser(success, (Error) failObject);
                        break;
                    case "publish":
                        listener.onPublish(success, (Error) failObject);
                        break;
                    case "unpublish":
                        listener.onUnpublish(success, (Error) failObject);
                        break;
                    case "getStatus":
                        listener.onGetStatus((com.fieldnation.data.bv2.model.Status) successObject, success, (Error) failObject);
                        break;
                    case "approveWorkOrder":
                        listener.onApproveWorkOrder(success, (Error) failObject);
                        break;
                    case "unapproveWorkOrder":
                        listener.onUnapproveWorkOrder(success, (Error) failObject);
                        break;
                    case "acceptIncrease":
                        listener.onAcceptIncrease(success, (Error) failObject);
                        break;
                    case "deleteShipment":
                        listener.onDeleteShipment(success, (Error) failObject);
                        break;
                    case "updateShipment":
                        listener.onUpdateShipment(success, (Error) failObject);
                        break;
                    case "addPenalty":
                        listener.onAddPenalty(success, (Error) failObject);
                        break;
                    case "getPenalty":
                        listener.onGetPenalty((PayModifier) successObject, success, (Error) failObject);
                        break;
                    case "removePenalty":
                        listener.onRemovePenalty(success, (Error) failObject);
                        break;
                    case "updatePenalty":
                        listener.onUpdatePenalty(success, (Error) failObject);
                        break;
                    case "declineSwapRequest":
                        listener.onDeclineSwapRequest((SwapResponse) successObject, success, (Error) failObject);
                        break;
                    case "replyMessage":
                        listener.onReplyMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "removeMessage":
                        listener.onRemoveMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "updateMessage":
                        listener.onUpdateMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getTask":
                        listener.onGetTask((Task) successObject, success, (Error) failObject);
                        break;
                    case "removeTask":
                        listener.onRemoveTask(success, (Error) failObject);
                        break;
                    case "updateTask":
                        listener.onUpdateTask(success, (Error) failObject);
                        break;
                    case "denyIncrease":
                        listener.onDenyIncrease(success, (Error) failObject);
                        break;
                    case "addAlertToWorkOrderAndTask":
                        listener.onAddAlertToWorkOrderAndTask(success, (Error) failObject);
                        break;
                    case "removeAlerts":
                        listener.onRemoveAlerts(success, (Error) failObject);
                        break;
                    case "request":
                        listener.onRequest(success, (Error) failObject);
                        break;
                    case "removeRequest":
                        listener.onRemoveRequest(success, (Error) failObject);
                        break;
                    case "acceptSwapRequest":
                        listener.onAcceptSwapRequest((SwapResponse) successObject, success, (Error) failObject);
                        break;
                    case "getCustomFields":
                        listener.onGetCustomFields((CustomFields) successObject, success, (Error) failObject);
                        break;
                    case "removeAlert":
                        listener.onRemoveAlert(success, (Error) failObject);
                        break;
                    case "getSchedule":
                        listener.onGetSchedule((Schedule) successObject, success, (Error) failObject);
                        break;
                    case "updateSchedule":
                        listener.onUpdateSchedule(success, (Error) failObject);
                        break;
                    case "updateHolds":
                        listener.onUpdateHolds(success, (Error) failObject);
                        break;
                    case "resolveReopenReportProblem":
                        listener.onResolveReopenReportProblem(success, (Error) failObject);
                        break;
                    case "addDiscount":
                        listener.onAddDiscount((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getDiscounts":
                        listener.onGetDiscounts((PayModifiers) successObject, success, (Error) failObject);
                        break;
                    case "reorderTask":
                        listener.onReorderTask(success, (Error) failObject);
                        break;
                    case "groupTask":
                        listener.onGroupTask(success, (Error) failObject);
                        break;
                    case "addContact":
                        listener.onAddContact((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getContacts":
                        listener.onGetContacts((Contacts) successObject, success, (Error) failObject);
                        break;
                    case "addShipment":
                        listener.onAddShipment((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "getShipments":
                        listener.onGetShipments((Shipments) successObject, success, (Error) failObject);
                        break;
                    case "getPenalties":
                        listener.onGetPenalties((PayModifiers) successObject, success, (Error) failObject);
                        break;
                    case "routeUser":
                        listener.onRouteUser(success, (Error) failObject);
                        break;
                    case "unRouteUser":
                        listener.onUnRouteUser(success, (Error) failObject);
                        break;
                    case "getWorkOrderLists":
                        listener.onGetWorkOrderLists((SavedList[]) successObject, success, (Error) failObject);
                        break;
                    case "getProblemReasons":
                        listener.onGetProblemReasons((Problems[]) successObject, success, (Error) failObject);
                        break;
                    case "reportProblem":
                        listener.onReportProblem((byte[]) successObject, success, (Error) failObject);
                        break;
                    case "getBonuses":
                        listener.onGetBonuses((PayModifiers) successObject, success, (Error) failObject);
                        break;
                    case "getLocation":
                        listener.onGetLocation((Location) successObject, success, (Error) failObject);
                        break;
                    case "updateLocation":
                        listener.onUpdateLocation(success, (Error) failObject);
                        break;
                    case "addBonus":
                        listener.onAddBonus(success, (Error) failObject);
                        break;
                    case "getBonus":
                        listener.onGetBonus((PayModifier) successObject, success, (Error) failObject);
                        break;
                    case "removeBonus":
                        listener.onRemoveBonus(success, (Error) failObject);
                        break;
                    case "updateBonus":
                        listener.onUpdateBonus(success, (Error) failObject);
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }
}

package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/27/17.
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
    /**
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/draft"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRevertWorkOrderToDraft(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/draft"));
    }
    /**
     * Reverts a work order to draft status
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/draft" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRevertWorkOrderToDraft(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/draft" + "?async=" + async));
    }
    /**
     * Marks a task associated with a work order as incomplete
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     */
    public static void incompleteTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/incomplete");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/incomplete")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/incomplete"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subIncompleteTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/incomplete"));
    }
    /**
     * Get a custom field by work order and custom field
     *
     * @param workOrderId ID of work order
     * @param customFieldId Custom field id
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomField(Context context, Integer workOrderId, Integer customFieldId, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId));
    }
    /**
     * Update a custom field value on a work order
     *
     * @param workOrderId Work Order ID
     * @param customFieldId Custom field ID
     * @param customField Custom field
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomField(Integer workOrderId, Integer customFieldId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId));
    }
    /**
     * Update a custom field value on a work order
     *
     * @param workOrderId Work Order ID
     * @param customFieldId Custom field ID
     * @param customField Custom field
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateCustomField(Integer workOrderId, Integer customFieldId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields/" + customFieldId + "?async=" + async));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/complete"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCompleteWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/complete"));
    }
    /**
     * Marks a work order complete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/complete" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCompleteWorkOrder(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/complete" + "?async=" + async));
    }
    /**
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param reason Reason
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/complete" + "?reason=" + reason))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subIncompleteWorkOrder(Integer workOrderId, String reason) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/complete" + "?reason=" + reason));
    }
    /**
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param reason Reason
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/complete" + "?reason=" + reason + "&async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subIncompleteWorkOrder(Integer workOrderId, String reason, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/complete" + "?reason=" + reason + "&async=" + async));
    }
    /**
     * Adds an expense on a work order
     *
     * @param workOrderId ID of work order
     * @param expense Expense
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddExpense(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses"));
    }
    /**
     * Adds an expense on a work order
     *
     * @param workOrderId ID of work order
     * @param expense Expense
     * @param async Asynchroneous (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddExpense(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses" + "?async=" + async));
    }
    /**
     * Get all expenses of a work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses"));
    }
    /**
     * Uploads a file by an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachment Folder
     * @param file File
     */
    public static void addAttachment(Context context, Integer workOrderId, Integer folderId, String attachment, java.io.File file) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .multipartField("attachment", attachment)                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAttachment(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId));
    }
    /**
     * Uploads a file by an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachment Folder
     * @param file File
     * @param async Async (Optional)
     */
    public static void addAttachment(Context context, Integer workOrderId, Integer folderId, String attachment, java.io.File file, Boolean async) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async)
                    .multipartField("attachment", attachment)                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAttachment(Integer workOrderId, Integer folderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async));
    }
    /**
     * Gets an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId));
    }
    /**
     * Deletes an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     */
    public static void deleteFolder(Context context, Integer workOrderId, Integer folderId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteFolder(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId));
    }
    /**
     * Deletes an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteFolder(Integer workOrderId, Integer folderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async));
    }
    /**
     * Updates an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param folder Folder
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateFolder(Integer workOrderId, Integer folderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId));
    }
    /**
     * Updates an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param folder Folder
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateFolder(Integer workOrderId, Integer folderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders"));
    }
    /**
     * Returns a list of work orders.
     *
     * @param getWorkOrdersOptions Additional optional parameters
     * @param isBackground indicates that this call is low priority
     */
    public static void getWorkOrders(Context context, GetWorkOrdersOptions getWorkOrdersOptions, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders")
                    .urlParams("" + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
                                    + "" + (getWorkOrdersOptions.getColumns() != null ? "&columns=" + getWorkOrdersOptions.getColumns() : "")
                                    + "" + (getWorkOrdersOptions.getPage() != null ? "&page=" + getWorkOrdersOptions.getPage() : "")
                                    + "" + (getWorkOrdersOptions.getPerPage() != null ? "&per_page=" + getWorkOrdersOptions.getPerPage() : "")
                                    + "" + (getWorkOrdersOptions.getView() != null ? "&view=" + getWorkOrdersOptions.getView() : "")
                                    + "" + (getWorkOrdersOptions.getSticky() != null ? "&sticky=" + getWorkOrdersOptions.getSticky() : "")
                                    + "" + (getWorkOrdersOptions.getSort() != null ? "&sort=" + getWorkOrdersOptions.getSort() : "")
                                    + "" + (getWorkOrdersOptions.getOrder() != null ? "&order=" + getWorkOrdersOptions.getOrder() : "")
                                    + "" + (getWorkOrdersOptions.getF() != null ? "&f_=" + getWorkOrdersOptions.getF() : "")
                                    + "" + (getWorkOrdersOptions.getFMaxApprovalTime() != null ? "&f_max_approval_time=" + getWorkOrdersOptions.getFMaxApprovalTime() : "")
                                    + "" + (getWorkOrdersOptions.getFRating() != null ? "&f_rating=" + getWorkOrdersOptions.getFRating() : "")
                                    + "" + (getWorkOrdersOptions.getFRequests() != null ? "&f_requests=" + getWorkOrdersOptions.getFRequests() : "")
                                    + "" + (getWorkOrdersOptions.getFCounterOffers() != null ? "&f_counter_offers=" + getWorkOrdersOptions.getFCounterOffers() : "")
                                    + "" + (getWorkOrdersOptions.getFHourly() != null ? "&f_hourly=" + getWorkOrdersOptions.getFHourly() : "")
                                    + "" + (getWorkOrdersOptions.getFFixed() != null ? "&f_fixed=" + getWorkOrdersOptions.getFFixed() : "")
                                    + "" + (getWorkOrdersOptions.getFDevice() != null ? "&f_device=" + getWorkOrdersOptions.getFDevice() : "")
                                    + "" + (getWorkOrdersOptions.getFPay() != null ? "&f_pay=" + getWorkOrdersOptions.getFPay() : "")
                                    + "" + (getWorkOrdersOptions.getFTemplates() != null ? "&f_templates=" + getWorkOrdersOptions.getFTemplates() : "")
                                    + "" + (getWorkOrdersOptions.getFTypeOfWork() != null ? "&f_type_of_work=" + getWorkOrdersOptions.getFTypeOfWork() : "")
                                    + "" + (getWorkOrdersOptions.getFTimeZone() != null ? "&f_time_zone=" + getWorkOrdersOptions.getFTimeZone() : "")
                                    + "" + (getWorkOrdersOptions.getFMode() != null ? "&f_mode=" + getWorkOrdersOptions.getFMode() : "")
                                    + "" + (getWorkOrdersOptions.getFCompany() != null ? "&f_company=" + getWorkOrdersOptions.getFCompany() : "")
                                    + "" + (getWorkOrdersOptions.getFWorkedWith() != null ? "&f_worked_with=" + getWorkOrdersOptions.getFWorkedWith() : "")
                                    + "" + (getWorkOrdersOptions.getFManager() != null ? "&f_manager=" + getWorkOrdersOptions.getFManager() : "")
                                    + "" + (getWorkOrdersOptions.getFClient() != null ? "&f_client=" + getWorkOrdersOptions.getFClient() : "")
                                    + "" + (getWorkOrdersOptions.getFProject() != null ? "&f_project=" + getWorkOrdersOptions.getFProject() : "")
                                    + "" + (getWorkOrdersOptions.getFApprovalWindow() != null ? "&f_approval_window=" + getWorkOrdersOptions.getFApprovalWindow() : "")
                                    + "" + (getWorkOrdersOptions.getFReviewWindow() != null ? "&f_review_window=" + getWorkOrdersOptions.getFReviewWindow() : "")
                                    + "" + (getWorkOrdersOptions.getFNetwork() != null ? "&f_network=" + getWorkOrdersOptions.getFNetwork() : "")
                                    + "" + (getWorkOrdersOptions.getFAutoAssign() != null ? "&f_auto_assign=" + getWorkOrdersOptions.getFAutoAssign() : "")
                                    + "" + (getWorkOrdersOptions.getFSchedule() != null ? "&f_schedule=" + getWorkOrdersOptions.getFSchedule() : "")
                                    + "" + (getWorkOrdersOptions.getFCreated() != null ? "&f_created=" + getWorkOrdersOptions.getFCreated() : "")
                                    + "" + (getWorkOrdersOptions.getFPublished() != null ? "&f_published=" + getWorkOrdersOptions.getFPublished() : "")
                                    + "" + (getWorkOrdersOptions.getFRouted() != null ? "&f_routed=" + getWorkOrdersOptions.getFRouted() : "")
                                    + "" + (getWorkOrdersOptions.getFPublishedRouted() != null ? "&f_published_routed=" + getWorkOrdersOptions.getFPublishedRouted() : "")
                                    + "" + (getWorkOrdersOptions.getFCompleted() != null ? "&f_completed=" + getWorkOrdersOptions.getFCompleted() : "")
                                    + "" + (getWorkOrdersOptions.getFApprovedCancelled() != null ? "&f_approved_cancelled=" + getWorkOrdersOptions.getFApprovedCancelled() : "")
                                    + "" + (getWorkOrdersOptions.getFConfirmed() != null ? "&f_confirmed=" + getWorkOrdersOptions.getFConfirmed() : "")
                                    + "" + (getWorkOrdersOptions.getFAssigned() != null ? "&f_assigned=" + getWorkOrdersOptions.getFAssigned() : "")
                                    + "" + (getWorkOrdersOptions.getFSavedLocation() != null ? "&f_saved_location=" + getWorkOrdersOptions.getFSavedLocation() : "")
                                    + "" + (getWorkOrdersOptions.getFSavedLocationGroup() != null ? "&f_saved_location_group=" + getWorkOrdersOptions.getFSavedLocationGroup() : "")
                                    + "" + (getWorkOrdersOptions.getFCity() != null ? "&f_city=" + getWorkOrdersOptions.getFCity() : "")
                                    + "" + (getWorkOrdersOptions.getFState() != null ? "&f_state=" + getWorkOrdersOptions.getFState() : "")
                                    + "" + (getWorkOrdersOptions.getFPostalCode() != null ? "&f_postal_code=" + getWorkOrdersOptions.getFPostalCode() : "")
                                    + "" + (getWorkOrdersOptions.getFCountry() != null ? "&f_country=" + getWorkOrdersOptions.getFCountry() : "")
                                    + "" + (getWorkOrdersOptions.getFFlags() != null ? "&f_flags=" + getWorkOrdersOptions.getFFlags() : "")
                                    + "" + (getWorkOrdersOptions.getFAssignment() != null ? "&f_assignment=" + getWorkOrdersOptions.getFAssignment() : "")
                                    + "" + (getWorkOrdersOptions.getFConfirmation() != null ? "&f_confirmation=" + getWorkOrdersOptions.getFConfirmation() : "")
                                    + "" + (getWorkOrdersOptions.getFFinancing() != null ? "&f_financing=" + getWorkOrdersOptions.getFFinancing() : "")
                                    + "" + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                                    + "" + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                                   );

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders" + "" + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
                                    + "" + (getWorkOrdersOptions.getColumns() != null ? "&columns=" + getWorkOrdersOptions.getColumns() : "")
                                    + "" + (getWorkOrdersOptions.getPage() != null ? "&page=" + getWorkOrdersOptions.getPage() : "")
                                    + "" + (getWorkOrdersOptions.getPerPage() != null ? "&per_page=" + getWorkOrdersOptions.getPerPage() : "")
                                    + "" + (getWorkOrdersOptions.getView() != null ? "&view=" + getWorkOrdersOptions.getView() : "")
                                    + "" + (getWorkOrdersOptions.getSticky() != null ? "&sticky=" + getWorkOrdersOptions.getSticky() : "")
                                    + "" + (getWorkOrdersOptions.getSort() != null ? "&sort=" + getWorkOrdersOptions.getSort() : "")
                                    + "" + (getWorkOrdersOptions.getOrder() != null ? "&order=" + getWorkOrdersOptions.getOrder() : "")
                                    + "" + (getWorkOrdersOptions.getF() != null ? "&f_=" + getWorkOrdersOptions.getF() : "")
                                    + "" + (getWorkOrdersOptions.getFMaxApprovalTime() != null ? "&f_max_approval_time=" + getWorkOrdersOptions.getFMaxApprovalTime() : "")
                                    + "" + (getWorkOrdersOptions.getFRating() != null ? "&f_rating=" + getWorkOrdersOptions.getFRating() : "")
                                    + "" + (getWorkOrdersOptions.getFRequests() != null ? "&f_requests=" + getWorkOrdersOptions.getFRequests() : "")
                                    + "" + (getWorkOrdersOptions.getFCounterOffers() != null ? "&f_counter_offers=" + getWorkOrdersOptions.getFCounterOffers() : "")
                                    + "" + (getWorkOrdersOptions.getFHourly() != null ? "&f_hourly=" + getWorkOrdersOptions.getFHourly() : "")
                                    + "" + (getWorkOrdersOptions.getFFixed() != null ? "&f_fixed=" + getWorkOrdersOptions.getFFixed() : "")
                                    + "" + (getWorkOrdersOptions.getFDevice() != null ? "&f_device=" + getWorkOrdersOptions.getFDevice() : "")
                                    + "" + (getWorkOrdersOptions.getFPay() != null ? "&f_pay=" + getWorkOrdersOptions.getFPay() : "")
                                    + "" + (getWorkOrdersOptions.getFTemplates() != null ? "&f_templates=" + getWorkOrdersOptions.getFTemplates() : "")
                                    + "" + (getWorkOrdersOptions.getFTypeOfWork() != null ? "&f_type_of_work=" + getWorkOrdersOptions.getFTypeOfWork() : "")
                                    + "" + (getWorkOrdersOptions.getFTimeZone() != null ? "&f_time_zone=" + getWorkOrdersOptions.getFTimeZone() : "")
                                    + "" + (getWorkOrdersOptions.getFMode() != null ? "&f_mode=" + getWorkOrdersOptions.getFMode() : "")
                                    + "" + (getWorkOrdersOptions.getFCompany() != null ? "&f_company=" + getWorkOrdersOptions.getFCompany() : "")
                                    + "" + (getWorkOrdersOptions.getFWorkedWith() != null ? "&f_worked_with=" + getWorkOrdersOptions.getFWorkedWith() : "")
                                    + "" + (getWorkOrdersOptions.getFManager() != null ? "&f_manager=" + getWorkOrdersOptions.getFManager() : "")
                                    + "" + (getWorkOrdersOptions.getFClient() != null ? "&f_client=" + getWorkOrdersOptions.getFClient() : "")
                                    + "" + (getWorkOrdersOptions.getFProject() != null ? "&f_project=" + getWorkOrdersOptions.getFProject() : "")
                                    + "" + (getWorkOrdersOptions.getFApprovalWindow() != null ? "&f_approval_window=" + getWorkOrdersOptions.getFApprovalWindow() : "")
                                    + "" + (getWorkOrdersOptions.getFReviewWindow() != null ? "&f_review_window=" + getWorkOrdersOptions.getFReviewWindow() : "")
                                    + "" + (getWorkOrdersOptions.getFNetwork() != null ? "&f_network=" + getWorkOrdersOptions.getFNetwork() : "")
                                    + "" + (getWorkOrdersOptions.getFAutoAssign() != null ? "&f_auto_assign=" + getWorkOrdersOptions.getFAutoAssign() : "")
                                    + "" + (getWorkOrdersOptions.getFSchedule() != null ? "&f_schedule=" + getWorkOrdersOptions.getFSchedule() : "")
                                    + "" + (getWorkOrdersOptions.getFCreated() != null ? "&f_created=" + getWorkOrdersOptions.getFCreated() : "")
                                    + "" + (getWorkOrdersOptions.getFPublished() != null ? "&f_published=" + getWorkOrdersOptions.getFPublished() : "")
                                    + "" + (getWorkOrdersOptions.getFRouted() != null ? "&f_routed=" + getWorkOrdersOptions.getFRouted() : "")
                                    + "" + (getWorkOrdersOptions.getFPublishedRouted() != null ? "&f_published_routed=" + getWorkOrdersOptions.getFPublishedRouted() : "")
                                    + "" + (getWorkOrdersOptions.getFCompleted() != null ? "&f_completed=" + getWorkOrdersOptions.getFCompleted() : "")
                                    + "" + (getWorkOrdersOptions.getFApprovedCancelled() != null ? "&f_approved_cancelled=" + getWorkOrdersOptions.getFApprovedCancelled() : "")
                                    + "" + (getWorkOrdersOptions.getFConfirmed() != null ? "&f_confirmed=" + getWorkOrdersOptions.getFConfirmed() : "")
                                    + "" + (getWorkOrdersOptions.getFAssigned() != null ? "&f_assigned=" + getWorkOrdersOptions.getFAssigned() : "")
                                    + "" + (getWorkOrdersOptions.getFSavedLocation() != null ? "&f_saved_location=" + getWorkOrdersOptions.getFSavedLocation() : "")
                                    + "" + (getWorkOrdersOptions.getFSavedLocationGroup() != null ? "&f_saved_location_group=" + getWorkOrdersOptions.getFSavedLocationGroup() : "")
                                    + "" + (getWorkOrdersOptions.getFCity() != null ? "&f_city=" + getWorkOrdersOptions.getFCity() : "")
                                    + "" + (getWorkOrdersOptions.getFState() != null ? "&f_state=" + getWorkOrdersOptions.getFState() : "")
                                    + "" + (getWorkOrdersOptions.getFPostalCode() != null ? "&f_postal_code=" + getWorkOrdersOptions.getFPostalCode() : "")
                                    + "" + (getWorkOrdersOptions.getFCountry() != null ? "&f_country=" + getWorkOrdersOptions.getFCountry() : "")
                                    + "" + (getWorkOrdersOptions.getFFlags() != null ? "&f_flags=" + getWorkOrdersOptions.getFFlags() : "")
                                    + "" + (getWorkOrdersOptions.getFAssignment() != null ? "&f_assignment=" + getWorkOrdersOptions.getFAssignment() : "")
                                    + "" + (getWorkOrdersOptions.getFConfirmation() != null ? "&f_confirmation=" + getWorkOrdersOptions.getFConfirmation() : "")
                                    + "" + (getWorkOrdersOptions.getFFinancing() != null ? "&f_financing=" + getWorkOrdersOptions.getFFinancing() : "")
                                    + "" + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                                    + "" + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                                   ))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetWorkOrders(GetWorkOrdersOptions getWorkOrdersOptions) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders" + "" + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
                                    + "" + (getWorkOrdersOptions.getColumns() != null ? "&columns=" + getWorkOrdersOptions.getColumns() : "")
                                    + "" + (getWorkOrdersOptions.getPage() != null ? "&page=" + getWorkOrdersOptions.getPage() : "")
                                    + "" + (getWorkOrdersOptions.getPerPage() != null ? "&per_page=" + getWorkOrdersOptions.getPerPage() : "")
                                    + "" + (getWorkOrdersOptions.getView() != null ? "&view=" + getWorkOrdersOptions.getView() : "")
                                    + "" + (getWorkOrdersOptions.getSticky() != null ? "&sticky=" + getWorkOrdersOptions.getSticky() : "")
                                    + "" + (getWorkOrdersOptions.getSort() != null ? "&sort=" + getWorkOrdersOptions.getSort() : "")
                                    + "" + (getWorkOrdersOptions.getOrder() != null ? "&order=" + getWorkOrdersOptions.getOrder() : "")
                                    + "" + (getWorkOrdersOptions.getF() != null ? "&f_=" + getWorkOrdersOptions.getF() : "")
                                    + "" + (getWorkOrdersOptions.getFMaxApprovalTime() != null ? "&f_max_approval_time=" + getWorkOrdersOptions.getFMaxApprovalTime() : "")
                                    + "" + (getWorkOrdersOptions.getFRating() != null ? "&f_rating=" + getWorkOrdersOptions.getFRating() : "")
                                    + "" + (getWorkOrdersOptions.getFRequests() != null ? "&f_requests=" + getWorkOrdersOptions.getFRequests() : "")
                                    + "" + (getWorkOrdersOptions.getFCounterOffers() != null ? "&f_counter_offers=" + getWorkOrdersOptions.getFCounterOffers() : "")
                                    + "" + (getWorkOrdersOptions.getFHourly() != null ? "&f_hourly=" + getWorkOrdersOptions.getFHourly() : "")
                                    + "" + (getWorkOrdersOptions.getFFixed() != null ? "&f_fixed=" + getWorkOrdersOptions.getFFixed() : "")
                                    + "" + (getWorkOrdersOptions.getFDevice() != null ? "&f_device=" + getWorkOrdersOptions.getFDevice() : "")
                                    + "" + (getWorkOrdersOptions.getFPay() != null ? "&f_pay=" + getWorkOrdersOptions.getFPay() : "")
                                    + "" + (getWorkOrdersOptions.getFTemplates() != null ? "&f_templates=" + getWorkOrdersOptions.getFTemplates() : "")
                                    + "" + (getWorkOrdersOptions.getFTypeOfWork() != null ? "&f_type_of_work=" + getWorkOrdersOptions.getFTypeOfWork() : "")
                                    + "" + (getWorkOrdersOptions.getFTimeZone() != null ? "&f_time_zone=" + getWorkOrdersOptions.getFTimeZone() : "")
                                    + "" + (getWorkOrdersOptions.getFMode() != null ? "&f_mode=" + getWorkOrdersOptions.getFMode() : "")
                                    + "" + (getWorkOrdersOptions.getFCompany() != null ? "&f_company=" + getWorkOrdersOptions.getFCompany() : "")
                                    + "" + (getWorkOrdersOptions.getFWorkedWith() != null ? "&f_worked_with=" + getWorkOrdersOptions.getFWorkedWith() : "")
                                    + "" + (getWorkOrdersOptions.getFManager() != null ? "&f_manager=" + getWorkOrdersOptions.getFManager() : "")
                                    + "" + (getWorkOrdersOptions.getFClient() != null ? "&f_client=" + getWorkOrdersOptions.getFClient() : "")
                                    + "" + (getWorkOrdersOptions.getFProject() != null ? "&f_project=" + getWorkOrdersOptions.getFProject() : "")
                                    + "" + (getWorkOrdersOptions.getFApprovalWindow() != null ? "&f_approval_window=" + getWorkOrdersOptions.getFApprovalWindow() : "")
                                    + "" + (getWorkOrdersOptions.getFReviewWindow() != null ? "&f_review_window=" + getWorkOrdersOptions.getFReviewWindow() : "")
                                    + "" + (getWorkOrdersOptions.getFNetwork() != null ? "&f_network=" + getWorkOrdersOptions.getFNetwork() : "")
                                    + "" + (getWorkOrdersOptions.getFAutoAssign() != null ? "&f_auto_assign=" + getWorkOrdersOptions.getFAutoAssign() : "")
                                    + "" + (getWorkOrdersOptions.getFSchedule() != null ? "&f_schedule=" + getWorkOrdersOptions.getFSchedule() : "")
                                    + "" + (getWorkOrdersOptions.getFCreated() != null ? "&f_created=" + getWorkOrdersOptions.getFCreated() : "")
                                    + "" + (getWorkOrdersOptions.getFPublished() != null ? "&f_published=" + getWorkOrdersOptions.getFPublished() : "")
                                    + "" + (getWorkOrdersOptions.getFRouted() != null ? "&f_routed=" + getWorkOrdersOptions.getFRouted() : "")
                                    + "" + (getWorkOrdersOptions.getFPublishedRouted() != null ? "&f_published_routed=" + getWorkOrdersOptions.getFPublishedRouted() : "")
                                    + "" + (getWorkOrdersOptions.getFCompleted() != null ? "&f_completed=" + getWorkOrdersOptions.getFCompleted() : "")
                                    + "" + (getWorkOrdersOptions.getFApprovedCancelled() != null ? "&f_approved_cancelled=" + getWorkOrdersOptions.getFApprovedCancelled() : "")
                                    + "" + (getWorkOrdersOptions.getFConfirmed() != null ? "&f_confirmed=" + getWorkOrdersOptions.getFConfirmed() : "")
                                    + "" + (getWorkOrdersOptions.getFAssigned() != null ? "&f_assigned=" + getWorkOrdersOptions.getFAssigned() : "")
                                    + "" + (getWorkOrdersOptions.getFSavedLocation() != null ? "&f_saved_location=" + getWorkOrdersOptions.getFSavedLocation() : "")
                                    + "" + (getWorkOrdersOptions.getFSavedLocationGroup() != null ? "&f_saved_location_group=" + getWorkOrdersOptions.getFSavedLocationGroup() : "")
                                    + "" + (getWorkOrdersOptions.getFCity() != null ? "&f_city=" + getWorkOrdersOptions.getFCity() : "")
                                    + "" + (getWorkOrdersOptions.getFState() != null ? "&f_state=" + getWorkOrdersOptions.getFState() : "")
                                    + "" + (getWorkOrdersOptions.getFPostalCode() != null ? "&f_postal_code=" + getWorkOrdersOptions.getFPostalCode() : "")
                                    + "" + (getWorkOrdersOptions.getFCountry() != null ? "&f_country=" + getWorkOrdersOptions.getFCountry() : "")
                                    + "" + (getWorkOrdersOptions.getFFlags() != null ? "&f_flags=" + getWorkOrdersOptions.getFFlags() : "")
                                    + "" + (getWorkOrdersOptions.getFAssignment() != null ? "&f_assignment=" + getWorkOrdersOptions.getFAssignment() : "")
                                    + "" + (getWorkOrdersOptions.getFConfirmation() != null ? "&f_confirmation=" + getWorkOrdersOptions.getFConfirmation() : "")
                                    + "" + (getWorkOrdersOptions.getFFinancing() != null ? "&f_financing=" + getWorkOrdersOptions.getFFinancing() : "")
                                    + "" + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                                    + "" + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                                   ));
    }
    /**
     * Verify time log for assigned work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subVerifyTimeLog(Integer workOrderId, Integer workorderHoursId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify"));
    }
    /**
     * Verify time log for assigned work order
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async Return the model in the response (slower) (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subVerifyTimeLog(Integer workOrderId, Integer workorderHoursId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "/verify" + "?async=" + async));
    }
    /**
     * Removes a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId Contact id
     */
    public static void removeContact(Context context, Integer workOrderId, Integer contactId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveContact(Integer workOrderId, Integer contactId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId));
    }
    /**
     * Updates a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId Contact id
     * @param json JSON Model
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateContact(Integer workOrderId, Integer contactId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId));
    }
    /**
     * Get pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId));
    }
    /**
     * Get pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param async Async (Optional)
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetIncrease(Integer workOrderId, Integer increaseId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async));
    }
    /**
     * Delete pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     */
    public static void deleteIncrease(Context context, Integer workOrderId, Integer increaseId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId));
    }
    /**
     * Delete pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteIncrease(Integer workOrderId, Integer increaseId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async));
    }
    /**
     * Update pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param increase Increase structure for update
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId));
    }
    /**
     * Update pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param increase Increase structure for update
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateIncrease(Integer workOrderId, Integer increaseId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "?async=" + async));
    }
    /**
     * Delete an expense from a work order
     *
     * @param workOrderId ID of work order
     * @param expenseId ID of expense
     */
    public static void deleteExpense(Context context, Integer workOrderId, Integer expenseId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteExpense(Integer workOrderId, Integer expenseId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId));
    }
    /**
     * Delete an expense from a work order
     *
     * @param workOrderId ID of work order
     * @param expenseId ID of expense
     * @param async Asynchroneous (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteExpense(Integer workOrderId, Integer expenseId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "?async=" + async));
    }
    /**
     * Update an Expense of a Work order
     *
     * @param workOrderId ID of work order
     * @param expenseId ID of expense
     */
    public static void updateExpense(Context context, Integer workOrderId, Integer expenseId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateExpense(Integer workOrderId, Integer expenseId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId));
    }
    /**
     * Update an Expense of a Work order
     *
     * @param workOrderId ID of work order
     * @param expenseId ID of expense
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "" + (updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
                                   ))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateExpense(Integer workOrderId, Integer expenseId, UpdateExpenseOptions updateExpenseOptions) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/expenses/" + expenseId + "" + (updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
                                   ));
    }
    /**
     * Returns a list of pay increases requested by the assigned provider.
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/increases"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/increases"));
    }
    /**
     * Gets the pay for a work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/pay"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/pay"));
    }
    /**
     * Updates the pay of a work order, or requests an adjustment
     *
     * @param workOrderId ID of work order
     * @param pay Pay
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/pay"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePay(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/pay"));
    }
    /**
     * Updates the pay of a work order, or requests an adjustment
     *
     * @param workOrderId ID of work order
     * @param pay Pay
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/pay" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePay(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/pay" + "?async=" + async));
    }
    /**
     * Adds a task to a work order
     *
     * @param workOrderId Work order id
     * @param json JSON Model
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddTask(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks"));
    }
    /**
     * Get a list of a work order's tasks
     *
     * @param workOrderId Work order id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks"));
    }
    /**
     * Get the milestones of a work order
     *
     * @param workOrderId ID of Work Order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/milestones"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/milestones"));
    }
    /**
     * Add signature by work order
     *
     * @param workOrderId ID of work order
     * @param signature Signature JSON
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddSignature(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures"));
    }
    /**
     * Add signature by work order
     *
     * @param workOrderId ID of work order
     * @param signature Signature JSON
     * @param async async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddSignature(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures" + "?async=" + async));
    }
    /**
     * Returns a list of signatures uploaded by the assigned provider
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures"));
    }
    /**
     * Gets list of providers available for a work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/providers"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/providers"));
    }
    /**
     * Gets list of providers available for a work order
     *
     * @param workOrderId ID of work order
     * @param getProvidersOptions Additional optional parameters
     * @param isBackground indicates that this call is low priority
     */
    public static void getProviders(Context context, String workOrderId, GetProvidersOptions getProvidersOptions, boolean isBackground) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/providers")
                    .urlParams("" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                                    + "" + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                                    + "" + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                                   );

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/providers" + "" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                                    + "" + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                                    + "" + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                                   ))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetProviders(String workOrderId, GetProvidersOptions getProvidersOptions) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/providers" + "" + (getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                                    + "" + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                                    + "" + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                                   ));
    }
    /**
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json JSON payload
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddMessage(String workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages"));
    }
    /**
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json JSON payload
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddMessage(String workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages" + "?async=" + async));
    }
    /**
     * Gets a list of work order messages
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/messages"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/messages"));
    }
    /**
     * Cancel work order swap request.
     *
     */
    public static void cancelSwapRequest(Context context) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCancelSwapRequest() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel"));
    }
    /**
     * Add time log for work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog Check in information
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddTimeLog(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs"));
    }
    /**
     * Returns a list of time logs applied by the assigned provider
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs"));
    }
    /**
     * Update all time logs for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog Check in information
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateAllTimeLogs(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs"));
    }
    /**
     * Update all time logs for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog Check in information
     * @param async Return the model in the response (slower) (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateAllTimeLogs(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs" + "?async=" + async));
    }
    /**
     * Gets a work order by its id
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId));
    }
    /**
     * Deletes a work order by its id
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId));
    }
    /**
     * Deletes a work order by its id
     *
     * @param workOrderId ID of work order
     * @param cancellation Cancellation reasons
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteWorkOrder(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "?async=" + async));
    }
    /**
     * Updates a work order by its id
     *
     * @param workOrderId ID of work order
     * @param workOrder Work order model
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId));
    }
    /**
     * Updates a work order by its id
     *
     * @param workOrderId ID of work order
     * @param workOrder Work order model
     * @param async Asynchroneous (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateWorkOrder(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "?async=" + async));
    }
    /**
     * Gets a single signature uploaded by the assigned provider
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId));
    }
    /**
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteSignature(Integer workOrderId, Integer signatureId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId));
    }
    /**
     * Delete signature by work order and signature
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
     * @param async async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteSignature(Integer workOrderId, Integer signatureId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/signatures/" + signatureId + "?async=" + async));
    }
    /**
     * Adds an attachment folder
     *
     * @param workOrderId Work order id
     * @param folder Folder
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddFolder(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments"));
    }
    /**
     * Adds an attachment folder
     *
     * @param workOrderId Work order id
     * @param folder Folder
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddFolder(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments" + "?async=" + async));
    }
    /**
     * Gets a list of attachment folders which contain files and deliverables for the work order
     *
     * @param workOrderId Work order id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments"));
    }
    /**
     * Completes a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     */
    public static void completeTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/complete");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/complete")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/complete"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subCompleteTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/complete"));
    }
    /**
     * Allows an assigned provider to removes a discount they previously applied from a work order, increasing the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param discountId ID of the discount
     */
    public static void removeDiscount(Context context, Integer workOrderId, Integer discountId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveDiscount(Integer workOrderId, Integer discountId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId));
    }
    /**
     * Updates the amount or description of a discount applied to the work order.
     *
     * @param workOrderId ID of work order
     * @param discountId ID of the discount
     * @param json Payload of the discount
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateDiscount(Integer workOrderId, Integer discountId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId));
    }
    /**
     * Remove time log for assigned work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveTimeLog(Integer workOrderId, Integer workorderHoursId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId));
    }
    /**
     * Remove time log for assigned work order
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async Return the model in the response (slower) (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveTimeLog(Integer workOrderId, Integer workorderHoursId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async));
    }
    /**
     * Update time log for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param timeLog Check in information
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTimeLog(Integer workOrderId, Integer workorderHoursId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId));
    }
    /**
     * Update time log for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param timeLog Check in information
     * @param async Return the model in the response (slower) (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTimeLog(Integer workOrderId, Integer workorderHoursId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async));
    }
    /**
     * Gets an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId));
    }
    /**
     * Deletes an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteAttachment(Integer workOrderId, Integer folderId, Integer attachmentId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId));
    }
    /**
     * Deletes an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachmentId File id
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteAttachment(Integer workOrderId, Integer folderId, Integer attachmentId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async));
    }
    /**
     * Updates an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachmentId File id
     * @param attachment Attachment
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateAttachment(Integer workOrderId, Integer folderId, Integer attachmentId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId));
    }
    /**
     * Updates an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachmentId File id
     * @param attachment Attachment
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateAttachment(Integer workOrderId, Integer folderId, Integer attachmentId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId + "?async=" + async));
    }
    /**
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee JSON Model
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAssignUser(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee"));
    }
    /**
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee JSON Model
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAssignUser(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee" + "?async=" + async));
    }
    /**
     * Get assignee of a work order
     *
     * @param workOrderId Work order id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee"));
    }
    /**
     * Unassign user from a work order
     *
     * @param workOrderId Work order id
     * @param assignee JSON Model
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnassignUser(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee"));
    }
    /**
     * Unassign user from a work order
     *
     * @param workOrderId Work order id
     * @param assignee JSON Model
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnassignUser(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/assignee" + "?async=" + async));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/publish"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subPublish(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/publish"));
    }
    /**
     * Publishes a work order to the marketplace where it can garner requests.
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/publish" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subPublish(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/publish" + "?async=" + async));
    }
    /**
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/publish"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnpublish(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/publish"));
    }
    /**
     * Unpublishes a work order from the marketplace so that no requests or counter-offers can be made. Moves to draft unless it was also routed.
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/publish" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnpublish(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/publish" + "?async=" + async));
    }
    /**
     * Gets the current real-time status for a work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/status"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/status"));
    }
    /**
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/approve"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subApproveWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/approve"));
    }
    /**
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/approve" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subApproveWorkOrder(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/approve" + "?async=" + async));
    }
    /**
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/approve"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnapproveWorkOrder(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/approve"));
    }
    /**
     * Unapproves a completed work order and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/approve" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnapproveWorkOrder(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/approve" + "?async=" + async));
    }
    /**
     * Accept pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     */
    public static void acceptIncrease(Context context, Integer workOrderId, Integer increaseId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/accept")
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAcceptIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/accept"));
    }
    /**
     * Deletes a shipment from a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId Shipment id
     */
    public static void deleteShipment(Context context, Integer workOrderId, Integer shipmentId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteShipment(Integer workOrderId, Integer shipmentId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId));
    }
    /**
     * Deletes a shipment from a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId Shipment id
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeleteShipment(Integer workOrderId, Integer shipmentId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async));
    }
    /**
     * Updates a shipment attached to a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId Shipment id
     * @param shipment Shipment
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateShipment(Integer workOrderId, Integer shipmentId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId));
    }
    /**
     * Updates a shipment attached to a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId Shipment id
     * @param shipment Shipment
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateShipment(Integer workOrderId, Integer shipmentId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments/" + shipmentId + "?async=" + async));
    }
    /**
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     */
    public static void addPenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId));
    }
    /**
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     * @param penalty Penalty (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddPenalty(Integer workOrderId, Integer penaltyId, PayModifier penalty) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId));
    }
    /**
     * Gets a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId));
    }
    /**
     * Removes a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId ID of Work Order
     * @param penaltyId Penalty ID
     */
    public static void removePenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemovePenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId));
    }
    /**
     * Updates a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     */
    public static void updatePenalty(Context context, Integer workOrderId, Integer penaltyId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePenalty(Integer workOrderId, Integer penaltyId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId));
    }
    /**
     * Updates a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     * @param penalty Penalty (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdatePenalty(Integer workOrderId, Integer penaltyId, PayModifier penalty) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId));
    }
    /**
     * Decline work order swap request.
     *
     */
    public static void declineSwapRequest(Context context) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDeclineSwapRequest() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline"));
    }
    /**
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     * @param json JSON payload
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReplyMessage(String workOrderId, String messageId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId));
    }
    /**
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     * @param json JSON payload
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReplyMessage(String workOrderId, String messageId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId + "?async=" + async));
    }
    /**
     * Removes a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     */
    public static void removeMessage(Context context, String workOrderId, String messageId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveMessage(String workOrderId, String messageId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId));
    }
    /**
     * Updates a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     * @param json JSON payload
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateMessage(String workOrderId, String messageId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId));
    }
    /**
     * Get a task by work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId));
    }
    /**
     * Remove a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     */
    public static void removeTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId));
    }
    /**
     * Updates a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param json JSON Model
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId));
    }
    /**
     * Deny pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     */
    public static void denyIncrease(Context context, Integer workOrderId, Integer increaseId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/deny")
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subDenyIncrease(Integer workOrderId, Integer increaseId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/increases/" + increaseId + "/deny"));
    }
    /**
     * Sets up an alert to be fired upon the completion of a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param json JSON Model
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddAlertToWorkOrderAndTask(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts"));
    }
    /**
     * Removes all alerts associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     */
    public static void removeAlerts(Context context, Integer workOrderId, Integer taskId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveAlerts(Integer workOrderId, Integer taskId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts"));
    }
    /**
     * Request or un-hide a request for a work order
     *
     * @param workOrderId Work order id
     * @param request JSON Model
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/requests"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRequest(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/requests"));
    }
    /**
     * Request or un-hide a request for a work order
     *
     * @param workOrderId Work order id
     * @param request JSON Model
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/requests" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRequest(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/requests" + "?async=" + async));
    }
    /**
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param request JSON Model
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/requests"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveRequest(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/requests"));
    }
    /**
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param request JSON Model
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/requests" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveRequest(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/requests" + "?async=" + async));
    }
    /**
     * Accept work order swap request.
     *
     */
    public static void acceptSwapRequest(Context context) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAcceptSwapRequest() {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept"));
    }
    /**
     * Get a list of custom fields and their values for a work order.
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/custom_fields"));
    }
    /**
     * Removes a single alert associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param alertId Alert id
     */
    public static void removeAlert(Context context, Integer workOrderId, Integer taskId, Integer alertId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts/{alert_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveAlert(Integer workOrderId, Integer taskId, Integer alertId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId));
    }
    /**
     * Gets the service schedule for a work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/schedule"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/schedule"));
    }
    /**
     * Updates the service schedule or eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param schedule JSON Payload
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/schedule"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateSchedule(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/schedule"));
    }
    /**
     * Updates the service schedule or eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param schedule JSON Payload
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/schedule" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateSchedule(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/schedule" + "?async=" + async));
    }
    /**
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds Holds
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/holds"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateHolds(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/holds"));
    }
    /**
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds Holds
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/holds" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateHolds(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/holds" + "?async=" + async));
    }
    /**
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId ID of report problem flag
     * @param json JSON payload
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subResolveReopenReportProblem(Integer workOrderId, Integer flagId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId));
    }
    /**
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId ID of report problem flag
     * @param json JSON payload
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subResolveReopenReportProblem(Integer workOrderId, Integer flagId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId + "?async=" + async));
    }
    /**
     * Assigned provider route to adds and apply a discount to a work order which reduces the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param json Payload of the discount
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddDiscount(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts"));
    }
    /**
     * Returns a list of discounts applied by the assigned provider to reduce the payout of the work order.
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/discounts"));
    }
    /**
     * Reorders a task associated with a work order to a position before or after a target task
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param targetTaskId Target task id
     * @param position before or after target task
     */
    public static void reorderTask(Context context, Integer workOrderId, Integer taskId, Integer targetTaskId, String position) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/reorder/{position}/{target_task_id}")
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReorderTask(Integer workOrderId, Integer taskId, Integer targetTaskId, String position) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/reorder/" + position + "/" + targetTaskId));
    }
    /**
     * Regroups a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param group New group
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGroupTask(Integer workOrderId, Integer taskId, String group, String destination) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/group/" + group + "/" + destination));
    }
    /**
     * Adds a contact to a work order
     *
     * @param workOrderId Work order id
     * @param json JSON Model
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddContact(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts"));
    }
    /**
     * Get a list of contacts on a work order
     *
     * @param workOrderId Work order id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/contacts"));
    }
    /**
     * Adds a shipment to a work order
     *
     * @param workOrderId Work order id
     * @param shipment Shipment
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddShipment(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments"));
    }
    /**
     * Adds a shipment to a work order
     *
     * @param workOrderId Work order id
     * @param shipment Shipment
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddShipment(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments" + "?async=" + async));
    }
    /**
     * Get a list of shipments on a work order
     *
     * @param workOrderId Work order id
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/shipments"));
    }
    /**
     * Get a list of penalties and their applied status for a work order
     *
     * @param workOrderId Work Order ID
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/penalties"));
    }
    /**
     * Route a user to a work order
     *
     * @param workOrderId Work order id
     * @param route JSON Model
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/route"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRouteUser(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/route"));
    }
    /**
     * Route a user to a work order
     *
     * @param workOrderId Work order id
     * @param route JSON Model
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/route" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRouteUser(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/route" + "?async=" + async));
    }
    /**
     * Unroute a user from a work order
     *
     * @param workOrderId Work order id
     * @param route JSON Model
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/route"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnRouteUser(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/route"));
    }
    /**
     * Unroute a user from a work order
     *
     * @param workOrderId Work order id
     * @param route JSON Model
     * @param async Async (Optional)
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
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/route" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUnRouteUser(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/route" + "?async=" + async));
    }
    /**
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/lists"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/lists"));
    }
    /**
     * Gets list of problem reasons by work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/problems/messages"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/problems/messages"));
    }
    /**
     * Report a problem to a work order
     *
     * @param workOrderId ID of work order
     * @param json JSON payload
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/messages"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReportProblem(String workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/messages"));
    }
    /**
     * Report a problem to a work order
     *
     * @param workOrderId ID of work order
     * @param json JSON payload
     * @param async Async (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/messages" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subReportProblem(String workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/report-problem/messages" + "?async=" + async));
    }
    /**
     * Get a list of available bonuses on a work order that can be applied to increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId Work Order ID
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses"));
    }
    /**
     * Gets the address and geo information for a work order
     *
     * @param workOrderId ID of work order
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/location"))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/location"));
    }
    /**
     * Updates the location of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param location JSON Payload
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/location"))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateLocation(Integer workOrderId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/location"));
    }
    /**
     * Updates the location of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param location JSON Payload
     * @param async Async (Optional)
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/location" + "?async=" + async))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateLocation(Integer workOrderId, Boolean async) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/location" + "?async=" + async));
    }
    /**
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     */
    public static void addBonus(Context context, Integer workOrderId, Integer bonusId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId));
    }
    /**
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     * @param bonus Bonus (Optional)
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
                    .key(misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subAddBonus(Integer workOrderId, Integer bonusId, PayModifier bonus) {
        return register("TOPIC_ID_API_V2/" + misc.md5("POST/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId));
    }
    /**
     * Gets a bonus for a work order
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
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
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId));
    }
    /**
     * Gets a bonus for a work order
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     * @param bonus Bonus (Optional)
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
                    .key(misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subGetBonus(Integer workOrderId, Integer bonusId, PayModifier bonus) {
        return register("TOPIC_ID_API_V2/" + misc.md5("GET/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId));
    }
    /**
     * Removes a bonus from a work order
     *
     * @param workOrderId Work Order ID
     * @param bonusId Bonus ID
     */
    public static void removeBonus(Context context, Integer workOrderId, Integer bonusId) {
        try {
            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subRemoveBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("DELETE/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId));
    }
    /**
     * Updates a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     * @param bonus Bonus
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
                    .key(misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId))
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    public boolean subUpdateBonus(Integer workOrderId, Integer bonusId) {
        return register("TOPIC_ID_API_V2/" + misc.md5("PUT/" + "/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId));
    }
}

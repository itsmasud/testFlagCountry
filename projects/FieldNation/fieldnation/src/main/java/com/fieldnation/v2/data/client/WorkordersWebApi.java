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
import com.fieldnation.v2.data.model.IdResponse;
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
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.Schedule;
import com.fieldnation.v2.data.model.Shipment;
import com.fieldnation.v2.data.model.Shipments;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Status;
import com.fieldnation.v2.data.model.SwapResponse;
import com.fieldnation.v2.data.model.Task;
import com.fieldnation.v2.data.model.TaskAlert;
import com.fieldnation.v2.data.model.Tasks;
import com.fieldnation.v2.data.model.TimeLog;
import com.fieldnation.v2.data.model.TimeLogs;
import com.fieldnation.v2.data.model.Users;
import com.fieldnation.v2.data.model.WorkOrder;
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "acceptIncrease"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "acceptSwapRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addAlertToWorkOrderAndTask"))
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
                    .multipartField("attachment", attachment, "application/json")
                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/attachments/" + folderId)
                    .urlParams("?async=" + async)
                    .multipartField("attachment", attachment)
                    .multipartFile("file", file.getName(), Uri.fromFile(file));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: addContactByWorkOrder
     * Adds a contact to a work order
     *
     * @param workOrderId Work order id
     * @param json        JSON Model
     */
    public static void addContact(Context context, Integer workOrderId, Contact json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/contacts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts");

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/contacts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addContact"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/discounts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addDiscount"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addIncrease"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addIncrease"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/problems")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addProblem"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/problems")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addProblem"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/tasks")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addTask"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addTimeLog"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "addWorkOrder"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/cancel")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "cancelSwapRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/declines")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "decline"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/declines")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "decline"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/declines/{user_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "declineRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/declines/{user_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "declineRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/swaps/{swap_request_id}/decline")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "declineSwapRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}/deny")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "denyIncrease"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getAssignee"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getAttachments"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getBonus"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getBonus"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/bonuses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getBonuses"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/contacts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getContacts"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getCustomField"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/custom_fields")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getCustomFields"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/discounts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getDiscounts"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/eta")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getETA"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/expenses")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getExpenses"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getFile"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getFolder"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getHolds"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getIncrease"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getIncrease"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/increases")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getIncreases"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/location")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getLocation"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/messages")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getMessages"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/milestones")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getMilestones"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getPay"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/penalties")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getPenalties"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getPenalty"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProblem"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/problems")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProblems"))
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
    public static void getProviders(Context context, String workOrderId, boolean allowCacheResponse, boolean isBackground) {
        try {
            String key = misc.md5("GET//api/rest/v2/workorders/" + workOrderId + "/providers");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/providers");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProviders"))
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
    public static void getProviders(Context context, String workOrderId, GetProvidersOptions getProvidersOptions, boolean allowCacheResponse, boolean isBackground) {
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getProviders"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getRequest"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getRequests"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/mass-accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "GetScheduleAndLocation"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getSchedule"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/shipments")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getShipments"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/signatures/{signature_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getSignature"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/signatures")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getSignatures"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/status")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getStatus"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTask"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/tasks")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTasks"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getTimeLogs"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrder"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/lists")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrderLists"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrders"))
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
            String key = misc.md5("GET//api/rest/v2/workorders" + (getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
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
                    + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                    + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
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
                            + (getWorkOrdersOptions.getFGeo() != null ? "&f_geo=" + getWorkOrdersOptions.getFGeo() : "")
                            + (getWorkOrdersOptions.getFSearch() != null ? "&f_search=" + getWorkOrdersOptions.getFSearch() : "")
                    );

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "getWorkOrders"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/group/{group}/{destination}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "groupTask"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/complete")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/mass-accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "MassAcceptWorkOrder"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/mass-accept")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "MassAcceptWorkOrder"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: removeAlertByWorkOrderAndTask
     * Removes a single alert associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param alertId     Alert id
     */
    public static void removeAlert(Context context, Integer workOrderId, Integer taskId, Integer alertId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts/{alert_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeAlert"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeAlerts"))
                    .useAuth(true)
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/bonuses/" + bonusId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeBonus"))
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeContact"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/discounts/" + discountId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeDiscount"))
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeMessage"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/penalties/" + penaltyId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removePenalty"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeProblemByWorkOrder
     * Removes a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problemId   ID of work order
     */
    public static void removeProblem(Context context, Integer workOrderId, Integer problemId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeProblem"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: removeProblemByWorkOrder
     * Removes a problem on a work order
     *
     * @param workOrderId ID of work order
     * @param problemId   ID of work order
     * @param async       Async (Optional)
     */
    public static void removeProblem(Context context, Integer workOrderId, Integer problemId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/problems/" + problemId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeProblem"))
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
     * @param requestId   JSON Model
     */
    public static void removeRequest(Context context, Integer workOrderId, Integer requestId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/requests/{request_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: removeRequestByWorkOrder
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param requestId   JSON Model
     * @param async       Async (Optional)
     */
    public static void removeRequest(Context context, Integer workOrderId, Integer requestId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/requests/" + requestId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/requests/[request_id]")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: removeTaskByWorkOrder
     * Remove a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void removeTask(Context context, Integer workOrderId, Integer taskId) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/tasks/" + taskId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "removeTask"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: removeTimeLogByWorkOrder
     * Remove time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async            Return the model in the response (slower) (Optional)
     */
    public static void removeTimeLog(Context context, Integer workOrderId, Integer workorderHoursId, Boolean async) {
        try {
            String key = misc.md5("DELETE//api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("DELETE")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                    .urlParams("?async=" + async);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}/reorder/{position}/{target_task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "reorderTask"))
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
    public static void replyMessage(Context context, String workOrderId, String messageId, Message json) {
        try {
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
            String key = misc.md5("POST//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("POST")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId)
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/requests")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: resolveReopenReportProblemByWorkOrder
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId      ID of report problem flag
     * @param json        JSON payload
     */
    public static void resolveReopenReportProblem(Context context, Integer workOrderId, Integer flagId, Message json) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId);

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/report-problem/{flag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId + "?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/report-problem/" + flagId)
                    .urlParams("?async=" + async);

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/report-problem/{flag_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/draft")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/draft")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("POST//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/approve")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/assignee")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/publish")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//api/rest/v2/workorders/{work_order_id}/route")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/bonuses/{bonus_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateBonus"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
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
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/contacts/" + contactId);

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/contacts/{contact_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateContact"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/discounts/{discount_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateDiscount"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/eta")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateETA"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/eta")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateETA"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/attachments/{folder_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: updateHoldByWorkOrder
     * Updates a single hold on a work order
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateHold"))
                    .useAuth(true)
                    .request(builder)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(STAG, ex);
        }
    }

    /**
     * Swagger operationId: updateHoldByWorkOrder
     * Updates a single hold on a work order
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds/{hold_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateHold"))
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
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/holds");

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds");

            if (holds != null)
                builder.body(holds);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: updateHoldsByWorkOrder
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds       Holds
     * @param async       Async (Optional)
     */
    public static void updateHolds(Context context, Integer workOrderId, String holds, Boolean async) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/holds?async=" + async);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/holds")
                    .urlParams("?async=" + async);

            if (holds != null)
                builder.body(holds);

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/holds")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/increases/{increase_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/location")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/location")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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
     * Swagger operationId: updateMessageByWorkOrder
     * Updates a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void updateMessage(Context context, String workOrderId, String messageId, Message json) {
        try {
            String key = misc.md5("PUT//api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("PUT")
                    .path("/api/rest/v2/workorders/" + workOrderId + "/messages/" + messageId);

            if (json != null)
                builder.body(json.getJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/messages/{message_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateMessage"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/pay")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/penalties/{penalty_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateProblem"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/problems/{problem_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateProblem"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/schedule")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/shipments/{shipment_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/tasks/{task_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "updateTask"))
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}/verify")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
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

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/time_logs/{workorder_hours_id}/verify")
                    .key(key)
                    .priority(Priority.HIGH)
                    .listener(TransactionListener.class)
                    .listenerParams(
                            TransactionListener.params("TOPIC_ID_WEB_API_V2/WorkordersWebApi",
                                    WorkordersWebApi.class, "verifyTimeLog"))
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
            new AsyncParser(this, (Bundle) payload);
        }

        public void onWorkordersWebApi(String methodName, Object successObject, boolean success, Object failObject) {
        }

        public void onAcceptIncrease(boolean success, Error error) {
        }

        public void onAcceptSwapRequest(SwapResponse swapResponse, boolean success, Error error) {
        }

        public void onAddAlertToWorkOrderAndTask(boolean success, Error error) {
        }

        public void onAddAttachment(Attachment attachment, boolean success, Error error) {
        }

        public void onAddBonus(boolean success, Error error) {
        }

        public void onAddContact(IdResponse idResponse, boolean success, Error error) {
        }

        public void onAddDiscount(IdResponse idResponse, boolean success, Error error) {
        }

        public void onAddExpense(Expense expense, boolean success, Error error) {
        }

        public void onAddFolder(boolean success, Error error) {
        }

        public void onAddIncrease(PayIncrease payIncrease, boolean success, Error error) {
        }

        public void onAddMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onAddPenalty(boolean success, Error error) {
        }

        public void onAddProblem(boolean success, Error error) {
        }

        public void onAddShipment(IdResponse idResponse, boolean success, Error error) {
        }

        public void onAddSignature(Signature signature, boolean success, Error error) {
        }

        public void onAddTask(IdResponse idResponse, boolean success, Error error) {
        }

        public void onAddTimeLog(TimeLog timeLog, boolean success, Error error) {
        }

        public void onAddWorkOrder(boolean success, Error error) {
        }

        public void onApproveWorkOrder(boolean success, Error error) {
        }

        public void onAssignUser(boolean success, Error error) {
        }

        public void onCancelSwapRequest(SwapResponse swapResponse, boolean success, Error error) {
        }

        public void onCompleteWorkOrder(boolean success, Error error) {
        }

        public void onDecline(boolean success, Error error) {
        }

        public void onDeclineRequest(boolean success, Error error) {
        }

        public void onDeclineSwapRequest(SwapResponse swapResponse, boolean success, Error error) {
        }

        public void onDeleteAttachment(boolean success, Error error) {
        }

        public void onDeleteExpense(boolean success, Error error) {
        }

        public void onDeleteFolder(boolean success, Error error) {
        }

        public void onDeleteIncrease(boolean success, Error error) {
        }

        public void onDeleteShipment(boolean success, Error error) {
        }

        public void onDeleteSignature(boolean success, Error error) {
        }

        public void onDeleteWorkOrder(boolean success, Error error) {
        }

        public void onDenyIncrease(boolean success, Error error) {
        }

        public void onGetAssignee(Assignee assignee, boolean success, Error error) {
        }

        public void onGetAttachments(AttachmentFolders attachmentFolders, boolean success, Error error) {
        }

        public void onGetBonus(PayModifier payModifier, boolean success, Error error) {
        }

        public void onGetBonuses(PayModifiers payModifiers, boolean success, Error error) {
        }

        public void onGetContacts(Contacts contacts, boolean success, Error error) {
        }

        public void onGetCustomField(CustomField customField, boolean success, Error error) {
        }

        public void onGetCustomFields(CustomFields customFields, boolean success, Error error) {
        }

        public void onGetDiscounts(PayModifiers payModifiers, boolean success, Error error) {
        }

        public void onGetETA(ETA eTA, boolean success, Error error) {
        }

        public void onGetExpenses(Expenses expenses, boolean success, Error error) {
        }

        public void onGetFile(Attachment attachment, boolean success, Error error) {
        }

        public void onGetFolder(AttachmentFolder attachmentFolder, boolean success, Error error) {
        }

        public void onGetHolds(Holds holds, boolean success, Error error) {
        }

        public void onGetIncrease(PayIncrease payIncrease, boolean success, Error error) {
        }

        public void onGetIncreases(PayIncreases payIncreases, boolean success, Error error) {
        }

        public void onGetLocation(Location location, boolean success, Error error) {
        }

        public void onGetMessages(Messages messages, boolean success, Error error) {
        }

        public void onGetMilestones(Milestones milestones, boolean success, Error error) {
        }

        public void onGetPay(Pay pay, boolean success, Error error) {
        }

        public void onGetPenalties(PayModifiers payModifiers, boolean success, Error error) {
        }

        public void onGetPenalty(PayModifier payModifier, boolean success, Error error) {
        }

        public void onGetProblem(Problems problems, boolean success, Error error) {
        }

        public void onGetProblems(Problems problems, boolean success, Error error) {
        }

        public void onGetProviders(Users[] users, boolean success, Error error) {
        }

        public void onGetRequest(Request request, boolean success, Error error) {
        }

        public void onGetRequests(Requests requests, boolean success, Error error) {
        }

        public void onGetScheduleAndLocation(EtaMassAcceptWithLocation etaMassAcceptWithLocation, boolean success, Error error) {
        }

        public void onGetSchedule(Schedule schedule, boolean success, Error error) {
        }

        public void onGetShipments(Shipments shipments, boolean success, Error error) {
        }

        public void onGetSignature(Signature signature, boolean success, Error error) {
        }

        public void onGetSignatures(Signature[] signatures, boolean success, Error error) {
        }

        public void onGetStatus(Status status, boolean success, Error error) {
        }

        public void onGetTask(Task task, boolean success, Error error) {
        }

        public void onGetTasks(Tasks tasks, boolean success, Error error) {
        }

        public void onGetTimeLogs(TimeLogs timeLogs, boolean success, Error error) {
        }

        public void onGetWorkOrder(WorkOrder workOrder, boolean success, Error error) {
        }

        public void onGetWorkOrderLists(SavedList[] savedList, boolean success, Error error) {
        }

        public void onGetWorkOrders(WorkOrders workOrders, boolean success, Error error) {
        }

        public void onGroupTask(boolean success, Error error) {
        }

        public void onIncompleteWorkOrder(boolean success, Error error) {
        }

        public void onMassAcceptWorkOrder(boolean success, Error error) {
        }

        public void onPublish(boolean success, Error error) {
        }

        public void onRemoveAlert(boolean success, Error error) {
        }

        public void onRemoveAlerts(boolean success, Error error) {
        }

        public void onRemoveBonus(boolean success, Error error) {
        }

        public void onRemoveContact(boolean success, Error error) {
        }

        public void onRemoveDiscount(boolean success, Error error) {
        }

        public void onRemoveMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onRemovePenalty(boolean success, Error error) {
        }

        public void onRemoveProblem(boolean success, Error error) {
        }

        public void onRemoveRequest(boolean success, Error error) {
        }

        public void onRemoveTask(boolean success, Error error) {
        }

        public void onRemoveTimeLog(boolean success, Error error) {
        }

        public void onReorderTask(boolean success, Error error) {
        }

        public void onReplyMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onRequest(boolean success, Error error) {
        }

        public void onResolveReopenReportProblem(boolean success, Error error) {
        }

        public void onRevertWorkOrderToDraft(boolean success, Error error) {
        }

        public void onRouteUser(boolean success, Error error) {
        }

        public void onUnapproveWorkOrder(boolean success, Error error) {
        }

        public void onUnassignUser(boolean success, Error error) {
        }

        public void onUnpublish(boolean success, Error error) {
        }

        public void onUnRouteUser(boolean success, Error error) {
        }

        public void onUpdateAllTimeLogs(boolean success, Error error) {
        }

        public void onUpdateAttachment(boolean success, Error error) {
        }

        public void onUpdateBonus(boolean success, Error error) {
        }

        public void onUpdateContact(boolean success, Error error) {
        }

        public void onUpdateCustomField(CustomField customField, boolean success, Error error) {
        }

        public void onUpdateDiscount(IdResponse idResponse, boolean success, Error error) {
        }

        public void onUpdateETA(boolean success, Error error) {
        }

        public void onUpdateExpense(Expense expense, boolean success, Error error) {
        }

        public void onUpdateFolder(boolean success, Error error) {
        }

        public void onUpdateHold(WorkOrder workOrder, boolean success, Error error) {
        }

        public void onUpdateHolds(boolean success, Error error) {
        }

        public void onUpdateIncrease(PayIncrease payIncrease, boolean success, Error error) {
        }

        public void onUpdateLocation(boolean success, Error error) {
        }

        public void onUpdateMessage(IdResponse idResponse, boolean success, Error error) {
        }

        public void onUpdatePay(boolean success, Error error) {
        }

        public void onUpdatePenalty(boolean success, Error error) {
        }

        public void onUpdateProblem(boolean success, Error error) {
        }

        public void onUpdateSchedule(boolean success, Error error) {
        }

        public void onUpdateShipment(boolean success, Error error) {
        }

        public void onUpdateTask(boolean success, Error error) {
        }

        public void onUpdateTimeLog(boolean success, Error error) {
        }

        public void onUpdateWorkOrder(boolean success, Error error) {
        }

        public void onVerifyTimeLog(boolean success, Error error) {
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
                switch (transactionParams.apiFunction) {
                    case "acceptIncrease":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "acceptSwapRequest":
                        if (success)
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addAlertToWorkOrderAndTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addAttachment":
                        if (success)
                            successObject = Attachment.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addBonus":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addContact":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addDiscount":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addExpense":
                        if (success)
                            successObject = Expense.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addFolder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addIncrease":
                        if (success)
                            successObject = PayIncrease.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addPenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addProblem":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addShipment":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addSignature":
                        if (success)
                            successObject = Signature.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addTask":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addTimeLog":
                        if (success)
                            successObject = TimeLog.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "addWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "approveWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "assignUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "cancelSwapRequest":
                        if (success)
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "completeWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "decline":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "declineRequest":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "declineSwapRequest":
                        if (success)
                            successObject = SwapResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteAttachment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteExpense":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteFolder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteIncrease":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteShipment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteSignature":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "deleteWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "denyIncrease":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getAssignee":
                        if (success)
                            successObject = Assignee.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getAttachments":
                        if (success)
                            successObject = AttachmentFolders.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getBonus":
                        if (success)
                            successObject = PayModifier.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getBonuses":
                        if (success)
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getContacts":
                        if (success)
                            successObject = Contacts.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getCustomField":
                        if (success)
                            successObject = CustomField.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getCustomFields":
                        if (success)
                            successObject = CustomFields.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getDiscounts":
                        if (success)
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getETA":
                        if (success)
                            successObject = ETA.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getExpenses":
                        if (success)
                            successObject = Expenses.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getFile":
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
                    case "getHolds":
                        if (success)
                            successObject = Holds.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getIncrease":
                        if (success)
                            successObject = PayIncrease.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getIncreases":
                        if (success)
                            successObject = PayIncreases.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getLocation":
                        if (success)
                            successObject = Location.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getMessages":
                        if (success)
                            successObject = Messages.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getMilestones":
                        if (success)
                            successObject = Milestones.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPay":
                        if (success)
                            successObject = Pay.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPenalties":
                        if (success)
                            successObject = PayModifiers.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getPenalty":
                        if (success)
                            successObject = PayModifier.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getProblem":
                        if (success)
                            successObject = Problems.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getProblems":
                        if (success)
                            successObject = Problems.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getProviders":
                        if (success)
                            successObject = Users.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getRequest":
                        if (success)
                            successObject = Request.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getRequests":
                        if (success)
                            successObject = Requests.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "GetScheduleAndLocation":
                        if (success)
                            successObject = EtaMassAcceptWithLocation.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getSchedule":
                        if (success)
                            successObject = Schedule.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getShipments":
                        if (success)
                            successObject = Shipments.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getSignature":
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
                    case "getStatus":
                        if (success)
                            successObject = com.fieldnation.v2.data.model.Status.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTask":
                        if (success)
                            successObject = Task.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTasks":
                        if (success)
                            successObject = Tasks.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getTimeLogs":
                        if (success)
                            successObject = TimeLogs.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getWorkOrder":
                        if (success)
                            successObject = WorkOrder.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getWorkOrderLists":
                        if (success)
                            successObject = SavedList.fromJsonArray(new JsonArray(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "getWorkOrders":
                        if (success)
                            successObject = WorkOrders.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "groupTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "incompleteWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "MassAcceptWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "publish":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeAlert":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeAlerts":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeBonus":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeContact":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeDiscount":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removePenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeProblem":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeRequest":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "removeTimeLog":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "reorderTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "replyMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "request":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "resolveReopenReportProblem":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "revertWorkOrderToDraft":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "routeUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unapproveWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unassignUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unpublish":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "unRouteUser":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateAllTimeLogs":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateAttachment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateBonus":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateContact":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateCustomField":
                        if (success)
                            successObject = CustomField.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateDiscount":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateETA":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateExpense":
                        if (success)
                            successObject = Expense.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateFolder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateHold":
                        if (success)
                            successObject = WorkOrder.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateHolds":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateIncrease":
                        if (success)
                            successObject = PayIncrease.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateLocation":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateMessage":
                        if (success)
                            successObject = IdResponse.fromJson(new JsonObject(data));
                        else
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updatePay":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updatePenalty":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateProblem":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateSchedule":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateShipment":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateTask":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateTimeLog":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "updateWorkOrder":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    case "verifyTimeLog":
                        if (!success)
                            failObject = Error.fromJson(new JsonObject(data));
                        break;
                    default:
                        Log.v(TAG, "Don't know how to handle " + transactionParams.apiFunction);
                        break;
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
                listener.onWorkordersWebApi(transactionParams.apiFunction, successObject, success, failObject);
                switch (transactionParams.apiFunction) {
                    case "acceptIncrease":
                        listener.onAcceptIncrease(success, (Error) failObject);
                        break;
                    case "acceptSwapRequest":
                        listener.onAcceptSwapRequest((SwapResponse) successObject, success, (Error) failObject);
                        break;
                    case "addAlertToWorkOrderAndTask":
                        listener.onAddAlertToWorkOrderAndTask(success, (Error) failObject);
                        break;
                    case "addAttachment":
                        listener.onAddAttachment((Attachment) successObject, success, (Error) failObject);
                        break;
                    case "addBonus":
                        listener.onAddBonus(success, (Error) failObject);
                        break;
                    case "addContact":
                        listener.onAddContact((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "addDiscount":
                        listener.onAddDiscount((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "addExpense":
                        listener.onAddExpense((Expense) successObject, success, (Error) failObject);
                        break;
                    case "addFolder":
                        listener.onAddFolder(success, (Error) failObject);
                        break;
                    case "addIncrease":
                        listener.onAddIncrease((PayIncrease) successObject, success, (Error) failObject);
                        break;
                    case "addMessage":
                        listener.onAddMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "addPenalty":
                        listener.onAddPenalty(success, (Error) failObject);
                        break;
                    case "addProblem":
                        listener.onAddProblem(success, (Error) failObject);
                        break;
                    case "addShipment":
                        listener.onAddShipment((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "addSignature":
                        listener.onAddSignature((Signature) successObject, success, (Error) failObject);
                        break;
                    case "addTask":
                        listener.onAddTask((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "addTimeLog":
                        listener.onAddTimeLog((TimeLog) successObject, success, (Error) failObject);
                        break;
                    case "addWorkOrder":
                        listener.onAddWorkOrder(success, (Error) failObject);
                        break;
                    case "approveWorkOrder":
                        listener.onApproveWorkOrder(success, (Error) failObject);
                        break;
                    case "assignUser":
                        listener.onAssignUser(success, (Error) failObject);
                        break;
                    case "cancelSwapRequest":
                        listener.onCancelSwapRequest((SwapResponse) successObject, success, (Error) failObject);
                        break;
                    case "completeWorkOrder":
                        listener.onCompleteWorkOrder(success, (Error) failObject);
                        break;
                    case "decline":
                        listener.onDecline(success, (Error) failObject);
                        break;
                    case "declineRequest":
                        listener.onDeclineRequest(success, (Error) failObject);
                        break;
                    case "declineSwapRequest":
                        listener.onDeclineSwapRequest((SwapResponse) successObject, success, (Error) failObject);
                        break;
                    case "deleteAttachment":
                        listener.onDeleteAttachment(success, (Error) failObject);
                        break;
                    case "deleteExpense":
                        listener.onDeleteExpense(success, (Error) failObject);
                        break;
                    case "deleteFolder":
                        listener.onDeleteFolder(success, (Error) failObject);
                        break;
                    case "deleteIncrease":
                        listener.onDeleteIncrease(success, (Error) failObject);
                        break;
                    case "deleteShipment":
                        listener.onDeleteShipment(success, (Error) failObject);
                        break;
                    case "deleteSignature":
                        listener.onDeleteSignature(success, (Error) failObject);
                        break;
                    case "deleteWorkOrder":
                        listener.onDeleteWorkOrder(success, (Error) failObject);
                        break;
                    case "denyIncrease":
                        listener.onDenyIncrease(success, (Error) failObject);
                        break;
                    case "getAssignee":
                        listener.onGetAssignee((Assignee) successObject, success, (Error) failObject);
                        break;
                    case "getAttachments":
                        listener.onGetAttachments((AttachmentFolders) successObject, success, (Error) failObject);
                        break;
                    case "getBonus":
                        listener.onGetBonus((PayModifier) successObject, success, (Error) failObject);
                        break;
                    case "getBonuses":
                        listener.onGetBonuses((PayModifiers) successObject, success, (Error) failObject);
                        break;
                    case "getContacts":
                        listener.onGetContacts((Contacts) successObject, success, (Error) failObject);
                        break;
                    case "getCustomField":
                        listener.onGetCustomField((CustomField) successObject, success, (Error) failObject);
                        break;
                    case "getCustomFields":
                        listener.onGetCustomFields((CustomFields) successObject, success, (Error) failObject);
                        break;
                    case "getDiscounts":
                        listener.onGetDiscounts((PayModifiers) successObject, success, (Error) failObject);
                        break;
                    case "getETA":
                        listener.onGetETA((ETA) successObject, success, (Error) failObject);
                        break;
                    case "getExpenses":
                        listener.onGetExpenses((Expenses) successObject, success, (Error) failObject);
                        break;
                    case "getFile":
                        listener.onGetFile((Attachment) successObject, success, (Error) failObject);
                        break;
                    case "getFolder":
                        listener.onGetFolder((AttachmentFolder) successObject, success, (Error) failObject);
                        break;
                    case "getHolds":
                        listener.onGetHolds((Holds) successObject, success, (Error) failObject);
                        break;
                    case "getIncrease":
                        listener.onGetIncrease((PayIncrease) successObject, success, (Error) failObject);
                        break;
                    case "getIncreases":
                        listener.onGetIncreases((PayIncreases) successObject, success, (Error) failObject);
                        break;
                    case "getLocation":
                        listener.onGetLocation((Location) successObject, success, (Error) failObject);
                        break;
                    case "getMessages":
                        listener.onGetMessages((Messages) successObject, success, (Error) failObject);
                        break;
                    case "getMilestones":
                        listener.onGetMilestones((Milestones) successObject, success, (Error) failObject);
                        break;
                    case "getPay":
                        listener.onGetPay((Pay) successObject, success, (Error) failObject);
                        break;
                    case "getPenalties":
                        listener.onGetPenalties((PayModifiers) successObject, success, (Error) failObject);
                        break;
                    case "getPenalty":
                        listener.onGetPenalty((PayModifier) successObject, success, (Error) failObject);
                        break;
                    case "getProblem":
                        listener.onGetProblem((Problems) successObject, success, (Error) failObject);
                        break;
                    case "getProblems":
                        listener.onGetProblems((Problems) successObject, success, (Error) failObject);
                        break;
                    case "getProviders":
                        listener.onGetProviders((Users[]) successObject, success, (Error) failObject);
                        break;
                    case "getRequest":
                        listener.onGetRequest((Request) successObject, success, (Error) failObject);
                        break;
                    case "getRequests":
                        listener.onGetRequests((Requests) successObject, success, (Error) failObject);
                        break;
                    case "GetScheduleAndLocation":
                        listener.onGetScheduleAndLocation((EtaMassAcceptWithLocation) successObject, success, (Error) failObject);
                        break;
                    case "getSchedule":
                        listener.onGetSchedule((Schedule) successObject, success, (Error) failObject);
                        break;
                    case "getShipments":
                        listener.onGetShipments((Shipments) successObject, success, (Error) failObject);
                        break;
                    case "getSignature":
                        listener.onGetSignature((Signature) successObject, success, (Error) failObject);
                        break;
                    case "getSignatures":
                        listener.onGetSignatures((Signature[]) successObject, success, (Error) failObject);
                        break;
                    case "getStatus":
                        listener.onGetStatus((com.fieldnation.v2.data.model.Status) successObject, success, (Error) failObject);
                        break;
                    case "getTask":
                        listener.onGetTask((Task) successObject, success, (Error) failObject);
                        break;
                    case "getTasks":
                        listener.onGetTasks((Tasks) successObject, success, (Error) failObject);
                        break;
                    case "getTimeLogs":
                        listener.onGetTimeLogs((TimeLogs) successObject, success, (Error) failObject);
                        break;
                    case "getWorkOrder":
                        listener.onGetWorkOrder((WorkOrder) successObject, success, (Error) failObject);
                        break;
                    case "getWorkOrderLists":
                        listener.onGetWorkOrderLists((SavedList[]) successObject, success, (Error) failObject);
                        break;
                    case "getWorkOrders":
                        listener.onGetWorkOrders((WorkOrders) successObject, success, (Error) failObject);
                        break;
                    case "groupTask":
                        listener.onGroupTask(success, (Error) failObject);
                        break;
                    case "incompleteWorkOrder":
                        listener.onIncompleteWorkOrder(success, (Error) failObject);
                        break;
                    case "MassAcceptWorkOrder":
                        listener.onMassAcceptWorkOrder(success, (Error) failObject);
                        break;
                    case "publish":
                        listener.onPublish(success, (Error) failObject);
                        break;
                    case "removeAlert":
                        listener.onRemoveAlert(success, (Error) failObject);
                        break;
                    case "removeAlerts":
                        listener.onRemoveAlerts(success, (Error) failObject);
                        break;
                    case "removeBonus":
                        listener.onRemoveBonus(success, (Error) failObject);
                        break;
                    case "removeContact":
                        listener.onRemoveContact(success, (Error) failObject);
                        break;
                    case "removeDiscount":
                        listener.onRemoveDiscount(success, (Error) failObject);
                        break;
                    case "removeMessage":
                        listener.onRemoveMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "removePenalty":
                        listener.onRemovePenalty(success, (Error) failObject);
                        break;
                    case "removeProblem":
                        listener.onRemoveProblem(success, (Error) failObject);
                        break;
                    case "removeRequest":
                        listener.onRemoveRequest(success, (Error) failObject);
                        break;
                    case "removeTask":
                        listener.onRemoveTask(success, (Error) failObject);
                        break;
                    case "removeTimeLog":
                        listener.onRemoveTimeLog(success, (Error) failObject);
                        break;
                    case "reorderTask":
                        listener.onReorderTask(success, (Error) failObject);
                        break;
                    case "replyMessage":
                        listener.onReplyMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "request":
                        listener.onRequest(success, (Error) failObject);
                        break;
                    case "resolveReopenReportProblem":
                        listener.onResolveReopenReportProblem(success, (Error) failObject);
                        break;
                    case "revertWorkOrderToDraft":
                        listener.onRevertWorkOrderToDraft(success, (Error) failObject);
                        break;
                    case "routeUser":
                        listener.onRouteUser(success, (Error) failObject);
                        break;
                    case "unapproveWorkOrder":
                        listener.onUnapproveWorkOrder(success, (Error) failObject);
                        break;
                    case "unassignUser":
                        listener.onUnassignUser(success, (Error) failObject);
                        break;
                    case "unpublish":
                        listener.onUnpublish(success, (Error) failObject);
                        break;
                    case "unRouteUser":
                        listener.onUnRouteUser(success, (Error) failObject);
                        break;
                    case "updateAllTimeLogs":
                        listener.onUpdateAllTimeLogs(success, (Error) failObject);
                        break;
                    case "updateAttachment":
                        listener.onUpdateAttachment(success, (Error) failObject);
                        break;
                    case "updateBonus":
                        listener.onUpdateBonus(success, (Error) failObject);
                        break;
                    case "updateContact":
                        listener.onUpdateContact(success, (Error) failObject);
                        break;
                    case "updateCustomField":
                        listener.onUpdateCustomField((CustomField) successObject, success, (Error) failObject);
                        break;
                    case "updateDiscount":
                        listener.onUpdateDiscount((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "updateETA":
                        listener.onUpdateETA(success, (Error) failObject);
                        break;
                    case "updateExpense":
                        listener.onUpdateExpense((Expense) successObject, success, (Error) failObject);
                        break;
                    case "updateFolder":
                        listener.onUpdateFolder(success, (Error) failObject);
                        break;
                    case "updateHold":
                        listener.onUpdateHold((WorkOrder) successObject, success, (Error) failObject);
                        break;
                    case "updateHolds":
                        listener.onUpdateHolds(success, (Error) failObject);
                        break;
                    case "updateIncrease":
                        listener.onUpdateIncrease((PayIncrease) successObject, success, (Error) failObject);
                        break;
                    case "updateLocation":
                        listener.onUpdateLocation(success, (Error) failObject);
                        break;
                    case "updateMessage":
                        listener.onUpdateMessage((IdResponse) successObject, success, (Error) failObject);
                        break;
                    case "updatePay":
                        listener.onUpdatePay(success, (Error) failObject);
                        break;
                    case "updatePenalty":
                        listener.onUpdatePenalty(success, (Error) failObject);
                        break;
                    case "updateProblem":
                        listener.onUpdateProblem(success, (Error) failObject);
                        break;
                    case "updateSchedule":
                        listener.onUpdateSchedule(success, (Error) failObject);
                        break;
                    case "updateShipment":
                        listener.onUpdateShipment(success, (Error) failObject);
                        break;
                    case "updateTask":
                        listener.onUpdateTask(success, (Error) failObject);
                        break;
                    case "updateTimeLog":
                        listener.onUpdateTimeLog(success, (Error) failObject);
                        break;
                    case "updateWorkOrder":
                        listener.onUpdateWorkOrder(success, (Error) failObject);
                        break;
                    case "verifyTimeLog":
                        listener.onVerifyTimeLog(success, (Error) failObject);
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

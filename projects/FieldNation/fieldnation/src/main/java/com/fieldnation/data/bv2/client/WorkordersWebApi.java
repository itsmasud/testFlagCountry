package com.fieldnation.data.bv2.client;

import android.content.Context;
import android.net.Uri;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class WorkordersWebApi {
    private static final String TAG = "WorkordersWebApi";

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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .urlParams((getWorkOrdersOptions.getList() != null ? "?list=" + getWorkOrdersOptions.getList() : "")
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .urlParams((updateExpenseOptions.getAsync() != null ? "?async=" + updateExpenseOptions.getAsync() : "")
                                   );

            if (updateExpenseOptions.getExpense() != null)
                builder.body(updateExpenseOptions.getExpense().toJson().toString());

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("PUT//api/rest/v2/workorders/{work_order_id}/expenses/{expense_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .urlParams((getProvidersOptions.getSticky() != null ? "?sticky=" + getProvidersOptions.getSticky() : "")
                                    + (getProvidersOptions.getDefaultView() != null ? "&default_view=" + getProvidersOptions.getDefaultView() : "")
                                    + (getProvidersOptions.getView() != null ? "&view=" + getProvidersOptions.getView() : "")
                                   );

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//api/rest/v2/workorders/{work_order_id}/providers")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
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
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(builder).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}

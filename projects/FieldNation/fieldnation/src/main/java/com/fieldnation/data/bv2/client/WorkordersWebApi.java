package com.fieldnation.data.bv2.client;

import android.content.Context;

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
     * @param isBackground indicates that this call is low priority
     */
    public static void revertWorkOrderToDraft(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/draft")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/draft")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void revertWorkOrderToDraft(Context context, int workOrderId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/draft")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/draft")
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void incompleteTask(Context context, int workOrderId, int taskId, boolean isBackground) {
    }

    /**
     * Get a custom field by work order and custom field
     *
     * @param workOrderId ID of work order
     * @param customFieldId Custom field id
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomField(Context context, int workOrderId, int customFieldId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/custom_fields/{custom_field_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/custom_fields/" + customFieldId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateCustomField(Context context, int workOrderId, int customFieldId, CustomField customField, boolean isBackground) {
    }

    /**
     * Update a custom field value on a work order
     *
     * @param workOrderId Work Order ID
     * @param customFieldId Custom field ID
     * @param customField Custom field
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateCustomField(Context context, int workOrderId, int customFieldId, CustomField customField, Boolean async, boolean isBackground) {
    }

    /**
     * Marks a work order complete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void completeWorkOrder(Context context, int workOrderId, boolean isBackground) {
    }

    /**
     * Marks a work order complete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void completeWorkOrder(Context context, int workOrderId, Boolean async, boolean isBackground) {
    }

    /**
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param reason Reason
     * @param isBackground indicates that this call is low priority
     */
    public static void incompleteWorkOrder(Context context, int workOrderId, String reason, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/complete")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/complete")
                            .urlParams("?reason=" + reason)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void incompleteWorkOrder(Context context, int workOrderId, String reason, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/complete")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/complete")
                            .urlParams("?reason=" + reason + "&async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void addExpense(Context context, int workOrderId, Expense expense, boolean isBackground) {
    }

    /**
     * Adds an expense on a work order
     *
     * @param workOrderId ID of work order
     * @param expense Expense
     * @param async Asynchroneous (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addExpense(Context context, int workOrderId, Expense expense, Boolean async, boolean isBackground) {
    }

    /**
     * Get all expenses of a work order
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getExpenses(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/expenses")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/expenses")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void addAttachment(Context context, int workOrderId, int folderId, String attachment, java.io.File file, boolean isBackground) {
    }

    /**
     * Uploads a file by an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachment Folder
     * @param file File
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addAttachment(Context context, int workOrderId, int folderId, String attachment, java.io.File file, Boolean async, boolean isBackground) {
    }

    /**
     * Gets an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param isBackground indicates that this call is low priority
     */
    public static void getFolder(Context context, int workOrderId, int folderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/attachments/{folder_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/attachments/" + folderId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteFolder(Context context, int workOrderId, int folderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/attachments/{folder_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/attachments/" + folderId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteFolder(Context context, int workOrderId, int folderId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/attachments/{folder_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/attachments/" + folderId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateFolder(Context context, int workOrderId, int folderId, AttachmentFolder folder, boolean isBackground) {
    }

    /**
     * Updates an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param folder Folder
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateFolder(Context context, int workOrderId, int folderId, AttachmentFolder folder, Boolean async, boolean isBackground) {
    }

    /**
     * Returns a list of work orders.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void getWorkOrders(Context context, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders")
                    ).build();

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
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders")
                            .urlParams("?list=" + getWorkOrdersOptions.getList() + "&columns=" + getWorkOrdersOptions.getColumns() + "&page=" + getWorkOrdersOptions.getPage() + "&per_page=" + getWorkOrdersOptions.getPerPage() + "&view=" + getWorkOrdersOptions.getView() + "&sticky=" + getWorkOrdersOptions.getSticky() + "&sort=" + getWorkOrdersOptions.getSort() + "&order=" + getWorkOrdersOptions.getOrder() + "&f_=" + getWorkOrdersOptions.getF() + "&f_max_approval_time=" + getWorkOrdersOptions.getFMaxApprovalTime() + "&f_rating=" + getWorkOrdersOptions.getFRating() + "&f_requests=" + getWorkOrdersOptions.getFRequests() + "&f_counter_offers=" + getWorkOrdersOptions.getFCounterOffers() + "&f_hourly=" + getWorkOrdersOptions.getFHourly() + "&f_fixed=" + getWorkOrdersOptions.getFFixed() + "&f_device=" + getWorkOrdersOptions.getFDevice() + "&f_pay=" + getWorkOrdersOptions.getFPay() + "&f_templates=" + getWorkOrdersOptions.getFTemplates() + "&f_type_of_work=" + getWorkOrdersOptions.getFTypeOfWork() + "&f_time_zone=" + getWorkOrdersOptions.getFTimeZone() + "&f_mode=" + getWorkOrdersOptions.getFMode() + "&f_company=" + getWorkOrdersOptions.getFCompany() + "&f_worked_with=" + getWorkOrdersOptions.getFWorkedWith() + "&f_manager=" + getWorkOrdersOptions.getFManager() + "&f_client=" + getWorkOrdersOptions.getFClient() + "&f_project=" + getWorkOrdersOptions.getFProject() + "&f_approval_window=" + getWorkOrdersOptions.getFApprovalWindow() + "&f_review_window=" + getWorkOrdersOptions.getFReviewWindow() + "&f_network=" + getWorkOrdersOptions.getFNetwork() + "&f_auto_assign=" + getWorkOrdersOptions.getFAutoAssign() + "&f_schedule=" + getWorkOrdersOptions.getFSchedule() + "&f_created=" + getWorkOrdersOptions.getFCreated() + "&f_published=" + getWorkOrdersOptions.getFPublished() + "&f_routed=" + getWorkOrdersOptions.getFRouted() + "&f_published_routed=" + getWorkOrdersOptions.getFPublishedRouted() + "&f_completed=" + getWorkOrdersOptions.getFCompleted() + "&f_approved_cancelled=" + getWorkOrdersOptions.getFApprovedCancelled() + "&f_confirmed=" + getWorkOrdersOptions.getFConfirmed() + "&f_assigned=" + getWorkOrdersOptions.getFAssigned() + "&f_saved_location=" + getWorkOrdersOptions.getFSavedLocation() + "&f_saved_location_group=" + getWorkOrdersOptions.getFSavedLocationGroup() + "&f_city=" + getWorkOrdersOptions.getFCity() + "&f_state=" + getWorkOrdersOptions.getFState() + "&f_postal_code=" + getWorkOrdersOptions.getFPostalCode() + "&f_country=" + getWorkOrdersOptions.getFCountry() + "&f_flags=" + getWorkOrdersOptions.getFFlags() + "&f_assignment=" + getWorkOrdersOptions.getFAssignment() + "&f_confirmation=" + getWorkOrdersOptions.getFConfirmation() + "&f_financing=" + getWorkOrdersOptions.getFFinancing() + "&f_geo=" + getWorkOrdersOptions.getFGeo() + "&f_search=" + getWorkOrdersOptions.getFSearch())
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void verifyTimeLog(Context context, int workOrderId, int workorderHoursId, boolean isBackground) {
    }

    /**
     * Verify time log for assigned work order
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param async Return the model in the response (slower) (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void verifyTimeLog(Context context, int workOrderId, int workorderHoursId, Boolean async, boolean isBackground) {
    }

    /**
     * Removes a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId Contact id
     * @param isBackground indicates that this call is low priority
     */
    public static void removeContact(Context context, int workOrderId, int contactId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/contacts/{contact_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/contacts/" + contactId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateContact(Context context, int workOrderId, int contactId, Contact json, boolean isBackground) {
    }

    /**
     * Get pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param isBackground indicates that this call is low priority
     */
    public static void getIncrease(Context context, int workOrderId, int increaseId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/increases/{increase_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/increases/" + increaseId)
                    ).build();

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
    public static void getIncrease(Context context, int workOrderId, int increaseId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/increases/{increase_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/increases/" + increaseId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteIncrease(Context context, int workOrderId, int increaseId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/increases/{increase_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/increases/" + increaseId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteIncrease(Context context, int workOrderId, int increaseId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/increases/{increase_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/increases/" + increaseId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateIncrease(Context context, int workOrderId, int increaseId, PayIncrease increase, boolean isBackground) {
    }

    /**
     * Update pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param increase Increase structure for update
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateIncrease(Context context, int workOrderId, int increaseId, PayIncrease increase, Boolean async, boolean isBackground) {
    }

    /**
     * Delete an expense from a work order
     *
     * @param workOrderId ID of work order
     * @param expenseId ID of expense
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteExpense(Context context, int workOrderId, int expenseId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/expenses/{expense_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/expenses/" + expenseId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteExpense(Context context, int workOrderId, int expenseId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/expenses/{expense_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/expenses/" + expenseId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateExpense(Context context, int workOrderId, int expenseId, boolean isBackground) {
    }

    /**
     * Update an Expense of a Work order
     *
     * @param workOrderId ID of work order
     * @param expenseId ID of expense
     * @param updateExpenseOptions Additional optional parameters
     * @param isBackground indicates that this call is low priority
     */
    public static void updateExpense(Context context, int workOrderId, int expenseId, UpdateExpenseOptions updateExpenseOptions, boolean isBackground) {
    }

    /**
     * Returns a list of pay increases requested by the assigned provider.
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getIncreases(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/increases")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/increases")
                    ).build();

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
    public static void getPay(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/pay")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/pay")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updatePay(Context context, int workOrderId, Pay pay, boolean isBackground) {
    }

    /**
     * Updates the pay of a work order, or requests an adjustment
     *
     * @param workOrderId ID of work order
     * @param pay Pay
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updatePay(Context context, int workOrderId, Pay pay, Boolean async, boolean isBackground) {
    }

    /**
     * Adds a task to a work order
     *
     * @param workOrderId Work order id
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void addTask(Context context, int workOrderId, Task json, boolean isBackground) {
    }

    /**
     * Get a list of a work order's tasks
     *
     * @param workOrderId Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getTasks(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/tasks")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/tasks")
                    ).build();

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
    public static void getMilestones(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/milestones")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/milestones")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void addSignature(Context context, int workOrderId, Signature signature, boolean isBackground) {
    }

    /**
     * Add signature by work order
     *
     * @param workOrderId ID of work order
     * @param signature Signature JSON
     * @param async async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addSignature(Context context, int workOrderId, Signature signature, Boolean async, boolean isBackground) {
    }

    /**
     * Returns a list of signatures uploaded by the assigned provider
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getSignatures(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/signatures")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/signatures")
                    ).build();

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
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/providers")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/providers")
                    ).build();

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
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/providers")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/providers")
                            .urlParams("?sticky=" + getProvidersOptions.getSticky() + "&default_view=" + getProvidersOptions.getDefaultView() + "&view=" + getProvidersOptions.getView())
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void addMessage(Context context, String workOrderId, Message json, boolean isBackground) {
    }

    /**
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json JSON payload
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addMessage(Context context, String workOrderId, Message json, Boolean async, boolean isBackground) {
    }

    /**
     * Gets a list of work order messages
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getMessages(Context context, String workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/messages")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/messages")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Cancel work order swap request.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void cancelSwapRequest(Context context, boolean isBackground) {
    }

    /**
     * Add time log for work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog Check in information
     * @param isBackground indicates that this call is low priority
     */
    public static void addTimeLog(Context context, int workOrderId, TimeLog timeLog, boolean isBackground) {
    }

    /**
     * Returns a list of time logs applied by the assigned provider
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getTimeLogs(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/time_logs")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/time_logs")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateAllTimeLogs(Context context, int workOrderId, TimeLog timeLog, boolean isBackground) {
    }

    /**
     * Update all time logs for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog Check in information
     * @param async Return the model in the response (slower) (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateAllTimeLogs(Context context, int workOrderId, TimeLog timeLog, Boolean async, boolean isBackground) {
    }

    /**
     * Gets a work order by its id
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getWorkOrder(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteWorkOrder(Context context, int workOrderId, Cancellation cancellation, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteWorkOrder(Context context, int workOrderId, Cancellation cancellation, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateWorkOrder(Context context, int workOrderId, WorkOrder workOrder, boolean isBackground) {
    }

    /**
     * Updates a work order by its id
     *
     * @param workOrderId ID of work order
     * @param workOrder Work order model
     * @param async Asynchroneous (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateWorkOrder(Context context, int workOrderId, WorkOrder workOrder, Boolean async, boolean isBackground) {
    }

    /**
     * Gets a single signature uploaded by the assigned provider
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
     * @param isBackground indicates that this call is low priority
     */
    public static void getSignature(Context context, int workOrderId, int signatureId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/signatures/{signature_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/signatures/" + signatureId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteSignature(Context context, int workOrderId, int signatureId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/signatures/{signature_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/signatures/" + signatureId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteSignature(Context context, int workOrderId, int signatureId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/signatures/{signature_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/signatures/" + signatureId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void addFolder(Context context, int workOrderId, AttachmentFolder folder, boolean isBackground) {
    }

    /**
     * Adds an attachment folder
     *
     * @param workOrderId Work order id
     * @param folder Folder
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addFolder(Context context, int workOrderId, AttachmentFolder folder, Boolean async, boolean isBackground) {
    }

    /**
     * Gets a list of attachment folders which contain files and deliverables for the work order
     *
     * @param workOrderId Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getAttachments(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/attachments")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/attachments")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void completeTask(Context context, int workOrderId, int taskId, boolean isBackground) {
    }

    /**
     * Allows an assigned provider to removes a discount they previously applied from a work order, increasing the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param discountId ID of the discount
     * @param isBackground indicates that this call is low priority
     */
    public static void removeDiscount(Context context, int workOrderId, int discountId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/discounts/{discount_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/discounts/" + discountId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateDiscount(Context context, int workOrderId, int discountId, PayModifier json, boolean isBackground) {
    }

    /**
     * Remove time log for assigned work order
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param isBackground indicates that this call is low priority
     */
    public static void removeTimeLog(Context context, int workOrderId, int workorderHoursId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void removeTimeLog(Context context, int workOrderId, int workorderHoursId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/time_logs/{workorder_hours_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/time_logs/" + workorderHoursId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateTimeLog(Context context, int workOrderId, int workorderHoursId, TimeLog timeLog, boolean isBackground) {
    }

    /**
     * Update time log for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param workorderHoursId ID of work order hour
     * @param timeLog Check in information
     * @param async Return the model in the response (slower) (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateTimeLog(Context context, int workOrderId, int workorderHoursId, TimeLog timeLog, Boolean async, boolean isBackground) {
    }

    /**
     * Gets an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachmentId File id
     * @param isBackground indicates that this call is low priority
     */
    public static void getFile(Context context, int workOrderId, int folderId, int attachmentId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteAttachment(Context context, int workOrderId, int folderId, int attachmentId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteAttachment(Context context, int workOrderId, int folderId, int attachmentId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/attachments/{folder_id}/{attachment_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/attachments/" + folderId + "/" + attachmentId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateAttachment(Context context, int workOrderId, int folderId, int attachmentId, Attachment attachment, boolean isBackground) {
    }

    /**
     * Updates an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId Folder id
     * @param attachmentId File id
     * @param attachment Attachment
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateAttachment(Context context, int workOrderId, int folderId, int attachmentId, Attachment attachment, Boolean async, boolean isBackground) {
    }

    /**
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void assignUser(Context context, int workOrderId, Assignee assignee, boolean isBackground) {
    }

    /**
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee JSON Model
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void assignUser(Context context, int workOrderId, Assignee assignee, Boolean async, boolean isBackground) {
    }

    /**
     * Get assignee of a work order
     *
     * @param workOrderId Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getAssignee(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/assignee")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/assignee")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void unassignUser(Context context, int workOrderId, Assignee assignee, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/assignee")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/assignee")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void unassignUser(Context context, int workOrderId, Assignee assignee, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/assignee")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/assignee")
                            .urlParams("?async=" + async)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Publishes a work order to the marketplace where it can garner requests.
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void publish(Context context, int workOrderId, boolean isBackground) {
    }

    /**
     * Publishes a work order to the marketplace where it can garner requests.
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void publish(Context context, int workOrderId, Boolean async, boolean isBackground) {
    }

    /**
     * Unpublishes a work order from the marketplace so that no requests or counter-offers can be made. Moves to draft unless it was also routed.
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void unpublish(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/publish")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/publish")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void unpublish(Context context, int workOrderId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/publish")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/publish")
                            .urlParams("?async=" + async)
                    ).build();

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
    public static void getStatus(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/status")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/status")
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void approveWorkOrder(Context context, int workOrderId, boolean isBackground) {
    }

    /**
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void approveWorkOrder(Context context, int workOrderId, Boolean async, boolean isBackground) {
    }

    /**
     * Unapproves a completed work order and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void unapproveWorkOrder(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/approve")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/approve")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void unapproveWorkOrder(Context context, int workOrderId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/approve")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/approve")
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void acceptIncrease(Context context, int workOrderId, int increaseId, boolean isBackground) {
    }

    /**
     * Deletes a shipment from a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId Shipment id
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteShipment(Context context, int workOrderId, int shipmentId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/shipments/{shipment_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/shipments/" + shipmentId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void deleteShipment(Context context, int workOrderId, int shipmentId, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/shipments/{shipment_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/shipments/" + shipmentId)
                            .urlParams("?async=" + async)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateShipment(Context context, int workOrderId, int shipmentId, Shipment shipment, boolean isBackground) {
    }

    /**
     * Updates a shipment attached to a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId Shipment id
     * @param shipment Shipment
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateShipment(Context context, int workOrderId, int shipmentId, Shipment shipment, Boolean async, boolean isBackground) {
    }

    /**
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     * @param isBackground indicates that this call is low priority
     */
    public static void addPenalty(Context context, int workOrderId, int penaltyId, boolean isBackground) {
    }

    /**
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     * @param penalty Penalty (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addPenalty(Context context, int workOrderId, int penaltyId, PayModifier penalty, boolean isBackground) {
    }

    /**
     * Gets a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getPenalty(Context context, int workOrderId, int penaltyId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/penalties/{penalty_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/penalties/" + penaltyId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void removePenalty(Context context, int workOrderId, int penaltyId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/penalties/{penalty_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/penalties/" + penaltyId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updatePenalty(Context context, int workOrderId, int penaltyId, boolean isBackground) {
    }

    /**
     * Updates a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId Penalty ID
     * @param penalty Penalty (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updatePenalty(Context context, int workOrderId, int penaltyId, PayModifier penalty, boolean isBackground) {
    }

    /**
     * Decline work order swap request.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void declineSwapRequest(Context context, boolean isBackground) {
    }

    /**
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     * @param json JSON payload
     * @param isBackground indicates that this call is low priority
     */
    public static void replyMessage(Context context, String workOrderId, String messageId, Message json, boolean isBackground) {
    }

    /**
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     * @param json JSON payload
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void replyMessage(Context context, String workOrderId, String messageId, Message json, Boolean async, boolean isBackground) {
    }

    /**
     * Removes a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId ID of work order message
     * @param isBackground indicates that this call is low priority
     */
    public static void removeMessage(Context context, String workOrderId, String messageId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/messages/{message_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/messages/" + messageId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateMessage(Context context, String workOrderId, String messageId, Message json, boolean isBackground) {
    }

    /**
     * Get a task by work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param isBackground indicates that this call is low priority
     */
    public static void getTask(Context context, int workOrderId, int taskId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/tasks/{task_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/tasks/" + taskId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void removeTask(Context context, int workOrderId, int taskId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/tasks/{task_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/tasks/" + taskId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateTask(Context context, int workOrderId, int taskId, Task json, boolean isBackground) {
    }

    /**
     * Deny pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId ID of work order increase
     * @param isBackground indicates that this call is low priority
     */
    public static void denyIncrease(Context context, int workOrderId, int increaseId, boolean isBackground) {
    }

    /**
     * Sets up an alert to be fired upon the completion of a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void addAlertToWorkOrderAndTask(Context context, int workOrderId, int taskId, TaskAlert json, boolean isBackground) {
    }

    /**
     * Removes all alerts associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param isBackground indicates that this call is low priority
     */
    public static void removeAlerts(Context context, int workOrderId, int taskId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/tasks/{task_id}/alerts")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void request(Context context, int workOrderId, Request request, boolean isBackground) {
    }

    /**
     * Request or un-hide a request for a work order
     *
     * @param workOrderId Work order id
     * @param request JSON Model
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void request(Context context, int workOrderId, Request request, Boolean async, boolean isBackground) {
    }

    /**
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param request JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void removeRequest(Context context, int workOrderId, Request request, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/requests")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/requests")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void removeRequest(Context context, int workOrderId, Request request, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/requests")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/requests")
                            .urlParams("?async=" + async)
                    ).build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    /**
     * Accept work order swap request.
     *
     * @param isBackground indicates that this call is low priority
     */
    public static void acceptSwapRequest(Context context, boolean isBackground) {
    }

    /**
     * Get a list of custom fields and their values for a work order.
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getCustomFields(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/custom_fields")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/custom_fields")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void removeAlert(Context context, int workOrderId, int taskId, int alertId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/tasks/{task_id}/alerts/{alert_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/tasks/" + taskId + "/alerts/" + alertId)
                    ).build();

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
    public static void getSchedule(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/schedule")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/schedule")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateSchedule(Context context, int workOrderId, Schedule schedule, boolean isBackground) {
    }

    /**
     * Updates the service schedule or eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param schedule JSON Payload
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateSchedule(Context context, int workOrderId, Schedule schedule, Boolean async, boolean isBackground) {
    }

    /**
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds Holds
     * @param isBackground indicates that this call is low priority
     */
    public static void updateHolds(Context context, int workOrderId, String holds, boolean isBackground) {
    }

    /**
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds Holds
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateHolds(Context context, int workOrderId, String holds, Boolean async, boolean isBackground) {
    }

    /**
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId ID of report problem flag
     * @param json JSON payload
     * @param isBackground indicates that this call is low priority
     */
    public static void resolveReopenReportProblem(Context context, int workOrderId, int flagId, Message json, boolean isBackground) {
    }

    /**
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId ID of report problem flag
     * @param json JSON payload
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void resolveReopenReportProblem(Context context, int workOrderId, int flagId, Message json, Boolean async, boolean isBackground) {
    }

    /**
     * Assigned provider route to adds and apply a discount to a work order which reduces the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param json Payload of the discount
     * @param isBackground indicates that this call is low priority
     */
    public static void addDiscount(Context context, int workOrderId, PayModifier json, boolean isBackground) {
    }

    /**
     * Returns a list of discounts applied by the assigned provider to reduce the payout of the work order.
     *
     * @param workOrderId ID of work order
     * @param isBackground indicates that this call is low priority
     */
    public static void getDiscounts(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/discounts")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/discounts")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void reorderTask(Context context, int workOrderId, int taskId, int targetTaskId, String position, boolean isBackground) {
    }

    /**
     * Regroups a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId Task id
     * @param group New group
     * @param destination beginning or end (position in new group)
     * @param isBackground indicates that this call is low priority
     */
    public static void groupTask(Context context, int workOrderId, int taskId, String group, String destination, boolean isBackground) {
    }

    /**
     * Adds a contact to a work order
     *
     * @param workOrderId Work order id
     * @param json JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void addContact(Context context, int workOrderId, Contact json, boolean isBackground) {
    }

    /**
     * Get a list of contacts on a work order
     *
     * @param workOrderId Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getContacts(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/contacts")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/contacts")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void addShipment(Context context, int workOrderId, Shipment shipment, boolean isBackground) {
    }

    /**
     * Adds a shipment to a work order
     *
     * @param workOrderId Work order id
     * @param shipment Shipment
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addShipment(Context context, int workOrderId, Shipment shipment, Boolean async, boolean isBackground) {
    }

    /**
     * Get a list of shipments on a work order
     *
     * @param workOrderId Work order id
     * @param isBackground indicates that this call is low priority
     */
    public static void getShipments(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/shipments")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/shipments")
                    ).build();

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
    public static void getPenalties(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/penalties")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/penalties")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void routeUser(Context context, int workOrderId, Route route, boolean isBackground) {
    }

    /**
     * Route a user to a work order
     *
     * @param workOrderId Work order id
     * @param route JSON Model
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void routeUser(Context context, int workOrderId, Route route, Boolean async, boolean isBackground) {
    }

    /**
     * Unroute a user from a work order
     *
     * @param workOrderId Work order id
     * @param route JSON Model
     * @param isBackground indicates that this call is low priority
     */
    public static void unRouteUser(Context context, int workOrderId, Route route, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/route")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/route")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void unRouteUser(Context context, int workOrderId, Route route, Boolean async, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/route")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/route")
                            .urlParams("?async=" + async)
                    ).build();

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
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/lists")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/lists")
                    ).build();

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
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/problems/messages")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/problems/messages")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void reportProblem(Context context, String workOrderId, Message json, boolean isBackground) {
    }

    /**
     * Report a problem to a work order
     *
     * @param workOrderId ID of work order
     * @param json JSON payload
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void reportProblem(Context context, String workOrderId, Message json, Boolean async, boolean isBackground) {
    }

    /**
     * Get a list of available bonuses on a work order that can be applied to increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId Work Order ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getBonuses(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/bonuses")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/bonuses")
                    ).build();

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
    public static void getLocation(Context context, int workOrderId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/location")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/location")
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateLocation(Context context, int workOrderId, Location location, boolean isBackground) {
    }

    /**
     * Updates the location of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param location JSON Payload
     * @param async Async (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void updateLocation(Context context, int workOrderId, Location location, Boolean async, boolean isBackground) {
    }

    /**
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     * @param isBackground indicates that this call is low priority
     */
    public static void addBonus(Context context, int workOrderId, int bonusId, boolean isBackground) {
    }

    /**
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     * @param bonus Bonus (Optional)
     * @param isBackground indicates that this call is low priority
     */
    public static void addBonus(Context context, int workOrderId, int bonusId, PayModifier bonus, boolean isBackground) {
    }

    /**
     * Gets a bonus for a work order
     *
     * @param workOrderId ID of work order
     * @param bonusId Bonus ID
     * @param isBackground indicates that this call is low priority
     */
    public static void getBonus(Context context, int workOrderId, int bonusId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/bonuses/{bonus_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/bonuses/" + bonusId)
                    ).build();

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
    public static void getBonus(Context context, int workOrderId, int bonusId, PayModifier bonus, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET//workorders/{work_order_id}/bonuses/{bonus_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("GET")
                            .path("/workorders/" + workOrderId + "/bonuses/" + bonusId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void removeBonus(Context context, int workOrderId, int bonusId, boolean isBackground) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("DELETE//workorders/{work_order_id}/bonuses/{bonus_id}")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .isSyncCall(isBackground)
                    .request(new HttpJsonBuilder()
                            .protocol("https")
                            .method("DELETE")
                            .path("/workorders/" + workOrderId + "/bonuses/" + bonusId)
                    ).build();

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
     * @param isBackground indicates that this call is low priority
     */
    public static void updateBonus(Context context, int workOrderId, int bonusId, PayModifier bonus, boolean isBackground) {
    }

}

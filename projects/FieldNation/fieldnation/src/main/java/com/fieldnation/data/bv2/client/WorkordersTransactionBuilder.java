package com.fieldnation.data.bv2.client;

import android.content.Context;

import com.fieldnation.data.bv2.model.*;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by dmgen from swagger on 1/25/17.
 */

public class WorkordersTransactionBuilder {
    private static final String TAG = "WorkordersTransactionBuilder";

    /**
     * Reverts a work order to draft status
     *
     * @param workOrderId ID of work order
     */
    public static void revertWorkOrderToDraft(Context context, int workOrderId) {
    }

    /**
     * Marks a task associated with a work order as incomplete
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void incompleteTask(Context context, int workOrderId, int taskId) {
    }

    /**
     * Get a custom field by work order and custom field
     *
     * @param workOrderId   ID of work order
     * @param customFieldId Custom field id
     */
    public static void getCustomField(Context context, int workOrderId, int customFieldId) {
    }

    /**
     * Update a custom field value on a work order
     *
     * @param workOrderId   Work Order ID
     * @param customFieldId Custom field ID
     * @param customField   Custom field
     */
    public static void updateCustomField(Context context, int workOrderId, int customFieldId, CustomField customField) {
    }

    /**
     * Marks a work order complete and moves it to work done status
     *
     * @param workOrderId ID of work order
     */
    public static void completeWorkOrder(Context context, int workOrderId) {
    }

    /**
     * Marks a work order incomplete and moves it to work done status
     *
     * @param workOrderId ID of work order
     * @param reason      Reason
     */
    public static void incompleteWorkOrder(Context context, int workOrderId, String reason) {
    }

    /**
     * Adds an expense on a work order
     *
     * @param workOrderId ID of work order
     * @param expense     Expense
     */
    public static void addExpense(Context context, int workOrderId, Expense expense) {
    }

    /**
     * Get all expenses of a work order
     *
     * @param workOrderId ID of work order
     */
    public static void getExpenses(Context context, int workOrderId) {
    }

    /**
     * Uploads a file by an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param attachment  Folder
     * @param file        File
     */
    public static void addAttachment(Context context, int workOrderId, int folderId, String attachment, java.io.File file) {
    }

    /**
     * Gets an attachment folder and its contents
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     */
    public static void getFolder(Context context, int workOrderId, int folderId) {
    }

    /**
     * Deletes an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     */
    public static void deleteFolder(Context context, int workOrderId, int folderId) {
    }

    /**
     * Updates an attachment folder
     *
     * @param workOrderId Work order id
     * @param folderId    Folder id
     * @param folder      Folder
     */
    public static void updateFolder(Context context, int workOrderId, int folderId, AttachmentFolder folder) {
    }

    /**
     * Returns a list of work orders.
     */
    public static void getWorkOrders(Context context) {
    }

    /**
     * Verify time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     */
    public static void verifyTimeLog(Context context, int workOrderId, int workorderHoursId) {
    }

    /**
     * Removes a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId   Contact id
     */
    public static void removeContact(Context context, int workOrderId, int contactId) {
    }

    /**
     * Updates a work order contact
     *
     * @param workOrderId Work order id
     * @param contactId   Contact id
     * @param json        JSON Model
     */
    public static void updateContact(Context context, int workOrderId, int contactId, Contact json) {
    }

    /**
     * Get pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void getIncrease(Context context, int workOrderId, int increaseId) {
    }

    /**
     * Delete pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void deleteIncrease(Context context, int workOrderId, int increaseId) {
    }

    /**
     * Update pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     * @param increase    Increase structure for update
     */
    public static void updateIncrease(Context context, int workOrderId, int increaseId, PayIncrease increase) {
    }

    /**
     * Delete an expense from a work order
     *
     * @param workOrderId ID of work order
     * @param expenseId   ID of expense
     */
    public static void deleteExpense(Context context, int workOrderId, int expenseId) {
    }

    /**
     * Update an Expense of a Work order
     *
     * @param workOrderId ID of work order
     * @param expenseId   ID of expense
     */
    public static void updateExpense(Context context, int workOrderId, int expenseId) {
    }

    /**
     * Returns a list of pay increases requested by the assigned provider.
     *
     * @param workOrderId ID of work order
     */
    public static void getIncreases(Context context, int workOrderId) {
    }

    /**
     * Gets the pay for a work order
     *
     * @param workOrderId ID of work order
     */
    public static void getPay(Context context, int workOrderId) {
    }

    /**
     * Updates the pay of a work order, or requests an adjustment
     *
     * @param workOrderId ID of work order
     * @param pay         Pay
     */
    public static void updatePay(Context context, int workOrderId, Pay pay) {
    }

    /**
     * Adds a task to a work order
     *
     * @param workOrderId Work order id
     * @param json        JSON Model
     */
    public static void addTask(Context context, int workOrderId, Task json) {
    }

    /**
     * Get a list of a work order's tasks
     *
     * @param workOrderId Work order id
     */
    public static void getTasks(Context context, int workOrderId) {
    }

    /**
     * Get the milestones of a work order
     *
     * @param workOrderId ID of Work Order
     */
    public static void getMilestones(Context context, int workOrderId) {
    }

    /**
     * Add signature by work order
     *
     * @param workOrderId ID of work order
     * @param signature   Signature JSON
     */
    public static void addSignature(Context context, int workOrderId, Signature signature) {
    }

    /**
     * Returns a list of signatures uploaded by the assigned provider
     *
     * @param workOrderId ID of work order
     */
    public static void getSignatures(Context context, int workOrderId) {
    }

    /**
     * Gets list of providers available for a work order
     *
     * @param workOrderId ID of work order
     */
    public static void getProviders(Context context, String workOrderId) {
    }

    /**
     * Adds a message to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     */
    public static void addMessage(Context context, String workOrderId, Message json) {
    }

    /**
     * Gets a list of work order messages
     *
     * @param workOrderId ID of work order
     */
    public static void getMessages(Context context, String workOrderId) {
    }

    /**
     * Cancel work order swap request.
     */
    public static void cancelSwapRequest(Context context) {
    }

    /**
     * Add time log for work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog     Check in information
     */
    public static void addTimeLog(Context context, int workOrderId, TimeLog timeLog) {
    }

    /**
     * Returns a list of time logs applied by the assigned provider
     *
     * @param workOrderId ID of work order
     */
    public static void getTimeLogs(Context context, int workOrderId) {
    }

    /**
     * Update all time logs for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param timeLog     Check in information
     */
    public static void updateAllTimeLogs(Context context, int workOrderId, TimeLog timeLog) {
    }

    /**
     * Gets a work order by its id
     *
     * @param workOrderId ID of work order
     */
    public static void getWorkOrder(Context context, int workOrderId) {
    }

    /**
     * Deletes a work order by its id
     *
     * @param workOrderId  ID of work order
     * @param cancellation Cancellation reasons
     */
    public static void deleteWorkOrder(Context context, int workOrderId, Cancellation cancellation) {
    }

    /**
     * Updates a work order by its id
     *
     * @param workOrderId ID of work order
     * @param workOrder   Work order model
     */
    public static void updateWorkOrder(Context context, int workOrderId, WorkOrder workOrder) {
    }

    /**
     * Gets a single signature uploaded by the assigned provider
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
     */
    public static void getSignature(Context context, int workOrderId, int signatureId) {
    }

    /**
     * Delete signature by work order and signature
     *
     * @param workOrderId ID of work order
     * @param signatureId ID of signature
     */
    public static void deleteSignature(Context context, int workOrderId, int signatureId) {
    }

    /**
     * Adds an attachment folder
     *
     * @param workOrderId Work order id
     * @param folder      Folder
     */
    public static void addFolder(Context context, int workOrderId, AttachmentFolder folder) {
    }

    /**
     * Gets a list of attachment folders which contain files and deliverables for the work order
     *
     * @param workOrderId Work order id
     */
    public static void getAttachments(Context context, int workOrderId) {
    }

    /**
     * Completes a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void completeTask(Context context, int workOrderId, int taskId) {
    }

    /**
     * Allows an assigned provider to removes a discount they previously applied from a work order, increasing the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param discountId  ID of the discount
     */
    public static void removeDiscount(Context context, int workOrderId, int discountId) {
    }

    /**
     * Updates the amount or description of a discount applied to the work order.
     *
     * @param workOrderId ID of work order
     * @param discountId  ID of the discount
     * @param json        Payload of the discount
     */
    public static void updateDiscount(Context context, int workOrderId, int discountId, PayModifier json) {
    }

    /**
     * Remove time log for assigned work order
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     */
    public static void removeTimeLog(Context context, int workOrderId, int workorderHoursId) {
    }

    /**
     * Update time log for assigned work order.
     *
     * @param workOrderId      ID of work order
     * @param workorderHoursId ID of work order hour
     * @param timeLog          Check in information
     */
    public static void updateTimeLog(Context context, int workOrderId, int workorderHoursId, TimeLog timeLog) {
    }

    /**
     * Gets an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     */
    public static void getFile(Context context, int workOrderId, int folderId, int attachmentId) {
    }

    /**
     * Deletes an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     */
    public static void deleteAttachment(Context context, int workOrderId, int folderId, int attachmentId) {
    }

    /**
     * Updates an attachment folder and its contents
     *
     * @param workOrderId  Work order id
     * @param folderId     Folder id
     * @param attachmentId File id
     * @param attachment   Attachment
     */
    public static void updateAttachment(Context context, int workOrderId, int folderId, int attachmentId, Attachment attachment) {
    }

    /**
     * Assign a user to a work order
     *
     * @param workOrderId Work order id
     * @param assignee    JSON Model
     */
    public static void assignUser(Context context, int workOrderId, Assignee assignee) {
    }

    /**
     * Get assignee of a work order
     *
     * @param workOrderId Work order id
     */
    public static void getAssignee(Context context, int workOrderId) {
    }

    /**
     * Unassign user from a work order
     *
     * @param workOrderId Work order id
     * @param assignee    JSON Model
     */
    public static void unassignUser(Context context, int workOrderId, Assignee assignee) {
    }

    /**
     * Publishes a work order to the marketplace where it can garner requests.
     *
     * @param workOrderId ID of work order
     */
    public static void publish(Context context, int workOrderId) {
    }

    /**
     * Unpublishes a work order from the marketplace so that no requests or counter-offers can be made. Moves to draft unless it was also routed.
     *
     * @param workOrderId ID of work order
     */
    public static void unpublish(Context context, int workOrderId) {
    }

    /**
     * Gets the current real-time status for a work order
     *
     * @param workOrderId ID of work order
     */
    public static void getStatus(Context context, int workOrderId) {
    }

    /**
     * Approves a completed work order and moves it to paid status
     *
     * @param workOrderId ID of work order
     */
    public static void approveWorkOrder(Context context, int workOrderId) {
    }

    /**
     * Unapproves a completed work order and moves it to work done status
     *
     * @param workOrderId ID of work order
     */
    public static void unapproveWorkOrder(Context context, int workOrderId) {
    }

    /**
     * Accept pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void acceptIncrease(Context context, int workOrderId, int increaseId) {
    }

    /**
     * Deletes a shipment from a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId  Shipment id
     */
    public static void deleteShipment(Context context, int workOrderId, int shipmentId) {
    }

    /**
     * Updates a shipment attached to a work order
     *
     * @param workOrderId Work order id
     * @param shipmentId  Shipment id
     * @param shipment    Shipment
     */
    public static void updateShipment(Context context, int workOrderId, int shipmentId, Shipment shipment) {
    }

    /**
     * Adds a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     */
    public static void addPenalty(Context context, int workOrderId, int penaltyId) {
    }

    /**
     * Gets a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     */
    public static void getPenalty(Context context, int workOrderId, int penaltyId) {
    }

    /**
     * Removes a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId ID of Work Order
     * @param penaltyId   Penalty ID
     */
    public static void removePenalty(Context context, int workOrderId, int penaltyId) {
    }

    /**
     * Updates a penalty option which would allow the raising of the amount paid to the provider if a condition being met.
     *
     * @param workOrderId Work Order ID
     * @param penaltyId   Penalty ID
     */
    public static void updatePenalty(Context context, int workOrderId, int penaltyId) {
    }

    /**
     * Decline work order swap request.
     */
    public static void declineSwapRequest(Context context) {
    }

    /**
     * Reply a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void replyMessage(Context context, String workOrderId, String messageId, Message json) {
    }

    /**
     * Removes a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     */
    public static void removeMessage(Context context, String workOrderId, String messageId) {
    }

    /**
     * Updates a message on a work order
     *
     * @param workOrderId ID of work order
     * @param messageId   ID of work order message
     * @param json        JSON payload
     */
    public static void updateMessage(Context context, String workOrderId, String messageId, Message json) {
    }

    /**
     * Get a task by work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void getTask(Context context, int workOrderId, int taskId) {
    }

    /**
     * Remove a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void removeTask(Context context, int workOrderId, int taskId) {
    }

    /**
     * Updates a work order's task
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param json        JSON Model
     */
    public static void updateTask(Context context, int workOrderId, int taskId, Task json) {
    }

    /**
     * Deny pay increase for assigned work order.
     *
     * @param workOrderId ID of work order
     * @param increaseId  ID of work order increase
     */
    public static void denyIncrease(Context context, int workOrderId, int increaseId) {
    }

    /**
     * Sets up an alert to be fired upon the completion of a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param json        JSON Model
     */
    public static void addAlertToWorkOrderAndTask(Context context, int workOrderId, int taskId, TaskAlert json) {
    }

    /**
     * Removes all alerts associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     */
    public static void removeAlerts(Context context, int workOrderId, int taskId) {
    }

    /**
     * Request or un-hide a request for a work order
     *
     * @param workOrderId Work order id
     * @param request     JSON Model
     */
    public static void request(Context context, int workOrderId, Request request) {
    }

    /**
     * Removes or hides a request by a user from a work order
     *
     * @param workOrderId Work order id
     * @param request     JSON Model
     */
    public static void removeRequest(Context context, int workOrderId, Request request) {
    }

    /**
     * Accept work order swap request.
     */
    public static void acceptSwapRequest(Context context) {
    }

    /**
     * Get a list of custom fields and their values for a work order.
     *
     * @param workOrderId ID of work order
     */
    public static void getCustomFields(Context context, int workOrderId) {
    }

    /**
     * Removes a single alert associated with a single task on a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param alertId     Alert id
     */
    public static void removeAlert(Context context, int workOrderId, int taskId, int alertId) {
    }

    /**
     * Gets the service schedule for a work order
     *
     * @param workOrderId ID of work order
     */
    public static void getSchedule(Context context, int workOrderId) {
    }

    /**
     * Updates the service schedule or eta of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param schedule    JSON Payload
     */
    public static void updateSchedule(Context context, int workOrderId, Schedule schedule) {
    }

    /**
     * Updates any holds on a work order.
     *
     * @param workOrderId ID of work order
     * @param holds       Holds
     */
    public static void updateHolds(Context context, int workOrderId, String holds) {
    }

    /**
     * Resolve or Reopen a problem reported to work order
     *
     * @param workOrderId ID of work order
     * @param flagId      ID of report problem flag
     * @param json        JSON payload
     */
    public static void resolveReopenReportProblem(Context context, int workOrderId, int flagId, Message json) {
    }

    /**
     * Assigned provider route to adds and apply a discount to a work order which reduces the amount they will be paid.
     *
     * @param workOrderId ID of work order
     * @param json        Payload of the discount
     */
    public static void addDiscount(Context context, int workOrderId, PayModifier json) {
    }

    /**
     * Returns a list of discounts applied by the assigned provider to reduce the payout of the work order.
     *
     * @param workOrderId ID of work order
     */
    public static void getDiscounts(Context context, int workOrderId) {
    }

    /**
     * Reorders a task associated with a work order to a position before or after a target task
     *
     * @param workOrderId  Work order id
     * @param taskId       Task id
     * @param targetTaskId Target task id
     * @param position     before or after target task
     */
    public static void reorderTask(Context context, int workOrderId, int taskId, int targetTaskId, String position) {
    }

    /**
     * Regroups a task associated with a work order
     *
     * @param workOrderId Work order id
     * @param taskId      Task id
     * @param group       New group
     * @param destination beginning or end (position in new group)
     */
    public static void groupTask(Context context, int workOrderId, int taskId, String group, String destination) {
    }

    /**
     * Adds a contact to a work order
     *
     * @param workOrderId Work order id
     * @param json        JSON Model
     */
    public static void addContact(Context context, int workOrderId, Contact json) {
    }

    /**
     * Get a list of contacts on a work order
     *
     * @param workOrderId Work order id
     */
    public static void getContacts(Context context, int workOrderId) {
    }

    /**
     * Adds a shipment to a work order
     *
     * @param workOrderId Work order id
     * @param shipment    Shipment
     */
    public static void addShipment(Context context, int workOrderId, Shipment shipment) {
    }

    /**
     * Get a list of shipments on a work order
     *
     * @param workOrderId Work order id
     */
    public static void getShipments(Context context, int workOrderId) {
    }

    /**
     * Get a list of penalties and their applied status for a work order
     *
     * @param workOrderId Work Order ID
     */
    public static void getPenalties(Context context, int workOrderId) {
    }

    /**
     * Route a user to a work order
     *
     * @param workOrderId Work order id
     * @param route       JSON Model
     */
    public static void routeUser(Context context, int workOrderId, Route route) {
    }

    /**
     * Unroute a user from a work order
     *
     * @param workOrderId Work order id
     * @param route       JSON Model
     */
    public static void unRouteUser(Context context, int workOrderId, Route route) {
    }

    /**
     * Pre-filters results by a certain category or type, settings by list are persisted with 'sticky' by user.
     */
    public static void getWorkOrderLists(Context context) {
    }

    /**
     * Gets list of problem reasons by work order
     *
     * @param workOrderId ID of work order
     */
    public static void getProblemReasons(Context context, String workOrderId) {
    }

    /**
     * Report a problem to a work order
     *
     * @param workOrderId ID of work order
     * @param json        JSON payload
     */
    public static void reportProblem(Context context, String workOrderId, Message json) {
    }

    /**
     * Get a list of available bonuses on a work order that can be applied to increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId Work Order ID
     */
    public static void getBonuses(Context context, int workOrderId) {
    }

    /**
     * Gets the address and geo information for a work order
     *
     * @param workOrderId ID of work order
     */
    public static void getLocation(Context context, int workOrderId) {
    }

    /**
     * Updates the location of a work order (depending on your role)
     *
     * @param workOrderId ID of work order
     * @param location    JSON Payload
     */
    public static void updateLocation(Context context, int workOrderId, Location location) {
    }

    /**
     * Adds a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId     Bonus ID
     */
    public static void addBonus(Context context, int workOrderId, int bonusId) {
    }

    /**
     * Gets a bonus for a work order
     *
     * @param workOrderId ID of work order
     * @param bonusId     Bonus ID
     */
    public static void getBonus(Context context, int workOrderId, int bonusId) {
    }

    /**
     * Removes a bonus from a work order
     *
     * @param workOrderId Work Order ID
     * @param bonusId     Bonus ID
     */
    public static void removeBonus(Context context, int workOrderId, int bonusId) {
    }

    /**
     * Updates a bonus on a work order which can conditionally increase the amount paid to the provider upon conditions being met
     *
     * @param workOrderId ID of work order
     * @param bonusId     Bonus ID
     * @param bonus       Bonus
     */
    public static void updateBonus(Context context, int workOrderId, int bonusId, PayModifier bonus) {
    }

}

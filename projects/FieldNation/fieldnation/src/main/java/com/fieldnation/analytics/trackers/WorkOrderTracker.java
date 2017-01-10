package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.EventCategory;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.workorder.TaskType;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;

/**
 * Created by mc on 1/5/17.
 */

public class WorkOrderTracker {
    private static final String SCREEN_WORK_ORDER_DETAILS = "Work Order Details";
    private static final String SCREEN_WORK_ORDER_MESSAGES = "Work Order Messages";
    private static final String SCREEN_WORK_ORDER_ATTACHMENTS = "Work Order Attachments";
    private static final String SCREEN_WORK_ORDER_NOTIFICATIONS = "Work Order Notifications";

    public enum Action {
        ACK_HOLD("Acknowledge Hold"),
        MARK_COMPLETE("Mark Complete"),
        MARK_INCOMPLETE("Mark Incomplete"),
        READY_TO_GO("Ready To Go"),
        WITHDRAW("Withdraw"),
        UNIQUE_TASK("Unique Task"),
        ON_MY_WAY("On My Way");

        private String action;
        private String category = EventCategory.WORK_ORDER;

        Action(String action) {
            this.action = action;
        }
    }

    public enum Identity {
        // Action
        CHECK_IN_ACTION_BUTTON("Check In Action"),
        CHECK_IN_AGAIN_ACTION_BUTTON("Check In Again Action"),
        CHECK_OUT_ACTION_BUTTON("Check Out Action"),
        VIEW_COUNTER_OFFER_ACTION_BUTTON("View Counter Offer Action"),
        COUNTER_OFFER_ACTION_BUTTON("Counter Offer Action"),
        CLOSING_NOTE_ACTION_BUTTON("Closing Note Action"),
        CONFIRM_ACTION_BUTTON("Confirm Action"),
        ACKNOWLEDGE_HOLD_ACTION_BUTTON("Acknowledge Hold Action"),
        MARK_COMPLETE_ACTION_BUTTON("Mark Complete Action"),
        MARK_INCOMPLETE_ACTION_BUTTON("Mark Incomplete Action"),
        ACCEPT_ACTION_BUTTON("Accept Action"),
        REQUEST_ACTION_BUTTON("Request Action"),
        NOT_INTERESTED_ACTION_BUTTON("Not Interested Action"),
        READY_TO_GO_ACTION_BUTTON("Ready To Go Action"),
        REPORT_PROBLEM_ACTION_BUTTON("Report Problem Action"),
        WITHDRAW_ACTION_BUTTON("Withdraw Action"),
        VIEW_PAYMENT_ACTION_BUTTON("View Payment Action"),
        RUNNING_LATE_ACTION_BUTTON("Running Late Action"),
        ON_MY_WAY_ACTION_BUTTON("On My Way Action"),
        VIEW_BUNDLE_ACTION_BUTTON("View Bundle Action"),
        CALL_BUYER_ACTION_BUTTON("Call Buyer Action"),
        VIEW_MESSAGES_ACTION_BUTTON("View Messages Action"),
        VIEW_DIRECTIONS_ACTION_BUTTON("View Directions Action"),

        // Add
        TIME_LOG_ADD_BUTTON("Time Log Add"),
        SIGNATURE_ADD_BUTTON("Signature Add"),
        EXPENSE_ADD_BUTTON("Expense Add"),
        DISCOUNT_ADD_BUTTON("Discount Add"),
        CLOSING_NOTE_ADD_BUTTON("Closing Note Add"),
        SHIPMENT_ADD_BUTTON("Shipment Add"),

        // Edit
        TIME_LOG_EDIT_ITEM("Time Log Edit"),
        SIGNATURE_EDIT_ITEM("Signature Edit"),
        EXPENSE_EDIT_ITEM("Expense Edit"),
        DISCOUNT_EDIT_ITEM("Discount Edit"),
        CLOSING_NOTE_EDIT_ITEM("Closing Note Edit"),
        SHIPMENT_EDIT_ITEM("Shipment Edit"),

        // Delete
        TIME_LOG_DELETE_ITEM("Time Log Delete"),
        SIGNATURE_DELETE_ITEM("Signature Delete"),
        EXPENSE_DELETE_ITEM("Expense Delete"),
        DISCOUNT_DELETE_ITEM("Discount Delete"),
        SHIPMENT_DELETE_ITEM("Shipment Delete"),

        // Task
        CONFIRM_TASK("Confirm Assignment Task"),
        CLOSING_NOTE_TASK("Closing Note Task"),
        CHECK_IN_TASK("Check In Task"),
        CHECK_OUT_TASK("Check Out Task"),
        UPLOAD_DOCUMENT_TASK("Upload Document Task"),
        UPLOAD_PICTURE_TASK("Upload Picture Task"),
        CUSTOM_FIELD_TASK("Custom Field Task"),
        CALL_NUMBER_TASK("Call Number Task"),
        SEND_EMAIL_TASK("Send Email Task"),
        UNIQUE_TASK("Unique Task"),
        COLLECT_SIGNATURE_TASK("Collect Signature Task"),
        COLLECT_SHIPMENT_TASK("Collect Shipment Task"),
        DOWNLOAD_FILE_TASK("Download File Task"),

        // Description Modal
        CONFIDENTIAL_INFORMATION_BUTTON("Confidential Information"),
        CUSTOMER_POLICIES_BUTTON("Customer Policies"),
        STANDARD_INSTRUCTIONS_BUTTON("Standard Instructions");

        private String identity;

        Identity(String identity) {
            this.identity = identity;
        }

        public String getElementType() {
            switch (this) {
                case CHECK_IN_ACTION_BUTTON:
                case CHECK_IN_AGAIN_ACTION_BUTTON:
                case CHECK_OUT_ACTION_BUTTON:
                case VIEW_COUNTER_OFFER_ACTION_BUTTON:
                case COUNTER_OFFER_ACTION_BUTTON:
                case CLOSING_NOTE_ACTION_BUTTON:
                case CONFIRM_ACTION_BUTTON:
                case ACKNOWLEDGE_HOLD_ACTION_BUTTON:
                case MARK_COMPLETE_ACTION_BUTTON:
                case MARK_INCOMPLETE_ACTION_BUTTON:
                case ACCEPT_ACTION_BUTTON:
                case REQUEST_ACTION_BUTTON:
                case NOT_INTERESTED_ACTION_BUTTON:
                case READY_TO_GO_ACTION_BUTTON:
                case REPORT_PROBLEM_ACTION_BUTTON:
                case WITHDRAW_ACTION_BUTTON:
                case VIEW_PAYMENT_ACTION_BUTTON:
                case TIME_LOG_ADD_BUTTON:
                case SIGNATURE_ADD_BUTTON:
                case EXPENSE_ADD_BUTTON:
                case DISCOUNT_ADD_BUTTON:
                case CLOSING_NOTE_ADD_BUTTON:
                case SHIPMENT_ADD_BUTTON:
                case CONFIDENTIAL_INFORMATION_BUTTON:
                case CUSTOMER_POLICIES_BUTTON:
                case STANDARD_INSTRUCTIONS_BUTTON:
                    return ElementType.BUTTON;

                case TIME_LOG_EDIT_ITEM:
                case SIGNATURE_EDIT_ITEM:
                case EXPENSE_EDIT_ITEM:
                case DISCOUNT_EDIT_ITEM:
                case CLOSING_NOTE_EDIT_ITEM:
                case SHIPMENT_EDIT_ITEM:
                case CONFIRM_TASK:
                case CLOSING_NOTE_TASK:
                case CHECK_IN_TASK:
                case CHECK_OUT_TASK:
                case UPLOAD_DOCUMENT_TASK:
                case UPLOAD_PICTURE_TASK:
                case CUSTOM_FIELD_TASK:
                case CALL_NUMBER_TASK:
                case SEND_EMAIL_TASK:
                case UNIQUE_TASK:
                case COLLECT_SIGNATURE_TASK:
                case COLLECT_SHIPMENT_TASK:
                case DOWNLOAD_FILE_TASK:
                    return ElementType.LIST_ITEM;
            }
            return null;
        }

        public static Identity fromTaskType(TaskType taskType) {
            switch (taskType) {
                case CONFIRM_ASSIGNMENT:
                    return CONFIRM_TASK;
                case CLOSE_OUT_NOTES:
                    return CLOSING_NOTE_TASK;
                case CHECKIN:
                    return CHECK_IN_TASK;
                case CHECKOUT:
                    return CHECK_OUT_TASK;
                case UPLOAD_FILE:
                    return UPLOAD_DOCUMENT_TASK;
                case UPLOAD_PICTURE:
                    return UPLOAD_PICTURE_TASK;
                case CUSTOM_FIELD:
                    return CUSTOM_FIELD_TASK;
                case PHONE:
                    return CALL_NUMBER_TASK;
                case EMAIL:
                    return SEND_EMAIL_TASK;
                case UNIQUE_TASK:
                    return UNIQUE_TASK;
                case SIGNATURE:
                    return COLLECT_SIGNATURE_TASK;
                case SHIPMENT_TRACKING:
                    return COLLECT_SHIPMENT_TASK;
                case DOWNLOAD:
                    return DOWNLOAD_FILE_TASK;
            }
            return null;
        }
    }

    public enum ModalType {
        CONFIDENTIAL_INFORMATION, CUSTOMER_POLICIES, STANDARD_INSTRUCTIONS;

        public Identity getIdentity() {
            switch (this) {
                case CONFIDENTIAL_INFORMATION:
                    return Identity.CONFIDENTIAL_INFORMATION_BUTTON;
                case CUSTOMER_POLICIES:
                    return Identity.CUSTOMER_POLICIES_BUTTON;
                case STANDARD_INSTRUCTIONS:
                    return Identity.STANDARD_INSTRUCTIONS_BUTTON;
            }
            return null;
        }
    }

    public enum WorkOrderDetailsSection {
        TIME_LOGGED, SIGNATURES, EXPENSES, DISCOUNTS, SHIPMENTS, CLOSING_NOTES;

        public Identity getEditIdentity() {
            switch (this) {
                case TIME_LOGGED:
                    return Identity.TIME_LOG_EDIT_ITEM;
                case EXPENSES:
                    return Identity.EXPENSE_EDIT_ITEM;
                case DISCOUNTS:
                    return Identity.DISCOUNT_EDIT_ITEM;
                case SHIPMENTS:
                    return Identity.SHIPMENT_EDIT_ITEM;
                case CLOSING_NOTES:
                    return Identity.CLOSING_NOTE_EDIT_ITEM;
            }
            return null;
        }

        public Identity getAddIdentity() {
            switch (this) {
                case TIME_LOGGED:
                    return Identity.TIME_LOG_ADD_BUTTON;
                case SIGNATURES:
                    return Identity.SIGNATURE_ADD_BUTTON;
                case EXPENSES:
                    return Identity.EXPENSE_ADD_BUTTON;
                case DISCOUNTS:
                    return Identity.DISCOUNT_ADD_BUTTON;
                case SHIPMENTS:
                    return Identity.SHIPMENT_ADD_BUTTON;
                case CLOSING_NOTES:
                    return Identity.CLOSING_NOTE_ADD_BUTTON;
            }
            return null;
        }

        public Identity getDeleteIdentity() {
            switch (this) {
                case TIME_LOGGED:
                    return Identity.TIME_LOG_DELETE_ITEM;
                case SIGNATURES:
                    return Identity.SIGNATURE_DELETE_ITEM;
                case EXPENSES:
                    return Identity.EXPENSE_DELETE_ITEM;
                case DISCOUNTS:
                    return Identity.DISCOUNT_DELETE_ITEM;
                case SHIPMENTS:
                    return Identity.SHIPMENT_DELETE_ITEM;
            }
            return null;
        }
    }

    public enum Tab {
        DETAILS("Work Order Details"),
        MESSAGES("Work Order Messages"),
        ATTACHMENTS("Work Order Attachments"),
        NOTIFICATIONS("Work Order Notifications");

        private String tab;

        Tab(String tab) {
            this.tab = tab;
        }
    }

    public enum ActionButton {
        CHECK_IN, CHECK_IN_AGAIN, CHECK_OUT, VIEW_COUNTER_OFFER, COUNTER_OFFER, CLOSING_NOTES,
        CONFIRM, ACCEPT_WORK, REQUEST, NOT_INTERESTED, REPORT_PROBLEM, VIEW_PAYMENT, ACKNOWLEDGE_HOLD,
        MARK_COMPlETE, MARK_INCOMPLETE, READY_TO_GO, WITHDRAW, RUNNING_LATE, ON_MY_WAY, VIEW_BUNDLE,
        CALL_BUYER, VIEW_MESSAGES, DIRECTIONS;

        public Identity getIdentity() {
            switch (this) {
                case CHECK_IN:
                    return Identity.CHECK_IN_ACTION_BUTTON;
                case CHECK_IN_AGAIN:
                    return Identity.CHECK_IN_ACTION_BUTTON;
                case CHECK_OUT:
                    return Identity.CHECK_OUT_ACTION_BUTTON;
                case VIEW_COUNTER_OFFER:
                    return Identity.VIEW_COUNTER_OFFER_ACTION_BUTTON;
                case COUNTER_OFFER:
                    return Identity.COUNTER_OFFER_ACTION_BUTTON;
                case CLOSING_NOTES:
                    return Identity.CLOSING_NOTE_ACTION_BUTTON;
                case CONFIRM:
                    return Identity.CONFIRM_ACTION_BUTTON;
                case ACCEPT_WORK:
                    return Identity.ACCEPT_ACTION_BUTTON;
                case REQUEST:
                    return Identity.REQUEST_ACTION_BUTTON;
                case NOT_INTERESTED:
                    return Identity.NOT_INTERESTED_ACTION_BUTTON;
                case REPORT_PROBLEM:
                    return Identity.REPORT_PROBLEM_ACTION_BUTTON;
                case VIEW_PAYMENT:
                    return Identity.VIEW_PAYMENT_ACTION_BUTTON;
                case ACKNOWLEDGE_HOLD:
                    return Identity.ACKNOWLEDGE_HOLD_ACTION_BUTTON;
                case MARK_COMPlETE:
                    return Identity.MARK_COMPLETE_ACTION_BUTTON;
                case MARK_INCOMPLETE:
                    return Identity.MARK_INCOMPLETE_ACTION_BUTTON;
                case READY_TO_GO:
                    return Identity.READY_TO_GO_ACTION_BUTTON;
                case WITHDRAW:
                    return Identity.WITHDRAW_ACTION_BUTTON;
                case RUNNING_LATE:
                    return Identity.RUNNING_LATE_ACTION_BUTTON;
                case ON_MY_WAY:
                    return Identity.ON_MY_WAY_ACTION_BUTTON;
                case VIEW_BUNDLE:
                    return Identity.VIEW_BUNDLE_ACTION_BUTTON;
                case CALL_BUYER:
                    return Identity.CALL_BUYER_ACTION_BUTTON;
                case VIEW_MESSAGES:
                    return Identity.VIEW_MESSAGES_ACTION_BUTTON;
                case DIRECTIONS:
                    return Identity.VIEW_DIRECTIONS_ACTION_BUTTON;
            }
            return null;
        }

        public Action getAction() {
            switch (this) {
                case ACKNOWLEDGE_HOLD:
                    return Action.ACK_HOLD;
                case MARK_COMPlETE:
                    return Action.MARK_COMPLETE;
                case MARK_INCOMPLETE:
                    return Action.MARK_INCOMPLETE;
                case READY_TO_GO:
                    return Action.READY_TO_GO;
                case WITHDRAW:
                    return Action.WITHDRAW;
                case ON_MY_WAY:
                    return Action.ON_MY_WAY;
            }
            return null;
        }
    }

    public static void onShow(Context context, Tab tab, long workOrderId) {
        Tracker.screen(context,
                new Screen.Builder()
                        .tag(SnowplowWrapper.TAG)
                        .name(tab.tab)
                        .addContext(new SpWorkOrderContext.Builder()
                                .workOrderId(workOrderId)
                                .build())
                        .addContext(new SpUIContext.Builder()
                                .page(tab.tab)
                                .build())
                        .build());
    }

    public static void onTabSwitchEvent(Context context, Tab currentTab, Tab newTab) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(currentTab.tab)
                        .elementAction(ElementAction.CLICK)
                        .elementIdentity(newTab.tab)
                        .elementType(ElementType.TAB)
                        .build())
                .build());
    }

    public static void onActionButtonEvent(Context context, ActionButton actionButton, Long workOrderId) {
        Identity identity = actionButton.getIdentity();
        if (identity != null) {
            Action action = actionButton.getAction();
            if (action != null && workOrderId != null) {
                onEvent(context, identity, action, workOrderId);
            } else {
                navigationEvent(context, Tab.DETAILS, identity);
            }
        }
    }

    public static void onActionButtonEvent(Context context, String searchTitle, ActionButton actionButton) {
        Identity identity = actionButton.getIdentity();

        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(searchTitle + " Saved Search")
                        .elementAction(ElementAction.CLICK)
                        .elementIdentity(identity.identity)
                        .elementType(identity.getElementType())
                        .build())
                .build());
    }

    public static void onAddEvent(Context context, WorkOrderDetailsSection section) {
        Identity identity = section.getAddIdentity();
        if (identity != null)
            navigationEvent(context, Tab.DETAILS, identity);
    }

    public static void onEditEvent(Context context, WorkOrderDetailsSection section) {
        Identity identity = section.getEditIdentity();
        if (identity != null)
            navigationEvent(context, Tab.DETAILS, identity);
    }

    public static void onDeleteEvent(Context context, WorkOrderDetailsSection section) {
        Identity identity = section.getDeleteIdentity();
        if (identity != null)
            navigationEvent(context, Tab.DETAILS, identity);
    }

    public static void onDescriptionModalEvent(Context context, ModalType modalType) {
        navigationEvent(context, Tab.DETAILS, modalType.getIdentity());
    }

    public static void onTaskEvent(Context context, TaskType taskType, Long workOrderId) {
        Identity identity = Identity.fromTaskType(taskType);

        if (identity != null) {
            if (taskType == TaskType.UNIQUE_TASK && workOrderId != null) {
                onEvent(context, identity, Action.UNIQUE_TASK, workOrderId);
            } else {
                navigationEvent(context, Tab.DETAILS, identity);
            }
        }
    }

    public static void directionsEvent(Context context) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(Tab.DETAILS.tab)
                        .elementAction(ElementAction.CLICK)
                        .elementIdentity("Directions")
                        .elementType(ElementType.LIST_ITEM)
                        .build())
                .build());
    }

    private static void navigationEvent(Context context, Tab tab, Identity identity) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(tab.tab)
                        .elementAction(ElementAction.CLICK)
                        .elementIdentity(identity.identity)
                        .elementType(identity.getElementType())
                        .build())
                .build());
    }

    public static void onEvent(Context context, Identity identity, Action action, long workOrderId) {
        Tracker.event(context, new SimpleEvent.Builder()
                .action(action.action)
                .category(action.category)
                .property("workorder_id")
                .label(workOrderId + "")
                .addContext(new SpWorkOrderContext.Builder()
                        .workOrderId(workOrderId)
                        .build())
                .addContext(new SpUIContext.Builder()
                        .elementIdentity(identity.identity)
                        .elementType(identity.getElementType())
                        .elementAction(ElementAction.CLICK)
                        .page(SCREEN_WORK_ORDER_DETAILS)
                        .build())
                .build());
    }

    public static void test(Context context) {
        SavedSearchParams[] list = SavedSearchClient.defaults;

        for (Tab tab : Tab.values()) {
            onShow(context, tab, 1);
        }
        for (Tab tab1 : Tab.values()) {
            for (Tab tab2 : Tab.values()) {
                if (tab1 != tab2)
                    onTabSwitchEvent(context, tab1, tab2);
            }
        }

        for (ActionButton ab : ActionButton.values()) {
            onActionButtonEvent(context, ab, 1L);
            for (SavedSearchParams p : list) {
                onActionButtonEvent(context, p.title, ab);
            }
        }

        for (WorkOrderDetailsSection section : WorkOrderDetailsSection.values()) {
            onAddEvent(context, section);
            onEditEvent(context, section);
            onDeleteEvent(context, section);
        }

        for (ModalType mt : ModalType.values()) {
            onDescriptionModalEvent(context, mt);
        }

        for (TaskType tt : TaskType.values()) {
            onTaskEvent(context, tt, 1L);
        }

        directionsEvent(context);

        for (Identity identity : Identity.values()) {
            for (Action action : Action.values()) {
                onEvent(context, identity, action, 1L);
            }
        }
    }
}
package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.EventCategory;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/5/17.
 */

public class WorkOrderTracker {
    private static final Screen SCREEN = new Screen.Builder().name("Work Order Details").tag(SnowplowWrapper.TAG).build();

    public enum Action {
        ACK_HOLD("Acknowledge Hold"),
        MARK_COMPLETE("Mark Complete"),
        MARK_INCOMPLETE("Mark Incomplete"),
        READY_TO_GO("Ready To Go"),
        WITHDRAW("Withdraw"),
        UNIQUE_TASK("Unique Task");

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
        REPORT_PROBLEM_ACTION_BUTTON("Report Probem Action"),
        WITHDRAW_ACTION_BUTTON("Withdraw Action"),
        VIEW_PAYMENT_ACTION_BUTTON("View Payment Action"),

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
        private String elementType;

        Identity(String identity) {
            this.identity = identity;

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
                    elementType = ElementType.BUTTON;
                    break;

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
                    elementType = ElementType.LIST_ITEM;
                    break;
            }
        }
    }

    public static void onShow(Context context, long workOrderId) {
        Tracker.screen(context,
                new Screen.Builder()
                        .tag(SnowplowWrapper.TAG)
                        .name("Work Order Details")
                        .addContext(new SpWorkOrderContext.Builder()
                                .workOrderId(workOrderId)
                                .build())
                        .build());
    }

    public static void onEvent(Context context, Action action, Identity identity, long workOrderId) {
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
                        .elementType(identity.elementType)
                        .elementAction(ElementAction.CLICK)
                        .page(SCREEN.name)
                        .build())
                .build());
    }


}

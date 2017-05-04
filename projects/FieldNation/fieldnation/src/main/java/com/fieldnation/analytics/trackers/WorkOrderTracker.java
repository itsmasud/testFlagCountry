package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.EventCategory;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;
import com.fieldnation.v2.data.model.TaskType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 1/5/17.
 */

public class WorkOrderTracker {
    private static final String SCREEN_WORK_ORDER_DETAILS = "Work Order Details";
    private static final String SCREEN_WORK_ORDER_MESSAGES = "Work Order Messages";
    private static final String SCREEN_WORK_ORDER_ATTACHMENTS = "Work Order Attachments";
    private static final String SCREEN_WORK_ORDER_NOTIFICATIONS = "Work Order Notifications";


    public static class Action implements TrackerBase.Action, Cloneable {
        private static List<Action> valuesList = new LinkedList<>();
        private static Action[] valuesArray;

        public static final Action MARK_COMPLETE = new Action("Mark Complete", true);
        public static final Action MARK_INCOMPLETE = new Action("Mark Incomplete", true);
        public static final Action READY_TO_GO = new Action("Ready To Go", true);
        public static final Action WITHDRAW = new Action("Withdraw", true);
        public static final Action UNIQUE_TASK = new Action("Unique Task", true);
        public static final Action ON_MY_WAY = new Action("On My Way", true);
        public static final Action CHECK_IN = new Action("Check In", true);
        public static final Action CHECK_IN_AGAIN = new Action("Check In Again", true);
        public static final Action CHECK_OUT = new Action("Check Out", true);
        public static final Action VIEW_COUNTER_OFFER = new Action("View Counter Offer", true);
        public static final Action COUNTER_OFFER = new Action("Counter Offer", true);
        public static final Action CLOSING_NOTES = new Action("Closing Notes", true);
        public static final Action CONFIRM = new Action("Confirm", true);
        public static final Action ETA = new Action("Eta", true);
        public static final Action ACCEPT_WORK = new Action("Accept Work", true);
        public static final Action REQUEST = new Action("Request", true);
        public static final Action NOT_INTERESTED = new Action("Not Interested", true);
        public static final Action REPORT_PROBLEM = new Action("Report Problem", true);
        public static final Action VIEW_PAYMENT = new Action("View Payment", true);
        public static final Action ACKNOWLEDGE_HOLD = new Action("Acknowledge Hold", true);
        public static final Action RUNNING_LATE = new Action("Running Late", true);
        public static final Action VIEW_BUNDLE = new Action("View Bundle", true);
        public static final Action CALL_BUYER = new Action("Call Buyer", true);
        public static final Action VIEW_MESSAGES = new Action("View Messages", true);
        public static final Action DIRECTIONS = new Action("Directions", true);

        private String action;

        private Action(String action, boolean internal) {
            this.action = action;
            valuesList.add(this);
        }

        public Action(String action) {
            this.action = action;
        }

        @Override
        public String action() {
            return action;
        }

        @Override
        public String category() {
            return EventCategory.WORK_ORDER;
        }

        public Action clone() {
            try {
                return (Action) super.clone();
            } catch (Exception ex) {
            }
            return null;
        }

        public static Action[] values() {
            if (valuesArray != null && valuesArray.length == valuesList.size()) {
                return valuesArray;
            }
            return valuesArray = valuesList.toArray(new Action[valuesList.size()]);
        }
    }

    public static class Identity implements TrackerBase.Identity, Cloneable {
        private static List<Identity> valuesList = new LinkedList<>();
        private static Identity[] valuesArray;

        // Action
        public static final Identity CHECK_IN_ACTION_BUTTON = new Identity("Check In Action", ElementType.BUTTON, true);
        public static final Identity CHECK_IN_AGAIN_ACTION_BUTTON = new Identity("Check In Again Action", ElementType.BUTTON, true);
        public static final Identity CHECK_OUT_ACTION_BUTTON = new Identity("Check Out Action", ElementType.BUTTON, true);
        public static final Identity VIEW_COUNTER_OFFER_ACTION_BUTTON = new Identity("View Counter Offer Action", ElementType.BUTTON, true);
        public static final Identity COUNTER_OFFER_ACTION_BUTTON = new Identity("Counter Offer Action", ElementType.BUTTON, true);
        public static final Identity CLOSING_NOTE_ACTION_BUTTON = new Identity("Closing Note Action", ElementType.BUTTON, true);
        public static final Identity CONFIRM_ACTION_BUTTON = new Identity("Confirm Action", ElementType.BUTTON, true);
        public static final Identity ETA_ACTION_BUTTON = new Identity("Eta Action", ElementType.BUTTON, true);
        public static final Identity ACKNOWLEDGE_HOLD_ACTION_BUTTON = new Identity("Acknowledge Hold Action", ElementType.BUTTON, true);
        public static final Identity MARK_COMPLETE_ACTION_BUTTON = new Identity("Mark Complete Action", ElementType.BUTTON, true);
        public static final Identity MARK_INCOMPLETE_ACTION_BUTTON = new Identity("Mark Incomplete Action", ElementType.BUTTON, true);
        public static final Identity ACCEPT_ACTION_BUTTON = new Identity("Accept Action", ElementType.BUTTON, true);
        public static final Identity REQUEST_ACTION_BUTTON = new Identity("Request Action", ElementType.BUTTON, true);
        public static final Identity NOT_INTERESTED_ACTION_BUTTON = new Identity("Not Interested Action", ElementType.BUTTON, true);
        public static final Identity READY_TO_GO_ACTION_BUTTON = new Identity("Ready To Go Action", ElementType.BUTTON, true);
        public static final Identity REPORT_PROBLEM_ACTION_BUTTON = new Identity("Report Problem Action", ElementType.BUTTON, true);
        public static final Identity WITHDRAW_ACTION_BUTTON = new Identity("Withdraw Action", ElementType.BUTTON, true);
        public static final Identity VIEW_PAYMENT_ACTION_BUTTON = new Identity("View Payment Action", ElementType.BUTTON, true);
        public static final Identity RUNNING_LATE_ACTION_BUTTON = new Identity("Running Late Action", ElementType.BUTTON, true);
        public static final Identity ON_MY_WAY_ACTION_BUTTON = new Identity("On My Way Action", ElementType.BUTTON, true);
        public static final Identity VIEW_BUNDLE_ACTION_BUTTON = new Identity("View Bundle Action", ElementType.BUTTON, true);
        public static final Identity CALL_BUYER_ACTION_BUTTON = new Identity("Call Buyer Action", ElementType.BUTTON, true);
        public static final Identity VIEW_MESSAGES_ACTION_BUTTON = new Identity("View Messages Action", ElementType.BUTTON, true);
        public static final Identity VIEW_DIRECTIONS_ACTION_BUTTON = new Identity("View Directions Action", ElementType.BUTTON, true);

        // Add
        public static final Identity TIME_LOG_ADD_BUTTON = new Identity("Time Log Add", ElementType.BUTTON, true);
        public static final Identity SIGNATURE_ADD_BUTTON = new Identity("Signature Add", ElementType.BUTTON, true);
        public static final Identity EXPENSE_ADD_BUTTON = new Identity("Expense Add", ElementType.BUTTON, true);
        public static final Identity DISCOUNT_ADD_BUTTON = new Identity("Discount Add", ElementType.BUTTON, true);
        public static final Identity CLOSING_NOTE_ADD_BUTTON = new Identity("Closing Note Add", ElementType.BUTTON, true);
        public static final Identity SHIPMENT_ADD_BUTTON = new Identity("Shipment Add", ElementType.BUTTON, true);

        // Edit
        public static final Identity TIME_LOG_EDIT_ITEM = new Identity("Time Log Edit", ElementType.LIST_ITEM, true);
        public static final Identity SIGNATURE_EDIT_ITEM = new Identity("Signature Edit", ElementType.LIST_ITEM, true);
        public static final Identity EXPENSE_EDIT_ITEM = new Identity("Expense Edit", ElementType.LIST_ITEM, true);
        public static final Identity DISCOUNT_EDIT_ITEM = new Identity("Discount Edit", ElementType.LIST_ITEM, true);
        public static final Identity CLOSING_NOTE_EDIT_ITEM = new Identity("Closing Note Edit", ElementType.LIST_ITEM, true);
        public static final Identity SHIPMENT_EDIT_ITEM = new Identity("Shipment Edit", ElementType.LIST_ITEM, true);

        // Delete
        public static final Identity TIME_LOG_DELETE_ITEM = new Identity("Time Log Delete", ElementType.LIST_ITEM, true);
        public static final Identity SIGNATURE_DELETE_ITEM = new Identity("Signature Delete", ElementType.LIST_ITEM, true);
        public static final Identity EXPENSE_DELETE_ITEM = new Identity("Expense Delete", ElementType.LIST_ITEM, true);
        public static final Identity DISCOUNT_DELETE_ITEM = new Identity("Discount Delete", ElementType.LIST_ITEM, true);
        public static final Identity SHIPMENT_DELETE_ITEM = new Identity("Shipment Delete", ElementType.LIST_ITEM, true);

        // Task
        public static final Identity CONFIRM_TASK = new Identity("Confirm Assignment Task", ElementType.LIST_ITEM, true);
        public static final Identity CLOSING_NOTE_TASK = new Identity("Closing Note Task", ElementType.LIST_ITEM, true);
        public static final Identity CHECK_IN_TASK = new Identity("Check In Task", ElementType.LIST_ITEM, true);
        public static final Identity CHECK_OUT_TASK = new Identity("Check Out Task", ElementType.LIST_ITEM, true);
        public static final Identity UPLOAD_DOCUMENT_TASK = new Identity("Upload Document Task", ElementType.LIST_ITEM, true);
        public static final Identity UPLOAD_PICTURE_TASK = new Identity("Upload Picture Task", ElementType.LIST_ITEM, true);
        public static final Identity CUSTOM_FIELD_TASK = new Identity("Custom Field Task", ElementType.LIST_ITEM, true);
        public static final Identity CALL_NUMBER_TASK = new Identity("Call Number Task", ElementType.LIST_ITEM, true);
        public static final Identity SEND_EMAIL_TASK = new Identity("Send Email Task", ElementType.LIST_ITEM, true);
        public static final Identity UNIQUE_TASK = new Identity("Unique Task", ElementType.LIST_ITEM, true);
        public static final Identity COLLECT_SIGNATURE_TASK = new Identity("Collect Signature Task", ElementType.LIST_ITEM, true);
        public static final Identity COLLECT_SHIPMENT_TASK = new Identity("Collect Shipment Task", ElementType.LIST_ITEM, true);
        public static final Identity DOWNLOAD_FILE_TASK = new Identity("Download File Task", ElementType.LIST_ITEM, true);

        // Description Modal
        public static final Identity CONFIDENTIAL_INFORMATION_BUTTON = new Identity("Confidential Information", ElementType.BUTTON, true);
        public static final Identity CUSTOMER_POLICIES_BUTTON = new Identity("Customer Policies", ElementType.BUTTON, true);
        public static final Identity STANDARD_INSTRUCTIONS_BUTTON = new Identity("Standard Instructions", ElementType.BUTTON, true);

        private String _page;
        private String _identity;
        private ElementAction _elementAction;
        private ElementType _elementType;

        private Identity(String identity, ElementType elementType, boolean internal) {
            this._page = SCREEN_WORK_ORDER_DETAILS;
            this._identity = identity;
            this._elementAction = ElementAction.CLICK;
            this._elementType = elementType;

            valuesList.add(this);
        }

        public Identity(String identity, ElementType elementType) {
            this._page = SCREEN_WORK_ORDER_DETAILS;
            this._identity = identity;
            this._elementAction = ElementAction.CLICK;
            this._elementType = elementType;
        }

        @Override
        public String page() {
            return _page;
        }

        @Override
        public String identity() {
            return _identity;
        }

        @Override
        public ElementAction elementAction() {
            return _elementAction;
        }

        @Override
        public ElementType elementType() {
            return _elementType;
        }

        public Identity clone() {
            try {
                return (Identity) super.clone();
            } catch (Exception e) {
                return null;
            }
        }

        public static Identity fromTaskType(TaskType taskType) {
            switch (taskType.getId()) {
                case 1:
                    return CONFIRM_TASK;
                case 2:
                    return CLOSING_NOTE_TASK;
                case 3:
                    return CHECK_IN_TASK;
                case 4:
                    return CHECK_OUT_TASK;
                case 5:
                    return UPLOAD_DOCUMENT_TASK;
                case 6:
                    return UPLOAD_PICTURE_TASK;
                case 7:
                    return CUSTOM_FIELD_TASK;
                case 8:
                    return CALL_NUMBER_TASK;
                case 9:
                    return SEND_EMAIL_TASK;
                case 10:
                    return UNIQUE_TASK;
                case 11:
                    return COLLECT_SIGNATURE_TASK;
                case 12:
                    return COLLECT_SHIPMENT_TASK;
                case 13:
                    return DOWNLOAD_FILE_TASK;
            }
            return null;
        }

        public static Identity[] values() {
            if (valuesArray != null && valuesArray.length == valuesList.size()) {
                return valuesArray;
            }
            return valuesArray = valuesList.toArray(new Identity[valuesList.size()]);
        }
    }

    public enum Tab {
        DETAILS(SCREEN_WORK_ORDER_DETAILS),
        MESSAGES(SCREEN_WORK_ORDER_MESSAGES),
        ATTACHMENTS(SCREEN_WORK_ORDER_ATTACHMENTS),
        NOTIFICATIONS(SCREEN_WORK_ORDER_NOTIFICATIONS);

        private String tab;

        Tab(String tab) {
            this.tab = tab;
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

    public enum ActionButton {
        CHECK_IN, CHECK_IN_AGAIN, CHECK_OUT, VIEW_COUNTER_OFFER, COUNTER_OFFER, CLOSING_NOTES,
        CONFIRM, ETA, ACCEPT_WORK, REQUEST, NOT_INTERESTED, REPORT_PROBLEM, VIEW_PAYMENT, ACKNOWLEDGE_HOLD,
        MARK_COMPlETE, MARK_INCOMPLETE, READY_TO_GO, WITHDRAW, RUNNING_LATE, ON_MY_WAY, VIEW_BUNDLE,
        CALL_BUYER, VIEW_MESSAGES, DIRECTIONS, WARNING;

        public Identity getIdentity() {
            switch (this) {
                case CHECK_IN:
                    return Identity.CHECK_IN_ACTION_BUTTON;
                case CHECK_IN_AGAIN:
                    return Identity.CHECK_IN_AGAIN_ACTION_BUTTON;
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
                case ETA:
                    return Identity.ETA_ACTION_BUTTON;
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
                case CHECK_IN:
                    return Action.CHECK_IN;
                case CHECK_IN_AGAIN:
                    return Action.CHECK_IN_AGAIN;
                case CHECK_OUT:
                    return Action.CHECK_OUT;
                case VIEW_COUNTER_OFFER:
                    return Action.VIEW_COUNTER_OFFER;
                case COUNTER_OFFER:
                    return Action.COUNTER_OFFER;
                case CLOSING_NOTES:
                    return Action.CLOSING_NOTES;
                case CONFIRM:
                    return Action.CONFIRM;
                case ETA:
                    return Action.ETA;
                case ACCEPT_WORK:
                    return Action.ACCEPT_WORK;
                case REQUEST:
                    return Action.REQUEST;
                case NOT_INTERESTED:
                    return Action.NOT_INTERESTED;
                case REPORT_PROBLEM:
                    return Action.REPORT_PROBLEM;
                case VIEW_PAYMENT:
                    return Action.VIEW_PAYMENT;
                case ACKNOWLEDGE_HOLD:
                    return Action.ACKNOWLEDGE_HOLD;
                case MARK_COMPlETE:
                    return Action.MARK_COMPLETE;
                case MARK_INCOMPLETE:
                    return Action.MARK_INCOMPLETE;
                case READY_TO_GO:
                    return Action.READY_TO_GO;
                case WITHDRAW:
                    return Action.WITHDRAW;
                case RUNNING_LATE:
                    return Action.RUNNING_LATE;
                case ON_MY_WAY:
                    return Action.ON_MY_WAY;
                case VIEW_BUNDLE:
                    return Action.VIEW_BUNDLE;
                case CALL_BUYER:
                    return Action.CALL_BUYER;
                case VIEW_MESSAGES:
                    return Action.VIEW_MESSAGES;
                case DIRECTIONS:
                    return Action.DIRECTIONS;
            }
            return null;
        }
    }

    public static void onShow(Context context, Tab tab, int workOrderId) {
        TrackerBase.show(context, tab.tab, new SpWorkOrderContext.Builder()
                .workOrderId((int) workOrderId)
                .build());
    }

    public static void onTabSwitchEvent(Context context, final Tab currentTab, final Tab newTab) {
        TrackerBase.unstructuredEvent(context, new TrackerBase.Identity() {
            @Override
            public String page() {
                return currentTab.tab;
            }

            @Override
            public String identity() {
                return newTab.tab;
            }

            @Override
            public ElementAction elementAction() {
                return ElementAction.CLICK;
            }

            @Override
            public ElementType elementType() {
                return ElementType.TAB;
            }
        });
    }

    public static void onActionButtonEvent(Context context, ActionButton actionButton, Action action, Integer workOrderId) {
        onActionButtonEvent(context, null, actionButton, action, workOrderId);
    }

    public static void onActionButtonEvent(Context context, ActionButton actionButton, Action action, Long workOrderId) {
        onActionButtonEvent(context, null, actionButton, action, workOrderId.intValue());
    }

    public static void onActionButtonEvent(Context context, String searchTitle, ActionButton actionButton, Action action, Long workOrderId) {
        onActionButtonEvent(context, searchTitle, actionButton, action, workOrderId.intValue());
    }

    public static void onActionButtonEvent(Context context, String searchTitle, ActionButton actionButton, Action action, Integer workOrderId) {
        Identity identity = actionButton.getIdentity();
        if (identity != null) {
            if (action != null && workOrderId != null) {
                onEvent(context, identity, action, workOrderId);
            } else {
                if (misc.isEmptyOrNull(searchTitle))
                    navigationEvent(context, Tab.DETAILS, identity);
                else
                    navigationEvent(context, searchTitle, identity);
            }
        }
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

    public static void onTaskEvent(Context context, TaskType taskType, Integer workOrderId) {
        Identity identity = Identity.fromTaskType(taskType);

        if (identity != null) {
            if (taskType.getId() == 10 && workOrderId != null) {
                onEvent(context, identity, Action.UNIQUE_TASK, workOrderId);
            } else {
                navigationEvent(context, Tab.DETAILS, identity);
            }
        }
    }

    public static void directionsEvent(Context context) {
        TrackerBase.unstructuredEvent(context, new TrackerBase.Identity() {
            @Override
            public String page() {
                return Tab.DETAILS.tab;
            }

            @Override
            public String identity() {
                return "Directions";
            }

            @Override
            public ElementAction elementAction() {
                return ElementAction.CLICK;
            }

            @Override
            public ElementType elementType() {
                return ElementType.LIST_ITEM;
            }
        });
    }

    private static void navigationEvent(Context context, Tab tab, Identity identity) {
        Identity hijacked = identity.clone();
        hijacked._page = tab.tab;
        TrackerBase.unstructuredEvent(context, hijacked);
    }

    private static void navigationEvent(Context context, String page, Identity identity) {
        Identity hijacked = identity.clone();
        hijacked._page = page;
        TrackerBase.unstructuredEvent(context, hijacked);
    }


    public static void onEvent(Context context, Identity identity, Action action, int workOrderId) {
        TrackerBase.event(context,
                identity,
                action,
                "workorder_id",
                workOrderId + "",
                new SpWorkOrderContext.Builder()
                        .workOrderId(workOrderId)
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
            onActionButtonEvent(context, ab, ab.getAction(), 1);
            onActionButtonEvent(context, ab, null, 1);
            for (SavedSearchParams p : list) {
                onActionButtonEvent(context, p.title, ab, ab.getAction(), 1);
                onActionButtonEvent(context, p.title, ab, null, 1);
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

        // TODO
//        for (TaskType tt : TaskType.values()) {
//            onTaskEvent(context, tt, 1L);
//        }

        directionsEvent(context);

        for (Identity identity : Identity.values()) {
            for (Action action : Action.values()) {
                onEvent(context, identity, action, 1);
            }
        }
    }
}

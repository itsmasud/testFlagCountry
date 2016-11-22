package com.fieldnation.service.data.v2.workorder;

import com.fieldnation.data.v2.SavedSearchParams;

/**
 * Created by Michael on 8/8/2016.
 */
public enum WorkOrderListType {
    CONFIRM_TOMORROW("assigned", SavedSearchParams.ListType.ASSIGNED, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.TOMORROW}),
    TODAYS_WORK("assigned", SavedSearchParams.ListType.ASSIGNED, new SavedSearchParams.Status[]{
//            SavedSearchParams.Status.IN_PROGRESS, SavedSearchParams.Status.ON_HOLD,
//            SavedSearchParams.Status.ON_HOLD_ACK, SavedSearchParams.Status.NEEDS_READY_TO_GO,
//            SavedSearchParams.Status.UNCONFIRMED, SavedSearchParams.Status.READY_TO_GO,
//            SavedSearchParams.Status.CONFIRMED, SavedSearchParams.Status.CONFIRMED_ETA,
//            SavedSearchParams.Status.CHECKED_IN, SavedSearchParams.Status.CHECKED_OUT,
            SavedSearchParams.Status.TODAY
    }),
    TOMORROWS_WORK("assigned", SavedSearchParams.ListType.ASSIGNED, new SavedSearchParams.Status[]{
//            SavedSearchParams.Status.IN_PROGRESS, SavedSearchParams.Status.ON_HOLD,
//            SavedSearchParams.Status.ON_HOLD_ACK, SavedSearchParams.Status.NEEDS_READY_TO_GO,
//            SavedSearchParams.Status.CONFIRMED, SavedSearchParams.Status.UNCONFIRMED,
//            SavedSearchParams.Status.CONFIRMED_ETA, SavedSearchParams.Status.READY_TO_GO,
//            SavedSearchParams.Status.CHECKED_IN, SavedSearchParams.Status.CHECKED_OUT,
            SavedSearchParams.Status.TOMORROW
    }),
    ASSIGNED("assigned", SavedSearchParams.ListType.ASSIGNED, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.IN_PROGRESS, SavedSearchParams.Status.ON_HOLD,
            SavedSearchParams.Status.ON_HOLD_ACK, SavedSearchParams.Status.NEEDS_READY_TO_GO,
            SavedSearchParams.Status.CONFIRMED, SavedSearchParams.Status.UNCONFIRMED,
            SavedSearchParams.Status.CONFIRMED_ETA, SavedSearchParams.Status.READY_TO_GO,
            SavedSearchParams.Status.CHECKED_IN, SavedSearchParams.Status.CHECKED_OUT,}),

    AVAILABLE("available", SavedSearchParams.ListType.AVAILABLE, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.PUBLISHED}),

    CANCELED("canceled", SavedSearchParams.ListType.ASSIGNED, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.CANCELED}),

    COMPLETED("completed", SavedSearchParams.ListType.COMPLETED, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.PENDING_REVIEW, SavedSearchParams.Status.APPROVED,
            SavedSearchParams.Status.PAID}),

    REQUESTED("requested", SavedSearchParams.ListType.AVAILABLE, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.REQUESTED, SavedSearchParams.Status.COUNTER_OFFER}),

    ROUTED("routed", SavedSearchParams.ListType.AVAILABLE, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.ROUTED}),

    ATTACHABLE("attachable", SavedSearchParams.ListType.ASSIGNED, new SavedSearchParams.Status[]{
            SavedSearchParams.Status.IN_PROGRESS, SavedSearchParams.Status.ON_HOLD_ACK,
            SavedSearchParams.Status.CONFIRMED, SavedSearchParams.Status.CONFIRMED_ETA,
            SavedSearchParams.Status.READY_TO_GO, SavedSearchParams.Status.CHECKED_IN,
            SavedSearchParams.Status.CHECKED_OUT,});


    private final String param;
    private final SavedSearchParams.Status[] statuses;
    private final SavedSearchParams.ListType type;

    WorkOrderListType(String param, SavedSearchParams.ListType type, SavedSearchParams.Status[] statuses) {
        this.param = param;
        this.statuses = statuses;
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public SavedSearchParams.Status[] getStatuses() {
        return statuses;
    }

    public SavedSearchParams.ListType getType() {
        return type;
    }
}

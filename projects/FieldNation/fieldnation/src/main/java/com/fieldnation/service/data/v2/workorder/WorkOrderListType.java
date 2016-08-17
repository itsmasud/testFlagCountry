package com.fieldnation.service.data.v2.workorder;

/**
 * Created by Michael on 8/8/2016.
 */
public enum WorkOrderListType {
    ASSIGNED("assigned", SearchParams.ListType.ASSIGNED, new SearchParams.Status[]{
            SearchParams.Status.IN_PROGRESS, SearchParams.Status.ON_HOLD,
            SearchParams.Status.ON_HOLD_ACK, SearchParams.Status.NEEDS_READY_TO_GO,
            SearchParams.Status.CONFIRMED, SearchParams.Status.UNCONFIRMED,
            SearchParams.Status.CONFIRMED_ETA, SearchParams.Status.READY_TO_GO,
            SearchParams.Status.CHECKED_IN, SearchParams.Status.CHECKED_OUT,}),

    AVAILABLE("available", SearchParams.ListType.AVAILABLE, new SearchParams.Status[]{SearchParams.Status.PUBLISHED}),

    CANCELED("canceled", SearchParams.ListType.ASSIGNED, new SearchParams.Status[]{SearchParams.Status.CANCELED}),

    COMPLETED("completed", SearchParams.ListType.COMPLETED, new SearchParams.Status[]{
            SearchParams.Status.PENDING_REVIEW, SearchParams.Status.APPROVED,
            SearchParams.Status.PAID}),

    REQUESTED("requested", SearchParams.ListType.AVAILABLE, new SearchParams.Status[]{SearchParams.Status.REQUESTED, SearchParams.Status.COUNTER_OFFER}),

    ROUTED("routed", SearchParams.ListType.AVAILABLE, new SearchParams.Status[]{SearchParams.Status.ROUTED});

    private final String param;
    private final SearchParams.Status[] statuses;
    private final SearchParams.ListType type;

    WorkOrderListType(String param, SearchParams.ListType type, SearchParams.Status[] statuses) {
        this.param = param;
        this.statuses = statuses;
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public SearchParams.Status[] getStatuses() {
        return statuses;
    }

    public SearchParams.ListType getType() {
        return type;
    }
}

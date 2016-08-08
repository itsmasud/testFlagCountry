package com.fieldnation.ui.workorder;

import com.fieldnation.service.data.v2.workorder.WorkOrderListType;

/**
 * <P>
 * An enum that provides data selection abilities. Used primarily in
 * </p>
 *
 * @author michael.carver
 */
public enum WorkorderDataSelector {
    AVAILABLE("available"),
    REQUESTED("requested"),
    ASSIGNED("assigned", true),
    COMPLETED("completed"),
    CANCELED("canceled"),
    ROUTED("available");

    private final String _call;
    private boolean _allowCache = false;

    WorkorderDataSelector(String call) {
        _call = call;
    }

    WorkorderDataSelector(String call, boolean allowCache) {
        _call = call;
        _allowCache = allowCache;
    }

    public String getCall() {
        return _call;
    }

    public boolean allowCache() {
        return _allowCache;
    }

    public boolean shouldShowGoToMarketplace() {
        return this == ASSIGNED || this == COMPLETED || this == CANCELED;
    }

    public WorkOrderListType toWorkOrderListType() {
        switch (this) {
            case AVAILABLE:
                return WorkOrderListType.AVAILABLE;
            case REQUESTED:
                return WorkOrderListType.REQUESTED;
            case ASSIGNED:
                return WorkOrderListType.ASSIGNED;
            case COMPLETED:
                return WorkOrderListType.COMPLETED;
            case CANCELED:
                return WorkOrderListType.CANCELED;
            case ROUTED:
                return WorkOrderListType.ROUTED;
        }
        return null;
    }

    public static WorkorderDataSelector fromName(String name) {
        WorkorderDataSelector[] vs = values();
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].name().equals(name)) {
                return vs[i];
            }
        }
        return null;
    }
}